import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {SharedInfoService} from "../../common/shared-info.service";

@Component({
  selector: 'app-chips',
  templateUrl: './chips.component.html',
  styleUrls: ['./chips.component.css']
})
export class ChipsComponent implements OnInit {

  @Input() category!: string;

  constructor(private route: ActivatedRoute, private router: Router, private sharedInfo: SharedInfoService) {
  }

  ngOnInit(): void {
  }

  categoryRemoved() {
    this.sharedInfo.chips.splice(this.sharedInfo.chips.indexOf(this.category), 1);
    this.sharedInfo.updateCategoriesInUrl(this.category)
  }
}
