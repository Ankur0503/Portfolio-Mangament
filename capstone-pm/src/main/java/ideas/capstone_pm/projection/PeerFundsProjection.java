package ideas.capstone_pm.projection;

public interface PeerFundsProjection {
    Integer getFundId();
    String getFundName();
    String getFundType();
    Double getFundAUM();
    Double getFundRating();
    FundReturnDTO getFundReturn();

    interface FundReturnDTO {
        Double getFundReturn1Year();
        Double getFundReturn3Year();
        Double getFundReturnTotal();
    }
}