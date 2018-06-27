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

import com.bravi.test.restfulapi.models.Contact;
import com.bravi.test.restfulapi.repositories.ContactRepository;

@RestController
public class ContactResource {

	@Autowired
	private ContactRepository contactRepository;
	
	@GetMapping("/contacts")
	public List<Contact> retrieveAllContacts() {
		return contactRepository.findAll();
	}
	
	@GetMapping("/contacts/{id}")
	public ResponseEntity<Contact> retrieveContact(@PathVariable Long id) {
		Optional<Contact> contact = contactRepository.findById(id);
		
		if (!contact.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(contact.get());
	}
	
	@PostMapping("/contacts")
	public ResponseEntity<Object> createContact(@Valid @RequestBody Contact contact) {
		Contact savedContact = contactRepository.save(contact);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedContact.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/contacts/{id}")
	public ResponseEntity<Object> updateContact(@PathVariable Long id, @Valid @RequestBody Contact updatedContact) {
		Optional<Contact> optional = contactRepository.findById(id);
		
		if (!optional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Contact contact = optional.get();		
		contact.update(updatedContact);
		contactRepository.save(contact);
		
		return ResponseEntity.ok().build();
	}	
	
	@DeleteMapping("/contacts/{id}")
	public void deleteContact(@PathVariable Long id) {
		contactRepository.deleteById(id);
	}
}
