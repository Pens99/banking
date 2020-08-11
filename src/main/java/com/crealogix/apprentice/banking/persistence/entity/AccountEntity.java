package com.crealogix.apprentice.banking.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "account")
@SequenceGenerator(name = BaseEntity.GENERATOR_NAME, sequenceName = "seq_person_id", initialValue = 1000)
public class AccountEntity extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinColumn(name = "person_id", referencedColumnName = "id")
  private PersonEntity person;

  @Column(name = "iban")
  private String iban;

  @Column(name = "account_type")
  private String account_type;

  @Column(name = "balance")
  private double balance;

  @Column(name = "bank")
  private String bank;

  public AccountEntity() {
    // Needed by Hibernate
  }

  public AccountEntity(String iban, String account_type, PersonEntity person, double balance, String bank) {
    this.iban = iban;
    this.account_type = account_type;
    this.person = person;
    this. balance = balance;
    this.bank = bank;
  }

  public PersonEntity getPerson() {
    return person;
  }

  public void setPerson(PersonEntity person) {
    this.person = person;
  }

  public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public String getAccount_type() {
    return account_type;
  }

  public void setAccount_type(String account_type) {
    this.account_type = account_type;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public String getBank() {
    return bank;
  }

  public void setBank(String bank) {
    this.bank = bank;
  }
}
