package com.bravi.test.restfulapi.resources;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bravi.test.restfulapi.models.BalancedBrackets;

@RestController
public class BalancedBracketsResource {

	@PostMapping("/balanced-brackets")
	public boolean checkBracketsSequence(@RequestBody String sequence) {
		BalancedBrackets balancedBrackets = new BalancedBrackets();
		return balancedBrackets.isAValidBracketsSequence(sequence);
	}
}
