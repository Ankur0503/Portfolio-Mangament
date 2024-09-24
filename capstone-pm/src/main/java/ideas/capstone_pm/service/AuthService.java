package ideas.capstone_pm.service;

import ideas.capstone_pm.dto.LoginDTO;
import ideas.capstone_pm.dto.UserProjection;
import ideas.capstone_pm.dto.authentication.AuthenticationResponse;
import ideas.capstone_pm.repository.UserRepository;
import ideas.capstone_pm.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ApplicationUserDetailsService applicationUserDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationResponse authenticate(LoginDTO loginRequest) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        UserProjection userDTO = userRepository.findByUserEmail(loginRequest.getEmail());
        String jwt = jwtUtil.generateToken(userDetails);

        return new AuthenticationResponse(userDTO, jwt);
    }
}
