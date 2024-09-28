package ideas.capstone_pm.projection;

public interface FundReturnProjection {

    Double getFundReturn1Month();
    Double getFundReturn1Year();
    Double getFundReturn3Year();
    Double getFundReturn5Year();
    Double getFundReturnTotal();
    FundDTO getFund();

    interface FundDTO {
        Integer getFundId();
        String getFundName();
        String getFundType();
    }
}
