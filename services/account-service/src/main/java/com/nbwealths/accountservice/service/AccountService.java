package com.nbwealths.accountservice.service;

import com.nbwealths.accountservice.dto.AccountDto;
import com.nbwealths.accountservice.entity.Account;
import com.nbwealths.accountservice.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountDto createAccount(AccountDto dto) {
        Account a = new Account();
        a.setOwner(dto.getOwner());
        a.setType(dto.getType());
        a.setCurrency(dto.getCurrency());
        a.setBalance(dto.getBalance() == null ? 0.0 : dto.getBalance());
        Account saved = accountRepository.save(a);
        return toDto(saved);
    }

    public Optional<AccountDto> getAccount(Long id) {
        return accountRepository.findById(id).map(this::toDto);
    }

    private AccountDto toDto(Account a) {
        return new AccountDto(a.getId(), a.getOwner(), a.getType(), a.getCurrency(), a.getBalance());
    }
}
