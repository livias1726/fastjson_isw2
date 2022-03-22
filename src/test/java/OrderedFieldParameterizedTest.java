package test.java;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class OrderedFieldParameterizedTest extends TestCase {

    @Parameterized.Parameters
    public static Collection<Object[]> configure(){
        String text = "{\"id\":1001}";
        Model model = JSON.parseObject(text, Model.class, Feature.OrderedField);

        Object[][] params = {
                {1001, model.getId(), null, null},
                {0, 0, text, JSON.toJSONString(model)}
        };

        return Arrays.asList(params);
    }

    private final int param1;
    private final int param2;
    private final String param3;
    private final String param4;

    public OrderedFieldParameterizedTest(int param1, int param2, String param3, String param4){
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
    }

    @Test
    public void test_ordered_field(){
        Assert.assertEquals(param1, param2);
        Assert.assertEquals(param3, param4);
    }

    public static interface Model {
        public int getId();
        public void setId(int value);
    }
}
