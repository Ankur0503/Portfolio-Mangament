package ideas.capstone_pm.service;

import ideas.capstone_pm.dto.*;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.exception.fundexceptions.FundNotFoundException;
import ideas.capstone_pm.projection.DashBoardFundProjection;
import ideas.capstone_pm.projection.FundReturnProjection;
import ideas.capstone_pm.repository.FundRepository;
import ideas.capstone_pm.repository.FundReturnRepository;
import ideas.capstone_pm.util.FundServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return new DashBoardFilters(fundRepository.findAllDistinctFundAMCs(), fundRepository.findAllDistinctFundTypes(), fundRepository.findAllDistinctFundRisks());
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
        else if(fundServiceUtils.isFundAUMValid(fundAUM)) {
            return fundRepository.findByFundAUMLessThanEqual(fundAUM);
        }
        return fundRepository.findBy();
    }

    public Double calculateFundValue(Integer fundId, Double initialInvestment, Integer years) {
        FundReturnProjection fundReturnProjection = fundReturnRepository.findByFundFundId(fundId);

        if (fundReturnProjection == null) {
            throw new IllegalArgumentException("Fund not found.");
        }

        Double returnMultiplier = fundReturnProjection.getFundReturnTotal();
        returnMultiplier = switch (years) {
            case 1 -> fundReturnProjection.getFundReturn1Year();
            case 3 -> fundReturnProjection.getFundReturn3Year();
            case 5 -> fundReturnProjection.getFundReturn5Year();
            default -> returnMultiplier;
        };
        returnMultiplier = returnMultiplier / 100.00 + 1.0;
        return initialInvestment * Math.pow(returnMultiplier, years);
    }
}
