package com.example.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.ClientSideException;
import com.example.exception.ConflictionException;
import com.example.exception.UnauthorizedException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    AccountRepository accountRepository;
    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }
    
    public Account registerUser(Account account) throws ClientSideException,ConflictionException{
        if(account.getPassword().length() < 4) throw new ClientSideException();
        if(account.getUsername().trim().length() == 0) throw new ClientSideException();
        if(accountRepository.findAccountByUsername(account.getUsername()).isPresent()) throw new ConflictionException();
        return accountRepository.save(account);
    }

    public Account loginUser(Account account) throws UnauthorizedException{
        Optional<Account> resultAccount = accountRepository.findAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
        if(!resultAccount.isPresent()) throw new UnauthorizedException();
        return resultAccount.get();
    }
}
