package ideas.capstone_pm.service;

import ideas.capstone_pm.dto.LoginDTO;
import ideas.capstone_pm.dto.UserDTO;
import ideas.capstone_pm.dto.UserProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.exception.userexpcetions.EmailAlreadyRegisteredException;
import ideas.capstone_pm.exception.userexpcetions.EmailNotFound;
import ideas.capstone_pm.exception.userexpcetions.InvalidCredentialException;
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

    public UserProjection loginUser(LoginDTO loginDTO) {
        if (!userRepository.existsByUserEmail(loginDTO.getEmail())) {
            throw new EmailNotFound();
        }
        UserProjection user = userRepository.findByUserEmail(loginDTO.getEmail());
        if(user.getUserPassword().equals(loginDTO.getPassword())) {
            return user;
        }
        throw new InvalidCredentialException();
    }

    public UserProjection getCurrentUser(String authorizationHeader) {
        String jwt = authorizationHeader.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return userRepository.findByUserEmail(username);
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
