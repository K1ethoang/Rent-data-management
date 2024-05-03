package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Nationalized;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, insertable = false)
    private String id;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Nationalized
    private String address;

    @Column(name = "citizen_id")
    private String citizenId;

    @Column(name = "date_of_birth")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @Column(name = "phone_number")
    private String phoneNumber;
}
