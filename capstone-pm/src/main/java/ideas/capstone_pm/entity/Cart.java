package ideas.capstone_pm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cartId;

    @ManyToOne
    @JoinColumn(name = "fundId")
    private Fund fund;

    @ManyToOne
    @JoinColumn(name = "userId")
    private ApplicationUser user;
    private double plannedInvestment;
}
