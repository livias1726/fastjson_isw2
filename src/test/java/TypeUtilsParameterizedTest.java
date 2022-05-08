import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import org.junit.Assert;
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

    @RunWith(Parameterized.class)
    public static class TypeUtilsCastToJavaBeanTest {

        private final Object expected;
        private final Object value1;
        private final Class<Map> value2;

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

        public TypeUtilsCastToJavaBeanTest(Object param1, Object param2, Class<Map> param3) {
            this.expected = param1;
            this.value1 = param2;
            this.value2 = param3;
        }

        @Test
        public void test_castToJavaBean() {
            Assert.assertTrue(expected == TypeUtils.castToJavaBean(value1, value2));
        }
    }

    @RunWith(Parameterized.class)
    public static class TypeUtilsUserTest {
        private long expected1;
        private String expected2;
        private JSONObject value1;
        private Class<User> value2;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {

            Object[][] params = {
                {1L, "panlei", User.class} //test_2, test_3
            };

            return Arrays.asList(params);
        }

        public TypeUtilsUserTest(long param1, String param2, Class<User> param3) {
            configure(param1, param2, param3);
        }

        public void configure(long param1, String param2, Class<User> param3) {
            JSONObject localParam = new JSONObject();

            localParam.put("id", 1);
            localParam.put("name", "panlei");

            //Params
            this.expected1 = param1;
            this.value1 = localParam;
            this.expected2 = param2;
            this.value2 = param3;
        }

        @Test
        public void test_2() {
            User user = TypeUtils.castToJavaBean(value1, value2);

            Assert.assertEquals(expected1, user.getId());
            Assert.assertEquals(expected2, user.getName());
        }

        @Test
        public void test_3() {
            User user = JSON.toJavaObject(value1, value2);

            Assert.assertEquals(expected1, user.getId());
            Assert.assertEquals(expected2, user.getName());
        }

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
    }

    @RunWith(Parameterized.class)
    public static class TypeUtilsGetObjectTest {

        public enum Type {CAST, CALENDAR}

        private final Type type;

        private Object expected;
        private String value1;
        private Class<?> value2;
        private JSONObject json;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {

            long millis;
            B b;

            Object[][] params = {
                {Type.CAST, 1L, "id", 1, int.class}, //test_cast_Integer
                {Type.CAST, 1L, "id", 1, Integer.class}, //test_cast_Integer_2
                {Type.CAST, 1, "id", 1L, long.class}, //test_cast_to_long
                {Type.CAST, 1, "id", 1L, Long.class}, //test_cast_to_Long
                {Type.CAST, 1, "id", (short) 1, short.class}, //test_cast_to_short
                {Type.CAST, 1, "id", (short) 1, Short.class}, //test_cast_to_Short
                {Type.CAST, 1, "id", (byte) 1, byte.class}, //test_cast_to_byte
                {Type.CAST, 1, "id", (byte) 1, Byte.class}, //test_cast_to_Byte
                {Type.CAST, 1, "id", new BigInteger("1"), BigInteger.class}, //test_cast_to_BigInteger
                {Type.CAST, 1, "id", new BigDecimal("1"), BigDecimal.class}, //test_cast_to_BigDecimal
                {Type.CAST, 1, "id", Boolean.TRUE, boolean.class}, //test_cast_to_boolean
                {Type.CAST, 1, "id", Boolean.TRUE, Boolean.class}, //test_cast_to_Boolean
                {Type.CAST, null, "id", null, Boolean.class}, //test_cast_null
                {Type.CAST, 1, "id", "1", String.class}, //test_cast_to_String
                {Type.CAST, millis = System.currentTimeMillis(), "date", new Date(millis), Date.class}, //test_cast_to_Date
                {Type.CAST, millis = System.currentTimeMillis(), "date", new java.sql.Date(millis), java.sql.Date.class}, //test_cast_to_SqlDate
                {Type.CAST, Long.toString(millis = System.currentTimeMillis()), "date", new java.sql.Date(millis), java.sql.Date.class}, //test_cast_to_SqlDate_string
                {Type.CAST, null, "date", null, java.sql.Date.class}, //test_cast_to_SqlDate_null
                {Type.CAST, new Date(millis = System.currentTimeMillis()), "date", new java.sql.Date(millis), java.sql.Date.class}, //test_cast_to_SqlDate_util_Date
                {Type.CAST, new java.sql.Date(millis = System.currentTimeMillis()), "date", new java.sql.Date(millis), java.sql.Date.class}, //test_cast_to_SqlDate_sql_Date
                {Type.CAST, millis = System.currentTimeMillis(), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class}, //test_cast_to_Timestamp
                {Type.CAST, Long.toString(millis = System.currentTimeMillis()), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class}, //test_cast_to_Timestamp_string
                {Type.CAST, new BigDecimal(Long.toString(millis = System.currentTimeMillis())), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class}, //test_cast_to_Timestamp_number
                {Type.CAST, null, "date", null, java.sql.Timestamp.class}, //test_cast_to_Timestamp_null
                {Type.CAST, new Date(millis = System.currentTimeMillis()), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class}, //test_cast_to_Timestamp_util_Date
                {Type.CAST, new java.sql.Date(millis = System.currentTimeMillis()), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class}, //test_cast_to_Timestamp_sql_Date
                {Type.CAST, b = new B(), "value", b, A.class}, //test_cast_ab
                {Type.CAST, b = new B(), "value", b, IA.class}, //test_cast_ab_1
                {Type.CALENDAR, millis = System.currentTimeMillis(), "date", new java.sql.Date(millis), java.sql.Date.class}, //test_cast_to_SqlDate_calendar
                {Type.CALENDAR, millis = System.currentTimeMillis(), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class} //test_cast_to_Timestamp_calendar
            };

            return Arrays.asList(params);
        }

        public TypeUtilsGetObjectTest(Type type, Object param1, String param2, Object param3, Class<?> param4) {
            this.type = type;

            configure(param1, param2, param3, param4);
        }

        public void configure(Object param1, String param2, Object param3, Class<?> param4) {
            JSONObject localParam = new JSONObject();

            if(type.equals(Type.CALENDAR)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis((Long) param1);

                localParam.put(param2, calendar);
            }else {
                localParam.put(param2, param1);
            }

            //Params
            this.expected = param3;
            this.value1 = param2;
            this.value2 = param4;

            this.json = localParam;
        }

        @Test
        public void test_getObject() {
            Assert.assertEquals(expected, json.getObject(value1, value2));
        }

        public static class A implements IA {

        }

        public static interface IA {

        }

        public static class B extends A {

        }
    }

    @RunWith(Parameterized.class)
    public static class TypeUtilsCastToSqlDateTest {
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

        public TypeUtilsCastToSqlDateTest(java.sql.Date param1, java.sql.Date param2) {
            this.expected = param1;
            this.value = param2;
        }

        @Test
        public void test_castToSqlDate() {
            Assert.assertEquals(expected, TypeUtils.castToSqlDate(value));
        }
    }

    @RunWith(Parameterized.class)
    public static class TypeUtilsJSONExceptionTest {
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

        public TypeUtilsJSONExceptionTest(String param1, Object param2, Class<?> param3) {
            configure(param1, param2, param3);
        }

        public void configure(String param1, Object param2, Class<?> param3) {
            JSONObject localParam = new JSONObject();
            localParam.put(param1, param2);

            this.json = localParam;
            this.value1 = param1;
            this.value2 = param3;
        }

        @Test
        public void test_JSONException() {

            JSONException error = null;
            try {
                json.getObject(value1, value2);
            } catch (JSONException e) {
                error = e;
            }
            Assert.assertNotNull(error);
        }

        public static class A implements IA {

        }

        public static interface IA {

        }

        public static class B extends A {

        }
    }

    @RunWith(Parameterized.class)
    public static class TypeUtilsCastToTimestampTest {
        public enum Type {TIMESTAMP, TIMEZONE}

        private final Type type;

        private Timestamp expected;
        private Object value;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {
            Timestamp timestamp;

            Object[][] params = {
                {Type.TIMESTAMP, null, null}, //test_cast_to_Timestamp_null2
                {Type.TIMESTAMP, timestamp = new java.sql.Timestamp(System.currentTimeMillis()), timestamp}, //test_cast_to_Timestamp_sql_Timestamp
                {Type.TIMEZONE, new Timestamp(0), "1970-01-01 08:00:00"}, //test_cast_to_Timestamp_1970_01_01_00_00_00
            };

            return Arrays.asList(params);
        }

        public TypeUtilsCastToTimestampTest(Type type, Timestamp param1, Object param2) {
            this.type = type;

            configure(param1, param2);
        }

        public void configure(Timestamp param1, Object param2) {
            if(type.equals(Type.TIMEZONE)){
                JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
            }

            this.expected = param1;
            this.value = param2;
        }

        @Test
        public void test_castToTimestamp() {
            Assert.assertEquals(expected, TypeUtils.castToTimestamp(value));
        }
    }

    @RunWith(Parameterized.class)
    public static class TypeUtilsCastToBigDecimalTest {

        private final BigDecimal expected;
        private final BigDecimal value;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {
            BigDecimal bd;

            Object[][] params = {
                {bd = new BigDecimal("123"), bd} //test_cast_to_BigDecimal_same
            };

            return Arrays.asList(params);
        }

        public TypeUtilsCastToBigDecimalTest(BigDecimal param1, BigDecimal param2) {
            this.expected = param1;
            this.value = param2;
        }

        @Test
        public void test_cast_to_BigDecimal_same() {
            Assert.assertEquals(expected, TypeUtils.castToBigDecimal(value));
        }
    }

    @RunWith(Parameterized.class)
    public static class TypeUtilsCastToBigIntegerTest {

        private final BigInteger expected;
        private final BigInteger value;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {
            BigInteger bi;

            Object[][] params = {
                {bi = new BigInteger("123"), bi} //test_cast_to_BigInteger_same
            };

            return Arrays.asList(params);
        }

        public TypeUtilsCastToBigIntegerTest(BigInteger param1, BigInteger param2) {
            this.expected = param1;
            this.value = param2;
        }

        @Test
        public void test_cast_to_BigInteger_same() {
            Assert.assertEquals(expected, TypeUtils.castToBigInteger(value));
        }
    }

    @RunWith(Parameterized.class)
    public static class TypeUtilsCastTest {

        private final Class<Integer[]> expected;
        private final ArrayList value1;
        private final ParserConfig value2;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {

            Object[][] params = {
                    {Integer[].class, new ArrayList(), null} //test_cast_Array
            };

            return Arrays.asList(params);
        }

        public TypeUtilsCastTest(Class<Integer[]> param1, ArrayList param2, ParserConfig param3) {
            this.expected = param1;
            this.value1 = param2;
            this.value2 = param3;
        }

        @Test
        public void test_cast() {
            Assert.assertEquals(expected, TypeUtils.cast(value1, expected, value2).getClass());
        }
    }

    @RunWith(Parameterized.class)
    public static class TypeUtilsNotErrorTest {

        private JSONObject json;
        private Timestamp expected;
        private String value1;
        private Class<Timestamp> value2;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {

            Object[][] params = {
                {"date", -1, java.sql.Timestamp.class, new Timestamp(-1L)} //test_cast_to_Timestamp_not_error
            };

            return Arrays.asList(params);
        }

        public TypeUtilsNotErrorTest(String param1, int param2, Class<java.sql.Timestamp> param3, Timestamp param4) {
            configure(param1, param2, param3, param4);
        }

        public void configure(String param1, int param2, Class<java.sql.Timestamp> param3, Timestamp param4) {
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

    @RunWith(Parameterized.class)
    public static class TypeUtilsErrorTest {

        private JSONObject json;
        private Class<C> value1;
        private ParserConfig value2;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {

            Object[][] params = {
                    {"id", 1, C.class} //test_error
            };

            return Arrays.asList(params);
        }

        public TypeUtilsErrorTest(String param1, int param2, Class<C> param3) {
            configureTestError(param1, param2, param3);
        }

        public void configureTestError(String param1, int param2, Class<C> param3) {
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

        public static class A implements IA {

        }

        public static interface IA {

        }

        public static class B extends A {

        }

        public static class C extends B {

            public int getId() {
                throw new UnsupportedOperationException();
            }

            public void setId(int id) {
                throw new UnsupportedOperationException();
            }
        }
    }

    @RunWith(Parameterized.class)
    public static class TypeUtilsError2Test {

        private JSONObject json;
        private Type value1;
        private ParserConfig value2;

        @Parameterized.Parameters
        public static Collection<Object[]> getTestParameters() {

            Object[][] params = {
                {"id", 1, "f", List.class} //test_error_2
            };

            return Arrays.asList(params);
        }

        public TypeUtilsError2Test(String param1, int param2, String param3, Class<List> param4) throws NoSuchMethodException {
            configure(param1, param2, param3, param4);
        }

        public void configure(String param1, int param2, String param3, Class<List> param4) throws NoSuchMethodException {

            JSONObject localParam = new JSONObject();
            localParam.put(param1, param2);

            this.json = localParam;

            Method method = TypeUtilsError2Test.class.getMethod(param3, param4);
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

        public static void f(List<?> list) {

        }
    }
}
