import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {SharedInfoService} from "../common/shared-info.service";
import {BookModel} from "../common/book.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrls: ['./book-card.component.css']
})
export class BookCardComponent implements OnInit {
  @Input() book!: BookModel;
  @Output() readMoreClick = new EventEmitter<{ imgUrl: string, heading: string, summary: string }>()

  constructor(private sharedInfo: SharedInfoService, private router: Router) {
  }

  ngOnInit(): void {
  }

  readMoreClicked() {
    this.sharedInfo.isBookDetailsSelected = false;
    this.router.navigate(['/books', this.book.isbn]);
  }
}
