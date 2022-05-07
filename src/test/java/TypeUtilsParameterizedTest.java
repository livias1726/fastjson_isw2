import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

@RunWith(Enclosed.class)
@SuppressWarnings("rawtypes")
public class TypeUtilsParameterizedTest {

    /* *
     * test_0
     * test_1
     * */
    @RunWith(Parameterized.class)
    public static class TypeUtils0Test {
        private final Object expected;
        private final Object value2;
        private final Class<Map> value3;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {
            HashMap map;
            JSONObject json;

            Object[][] params = {
                    {map = new HashMap(), map, Map.class}, //test_0
                    {json = new JSONObject(), json, Map.class}, //test_1
            };

            return Arrays.asList(params);
        }

        public TypeUtils0Test(Object value1, Object value2, Class<Map> value3) {
            this.expected = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        @Test
        public void test_0() {
            Assert.assertTrue(expected == TypeUtils.castToJavaBean(value2, value3));
        }
    }

    /* *
     * test_2
     * */
    @RunWith(Parameterized.class)
    public static class TypeUtils2Test {
        private long expected1;
        private String expected2;
        private JSONObject value1;
        private Class<User> value2;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {

            Object[][] params = {
                    {1L, "panlei", User.class} //test_2
            };

            return Arrays.asList(params);
        }

        public TypeUtils2Test(long expected1, String expected2, Class<User> value2) {
            configureTest2(expected1, expected2, value2);
        }

        public void configureTest2(long expected1, String expected2, Class<User> value2) {
            JSONObject localParam = new JSONObject();

            localParam.put("id", 1);
            localParam.put("name", "panlei");

            //Params
            this.expected1 = expected1;
            this.value1 = localParam;
            this.expected2 = expected2;
            this.value2 = value2;
        }

        @Test
        public void test_2() {
            User user = TypeUtils.castToJavaBean(value1, value2);

            Assert.assertEquals(expected1, user.getId());
            Assert.assertEquals(expected2, user.getName());
        }
    }

    /* *
     * test_cast_Integer
     * test_cast_Integer_2
     * test_cast_to_long
     * test_cast_to_Long
     * test_cast_to_short
     * test_cast_to_Short
     * test_cast_to_byte
     * test_cast_to_Byte
     * test_cast_to_BigInteger
     * test_cast_to_BigDecimal
     * test_cast_to_boolean
     * test_cast_to_Boolean
     * test_cast_null
     * test_cast_to_String
     * test_cast_to_Date
     * test_cast_to_SqlDate
     * test_cast_to_SqlDate_string
     * test_cast_to_SqlDate_null
     * test_cast_to_SqlDate_util_Date
     * test_cast_to_SqlDate_sql_Date
     * test_cast_to_Timestamp
     * test_cast_to_Timestamp_string
     * test_cast_to_Timestamp_number
     * test_cast_to_Timestamp_null
     * test_cast_to_Timestamp_util_Date
     * test_cast_to_Timestamp_sql_Date
     * test_cast_ab
     * test_cast_ab_1
     * */
    @RunWith(Parameterized.class)
    public static class TypeUtilsCastTest {

        private Object expected;
        private String value1;
        private Class<?> value2;
        private JSONObject json;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {

            long millis;
            B b;

            Object[][] params = {
                    {1L, "id", 1, int.class}, //test_cast_Integer
                    {1L, "id", 1, Integer.class}, //test_cast_Integer_2
                    {1, "id", 1L, long.class}, //test_cast_to_long
                    {1, "id", 1L, Long.class}, //test_cast_to_Long
                    {1, "id", (short) 1, short.class}, //test_cast_to_short
                    {1, "id", (short) 1, Short.class}, //test_cast_to_Short
                    {1, "id", (byte) 1, byte.class}, //test_cast_to_byte
                    {1, "id", (byte) 1, Byte.class}, //test_cast_to_Byte
                    {1, "id", new BigInteger("1"), BigInteger.class}, //test_cast_to_BigInteger
                    {1, "id", new BigDecimal("1"), BigDecimal.class}, //test_cast_to_BigDecimal
                    {1, "id", Boolean.TRUE, boolean.class}, //test_cast_to_boolean
                    {1, "id", Boolean.TRUE, Boolean.class}, //test_cast_to_Boolean
                    {null, "id", null, Boolean.class}, //test_cast_null
                    {1, "id", "1", String.class}, //test_cast_to_String
                    {millis = System.currentTimeMillis(), "date", new Date(millis), Date.class}, //test_cast_to_Date
                    {millis = System.currentTimeMillis(), "date", new java.sql.Date(millis), java.sql.Date.class}, //test_cast_to_SqlDate
                    {Long.toString(millis = System.currentTimeMillis()), "date", new java.sql.Date(millis), java.sql.Date.class}, //test_cast_to_SqlDate_string
                    {null, "date", null, java.sql.Date.class}, //test_cast_to_SqlDate_null
                    {new Date(millis = System.currentTimeMillis()), "date", new java.sql.Date(millis), java.sql.Date.class}, //test_cast_to_SqlDate_util_Date
                    {new java.sql.Date(millis = System.currentTimeMillis()), "date", new java.sql.Date(millis), java.sql.Date.class}, //test_cast_to_SqlDate_sql_Date
                    {millis = System.currentTimeMillis(), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class}, //test_cast_to_Timestamp
                    {Long.toString(millis = System.currentTimeMillis()), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class}, //test_cast_to_Timestamp_string
                    {new BigDecimal(Long.toString(millis = System.currentTimeMillis())), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class}, //test_cast_to_Timestamp_number
                    {null, "date", null, java.sql.Timestamp.class}, //test_cast_to_Timestamp_null
                    {new Date(millis = System.currentTimeMillis()), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class}, //test_cast_to_Timestamp_util_Date
                    {new java.sql.Date(millis = System.currentTimeMillis()), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class}, //test_cast_to_Timestamp_sql_Date
                    {b = new B(), "value", b, A.class}, //test_cast_ab
                    {b = new B(), "value", b, IA.class}, //test_cast_ab_1
            };

            return Arrays.asList(params);
        }

        public TypeUtilsCastTest(Object param1, String param2, Object param3, Class<?> param4) {
            configureTestCast(param1, param2, param3, param4);
        }

        public void configureTestCast(Object param1, String param2, Object param3, Class<?> param4) {
            JSONObject localParam = new JSONObject();

            localParam.put(param2, param1);

            //Params
            this.expected = param3;
            this.value1 = param2;
            this.value2 = param4;

            this.json = localParam;
        }

        @Test
        public void test_cast() {
            Assert.assertEquals(expected, json.getObject(value1, value2));
        }
    }

    /* *
     * test_cast_to_SqlDate_null2
     * test_cast_to_SqlDate_sql_Date2
     * */
    @RunWith(Parameterized.class)
    public static class TypeUtilsCastSqlDateTest {
        private final java.sql.Date expected;
        private final java.sql.Date value;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {
            java.sql.Date date;

            Object[][] params = {
                    {null, null}, //test_cast_to_SqlDate_null2
                    {date = new java.sql.Date(System.currentTimeMillis()), date}, //test_cast_to_SqlDate_sql_Date2
            };

            return Arrays.asList(params);
        }

        public TypeUtilsCastSqlDateTest(java.sql.Date param1, java.sql.Date param2) {
            this.expected = param1;
            this.value = param2;
        }

        @Test
        public void test_cast_to_SqlDate() {
            Assert.assertEquals(expected, TypeUtils.castToSqlDate(value));
        }
    }

    /* *
     * test_cast_to_SqlDate_calendar
     * test_cast_to_Timestamp_calendar
     * */
    @RunWith(Parameterized.class)
    public static class TypeUtilsCalendarTest {
        private Object expected;
        private String value1;
        private Class<?> value2;
        private JSONObject json;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {
            long millis;

            Object[][] params = {
                    {millis = System.currentTimeMillis(), "date", new java.sql.Date(millis), java.sql.Date.class},
                    {millis = System.currentTimeMillis(), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class}
            };

            return Arrays.asList(params);
        }

        public TypeUtilsCalendarTest(long param1, String param2, Object param3, Class<?> param4) {
            configureTestCalendar(param1, param2, param3, param4);
        }

        public void configureTestCalendar(long param1, String param2, Object param3, Class<?> param4) {
            JSONObject localParam = new JSONObject();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(param1);

            localParam.put(param2, calendar);

            //Params
            this.expected = param3;
            this.value1 = param2;
            this.value2 = param4;

            this.json = localParam;
        }

        @Test
        public void test_calendar() {
            Assert.assertEquals(expected, json.getObject(value1, value2));
        }
    }

    /* *
     * test_cast_to_SqlDate_error
     * test_cast_ab_error
     * */
    @RunWith(Parameterized.class)
    public static class TypeUtilsCastErrorTest {
        private String value1;
        private Class<?> value2;
        private JSONObject json;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {

            Object[][] params = {
                    {"date", 0, java.sql.Date.class}, //test_cast_to_SqlDate_error
                    {"value", new A(), B.class} //test_cast_ab_error
            };

            return Arrays.asList(params);
        }

        public TypeUtilsCastErrorTest(String param1, Object param2, Class<?> param3) {
            configureTestCastError(param1, param2, param3);
        }

        public void configureTestCastError(String param1, Object param2, Class<?> param3) {
            JSONObject localParam = new JSONObject();
            localParam.put(param1, param2);

            this.json = localParam;
            this.value1 = param1;
            this.value2 = param3;
        }

        @Test
        public void test_cast_error() {

            JSONException error = null;
            try {
                json.getObject(value1, value2);
            } catch (JSONException e) {
                error = e;
            }
            Assert.assertNotNull(error);
        }
    }

    /* *
     * test_cast_to_Timestamp_null2
     * test_cast_to_Timestamp_sql_Timestamp
     * */
    @RunWith(Parameterized.class)
    public static class TypeUtilsCastTimestampTest {
        private final Timestamp expected;
        private final Timestamp value;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {
            Timestamp timestamp;

            Object[][] params = {
                    {null, null}, //test_cast_to_Timestamp_null2
                    {timestamp = new java.sql.Timestamp(System.currentTimeMillis()), timestamp} //test_cast_to_Timestamp_sql_Timestamp
            };

            return Arrays.asList(params);
        }

        public TypeUtilsCastTimestampTest(Timestamp param1, Timestamp param2) {
            this.expected = param1;
            this.value = param2;
        }

        @Test
        public void test_cast_to_Timestamp() {
            Assert.assertEquals(expected, TypeUtils.castToTimestamp(value));
        }
    }

    //test_cast_to_Timestamp_1970_01_01_00_00_00
    @RunWith(Parameterized.class)
    public static class TypeUtilsCastTimezoneTest {
        private Timestamp expected;
        private String value;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {

            Object[][] params = {
                    {"Asia/Shanghai", new Timestamp(0), "1970-01-01 08:00:00"}, //test_cast_to_Timestamp_1970_01_01_00_00_00
            };

            return Arrays.asList(params);
        }

        public TypeUtilsCastTimezoneTest(String param1, Timestamp param2, String param3) {
            configureTestTimezone(param1, param2, param3);
        }

        public void configureTestTimezone(String param1, Timestamp param2, String param3) {
            JSON.defaultTimeZone = TimeZone.getTimeZone(param1);

            this.expected = param2;
            this.value = param3;
        }

        @Test
        public void test_cast_to_Timestamp_1970_01_01_00_00_00() {
            Assert.assertEquals(expected, TypeUtils.castToTimestamp(value));
        }
    }

    // test_cast_to_BigDecimal_same
    @RunWith(Parameterized.class)
    public static class TypeUtilsCastBigDecimalTest {

        private final BigDecimal expected;
        private final BigDecimal value;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {
            BigDecimal bd;

            Object[][] params = {
                    {bd = new BigDecimal("123"), bd}
            };

            return Arrays.asList(params);
        }

        public TypeUtilsCastBigDecimalTest(BigDecimal param1, BigDecimal param2) {
            this.expected = param1;
            this.value = param2;
        }

        @Test
        public void test_cast_to_BigDecimal_same() {
            Assert.assertEquals(expected, TypeUtils.castToBigDecimal(value));
        }
    }

    //test_cast_to_BigInteger_same
    @RunWith(Parameterized.class)
    public static class TypeUtilsCastBigIntegerTest {

        private final BigInteger expected;
        private final BigInteger value;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {
            BigInteger bi;

            Object[][] params = {
                    {bi = new BigInteger("123"), bi}
            };

            return Arrays.asList(params);
        }

        public TypeUtilsCastBigIntegerTest(BigInteger param1, BigInteger param2) {
            this.expected = param1;
            this.value = param2;
        }

        @Test
        public void test_cast_to_BigInteger_same() {
            Assert.assertEquals(expected, TypeUtils.castToBigInteger(value));
        }
    }

    // test_cast_Array
    @RunWith(Parameterized.class)
    public static class TypeUtilsCastArrayTest {

        private final Class<Integer[]> expected;
        private final ArrayList value1;
        private final ParserConfig value2;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {

            Object[][] params = {
                    {Integer[].class, new ArrayList(), null}
            };

            return Arrays.asList(params);
        }

        public TypeUtilsCastArrayTest(Class<Integer[]> param1, ArrayList param2, ParserConfig param3) {
            this.expected = param1;
            this.value1 = param2;
            this.value2 = param3;
        }

        @Test
        public void test_cast_Array() {
            Assert.assertEquals(expected, TypeUtils.cast(value1, expected, value2).getClass());
        }
    }

    // test_cast_to_Timestamp_not_error
    @RunWith(Parameterized.class)
    public static class TypeUtilsNotErrorTest {

        private JSONObject json;
        private Timestamp expected;
        private String value1;
        private Class<Timestamp> value2;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {

            Object[][] params = {
                    {"date", -1, java.sql.Timestamp.class, new Timestamp(-1L)}
            };

            return Arrays.asList(params);
        }

        public TypeUtilsNotErrorTest(String param1, int param2, Class<java.sql.Timestamp> param3, Timestamp param4) {
            configureTestCastError(param1, param2, param3, param4);
        }

        public void configureTestCastError(String param1, int param2, Class<java.sql.Timestamp> param3, Timestamp param4) {
            JSONObject localParam = new JSONObject();
            localParam.put(param1, param2);

            this.json = localParam;

            this.expected = param4;
            this.value1 = param1;
            this.value2 = param3;
        }

        @Test
        public void test_not_error() {

            JSONException error = null;
            try {
                json.getObject(value1, value2);
            } catch (JSONException e) {
                error = e;
            }

            Assert.assertNull(error);
            Assert.assertEquals(expected, json.getObject(value1, value2));
        }
    }

    // test_error
    @RunWith(Parameterized.class)
    public static class TypeUtilsErrorTest {

        private JSONObject json;
        private Class<C> value1;
        private ParserConfig value2;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {

            Object[][] params = {
                    {"id", 1, C.class}
            };

            return Arrays.asList(params);
        }

        public TypeUtilsErrorTest(String param1, int param2, Class<C> param3) {
            configureTestError(param1, param2, param3);
        }

        public void configureTestError(String param1, int param2, Class<C> param3){
            JSONObject localParam = new JSONObject();
            localParam.put(param1, param2);

            this.json = localParam;

            this.value1 = param3;
            this.value2 = ParserConfig.getGlobalInstance();
        }

        @Test
        public void test_error() {

            JSONException error = null;
            try {
                TypeUtils.castToJavaBean(json, value1, value2);
            } catch (JSONException e) {
                error = e;
            }
            Assert.assertNotNull(error);
        }
    }

    // test_error_2
    @RunWith(Parameterized.class)
    public static class TypeUtilsError2Test {

        private JSONObject json;
        private Type value1;
        private ParserConfig value2;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {

            Object[][] params = {
                    {"id", 1, "f", List.class}
            };

            return Arrays.asList(params);
        }

        public TypeUtilsError2Test(String param1, int param2, String param3, Class<List> param4) throws NoSuchMethodException {
            configureTestError(param1, param2, param3, param4);
        }

        public void configureTestError(String param1, int param2, String param3, Class<List> param4) throws NoSuchMethodException {

            JSONObject localParam = new JSONObject();
            localParam.put(param1, param2);

            this.json = localParam;

            Method method = TypeUtilsParameterizedTest.class.getMethod(param3, param4);
            this.value1 = method.getGenericParameterTypes()[0];

            this.value2 = ParserConfig.getGlobalInstance();
        }

        @Test
        public void test_error_2() {
            Throwable error = null;
            try {
                TypeUtils.cast(json, value1, value2);
            } catch (JSONException e) {
                error = e;
            }
            Assert.assertNotNull(error);
        }
    }

    // test_3
    @RunWith(Parameterized.class)
    public static class TypeUtils3Test {

        private long expected1;
        private JSONObject value1;
        private String expected2;
        private Class<User> value2;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {
            Object[][] params = {
                    {1L, "panlei", User.class}
            };

            return Arrays.asList(params);
        }

        public TypeUtils3Test(long param1, String param2, Class<User> param3) {
            configureTest2(param1, param2, param3);
        }

        public void configureTest2(long expected1, String expected2, Class<User> value2) {
            JSONObject localParam = new JSONObject();

            localParam.put("id", 1);
            localParam.put("name", "panlei");

            //Params
            this.expected1 = expected1;
            this.value1 = localParam;
            this.expected2 = expected2;
            this.value2 = value2;
        }

        @Test
        public void test_3() {
            User user = JSON.toJavaObject(value1, value2);

            Assert.assertEquals(expected1, user.getId());
            Assert.assertEquals(expected2, user.getName());
        }
    }

    //--------------------------------------------- SUPPORT CLASSES ------------------------------------------------
    @Ignore
    public static class User {

        private long id;
        private String name;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Ignore
    public static class A implements TypeUtilsParameterizedTest.IA {

    }

    @Ignore
    public static interface IA {

    }

    @Ignore
    public static class B extends TypeUtilsParameterizedTest.A {

    }

    @Ignore
    public static class C extends TypeUtilsParameterizedTest.B {

        public int getId() {
            throw new UnsupportedOperationException();
        }

        public void setId(int id) {
            throw new UnsupportedOperationException();
        }
    }

    @Ignore
    public static void f(List<?> list) {

    }
}
