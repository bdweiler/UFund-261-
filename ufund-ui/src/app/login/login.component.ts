import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../user';
import { UserService } from '../user.service';
import { CurrentUserService } from '../current-user.service';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  users: User[] = [];
  arr: number[] = [];
  nextID: number = 0;
  errorMessage: string = '';

  constructor(private userService: UserService, private router: Router, public username: CurrentUserService) { }

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers(): void {
    this.userService.getUsers()
      .subscribe(users => {
        this.users = users;
        for (var user of users) {
          if (user.id > this.nextID) { this.nextID = user.id; }
        }
        this.nextID++;
      });
  }

  login(name: string): void {
    this.errorMessage = '';
    if (name.toLowerCase() == "admin") {
      this.username.username = name;
      this.router.navigate(['/needs']);
    }

    for (var user of this.users) {
      if (user.name == name) {
        this.username.username = name;
        this.router.navigate(['/needs']);
        return;
      }
    }
    this.errorMessage = "Username does not exist!"
  }

  createUser(name: string): void {
    if (name.toLowerCase() == "admin") {
      return;
    }
    for (var user of this.users) {
      if (user.name == name) {
        this.errorMessage = "Username already exists!";
        return;
      }
    }
    var obj: User = { id: this.nextID, name: name, needList: this.arr, quantity: this.arr, spent: 0 };
    this.userService.addUser(obj)
      .subscribe(user => {
        this.users.push(user);
      });
    this.username.username = name;
    this.router.navigate(['/needs']);
  }
}
