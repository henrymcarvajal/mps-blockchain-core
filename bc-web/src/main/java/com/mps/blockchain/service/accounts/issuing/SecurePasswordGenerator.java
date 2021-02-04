package com.mps.blockchain.service.accounts.issuing;

import java.security.SecureRandom;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

public class SecurePasswordGenerator {
	
	private static final String ERROR_CODE = "INSUFICIENT_SPANISH";	
	private static final String SPANISH_CHARACTERS = "áéíóúüñAÉÍÓÚÜÑ";
	private static int MIN_PASSWORD_SIZE = 33;
	private static int MAX_PASSWORD_SIZE = 65;
	
	public String generatePassword() {
		int passwordSize = MIN_PASSWORD_SIZE + new SecureRandom().nextInt(MAX_PASSWORD_SIZE - MIN_PASSWORD_SIZE);

	    CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
	    CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
	    lowerCaseRule.setNumberOfCharacters(2);
	 
	    CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
	    CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
	    upperCaseRule.setNumberOfCharacters(2);
	 
	    CharacterData digitChars = EnglishCharacterData.Digit;
	    CharacterRule digitRule = new CharacterRule(digitChars);
	    digitRule.setNumberOfCharacters(2);
	 
	    CharacterData specialChars = EnglishCharacterData.Special;
	    CharacterRule splCharRule = new CharacterRule(specialChars);
	    splCharRule.setNumberOfCharacters(2);
	    
	    CharacterData spanishChars = new CharacterData() {
	        public String getErrorCode() {
	            return ERROR_CODE;
	        }
	 
	        public String getCharacters() {
	            return SPANISH_CHARACTERS;
	        }
	    };
	    CharacterRule spanishCharRule = new CharacterRule(spanishChars);
	    spanishCharRule.setNumberOfCharacters(2);
	 
	    PasswordGenerator gen = new PasswordGenerator();
	    String password = gen.generatePassword(passwordSize, splCharRule, lowerCaseRule, 
	      upperCaseRule, digitRule);
	    return password;
	}
}
