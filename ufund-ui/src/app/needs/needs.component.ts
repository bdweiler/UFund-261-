import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Category, Need } from '../need';
import { NeedService } from '../need.service';
import { CurrentUserService } from '../current-user.service';
import { User } from '../user';
import { UserService } from '../user.service';
import { BasketExpandableComponent } from '../basket-expandable/basket-expandable.component';
import { BasketComponent } from '../basket/basket.component';

@Component({
  selector: 'app-needs',
  templateUrl: './needs.component.html',
  styleUrls: ['./needs.component.css']
})
export class NeedsComponent implements OnInit {
  needs: Need[] = [];
  users: User[] = [];
  needID: number[] = [];
  index: number | undefined;
  sidebarVisible: boolean = false;
  selectedCategories: string[] = [];
  selectedQuantity: { [key: number]: number } = {};
  need: Need | undefined;
  found: boolean = false;
  foundID: number | undefined;
  sortOrder: string | null = null;

  
  @ViewChild(BasketExpandableComponent) basketExpandableComponent!: BasketExpandableComponent;
  @ViewChild(BasketComponent) basketComponent!: BasketComponent;


  constructor(
    private needService: NeedService,
    private router: Router,
    public currentUser: CurrentUserService,
    private userService: UserService,
  ) { }


  ngOnInit(): void {
    if (this.currentUser.username == '') {
      this.router.navigate(['/login']);
    }
    if (this.currentUser.username != 'admin') {
      this.removeRedundant();
    }
    else {
      this.fetchAllNeeds();
    }
  }

  removeRedundant(): void {
    this.userService.searchUsers(this.currentUser.username)
      .subscribe(users => {
        this.users = users;
        this.needService.getNeeds()
          .subscribe(needs => {
            this.needs = needs;
            for (var need of this.needs) {
              this.needID.push(need.id)
            }
            console.log(this.needID);
            for (let i = this.users[0].needList.length - 1; i >= 0; i--) {
              this.index = this.needID.indexOf(this.users[0].needList[i]);
              if (this.index == -1) {
                console.log("Taking need out!");
                this.users[0].needList.splice(i, 1);
                this.users[0].quantity.splice(i, 1);
              }
            }
            this.userService.updateUser(this.users[0]).subscribe();
          });
      });
  }

  add(type: string, cost: string, quantity: string, category: string): void {
    type = type.trim();
    if (!type) { return; }

    console.log(category);

    const parsedCategory = Category[category.toUpperCase() as keyof typeof Category];
    if (!type || !parsedCategory) { return; }

    var obj: Need = { id: 0, type: type, cost: parseFloat(cost), quantity: Number(quantity), category: parsedCategory };
    this.needService.addNeed(obj)
      .subscribe(need => {
        this.needs.push(need);
      });
  }

  delete(need: Need): void {
    this.needs = this.needs.filter(h => h !== need);
    this.needService.deleteNeed(need.id).subscribe();
  }

  filterSidebar(): void {
    this.sidebarVisible = !this.sidebarVisible;
  }

  basket(): void {
    this.router.navigate(['/basket']);
  }

  signOut(): void {
    this.currentUser.username = '';
    this.router.navigate(['/login']);
  }

  categoryKeys(): string[] {
    return Object.keys(Category);
  }

  leaderboard(): void {
    this.router.navigate(['/leaderboard']);
}
  fetchAllNeeds(): void {
    this.needService.getNeeds().subscribe(needs => {
      this.needs = needs;
    });
  }

  updateCategoryFilter(category: string, isChecked: boolean): void {
    if (isChecked) {
      this.selectedCategories.push(category);
    } else {
      this.selectedCategories = this.selectedCategories.filter(c => c !== category);
    }
    this.applyFilters();
  }


  applyFilters(): void {
    this.needService.getNeeds().subscribe(needs => {
      let filteredNeeds = needs;

      // Filter by selected categories
      if (this.selectedCategories.length > 0) {
        filteredNeeds = filteredNeeds.filter(need =>
          this.selectedCategories.includes(need.category)
        );
      }

      // Apply sorting by cost
      if (this.sortOrder) {
        if (this.sortOrder === 'high-lowC') {
          filteredNeeds = filteredNeeds.sort((a, b) => b.cost - a.cost);
        } else if (this.sortOrder === 'low-highC') {
          filteredNeeds = filteredNeeds.sort((a, b) => a.cost - b.cost);
        } else if (this.sortOrder === 'high-lowQ') {
          filteredNeeds = filteredNeeds.sort((a, b) => b.quantity - a.quantity);
        } else if (this.sortOrder === 'low-highQ') {
          filteredNeeds = filteredNeeds.sort((a, b) => a.quantity - b.quantity);
        } 


      }

      this.needs = filteredNeeds;
    });
  }

  toggleSidebar(): void {
    this.sidebarVisible = !this.sidebarVisible;
  }

  getImagePath(type: string): string {
    const formattedType = type.toLowerCase().replace(/\s+/g, '-');
    console.log(`assets/${formattedType}.png`)
    return `assets/${formattedType}.png`;
  }

  increaseQuantity(id: number): void {
    this.selectedQuantity[id] = (this.selectedQuantity[id] || 1) + 1;
  }

  decreaseQuantity(id: number): void {
    if (this.selectedQuantity[id] > 1) {
      this.selectedQuantity[id]--;
    }
  }

  addAmountToBasket(need: Need, quantity: number): void {
    if (this.users && need) {
      let found = false;
      let foundID: number | undefined;

      // Check if the item already exists in the users need list
      for (let i = 0; i < this.users[0].needList.length; i++) {
        if (this.users[0].needList[i] === need.id) {
          found = true;
          foundID = i;
          this.users[0].quantity[i] += quantity; // Add the selected quantity
          console.log("Increasing quantity for existing item");
          break;
        }
      }

      // If item is not found, add it to the need list with the specified quantity
      if (!found) {
        this.users[0].needList.push(need.id);
        this.users[0].quantity.push(quantity); // Add the selected quantity
        console.log("Adding new item to basket with quantity:", quantity);
      }

      this.userService.updateUser(this.users[0]).subscribe();
    }
  }

  updateCostSort(order: string): void {
    this.sortOrder = order;
    this.applyFilters();
  }

  clearAllFilters(): void {
    this.selectedCategories = [];
    this.sortOrder = null; // Clear the sort order
    this.fetchAllNeeds();
  }
}