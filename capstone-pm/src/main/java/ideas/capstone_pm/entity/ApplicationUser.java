package ideas.capstone_pm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int userId;
    @Column(nullable = false)
    private String userName;
    @Column(unique = true, nullable = false)
    private String userEmail;
    @Column(nullable = false)
    private String userPassword;
    @Column(nullable = false)
    private String userPhone;
    private int userAge;
    private String userRole;

    @OneToMany(mappedBy = "user")
    private List<Transaction> userTransactions;
    @OneToMany(mappedBy = "user")
    private List<Cart> userCarts;
}
