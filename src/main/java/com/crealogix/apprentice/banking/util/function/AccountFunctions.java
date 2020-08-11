package com.crealogix.apprentice.banking.util.function;

import com.crealogix.apprentice.banking.domain.api.PersonDomainService;
import com.crealogix.apprentice.banking.domain.person.PersonDomainServiceImpl;
import com.crealogix.apprentice.banking.dto.Account;
import com.crealogix.apprentice.banking.dto.Person;
import com.crealogix.apprentice.banking.persistence.entity.AccountEntity;
import com.crealogix.apprentice.banking.persistence.entity.PersonEntity;
import com.crealogix.apprentice.banking.persistence.repository.PersonRepository;
import com.crealogix.apprentice.banking.util.exception.ObjectNotCreatedException;
import com.crealogix.apprentice.banking.util.exception.ValidationException;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.function.Function;

public interface AccountFunctions {

  Function<AccountEntity, Account> mapToDto = ce -> new Account(//
      ce.getId(), //
      ce.getPerson().getId(), //
      ce.getIban(), //
      ce.getAccount_type(), //
      ce.getBank(), ce.getBalance()//
  );


  static void isValid(Account account) {
    if (account.getId() != null || StringUtils.isBlank(account.getIban()) || StringUtils.isBlank(account.getAccountType())
        || StringUtils.isBlank(account.getBank())) {
      throw new ValidationException("Not valid " + account + ".");
    }
  }

}
