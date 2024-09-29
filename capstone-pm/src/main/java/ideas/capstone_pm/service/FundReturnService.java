package ideas.capstone_pm.service;

import ideas.capstone_pm.entity.FundReturn;
import ideas.capstone_pm.repository.FundReturnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FundReturnService {
    @Autowired
    FundReturnRepository fundReturnRepository;

    public FundReturn addFundReturn(FundReturn fundReturn) {
        return fundReturnRepository.save(fundReturn);
    }

    public List<FundReturn> getAllFundReturns() {
        return (List<FundReturn>) fundReturnRepository.findAll();
    }
}
