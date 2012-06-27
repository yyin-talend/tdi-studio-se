package cobolConversion;
import java.math.BigDecimal;

public class EBCDICWriteType9 {

	public static byte[] writeType9Value(int length, int decimal, BigDecimal value,
			boolean isSigned, boolean isImpliedDecimal,String charset) throws Exception {

		byte[] reValue = null;

		if (decimal != value.scale()) {
			BigDecimal tmp = value.setScale(decimal, BigDecimal.ROUND_FLOOR);
			value = tmp;
		}

		String str = value.toPlainString();
		StringBuffer buf = new StringBuffer(str);

		// Modify byte with sign ...
		if (isSigned) {
			int lastidx = buf.length() - 1;
			char lastbyte = buf.charAt(lastidx);
			char repl;

			if (value.signum() > 0) {
				repl = "{ABCDEFGHI".charAt(Integer.parseInt(String
						.valueOf(lastbyte)));
			} else {
				repl = "}JKLMNOPQR".charAt(Integer.parseInt(String
						.valueOf(lastbyte)));
			}

			buf.setCharAt(lastidx, repl);

			// Delete sign char
			String tmp = buf.toString();
			String tmp2 = tmp.replace("-", "");
			buf = new StringBuffer(tmp2);
		}

		// Delete decimal point...
		if (isImpliedDecimal) {
			String tmp = buf.toString();
			String tmp2 = tmp.replace(".", "");
			buf = new StringBuffer(tmp2);
		}

		// Adding some leading zeros ...
		while (length > buf.length()) {
			buf.insert(0, "0");
		}
		reValue = buf.toString().getBytes(charset);
		return reValue;
	}
}
