package com.ice.chineseteacher;

// Class for Chinese Character

public class Character {
    private String unicode;
    private int resId;
    private String charName;
    private String tone;
    private String pronunciation;
    
	public Character(String string, int i, String s2, String s3, String s4) {
		unicode = string;
	    resId = i;
	    charName = s2;
	    tone = s3;
	    pronunciation = s4;
	}

}
