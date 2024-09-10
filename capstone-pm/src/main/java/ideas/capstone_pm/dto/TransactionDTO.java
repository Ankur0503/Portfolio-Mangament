package ideas.capstone_pm.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private int transactionId;
    private double transactionInitialInvestment;
    private String transactionDuration;
    private Date transactionDate;
    private int fundId;
    private int userId;
}
