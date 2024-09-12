package ideas.capstone_pm.controller;

import ideas.capstone_pm.dto.LoginDTO;
import ideas.capstone_pm.dto.UserDTO;
import ideas.capstone_pm.dto.authentication.AuthenticationResponse;
import ideas.capstone_pm.repository.UserRepository;
import ideas.capstone_pm.service.ApplicationUserDetailsService;
import ideas.capstone_pm.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ApplicationUserDetailsService applicationUserDetailsService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginDTO loginRequest) throws AuthenticationException {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        final UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        UserDTO userDTO = userRepository.findByUserEmail(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(userDTO, jwt));


    }
}
