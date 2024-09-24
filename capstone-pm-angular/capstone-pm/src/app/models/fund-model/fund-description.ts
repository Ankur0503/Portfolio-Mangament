import { FundAverageReturn } from "./fund-average-return"
import { FundHistory } from "./fund-history"
import { PeerFund } from "./peer-fund"

export class FundDescription {
    averageReturnDTO: FundAverageReturn
    fundAMC: string
    fundAUM: number
    fundDescription: string
    fundHistory: FundHistory
    fundId: number
    fundManager: string
    fundNAV: number
    fundName: string
    fundRating: number
    fundRisk: string
    fundType: string
    peerFunds: PeerFund[]

    constructor() {
        this.averageReturnDTO = new FundAverageReturn
        this.fundAMC = ''
        this.fundAUM = 0
        this.fundDescription = ''
        this.fundHistory = new FundHistory
        this.fundId = 0
        this.fundManager = ''
        this.fundNAV = 0
        this.fundName = ''
        this.fundRating = 0
        this.fundRisk = ''
        this.fundType = ''
        this.peerFunds = []
    }
}