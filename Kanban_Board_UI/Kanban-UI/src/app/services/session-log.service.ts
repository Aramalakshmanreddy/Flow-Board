import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SessionLogService {

  isUserLoggedIn: boolean = false;

  constructor() { }

  //method the change the userloginstatus to true for the successful login
  login() {
    if(localStorage.getItem('token')){
      this.isUserLoggedIn = true;
    } 
  }

  //method the change the userloginstatus to false for the successful logout
  logout() {
    if(localStorage.getItem('token')){
      this.isUserLoggedIn = true;
    }else{
      this.isUserLoggedIn = false;
    }
    
  }

  //method to return the userlogin status when called
  isLoggedIn() {
    return localStorage.getItem('token') ? true : this.isUserLoggedIn;

  }
}
