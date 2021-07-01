import {AfterViewInit, Component} from '@angular/core';
import {SharedInfoService} from "../../common/shared-info.service";
import {ActivatedRoute, Params, Router} from "@angular/router";

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements AfterViewInit {

  constructor(public sharedInfo: SharedInfoService, private route: ActivatedRoute, private router: Router) {
    route.queryParams
      .subscribe((params: Params) => this.pageChanged(params));
  }

  ngAfterViewInit() {
  }

  pageBackward() {
    this.sharedInfo.pageBackward()
  }

  pageForward() {
    this.sharedInfo.pageForward();
  }

  private pageChanged(params: Params) {
    if (params['page']) {
      if (params['categories']) {
        this.sharedInfo.updateChips();
      }
      return this.sharedInfo.loadPage(this.route.snapshot.queryParams);
    }
    this.router.navigate([], {queryParams: {page: 0}, queryParamsHandling: 'merge'})
  }
}
