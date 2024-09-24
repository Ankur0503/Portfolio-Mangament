import { FundInformation } from "./fund-info"
import { UserInformation } from "./user-info"

export class UserFunds {
    transactionInitialInvestment: number
    user: UserInformation
    fund: FundInformation

    constructor() {
        this.transactionInitialInvestment = 0
        this.user = new UserInformation()
        this.fund = new FundInformation()
    }
}