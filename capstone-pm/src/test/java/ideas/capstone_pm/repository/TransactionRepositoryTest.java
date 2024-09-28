package ideas.capstone_pm.repository;

import ideas.capstone_pm.projection.TransactionProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.entity.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void shouldFindTransactionsByUser() {
        ApplicationUser user = new ApplicationUser();
        user.setUserId(452);
        List<TransactionProjection> transactions = transactionRepository.findByUser(user);

        assertNotNull(transactions);
    }

    @Test
    void shouldFindTransactionByUserAndByFund() {
        ApplicationUser user = new ApplicationUser();
        user.setUserId(352);
        Fund fund = new Fund();
        fund.setFundId(2);
        Transaction transaction = transactionRepository.findByUserAndFund(user, fund);

        assertNotNull(transaction);
    }
}
