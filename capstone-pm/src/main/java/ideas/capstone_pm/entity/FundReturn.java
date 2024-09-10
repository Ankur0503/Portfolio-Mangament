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
public class FundReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int fundReturnId;
    @Column(nullable = false)
    private double fundReturn1Month;
    @Column(nullable = false)
    private double fundReturn1Year;
    @Column(nullable = false)
    private double fundReturn3Year;
    @Column(nullable = false)
    private double fundReturn5Year;
    @Column(nullable = false)
    private double fundReturnTotal;

    @OneToOne
    @JoinColumn(name = "fundId")
    private Fund fund;
}
