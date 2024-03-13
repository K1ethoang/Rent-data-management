package com.example.demo.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Date;

@Builder
@AllArgsConstructor
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NaturalId // định nghĩa cho các trường mà mặc định no là unique (vd: Số CCCD, email)
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String avatar;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_date")
    private Date createDate;

    private State state;

    @ManyToOne
    private Role role;
}
