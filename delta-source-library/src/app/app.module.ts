import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {BookCardComponent} from './book-card/book-card.component';
import {MainContentComponent} from './main-content/main-content.component';
import {LeftSidebarComponent} from './left-sidebar/left-sidebar.component';
import {HeaderComponent} from './header/header.component';
import {RightSidebarComponent} from './right-sidebar/right-sidebar.component';
import {BooksComponent} from './main-content/books/books.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {RouterModule, Routes} from "@angular/router";
import {BookDetailsComponent} from './book-details/book-details.component';
import {CategoryComponent} from './right-sidebar/category/category.component';
import {ChipsComponent} from './right-sidebar/chips/chips.component';
import {LoginComponent} from './auth/login/login.component';
import {ReactiveFormsModule} from "@angular/forms";
import {SignupComponent} from './auth/signup/signup.component';
import {BasicAuthHttpInterceptorService} from "./common/basic-auth-http-interceptor.service";
import {ErrorComponent} from './auth/error/error.component';

const appRoutes: Routes = [
  {path: '', redirectTo: '/books', pathMatch: 'full'},
  {
    path: 'books', component: MainContentComponent, children: [
      {path: '', component: BooksComponent},
      {path: ':title', component: BookDetailsComponent}
    ]
  },
  {path: 'login', component: LoginComponent},
  {path: 'signup', component: SignupComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    BookCardComponent,
    MainContentComponent,
    LeftSidebarComponent,
    HeaderComponent,
    RightSidebarComponent,
    BooksComponent,
    BookDetailsComponent,
    CategoryComponent,
    ChipsComponent,
    LoginComponent,
    SignupComponent,
    ErrorComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
    ReactiveFormsModule
  ],
  providers: [
    HttpClientModule,
    {
      provide: HTTP_INTERCEPTORS, useClass: BasicAuthHttpInterceptorService, multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
