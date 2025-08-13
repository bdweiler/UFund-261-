import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { NeedsComponent } from './needs/needs.component';
import { NeedDetailComponent } from './need-detail/need-detail.component';
import { LoginComponent } from './login/login.component';
import { BasketComponent } from './basket/basket.component';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';
import { SidebarFilterComponent } from './sidebar-filter/sidebar-filter.component';


const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'detail/:id', component: NeedDetailComponent },
  { path: 'needs', component: NeedsComponent },
  { path: 'basket', component: BasketComponent },
  { path: 'leaderboard', component: LeaderboardComponent },
  { path: 'filter', component: SidebarFilterComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
