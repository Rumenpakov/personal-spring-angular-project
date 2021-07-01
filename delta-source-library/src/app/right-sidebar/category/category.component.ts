import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {SharedInfoService} from "../../common/shared-info.service";

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {

  @Input() public category!: string;

  constructor(private route: ActivatedRoute, private router: Router, public sharedInfo: SharedInfoService) {
  }

  ngOnInit(): void {
  }

  onCategorySelected() {
    if (this.sharedInfo.categoriesToArray().indexOf(this.category) > -1) {
      return;
    }
    let selectedCategoriesInUrl = this.route.snapshot.queryParams['categories'] ? this.route.snapshot.queryParams['categories'] + ',' : '';
    this.router.navigate([], {
      queryParams: {categories: selectedCategoriesInUrl + this.category},
      queryParamsHandling: "merge"
    });
  }
}
