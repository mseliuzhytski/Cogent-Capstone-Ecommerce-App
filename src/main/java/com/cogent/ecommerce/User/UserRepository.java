package com.cogent.ecommerce.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CustomUser, Integer> {

    Optional<CustomUser> findByUsername(String username);

    Optional<CustomUser> findByEmail(String email);

    Optional<CustomUser> findByEmailAndResetToken(String email, String resetToken);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);


}
