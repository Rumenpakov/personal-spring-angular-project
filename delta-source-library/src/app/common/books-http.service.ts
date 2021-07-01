import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {BookModel} from "./book.model";
import {ResponseModel} from "./response.model";
import {SignupModel} from "../auth/signup/signup.model";
import {Router} from "@angular/router";

@Injectable({providedIn: 'root'})
export class BooksHttpService {

  //TODO extract common
  urlSingleBook: string = "http://localhost:8080/books/paper-book/";
  urlBooksCount: string = "http://localhost:8080/books/paper-books/count";

  fetchBooksUrl: string = 'http://localhost:8080/books/paper-book?pageSize=9';
  signUpUrl: string = 'http://localhost:8080/auth/signup';
  logInUrl: string = 'http://localhost:8080/auth/login';

  constructor(private http: HttpClient, private router: Router) {
  }

  getBook(isbn: string) {
    return this.http.get<BookModel>(this.urlSingleBook + isbn);
  }

  getBooksCount() {
    return this.http.get<number>(this.urlBooksCount);
  }

  fetchBooksAllParams(keys: string[], values: string[]) {
    let urlBuild: string = this.fetchBooksUrl;
    for (let i = 0; i < keys.length; i++) {
      urlBuild += '&' + keys[i] + '=' + values[i];
    }
    return this.http.get<ResponseModel>(urlBuild);
  }

  signup(signUpModel: SignupModel) {
    this.http.post(this.signUpUrl, signUpModel).subscribe(data => console.log(data));
  }
}
