package ideas.capstone_pm.service;

import ideas.capstone_pm.dto.*;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.entity.FundReturn;
import ideas.capstone_pm.exception.fundexceptions.FundNotFoundException;
import ideas.capstone_pm.repository.FundRepository;
import ideas.capstone_pm.repository.FundReturnRepository;
import ideas.capstone_pm.util.FundServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FundService {
    @Autowired
    FundRepository fundRepository;
    @Autowired
    FundReturnRepository fundReturnRepository;
    @Autowired
    FundServiceUtils fundServiceUtils;

    public DashBoardFilters getAllFilters() {
        return new DashBoardFilters(fundRepository.findAllDistinctFundAMCs(), fundRepository.findAllDistinctFundTypes());
    }

    public List<DashBoardFundProjection> getAllFunds() {
        return fundRepository.findBy();
    }

    public FundDescriptionDTO getFundById(int fundId) {
        if(fundRepository.existsByFundId(fundId)) {
            return fundServiceUtils.buildFundDTO(fundRepository.findById(fundId).orElseThrow(() -> new RuntimeException("Fund Not Found")));
        }
        throw new FundNotFoundException();
    }

    public Fund addFund(FundDescriptionDTO fundDescriptionDTO) {
        Fund fund = fundServiceUtils.buildFund(fundDescriptionDTO);
        return fundRepository.save(fund);
    }

    public List<DashBoardFundProjection> getFundsByFilter(List<String> fundAMCs, List<String> fundRisks, Double fundAUM) {
        if(fundServiceUtils.isFundAMCsValid(fundAMCs) && fundServiceUtils.isFundRisksValid(fundRisks) && fundServiceUtils.isFundAUMValid(fundAUM)) {
            return fundRepository.findByFundAMCInAndFundRiskInAndFundAUMLessThanEqual(fundAMCs, fundRisks, fundAUM);
        }
        else if(fundServiceUtils.isFundAMCsValid(fundAMCs) && fundServiceUtils.isFundRisksValid(fundRisks)) {
            return fundRepository.findByFundAMCInAndFundRiskIn(fundAMCs, fundRisks);
        }
        else if(fundServiceUtils.isFundAMCsValid(fundAMCs) && fundServiceUtils.isFundAUMValid(fundAUM)) {
            return fundRepository.findByFundAMCInAndFundAUMLessThanEqual(fundAMCs, fundAUM);
        }
        else if(fundServiceUtils.isFundRisksValid(fundRisks) && fundServiceUtils.isFundAUMValid(fundAUM)) {
            return fundRepository.findByFundRiskInAndFundAUMLessThanEqual(fundRisks, fundAUM);
        }
        else if(fundServiceUtils.isFundAMCsValid(fundAMCs)) {
            return fundRepository.findByFundAMCIn(fundAMCs);
        }
        else if(fundServiceUtils.isFundRisksValid(fundRisks)) {
            return fundRepository.findByFundRiskIn(fundRisks);
        }
        return fundRepository.findByFundAUMLessThanEqual(fundAUM);
    }

    public Double calculateFundValue(Fund fund, Double initialInvestment, Integer years) {
        FundReturnDTO fundReturnDTO = fundReturnRepository.findByFund(fund);
        Double returnMultiplier = fundReturnDTO.getFundReturnTotal();
        returnMultiplier = switch (years) {
            case 1 -> fundReturnDTO.getFundReturn1Year();
            case 3 -> fundReturnDTO.getFundReturn3Year();
            case 5 -> fundReturnDTO.getFundReturn5Year();
            default -> returnMultiplier;
        };
        returnMultiplier = returnMultiplier / 100.00 + 1.0;
        return initialInvestment * Math.pow(returnMultiplier, years);
    }
}
