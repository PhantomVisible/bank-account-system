package com.banking.system.adapter.out.persistence.postgres.repository;

import com.banking.system.adapter.out.persistence.postgres.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountJpaRepository extends JpaRepository<BankAccountEntity, Long> {

    Optional<BankAccountEntity> findByAccountNumber(String accountNumber);
    // Spring Data JPA automatically creates this SQL:
    // SELECT * FROM bank_accounts WHERE account_number = ?
    // And handles: Parameters, Execution, Result mapping, Optional wrapping

    List<BankAccountEntity> findByUserId(Long userId);
    /** Spring looks at BankAccountEntity and sees:
    *@ManyToOne
    *@JoinColumn(name = "user_id")
    *private UserEntity user;

    * Spring understands: "Find by user's id"
    * Generates: SELECT * FROM bank_accounts WHERE user_id = ?
    */

    boolean existsByAccountNumber(String accountNumber);
}