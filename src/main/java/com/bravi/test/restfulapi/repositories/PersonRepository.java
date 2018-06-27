package com.bravi.test.restfulapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bravi.test.restfulapi.models.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
