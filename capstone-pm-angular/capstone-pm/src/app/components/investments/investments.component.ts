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

  currentUser: User;
  userFunds: any[]

  investedValue = 755;
  totalReturns = 95;
  currentReturns = 1;

  totalReturnsPercentage = ((this.totalReturns / this.investedValue) * 100).toFixed(2);
  currentReturnsPercentage = ((this.currentReturns / this.investedValue) * 100).toFixed(2);

  constructor(public transactionService: TransactionService,public    authService: AuthService) {
    this.currentUser = new User()
    this.userFunds = []
  }

  ngOnInit() {
    this.transactionService.retrieveTransactionByUser(this.authService.userProfile.user.userId).subscribe(response => {
      Object.assign(this.userFunds, response.data)
    })
  }
}
