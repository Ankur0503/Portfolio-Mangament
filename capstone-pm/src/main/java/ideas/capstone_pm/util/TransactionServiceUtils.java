package ideas.capstone_pm.util;

import ideas.capstone_pm.dto.TransactionDTO;
import ideas.capstone_pm.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionServiceUtils {
    public TransactionDTO buildTransactionDTO(Transaction transaction) {
        return new TransactionDTO(transaction.getTransactionId(), transaction.getTransactionInitialInvestment(), transaction.getTransactionDate(), transaction.getFund().getFundId(), transaction.getUser().getUserId());
    }
}
