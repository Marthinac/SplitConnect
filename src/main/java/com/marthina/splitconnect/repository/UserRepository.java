package com.marthina.splitconnect.repository;

import com.marthina.splitconnect.dto.UserResponseDTO;
import com.marthina.splitconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    List<User> findAllByOrderByIdAsc();

}
