import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Need } from '../need';
import { NeedService } from '../need.service';
import { User } from '../user';
import { UserService } from '../user.service';
import { CurrentUserService } from '../current-user.service';


@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrl: './leaderboard.component.css'
})
export class LeaderboardComponent implements OnInit {
  topSpenders: User[] = [];

  constructor(
    private userService: UserService,
    private router: Router,
    public currentUser: CurrentUserService,
  ) { };

  ngOnInit(): void {
    this.loadTopSpenders();
  }

  loadTopSpenders(): void {
    this.userService.getTopSpenders().subscribe({
      next: (users: User[]) => {
        this.topSpenders = users.slice(0, 10);
      },
      error: (error) => {
        console.error('Error loading top spenders:', error);
      },
      complete: () => {
        console.log('Top spenders loaded successfully');
      }
    });
  }


  needPage(): void {
    this.router.navigate(['/needs']);
  }

  basket(): void {
    this.router.navigate(['/basket']);
  }

  signOut(): void {
    this.currentUser.username = '';
    this.router.navigate(['/login']);
  }

  leaderboard(): void {
  }
}
