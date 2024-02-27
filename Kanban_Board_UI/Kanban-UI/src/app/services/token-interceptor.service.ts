import { HttpInterceptor } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class TokenInterceptorService implements HttpInterceptor {

  constructor() { }


  intercept(req, next) {
    const token = localStorage.getItem('token');

    let tokenReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next.handle(tokenReq);
  }

  getUserName(): string {
    const token = localStorage.getItem('token');
    const decodedToken: { [key: string]: any } = jwtDecode(token);//(npm install jwtwebtoken )package

    const userName = decodedToken?.['UserName'];

    return userName;
  }
}

