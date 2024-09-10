package ideas.capstone_pm.fundservice;

import ideas.capstone_pm.dto.*;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.repository.FundRepository;
import ideas.capstone_pm.repository.FundReturnRepository;
import ideas.capstone_pm.service.FundService;
import ideas.capstone_pm.util.FundServiceUtils;
import ideas.capstone_pm.utils.MockUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FundServiceTest {
    @Mock
    FundRepository fundRepository;
    @Mock
    FundReturnRepository fundReturnRepository;
    @Mock
    FundServiceUtils fundServiceUtils;
    @InjectMocks
    FundService fundService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFilters() {
        List<String> mockFundAMCs = Arrays.asList("AMC1", "AMC2", "AMC3");
        List<String> mockFundTypes = Arrays.asList("Type1", "Type2");

        when(fundRepository.findAllDistinctFundAMCs()).thenReturn(mockFundAMCs);
        when(fundRepository.findAllDistinctFundTypes()).thenReturn(mockFundTypes);

        DashBoardFilters actualFilters = fundService.getAllFilters();

        assertNotNull(actualFilters);
        assertEquals(mockFundAMCs, actualFilters.getFundAMCs());
        assertEquals(mockFundTypes, actualFilters.getFundTypes());
    }

    @Test
    void testGetAllFunds() {
        List<DashBoardFundProjection> mockFundProjections = MockUtils.mockFundProjectionsList();

        when(fundRepository.findBy()).thenReturn(mockFundProjections);

        List<DashBoardFundProjection> actualFunds = fundService.getAllFunds();

        assertNotNull(actualFunds);
        assertEquals(mockFundProjections.size(), actualFunds.size());
        assertEquals(mockFundProjections.get(0).getFundId(), actualFunds.get(0).getFundId());
        assertEquals(mockFundProjections.get(0).getFundName(), actualFunds.get(0).getFundName());
        assertEquals(mockFundProjections.get(0).getFundRisk(), actualFunds.get(0).getFundRisk());
        assertEquals(mockFundProjections.get(0).getFundType(), actualFunds.get(0).getFundType());
        assertEquals(mockFundProjections.get(0).getFundRating(), actualFunds.get(0).getFundRating());
        assertEquals(mockFundProjections.get(0).getFundReturn(), actualFunds.get(0).getFundReturn());

        assertEquals(mockFundProjections.get(0).getFundReturn().getFundReturn1Year(), actualFunds.get(0).getFundReturn().getFundReturn1Year());
        assertEquals(mockFundProjections.get(0).getFundReturn().getFundReturn3Year(), actualFunds.get(0).getFundReturn().getFundReturn3Year());
        assertEquals(mockFundProjections.get(0).getFundReturn().getFundReturn5Year(), actualFunds.get(0).getFundReturn().getFundReturn5Year());

        // Verify that the repository method was called exactly once
        verify(fundRepository, times(1)).findBy();
    }

    @Test
    void testGetFundById() {
        Fund expectedFund = buildFund();
        FundDescriptionDTO expectedFundDescriptionDTO = buildFundDescriptionDTO();

        when(fundRepository.existsByFundId(1)).thenReturn(true);
        when(fundRepository.findById(1)).thenReturn(Optional.of(expectedFund));
        when(fundServiceUtils.buildFundDTO(expectedFund)).thenReturn(expectedFundDescriptionDTO);

        FundDescriptionDTO actualFundDescription = fundService.getFundById(1);

        assertNotNull(actualFundDescription);
        assertEquals(expectedFundDescriptionDTO, actualFundDescription);
    }

    @Test
    void testAddFund() {
        Fund expectedFund = buildFund();
        FundDescriptionDTO expectedFundDescriptionDTO = buildFundDescriptionDTO();

        when(fundRepository.save(expectedFund)).thenReturn(expectedFund);
        when(fundServiceUtils.buildFund(expectedFundDescriptionDTO)).thenReturn(expectedFund);

        Fund actualFund = fundService.addFund(expectedFundDescriptionDTO);
        assertNotNull(actualFund);
        assertEquals(expectedFund, actualFund);
    }

    @Test
    void testGetFundsByFilter() {
        List<String> validFundAMCs = List.of("AMC1", "AMC2");
        List<String> validFundRisks = List.of("High", "Low");
        double validFundAUM = 10000.00;
        List<String> invalidFundAMCs = List.of();
        List<String> invalidFundRisks = List.of();
        double invalidFundAUM = 0.00;

        List<DashBoardFundProjection> mockFunds = MockUtils.mockFundProjectionsList();

        setUpMockRepository(validFundAMCs, validFundRisks, validFundAUM, mockFunds);

        when(fundServiceUtils.isFundAMCsValid(validFundAMCs)).thenReturn(true);
        when(fundServiceUtils.isFundRisksValid(validFundRisks)).thenReturn(true);
        when(fundServiceUtils.isFundAUMValid(validFundAUM)).thenReturn(true);
        when(fundServiceUtils.isFundAMCsValid(invalidFundAMCs)).thenReturn(false);
        when(fundServiceUtils.isFundRisksValid(invalidFundRisks)).thenReturn(false);
        when(fundServiceUtils.isFundAUMValid(invalidFundAUM)).thenReturn(false);

        List<DashBoardFundProjection> actualFunds = fundService.getFundsByFilter(validFundAMCs, validFundRisks, validFundAUM);
        assertNotNull(actualFunds);
        assertEquals(mockFunds, actualFunds);

        actualFunds = fundService.getFundsByFilter(validFundAMCs, validFundRisks, invalidFundAUM);
        assertNotNull(actualFunds);
        assertEquals(mockFunds, actualFunds);

        actualFunds = fundService.getFundsByFilter(validFundAMCs, invalidFundRisks, validFundAUM);
        assertNotNull(actualFunds);
        assertEquals(mockFunds, actualFunds);

        actualFunds = fundService.getFundsByFilter(invalidFundAMCs, validFundRisks, validFundAUM);
        assertNotNull(actualFunds);
        assertEquals(mockFunds, actualFunds);

        actualFunds = fundService.getFundsByFilter(validFundAMCs, invalidFundRisks, invalidFundAUM);
        assertNotNull(actualFunds);
        assertEquals(mockFunds, actualFunds);

        actualFunds = fundService.getFundsByFilter(invalidFundAMCs, validFundRisks, invalidFundAUM);
        assertNotNull(actualFunds);
        assertEquals(mockFunds, actualFunds);

        actualFunds = fundService.getFundsByFilter(invalidFundAMCs, invalidFundRisks, validFundAUM);
        assertNotNull(actualFunds);
        assertEquals(mockFunds, actualFunds);
    }

    @Test
    void testCalculateFundValue() {
        FundReturnDTO mockedFundReturnDTO = MockUtils.mockFundReturnProjection(2.0, 12.5, 38.7, 75.4, 250.4, 1, "SBI BlueChip Fund", "Equity");
        FundReturnDTO.FundDTO mockedFundDTO = mockedFundReturnDTO.getFund();
        Fund mockedFund = buildFund();
        Double expectedFundValue = 1660152.3524741023;

        when(fundReturnRepository.findByFund(any(Fund.class))).thenReturn(mockedFundReturnDTO);
        Double actualFundValue = fundService.calculateFundValue(mockedFund, 100000.0, 5);
        assertNotNull(actualFundValue);
        assertEquals(expectedFundValue, actualFundValue);

        assertEquals(mockedFundReturnDTO.getFundReturn1Month(), 2.0);
        assertEquals(mockedFundReturnDTO.getFundReturn1Year(), 12.5);
        assertEquals(mockedFundReturnDTO.getFundReturn3Year(), 38.7);
        assertEquals(mockedFundReturnDTO.getFundReturn5Year(), 75.4);
        assertEquals(mockedFundReturnDTO.getFundReturnTotal(), 250.4);
        assertEquals(mockedFundReturnDTO.getFund(), mockedFundDTO);

        assertEquals(mockedFundDTO.getFundId(), 1);
        assertEquals(mockedFundDTO.getFundName(), "SBI BlueChip Fund");
        assertEquals(mockedFundDTO.getFundType(), "Equity");
    }

    private void setUpMockRepository(List<String> validFundAMCs, List<String> validFundRisks, double validFundAUM, List<DashBoardFundProjection> mockFunds) {
        when(fundRepository.findByFundAMCInAndFundRiskInAndFundAUMLessThanEqual(validFundAMCs, validFundRisks, validFundAUM)).thenReturn(mockFunds);
        when(fundRepository.findByFundAMCInAndFundRiskIn(validFundAMCs, validFundRisks)).thenReturn(mockFunds);
        when(fundRepository.findByFundAMCInAndFundAUMLessThanEqual(validFundAMCs, validFundAUM)).thenReturn(mockFunds);
        when(fundRepository.findByFundRiskInAndFundAUMLessThanEqual(validFundRisks, validFundAUM)).thenReturn(mockFunds);
        when(fundRepository.findByFundAMCIn(validFundAMCs)).thenReturn(mockFunds);
        when(fundRepository.findByFundRiskIn(validFundRisks)).thenReturn(mockFunds);
        when(fundRepository.findByFundAUMLessThanEqual(validFundAUM)).thenReturn(mockFunds);
    }

    private FundDescriptionDTO buildFundDescriptionDTO() {
        return new FundDescriptionDTO(101, "Growth Fund", "ABC Asset Management", "High", "Equity", 500.75, 120.45, "John Doe", "This is a high-growth equity fund focusing on technology stocks.", 3.5, null, null, null);
    }

    private Fund buildFund() {
        return new Fund(1, "Global Equity Fund", "XYZ Asset Management", "Medium", "Equity", 250.50, 95.75, "Alice Johnson", "A diversified global equity fund focusing on growth stocks.", 4.5, null, null, null);
    }
}
