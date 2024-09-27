import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth-service/auth.service';
import { CartService } from 'src/app/services/cart-service/cart.service';
import { FundService } from 'src/app/services/fund-service/fund.service';
import { SearchService } from 'src/app/services/search-service/search.service';
import { TransactionService } from 'src/app/services/transaction-service/transaction.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  fundName: string = '';

  constructor(public authService: AuthService, private searchService: SearchService, public router: Router) { }

  onSearch() {
    this.searchService.updateFundName(this.fundName);
  }

  logoutUser() {
    localStorage.removeItem('currentUser')
    this.authService.isLoggedIn = false
    this.router.navigateByUrl("/signin")
  }
}
