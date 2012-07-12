package cobolConversion;

public enum CharSetEnum {
	// define data table for using
	Cp037("Cp037", "{ABCDEFGHI", "}JKLMNOPQR"), 
	Cp1141("Cp1141", "äABCDEFGHI", "üJKLMNOPQR");

	String charSet;
	String failoverpositives;
	String failovernegatives;

	//default construct 
	CharSetEnum(String charSet, String failoverpositives,
			String failovernegatives) {
		this.charSet = charSet;
		this.failoverpositives = failoverpositives;
		this.failovernegatives = failovernegatives;
	}

	public String getCharSet() {
		return charSet;
	}

	public static void main(String[] a) {
		String charset = "Cp037";
		for (CharSetEnum charsetEnum : CharSetEnum.values()) {
			if (charset.equals(charsetEnum.getCharSet())) {
				System.out.println(charsetEnum.failoverpositives);
				System.out.println(charsetEnum.failovernegatives);
			}
		}
	}
}
