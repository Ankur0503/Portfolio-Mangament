package ideas.capstone_pm.controller;

import ideas.capstone_pm.dto.LoginDTO;
import ideas.capstone_pm.dto.UserDTO;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.exception.userexpcetions.EmailAlreadyRegisteredException;
import ideas.capstone_pm.exception.userexpcetions.EmailNotFound;
import ideas.capstone_pm.exception.userexpcetions.InvalidCredentialException;
import ideas.capstone_pm.repository.UserRepository;
import ideas.capstone_pm.roles.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("users")
    public List<UserDTO> getAllUsers() {
        return userRepository.findBy();
    }

    @PostMapping("/users/register")
    public ApplicationUser addUser(@RequestBody ApplicationUser applicationUser) {
        if(userRepository.existsByUserEmail(applicationUser.getUserEmail())) {
            throw new EmailAlreadyRegisteredException();
        }
        applicationUser.setUserPassword(passwordEncoder.encode(applicationUser.getUserPassword()));

        if(applicationUser.getUserRole() == null || applicationUser.getUserRole().isEmpty()) {
            applicationUser.setUserRole(Roles.ROLE_USER);
        }

        return userRepository.save(applicationUser);
    }

    @PostMapping("/user/login")
    public UserDTO loginUser(@RequestBody LoginDTO loginDTO) {
        if (!userRepository.existsByUserEmail(loginDTO.getEmail())) {
            throw new EmailNotFound();
        }
        UserDTO user = userRepository.findByUserEmail(loginDTO.getEmail());
        if(user.getUserPassword().equals(loginDTO.getPassword())) {
            return user;
        }
        throw new InvalidCredentialException();
    }

    @PutMapping("users")
    public ApplicationUser updateUser(@RequestBody ApplicationUser applicationUser) {
        if(!userRepository.existsByUserEmail(applicationUser.getUserEmail())) {
            throw new EmailNotFound();
        }

        return userRepository.save(applicationUser);
    }
}
