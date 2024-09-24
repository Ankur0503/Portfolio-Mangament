package ideas.capstone_pm.dto.authentication;

import ideas.capstone_pm.dto.UserProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private UserProjection user;
    private String jwt;
}
