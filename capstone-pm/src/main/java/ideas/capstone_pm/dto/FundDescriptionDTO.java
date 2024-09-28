package ideas.capstone_pm.dto;

import ideas.capstone_pm.projection.AverageReturnProjection;
import ideas.capstone_pm.projection.PeerFundsProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FundDescriptionDTO {
    private int fundId;
    private String fundName;
    private String fundAMC;
    private String fundRisk;
    private String fundType;
    private double fundAUM;
    private double fundNAV;
    private String fundManager;
    private String fundDescription;
    private double fundRating;
    private FundHistoryDTO fundHistory;
    private List<PeerFundsProjection> peerFunds;
    private AverageReturnProjection averageReturnProjection;
}
