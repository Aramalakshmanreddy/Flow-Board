import { Component } from '@angular/core';
import { SessionLogService } from '../services/session-log.service';
import { Router } from '@angular/router';
import { TokenInterceptorService } from '../services/token-interceptor.service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { NotificationComponent } from '../notification/notification.component';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  title: string = "Kanban Board";

  userName: string;

  constructor(private sessionLogservice: SessionLogService, private dialog: MatDialog ,private router: Router, private tokenInterceptorService: TokenInterceptorService) { }

  getUserName() {
    this.userName = this.tokenInterceptorService.getUserName();
  }

  logout() {
    localStorage.removeItem('token');
    this.sessionLogservice.logout();
    this.router.navigate(["/home"]);
  }


  toggle() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '400px'; 
    dialogConfig.position = { right: '0' };
    dialogConfig.disableClose=false;
    
    const modalResponse=  this.dialog.open(NotificationComponent, dialogConfig);
  }
}
