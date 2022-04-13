package com.mark.PhoneExercise.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
public class User {

    @Id
    @Type(type="uuid-char")
    @GeneratedValue(generator = "USER_UUID_GENERATOR")
    @GenericGenerator(name = "USER_UUID_GENERATOR", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "User_UUID", updatable = false, nullable = false)
    private UUID userId;

    @Column(name = "User_Name", nullable = false, unique=true)
    private String userName;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "Email_Address", nullable = false, unique=true)
    private String emailAddress;

    @Column(name = "Preferred_Phone_Number")
    private String preferredPhoneNumber;

}
