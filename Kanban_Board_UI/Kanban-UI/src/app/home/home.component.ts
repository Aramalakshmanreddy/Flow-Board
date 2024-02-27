import { Component, OnInit } from '@angular/core';
import { SessionLogService } from '../services/session-log.service';
import { Router } from '@angular/router';
import { TokenInterceptorService } from '../services/token-interceptor.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  title: string = "Kanban Management Tool";
  loginFlag: boolean = false;
  signUpFlag: boolean = false;
  isUserLoggedIn: boolean = false;

  userName: string;

  constructor(private sessionLogservice: SessionLogService, private router: Router, private tokenInterceptorService: TokenInterceptorService) {
    this.isUserLoggedIn = sessionLogservice.isLoggedIn();
   }

  ngOnInit(): void {
  }

  getUserName() {
    this.userName = this.tokenInterceptorService.getUserName();
  }

  logout() {
    localStorage.removeItem('token');
    this.sessionLogservice.logout();
    this.router.navigate(["/home"]);
    this.isUserLoggedIn = false;
  }

  logInButtonClicked() {
    this.loginFlag = !this.loginFlag;
    this.signUpFlag = false;
  }

  signUpButtonClicked() {
    this.signUpFlag = !this.signUpFlag;
    this.loginFlag = false;
  }
}
