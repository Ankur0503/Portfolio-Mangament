package ideas.capstone_pm.dto;

import java.util.Date;

public interface TransactionProjection {

    Double getTransactionInitialInvestment();
    Date getTransactionDate();
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
