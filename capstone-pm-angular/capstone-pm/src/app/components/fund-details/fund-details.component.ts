import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { CartFund } from 'src/app/models/fund-model/cart-fund';
import { FundDescription } from 'src/app/models/fund-model/fund-description';
import { FundInformation } from 'src/app/models/transaction-model/fund-info';
import { UserFunds } from 'src/app/models/transaction-model/user-funds';
import { UserInformation } from 'src/app/models/transaction-model/user-info';
import { AuthService } from 'src/app/services/auth-service/auth.service';
import { CartService } from 'src/app/services/cart-service/cart.service';
import { FundService } from 'src/app/services/fund-service/fund.service';
import { Chart, LineController, LinearScale, CategoryScale, Title, Tooltip, Legend, Filler, PointElement, LineElement } from 'chart.js';

@Component({
  selector: 'app-fund-details',
  templateUrl: './fund-details.component.html',
  styleUrls: ['./fund-details.component.css']
})
export class FundDetailsComponent {

  fundId: number;
  fundDescription: FundDescription
  initialAmount: number
  timePeriod: number
  currentReturn = new BehaviorSubject<number>(0)
  increasePercentage: number
  investmentAmount: number
  chart: Chart | undefined
  errorFlag: boolean = false

  fundHistoryData = {
    fundReturn1Month: 2,
    fundReturn1Year: 12.5,
    fundReturn3Year: 38.7,
    fundReturn5Year: 75.4,
    fundReturnId: 1,
    fundReturnTotal: 250.4
  };

  constructor(public fundService: FundService, public router: Router, public route: ActivatedRoute, public authService: AuthService, public cartService: CartService) {
    this.fundId = 0
    this.fundDescription = new FundDescription()
    this.initialAmount = 0
    this.timePeriod = 0
    this.increasePercentage = 0
    this.investmentAmount = 0
  }

  ngOnInit() {
    this.fetchFundById()
    this.authService.inFundDetailsPage = true
  }

  private createChart() {
    Chart.register(CategoryScale, LineController, LinearScale, Title, Tooltip, Legend, Filler, PointElement, LineElement);
    const ctx = document.getElementById('fundChart') as HTMLCanvasElement;

    this.chart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: ['1 Month', '1 Year', '3 Years', '5 Years'],
        datasets: [{
          label: 'Fund Returns (%)',
          data: [
            this.fundHistoryData.fundReturn1Month,
            this.fundHistoryData.fundReturn1Year,
            this.fundHistoryData.fundReturn3Year,
            this.fundHistoryData.fundReturn5Year
          ],
          borderColor: '#4CAF50',
          borderWidth: 2,
        }]
      },
      options: {
        responsive: true,
        scales: {
          y: {
            beginAtZero: true,
            title: {
              display: true,
              text: 'Returns (%)'
            }
          },
          x: {
            title: {
              display: true,
              text: 'Time Period'
            }
          }
        },
        plugins: {
          legend: {
            display: true,
            position: 'top',
          },
        }
      }
    });
  }

  fetchFundById() {
    this.route.queryParamMap.subscribe(params => {
      const fundId = params.get('fundId')
      if(fundId) {
        this.fundId = +fundId
        this.fundService.retrieveFundById(this.fundId).subscribe(response => {
          Object.assign(this.fundDescription, response.data)
          Object.assign(this.fundHistoryData, response.data.fundHistory)
          this.createChart()
        })
      }
    })
  }

  onSubmit(form: any): void {
    if(form.valid) {
      this.fundService.calculateFundReturn(this.fundId, this.initialAmount, this.timePeriod).subscribe(response => {
        this.currentReturn.next(response.data)
        this.increasePercentage = (((response.data - this.initialAmount) / this.initialAmount) * 100)
      })
    }
  }

  onPeerFundClick(peerFund: any) {
    this.router.navigate(['/funds', peerFund.fundName], {queryParams: {fundId: peerFund.fundId}})
    this.fetchFundById()
  }

  onInvest() {
    if(this.validateAmount(this.investmentAmount)) {
      let user: UserInformation = {
        userName: '',
        userId: this.authService.userProfile.user.userId
      }
  
      let fund: FundInformation = {
        fundId: this.fundId,
        fundName: '',
        fundType: ''
      }
  
      let transaction: UserFunds = {
        transactionInitialInvestment: this.investmentAmount,
        user: user,
        fund: fund
      }
  
      this.fundService.initiateTransaction(transaction).subscribe(response => {
        this.hideAlertAfterDelay()
      })
      this.errorFlag = false
    } else {
      this.errorFlag = true
    }
  }

  onAddToCart() {
    if(this.validateAmount(this.investmentAmount)) {
      let user: UserInformation = {
        userName: '',
        userId: this.authService.userProfile.user.userId
      }
  
      let fund: FundInformation = {
        fundId: this.fundId,
        fundName: '',
        fundType: ''
      }
  
      let cartFund: CartFund = {
        fund: fund,
        user: user,
        plannedInvestment: this.investmentAmount
      }
      
      this.cartService.addFundToUserCart(cartFund).subscribe(response => {
        this.hideAlertAfterDelay()
      })
      this.errorFlag = false
    } else {
      this.errorFlag = true
    }
  }

  validateAmount(amount: number) {
    return amount >= 1000
  }

  private hideAlertAfterDelay(): void {
    setTimeout(() => {
      this.fundService.investmentSuccessfull = false
      this.cartService.isItemAddedToCart = false
    }, 2000); 
  }

  ngOnDestroy() {
    this.authService.inFundDetailsPage = false
  }
}
