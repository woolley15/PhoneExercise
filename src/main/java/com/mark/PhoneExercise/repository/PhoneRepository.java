package com.mark.PhoneExercise.repository;

import com.mark.PhoneExercise.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PhoneRepository extends JpaRepository <Phone, UUID> {

    @Query("select p from Phone p where p.userId = :userId")
    List<Phone> findByUser(@Param("userId") UUID userId);

    void deleteByUserId(UUID userId);
}
