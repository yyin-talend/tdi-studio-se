package org.talend.ms.crm.odata;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class QueryExpandUtilTest {

    @Test
    public void getExpandConfigParametersSize() {
        List<String> fields = Arrays.asList("aaa", "bbb", "ccc");
        List<String> params = Arrays.asList("aaa", "bbb", "ccc");
        final List<String> expandConfig = QueryExpandUtil.getExpandConfig(fields, params);
        assertEquals(3, expandConfig.size());

        try {
            params = Arrays.asList("aaa", "bbb", "ccc", "ddd");
            QueryExpandUtil.getExpandConfig(fields, params);
            fail("An exception should be raised.");
        }
        catch (Exception e){
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    public void getExpandConfig(){
        List<String> fields = Arrays.asList("aaa", "_bbb_value", "_ccc_ddd", "eee_fff_v", "ggg_hhh_value");
        List<String> params = Arrays.asList("", "ppp1", "ppp1,ppp2", "aaa zzz eee", "");
        final List<String> expandConfig = QueryExpandUtil.getExpandConfig(fields, params);

        final String aaa = expandConfig.get(0);
        final String bbb = expandConfig.get(1);
        final String ccc = expandConfig.get(2);
        final String eee = expandConfig.get(3);
        final String ggg = expandConfig.get(4);

        assertEquals("aaa", aaa);
        assertEquals("bbb(ppp1)", bbb);
        assertEquals("ccc_ddd(ppp1,ppp2)", ccc);
        assertEquals("eee_fff_v(aaa zzz eee)", eee);
        assertEquals("ggg_hhh", ggg);
    }
}