package ideas.capstone_pm.repository;

import ideas.capstone_pm.dto.DashBoardFundProjection;
import ideas.capstone_pm.dto.PeerFundsDTO;
import ideas.capstone_pm.entity.Fund;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FundRepository extends CrudRepository<Fund, Integer> {
    List<DashBoardFundProjection> findByFundAMCIn(List<String> fundAMC);
    List<DashBoardFundProjection> findByFundRiskIn(List<String> fundRisk);
    List<DashBoardFundProjection> findByFundAUMLessThanEqual(double fundAUM);
    List<DashBoardFundProjection> findByFundAMCInAndFundAUMLessThanEqual(List<String> fundAMC, double fundAUM);
    List<DashBoardFundProjection> findByFundAMCInAndFundRiskIn(List<String> fundAMC, List<String> fundRisk);
    List<DashBoardFundProjection> findByFundRiskInAndFundAUMLessThanEqual(List<String> fundRisk, double fundAUM);
    List<DashBoardFundProjection> findByFundAMCInAndFundRiskInAndFundAUMLessThanEqual(List<String> fundAMC, List<String> fundRisk, double fundAUM);

    @Query(value = "select distinct f.fundAMC from Fund f", nativeQuery = true)
    List<String> findAllDistinctFundAMCs();
    @Query(value = "select distinct f.fund_Type from Fund f", nativeQuery = true)
    List<String> findAllDistinctFundTypes();

    List<PeerFundsDTO> findByFundTypeAndFundIdNotIn(String fundType, List<Integer> fundIds);
    List<DashBoardFundProjection> findBy();
    Boolean existsByFundId(int fundId);
}
