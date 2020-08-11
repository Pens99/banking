package com.crealogix.apprentice.banking.rest;

import com.crealogix.apprentice.banking.domain.api.AccountDomainService;
import com.crealogix.apprentice.banking.dto.Account;
import com.crealogix.apprentice.banking.util.exception.AuthorizationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class AccountRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AccountDomainService accountDomainService;

  private List<Account> accounts;

  @Mock
  private Account account1;

  @Mock
  private Account account2;

  @Mock
  private Account account3;

  @Mock
  private AuthorizationException authorizationException;

  @BeforeEach
  void setUp() {
    account1 = new Account(100L, 2L, "CH2789144561775262268", "TestAccount", "ImaginaryBank", 9999.99);
    account2 = new Account(101L, 2L, "CH2789144561775262269", "AnotherTestAcc", "TestBank", 3.00);
    account3 = new Account(102L, 2L, "CH2789144561775262270", "ImaginaryAccount", "Another Bank", 0.00);
    this.accounts = new ArrayList<>();
    this.accounts.add(account1);
    this.accounts.add(account2);
    this.accounts.add(account3);
    authorizationException = new AuthorizationException("Not Allowed");
  }

  @Test
  void getAccount() throws Exception {
    given(accountDomainService.getAccount()).willReturn(accounts);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/banking/v1/account").header("authorityId", 2)).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(accounts.size()))).andExpect(jsonPath("$.[0].id").value(account1.getId()))
        .andExpect(jsonPath("$.[0].personId").value(account1.getPersonId())).andExpect(jsonPath("$.[0].iban").value(account1.getIban()))
        .andExpect(jsonPath("$.[0].accountType").value(account1.getAccountType())).andExpect(jsonPath("$.[0].balance").value(account1.getBalance()))
        .andExpect(jsonPath("$.[0].bank").value(account1.getBank())).andExpect(jsonPath("$.[1].id").value(account2.getId()))
        .andExpect(jsonPath("$.[1].personId").value(account2.getPersonId())).andExpect(jsonPath("$.[1].iban").value(account2.getIban()))
        .andExpect(jsonPath("$.[1].accountType").value(account2.getAccountType())).andExpect(jsonPath("$.[1].balance").value(account2.getBalance()))
        .andExpect(jsonPath("$.[1].bank").value(account2.getBank())).andExpect(jsonPath("$.[2].id").value(account3.getId()))
        .andExpect(jsonPath("$.[2].personId").value(account3.getPersonId())).andExpect(jsonPath("$.[2].iban").value(account3.getIban()))
        .andExpect(jsonPath("$.[2].accountType").value(account3.getAccountType())).andExpect(jsonPath("$.[2].balance").value(account3.getBalance()))
        .andExpect(jsonPath("$.[2].bank").value(account3.getBank()));
  }

  @Test
  void getAccountByIdAsCustomer() throws Exception {
    given(accountDomainService.getOwnedAccountById(2L, 2L)).willReturn(account1);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/banking/v1/account/2").header("authorityId", 2)).andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(account1.getId())).andExpect(jsonPath("$.personId").value(account1.getPersonId()))
        .andExpect(jsonPath("$.iban").value(account1.getIban())).andExpect(jsonPath("$.accountType").value(account1.getAccountType()))
        .andExpect(jsonPath("$.balance").value(account1.getBalance())).andExpect(jsonPath("$.bank").value(account1.getBank()));
  }

  @Test
  void getAccountByCustomerIdAsCustomer() throws Exception {
    given(accountDomainService.getAccountByPersonId(2L)).willReturn(accounts);

    this.mockMvc.perform(MockMvcRequestBuilders.get("/banking/v1/account/person/2").header("authorityId", 2)).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(accounts.size()))).andExpect(jsonPath("$.[0].id").value(account1.getId()))
        .andExpect(jsonPath("$.[0].personId").value(account1.getPersonId())).andExpect(jsonPath("$.[0].iban").value(account1.getIban()))
        .andExpect(jsonPath("$.[0].accountType").value(account1.getAccountType())).andExpect(jsonPath("$.[0].balance").value(account1.getBalance()))
        .andExpect(jsonPath("$.[0].bank").value(account1.getBank())).andExpect(jsonPath("$.[1].id").value(account2.getId()))
        .andExpect(jsonPath("$.[1].personId").value(account2.getPersonId())).andExpect(jsonPath("$.[1].iban").value(account2.getIban()))
        .andExpect(jsonPath("$.[1].accountType").value(account2.getAccountType())).andExpect(jsonPath("$.[1].balance").value(account2.getBalance()))
        .andExpect(jsonPath("$.[1].bank").value(account2.getBank())).andExpect(jsonPath("$.[2].id").value(account3.getId()))
        .andExpect(jsonPath("$.[2].personId").value(account3.getPersonId())).andExpect(jsonPath("$.[2].iban").value(account3.getIban()))
        .andExpect(jsonPath("$.[2].accountType").value(account3.getAccountType())).andExpect(jsonPath("$.[2].balance").value(account3.getBalance()))
        .andExpect(jsonPath("$.[2].bank").value(account3.getBank()));
  }

  @Test
  void getAccountByIdAsAdmin() throws Exception {
    given(accountDomainService.getAccountById(2L)).willReturn(account1);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/banking/v1/account/2/adm").header("authorityId", 1)).andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(account1.getId())).andExpect(jsonPath("$.personId").value(account1.getPersonId()))
        .andExpect(jsonPath("$.iban").value(account1.getIban())).andExpect(jsonPath("$.accountType").value(account1.getAccountType()))
        .andExpect(jsonPath("$.balance").value(account1.getBalance())).andExpect(jsonPath("$.bank").value(account1.getBank()));
  }

  @Test
  void getAccountByCustomerIdAsAdmin() throws Exception {
    given(accountDomainService.getAccountByPersonId(2L)).willReturn(accounts);

    this.mockMvc.perform(MockMvcRequestBuilders.get("/banking/v1/account/person/2/adm").header("authorityId", 1)).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(accounts.size()))).andExpect(jsonPath("$.[0].id").value(account1.getId()))
        .andExpect(jsonPath("$.[0].personId").value(account1.getPersonId())).andExpect(jsonPath("$.[0].iban").value(account1.getIban()))
        .andExpect(jsonPath("$.[0].accountType").value(account1.getAccountType())).andExpect(jsonPath("$.[0].balance").value(account1.getBalance()))
        .andExpect(jsonPath("$.[0].bank").value(account1.getBank())).andExpect(jsonPath("$.[1].id").value(account2.getId()))
        .andExpect(jsonPath("$.[1].personId").value(account2.getPersonId())).andExpect(jsonPath("$.[1].iban").value(account2.getIban()))
        .andExpect(jsonPath("$.[1].accountType").value(account2.getAccountType())).andExpect(jsonPath("$.[1].balance").value(account2.getBalance()))
        .andExpect(jsonPath("$.[1].bank").value(account2.getBank())).andExpect(jsonPath("$.[2].id").value(account3.getId()))
        .andExpect(jsonPath("$.[2].personId").value(account3.getPersonId())).andExpect(jsonPath("$.[2].iban").value(account3.getIban()))
        .andExpect(jsonPath("$.[2].accountType").value(account3.getAccountType())).andExpect(jsonPath("$.[2].balance").value(account3.getBalance()))
        .andExpect(jsonPath("$.[2].bank").value(account3.getBank()));
  }

  @Test
  void createAccount() throws Exception {

    String accountAsJsonString = new ObjectMapper().writeValueAsString(account3);

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post("/banking/v1/account").header("authorityId", 1).content(accountAsJsonString).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void getAccountByIdAsCustomerWithoutPermission() throws Exception {
    given(accountDomainService.getOwnedAccountById(3L, 2L)).willThrow(authorizationException);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/banking/v1/account/2").header("authorityId", 3)).andExpect(status().isForbidden());
  }

  @Test
  void getAccountByCustomerIdAsCustomerWithoutPermission() throws Exception {
    given(accountDomainService.getAccountByPersonId(2L)).willReturn(accounts);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/banking/v1/account/person/2").header("authorityId", 3)).andExpect(status().isForbidden());
  }

  @Test
  void getAccountByIdAsAdminWithoutPermission() throws Exception {
    given(accountDomainService.getAccountById(2L)).willReturn(account1);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/banking/v1/account/2/adm").header("authorityId", 2)).andExpect(status().isForbidden());
  }

  @Test
  void getAccountByAsAdminIdAsCustomerWithoutPermission() throws Exception {
    given(accountDomainService.getAccountByPersonId(2L)).willReturn(accounts);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/banking/v1/account/person/2/adm").header("authorityId", 2)).andExpect(status().isForbidden());
  }

  @Test
  void createAccountWithoutPermission() throws Exception {
    String accountAsJsonString = new ObjectMapper().writeValueAsString(account3);
    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post("/banking/v1/account").header("authorityId", 2).content(accountAsJsonString).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }
}
