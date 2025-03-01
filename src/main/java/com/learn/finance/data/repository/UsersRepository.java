package com.learn.finance.data.repository;

import com.learn.finance.data.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity,String> {
    Optional<UsersEntity> findByEmail(String email);
}
