import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { Need } from '../need';
import { NeedService } from '../need.service';
import { User } from '../user';
import { UserService } from '../user.service';
import { CurrentUserService } from '../current-user.service';

@Component({
  selector: 'app-need-detail',
  templateUrl: './need-detail.component.html',
  styleUrls: [ './need-detail.component.css' ]
})
export class NeedDetailComponent implements OnInit {
  need: Need | undefined;
  users: User[] = [];
  found: boolean = false;
  foundID: number | undefined;

  constructor(
    private route: ActivatedRoute,
    private needService: NeedService,
    private userService: UserService,
    public currentUser: CurrentUserService,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit(): void {
    if(this.currentUser.username == '') {
      this.router.navigate(['/login']);
    }
    this.getNeed();
    this.searchUser();
  }

  searchUser(): void {
    this.userService.searchUsers(this.currentUser.username)
      .subscribe(users => this.users = users);
  }

  getNeed(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.needService.getNeed(id)
      .subscribe(need => this.need = need);
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    if (this.need) {
      this.needService.updateNeed(this.need)
        .subscribe(() => this.goBack());
    }
  }

  addBasket(): void {
    if (this.users && this.need) {
      if(!this.found){
        for(let i = 0; i < this.users[0].needList.length; i++){
          if(this.users[0].needList[i] == this.need.id){
            this.found = true;
            this.foundID = i;
            this.users[0].quantity[i]++;
            console.log("finding first time")
          }
        }
        if(!this.found){
          this.users[0].needList.push(this.need.id);
          this.users[0].quantity.push(1);
          this.found = true;
          this.foundID = this.users[0].needList.length-1
          console.log("creating new");
        }
      }
      else if (this.foundID != null) {
        this.users[0].quantity[this.foundID]++;
        console.log("already found first");
      }
      this.userService.updateUser(this.users[0])
        .subscribe();
    }
  }

  getImagePath(type: string): string {
    const formattedType = type.toLowerCase().replace(/\s+/g, '-');
    console.log(`assets/${formattedType}.png`)
    return `assets/${formattedType}.png`;
  }

}