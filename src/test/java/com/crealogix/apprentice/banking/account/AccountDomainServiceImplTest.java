package com.crealogix.apprentice.banking.account;

import com.crealogix.apprentice.banking.domain.account.AccountDomainServiceImpl;
import com.crealogix.apprentice.banking.domain.user.UserContext;
import com.crealogix.apprentice.banking.domain.user.UserContextHolder;
import com.crealogix.apprentice.banking.dto.Account;
import com.crealogix.apprentice.banking.persistence.entity.AccountEntity;
import com.crealogix.apprentice.banking.persistence.entity.PersonEntity;
import com.crealogix.apprentice.banking.persistence.repository.AccountRepository;
import com.crealogix.apprentice.banking.persistence.repository.PersonRepository;
import com.crealogix.apprentice.banking.persistence.repository.UserRepository;
import com.crealogix.apprentice.banking.util.exception.AuthorizationException;
import com.crealogix.apprentice.banking.util.function.AccountFunctions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountDomainServiceImplTest {

  @InjectMocks
  private AccountDomainServiceImpl accountDomainServiceImpl;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private PersonRepository personRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private Account account1;

  @Mock
  private Account account2;

  private List<Account> accounts;

  @Mock
  private AccountEntity accountEntity1;

  @Mock
  private AccountEntity accountEntity2;

  private List<AccountEntity> accountEntities;

  @Mock
  private PersonEntity personEntity;

  @Mock
  private UserContextHolder userContextHolder;

  @Mock
  UserContext userContext;

  @Mock
  NoSuchElementException noSuchElementException;

  private Optional<AccountEntity> optAccountEntity1;

  private Optional<PersonEntity> optPersonEntity1;

  @BeforeEach
  void setUp() {
    account1 = new Account(3L, 2L, "CH2789144561775262268", "TestAccount", "ImaginaryBank", 9999.99);
    account2 = new Account(4L, 2L, "CH2789144561775262269", "TestAccount2", "ImaginaryBank2", 11.00);
    personEntity = new PersonEntity("Caesar", "Julius");
    personEntity.setId(account1.getPersonId());
    accountEntity1 = new AccountEntity(account1.getIban(), account1.getAccountType(), personEntity, account1.getBalance(), account1.getBank());
    accountEntity1.setId(account1.getId());
    accountEntity2 = new AccountEntity(account2.getIban(), account2.getAccountType(), personEntity, account2.getBalance(), account2.getBank());
    accountEntity2.setId(account2.getId());
    accountEntities = new ArrayList<>();
    accountEntities.add(accountEntity1);
    accountEntities.add(accountEntity2);
    accounts = new ArrayList<>();
    accounts.add(account1);
    accounts.add(account2);
    optAccountEntity1 = Optional.of(accountEntity1);
    optPersonEntity1 = Optional.of(personEntity);
    userContext = new UserContext(account1.getPersonId(), false, account1.getPersonId());
  }

  @Test
  void getAccounts() {
    given(userContextHolder.getUserContext()).willReturn(userContext);
    given(accountRepository.findAccountByPerson(account1.getPersonId())).willReturn(accountEntities);

    List<Account> returnedAccounts = accountDomainServiceImpl.getAccounts();

    Assertions.assertThat(returnedAccounts.get(0).getId()).isEqualTo(account1.getId());
    Assertions.assertThat(returnedAccounts.get(0).getBank()).isEqualTo(account1.getBank());
    Assertions.assertThat(returnedAccounts.get(0).getBalance()).isEqualTo(account1.getBalance());
    Assertions.assertThat(returnedAccounts.get(0).getAccountType()).isEqualTo(account1.getAccountType());
    Assertions.assertThat(returnedAccounts.get(0).getIban()).isEqualTo(account1.getIban());
    Assertions.assertThat(returnedAccounts.get(0).getPersonId()).isEqualTo(account1.getPersonId());
    Assertions.assertThat(returnedAccounts.get(1).getId()).isEqualTo(account2.getId());
    Assertions.assertThat(returnedAccounts.get(1).getBank()).isEqualTo(account2.getBank());
    Assertions.assertThat(returnedAccounts.get(1).getBalance()).isEqualTo(account2.getBalance());
    Assertions.assertThat(returnedAccounts.get(1).getAccountType()).isEqualTo(account2.getAccountType());
    Assertions.assertThat(returnedAccounts.get(1).getIban()).isEqualTo(account2.getIban());
    Assertions.assertThat(returnedAccounts.get(1).getPersonId()).isEqualTo(account2.getPersonId());

    ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
    verify(accountRepository, times(1)).findAccountByPerson(longArgumentCaptor.capture());
    Assertions.assertThat(longArgumentCaptor.getValue().equals(userContext.getPersonId()));
  }

  @Test
  void getAccountById() {
    Mockito.when(accountRepository.findById(account1.getId())).thenAnswer(invocation -> {
      if ("".equals(invocation.getArgument(0)))
        throw new NoSuchElementException();
      return optAccountEntity1;
    });

    Account returnedAccount = accountDomainServiceImpl.getAccountById(account1.getId());

    Assertions.assertThat(returnedAccount.getId()).isEqualTo(account1.getId());
    Assertions.assertThat(returnedAccount.getBank()).isEqualTo(account1.getBank());
    Assertions.assertThat(returnedAccount.getBalance()).isEqualTo(account1.getBalance());
    Assertions.assertThat(returnedAccount.getAccountType()).isEqualTo(account1.getAccountType());
    Assertions.assertThat(returnedAccount.getIban()).isEqualTo(account1.getIban());
    Assertions.assertThat(returnedAccount.getPersonId()).isEqualTo(account1.getPersonId());

    ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
    verify(accountRepository, times(1)).findById(longArgumentCaptor.capture());
    Assertions.assertThat(longArgumentCaptor.getValue().equals(account1.getId()));
  }

  @Test
  void findAccountById() {
    Mockito.when(accountRepository.findAccountById(account1.getPersonId(), account1.getId())).thenAnswer(invocation -> {
      if ("".equals(invocation.getArgument(0)))
        throw new NoSuchElementException();
      return optAccountEntity1;
    });

    Account returnedAccount = accountDomainServiceImpl.findAccountById(account1.getId(), account1.getPersonId());

    Assertions.assertThat(returnedAccount.getId()).isEqualTo(account1.getId());
    Assertions.assertThat(returnedAccount.getBank()).isEqualTo(account1.getBank());
    Assertions.assertThat(returnedAccount.getBalance()).isEqualTo(account1.getBalance());
    Assertions.assertThat(returnedAccount.getAccountType()).isEqualTo(account1.getAccountType());
    Assertions.assertThat(returnedAccount.getIban()).isEqualTo(account1.getIban());
    Assertions.assertThat(returnedAccount.getPersonId()).isEqualTo(account1.getPersonId());

    ArgumentCaptor<Long> longArgumentCaptor1 = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Long> longArgumentCaptor2 = ArgumentCaptor.forClass(Long.class);
    verify(accountRepository, times(1)).findAccountById(longArgumentCaptor1.capture(), longArgumentCaptor2.capture());
    Assertions.assertThat(longArgumentCaptor1.getValue().equals(account1.getPersonId()) && longArgumentCaptor2.getValue().equals(account1.getId()));
  }

  @Test
  void getAccountByPersonId() {
    given(accountRepository.findAccountByPerson(account1.getPersonId())).willReturn(accountEntities);

    List<Account> returnedAccounts = accountDomainServiceImpl.getAccountByPersonId(account1.getPersonId());

    Assertions.assertThat(returnedAccounts.get(0).getId()).isEqualTo(account1.getId());
    Assertions.assertThat(returnedAccounts.get(0).getBank()).isEqualTo(account1.getBank());
    Assertions.assertThat(returnedAccounts.get(0).getBalance()).isEqualTo(account1.getBalance());
    Assertions.assertThat(returnedAccounts.get(0).getAccountType()).isEqualTo(account1.getAccountType());
    Assertions.assertThat(returnedAccounts.get(0).getIban()).isEqualTo(account1.getIban());
    Assertions.assertThat(returnedAccounts.get(0).getPersonId()).isEqualTo(account1.getPersonId());
    Assertions.assertThat(returnedAccounts.get(1).getId()).isEqualTo(account2.getId());
    Assertions.assertThat(returnedAccounts.get(1).getBank()).isEqualTo(account2.getBank());
    Assertions.assertThat(returnedAccounts.get(1).getBalance()).isEqualTo(account2.getBalance());
    Assertions.assertThat(returnedAccounts.get(1).getAccountType()).isEqualTo(account2.getAccountType());
    Assertions.assertThat(returnedAccounts.get(1).getIban()).isEqualTo(account2.getIban());
    Assertions.assertThat(returnedAccounts.get(1).getPersonId()).isEqualTo(account2.getPersonId());

    ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
    verify(accountRepository, times(1)).findAccountByPerson(longArgumentCaptor.capture());
    Assertions.assertThat(longArgumentCaptor.getValue().equals(userContext.getPersonId()));
  }

  @Test
  void createAccount() {
    Mockito.when(personRepository.findById(account1.getPersonId())).thenAnswer(invocation -> {
      if ("".equals(invocation.getArgument(0)))
        throw new NoSuchElementException();
      return optPersonEntity1;
    });

    account1 = new Account(null, 2L, "CH2789144561775262268", "TestAccount", "ImaginaryBank", 9999.99);
    accountDomainServiceImpl.createAccount(account1);

    ArgumentCaptor<AccountEntity> argumentCaptor = ArgumentCaptor.forClass(AccountEntity.class);
    verify(accountRepository, times(1)).save(argumentCaptor.capture());
    Assertions.assertThat(argumentCaptor.getValue()).hasSameClassAs(accountEntity1).hasNoNullFieldsOrPropertiesExcept("id");
  }

  @Test
  void getOwnedAcountById() {
    given(accountRepository.findAccountById(account1.getPersonId(), account1.getId())).willReturn(optAccountEntity1);

    AccountDomainServiceImpl spyedAccountDomainServiceImpl = Mockito
        .spy(new AccountDomainServiceImpl(userContextHolder, personRepository, accountRepository, userRepository));
    Account returnedAccount = spyedAccountDomainServiceImpl.getOwnedAccountById(account1.getPersonId(), account1.getId());

    Assertions.assertThat(returnedAccount.getId()).isEqualTo(account1.getId());
    Assertions.assertThat(returnedAccount.getBank()).isEqualTo(account1.getBank());
    Assertions.assertThat(returnedAccount.getBalance()).isEqualTo(account1.getBalance());
    Assertions.assertThat(returnedAccount.getAccountType()).isEqualTo(account1.getAccountType());
    Assertions.assertThat(returnedAccount.getIban()).isEqualTo(account1.getIban());
    Assertions.assertThat(returnedAccount.getPersonId()).isEqualTo(account1.getPersonId());

    ArgumentCaptor<Long> longArgumentCaptor1 = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Long> longArgumentCaptor2 = ArgumentCaptor.forClass(Long.class);
    verify(spyedAccountDomainServiceImpl, times(1)).findAccountById(longArgumentCaptor1.capture(), longArgumentCaptor2.capture());
    Assertions.assertThat(longArgumentCaptor1.getValue().equals(account1.getId()) && longArgumentCaptor2.getValue().equals(account1.getPersonId()));
  }

  @Test
  void getOwnedAccountByIdExceptionTest() {
    given(accountRepository.findAccountById(account1.getPersonId(), account1.getId())).willThrow(noSuchElementException);
    assertThrows(AuthorizationException.class, () -> accountDomainServiceImpl.getOwnedAccountById(account1.getPersonId(), account1.getId()));
  }

  @Test
  void returnAuthorizationException(){
    Assertions.assertThat(accountDomainServiceImpl.returnAutorizationException(account1.getId(), account1.getPersonId())).isInstanceOf(AuthorizationException.class);
  }
}