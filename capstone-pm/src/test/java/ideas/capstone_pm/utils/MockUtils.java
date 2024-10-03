package ideas.capstone_pm.utils;

import ideas.capstone_pm.projection.CartProjection;
import ideas.capstone_pm.projection.DashBoardFundProjection;
import ideas.capstone_pm.projection.FundReturnProjection;
import ideas.capstone_pm.projection.TransactionProjection;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class MockUtils {

    public static DashBoardFundProjection mockFundProjection(int fundId, String fundName, String fundRisk, String fundType, double fundRating, float return1Year, float return3Year, float return5Year) {
        DashBoardFundProjection fundProjection = Mockito.mock(DashBoardFundProjection.class);
        DashBoardFundProjection.DashBoardFundReturnProjection returnProjection = Mockito.mock(DashBoardFundProjection.DashBoardFundReturnProjection.class);

        when(returnProjection.getFundReturn1Year()).thenReturn(return1Year);
        when(returnProjection.getFundReturn3Year()).thenReturn(return3Year);
        when(returnProjection.getFundReturn5Year()).thenReturn(return5Year);

        when(fundProjection.getFundId()).thenReturn(fundId);
        when(fundProjection.getFundName()).thenReturn(fundName);
        when(fundProjection.getFundRisk()).thenReturn(fundRisk);
        when(fundProjection.getFundType()).thenReturn(fundType);
        when(fundProjection.getFundRating()).thenReturn(fundRating);
        when(fundProjection.getFundReturn()).thenReturn(returnProjection);

        return fundProjection;
    }

    public static List<DashBoardFundProjection> mockFundProjectionsList() {
        List<DashBoardFundProjection> dashBoardFundProjections = new ArrayList<>();
        DashBoardFundProjection dashBoardFundProjection = mockFundProjection(1, "Fund1", "Low", "Equity", 4.5, 10.5f, 25.0f, 40.0f);
        DashBoardFundProjection.DashBoardFundReturnProjection dashBoardFundReturnProjection = dashBoardFundProjection.getFundReturn();
        dashBoardFundProjections.add(dashBoardFundProjection);

        assertEquals(dashBoardFundProjection.getFundId(), 1);
        assertEquals(dashBoardFundProjection.getFundName(), "Fund1");
        assertEquals(dashBoardFundProjection.getFundRisk(), "Low");
        assertEquals(dashBoardFundProjection.getFundType(), "Equity");
        assertEquals(dashBoardFundProjection.getFundRating(), 4.5);

        assertEquals(dashBoardFundReturnProjection.getFundReturn1Year(), 10.5f);
        assertEquals(dashBoardFundReturnProjection.getFundReturn3Year(), 25.0f);
        assertEquals(dashBoardFundReturnProjection.getFundReturn5Year(), 40.0f);

        return dashBoardFundProjections;
    }

    public static TransactionProjection mockTransactionProjection(int userId, String userName, int fundId, String fundName, String fundType) {
        TransactionProjection transactionProjection = Mockito.mock(TransactionProjection.class);
        TransactionProjection.UserDTO userDTOProjection = Mockito.mock(TransactionProjection.UserDTO.class);
        TransactionProjection.FundDTO fundDTOProjection = Mockito.mock(TransactionProjection.FundDTO.class);

        when(userDTOProjection.getUserId()).thenReturn(userId);
        when(userDTOProjection.getUserName()).thenReturn(userName);

        when(fundDTOProjection.getFundId()).thenReturn(fundId);
        when(fundDTOProjection.getFundName()).thenReturn(fundName);
        when(fundDTOProjection.getFundType()).thenReturn(fundType);

        when(transactionProjection.getUser()).thenReturn(userDTOProjection);
        when(transactionProjection.getFund()).thenReturn(fundDTOProjection);
        when(transactionProjection.getTransactionInitialInvestment()).thenReturn(10000.00);
        when(transactionProjection.getTransactionDate()).thenReturn(new Date());

        return transactionProjection;
    }

    public static List<TransactionProjection> mockTransactionProjectionsList() {
        List<TransactionProjection> transactionProjections = new ArrayList<>();

        TransactionProjection transactionProjection = mockTransactionProjection(1, "John Doe", 1, "SBI BlueChip Mutual Fund", "Equity");
        TransactionProjection.UserDTO userDTOProjection = transactionProjection.getUser();
        TransactionProjection.FundDTO fundDTOProjection = transactionProjection.getFund();

        assertEquals(transactionProjection.getUser(), userDTOProjection);
        assertEquals(transactionProjection.getFund(), fundDTOProjection);
        assertEquals(transactionProjection.getTransactionInitialInvestment(), 10000.00);

        assertEquals(userDTOProjection.getUserId(), 1);
        assertEquals(userDTOProjection.getUserName(), "John Doe");

        assertEquals(fundDTOProjection.getFundId(), 1);
        assertEquals(fundDTOProjection.getFundName(), "SBI BlueChip Mutual Fund");
        assertEquals(fundDTOProjection.getFundType(), "Equity");

        transactionProjections.add(transactionProjection);
        return transactionProjections;
    }

    public static FundReturnProjection mockFundReturnProjection(Double fundReturn1Month, Double fundReturn1Year, Double fundReturn3Year, Double fundReturn5Year, Double fundReturnTotal, Integer fundId, String fundName, String fundType) {
        FundReturnProjection fundReturnProjection = Mockito.mock(FundReturnProjection.class);
        FundReturnProjection.FundDTO fundDTO = Mockito.mock(FundReturnProjection.FundDTO.class);

        when(fundDTO.getFundId()).thenReturn(fundId);
        when(fundDTO.getFundName()).thenReturn(fundName);
        when(fundDTO.getFundType()).thenReturn(fundType);

        when(fundReturnProjection.getFundReturn1Month()).thenReturn(fundReturn1Month);
        when(fundReturnProjection.getFundReturn1Year()).thenReturn(fundReturn1Year);
        when(fundReturnProjection.getFundReturn3Year()).thenReturn(fundReturn3Year);
        when(fundReturnProjection.getFundReturn5Year()).thenReturn(fundReturn5Year);
        when(fundReturnProjection.getFundReturnTotal()).thenReturn(fundReturnTotal);
        when(fundReturnProjection.getFund()).thenReturn(fundDTO);

        return fundReturnProjection;
    }

    public static CartProjection mockCartProjection(Integer cartId, Double plannedInvestment, Integer fundId, String fundName) {
        CartProjection mockedCartProjection = Mockito.mock(CartProjection.class);
        CartProjection.CartItemsProjection mockedCartItemsProjection = Mockito.mock(CartProjection.CartItemsProjection.class);

        when(mockedCartItemsProjection.getFundId()).thenReturn(fundId);
        when(mockedCartItemsProjection.getFundName()).thenReturn(fundName);

        when(mockedCartProjection.getCartId()).thenReturn(cartId);
        when(mockedCartProjection.getPlannedInvestment()).thenReturn(plannedInvestment);
        when(mockedCartProjection.getFund()).thenReturn(mockedCartItemsProjection);

        return mockedCartProjection;
    }

    public static List<CartProjection> mockCartProjectionList() {
        Integer cartId = 1;
        Double plannedInvestment = 5000.00;
        Integer fundId = 1001;
        String fundName = "SBI BlueChip Fund";

        CartProjection mockedCartProjection = mockCartProjection(cartId, plannedInvestment, fundId, fundName);

        assertNotNull(mockedCartProjection);
        assertEquals(cartId, mockedCartProjection.getCartId());
        assertEquals(plannedInvestment, mockedCartProjection.getPlannedInvestment());

        CartProjection.CartItemsProjection mockedCartItemsProjection = mockedCartProjection.getFund();
        assertNotNull(mockedCartItemsProjection);
        assertEquals(fundId, mockedCartItemsProjection.getFundId());
        assertEquals(fundName, mockedCartItemsProjection.getFundName());

        List<CartProjection> mockedCartProjectionList = new ArrayList<>();
        mockedCartProjectionList.add(mockedCartProjection);
        return mockedCartProjectionList;
    }
}
