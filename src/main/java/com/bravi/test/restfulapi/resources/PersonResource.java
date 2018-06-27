package com.bravi.test.restfulapi.resources;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bravi.test.restfulapi.models.Person;
import com.bravi.test.restfulapi.repositories.PersonRepository;

@RestController
public class PersonResource {

	@Autowired
	private PersonRepository personRepository;
	
	@GetMapping("/people")
	public List<Person> retrieveAllPeople() {
		return personRepository.findAll();
	}
	
	@GetMapping("/people/{id}")
	public ResponseEntity<Person> retrievePerson(@PathVariable Long id) {
		Optional<Person> person = personRepository.findById(id);
		
		if (!person.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(person.get());
	}
	
	@PostMapping("/people")
	public ResponseEntity<Object> createPerson(@Valid @RequestBody Person person) {
		Person savedPerson = personRepository.save(person);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedPerson.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/people/{id}")
	public ResponseEntity<Object> updatePerson(@PathVariable Long id, @Valid @RequestBody Person updatedPerson) {
		Optional<Person> optional = personRepository.findById(id);
		
		if (!optional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Person person = optional.get();		
		person.update(updatedPerson);
		personRepository.save(person);
		
		return ResponseEntity.ok().build();
	}	
	
	@DeleteMapping("/people/{id}")
	public void deletePerson(@PathVariable Long id) {
		personRepository.deleteById(id);
	}
}
