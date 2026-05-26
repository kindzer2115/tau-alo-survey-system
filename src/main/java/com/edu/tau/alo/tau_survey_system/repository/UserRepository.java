package com.edu.tau.alo.tau_survey_system.repository;

import com.edu.tau.alo.tau_survey_system.model.ClassStudent;
import com.edu.tau.alo.tau_survey_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByMicrosoftOid(String microsoftOid);

    long countByRole(User.Role role);

    @Query("SELECT cs.student FROM ClassStudent cs WHERE cs.clazz.id = :classId")
    List<User> findStudentsByClassId(@Param("classId") Long classId);
}