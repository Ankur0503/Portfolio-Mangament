import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '../auth-service/auth.service';
import { CartFund } from 'src/app/models/fund-model/cart-fund';
import { catchError, map, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  isItemAddedToCart: boolean = false
  isInvestmentSuccess: boolean = false;

  constructor(public http: HttpClient, public authService: AuthService) { }

  addFundToUserCart(cartFund: CartFund): Observable<any> {
    const headers = this.authService.getRequestHeaders()
    return this.http.post<any>("http://localhost:8080/user/cart", cartFund, {headers}).pipe(
      map(response => {
        this.isItemAddedToCart = true
      }),
      catchError(error => {
        return throwError(error)
      })
    )
  }

  retrieveAllFundsInCart(userId: number): Observable<any> {
    const headers = this.authService.getRequestHeaders()
    let params = new HttpParams().set('userId', userId)

    return this.http.get<any>("http://localhost:8080/user/cart", {params, headers}).pipe(
      map(response => {
        return {data: response}
      }),
      catchError(error => {
        return throwError(error)
      })
    )
  }

  deleteCartById(cartId: number): Observable<any> {
    const headers = this.authService.getRequestHeaders()
    let params = new HttpParams().set('cartId', cartId)
    return this.http.delete<any>("http://localhost:8080/user/cart", {headers, params}).pipe(
      map(response => {
        return {data: response}
      }),
      catchError(error => {
        return throwError(error)
      })
    )
  }

  processCartToPay(userId: number): Observable<any> {
    const headers = this.authService.getRequestHeaders()
    let user = {
      userId: userId
    }

    return this.http.post<any>("http://localhost:8080/user/cart/process", user, {headers}).pipe(
      map(response => {
        this.isInvestmentSuccess = true
      }),
      catchError(error => {
        return throwError(error)
      })
    )
  }
}
