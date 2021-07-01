import {Component} from '@angular/core';
import {SharedInfoService} from "../common/shared-info.service";

@Component({
  selector: 'app-left-sidebar',
  templateUrl: './left-sidebar.component.html',
  styleUrls: ['./left-sidebar.component.css']
})
export class LeftSidebarComponent {

  constructor(public sharedInfo: SharedInfoService) {
  }

}
