package com.learn.finance.data.repository;

import com.learn.finance.data.entity.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<BankEntity,String> {

    BankEntity findByBic(String bic);
}

