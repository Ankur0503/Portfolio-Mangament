package ideas.capstone_pm.repository;

import ideas.capstone_pm.projection.AverageReturnProjection;
import ideas.capstone_pm.projection.FundReturnProjection;
import ideas.capstone_pm.entity.FundReturn;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface FundReturnRepository extends CrudRepository<FundReturn, Integer> {
    FundReturnProjection findByFundFundId(Integer fundId);

    @Query(value = "select avg(fr.fund_return1year) as average1YearReturn, avg(fr.fund_return3year) as average3YearReturn, avg(fr.fund_return_total) as averageTotalReturn from fund_return fr join fund f on fr.fund_id = f.fund_id where f.fund_type = ?1", nativeQuery = true)
    AverageReturnProjection findAverageReturnsByFundType(String fundType);
}