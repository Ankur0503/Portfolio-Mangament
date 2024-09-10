package ideas.capstone_pm.dto;

public interface DashBoardFundProjection {
    Integer getFundId();
    String getFundName();
    String getFundRisk();
    String getFundType();
    Double getFundRating();
    DashBoardFundReturnProjection getFundReturn();

    interface DashBoardFundReturnProjection {
        Float getFundReturn1Year();
        Float getFundReturn3Year();
        Float getFundReturn5Year();
    }
}
