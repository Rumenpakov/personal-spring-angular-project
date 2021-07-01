import {Injectable} from '@angular/core';
import {BooksHttpService} from "./books-http.service";
import {BookModel} from "./book.model";
import {ActivatedRoute, Router} from "@angular/router";
import {SearchCriteriaInterface} from "./search-criteria.interface";
import {AbstractControl} from "@angular/forms";
import {SignupModel} from "../auth/signup/signup.model";

@Injectable({providedIn: 'root'})
export class SharedInfoService {
  isBookDetailsSelected = false;
  fetchedBooks!: BookModel[];
  isLastPage!: boolean;
  isFirstPage!: boolean;
  currentPage!: number;
  title = 'delta-source-library';
  chips!: string[];
  selected!: BookModel;
  newsToggle: boolean = false;

  constructor(public http: BooksHttpService, private router: Router, public route: ActivatedRoute) {
  }

  fetchBooksAllParams(keys: string[], values: string[]) {
    this.http.fetchBooksAllParams(keys, values)
      .subscribe((data) => {
        this.fetchedBooks = data.content;
        this.isLastPage = data.last;
        this.isFirstPage = data.first;
        this.currentPage = data.pageable.pageNumber;
      });
  }

  readMoreClicked(book: BookModel) {
    this.selected = book;
  }

  pageForward() {
    if (!this.isLastPage) {
      this.router.navigate([], {queryParams: {page: ++this.currentPage}, queryParamsHandling: "merge"});
    }
  }

  pageBackward() {
    if (!this.isFirstPage) {
      this.router.navigate([], {queryParams: {page: --this.currentPage}, queryParamsHandling: "merge"})
    }
  }

  loadPage(queryParams: SearchCriteriaInterface) {
    if (queryParams.page) {
      let keys: string[] = Object.keys(this.route.snapshot.queryParams);
      let values: string[] = Object.values(this.route.snapshot.queryParams);
      console.log(keys);
      console.log(values);
      this.fetchBooksAllParams(keys, values);
    }
  }

  categoriesToArray(): string[] {
    if (this.route.snapshot.queryParams['categories']) {
      return this.route.snapshot.queryParams['categories'].split(',', 255);
    }
    return [];
  }

  updateChips() {
    console.log(this.categoriesToArray())
    this.chips = this.categoriesToArray();
  }

  updateCategoriesInUrl(category: string) {
    let updatedCategories: string = '';
    this.categoriesToArray().forEach(element => {
      if (element != category) {
        updatedCategories += element + ',';
      }
    });
    updatedCategories = updatedCategories.substr(0, updatedCategories.length - 1);
    console.log(updatedCategories)
    if (updatedCategories.length < 1) {
      this.router.navigate([], {queryParams: {categories: null}, queryParamsHandling: 'merge'});
      return;
    }
    this.router.navigate([], {queryParams: {categories: updatedCategories}, queryParamsHandling: 'merge'})
  }

  signup(controls: { [p: string]: AbstractControl }) {
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
    console.log(signUpModel)
    this.http.signup(signUpModel);
  }

  signUpSuccessful() {
    this.router.navigate(['books'], {queryParamsHandling: 'preserve'});
  }
}
