package com.pq.grocery.entity;
import com.pq.grocery.enums.Role;
import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String phone;
    private String password;
    private String address;
    private Role role;
}
