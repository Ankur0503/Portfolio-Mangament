package ideas.capstone_pm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FundHistoryDTO {
    private int fundReturnId;
    private double fundReturn1Month;
    private double fundReturn1Year;
    private double fundReturn3Year;
    private double fundReturn5Year;
    private double fundReturnTotal;
}
