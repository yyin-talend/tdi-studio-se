package cobolConversion;

/*
 Copyright Jean-Yves MENGANT 1999

 this program is Free Software , you can distribute it or modify it
 under the term of the GNU General Public Licence as published
 by the Free Software Foundation providing you allways provide
 this copyright header.

 This program is distributed in the hope that it will be usefull but
 WITHOUT ANY WARRANTY; without event the implied warranty of MERCHANTABILITY
 or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public Licence for
 more details.


 This class only exports basic byte manipulators for PACKED CONVERSIONS

 @author Jean-Yves MENGANT
 */

public class BYTEFX {

	/* extract LowBytes and highbytes from short */
	public final static byte LOWBYTE(short ii) {
		return ((byte) ((short) (ii & 0x00ff)));
	}

	/* extract Lowword and highword from int */
	public final static short LOWWORD(int ii) {
		return ((short) ((int) (ii & 0x0000ffff)));
	}

	/**
	 * Misc static BYTE & WORD conversion functions
	 */
	public final static byte HIBYTE(short ii) {
		return (BYTEFX.LOWBYTE((short) (ii >> 8)));
	}

	public final static short HIWORD(int ii) {
		return (BYTEFX.LOWWORD((int) (ii >> 16)));
	}

	/* Basic nibble , byte , short int manipulation functions */

	public static final short MAKESHORT(byte l, byte h) {
		return (short) ((short) l | (short) (h << 8));
	}

	public static final int MAKEINT(short l, short h) {
		return (int) ((int) l | (int) (h << 16));
	}

	public static final byte MAKEBYTE(byte l, byte h) {
		return (byte) (l | (byte) (h << 4));
	}

	public static final byte LOWNIBBLE(byte b) {
		return (byte) (b & 0xF);
	}

	public static final byte HINIBBLE(byte b) {
		return LOWNIBBLE((byte) (b >> 4));
	}

	/* switch bytes FOR LITTE ENDIAN TO BIG_ENDIAN and reverse */
	public static int LITTLE_BIG(int wk) {
		short hi = BYTEFX.HIWORD(wk);
		short lo = BYTEFX.LOWWORD(wk);

		byte hib = BYTEFX.HIBYTE(hi);
		byte lob = BYTEFX.LOWBYTE(hi);
		hi = BYTEFX.MAKESHORT(hib, lob);

		hib = BYTEFX.HIBYTE(lo);
		lob = BYTEFX.LOWBYTE(lo);
		lo = BYTEFX.MAKESHORT(hib, lob);

		return (BYTEFX.MAKEINT(hi, lo));
	}

};
