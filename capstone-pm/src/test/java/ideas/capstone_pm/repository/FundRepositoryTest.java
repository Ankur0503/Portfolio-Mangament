package ideas.capstone_pm.repository;

import ideas.capstone_pm.dto.DashBoardFundProjection;
import ideas.capstone_pm.dto.PeerFundsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class FundRepositoryTest {
    private static final String FUND_AMC_SBI = "SBI Mutual Fund";
    private static final String FUND_AMC_HDFC = "HDFC Mutual Fund";
    private static final String FUND_RISK_HIGH = "High";
    private static final String FUND_RISK_LOW = "Low";
    private static final Double FUND_AUM = 100000000.0;
    private static final String FUND_TYPE_EQUITY = "Equity";
    private static final Integer FUND_ID = 1;
    private static final List<String> FUND_AMCs = List.of(FUND_AMC_SBI, FUND_AMC_HDFC);
    private static final List<String> FUND_RISKS = List.of(FUND_RISK_LOW, FUND_RISK_HIGH);

    @Autowired
    private FundRepository fundRepository;

    @Test
    void findByFundAMCIn() {
        List<DashBoardFundProjection> funds = fundRepository.findByFundAMCIn(FUND_AMCs);

        assertNotNull(funds);
        assertThat(funds).hasSize(2);
        assertThat(funds.get(0).getFundAMC()).isIn(FUND_AMCs);
    }

    @Test
    void findByFundRiskIn() {
        List<DashBoardFundProjection> funds = fundRepository.findByFundRiskIn(FUND_RISKS);

        assertNotNull(funds);
        assertThat(funds).hasSize(9);
        assertThat(funds.get(0).getFundRisk()).isIn(FUND_RISKS);
    }

    @Test
    void findByFundAUMLessThanEqual() {
        List<DashBoardFundProjection> funds = fundRepository.findByFundAUMLessThanEqual(FUND_AUM);

        assertNotNull(funds);
        assertThat(funds).hasSize(5);
        assertThat(funds.get(0).getFundAUM()).isLessThan(FUND_AUM);
    }

    @Test
    void findByFundAMCInAndFundAUMLessThanEqual() {
        List<DashBoardFundProjection> funds = fundRepository.findByFundAMCInAndFundAUMLessThanEqual(FUND_AMCs, FUND_AUM);

        assertNotNull(funds);
        assertThat(funds).hasSize(1);
        assertThat(funds.get(0).getFundAMC()).isIn(FUND_AMCs);
        assertThat(funds.get(0).getFundAUM()).isLessThan(FUND_AUM);
    }

    @Test
    void findByFundAMCInAndFundRiskIn() {
        List<DashBoardFundProjection> funds = fundRepository.findByFundAMCInAndFundRiskIn(FUND_AMCs, FUND_RISKS);

        assertNotNull(funds);
        assertThat(funds).hasSize(1);
        assertThat(funds.get(0).getFundAMC()).isIn(FUND_AMCs);
        assertThat(funds.get(0).getFundRisk()).isIn(FUND_RISKS);
    }

    @Test
    void findByFundRiskInAndFundAUMLessThanEqual() {
        List<DashBoardFundProjection> funds = fundRepository.findByFundRiskInAndFundAUMLessThanEqual(FUND_RISKS, FUND_AUM);

        assertNotNull(funds);
        assertThat(funds).hasSize(4);
        assertThat(funds.get(0).getFundRisk()).isIn(FUND_RISKS);
        assertThat(funds.get(0).getFundAUM()).isLessThan(FUND_AUM);
    }

    @Test
    void findByFundAMCInAndFundRiskInAndFundAUMLessThanEqual() {
        List<DashBoardFundProjection> funds = fundRepository.findByFundAMCInAndFundRiskInAndFundAUMLessThanEqual(FUND_AMCs, FUND_RISKS, FUND_AUM);

        assertNotNull(funds);
        assertThat(funds).hasSize(1);
        assertThat(funds.get(0).getFundAMC()).isIn(FUND_AMCs);
        assertThat(funds.get(0).getFundRisk()).isIn(FUND_RISKS);
        assertThat(funds.get(0).getFundAUM()).isLessThan(FUND_AUM);
    }

    @Test
    void findAllDistinctFundAMCs() {
        List<String> actualFundAMCs = fundRepository.findAllDistinctFundAMCs();

        assertNotNull(actualFundAMCs);
        assertThat(actualFundAMCs).hasSize(14);
        assertThat(FUND_AMC_HDFC).isIn(actualFundAMCs);
        assertThat(FUND_AMC_SBI).isIn(actualFundAMCs);
    }

    @Test
    void findAllDistinctFundRisks() {
        List<String> actualFundRisks = fundRepository.findAllDistinctFundRisks();

        assertNotNull(actualFundRisks);
        assertThat(actualFundRisks).hasSize(3);
        assertThat(FUND_RISK_HIGH).isIn(actualFundRisks);
        assertThat(FUND_RISK_LOW).isIn(actualFundRisks);
    }

    @Test
    void findByFundTypeAndFundIdNotIn() {
        List<Integer> excludedFundIds = List.of(FUND_ID);
        List<PeerFundsDTO> result = fundRepository.findByFundTypeAndFundIdNotIn(FUND_TYPE_EQUITY, excludedFundIds);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(10);
        assertThat(result.get(0).getFundId()).isEqualTo(2);
    }

    @Test
    void existsByFundId() {
        Boolean isFundPresent = fundRepository.existsByFundId(FUND_ID);
        assertThat(isFundPresent).isEqualTo(true);
    }
}
