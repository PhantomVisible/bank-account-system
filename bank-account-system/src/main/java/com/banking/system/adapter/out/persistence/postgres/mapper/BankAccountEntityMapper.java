package com.banking.system.adapter.out.persistence.postgres.mapper;

import com.banking.system.adapter.out.persistence.postgres.entity.BankAccountEntity;
import com.banking.system.adapter.out.persistence.postgres.entity.UserEntity;
import com.banking.system.domain.model.BankAccount;
import com.banking.system.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class BankAccountEntityMapper {

    private final UserEntityMapper userEntityMapper;

    public BankAccountEntityMapper(UserEntityMapper userEntityMapper) {
        this.userEntityMapper = userEntityMapper;
    }

    public BankAccountEntity toEntity(BankAccount bankAccount) {
        if (bankAccount == null) {
            return null;
        }

        // Convert Domain User to UserEntity for JPA
        UserEntity userEntity = userEntityMapper.toEntity(bankAccount.getUser());

        BankAccountEntity entity = new BankAccountEntity();
        entity.setId(bankAccount.getId());
        entity.setAccountNumber(bankAccount.getAccountNumber());
        entity.setBalance(bankAccount.getBalance());
        entity.setCurrency(bankAccount.getCurrency());
        entity.setUser(userEntity);

        return entity;
    }

    public BankAccount toDomain(BankAccountEntity entity) {
        if (entity == null) {
            return null;
        }

        // Convert UserEntity back to Domain User
        User user = userEntityMapper.toDomain(entity.getUser());

        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(entity.getId());
        bankAccount.setAccountNumber(entity.getAccountNumber());
        bankAccount.setBalance(entity.getBalance());
        bankAccount.setCurrency(entity.getCurrency());
        bankAccount.setUser(user);

        return bankAccount;
    }
}