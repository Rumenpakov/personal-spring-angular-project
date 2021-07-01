import {Component, OnInit} from '@angular/core';
import {SharedInfoService} from "../common/shared-info.service";
import {ActivatedRoute} from "@angular/router";
import {BooksHttpService} from "../common/books-http.service";

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css']
})
export class BookDetailsComponent implements OnInit {

  constructor(public sharedInfo: SharedInfoService, private route: ActivatedRoute, private http: BooksHttpService) {
    let urlParam = this.route.snapshot.params['title'];
    this.http.getBook(urlParam)
      .subscribe(data => this.sharedInfo.selected = data);
  }

  ngOnInit(): void {
  }


}
