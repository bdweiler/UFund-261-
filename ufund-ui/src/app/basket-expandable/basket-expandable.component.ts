import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Need } from '../need';
import { NeedService } from '../need.service';
import { User } from '../user';
import { UserService } from '../user.service';
import { CurrentUserService } from '../current-user.service';

@Component({
  selector: 'app-basket-expandable',
  templateUrl: './basket-expandable.component.html',
  styleUrl: './basket-expandable.component.css'
})
export class BasketExpandableComponent {
  windowVisible: boolean = false;
  users: User[] = [];
  userNeeds: Need[] = [];

  constructor(
    private userService: UserService,
    private router: Router,
    public currentUser: CurrentUserService,
    private needService: NeedService
  ) {}

  ngOnInit(): void {
    if (this.currentUser.username === '') {
      this.router.navigate(['/login']);
    }
  }

  // Trigger the user needs load when the window is toggled
  toggleWindowVisibility(): void {
    this.windowVisible = !this.windowVisible;

    if (this.windowVisible) {
      this.updateUserNeeds();  // Fetch user needs when the window is expanded
    }
  }

  updateUserNeeds(): void {
    this.userNeeds = [];  // Clear previous needs before loading new ones

    this.userService.searchUsers(this.currentUser.username)
      .subscribe(users => {
        this.users = users;
        console.log(this.users[0]);

        const totalNeeds = this.users[0].needList.length;

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
            });
        }
      });
  }

  basketPage(): void {
    this.router.navigate(['/basket']);
  }
}
