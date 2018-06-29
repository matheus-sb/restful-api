package com.bravi.test.restfulapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.bravi.test.restfulapi.models.Person;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestfulApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestfulApiApplicationTests {
	@LocalServerPort
	private int port;
	
	private TestRestTemplate restTemplate = new TestRestTemplate();
		
	@Test
	public void testIfBracketsSequenceIsValid() {
		String body = "[]({[]})";
		
		ResponseEntity<Boolean> response = callBalancedBrackets(body);
		
		Boolean isValid = response.getBody();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(isValid);		
	}
	
	@Test
	public void testIfBracketsSequenceIsNotValid() {		
		String body = "[]({[{])";
		
		ResponseEntity<Boolean> response = callBalancedBrackets(body);
		
		Boolean isNotValid = response.getBody();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(isNotValid);		
	}
	
	@Test
	public void testCreatePerson() {
		Person person = new Person();
		person.setName("Teste");
		
		HttpEntity<Person> entity = new HttpEntity<>(person);
		
		ResponseEntity<Void> response = restTemplate.postForEntity(
				createURLWithPort("/people"), entity, Void.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
	
	
	@Test
	public void testRetrieveAllPeople() {
		addPerson();
		
		ResponseEntity<Person[]> response = restTemplate.getForEntity(createURLWithPort("/people"), Person[].class);		
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().length > 0);
	}
	
	@Test
	public void testRetrievePerson() {
		long id = addPerson();
		String uri = String.format("/people/%d", id);
		ResponseEntity<Person> response = restTemplate.getForEntity(createURLWithPort(uri), Person.class);		
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(id, response.getBody().getId().longValue());
	}
	
	@Test
	public void testUpdatePerson() {
		long id = addPerson();
		
		Person person = new Person();
		person.setName("Teste Update");
		
		String uri = String.format("/people/%d", id);
		HttpEntity<Person> entity = new HttpEntity<>(person);
		
		ResponseEntity<Void> response = restTemplate.exchange(createURLWithPort(uri), HttpMethod.PUT, entity, Void.class);		
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void testDeletePerson() {
		long id = addPerson();
		
		String uri = String.format("/people/%d", id);
		ResponseEntity<Void> response = restTemplate.exchange(createURLWithPort(uri), HttpMethod.DELETE, null, Void.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	private long addPerson() {
		Person person = new Person();
		person.setName("Teste");
		
		HttpEntity<Person> entity = new HttpEntity<>(person);
		
		ResponseEntity<Void> response = restTemplate.postForEntity(
				createURLWithPort("/people"), entity, Void.class);
		
		String location = response.getHeaders().getLocation().toString();
		return Character.getNumericValue(location.charAt(location.length()-1));
	}
		
	private ResponseEntity<Boolean> callBalancedBrackets(String body) {
		HttpEntity<String> entity = new HttpEntity<>(body);
		
		ResponseEntity<Boolean> response = restTemplate.postForEntity(
				createURLWithPort("/balanced-brackets"), entity, Boolean.class);
		
		return response;
	}
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}
