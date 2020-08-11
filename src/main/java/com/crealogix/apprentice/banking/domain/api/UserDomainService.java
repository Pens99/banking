/**
 *  Copyright (c) 2020 by CREALOGIX AG. All rights reserved.
 */
package com.crealogix.apprentice.banking.domain.api;

public interface UserDomainService {

  void authorizeAdministratorRequest(Long authorityId);

  void authorizeCustomerRequest(Long authorityId);

  boolean isUserAdministrator(Long authorityId);
}