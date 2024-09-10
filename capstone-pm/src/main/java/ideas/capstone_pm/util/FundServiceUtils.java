package ideas.capstone_pm.util;

import ideas.capstone_pm.dto.AverageReturnDTO;
import ideas.capstone_pm.dto.FundDescriptionDTO;
import ideas.capstone_pm.dto.FundHistoryDTO;
import ideas.capstone_pm.dto.PeerFundsDTO;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.entity.FundReturn;
import ideas.capstone_pm.repository.FundRepository;
import ideas.capstone_pm.repository.FundReturnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FundServiceUtils {

    @Autowired
    FundRepository fundRepository;
    @Autowired
    FundReturnRepository fundReturnRepository;

    public Boolean isFundAMCsValid(List<String> fundAMCs) {
        return fundAMCs != null && !fundAMCs.isEmpty();
    }

    public Boolean isFundRisksValid(List<String> fundRisks) {
        return fundRisks != null && !fundRisks.isEmpty();
    }

    public Boolean isFundAUMValid(Double fundAUM) {
        return fundAUM != null && fundAUM >= 0;
    }

    public Fund buildFund(FundDescriptionDTO fundDescriptionDTO) {
        return new Fund(0, fundDescriptionDTO.getFundName(), fundDescriptionDTO.getFundAMC(), fundDescriptionDTO.getFundRisk(), fundDescriptionDTO.getFundType(),
                fundDescriptionDTO.getFundAUM(), fundDescriptionDTO.getFundNAV(), fundDescriptionDTO.getFundManager(), fundDescriptionDTO.getFundDescription(),
                fundDescriptionDTO.getFundRating(), null, null, null);
    }

    public FundDescriptionDTO buildFundDTO(Fund fund) {
        List<PeerFundsDTO> peerFunds = fundRepository.findByFundTypeAndFundIdNotIn(fund.getFundType(), List.of(fund.getFundId()));
        AverageReturnDTO averageReturnDTO = fundReturnRepository.findAverageReturnsByFundType(fund.getFundType());
        return new FundDescriptionDTO(fund.getFundId(), fund.getFundName(), fund.getFundAMC(), fund.getFundRisk(), fund.getFundType(),
                fund.getFundAUM(), fund.getFundNAV(), fund.getFundManager(), fund.getFundDescription(), fund.getFundRating(), buildFundHistoryDTO(fund.getFundReturn()), peerFunds, averageReturnDTO);
    }

    public FundHistoryDTO buildFundHistoryDTO(FundReturn fundReturn) {
        return new FundHistoryDTO(fundReturn.getFundReturnId(), fundReturn.getFundReturn1Month(), fundReturn.getFundReturn1Year(), fundReturn.getFundReturn3Year(),
                fundReturn.getFundReturn5Year(), fundReturn.getFundReturnTotal());
    }
}
