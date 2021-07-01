import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Observable} from "rxjs";
import {SharedInfoService} from "../../common/shared-info.service";
import {AuthenticationService} from "../../common/authentication.service";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  genders = ['male', 'female'];
  signupForm!: FormGroup;
  isAuthSuccessful: boolean;

  constructor(private sharedInfo: SharedInfoService, private authService: AuthenticationService) {

  }

  ngOnInit() {
    this.signupForm = new FormGroup({
      'username': new FormControl(null, [Validators.required, Validators.minLength(3), Validators.nullValidator]),
      'password': new FormControl(null, [Validators.required, Validators.minLength(3), Validators.nullValidator]),
      'name': new FormControl(null, [Validators.required, Validators.minLength(3), Validators.nullValidator]),
      'age': new FormControl(null, [Validators.required, Validators.nullValidator]),
      'gender': new FormControl(null, [Validators.required, Validators.nullValidator]),
      'email': new FormControl(null, [Validators.required, Validators.minLength(3), Validators.nullValidator]),
      'address': new FormControl(null, [Validators.required, Validators.minLength(3), Validators.nullValidator]),
      'city': new FormControl(null, [Validators.required, Validators.minLength(3), Validators.nullValidator]),
      'country': new FormControl(null, [Validators.required, Validators.minLength(3), Validators.nullValidator]),
    });
  }

  onSubmit() {
    console.log(this.signupForm);
    if (this.signupForm.valid) {
      this.authService.signUp(this.signupForm.controls);
    } else {
      this.authService.errorPopupToggle = true;
    }
  }

  usedEmails(control: FormControl): Promise<any> | Observable<any> {
    const promise = new Promise<any>((resolve, reject) => {
      setTimeout(() => {
        if (control.value === 'test@gmail.com') {
          resolve({'emailForbidden': true});
        } else {
          resolve(null);
        }
      }, 1500);
    });
    return promise;
  }
}
