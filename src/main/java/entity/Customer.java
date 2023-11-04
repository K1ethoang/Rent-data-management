package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String ID;
    private String firstName;
    private  String lastName;
    private String address;
    private int age;
    private String status;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Contract> contracts = new ArrayList<>();
}
