import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, map, Observable, tap, throwError } from 'rxjs';
import { User } from 'src/app/models/user-model/user';
import { UserCredentials } from 'src/app/models/user-model/user-credentials';
import { UserResponse } from 'src/app/models/user-model/user-response';

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
    this.router.navigate(["/signin"])
    return false
  }

  constructor(public http: HttpClient, public router: Router) {
    this.userProfile = new UserResponse()
    this.errorFlag = false
    this.isLoggedIn = false
  }

  register(user: User): Observable<any> {
    let savedUser: any;
    return this.http.post<any>("http://localhost:8080/users/register", user).pipe(
      map(response => {
        return {data: response}
      })
    );
  }

  loginUser(user: UserCredentials): Observable<any> {

    this.isLoggedIn = false
    this.errorFlag = false

    return this.http.post<any>("http://localhost:8080/auth/login", user).pipe(
      map(response => {
        if(response) {
          Object.assign(this.userProfile, response)
          this.isLoggedIn = true
          localStorage.setItem('currentUser', JSON.stringify(response.jwt))
          return {success: true, data: response}
        } else {
          throw new Error('Login Failed')
        }
      }),
      catchError(error => {
        this.errorFlag = true
        return throwError(error)
      })
    );
  }

  updateUser(updatedProfile: any): Observable<any> {
    const headers = this.getRequestHeaders()
    return this.http.put<any>("http://localhost:8080/users", updatedProfile, {headers}).pipe(
      map(response => {
        return {data: response}
      }),
      catchError(error => {
        return throwError(error)
      })
    )
  }

  getLoggedInUser(): Observable<any> {
    const headers = this.getRequestHeaders()
    return this.http.get<any>("http://localhost:8080/users", {headers}).pipe(
      map(response => {
        return {data: response}
      }),
      catchError(error => {
        this.router.navigateByUrl("/signin")
        return throwError(error)
      })
    )
  }

  getRequestHeaders(): any {
    var userObj = localStorage.getItem('currentUser')
    if(userObj !== null) {
      const token = JSON.parse(userObj)
      return new HttpHeaders().set('Authorization', `Bearer ${token}`); 
    }
  }
}
