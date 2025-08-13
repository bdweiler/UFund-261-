import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { NeedDetailComponent } from './need-detail/need-detail.component';
import { NeedsComponent } from './needs/needs.component';
import { NeedSearchComponent } from './need-search/need-search.component';
import { LoginComponent } from './login/login.component';
import { BasketComponent } from './basket/basket.component';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';
import { SidebarFilterComponent } from './sidebar-filter/sidebar-filter.component';
import { BasketExpandableComponent } from './basket-expandable/basket-expandable.component';


@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
  ],
  declarations: [
    AppComponent,
    NeedsComponent,
    NeedDetailComponent,
    NeedSearchComponent,
    LoginComponent,
    BasketComponent,
    LeaderboardComponent,
    SidebarFilterComponent,
    BasketExpandableComponent

  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }