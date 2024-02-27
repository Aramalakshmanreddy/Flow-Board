import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SessionLogService } from '../services/session-log.service';
import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {

  showPassword: boolean = false;   //initialize the password visibility value as true
  showPasswordType: string = "password";   //text interpolation variable for input type; initialize value as password
  toolTipMsg: string = "Show Password";
  loginMsg: boolean = false;    //initialize the login message value as false

  loginStatus: boolean = false;

  loginForm = this.fb.group({
    email: ['', [Validators.required]],
    password: ['', Validators.required],
  });

  constructor(private fb: FormBuilder, private sessionLogService: SessionLogService,private authenticationService: AuthenticationService, private router: Router) { }

  ngOnInit() { }

  togglePassword() {
    this.showPassword = !this.showPassword;
    this.showPasswordType = this.showPassword ? "text" : "password";
    this.toolTipMsg = this.showPassword ? "Hide Password" : "Show Password";
  }

  LoginUser() {
    const userDetails = {
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    }

    this.authenticationService.loginUser(userDetails).subscribe({
      next: data => {
        console.log(data);
        if (data) {
          localStorage.setItem('token', data);
          this.sessionLogService.login();
          this.router.navigate(["/dashboard"]);
        }
      },
      error: err => {
        this.loginMsg = true;
      }
    });
  }
}
