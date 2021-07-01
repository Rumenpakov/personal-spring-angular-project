import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../../common/authentication.service";

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {

  constructor(private authService: AuthenticationService) {
  }

  ngOnInit(): void {
    this.authService.errorPopupToggle = false;
  }

  isErrorPopUpOn() {
    return this.authService.errorPopupToggle;
  }


  removePopUp() {
    this.authService.errorPopupToggle = false;
  }
}
