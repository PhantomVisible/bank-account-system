package com.banking.system.adapter.out.persistence.postgres.repository;

import com.banking.system.adapter.out.persistence.postgres.entity.BankAccountEntity;
import com.banking.system.adapter.out.persistence.postgres.mapper.BankAccountEntityMapper;
import com.banking.system.application.port.out.persistence.BankAccountRepository;
import com.banking.system.domain.model.BankAccount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BankAccountPostgresRepository implements BankAccountRepository {

    private final BankAccountJpaRepository bankAccountJpaRepository;
    private final BankAccountEntityMapper bankAccountEntityMapper;

    public BankAccountPostgresRepository(BankAccountJpaRepository bankAccountJpaRepository,
                                         BankAccountEntityMapper bankAccountEntityMapper) {
        this.bankAccountJpaRepository = bankAccountJpaRepository;
        this.bankAccountEntityMapper = bankAccountEntityMapper;
    }

    @Override
    public BankAccount save(BankAccount bankAccount) {
        BankAccountEntity entity = bankAccountEntityMapper.toEntity(bankAccount);
        BankAccountEntity savedEntity = bankAccountJpaRepository.save(entity);
        return bankAccountEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<BankAccount> findById(Long id) {
        return bankAccountJpaRepository.findById(id)
                .map(bankAccountEntityMapper::toDomain);
    }

    @Override
    public Optional<BankAccount> findByAccountNumber(String accountNumber) {
        return bankAccountJpaRepository.findByAccountNumber(accountNumber)
                .map(bankAccountEntityMapper::toDomain);
    }

    @Override
    public List<BankAccount> findByUserId(Long userId) {
        return bankAccountJpaRepository.findByUserId(userId).stream()
                .map(bankAccountEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return bankAccountJpaRepository.existsByAccountNumber(accountNumber);
    }

    @Override
    public List<BankAccount> findAll() {
        return bankAccountJpaRepository.findAll().stream()
                .map(bankAccountEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        bankAccountJpaRepository.deleteById(id);
    }

    @Override
    public void updateBalance(Long accountId, BigDecimal newBalance) {
        // For performance, we can update just the balance without full object mapping
        bankAccountJpaRepository.findById(accountId).ifPresent(account -> {
            account.setBalance(newBalance);
            bankAccountJpaRepository.save(account);
        });
    }
}