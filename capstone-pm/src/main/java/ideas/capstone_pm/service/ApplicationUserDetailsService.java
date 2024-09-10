package ideas.capstone_pm.service;

import ideas.capstone_pm.dto.AdminDTO;
import ideas.capstone_pm.dto.UserDTO;
import ideas.capstone_pm.exception.userexpcetions.EmailNotFound;
import ideas.capstone_pm.repository.AdminRepository;
import ideas.capstone_pm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (userRepository.existsByUserEmail(username)) {
            UserDTO user = userRepository.findByUserEmail(username);
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserEmail())
                    .password(user.getUserPassword())
                    .roles(user.getUserRole())
                    .build();
        }
        if (adminRepository.existsByAdminEmail(username)) {
            AdminDTO admin = adminRepository.findByAdminEmail(username);
            return org.springframework.security.core.userdetails.User.builder()
                    .username(admin.getAdminEmail())
                    .password(admin.getAdminPassword())
                    .roles(admin.getAdminRole())
                    .build();
        }

        throw new EmailNotFound();
    }
}
