import {Component, OnInit} from '@angular/core';
import {SharedInfoService} from "../common/shared-info.service";
import {Categories} from "../common/categories.enum";

@Component({
  selector: 'app-right-sidebar',
  templateUrl: './right-sidebar.component.html',
  styleUrls: ['./right-sidebar.component.css']
})
export class RightSidebarComponent implements OnInit {

  constructor(public sharedInfo: SharedInfoService) {
  }

  ngOnInit(): void {
  }

  categories(): string[] {
    return Object.values(Categories);
  }
}
