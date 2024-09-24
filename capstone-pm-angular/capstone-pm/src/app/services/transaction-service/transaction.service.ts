import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, throwError } from 'rxjs';
import { AuthService } from '../auth-service/auth.service';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  constructor(public http: HttpClient, public authService: AuthService) { }

  retrieveTransactionByUser(userId: number): Observable<any> {
    const headers = this.authService.getRequestHeaders()
    
    let params = new HttpParams().set('userId', userId)
    return this.http.get<any>("http://localhost:8080/mutual-funds/user/investment", {params, headers}).pipe(
      map(response => {
        if(response) {
          return {data: response}
        }
        else {
          throw new Error('No Transactions Found')
        }
      }),
      catchError(error => {
        return throwError(error)
      })
    )
  }
}
