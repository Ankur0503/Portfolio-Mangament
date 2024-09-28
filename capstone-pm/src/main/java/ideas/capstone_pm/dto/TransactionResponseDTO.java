package ideas.capstone_pm.dto;

import ideas.capstone_pm.projection.TransactionProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {
    TransactionProjection transactionProjection;
    Double currentValue;
}
