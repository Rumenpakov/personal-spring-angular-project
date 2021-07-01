import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {SharedInfoService} from "./shared-info.service";
import {Router} from "@angular/router";
import {AbstractControl} from "@angular/forms";
import {SignupModel} from "../auth/signup/signup.model";
import {BooksHttpService} from "./books-http.service";

export class User {
  constructor(public status: string) {
  }
}

@Injectable({
  providedIn: "root"
})
export class AuthenticationService {

  errorPopupToggle = true;

  constructor(private httpClient: HttpClient, private sharedInfo: SharedInfoService, private router: Router, private http: BooksHttpService) {
  }

// Provide username and password for authentication, and once authentication is successful,
//store JWT token in session
  authenticate(username, password) {
    this.httpClient.post<any>('http://localhost:8080/auth/login', {
      username: username,
      password: password
    }).subscribe(userData => {
      return this.setTokenAndNavigateToHome(userData, username);
    }, error => {
      console.log(error)
      if (error.status != 200) {
        this.errorPopupToggle = true;
      }
    });
  }

  setTokenAndNavigateToHome(userData, username) {
    sessionStorage.setItem("username", username);
    let tokenStr = "Bearer " + userData.token;
    sessionStorage.setItem("token", tokenStr);
    this.router.navigate(['books']);
    return userData;
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem("username");
    return !(user === null);
  }

  logOut() {
    sessionStorage.removeItem("username");
  }

  signUp(controls: { [p: string]: AbstractControl }) {
    let signUpModel: SignupModel = {
      username: controls['username'].value,
      password: controls['password'].value,
      name: controls['name'].value,
      age: controls['age'].value,
      gender: controls['gender'].value,
      email: controls['email'].value,
      address: controls['address'].value,
      city: controls['city'].value,
      country: controls['country'].value
    }
    this.httpClient.post<{ any }>('http://localhost:8080/auth/signup', signUpModel).subscribe(userData => {
      this.setTokenAndNavigateToHome(userData, signUpModel.username);
    }, error => {
      console.log('s')
      console.log(error)
      if (error.status != 200) {
        this.errorPopupToggle = true;
      }
    });
  }
}
