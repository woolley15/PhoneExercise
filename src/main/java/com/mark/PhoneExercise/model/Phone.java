package com.mark.PhoneExercise.model;

import javax.persistence.*;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Phones")
public class Phone {

    @Id
    @Type(type="uuid-char")
    @GeneratedValue(generator = "PHONE_UUID_GENERATOR")
    @GenericGenerator(name = "PHONE_UUID_GENERATOR", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "Phone_UUID", nullable = false)
    private UUID phoneId;

    @Column(name = "Phone_Name", nullable = false)
    private String phoneName;

    @Column(name = "Phone_Number", nullable = false, unique=true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "Phone_Model", nullable = false)
    private PhoneModels phoneModel;

    @Column(name = "User_UUID", nullable = false)
    @Type(type="uuid-char")
    private UUID userId;

}
