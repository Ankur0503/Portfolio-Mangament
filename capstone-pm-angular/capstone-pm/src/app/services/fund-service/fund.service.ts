import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, throwError } from 'rxjs';
import { AuthService } from '../auth-service/auth.service';
import { FundInformation } from 'src/app/models/transaction-model/fund-info';
import { UserFunds } from 'src/app/models/transaction-model/user-funds';
import { CartFund } from 'src/app/models/fund-model/cart-fund';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class FundService {

  investmentSuccessfull: boolean

  constructor(public http: HttpClient, public authService: AuthService, public router: Router) { 
    this.investmentSuccessfull = false
  }

  retrieveAllFilters(): Observable<any> {
    const headers = this.authService.getRequestHeaders()
    return this.http.get<any>("http://localhost:8080/filters", {headers}).pipe(
      map(response => {
        if(response) {
          return {data: response}
        } else {
          throw new Error("Filters Not Loaded")
        }
      }),
      catchError(error => {
        this.router.navigateByUrl("/signin")
        return throwError(error)
      })
    )
  }

  retrieveAllFunds(): Observable<any> {
    const headers = this.authService.getRequestHeaders()
    return this.http.get<any>("http://localhost:8080/mutual-funds", {headers}).pipe(
      map(response => {
        if(response) {
          return {data: response}
        } else {
          throw new Error('Fund Not Found')
        }
      }),
      catchError(error => {
        this.router.navigateByUrl("/signin")
        return throwError(error)
      })
    )
  }

  retrieveFundById(fundId: number): Observable<any> {
    const headers = this.authService.getRequestHeaders()
    return this.http.get<any>(`http://localhost:8080/mutual-funds/${fundId}`, {headers}).pipe(
      map(response => {
        if(response) {
          return {data: response}
        } else {
          throw new Error('Fund Not Found')
        }
      }),
      catchError((error) => {
        this.router.navigateByUrl("/signin")
        return throwError(error)
      })
    )
  }

  retrieveFundsByFilters(selectedAMCs: Set<string>, selectedRisks: Set<string>, selectedFundAUM: number): Observable<any> {
    const amcsParam = Array.from(selectedAMCs).join(',');
    const risksParam = Array.from(selectedRisks).join(',');
    let params = new HttpParams()
      .set('fundAMCs', amcsParam)
      .set('fundRisks', risksParam)
      .set('fundAUM', selectedFundAUM)

    const headers = this.authService.getRequestHeaders()

    return this.http.get<any>("http://localhost:8080/mutual-funds/filter", {headers, params}).pipe(
      map(response => {
        return {data: response}
      }),
      catchError(error => {
        this.router.navigateByUrl("/signin")
        return throwError(error)
      })
    )
  }

  calculateFundReturn(fundId: number, initialAmount: number, timePeriod: number): Observable<any> {
    const headers = this.authService.getRequestHeaders()

    let params = new HttpParams().set('fundId', fundId).set('initialInvestment', initialAmount).set('years', timePeriod)

    return this.http.get<any>("http://localhost:8080/mutual-funds/calculate/return", {params, headers}).pipe(
      map(response => {
        if(response) {
          return {data: response}
        }
        else {
          throw new Error('Internal Error')
        }
      }),
      catchError(error => {
        this.router.navigateByUrl("/signin")
        return throwError(error)
      })
    )
  }

  initiateTransaction(transaction: UserFunds): Observable<any> {
    const headers = this.authService.getRequestHeaders()
    return this.http.post<any>(`http://localhost:8080/mutual-funds/user/investment`, transaction, {headers}).pipe(
      map(response => {
        this.investmentSuccessfull = true
      }),
      catchError(error => {
        this.router.navigateByUrl("/signin")
        return throwError(error)
      })
    )
  }
}
