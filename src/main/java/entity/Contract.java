package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String ID;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerID", foreignKey = @ForeignKey(name = "FkContract_CustomerID"))
    private Customer Customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartmentID", foreignKey = @ForeignKey(name = "FkContract_ApartmentID"))
    private Apartment apartment;
}
