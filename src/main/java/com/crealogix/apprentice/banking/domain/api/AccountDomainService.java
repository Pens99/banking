package com.crealogix.apprentice.banking.domain.api;

import com.crealogix.apprentice.banking.dto.Account;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountDomainService {

    List<Account> getAccounts();

    Account getAccountById(Long accountId);

    Account findAccountById(Long authorityId, Long accountId);

    List<Account> getAccountByPersonId(Long accountId);

    void createAccount(Account account);

    Account getOwnedAccountById(Long authorityId, Long accountId);
}
