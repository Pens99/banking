package com.crealogix.apprentice.banking.dto;

import com.crealogix.apprentice.banking.persistence.entity.PersonEntity;

import java.util.Objects;


public class Account {

  private final Long id;

  private Long personId;

  private final String iban;

  private final String accountType;

  private final String bank;

  private final double balance;

  public Account() {
    this(null, null, null, null, null, 0);
  }

  public Account(Long id, Long personId, String iban, String accountType, String bank, double balance) {
    this.id = id;
    this.personId = personId;
    this.iban = iban;
    this.accountType = accountType;
    this.bank = bank;
    this.balance = balance;
  }

  public Long getId() {
    return id;
  }

  public Long getPersonId() {
    return personId;
  }

  public String getIban() {
    return iban;
  }

  public String getAccountType() {
    return accountType;
  }

  public String getBank() {
    return bank;
  }

  public double getBalance() {
    return balance;
  }

  @Override
  public String toString() {
    return "Account [id=" + id + ", personId=" + personId + ", iban=" + iban + ", accountType=" + accountType + ", balance=" + balance + ", bank=" + bank + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(iban, id, accountType);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof Account))
      return false;
    Account other = (Account) obj;
    return Objects.equals(iban, other.iban) && Objects.equals(id, other.id) && Objects.equals(accountType, other.accountType);
  }
}
