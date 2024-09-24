export class FundHistory {
    fundReturnId: number
    fundReturn1Month: number
    fundReturn1Year: number
    fundReturn3Year: number
    fundReturn5Year: number
    fundReturnTotal: number

    constructor() {
        this.fundReturnId = 0
        this.fundReturn1Month = 0
        this.fundReturn1Year = 0
        this.fundReturn3Year = 0
        this.fundReturn5Year = 0
        this.fundReturnTotal = 0
    }
}