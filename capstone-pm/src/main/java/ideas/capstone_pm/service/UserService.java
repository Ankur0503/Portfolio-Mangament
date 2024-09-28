package ideas.capstone_pm.service;

import ideas.capstone_pm.dto.UserDTO;
import ideas.capstone_pm.projection.UserProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.exception.userexpcetions.EmailAlreadyRegisteredException;
import ideas.capstone_pm.exception.userexpcetions.EmailNotFound;
import ideas.capstone_pm.repository.UserRepository;
import ideas.capstone_pm.roles.Roles;
import ideas.capstone_pm.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtil jwtUtil;

    public ApplicationUser addUser(ApplicationUser applicationUser) {
        if(userRepository.existsByUserEmail(applicationUser.getUserEmail())) {
            throw new EmailAlreadyRegisteredException();
        }
        String encodedPassword = passwordEncoder.encode(applicationUser.getUserPassword());
        applicationUser.setUserPassword(encodedPassword);

        if(applicationUser.getUserRole() == null || applicationUser.getUserRole().isEmpty()) {
            applicationUser.setUserRole(Roles.ROLE_USER);
        }

        return userRepository.save(applicationUser);
    }

    public UserProjection getCurrentUser(String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Invalid authorization header.");
            }

            String jwt = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(jwt);

            UserProjection user = userRepository.findByUserEmail(username);

            if (user == null) {
                throw new EmailNotFound();
            }

            return user;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving the current user.");
        }
    }


    public ApplicationUser updateUser(UserDTO userDTO) {
        Optional<ApplicationUser> applicationUser = userRepository.findById(userDTO.getUserId());

        if(applicationUser.isPresent()) {
            applicationUser.get().setUserName(userDTO.getUserName());
            applicationUser.get().setUserPhone(userDTO.getUserPhone());
            applicationUser.get().setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));

            return userRepository.save(applicationUser.get());
        }
        throw new EmailNotFound();
    }
}
