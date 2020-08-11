package com.crealogix.apprentice.banking.account;

import com.crealogix.apprentice.banking.domain.account.AccountDomainServiceImpl;
import com.crealogix.apprentice.banking.dto.Account;
import com.crealogix.apprentice.banking.dto.Person;
import com.crealogix.apprentice.banking.persistence.entity.AccountEntity;
import com.crealogix.apprentice.banking.persistence.entity.PersonEntity;
import com.crealogix.apprentice.banking.persistence.repository.AccountRepository;
import com.crealogix.apprentice.banking.persistence.repository.PersonRepository;
import com.crealogix.apprentice.banking.util.function.AccountFunctions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class AccountDomainServiceImplTest {

    @InjectMocks
    private AccountDomainServiceImpl accountDomainServiceImpl;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private Account account;

    @Mock
    private AccountEntity accountEntity;

    private Person person;

    @BeforeEach
    void setUp() {
        account = new Account(3L, 2L, "CH2789144561775262268", "TestAccount", "ImaginaryBank", 9999.99);
        person = new Person(4L, "Caesar", "Julius");
        accountEntity = new AccountEntity(account.getIban(), account.getAccountType(), personRepository.findById(account.getPersonId()).get(), account.getBalance(), account.getBank());

    }

    @Test
    void getAccountWithoutId() {

    }

    @Test
    void getAccountWithId() {
        Mockito.when(accountRepository.findAccountByPerson(1L)).thenAnswer(invocation -> {
            if ("".equals(invocation.getArgument(0))) throw new NoSuchElementException();
            List<AccountEntity> accountEntities = new ArrayList<>();
            accountEntities.add(accountEntity);
            return accountEntities;
        });

        List<AccountEntity> accountEntityReturnedByService = accountRepository.findAccountByPerson(account.getId());
        List<Account> accounts = new ArrayList<>();
        for (AccountEntity accountEntity : accountEntityReturnedByService) {
            Optional<AccountEntity> accountEntityOpt = Optional.of(accountEntity);
            accounts.add(accountEntityOpt.map(AccountFunctions.mapToDto).get());
        }

        Assertions.assertThat(accounts.size()).isEqualTo(1);
        Assertions.assertThat(accounts.get(0).getId()).isEqualTo(account.getId());
        Assertions.assertThat(accounts.get(0).getBank()).isEqualTo(account.getBank());
        Assertions.assertThat(accounts.get(0).getBalance()).isEqualTo(account.getBalance());
        Assertions.assertThat(accounts.get(0).getAccountType()).isEqualTo(account.getAccountType());
        Assertions.assertThat(accounts.get(0).getIban()).isEqualTo(account.getIban());
        Assertions.assertThat(accounts.get(0).getPersonId()).isEqualTo(account.getPersonId());

        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(accountRepository, times(1)).findAccountByPerson(longArgumentCaptor.capture());
        Assertions.assertThat(longArgumentCaptor.getValue().equals(accounts.get(0).getId()));
    }

    @Test
    void createAccount() {

    }
}