import {Component} from '@angular/core';
import {SharedInfoService} from "./common/shared-info.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private sharedInfo: SharedInfoService) {
  }

  newsToggle(): boolean {
    // this.sharedInfo.newsToggle = !this.sharedInfo.newsToggle;
    return this.sharedInfo.newsToggle;
  }
}
