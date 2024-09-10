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
@NoArgsConstructor
@AllArgsConstructor
public class Fund {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int fundId;
    @Column(nullable = false)
    private String fundName;
    @Column(nullable = false)
    private String fundAMC;
    @Column(nullable = false)
    private String fundRisk;
    @Column(nullable = false)
    private String fundType;
    @Column(nullable = false)
    private double fundAUM;
    @Column(nullable = false)
    private double fundNAV;
    @Column(nullable = false)
    private String fundManager;
    private String fundDescription;
    private Double fundRating;

    @OneToOne(mappedBy = "fund")
    private FundReturn fundReturn;
    @OneToMany(mappedBy = "fund")
    private List<Transaction> fundTransactions;
    @OneToMany(mappedBy = "fund")
    private List<Cart> cartEntries;
}
