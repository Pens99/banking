/**
 *  Copyright (c) 2020 by CREALOGIX AG. All rights reserved.
 */
package com.crealogix.apprentice.banking.domain.user;

/***
 * Has connection with Person. Adds Information about Authority to it.
 */
public class UserContext {

  private final Long authorityId;

  private final boolean administrator;

  private final Long personId;

  public UserContext(Long authorityId, boolean administrator, Long personId) {
    this.authorityId = authorityId;
    this.administrator = administrator;
    this.personId = personId;
  }

  public Long getAuthorityId() {
    return authorityId;
  }

  public boolean isAdministrator() {
    return administrator;
  }

  public Long getPersonId() {
    return personId;
  }
}