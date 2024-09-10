package ideas.capstone_pm.transactionservice;

import ideas.capstone_pm.dto.TransactionDTO;
import ideas.capstone_pm.dto.TransactionProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.entity.Transaction;
import ideas.capstone_pm.repository.TransactionRepository;
import ideas.capstone_pm.service.TransactionService;
import ideas.capstone_pm.util.TransactionServiceUtils;
import ideas.capstone_pm.utils.MockUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    TransactionServiceUtils transactionServiceUtils;
    @InjectMocks
    TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFundByUser() {
        List<TransactionProjection> mockTransactions = MockUtils.mockTransactionProjectionsList();

        ApplicationUser user = new ApplicationUser(1, "John Doe", "johndoe@example.com", "password123", "1234567890", 30, "USER", null);
        when(transactionRepository.findByUser(user)).thenReturn(mockTransactions);

        List<TransactionProjection> actualTransactions = transactionService.getFundsByUser(user);
        assertNotNull(actualTransactions);
    }

    @Test
    void getTransactionByUserAndFund() {
        ApplicationUser user = buildApplicationUser();
        Transaction expectedTransaction = buildTransaction();
        TransactionDTO expectedTransactionDTO = buildTransactionDTO();

        when(transactionRepository.findByUserAndFund(any(ApplicationUser.class), any(Fund.class))).thenReturn(expectedTransaction);
        when(transactionServiceUtils.buildTransactionDTO(expectedTransaction)).thenReturn(expectedTransactionDTO);

        TransactionDTO actualTransactionDTO = transactionService.getTransactionByUserAndFund(user, 1);
        assertNotNull(actualTransactionDTO);
        assertEquals(expectedTransactionDTO, actualTransactionDTO);
    }

    @Test
    void testAddTransaction() {
        Transaction expectedTransaction = buildTransaction();
        TransactionDTO expectedTransactionDTO = buildTransactionDTO();
        when(transactionRepository.save(any(Transaction.class))).thenReturn(expectedTransaction);
        when(transactionServiceUtils.buildTransactionDTO(expectedTransaction)).thenReturn(expectedTransactionDTO);

        TransactionDTO actualTransactionDTO = transactionService.addTransaction(expectedTransaction);
        assertNotNull(actualTransactionDTO);
        assertEquals(expectedTransactionDTO, actualTransactionDTO);
    }

    private TransactionDTO buildTransactionDTO() {
        return  new TransactionDTO(1, 5000.00, "5 years", new Date(), 1001, 2001);
    }

    private ApplicationUser buildApplicationUser() {
        return new ApplicationUser(1, "John Doe", "johndoe@example.com", "password123", "1234567890", 30, "USER", null);
    }

    private Transaction buildTransaction() {
        return new Transaction(1, 5000.00, "5 years", new Date(), null, null);
    }
}
