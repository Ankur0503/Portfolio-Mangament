package ideas.capstone_pm.controller;

import ideas.capstone_pm.dto.LoginDTO;
import ideas.capstone_pm.dto.authentication.AuthenticationResponse;
import ideas.capstone_pm.service.ApplicationUserDetailsService;
import ideas.capstone_pm.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private ApplicationUserDetailsService applicationUserDetailsService;
    @Autowired
    private AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginDTO loginRequest, HttpServletResponse response) throws AuthenticationException {
        try {
            AuthenticationResponse authenticationResponse = authService.authenticate(loginRequest);
            return ResponseEntity.ok(authenticationResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Authentication failed");
        }
    }
}
