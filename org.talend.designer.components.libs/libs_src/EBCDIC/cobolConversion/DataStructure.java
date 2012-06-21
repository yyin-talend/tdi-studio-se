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
 * 
 * The class below implements data containers facilities for heterogenous data
 * types embedded into byte arrays
 * 
 * // Constructor public DataStructure( byte Origin[] , int Offset , int Size )
 * // CopyBack public void CopyFrom( byte Origin[] , int Size ) // Copy To
 * public void CopyTo( byte Dest[] , int Size ) // Byte Accessor public int
 * GiveElem( int Ii ) // Is there a data container defined public boolean
 * ContainerExists()
 * 
 * @author Jean-Yves MENGANT
 */

public class DataStructure {

	// Define the UNICODE Iso_Latin1 encoding identifier
	public final static String ISO_LATIN_1 = "8859_1";

	protected byte _Struct[]; // Origin of data structure
	protected int _Offset; // offset of child data
	protected int _Size; // Size in byte starting at _Offset

	// Public access starts here

	// Construct using an existing byte array(Supose to point on
	// a valid packed decimal format
	public DataStructure(byte Origin[], int Offset, int Size) {
		_Struct = Origin; // Point On user array
		_Size = Size;
		_Offset = Offset;
	}

	// CopyBack
	public void CopyFrom(byte Origin[], int Size) {
		if (_Struct != null)
			System.arraycopy(Origin, 0, _Struct, _Offset, Size);
	}

	// Copy To
	public void CopyTo(byte Dest[], int Size) {
		if (_Struct != null)
			System.arraycopy(_Struct, _Offset, Dest, 0, Size);
	}

	public byte[] getByteValueArray() {
		return _Struct;
	}

	// Byte Accessor
	public int GiveElem(int Ii) {
		return (_Struct[Ii + _Offset]);
	}

	// Byte Actuator ( set element Ii to byte Value )
	public void SetElem(int Ii, byte Value) {
		_Struct[Ii + _Offset] = Value;
	}

	// Is there a data container defined
	public boolean ContainerExists() {
		return (_Struct != null);
	}

	// Just use the main method for Class unit testing
	public static void main(String Argv[]) {

	}

}
