package com.crealogix.apprentice.banking.rest;

import com.crealogix.apprentice.banking.domain.api.AccountDomainService;
import com.crealogix.apprentice.banking.domain.api.UserDomainService;
import com.crealogix.apprentice.banking.dto.Account;
import com.sun.istack.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = RestControllerMappings.BANKING_REST + RestControllerMappings.ACCOUNT_ENDPOINT)
public class AccountRestController extends BaseRestController {

  private final AccountDomainService accountDomainService;

  public AccountRestController(UserDomainService userDomainService, AccountDomainService accountDomainService) {
    super(userDomainService, accountDomainService);
    this.accountDomainService = accountDomainService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody ResponseEntity<List<Account>> getAccounts(@NotNull @RequestHeader("authorityId") Long authorityId) {
    authorizeCustomerRequest(authorityId);
    List<Account> accounts = accountDomainService.getAccounts();
    return new ResponseEntity<>(accounts, HttpStatus.OK);
  }

  @GetMapping(value = "{accountId}/adm", produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody ResponseEntity<Account> getAccountByIdAsAdmin(@NotNull @RequestHeader("authorityId") Long authorityId,
      @NotNull @PathVariable("accountId") Long accountId) {
    authorizeAdministratorRequest(authorityId);
    Account account = accountDomainService.getAccountById(accountId);
    return new ResponseEntity<>(account, HttpStatus.OK);
  }

  @GetMapping(value = "{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody ResponseEntity<Account> getAccountByIdAsCustomer(@NotNull @RequestHeader("authorityId") Long authorityId,
      @NotNull @PathVariable("accountId") Long accountId) {
    authorizeCustomerRequest(authorityId);
      Account account = getOwnedAccountById(authorityId, accountId);
      return new ResponseEntity<>(account, HttpStatus.OK);

  }

  @GetMapping(value = "person/{customerId}/adm", produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody ResponseEntity<List<Account>> getAccountsByCustomerIdAsAdmin(@NotNull @RequestHeader("authorityId") Long authorityId,
      @NotNull @PathVariable("customerId") Long customerId) {
      authorizeAdministratorRequest(authorityId);
      List<Account> accounts = accountDomainService.getAccountByPersonId(customerId);
      return new ResponseEntity<>(accounts, HttpStatus.OK);
  }

  @GetMapping(value = "person/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody ResponseEntity<List<Account>> getAccountsByCustomerIdAsCustomer(@NotNull @RequestHeader("authorityId") Long authorityId,
      @NotNull @PathVariable("customerId") Long customerId) {
      authorizeCustomerRequest(authorityId);
      if (customerId == authorityId) {
        List<Account> accounts = accountDomainService.getAccountByPersonId(customerId);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
      }else{
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
      }
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createAccount(@NotNull @RequestHeader("authorityId") Long authorityId, @RequestBody Account account) {
    authorizeAdministratorRequest(authorityId);
    accountDomainService.createAccount(account);
    return new ResponseEntity<Object>("Account created.", HttpStatus.OK);
  }
}
