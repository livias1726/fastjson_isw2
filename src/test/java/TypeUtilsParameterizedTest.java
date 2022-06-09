import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

@SuppressWarnings("rawtypes")
public class TypeUtilsParameterizedTest {

    private Object expectedCastToJavaBean;
    private Object value1CastToJavaBean;
    private Class<Map> value2CastToJavaBean;

    private long expected1UserTest;
    private String expected2UserTest;
    private JSONObject value1UserTest;
    private Class<User> value2UserTest;

    private Object expectedCast;
    private String value1Cast;
    private Class<?> value2Cast;
    private JSONObject jsonCast;

    private Object expectedCalendar;
    private String value1Calendar;
    private Class<?> value2Calendar;
    private JSONObject jsonCalendar;

    private java.sql.Date expectedCastToSqlDate;
    private java.sql.Date valueCastToSqlDate;

    private String value1JSONException;
    private Class<?> value2JSONException;
    private JSONObject jsonJSONException;

    private Timestamp expectedCastToTimestamp;
    private Object valueCastToTimestamp;

    private Timestamp expectedCastToTimezone;
    private Object valueCastToTimezone;

    private BigDecimal expectedCastToBigDecimal;
    private BigDecimal valueCastToBigDecimal;

    private BigInteger expectedCastToBigInteger;
    private BigInteger valueCastToBigInteger;

    private Class<Integer[]> expectedCastArray;
    private ArrayList value1CastArray;
    private ParserConfig value2CastArray;

    private JSONObject jsonNotError;
    private Timestamp expectedNotError;
    private String value1NotError;
    private Class<Timestamp> value2NotError;

    private JSONObject jsonError;
    private Class<C> value1Error;
    private ParserConfig value2Error;

    private JSONObject jsonError2;
    private Type value1Error2;
    private ParserConfig value2Error2;

    public TypeUtilsParameterizedTest() throws NoSuchMethodException {
        wrapperConfigureCastToJavaBean();
        wrapperConfigureCast();
        wrapperConfigureCalendar();
        wrapperConfigureCastToSqlDate();
        wrapperConfigureJSONException();
        wrapperConfigureCastToTimestamp();

        configureUserTest(1L, "panlei", User.class); //test_2, test_3
        configureCastToTimezone(new Timestamp(0), "1970-01-01 08:00:00"); //test_cast_to_Timestamp_1970_01_01_00_00_00

        BigDecimal bd;
        configureCastToBigDecimal(bd = new BigDecimal("123"), bd); //test_cast_to_BigDecimal_same

        BigInteger bi;
        configureCastToBigInteger(bi = new BigInteger("123"), bi); //test_cast_to_BigInteger_same

        configureCastArray(Integer[].class, new ArrayList(), null); //test_cast_Array
        configureNotError("date", -1, java.sql.Timestamp.class, new Timestamp(-1L)); //test_cast_to_Timestamp_not_error
        configureError("id", 1, C.class); //test_error
        configureError2("id", 1, "f", List.class); //test_error_2
    }

    private void wrapperConfigureCastToTimestamp() {
        configureCastToTimestamp(null, null); //test_cast_to_Timestamp_null2

        /*
        Timestamp timestamp;
        configureCastToTimestamp(timestamp = new java.sql.Timestamp(System.currentTimeMillis()), timestamp); //test_cast_to_Timestamp_sql_Timestamp
         */
    }

    private void wrapperConfigureJSONException() {
        configureJSONException("value", new A(), B.class); //test_cast_ab_error
        //configureJSONException("date", 0, java.sql.Date.class); //test_cast_to_SqlDate_error
    }

    private void wrapperConfigureCastToSqlDate() {
        configureCastToSqlDate(null, null); //test_cast_to_SqlDate_null2

        /*
        java.sql.Date date;
        configureCastToSqlDate(date = new java.sql.Date(System.currentTimeMillis()), date); //test_cast_to_SqlDate_sql_Date2
         */
    }

    private void wrapperConfigureCalendar() {
        long millis;

        configureCalendar(millis = System.currentTimeMillis(), "date", new java.sql.Date(millis), java.sql.Date.class); //test_cast_to_SqlDate_calendar
        //configureCalendar(millis = System.currentTimeMillis(), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class); //test_cast_to_Timestamp_calendar
    }

    private void wrapperConfigureCast() {
        configureCast(1L, "id", 1, int.class); //test_cast_Integer

        /*
        configureCast(1L, "id", 1, Integer.class); //test_cast_Integer_2
        configureCast(1, "id", 1L, long.class); //test_cast_to_long
        configureCast(1, "id", 1L, Long.class); //test_cast_to_Long
        configureCast(1, "id", (short) 1, short.class); //test_cast_to_short
        configureCast(1, "id", (short) 1, Short.class); //test_cast_to_Short
        configureCast(1, "id", (byte) 1, byte.class); //test_cast_to_byte
        configureCast(1, "id", (byte) 1, Byte.class); //test_cast_to_Byte
        configureCast(1, "id", new BigInteger("1"), BigInteger.class); //test_cast_to_BigInteger
        configureCast(1, "id", new BigDecimal("1"), BigDecimal.class); //test_cast_to_BigDecimal
        configureCast(1, "id", Boolean.TRUE, boolean.class); //test_cast_to_boolean
        configureCast(1, "id", Boolean.TRUE, Boolean.class); //test_cast_to_Boolean
        configureCast(null, "id", null, Boolean.class); //test_cast_null
        configureCast(1, "id", "1", String.class); //test_cast_to_String
        configureCast(millis = System.currentTimeMillis(), "date", new Date(millis), Date.class); //test_cast_to_Date
        configureCast(millis = System.currentTimeMillis(), "date", new java.sql.Date(millis), java.sql.Date.class); //test_cast_to_SqlDate
        configureCast(Long.toString(millis = System.currentTimeMillis()), "date", new java.sql.Date(millis), java.sql.Date.class); //test_cast_to_SqlDate_string
        configureCast(null, "date", null, java.sql.Date.class); //test_cast_to_SqlDate_null
        configureCast(new Date(millis = System.currentTimeMillis()), "date", new java.sql.Date(millis), java.sql.Date.class); //test_cast_to_SqlDate_util_Date
        configureCast(new java.sql.Date(millis = System.currentTimeMillis()), "date", new java.sql.Date(millis), java.sql.Date.class); //test_cast_to_SqlDate_sql_Date
        configureCast(millis = System.currentTimeMillis(), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class); //test_cast_to_Timestamp
        configureCast(Long.toString(millis = System.currentTimeMillis()), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class); //test_cast_to_Timestamp_string
        configureCast(new BigDecimal(Long.toString(millis = System.currentTimeMillis())), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class); //test_cast_to_Timestamp_number
        configureCast(null, "date", null, java.sql.Timestamp.class); //test_cast_to_Timestamp_null
        configureCast(new Date(millis = System.currentTimeMillis()), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class); //test_cast_to_Timestamp_util_Date
        configureCast(new java.sql.Date(millis = System.currentTimeMillis()), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class); //test_cast_to_Timestamp_sql_Date

        B b;
        configureCast(b = new B(), "value", b, A.class); //test_cast_ab
        configureCast(b = new B(), "value", b, IA.class); //test_cast_ab_1
        */
    }

    private void wrapperConfigureCastToJavaBean() {
        HashMap map;
        configureCastToJavaBean(map = new HashMap(), map, Map.class); //test_0

        /*
        JSONObject json;
        configureCastToJavaBean(json = new JSONObject(), json, Map.class); //test_1
         */
    }

    public void configureCastToJavaBean(Object param1, Object param2, Class<Map> param3) {
        this.expectedCastToJavaBean = param1;
        this.value1CastToJavaBean = param2;
        this.value2CastToJavaBean = param3;
    }

    public void configureUserTest(long param1, String param2, Class<User> param3) {
        JSONObject localParam = new JSONObject();

        localParam.put("id", 1);
        localParam.put("name", "panlei");

        //Params
        this.expected1UserTest = param1;
        this.value1UserTest = localParam;
        this.expected2UserTest = param2;
        this.value2UserTest = param3;
    }

    public void configureCast(Object param1, String param2, Object param3, Class<?> param4) {
        JSONObject localParam = new JSONObject();

        localParam.put(param2, param1);

        //Params
        this.expectedCast = param3;
        this.value1Cast = param2;
        this.value2Cast = param4;

        this.jsonCast = localParam;
    }

    public void configureCalendar(Object param1, String param2, Object param3, Class<?> param4) {
        JSONObject localParam = new JSONObject();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((Long) param1);

        localParam.put(param2, calendar);

        //Params
        this.expectedCalendar = param3;
        this.value1Calendar = param2;
        this.value2Calendar = param4;

        this.jsonCalendar = localParam;
    }

    public void configureCastToSqlDate(java.sql.Date param1, java.sql.Date param2) {
        this.expectedCastToSqlDate = param1;
        this.valueCastToSqlDate = param2;
    }

    public void configureJSONException(String param1, Object param2, Class<?> param3) {
        JSONObject localParam = new JSONObject();
        localParam.put(param1, param2);

        this.jsonJSONException = localParam;
        this.value1JSONException = param1;
        this.value2JSONException = param3;
    }

    public void configureCastToTimestamp(Timestamp param1, Object param2) {
        this.expectedCastToTimestamp = param1;
        this.valueCastToTimestamp = param2;
    }

    public void configureCastToTimezone(Timestamp param1, Object param2) {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");

        this.expectedCastToTimezone = param1;
        this.valueCastToTimezone = param2;
    }

    public void configureCastToBigDecimal(BigDecimal param1, BigDecimal param2) {
        this.expectedCastToBigDecimal = param1;
        this.valueCastToBigDecimal = param2;
    }

    public void configureCastToBigInteger(BigInteger param1, BigInteger param2) {
        this.expectedCastToBigInteger = param1;
        this.valueCastToBigInteger = param2;
    }

    public void configureCastArray(Class<Integer[]> param1, ArrayList param2, ParserConfig param3) {
        this.expectedCastArray = param1;
        this.value1CastArray = param2;
        this.value2CastArray = param3;
    }

    public void configureNotError(String param1, int param2, Class<java.sql.Timestamp> param3, Timestamp param4) {
        JSONObject localParam = new JSONObject();
        localParam.put(param1, param2);

        this.jsonNotError = localParam;

        this.expectedNotError = param4;
        this.value1NotError = param1;
        this.value2NotError = param3;
    }

    public void configureError(String param1, int param2, Class<C> param3) {
        JSONObject localParam = new JSONObject();
        localParam.put(param1, param2);

        this.jsonError = localParam;

        this.value1Error = param3;
        this.value2Error = ParserConfig.getGlobalInstance();
    }

    public void configureError2(String param1, int param2, String param3, Class<List> param4) throws NoSuchMethodException {

        JSONObject localParam = new JSONObject();
        localParam.put(param1, param2);

        this.jsonError2 = localParam;

        Method method = TypeUtilsParameterizedTest.class.getMethod(param3, param4);
        this.value1Error2 = method.getGenericParameterTypes()[0];

        this.value2Error2 = ParserConfig.getGlobalInstance();
    }


    @Test
    public void test_castToJavaBean() {
        Assert.assertTrue(expectedCastToJavaBean == TypeUtils.castToJavaBean(value1CastToJavaBean, value2CastToJavaBean));
    }

    @Test
    public void test_2() {
        User user = TypeUtils.castToJavaBean(value1UserTest, value2UserTest);

        Assert.assertEquals(expected1UserTest, user.getId());
        Assert.assertEquals(expected2UserTest, user.getName());
    }

    @Test
    public void test_3() {
        User user = JSON.toJavaObject(value1UserTest, value2UserTest);

        Assert.assertEquals(expected1UserTest, user.getId());
        Assert.assertEquals(expected2UserTest, user.getName());
    }

    @Test
    public void test_getObject() {
        Assert.assertEquals(expectedCast, jsonCast.getObject(value1Cast, value2Cast));
    }

    @Test
    public void test_getObject_calendar() {
        Assert.assertEquals(expectedCalendar, jsonCalendar.getObject(value1Calendar, value2Calendar));
    }

    @Test
    public void test_castToSqlDate() {
        Assert.assertEquals(expectedCastToSqlDate, TypeUtils.castToSqlDate(valueCastToSqlDate));
    }

    @Test
    public void test_JSONException() {

        JSONException error = null;
        try {
            jsonJSONException.getObject(value1JSONException, value2JSONException);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    @Test
    public void test_castToTimestamp() {
        Assert.assertEquals(expectedCastToTimestamp, TypeUtils.castToTimestamp(valueCastToTimestamp));
    }

    @Test
    public void test_castToTimezone() {
        Assert.assertEquals(expectedCastToTimezone, TypeUtils.castToTimestamp(valueCastToTimezone));
    }

    @Test
    public void test_cast_to_BigDecimal_same() {
        Assert.assertEquals(expectedCastToBigDecimal, TypeUtils.castToBigDecimal(valueCastToBigDecimal));
    }

    @Test
    public void test_cast_to_BigInteger_same() {
        Assert.assertEquals(expectedCastToBigInteger, TypeUtils.castToBigInteger(valueCastToBigInteger));
    }

    @Test
    public void test_castArray() {
        Assert.assertEquals(expectedCastArray, TypeUtils.cast(value1CastArray, expectedCastArray, value2CastArray).getClass());
    }

    @Test
    public void test_not_error() {

        JSONException error = null;
        try {
            jsonNotError.getObject(value1NotError, value2NotError);
        } catch (JSONException e) {
            error = e;
        }

        Assert.assertNull(error);
        Assert.assertEquals(expectedNotError, jsonNotError.getObject(value1NotError, value2NotError));
    }

    @Test
    public void test_error() {

        JSONException error = null;
        try {
            TypeUtils.castToJavaBean(jsonError, value1Error, value2Error);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    @Test
    public void test_error_2() {
        Throwable error = null;
        try {
            TypeUtils.cast(jsonError2, value1Error2, value2Error2);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
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

    public static void f(List<?> list) {

    }
}
