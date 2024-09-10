package ideas.capstone_pm.dto;

public interface TransactionProjection {

    UserDTO getUser();
    FundDTO getFund();

    interface UserDTO {
        Integer getUserId();
        String getUserName();
    }

    interface FundDTO {
        Integer getFundId();
        String getFundName();
        String getFundType();
    }
}
