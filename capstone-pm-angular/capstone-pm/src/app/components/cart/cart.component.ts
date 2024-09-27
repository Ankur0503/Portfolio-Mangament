import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from 'src/app/services/auth-service/auth.service';
import { CartService } from 'src/app/services/cart-service/cart.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent {

  cartItems: any[]
  totalPayableAmount: number

  userId: number = 0
  constructor(public authService: AuthService, public cartService: CartService) {
    this.cartItems = []
    this.totalPayableAmount = 0
  }

  ngOnInit() {
    this.fetchAllCarts()
    this.authService.inCartPage = true
  }

  fetchAllCarts() {
    this.userId = this.authService.userProfile.user.userId
    this.totalPayableAmount = 0
    this.cartService.retrieveAllFundsInCart(this.userId).subscribe(response => {
      this.cartItems = response.data
      this.cartItems.forEach(cartItem => {
        this.totalPayableAmount += cartItem.plannedInvestment
      });
    })
  }

  deleteCart(cartId: number) {
    this.cartService.deleteCartById(cartId).subscribe(response => {
      this.fetchAllCarts()
    }),
    catchError(error => {
      console.log(error)
      return throwError(error)
    })
  }

  ngOnDestroy() {
    this.authService.inCartPage = false
  }
}
