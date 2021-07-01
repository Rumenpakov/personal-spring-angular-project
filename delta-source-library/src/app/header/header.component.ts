import {Component, OnInit} from '@angular/core';
import {SharedInfoService} from "../common/shared-info.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthenticationService} from "../common/authentication.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  profileDropDownToggle: boolean = false;


  constructor(public sharedInfo: SharedInfoService, private router: Router, private route: ActivatedRoute, private authService: AuthenticationService) {
  }

  ngOnInit(): void {
  }

  searchBarClicked(titleOrAuthor: string) {
    if (titleOrAuthor.trim().length === 0) {
      this.router.navigate([], {queryParams: {page: 0, titleOrAuthor: null}, queryParamsHandling: 'merge'});
      return;
    }
    this.router.navigate([], {queryParams: {page: 0, titleOrAuthor: titleOrAuthor}, queryParamsHandling: 'merge'})
  }

  getTitleOrAuthorQueryParam(): string {
    return this.route.snapshot.queryParams['titleOrAuthor'];
  }

  isUserLoggedIn() {
    return this.authService.isUserLoggedIn();
  }

  onLogOutClicked() {
    this.authService.logOut();
    this.router.navigate(['login'])
  }

  getLoggedUserUsername(): string {
    return sessionStorage.getItem('username')
  }
}
