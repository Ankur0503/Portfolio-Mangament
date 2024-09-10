package ideas.capstone_pm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int transactionId;
    @Column(nullable = false)
    private double transactionInitialInvestment;
    @Column(nullable = false)
    private String transactionDuration;
    @Column(nullable = false)
    private Date transactionDate;

    @ManyToOne
    @JoinColumn(name = "fundId")
    private Fund fund;
    @ManyToOne
    @JoinColumn(name = "userId")
    private ApplicationUser user;
}
