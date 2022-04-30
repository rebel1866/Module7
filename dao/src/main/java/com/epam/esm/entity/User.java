package com.epam.esm.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long SERIAL_VERSION_UID = 15447522564545L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "user_surname")
    private String userSurname;
    private String login;
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String email;
    private String operation;
    private LocalDateTime operationTime;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @PrePersist
    public void onPrePersist() {
        setOperation("User is persisted id DB");
        setOperationTime(LocalDateTime.now());
    }

    @PreUpdate
    public void onPreUpdate() {
        setOperation("User is updated");
        setOperationTime(LocalDateTime.now());
    }
}
