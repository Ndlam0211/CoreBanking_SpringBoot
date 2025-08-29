package com.lamnd.corebanking.repository;

import com.lamnd.corebanking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber(String accountNumber);
    User findByAccountNumber(String accountNumber);
}
