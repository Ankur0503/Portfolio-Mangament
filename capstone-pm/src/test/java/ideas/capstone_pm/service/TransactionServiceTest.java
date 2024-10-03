package ideas.capstone_pm.service;

import ideas.capstone_pm.projection.FundReturnProjection;
import ideas.capstone_pm.dto.TransactionDTO;
import ideas.capstone_pm.projection.TransactionProjection;
import ideas.capstone_pm.dto.TransactionResponseDTO;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.entity.Transaction;
import ideas.capstone_pm.repository.FundReturnRepository;
import ideas.capstone_pm.repository.TransactionRepository;
import ideas.capstone_pm.util.InvestmentCalculator;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    private static final double FUND_RETURN_1_MONTH = 2.0;
    private static final double FUND_RETURN_1_YEAR = 12.5;
    private static final double FUND_RETURN_3_YEAR = 38.7;
    private static final double FUND_RETURN_5_YEAR = 75.4;
    private static final double FUND_RETURN_TOTAL = 250.4;
    private static final int FUND_ID = 1;
    private static final String FUND_NAME = "SBI BlueChip Fund";
    private static final String FUND_TYPE = "Equity";
    private static final String USER_NAME = "John Doe";
    private static final String USER_EMAIL = "johndoe@example.com";
    private static final String USER_PASSWORD = "password123";
    private static final String USER_PHONE = "1234567890";
    private static final int USER_AGE = 30;
    private static final String USER_ROLE = "USER";

    private static final int USER_ID = 1;
    private static final int TRANSACTION_ID = 1;
    private static final double TRANSACTION_AMOUNT = 5000.00;
    private static final int USER_ID_FOR_TRANSACTION = 2001;
    private static final Date TRANSACTION_DATE = new Date();


    @Mock
    TransactionRepository transactionRepository;
    @Mock
    TransactionServiceUtils transactionServiceUtils;
    @Mock
    FundReturnRepository fundReturnRepository;
    @Mock
    InvestmentCalculator investmentCalculator;
    @InjectMocks
    TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetFundByUser() {
        ApplicationUser user = createUser();
        List<TransactionProjection> mockTransactions = MockUtils.mockTransactionProjectionsList();
        FundReturnProjection mockedFundReturnProjection = createMockFundReturnDTO();

        when(transactionRepository.findByUser(user)).thenReturn(mockTransactions);
        when(fundReturnRepository.findByFundFundId(any(Integer.class))).thenReturn(mockedFundReturnProjection);

        Double expectedReturn = 1104.71;
        doReturn(expectedReturn).when(investmentCalculator)
                .calculateCurrentValue(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.any(Date.class));


        List<TransactionResponseDTO> actualTransactions = transactionService.getFundsByUser(user);

        assertNotNull(actualTransactions);
        assertFundReturnDTO(mockedFundReturnProjection);
    }

    @Test
    void shouldGetTransactionByUserAndFund() {
        ApplicationUser user = buildApplicationUser();
        Transaction expectedTransaction = buildTransaction();
        TransactionDTO expectedTransactionDTO = buildTransactionDTO();

        mockTransactionFundByUserAndFund(expectedTransaction);
        mockTransactionServiceUtilsBuildTransactionDTO(expectedTransaction, expectedTransactionDTO);

        TransactionDTO actualTransactionDTO = transactionService.getTransactionByUserAndFund(user, 1);

        assertNotNull(actualTransactionDTO);
        assertEquals(expectedTransactionDTO, actualTransactionDTO);
    }

    @Test
    void shouldAddTransaction() {
        ApplicationUser user = buildApplicationUser();
        Transaction expectedTransaction = buildTransaction();
        TransactionDTO expectedTransactionDTO = buildTransactionDTO();

        mockTransactionSave(expectedTransaction);
        mockTransactionServiceUtilsBuildTransactionDTO(expectedTransaction, expectedTransactionDTO);

        TransactionDTO actualTransactionDTO = transactionService.addTransaction(expectedTransaction);

        assertNotNull(actualTransactionDTO);
        assertEquals(expectedTransactionDTO, actualTransactionDTO);
    }

    private TransactionDTO buildTransactionDTO() {
        return new TransactionDTO(TRANSACTION_ID, TRANSACTION_AMOUNT, TRANSACTION_DATE, FUND_ID, USER_ID_FOR_TRANSACTION);
    }

    private ApplicationUser buildApplicationUser() {
        return new ApplicationUser(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD, USER_PHONE, USER_AGE, USER_ROLE, null, null);
    }

    private Transaction buildTransaction() {
        return new Transaction(TRANSACTION_ID, TRANSACTION_AMOUNT, TRANSACTION_DATE, null, null);
    }

    private ApplicationUser createUser() {
        return new ApplicationUser(1, USER_NAME, USER_EMAIL, USER_PASSWORD, USER_PHONE, USER_AGE, USER_ROLE, null, null);
    }

    private FundReturnProjection createMockFundReturnDTO() {
        return MockUtils.mockFundReturnProjection(FUND_RETURN_1_MONTH, FUND_RETURN_1_YEAR, FUND_RETURN_3_YEAR, FUND_RETURN_5_YEAR, FUND_RETURN_TOTAL, FUND_ID, FUND_NAME, FUND_TYPE);
    }

    private void mockTransactionFundByUserAndFund(Transaction expectedTransaction) {
        when(transactionRepository.findByUserAndFund(any(ApplicationUser.class), any(Fund.class))).thenReturn(expectedTransaction);
    }

    private void mockTransactionServiceUtilsBuildTransactionDTO(Transaction expectedTransaction, TransactionDTO expectedTransactionDTO) {
        when(transactionServiceUtils.buildTransactionDTO(expectedTransaction)).thenReturn(expectedTransactionDTO);
    }

    private void mockTransactionSave(Transaction expectedTransaction) {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(expectedTransaction);
    }

    private void assertFundReturnDTO(FundReturnProjection fundReturnProjection) {
        FundReturnProjection.FundDTO fundDTO = fundReturnProjection.getFund();
        assertEquals(FUND_RETURN_1_MONTH, fundReturnProjection.getFundReturn1Month());
        assertEquals(FUND_RETURN_1_YEAR, fundReturnProjection.getFundReturn1Year());
        assertEquals(FUND_RETURN_3_YEAR, fundReturnProjection.getFundReturn3Year());
        assertEquals(FUND_RETURN_5_YEAR, fundReturnProjection.getFundReturn5Year());
        assertEquals(FUND_RETURN_TOTAL, fundReturnProjection.getFundReturnTotal());
        assertEquals(FUND_ID, fundDTO.getFundId());
        assertEquals(FUND_NAME, fundDTO.getFundName());
        assertEquals(FUND_TYPE, fundDTO.getFundType());
    }
}
