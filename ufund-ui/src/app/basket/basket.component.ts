import { Component, OnInit } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';
import { User } from '../user';
import { UserService } from '../user.service';
import { CurrentUserService } from '../current-user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrl: './basket.component.css'
})
export class BasketComponent implements OnInit {
  users: User[] = [];
  userNeeds: Need[] = [];
  totalItems: number = 0;
  totalCost: number = 0;

  constructor(
    private userService: UserService,
    private router: Router,
    public currentUser: CurrentUserService,
    private needService: NeedService
  ) { };

  ngOnInit(): void {
    if (this.currentUser.username == '') {
      this.router.navigate(['/login']);
    }
    this.searchUser();
  }

  searchUser(): void {
    this.userService.searchUsers(this.currentUser.username)
      .subscribe(users => {
        this.users = users;
        console.log(this.users[0]);

        const totalNeeds = this.users[0].needList.length; // Total number of needs to load
        let completedRequests = 0; // Counter for completed requests

        for (let i = 0; i < totalNeeds; i++) {
          this.needService.getNeed(this.users[0].needList[i])
            .subscribe(need => {
              const obj: Need = {
                id: need.id,
                type: need.type,
                cost: need.cost,
                quantity: this.users[0].quantity[i],
                category: need.category
              };
              this.userNeeds.push(obj);

              completedRequests++; // Increment the completed requests counter

              // Check if all requests are complete
              if (completedRequests === totalNeeds) {
                this.calculateSubtotal(); // Calculate subtotal now that all needs are loaded
                console.log(this.userNeeds);
              }
            });
        }
      });
  }

  remove(need: Need): void {
    if (this.users) {
      const index = this.users[0].needList.indexOf(need.id);
      if (index > -1 && this.users[0].quantity[index] == 1) { this.delete(need); }
      else if (index > -1) {
        this.users[0].quantity[index]--;
        this.userService.updateUser(this.users[0]).subscribe();
        this.userNeeds[this.userNeeds.indexOf(need)].quantity--;
      }
      this.calculateSubtotal();
    }
  }

  delete(need: Need): void {
    if (this.users) {
      this.userNeeds = this.userNeeds.filter(h => h !== need);
      const index = this.users[0].needList.indexOf(need.id);
      if (index > -1) {
        this.users[0].needList.splice(index, 1);
        this.users[0].quantity.splice(index, 1);
      }
      this.userService.updateUser(this.users[0]).subscribe();
      this.calculateSubtotal();
    }
  }

  needPage(): void {
    this.router.navigate(['/needs']);
  }

  signOut(): void {
    this.currentUser.username = '';
    this.router.navigate(['/login']);
  }

  updateQuantity(need: Need): void {
    const index = this.users[0].needList.indexOf(need.id);
    if (index > -1) {
      // Update the user's quantity and persist it
      this.users[0].quantity[index] = need.quantity;
      this.userService.updateUser(this.users[0]).subscribe();
      this.calculateSubtotal();
    }
  }

  calculateSubtotal(): void {
    this.totalItems = this.userNeeds.reduce((sum, need) => sum + need.quantity, 0);
    this.totalCost = this.userNeeds.reduce((sum, need) => sum + need.cost * need.quantity, 0);
  }

  leaderboard(): void {
    this.router.navigate(['/leaderboard'])
  }
  validateQuantity(need: any): void {
    if (need.quantity < 1) {
      need.quantity = 1;
    }
  }

  checkOut(): void {
    for(let i = 0; i < this.userNeeds.length; i++)
    {
      this.needService.getNeed(this.userNeeds[i].id).subscribe(need => {
        if(need.quantity - this.userNeeds[i].quantity < 0) { 
            console.log("Inusfficent amount;")
            return; 
          } //WRITE ERROR MESSAGE
        need.quantity -= this.userNeeds[i].quantity;
        this.needService.updateNeed(need).subscribe(newNeed => {
          if(i == this.userNeeds.length-1){
            console.log("Tests Passed!")
            this.users[0].spent += this.totalCost;

            this.users[0].needList = [];
            this.users[0].quantity = [];
            this.userService.updateUser(this.users[0]).subscribe();

            this.userNeeds = [];
            this.calculateSubtotal();
          }
        });
      });
    }
  }

}