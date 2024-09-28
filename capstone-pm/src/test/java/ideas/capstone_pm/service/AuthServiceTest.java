package ideas.capstone_pm.service;

import ideas.capstone_pm.dto.LoginDTO;
import ideas.capstone_pm.projection.UserProjection;
import ideas.capstone_pm.dto.AuthenticationResponse;
import ideas.capstone_pm.repository.UserRepository;
import ideas.capstone_pm.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private ApplicationUserDetailsService applicationUserDetailsService;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthService authService;

    @Test
    public void authenticate() {
        String email = "test@example.com";
        String password = "password";
        LoginDTO loginRequest = new LoginDTO(email, password);
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        UserProjection userProjection = Mockito.mock(UserProjection.class);
        String jwt = "jwt_token";

        when(applicationUserDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(userRepository.findByUserEmail(email)).thenReturn(userProjection);
        when(jwtUtil.generateToken(userDetails)).thenReturn(jwt);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, password));
        
        AuthenticationResponse response = authService.authenticate(loginRequest);
        assertNotNull(response);
    }

    @Test
    void testAuthenticateFailure() {
        String email = "test@example.com";
        String password = "wrongPassword";
        LoginDTO loginRequest = new LoginDTO(email, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Authentication failed"));

        assertThrows(AuthenticationException.class, () -> authService.authenticate(loginRequest));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(applicationUserDetailsService, never()).loadUserByUsername(anyString());
        verify(userRepository, never()).findByUserEmail(anyString());
        verify(jwtUtil, never()).generateToken(any());
    }
}
