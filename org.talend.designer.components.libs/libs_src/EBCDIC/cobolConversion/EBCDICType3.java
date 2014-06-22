package cobolConversion;
import java.math.BigDecimal;

import org.apache.commons.lang.mutable.MutableInt;

public class EBCDICType3 {

	public static BigDecimal readType3Value(byte[] byteValue, int decimal,
			boolean isImpliedDecimal) throws Exception {
		int len = byteValue.length;
		StringBuffer strbuf = new StringBuffer();
		int tmp;
		int tmp1;
		int tmp2;
		for (int i = 0; i < len; i++) {
			tmp = byteValue[i];
			tmp1 = tmp & 0xF0;
			tmp2 = tmp1 >> 4;
			strbuf.append(tmp2);

			if (i < (len - 1)) {
				tmp = byteValue[i];
				tmp1 = tmp & 0x0F;
				strbuf.append(tmp1);
			}
		}

		if ((decimal > 0) && isImpliedDecimal) {
			strbuf.insert(strbuf.length() - decimal, '.');
		}

		BigDecimal retVal;

		try {
			retVal = new BigDecimal(strbuf.toString());
		} catch (NumberFormatException ex) {
			throw new Exception();
		}

		tmp = byteValue[len - 1];
		tmp1 = tmp & 0x0F;

		if ((tmp1 == 0x0F) || (tmp1 == 0x0C)) {
			return retVal;
		} else if (tmp1 == 0x0D) {
			return retVal.negate();
		} else {
			return retVal;
		}
	}

	public static byte[] writeType3Value(int length, int decimal,
			BigDecimal value, boolean isSigned) throws Exception {

		if (decimal != value.scale()) {
			BigDecimal tmp = value.setScale(decimal, BigDecimal.ROUND_FLOOR);
			value = tmp;
		}

		String str = value.toPlainString();
		int len = str.length();
		byte[] buf = new byte[length];
		MutableInt k = new MutableInt(len);
		byte even; // left and right nibble ( we go from right to left )
		byte odd;

		for (int i = length - 1; i >= 0; i--) {
			// Last byte needs sign nibble
			if (i == (length - 1)) {
				even = getNextByte(str, k);

				if (isSigned) {
					if (value.signum() >= 0) {
						odd = 0x0C;
					} else {
						odd = 0x0D;
					}
				} else {
					odd = (byte) 0x0F;
				}
			} else {
				// Packing rest of the digits...
				// Get even digit if exist or zero
				odd = getNextByte(str, k);
				even = getNextByte(str, k);
			}

			buf[i] = (byte) ((even << 4) | odd);
		}

		// TODO: Check if str ">" buf and eventually throw an Exc.
		return buf;
	}

	private static byte getNextByte(String number, MutableInt recentlyReturned) {
		MutableInt zero = new MutableInt(0);
		recentlyReturned.decrement();

		if (recentlyReturned.compareTo(zero) >= 0) {
			while (!"0123456789".contains(String.valueOf(number
					.charAt(recentlyReturned.intValue())))) {
				recentlyReturned.decrement();

				if (recentlyReturned.compareTo(zero) < 0) {
					return 0;
				}
			}

			return (byte) (Character.getNumericValue(number
					.charAt(recentlyReturned.intValue())));
		} else {
			return 0;
		}
	}
}
