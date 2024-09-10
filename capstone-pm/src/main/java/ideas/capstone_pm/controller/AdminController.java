package ideas.capstone_pm.controller;

import ideas.capstone_pm.entity.Admin;
import ideas.capstone_pm.exception.userexpcetions.EmailAlreadyRegisteredException;
import ideas.capstone_pm.repository.AdminRepository;
import ideas.capstone_pm.roles.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Admin addAdmin(@RequestBody Admin admin) {
        if(adminRepository.existsByAdminEmail(admin.getAdminEmail())) {
            throw new EmailAlreadyRegisteredException();
        }
        admin.setAdminPassword(passwordEncoder.encode(admin.getAdminPassword()));

        if(admin.getAdminRole() == null || admin.getAdminRole().isEmpty()) {
            admin.setAdminRole(Roles.ROLE_USER);
        }

        return adminRepository.save(admin);
    }
}
