package ideas.capstone_pm.service;

import ideas.capstone_pm.dto.*;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.projection.DashBoardFundProjection;
import ideas.capstone_pm.projection.FundReturnProjection;
import ideas.capstone_pm.repository.FundRepository;
import ideas.capstone_pm.repository.FundReturnRepository;
import ideas.capstone_pm.util.FundServiceUtils;
import ideas.capstone_pm.utils.MockUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FundServiceTest {

    private static final String FUND_AMC_SBI = "SBI Mutual Fund";
    private static final String FUND_AMC_AXIS = "Axis Mutual Fund";
    private static final String FUND_AMC_HDFC = "HDFC Mutual Fund";

    private static final String FUND_RISK_HIGH = "High";
    private static final String FUND_RISK_LOW = "Low";

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
    void getAllFilters() {
        List<String> mockFundAMCs = createMockFundAMCs();
        List<String> mockFundRisks = createMockFundRisks();

        when(fundRepository.findAllDistinctFundAMCs()).thenReturn(mockFundAMCs);
        when(fundRepository.findAllDistinctFundRisks()).thenReturn(mockFundRisks);

        DashBoardFilters actualFilters = fundService.getAllFilters();

        assertNotNull(actualFilters);
        assertEquals(mockFundAMCs, actualFilters.getFundAMCs());
        assertEquals(mockFundRisks, actualFilters.getFundRisks());
    }

    @Test
    void getAllFunds() {
        List<DashBoardFundProjection> expectedFunds = MockUtils.mockFundProjectionsList();
        when(fundRepository.findBy()).thenReturn(expectedFunds);

        List<DashBoardFundProjection> actualFunds = fundService.getAllFunds();

        assertNotNull(actualFunds);
        assertEquals(expectedFunds.size(), actualFunds.size());

        for (int i = 0; i < expectedFunds.size(); i++) {
            assertFundEquals(expectedFunds.get(i), actualFunds.get(i));
        }

        verify(fundRepository, times(1)).findBy();
    }

    @Test
    void getFundById() {
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
    void addFund() {
        Fund expectedFund = buildFund();
        FundDescriptionDTO expectedFundDescriptionDTO = buildFundDescriptionDTO();

        when(fundRepository.save(expectedFund)).thenReturn(expectedFund);
        when(fundServiceUtils.buildFund(expectedFundDescriptionDTO)).thenReturn(expectedFund);

        Fund actualFund = fundService.addFund(expectedFundDescriptionDTO);
        assertNotNull(actualFund);
        assertEquals(expectedFund, actualFund);
    }

    @Test
    void getFundsByFilter() {
        List<String> validFundAMCs = createMockFundAMCs();
        List<String> validFundRisks = createMockFundRisks();
        double validFundAUM = 10_000.00;

        List<String> invalidFundAMCs = List.of();
        List<String> invalidFundRisks = List.of();
        double invalidFundAUM = 0.00;

        List<DashBoardFundProjection> mockFunds = MockUtils.mockFundProjectionsList();
        mockFundRepositoryResponses(validFundAMCs, validFundRisks, validFundAUM, mockFunds);

        setUpValidations(validFundAMCs, validFundRisks, validFundAUM);

        assertFundsByFilter(validFundAMCs, validFundRisks, validFundAUM, mockFunds);

        assertFundsByFilter(validFundAMCs, validFundRisks, invalidFundAUM, mockFunds);
        assertFundsByFilter(validFundAMCs, invalidFundRisks, validFundAUM, mockFunds);
        assertFundsByFilter(invalidFundAMCs, validFundRisks, validFundAUM, mockFunds);
        assertFundsByFilter(validFundAMCs, invalidFundRisks, invalidFundAUM, mockFunds);
        assertFundsByFilter(invalidFundAMCs, validFundRisks, invalidFundAUM, mockFunds);
        assertFundsByFilter(invalidFundAMCs, invalidFundRisks, validFundAUM, mockFunds);
    }

    @Test
    void calculateFundValue() {
        FundReturnProjection mockedFundReturnProjection = MockUtils.mockFundReturnProjection(2.0, 12.5, 38.7, 75.4, 250.4, 1, "SBI BlueChip Fund", "Equity");
        Fund mockedFund = buildFund();
        Double expectedFundValue = 1660152.3524741023;

        when(fundReturnRepository.findByFundFundId(any(Integer.class))).thenReturn(mockedFundReturnProjection);

        Double actualFundValue = fundService.calculateFundValue(1, 100000.0, 5);

        assertNotNull(actualFundValue);
        assertEquals(expectedFundValue, actualFundValue);

        assertFundReturnDTO(mockedFundReturnProjection);
    }

    private void mockFundRepositoryResponses(List<String> validFundAMCs, List<String> validFundRisks, double validFundAUM, List<DashBoardFundProjection> mockFunds) {
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

    private List<String> createMockFundAMCs() {
        return Arrays.asList(FUND_AMC_HDFC, FUND_AMC_AXIS, FUND_AMC_SBI);
    }

    private List<String> createMockFundRisks() {
        return Arrays.asList(FUND_RISK_HIGH, FUND_RISK_LOW);
    }

    private void assertFundEquals(DashBoardFundProjection expected, DashBoardFundProjection actual) {
        assertEquals(expected.getFundId(), actual.getFundId());
        assertEquals(expected.getFundName(), actual.getFundName());
        assertEquals(expected.getFundRisk(), actual.getFundRisk());
        assertEquals(expected.getFundType(), actual.getFundType());
        assertEquals(expected.getFundRating(), actual.getFundRating());
    }

    private void setUpValidations(List<String> fundAMCs, List<String> fundRisks, double fundAUM) {
        when(fundServiceUtils.isFundAMCsValid(fundAMCs)).thenReturn(true);
        when(fundServiceUtils.isFundRisksValid(fundRisks)).thenReturn(true);
        when(fundServiceUtils.isFundAUMValid(fundAUM)).thenReturn(true);
    }

    private void assertFundsByFilter(List<String> fundAMCs, List<String> risks, double aum, List<DashBoardFundProjection> expectedFunds) {
        List<DashBoardFundProjection> actualFunds = fundService.getFundsByFilter(fundAMCs, risks, aum);
        assertNotNull(actualFunds);
        assertEquals(expectedFunds, actualFunds);
    }

    private void assertFundReturnDTO(FundReturnProjection fundReturnProjection) {
        assertEquals(2.0, fundReturnProjection.getFundReturn1Month());
        assertEquals(12.5, fundReturnProjection.getFundReturn1Year());
        assertEquals(38.7, fundReturnProjection.getFundReturn3Year());
        assertEquals(75.4, fundReturnProjection.getFundReturn5Year());
        assertEquals(250.4, fundReturnProjection.getFundReturnTotal());

        FundReturnProjection.FundDTO fundDTO = fundReturnProjection.getFund();
        assertEquals(1, fundDTO.getFundId());
        assertEquals("SBI BlueChip Fund", fundDTO.getFundName());
        assertEquals("Equity", fundDTO.getFundType());
    }
}