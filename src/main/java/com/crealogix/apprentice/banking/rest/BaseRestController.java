/**
 *  Copyright (c) 2020 by CREALOGIX AG. All rights reserved.
 */
package com.crealogix.apprentice.banking.rest;

import com.crealogix.apprentice.banking.domain.api.AccountDomainService;
import com.crealogix.apprentice.banking.domain.api.UserDomainService;
import com.crealogix.apprentice.banking.dto.Account;

public abstract class BaseRestController {

  private final UserDomainService userDomainService;
  private final AccountDomainService accountDomainService;

  public BaseRestController(UserDomainService userDomainService, AccountDomainService accountDomainService) {
    this.userDomainService = userDomainService;
    this.accountDomainService = accountDomainService;
  }

  public BaseRestController(UserDomainService userDomainService) {
    this(userDomainService, null);
  }

  protected void authorizeAdministratorRequest(Long authorityId) {
    userDomainService.authorizeAdministratorRequest(authorityId);
  }

  protected void authorizeCustomerRequest(Long authorityId) {
    userDomainService.authorizeCustomerRequest(authorityId);
  }

  protected boolean isUserAdministrator(Long authorityId){
    return userDomainService.isUserAdministrator(authorityId);
  }

  protected Account getOwnedAccountById(Long authorityId, Long accountId){
    return accountDomainService.getOwnedAccountById(authorityId, accountId);
  }
}