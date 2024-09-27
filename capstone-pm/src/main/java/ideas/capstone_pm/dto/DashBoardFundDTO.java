package ideas.capstone_pm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashBoardFundDTO {
    Integer fundId;
    String fundName;
    String fundAMC;
    String fundRisk;
    Double fundNAV;
    String fundType;
    Double fundAUM;
    Double fundRating;
    DashBoardFundReturnDTO fundReturn;
}
