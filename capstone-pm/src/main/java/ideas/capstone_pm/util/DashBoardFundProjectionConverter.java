package ideas.capstone_pm.util;

import ideas.capstone_pm.dto.DashBoardFundDTO;
import ideas.capstone_pm.projection.DashBoardFundProjection;
import ideas.capstone_pm.dto.DashBoardFundReturnDTO;

import java.util.List;
import java.util.stream.Collectors;

public class DashBoardFundProjectionConverter {
    public static List<DashBoardFundDTO> convertToDTOList(List<DashBoardFundProjection> projections) {
        if (projections == null) {
            return List.of();
        }

        return projections.stream()
                .map(DashBoardFundProjectionConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    private static DashBoardFundDTO convertToDTO(DashBoardFundProjection projection) {
        if (projection == null) {
            return null;
        }

        DashBoardFundReturnDTO fundReturnDTO = null;
        if (projection.getFundReturn() != null) {
            fundReturnDTO = new DashBoardFundReturnDTO(
                    projection.getFundReturn().getFundReturn1Year(),
                    projection.getFundReturn().getFundReturn3Year(),
                    projection.getFundReturn().getFundReturn5Year()
            );
        }

        return new DashBoardFundDTO(
                projection.getFundId(),
                projection.getFundName(),
                projection.getFundAMC(),
                projection.getFundRisk(),
                projection.getFundNAV(),
                projection.getFundType(),
                projection.getFundAUM(),
                projection.getFundRating(),
                fundReturnDTO
        );
    }
}
