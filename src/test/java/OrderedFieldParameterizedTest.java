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

    private int param1;
    private String param2;
    private Class<?> param3;
    private Feature param4;

    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters(){
        Object[][] params = {
                {1001, "{\"id\":1001}", Model.class, Feature.OrderedField}
        };

        return Arrays.asList(params);
    }

    public OrderedFieldParameterizedTest(int param1, String param2, Class<?> param3, Feature param4){
        configure(param1, param2, param3, param4);
    }

    public void configure(int param1, String param2, Class<?> param3, Feature param4){
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
    }

    @Test
    public void test_ordered_field(){
        Model model = (Model) JSON.parseObject(param2, param3, param4);

        Assert.assertEquals(param1, model.getId());
        Assert.assertEquals(param2, JSON.toJSONString(model));
    }

    public static interface Model {
        public int getId();
        public void setId(int value);
    }
}
