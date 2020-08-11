/**
 *  Copyright (c) 2020 by CREALOGIX AG. All rights reserved.
 */
package com.crealogix.apprentice.banking.domain.user;

import com.crealogix.apprentice.banking.util.exception.AuthorizationException;

import java.util.Optional;

import org.springframework.stereotype.Component;

/***
 * Implementation of Service UserContextHolder. Sets and Gets UserContext.
 */
@Component
public class UserContextHolderImpl implements UserContextHolder {

  private static ThreadLocal<UserContext> userContextThreadLocal = new InheritableThreadLocal<>();

  @Override
  public void setUserContext(UserContext userContext) {
    userContextThreadLocal.set(userContext);
  }

  @Override
  public UserContext getUserContext() {
    return getUserContextOpt().orElseThrow(() -> new AuthorizationException("No UserContext set."));
  }

  private Optional<UserContext> getUserContextOpt() {
    return Optional.ofNullable(userContextThreadLocal.get());
  }
}