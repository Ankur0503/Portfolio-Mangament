import { PeerFundReturn } from "./peer-fund-return"

export class PeerFund {
    fundAUM: number
    fundId: number
    fundName: string
    fundType: string
    fundRating: number
    fundReturn: PeerFundReturn

    constructor() {
        this.fundAUM = 0
        this.fundId = 0
        this.fundName = ''
        this.fundRating = 0
        this.fundType = ''
        this.fundReturn = new PeerFundReturn()
    }
}