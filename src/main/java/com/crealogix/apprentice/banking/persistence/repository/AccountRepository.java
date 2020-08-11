package com.crealogix.apprentice.banking.persistence.repository;

import com.crealogix.apprentice.banking.dto.Account;
import com.crealogix.apprentice.banking.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

  @Transactional
  @Query(value = "Select * FROM account WHERE person_id = :authorityId", nativeQuery = true)
  List<AccountEntity> findAccountByPerson(@Param("authorityId") Long authorityId);

  @Transactional
  @Query(value = "Select * FROM account WHERE id = :accountId AND person_id = :authorityId", nativeQuery = true)
  Optional<AccountEntity> findAccountById(@Param("authorityId") Long authorityId, @Param("accountId") Long accountId);

}
