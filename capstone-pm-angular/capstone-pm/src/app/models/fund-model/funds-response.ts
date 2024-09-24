export class FundResponse {
    fundId: number
    fundName: string
    fundRating: number
    fundCategory: string
    fundRisk: string
    fundReturn: number
    fundNetAssetValue: number

    constructor() {
        this.fundId = 0
        this.fundName = ''
        this.fundRating = 0
        this.fundCategory = ''
        this.fundRisk = ''
        this.fundReturn = 0
        this.fundNetAssetValue = 0
    }
}