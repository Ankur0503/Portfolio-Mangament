import { FundInformation } from "../transaction-model/fund-info";
import { UserInformation } from "../transaction-model/user-info";

export class CartFund {
    fund: FundInformation
    user: UserInformation
    plannedInvestment: number

    constructor() {
        this.plannedInvestment = 0
        this.fund = new FundInformation()
        this.user = new UserInformation()
    }
}