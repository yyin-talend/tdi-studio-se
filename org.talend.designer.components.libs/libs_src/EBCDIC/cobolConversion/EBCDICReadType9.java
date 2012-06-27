package cobolConversion;
import java.math.BigDecimal;


public class EBCDICReadType9 {

	public static BigDecimal readType9Value(byte[] byteValue,int decimal,boolean isImpliedDecimal,String charset) throws Exception{

        byte[] b = byteValue;
        int idx;
        char[] pos = { 0xC0, 0xC1, 0xC2, 0xC3, 0xC4, 0xC5, 0xC6, 0xC7, 0xC8, 0xC9 };
        char[] neg = { 0xD0, 0xD1, 0xD2, 0xD3, 0xD4, 0xD5, 0xD6, 0xD7, 0xD8, 0xD9 };
        String IBMpositives = new String(pos);
        String IBMnegatives = new String(neg);
        String failoverpositives = "{ABCDEFGHI";
        String failovernegatives = "}JKLMNOPQR";
        String MFnegatives = "pqrstuvwxy";
				String CAnegatives = " !\"#$%&'()";

        StringBuffer buf = new StringBuffer(new String(b, charset));
        int lastidx = buf.length() - 1;
        char lastbyte = buf.charAt(lastidx);
        boolean positive = true;

        idx = IBMpositives.indexOf(lastbyte);
        if (idx > -1) {
            buf.replace(lastidx, lastidx + 1, new Integer(idx).toString());
            positive = true;
        }

        idx = IBMnegatives.indexOf(lastbyte);
        if (idx > -1) {
            buf.replace(lastidx, lastidx + 1, new Integer(idx).toString());
            positive = false;
        }

        idx = failoverpositives.indexOf(lastbyte);
        if (idx > -1) {
            buf.replace(lastidx, lastidx + 1, new Integer(idx).toString());
            positive = true;
        }

        idx = failovernegatives.indexOf(lastbyte);
        if (idx > -1) {
            buf.replace(lastidx, lastidx + 1, new Integer(idx).toString());
            positive = false;

        }

				idx = MFnegatives.indexOf(lastbyte);
        if (idx > -1) {
            buf.replace(lastidx, lastidx + 1, new Integer(idx).toString());
            positive = false;
				}

				idx = CAnegatives.indexOf(lastbyte);
        if (idx > -1) {
            buf.replace(lastidx, lastidx + 1, new Integer(idx).toString());
            positive = false;
				}

        if ((decimal > 0) && isImpliedDecimal) {
            buf.insert(buf.length() - decimal, '.');
        }

        BigDecimal retVal;

        try {
            retVal = new BigDecimal(buf.toString());
        } catch (NumberFormatException ex) {
            throw new Exception();
        }

        if (!positive) {
            retVal = retVal.negate();
        }

        return retVal;
    
	}
}
