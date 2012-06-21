package cobolConversion;

/**
 Copyright Jean-Yves MENGANT 1999

 this program is Free Software , you can distribute it or modify it
 under the term of the GNU General Public Licence as published
 by the Free Software Foundation providing you allways provide
 this copyright header.

 This program is distributed in the hope that it will be usefull but
 WITHOUT ANY WARRANTY; without event the implied warranty of MERCHANTABILITY
 or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public Licence for
 more details.


 The class bellow Implements a new EBCDIC string data type

 EbcdicString class EXTENDS DataStructure

 // Construct object using given Byte Array
 public EbcdicString ( byte GivenBuffer[] ,
 int  BufferPos     , // Starting Position
 int  CurSize
 )
 // Convert to ebcdic into _EbcdicBuffer
 // if origin is larger than dest , exception is thrown
 public void toEbcdic ( String InStr )
 throws ArrayIndexOutOfBoundsException

 // Convert into JavaStr and return the value
 public String toString()

 @author Jean-Yves MENGANT

 */
import java.io.*;

public class EbcdicString extends DataStructure {

	// Instance private data
	private byte[] _AsciiBuffer; // Conversion Ascii buffer
	private EbcdicTable _conversionTable; // provided by OS constructor

	/* /////////////////////////////////////////////////////////////// */
	/* PUBLIC STARTS HERE */
	/* /////////////////////////////////////////////////////////////// */

	/**
	 * Construct object using given Byte Array
	 */
	public EbcdicString(EbcdicTable conversionTable, byte GivenBuffer[],
			int BufferPos, // Starting Position
			int CurSize) {
		super(GivenBuffer, BufferPos, CurSize);
		_conversionTable = conversionTable;
	}

	public EbcdicString(EbcdicTable conversionTable, byte GivenBuffer[]) {
		super(GivenBuffer, 0, GivenBuffer.length);
		_conversionTable = conversionTable;
	}

	private byte FromEbcdic(int Ii) {
		int Pos = GiveElem(Ii);
		int tEbcdic2Ascii[] = _conversionTable.get_tEbcdic2Ascii();

		if (Pos < 0)
			return ((byte) (tEbcdic2Ascii[256 + Pos]));
		else
			return ((byte) (tEbcdic2Ascii[Pos]));
	}

	/**
	 * Convert to ebcdic into _EbcdicBuffer if origin is larger than dest ,
	 * exception is thrown
	 */
	public void toEbcdic(String InStr) throws ArrayIndexOutOfBoundsException {
		int CopySize = InStr.length();
		// First Copy In Place
		if (_Size < CopySize)
			throw new ArrayIndexOutOfBoundsException("Input String too big");

		try {
			byte wkByte[] = InStr.getBytes(ISO_LATIN_1);
			System.arraycopy(wkByte, 0, _Struct, _Offset, CopySize);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int tAscii2Ebcdic[] = _conversionTable.get_tAscii2Ebcdic();

		for (int Ii = 0; Ii < CopySize; Ii++)
			SetElem(Ii, (byte) (tAscii2Ebcdic[GiveElem(Ii)]));

		byte BlankChar = (byte) (tAscii2Ebcdic[(byte) (' ')]);

		for (int Ii = CopySize; Ii < _Size; Ii++)
			SetElem(Ii, BlankChar);
	}

	/**
	 * Convert into JavaStr and return the value
	 */
	public String toString() {
		_AsciiBuffer = new byte[_Size];

		for (int Ii = 0; Ii < (_Size - 1); Ii++)
			_AsciiBuffer[Ii] = FromEbcdic(Ii);

		// Return a String
		return (new String(_AsciiBuffer, 0, _Size - 1));
	}

	/**
	 * use this method just to debug or as a sample implementation of the
	 * EbcdicString class
	 */
	public static void main(String Argv[]) {
		byte myBuffer[] = new byte[50]; // PIC X(50) cobol DATA with trailing
										// blanks

		EbcdicString myEbcdicString = new EbcdicString(new EbcdicTable(),
				myBuffer, 0, myBuffer.length);
		/* convert to EBCDIC and store converted string into internal Array */
		myEbcdicString.toEbcdic("Hello World");
		String myString = myEbcdicString.toString();
		/*
		 * convert back to String "Hello World should be displayed padded with
		 * blanks (just call java trim() method when you need to remove COBOL
		 * trailing blanks
		 */
		System.out.println(myString);

	}
}
