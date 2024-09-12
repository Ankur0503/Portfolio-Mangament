import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs';
import { User } from 'src/app/models/user';
import { UserCredentials } from 'src/app/models/user-credentials';
import { UserResponse } from 'src/app/models/user-response';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  public userProfile: UserResponse;
  public isLoggedIn: boolean
  errorFlag: boolean

  canActivate(): boolean {
    if (this.isLoggedIn && this.userProfile.jwt)
      return true
    return false
  }

  constructor(public http: HttpClient) {
    this.userProfile = new UserResponse()
    this.errorFlag = false
    this.isLoggedIn = false
  }

  register(user: User) {
    let savedUser: any;
    this.http.post<any>("http://localhost:8080/users/register", user);
  }

  loginUser(user: UserCredentials) {

    this.isLoggedIn = false
    this.errorFlag = false

    this.http.post<any>("http://localhost:8080/auth/login", user).subscribe(response => {
      this.userProfile = response
      this.isLoggedIn = true
    },
      err => {
        this.errorFlag = true
        console.log(err)
      });

  }
}
