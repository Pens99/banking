/**
 *  Copyright (c) 2020 by CREALOGIX AG. All rights reserved.
 */
package com.crealogix.apprentice.banking.domain.user;

import com.crealogix.apprentice.banking.domain.api.UserDomainService;
import com.crealogix.apprentice.banking.persistence.entity.UserEntity;
import com.crealogix.apprentice.banking.persistence.repository.UserRepository;
import com.crealogix.apprentice.banking.util.exception.AuthorizationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserDomainServiceImpl implements UserDomainService {

  private static final Logger LOG = LoggerFactory.getLogger(UserDomainServiceImpl.class);

  private final UserRepository userRepository;

  private final UserContextHolder userContextHolder;

  public UserDomainServiceImpl(UserRepository userRepository, UserContextHolderImpl userContextHolder) {
    this.userRepository = userRepository;
    this.userContextHolder = userContextHolder;
  }

  @Override
  public void authorizeAdministratorRequest(Long authorityId) {
    UserEntity userEntity = getUser(authorityId);
    if (!userEntity.isAdministrator()) {
      throw new AuthorizationException("User is not administrator.");
    }
    setUserContext(userEntity);
  }

  @Override
  public void authorizeCustomerRequest(Long authorityId) {
    setUserContext(getUser(authorityId));
  }

  private UserEntity getUser(Long authorityId) {
    return userRepository.findById(authorityId).orElseThrow(() -> new AuthorizationException("No user found for authority id" + authorityId + "."));
  }

  @Override
  public boolean isUserAdministrator(Long authorityId){
    UserEntity userEntity = getUser(authorityId);
    if (!userEntity.isAdministrator()) {
      return false;
    }else {
      setUserContext(userEntity);
      return true;
    }
  }

  private void setUserContext(UserEntity userEntity) {
    userContextHolder.setUserContext(new UserContext(userEntity.getId(), userEntity.isAdministrator(), userEntity.getPerson().getId()));
    LOG.info("Request from {}", userEntity);
  }
}