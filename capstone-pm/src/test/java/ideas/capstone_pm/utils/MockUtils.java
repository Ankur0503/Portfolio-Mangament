package ideas.capstone_pm.utils;

import ideas.capstone_pm.dto.DashBoardFundProjection;
import ideas.capstone_pm.dto.FundReturnDTO;
import ideas.capstone_pm.dto.TransactionProjection;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MockUtils {

    public static DashBoardFundProjection mockFundProjection(int fundId, String fundName, String fundRisk, String fundType, double fundRating, float return1Year, float return3Year, float return5Year) {
        DashBoardFundProjection fundProjection = Mockito.mock(DashBoardFundProjection.class);
        DashBoardFundProjection.DashBoardFundReturnProjection returnProjection = Mockito.mock(DashBoardFundProjection.DashBoardFundReturnProjection.class);

        Mockito.when(returnProjection.getFundReturn1Year()).thenReturn(return1Year);
        Mockito.when(returnProjection.getFundReturn3Year()).thenReturn(return3Year);
        Mockito.when(returnProjection.getFundReturn5Year()).thenReturn(return5Year);

        Mockito.when(fundProjection.getFundId()).thenReturn(fundId);
        Mockito.when(fundProjection.getFundName()).thenReturn(fundName);
        Mockito.when(fundProjection.getFundRisk()).thenReturn(fundRisk);
        Mockito.when(fundProjection.getFundType()).thenReturn(fundType);
        Mockito.when(fundProjection.getFundRating()).thenReturn(fundRating);
        Mockito.when(fundProjection.getFundReturn()).thenReturn(returnProjection);

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

        Mockito.when(userDTOProjection.getUserId()).thenReturn(userId);
        Mockito.when(userDTOProjection.getUserName()).thenReturn(userName);

        Mockito.when(fundDTOProjection.getFundId()).thenReturn(fundId);
        Mockito.when(fundDTOProjection.getFundName()).thenReturn(fundName);
        Mockito.when(fundDTOProjection.getFundType()).thenReturn(fundType);

        Mockito.when(transactionProjection.getUser()).thenReturn(userDTOProjection);
        Mockito.when(transactionProjection.getFund()).thenReturn(fundDTOProjection);

        return transactionProjection;
    }

    public static List<TransactionProjection> mockTransactionProjectionsList() {
        List<TransactionProjection> transactionProjections = new ArrayList<>();

        TransactionProjection transactionProjection = mockTransactionProjection(1, "John Doe", 1, "SBI BlueChip Mutual Fund", "Equity");
        TransactionProjection.UserDTO userDTOProjection = transactionProjection.getUser();
        TransactionProjection.FundDTO fundDTOProjection = transactionProjection.getFund();

        assertEquals(transactionProjection.getUser(), userDTOProjection);
        assertEquals(transactionProjection.getFund(), fundDTOProjection);

        assertEquals(userDTOProjection.getUserId(), 1);
        assertEquals(userDTOProjection.getUserName(), "John Doe");

        assertEquals(fundDTOProjection.getFundId(), 1);
        assertEquals(fundDTOProjection.getFundName(), "SBI BlueChip Mutual Fund");
        assertEquals(fundDTOProjection.getFundType(), "Equity");

        transactionProjections.add(transactionProjection);
        return transactionProjections;
    }

    public static FundReturnDTO mockFundReturnProjection(Double fundReturn1Month, Double fundReturn1Year, Double fundReturn3Year, Double fundReturn5Year, Double fundReturnTotal, Integer fundId, String fundName, String fundType) {
        FundReturnDTO fundReturnDTO = Mockito.mock(FundReturnDTO.class);
        FundReturnDTO.FundDTO fundDTO = Mockito.mock(FundReturnDTO.FundDTO.class);

        Mockito.when(fundDTO.getFundId()).thenReturn(fundId);
        Mockito.when(fundDTO.getFundName()).thenReturn(fundName);
        Mockito.when(fundDTO.getFundType()).thenReturn(fundType);

        Mockito.when(fundReturnDTO.getFundReturn1Month()).thenReturn(fundReturn1Month);
        Mockito.when(fundReturnDTO.getFundReturn1Year()).thenReturn(fundReturn1Year);
        Mockito.when(fundReturnDTO.getFundReturn3Year()).thenReturn(fundReturn3Year);
        Mockito.when(fundReturnDTO.getFundReturn5Year()).thenReturn(fundReturn5Year);
        Mockito.when(fundReturnDTO.getFundReturnTotal()).thenReturn(fundReturnTotal);
        Mockito.when(fundReturnDTO.getFund()).thenReturn(fundDTO);

        return fundReturnDTO;
    }
}
