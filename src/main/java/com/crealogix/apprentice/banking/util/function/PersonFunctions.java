/**
 *  Copyright (c) 2020 by CREALOGIX AG. All rights reserved.
 */
package com.crealogix.apprentice.banking.util.function;

import com.crealogix.apprentice.banking.dto.Person;
import com.crealogix.apprentice.banking.persistence.entity.PersonEntity;
import com.crealogix.apprentice.banking.util.exception.ValidationException;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

/***
 * This is the Mapper of the Entities to the DTO's or otherwise.
 */
public interface PersonFunctions {

  Function<PersonEntity, Person> mapToDto = ce -> new Person(//
      ce.getId(), //
      ce.getName(), //
      ce.getFirstName()//
  );

  Function<Person, PersonEntity> mapToEntity = c -> new PersonEntity(//
      c.getName(), //
      c.getFirstName()//
  );

  static void isValid(Person person) {
    if (person.getId() != null || StringUtils.isBlank(person.getName()) || StringUtils.isBlank(person.getFirstName())) {
      throw new ValidationException("Not valid " + person + ".");
    }
  }
}