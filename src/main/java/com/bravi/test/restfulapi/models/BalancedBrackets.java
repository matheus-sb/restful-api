package com.bravi.test.restfulapi.models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.util.StringUtils;

public class BalancedBrackets {
	private static final Map<Character, Character> pairOfBrackets;
	
	static {
		pairOfBrackets = new HashMap<>();
		pairOfBrackets.put('(',')');
		pairOfBrackets.put('{','}');
		pairOfBrackets.put('[',']');
	}
	
	public boolean isAValidBracketsSequence(String sequence) {
		if (StringUtils.isEmpty(sequence)) {
			return false;
		}
		
		LinkedList<Character> startedBrackets = new LinkedList<>();
		
		for(int i = 0; i < sequence.length(); i++) {
			char b = sequence.charAt(i);
			
			if (pairOfBrackets.containsKey(b)) {
				startedBrackets.add(b);
				continue;
			}
			
			if (startedBrackets.isEmpty()) {
				return false;
			}
			
			Character lastStartedBracket = startedBrackets.getLast();
			
			if (!pairOfBrackets.get(lastStartedBracket).equals(b)) {
				return false;
			}
			
			startedBrackets.removeLast();
		}
		
		return startedBrackets.isEmpty();
	}
}
