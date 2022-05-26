import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class OrderedFieldParameterizedTest {

    private int expected1;
    private String expected2;
    private Class<Model> value1;
    private Feature value2;

    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters(){
        Object[][] params = {
                {1001, "{\"id\":1001}", Model.class, Feature.OrderedField}
        };

        return Arrays.asList(params);
    }

    public OrderedFieldParameterizedTest(int param1, String param2, Class<Model> param3, Feature param4){
        configure(param1, param2, param3, param4);
    }

    public void configure(int param1, String param2, Class<Model> param3, Feature param4){
        this.expected1 = param1;
        this.expected2 = param2;
        this.value1 = param3;
        this.value2 = param4;
    }

    @Test
    public void test_ordered_field(){
        Model model = JSON.parseObject(expected2, value1, value2);

        Assert.assertEquals(expected1, model.getId());
        Assert.assertEquals(expected2, JSON.toJSONString(model));
    }

    public static interface Model {
        public int getId();
        public void setId(int value);
    }
}
