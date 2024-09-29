import { Component } from '@angular/core';
import { User } from 'src/app/models/user-model/user';
import { AuthService } from 'src/app/services/auth-service/auth.service';
import { TransactionService } from 'src/app/services/transaction-service/transaction.service';

@Component({
  selector: 'app-investments',
  templateUrl: './investments.component.html',
  styleUrls: ['./investments.component.css']
})
export class InvestmentsComponent {

  currentUser: User
  userFunds: any[]

  investedValue: number = 0
  currentReturns: number = 0

  currentReturnsPercentage: number = 0

  constructor(public transactionService: TransactionService,public authService: AuthService) {
    this.currentUser = new User()
    this.userFunds = []
  }

  ngOnInit() {
    this.transactionService.retrieveTransactionByUser(this.authService.userProfile.user.userId).subscribe(response => {
      Object.assign(this.userFunds, response.data)
      this.userFunds.forEach(fund => {
        this.currentReturns += fund.currentValue
        this.investedValue += fund.transactionProjection.transactionInitialInvestment
      })
      this.currentReturnsPercentage = parseFloat((((this.currentReturns - this.investedValue )/ this.investedValue) * 100).toFixed(2));

    })
    this.authService.inInvestmentsPage = true
  }

  ngOnDestroy() {
    this.authService.inInvestmentsPage = false
  }
}
