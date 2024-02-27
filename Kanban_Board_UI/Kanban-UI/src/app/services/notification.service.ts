import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private http:HttpClient) { }

  getNotification(){
    return this.http.get(`http://localhost:9000/api/v3//notification/all`, {observe:'response'})
  }

 
}
