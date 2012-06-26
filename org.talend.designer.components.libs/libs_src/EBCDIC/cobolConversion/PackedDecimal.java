package cobolConversion;

/**
 * Copyright Jean-Yves MENGANT 1999
 * 
 * this program is Free Software , you can distribute it or modify it under the
 * term of the GNU General Public Licence as published by the Free Software
 * Foundation providing you allways provide this copyright header.
 * 
 * This program is distributed in the hope that it will be usefull but WITHOUT
 * ANY WARRANTY; without event the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public Licence for more
 * details.
 * 
 * e-mail : jymengant@directprovider.net
 * 
 * The class bellow deals with PACKED DECIMAL conversions A portable conversions
 * 
 * Only the final PackedDecimal class should be used other class are for
 * internal usage only
 * 
 * NumberFormatException may be Thrown by this class on invalid packed decimal
 * format
 * 
 * // Construct using an existing byte array(Supose to point on // a valid
 * packed decimal format or to be translated later // with toPackedDecimal
 * method public PackedDecimal( byte Packed_Origin[] , int Offset , int IntSize
 * , int DecSize )
 * 
 * // Build a packed decimal from a string number public void toPackedDecimal(
 * String Number )
 * 
 * // Translate current Packed into a String representation public String
 * toString()
 * 
 * @author Jean-Yves MENGANT
 */

/* Object used for increment / decrement passed to methods */

class Decimal_Increment {

	private int _Inc;

	// Public methods
	public void SetValue(int Value) {
		_Inc = Value;
	}

	public int Increment() {
		_Inc++;
		return (_Inc - 1); // Return value before increment
	}

	public int Decrement() {
		_Inc--;
		return (_Inc + 1); // Return value after increment
	}

	public int GetValue() {
		return _Inc;
	}
}

class DecimalString {

	private final static byte MAX_DECIMAL_SIZE = 20; // Max size of a decimal
														// num

	private char _IntPart[];
	private int _IntSize;
	private boolean _Negative_Number;
	private char _DecPart[];
	private int _DecSize;

	/* Public method on WORK decimal class starts here */

	public DecimalString(String DecString) throws NumberFormatException {
		boolean Decimal_Part = false;
		byte Ii = 0;
		int Len = DecString.length();
		int Decimal_Pos = 0;

		_DecPart = new char[MAX_DECIMAL_SIZE];
		_IntPart = new char[MAX_DECIMAL_SIZE];
		_Negative_Number = false;

		while (Ii < Len) {
			char CurChar = DecString.charAt(Ii);

			switch (CurChar) {
			case '+':
				if (Ii != 0)
					throw new NumberFormatException("Invalid + sign position");
				break;

			case '-':
				if (Ii != 0)
					throw new NumberFormatException("Invalid - sign position");
				else
					_Negative_Number = true;
				break;

			case '.':
				Decimal_Part = true;
				Decimal_Pos = 0;
				break;

			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				if (Decimal_Part) {
					_DecPart[Decimal_Pos++] = CurChar;
					_DecSize++;
				} else {
					_IntPart[_IntSize] = CurChar;
					_IntSize++;
				}
				break;

			default:
				throw new NumberFormatException(
						"PackedDecimal.DecimalString : unexpected character received");
			}
			Ii++;
		}

	}

	// Accessors
	public boolean IsNegative() {
		return _Negative_Number;
	}

	public int GiveIntSize() {
		return _IntSize;
	}

	/**
	 * Returns Digit corresponding to Pos and decrement Pos
	 */
	public byte DecDigit(Decimal_Increment Pos) {
		if (Pos.GetValue() > _DecSize) {
			Pos.Decrement();
			return 0;
		} else {
			Pos.Decrement();
			return BYTEFX.MAKEBYTE(
					BYTEFX.LOWNIBBLE((byte) (_DecPart[Pos.GetValue()])),
					(byte) (0));
		}
	}

	/**
	 * @return Digit corresponding to Pos in integer part
	 */
	public byte IntDigit(Decimal_Increment Pos) {
		Pos.Decrement();
		return BYTEFX.MAKEBYTE(
				BYTEFX.LOWNIBBLE((byte) (_IntPart[Pos.GetValue()])),
				BYTEFX.LOWNIBBLE((byte) (0)));
	}
};

public class PackedDecimal extends DataStructure {

	private static final byte DEFAULT_NEGATIVE = 0x0d;
	private static final byte DEFAULT_POSITIVE = 0x0c;

	private static final byte ASCII_HIGH_CHAR_NIBBLE = (byte) (0x30);
	private static final int MAX_DECIMAL_STRING_SIZE = 100;
	private static final byte CBL_SIGN_POS1 = (byte) (0x0A);
	private static final byte CBL_SIGN_POS2 = (byte) (0x0C);
	private static final byte CBL_SIGN_POS3 = (byte) (0x0E);
	private static final byte CBL_SIGN_POS4 = (byte) (0x0F);
	/* Consider unsigned as positive !!!! */

	private byte _PackedWk[]; // Use this as private internal work
	private int _IntSize; // Total number of non decimal digits
	private int _DecSize; // total number of decimal digits
	private int _Logical_Dest_Size; // Total logical size

	// Public access starts here

	/**
	 * giving int Size + dec size returns physical size necessary to store the
	 * packed decimal data
	 */
	public static int getPackedSize(int intSize, int decSize) {
		int Logical_Dest_Size = intSize + decSize;

		if (Logical_Dest_Size % 2 == 0)
			return (Logical_Dest_Size / 2) + 1;
		else
			return (Logical_Dest_Size + 1) / 2;
	}

	/**
	 * Construct using an existing byte array(Supose to point on a valid packed
	 * decimal format or to be translated later with toPackedDecimal method
	 * 
	 * @param Packed_Origin
	 *            byte array containing COBOL packed decimal data
	 * @param Offset
	 *            offset of packed decimal data in array
	 * @param IntSize
	 *            non decimal part size
	 * @param DecSize
	 *            decimal part size
	 */
	public PackedDecimal(byte Packed_Origin[], int Offset, int IntSize,
			int DecSize) {
		super(Packed_Origin, Offset, 0);

		_IntSize = IntSize;
		_DecSize = DecSize;
		_Logical_Dest_Size = IntSize + DecSize;
		_Size = getPackedSize(IntSize, DecSize);
	}

	/**
	 * Build a packed decimal from a string number
	 * 
	 * Convert the in String +-99999.99 in an packed decimal IBM data Flow ->
	 * Each digit is a 0..9 Numerical value last digit is the sign digit :
	 * A|C|E|F => + ; B|D => - ; the decimal point is virtual its position is
	 * defined in the second byte of dec_len
	 * 
	 * @param Number
	 *            decimal String representation to be converted
	 */
	public void toPackedDecimal(String Number) throws NumberFormatException {
		Decimal_Increment Ii = new Decimal_Increment();
		Decimal_Increment Jj = new Decimal_Increment();

		boolean Decimal_Part = false;
		int Physical_Dest_Size;
		byte Digit;
		int OutPos;
		boolean High = true;
		int CurIntSize = 0;

		// Build Local Wk Decimal String
		DecimalString Wk = new DecimalString(Number);
		_PackedWk = new byte[_Size]; // Allocate working

		OutPos = _Size - 1;

		if (Wk.IsNegative())
			_PackedWk[OutPos] = DEFAULT_NEGATIVE;
		else
			_PackedWk[OutPos] = DEFAULT_POSITIVE;

		Ii.SetValue(_DecSize);
		Jj.SetValue(Wk.GiveIntSize());

		if (Ii.GetValue() != 0)
			Decimal_Part = true;

		while ((OutPos != -1) && (Jj.GetValue() != 0)) {
			if (Decimal_Part) {
				Digit = Wk.DecDigit(Ii);

				if (Ii.GetValue() == 0)
					Decimal_Part = false;
			} else
				Digit = Wk.IntDigit(Jj);

			if (High) {
				_PackedWk[OutPos] = BYTEFX.MAKEBYTE(
						BYTEFX.LOWNIBBLE(_PackedWk[OutPos]),
						BYTEFX.LOWNIBBLE(Digit));
				OutPos--;
			} else
				_PackedWk[OutPos] = BYTEFX.MAKEBYTE(BYTEFX.LOWNIBBLE(Digit),
						BYTEFX.LOWNIBBLE(_PackedWk[OutPos]));

			High = !High; // Switch to Next Digit
		}

		// Move Working to super Data Structure
		System.arraycopy(_PackedWk, 0, _Struct, _Offset, _Size);
	}

	/**
	 * Translate current Packed into a String representation
	 * 
	 * @return String representation of packed decimal
	 */
	public String toString() {
		int Ii = 0;
		int Sign_Location;
		int Logical_Length;
		int IOut = 0; /* position OutStrDecimal */
		int IIn = 0; /* Position in InDecimal */
		boolean _End = false;/* =TRUE when number has been proceed */
		/* =FALSE else */
		boolean High = false;/* =TRUE when on high nibble */
		/* =FALSE when on low nibble */
		/* of pIndecimal PACKED STRING */
		boolean Significant = false; /* Bypass leading non significant 0 */
		byte Digit; /* extracted pIndecimal[IIn] */
		int NbDigit; /* number of proceed digit */
		boolean EvenNbDigits; /* True if number of digits in Dec number is even */
		boolean InDecimalPart = false;
		String Sign = new String("");

		/* Logical Data Length */
		Logical_Length = _IntSize + _DecSize; /* Len = Number of digits + Sign */
		EvenNbDigits = ((Logical_Length % 2) == 0);
		Logical_Length++;
		Logical_Length += 2; /* Reserve space for '0.' if needed */

		if (_DecSize != 0)
			InDecimalPart = true;

		Sign_Location = IOut; /* Save Sign Position */
		IOut++;

		char OutStrDecimal[] = new char[MAX_DECIMAL_STRING_SIZE];

		if (_IntSize == 0) {
			OutStrDecimal[IOut++] = '0';
			OutStrDecimal[IOut++] = '.';
		}

		NbDigit = 0;

		if (EvenNbDigits)
			High = true; /* BYPASS High padding nibble */

		while (!_End) {
			High = !High;

			if (High) {
				Digit = BYTEFX.HINIBBLE(_Struct[IIn]);
			} else {
				Digit = BYTEFX.LOWNIBBLE(_Struct[IIn]);
				IIn++;
			}

			NbDigit++;
			switch (Digit) {
			case CBL_SIGN_POS1:
			case CBL_SIGN_POS2:
			case CBL_SIGN_POS3:
			case CBL_SIGN_POS4:
				// Starting a positive number by '+' leads to
				// number format exceptions when building BigDecimal from
				// decimal string number so no leading sign is implicit '+'
				_End = true;
				break;

			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				if ((Digit != 0) || (Significant) || (NbDigit -1 == _IntSize)) {
					Significant = true; /*
										 * Anyway all incoming 0 will be
										 * significant
										 */
					OutStrDecimal[IOut++] = (char) (BYTEFX.MAKEBYTE(Digit,
							BYTEFX.HINIBBLE(ASCII_HIGH_CHAR_NIBBLE)));
					if ((NbDigit == _IntSize) && (_DecSize != 0)) {
						OutStrDecimal[IOut++] = '.';
						InDecimalPart = false;
					}
				}
				break;

			default:
				Sign = new String("-"); /* Set Negative */
				_End = true;
				break;

			} /* switch */
		} /* while */

		// A ZERO VALUE packed Decimal
		if (!Significant)
			return (new String("0"));

		if (InDecimalPart) // 0.9999 case with non null integer part defined
							// in cobol pic 9(9)V99
			return (Sign + (new String("0.")) + (new String(OutStrDecimal))
					.trim());
		else {
			if (Sign.length() != 0) { // Should be a negative number with
										// leading '-'
				OutStrDecimal[Sign_Location] = Sign.charAt(0);
				return (new String(OutStrDecimal));
			} else
				// Positive number with leading blank
				return (new String(OutStrDecimal)).trim();
		}
	}

	/**
	 * Just use the main method for Class unit testing
	 */
	public static void main(String Argv[]) {

		try {
			PackedDecimal MyPacked = new PackedDecimal(new byte[10], 0, 10, 2);
			MyPacked.toPackedDecimal("0.3542");
			System.out.println(" Decimal value is : " + MyPacked.toString());

			PackedDecimal MyPacked1 = new PackedDecimal(new byte[10], 0, 3, 3);
			MyPacked1.toPackedDecimal("1543.2545");
			System.out.println(" Decimal value is : " + MyPacked1.toString());

			PackedDecimal MyPacked2 = new PackedDecimal(new byte[10], 0, 10, 2);
			MyPacked2.toPackedDecimal("3.2");
			System.out.println(" Decimal value is : " + MyPacked2.toString());

			long StartTime = System.currentTimeMillis();

			for (int Ii = 0; Ii < 1000; Ii++)
				;
			{
				MyPacked = new PackedDecimal(new byte[10], 0, 10, 2);
				MyPacked.toPackedDecimal("41200000000");//get the last 10 char,'4' is ignored,only get 12000000000 
			}
			long Elapsed = System.currentTimeMillis() - StartTime;

			System.out.println(" Decimal value is : " + MyPacked.toString());

			System.out.println("Loop Elapsed in : " + Elapsed);

		} catch (NumberFormatException e) {
			System.out.println(" Error : " + e);
		}

	}

}
