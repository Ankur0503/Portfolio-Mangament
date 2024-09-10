package ideas.capstone_pm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashBoardFilters {
    List<String> fundAMCs;
    List<String> fundTypes;
}
