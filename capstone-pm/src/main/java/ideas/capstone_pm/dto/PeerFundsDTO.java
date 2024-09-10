package ideas.capstone_pm.dto;

public interface PeerFundsDTO {
    Integer getFundId();
    String getFundName();
    String getFundType();
    Double getFundAUM();
    FundReturnDTO getFundReturn();

    interface FundReturnDTO {
        Double getFundReturn1Year();
        Double getFundReturn3Year();
        Double getFundReturnTotal();
    }
}