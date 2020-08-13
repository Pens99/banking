package com.crealogix.apprentice.banking.domain.account;

import com.crealogix.apprentice.banking.domain.api.AccountDomainService;
import com.crealogix.apprentice.banking.domain.user.UserContext;
import com.crealogix.apprentice.banking.domain.user.UserContextHolder;
import com.crealogix.apprentice.banking.dto.Account;
import com.crealogix.apprentice.banking.persistence.entity.AccountEntity;
import com.crealogix.apprentice.banking.persistence.repository.AccountRepository;
import com.crealogix.apprentice.banking.persistence.repository.PersonRepository;
import com.crealogix.apprentice.banking.persistence.repository.UserRepository;
import com.crealogix.apprentice.banking.util.exception.AuthorizationException;
import com.crealogix.apprentice.banking.util.function.AccountFunctions;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class AccountDomainServiceImpl implements AccountDomainService {

    private final UserContextHolder userContextHolder;
    private final PersonRepository personRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountDomainServiceImpl(UserContextHolder userContextHolder, PersonRepository personRepository, AccountRepository accountRepository, UserRepository userRepository) {
        this.userContextHolder = userContextHolder;
        this.personRepository = personRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Account> getAccounts() {
        UserContext userContext = userContextHolder.getUserContext();
        return getAccountByPersonId(userContext.getPersonId());
    }

    @Override
    public Account getAccountById(Long accountId) {
        Optional<AccountEntity> optAccountEntity = accountRepository.findById(accountId);
        return optAccountEntity.map(AccountFunctions.mapToDto).get();
    }

    @Override
    public Account findAccountById(Long accountId, Long authorityId) {
        Optional<AccountEntity> optAccountEntity = accountRepository.findAccountById(authorityId, accountId);
        return optAccountEntity.map(AccountFunctions.mapToDto).get();
    }

    @Override
    public List<Account> getAccountByPersonId(Long personId) {
        List<AccountEntity> accountEntityList = accountRepository.findAccountByPerson(personId);
        List<Account> accounts = new ArrayList<>();
        for (AccountEntity accountEntity : accountEntityList) {
            Optional<AccountEntity> accountEntityOpt = Optional.of(accountEntity);
            accounts.add(accountEntityOpt.map(AccountFunctions.mapToDto).get());
        }
        return accounts;
    }

    @Override
    public void createAccount(Account account) {
        AccountFunctions.isValid(account);
        AccountEntity accountEntity = new AccountEntity(account.getIban(), account.getAccountType(), personRepository.findById(account.getPersonId()).get(), account.getBalance(), account.getBank());
        accountRepository.save(accountEntity);
    }

    @Override
    public Account getOwnedAccountById(Long authorityId, Long accountId) {
        try {
            Account account = findAccountById(accountId, authorityId);
            if (account != null) {
                return account;
            }else{
                throw returnAutorizationException(accountId, authorityId);
            }
        }catch (NoSuchElementException NSEx){
            throw returnAutorizationException(accountId, authorityId);
        }
    }

    public AuthorizationException returnAutorizationException(Long accountId, Long authorityId){
        return new AuthorizationException("No account with the id " + accountId + " could be fount for user with id " + authorityId);
    }
}
