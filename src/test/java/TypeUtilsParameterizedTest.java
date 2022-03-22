package test.java;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

/**
 * Version 5:
 *      Last version.
 * */
@RunWith(Parameterized.class)
@SuppressWarnings("rawtypes")
public class TypeUtilsParameterizedTest extends TestCase {

    private final ConfigType configType;
    private final TestType testType;
    private Object param1;
    private Object param2;
    private Object param3;
    private Object param4;

    enum ConfigType{
        TEST_0, TEST_1, TEST_2, TEST_INT, TEST_INT_2, TEST_LONG, TEST_LONG_2, TEST_SHORT, TEST_SHORT_2, TEST_BYTE,
        TEST_BYTE_2, TEST_BIGINTEGER, TEST_BIGDECIMAL, TEST_BOOLEAN, TEST_BOOLEAN_2, TEST_NULL, TEST_STRING,
        TEST_DATE, TEST_SQL_DATE, TEST_SQL_DATE_STRING, TEST_SQL_DATE_NULL, TEST_SQL_DATE_NULL_2, TEST_SQL_DATE_UTIL,
        TEST_SQL_DATE_SQL, TEST_SQL_DATE_SQL_2, TEST_SQL_DATE_CALENDAR, TEST_SQL_DATE_ERROR, TEST_TIMESTAMP,
        TEST_TIMESTAMP_STRING, TEST_TIMESTAMP_NUMBER, TEST_TIMESTAMP_NULL, TEST_TIMESTAMP_NULL_2, TEST_TIMESTAMP_1970,
        TEST_BIGDECIMAL_SAME, TEST_BIGINTEGER_SAME, TEST_ARRAY, TEST_TIMESTAMP_UTIL, TEST_TIMESTAMP_SQL_DATE,
        TEST_TIMESTAMP_TIMESTAMP, TEST_TIMESTAMP_CALENDAR, TEST_TIMESTAMP_NOT_ERROR, TEST_AB, TEST_AB_1,
        TEST_AB_ERROR, TEST_ERROR, TEST_ERROR_2, TEST_3
    }

    enum TestType{
        TEST_EQUALS, TEST_NOT_NULL, TEST_DOUBLE_EQUALS, TEST_NULL_EQUALS
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters(){

        Object[][] params = {
                {ConfigType.TEST_0, TestType.TEST_EQUALS, new HashMap(), null, null, null}, //0
                {ConfigType.TEST_1, TestType.TEST_EQUALS, new JSONObject(), null, null, null}, //1
                {ConfigType.TEST_2, TestType.TEST_DOUBLE_EQUALS, 1L, null, "panlei", null}, //2
                {ConfigType.TEST_INT, TestType.TEST_EQUALS, new Integer(1), null, null, null}, //3
                {ConfigType.TEST_INT_2, TestType.TEST_EQUALS, new Integer(1), null, null, null}, //4
                {ConfigType.TEST_LONG, TestType.TEST_EQUALS, new Long(1), null, null, null}, //5
                {ConfigType.TEST_LONG_2, TestType.TEST_EQUALS, new Long(1), null, null, null}, //6
                {ConfigType.TEST_SHORT, TestType.TEST_EQUALS, new Short((short) 1), null, null, null}, //7
                {ConfigType.TEST_SHORT_2, TestType.TEST_EQUALS, new Short((short) 1), null, null, null}, //8
                {ConfigType.TEST_BYTE, TestType.TEST_EQUALS, new Byte((byte) 1), null, null, null}, //9
                {ConfigType.TEST_BYTE_2, TestType.TEST_EQUALS, new Byte((byte) 1), null, null, null}, //10
                {ConfigType.TEST_BIGINTEGER, TestType.TEST_EQUALS, new BigInteger("1"), null, null, null}, //11
                {ConfigType.TEST_BIGDECIMAL, TestType.TEST_EQUALS, new BigDecimal("1"), null, null, null}, //12
                {ConfigType.TEST_BOOLEAN, TestType.TEST_EQUALS, Boolean.TRUE, null, null, null}, //13
                {ConfigType.TEST_BOOLEAN_2, TestType.TEST_EQUALS, Boolean.TRUE, null, null, null}, //14
                {ConfigType.TEST_NULL, TestType.TEST_EQUALS, null, null, null, null}, //15
                {ConfigType.TEST_STRING, TestType.TEST_EQUALS, "1", null, null, null}, //16
                {ConfigType.TEST_DATE, TestType.TEST_EQUALS, null, null, null, null}, //17
                {ConfigType.TEST_SQL_DATE, TestType.TEST_EQUALS, null, null, null, null}, //18
                {ConfigType.TEST_SQL_DATE_STRING, TestType.TEST_EQUALS, null, null, null, null}, //19
                {ConfigType.TEST_SQL_DATE_NULL, TestType.TEST_EQUALS, null, null, null, null}, //20
                {ConfigType.TEST_SQL_DATE_NULL_2, TestType.TEST_EQUALS, null, TypeUtils.castToSqlDate(null), null, null}, //21
                {ConfigType.TEST_SQL_DATE_UTIL, TestType.TEST_EQUALS, null, null, null, null}, //22
                {ConfigType.TEST_SQL_DATE_SQL, TestType.TEST_EQUALS, null, null, null, null}, //23
                {ConfigType.TEST_SQL_DATE_SQL_2, TestType.TEST_EQUALS, null, null, null, null}, //24
                {ConfigType.TEST_SQL_DATE_CALENDAR, TestType.TEST_EQUALS, null, null, null, null}, //25
                {ConfigType.TEST_SQL_DATE_ERROR, TestType.TEST_NOT_NULL, null, null, null, null}, //26
                {ConfigType.TEST_TIMESTAMP, TestType.TEST_EQUALS, null, null, null, null}, //27
                {ConfigType.TEST_TIMESTAMP_STRING, TestType.TEST_EQUALS, null, null, null, null}, //28
                {ConfigType.TEST_TIMESTAMP_NUMBER, TestType.TEST_EQUALS, null, null, null, null}, //29
                {ConfigType.TEST_TIMESTAMP_NULL, TestType.TEST_EQUALS, null, null, null, null}, //30
                {ConfigType.TEST_TIMESTAMP_NULL_2, TestType.TEST_EQUALS, null, TypeUtils.castToTimestamp(null), null, null}, //31
                {ConfigType.TEST_TIMESTAMP_1970, TestType.TEST_EQUALS, new Timestamp(0), null, null, null}, //32
                {ConfigType.TEST_BIGDECIMAL_SAME, TestType.TEST_EQUALS, true, null, null, null}, //33
                {ConfigType.TEST_BIGINTEGER_SAME, TestType.TEST_EQUALS, true, null, null, null}, //34
                {ConfigType.TEST_ARRAY, TestType.TEST_EQUALS, Integer[].class, TypeUtils.cast(new ArrayList(), Integer[].class, null).getClass(), null, null}, //35
                {ConfigType.TEST_TIMESTAMP_UTIL, TestType.TEST_EQUALS, null, null, null, null}, //36
                {ConfigType.TEST_TIMESTAMP_SQL_DATE, TestType.TEST_EQUALS, null, null, null, null}, //37
                {ConfigType.TEST_TIMESTAMP_TIMESTAMP, TestType.TEST_EQUALS, null, null, null, null}, //38
                {ConfigType.TEST_TIMESTAMP_CALENDAR, TestType.TEST_EQUALS, null, null, null, null}, //39
                {ConfigType.TEST_TIMESTAMP_NOT_ERROR, TestType.TEST_NULL_EQUALS, null, new Timestamp(-1L), null, null}, //40
                {ConfigType.TEST_AB, TestType.TEST_EQUALS, null, null, null, null}, //41
                {ConfigType.TEST_AB_1, TestType.TEST_EQUALS, null, null, null, null}, //42
                {ConfigType.TEST_AB_ERROR, TestType.TEST_NOT_NULL, null, null, null, null}, //43
                {ConfigType.TEST_ERROR, TestType.TEST_NOT_NULL, null, null, null, null}, //44
                {ConfigType.TEST_ERROR_2, TestType.TEST_NOT_NULL, null, null, null, null}, //45
                {ConfigType.TEST_3, TestType.TEST_DOUBLE_EQUALS, 1L, null, "panlei", null} //46
        };

        return Arrays.asList(params);
    }

    public TypeUtilsParameterizedTest(ConfigType configType, TestType testType, Object param1, Object param2, Object param3, Object param4){
        this.configType = configType;
        this.testType = testType;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
    }

    @Before
    public void configureTest() throws NoSuchMethodException {
        switch(configType) {
            case TEST_0:
            case TEST_1:
                param2 = TypeUtils.castToJavaBean(param1, Map.class);
                break;
            case TEST_2:
            case TEST_3:
                configureTest2();
                break;
            case TEST_INT:
                configureTestCastId(int.class);
                break;
            case TEST_INT_2:
                configureTestCastId(Integer.class);
                break;
            case TEST_LONG:
                configureTestCastId(long.class);
                break;
            case TEST_LONG_2:
                configureTestCastId(Long.class);
                break;
            case TEST_SHORT:
                configureTestCastId(short.class);
                break;
            case TEST_SHORT_2:
                configureTestCastId(Short.class);
                break;
            case TEST_BYTE:
                configureTestCastId(byte.class);
                break;
            case TEST_BYTE_2:
                configureTestCastId(Byte.class);
                break;
            case TEST_BIGINTEGER:
                configureTestCastId(BigInteger.class);
                break;
            case TEST_BIGDECIMAL:
                configureTestCastId(BigDecimal.class);
                break;
            case TEST_BOOLEAN:
                configureTestCastId(boolean.class);
                break;
            case TEST_BOOLEAN_2:
            case TEST_NULL:
                configureTestCastId(Boolean.class);
                break;
            case TEST_STRING:
                configureTestCastId(String.class);
                break;
            case TEST_DATE:
                configureTestCastDate(Date.class);
                break;
            case TEST_SQL_DATE:
            case TEST_SQL_DATE_STRING:
            case TEST_SQL_DATE_NULL:
            case TEST_SQL_DATE_UTIL:
            case TEST_SQL_DATE_SQL:
                configureTestCastDate(java.sql.Date.class);
                break;
            case TEST_SQL_DATE_SQL_2:
            case TEST_TIMESTAMP_TIMESTAMP:
                configureTestCastDate2();
                break;
            case TEST_SQL_DATE_CALENDAR:
                configureTestCastCalendar(java.sql.Date.class);
                break;
            case TEST_SQL_DATE_ERROR:
                configureError(java.sql.Date.class);
                break;
            case TEST_TIMESTAMP:
            case TEST_TIMESTAMP_STRING:
            case TEST_TIMESTAMP_NUMBER:
            case TEST_TIMESTAMP_NULL:
            case TEST_TIMESTAMP_UTIL:
            case TEST_TIMESTAMP_SQL_DATE:
                configureTestCastDate(Timestamp.class);
                break;
            case TEST_TIMESTAMP_1970:
                JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
                param2 = TypeUtils.castToTimestamp("1970-01-01 08:00:00");
                break;
            case TEST_BIGDECIMAL_SAME:
                BigDecimal val = new BigDecimal("123");
                param2 = val.equals(TypeUtils.castToBigDecimal(val));
                break;
            case TEST_BIGINTEGER_SAME:
                BigInteger value = new BigInteger("123");
                param2 = value.equals(TypeUtils.castToBigInteger(value));
                break;
            case TEST_TIMESTAMP_CALENDAR:
                configureTestCastCalendar(Timestamp.class);
                break;
            case TEST_TIMESTAMP_NOT_ERROR:
                configureError(Timestamp.class);
                break;
            case TEST_AB:
                configureTestCastValue(A.class);
                break;
            case TEST_AB_1:
                configureTestCastValue(IA.class);
                break;
            case TEST_AB_ERROR:
                configureError(B.class);
                break;
            case TEST_ERROR:
                configureError(C.class);
                break;
            case TEST_ERROR_2:
                configureError2(List.class);
                break;
        }
    }

    private void configureTest2(){
        JSONObject map = new JSONObject();
        map.put("id", 1);
        map.put("name", "panlei");

        User user;

        if(configType.equals(ConfigType.TEST_2)){
            user = TypeUtils.castToJavaBean(map, User.class);
        }else{
            user = JSON.toJavaObject(map, User.class);
        }

        param2 = user.getId();
        param4 = user.getName();
    }

    private void configureTestCastId(Class classParam){
        JSONObject json = new JSONObject();

        if(configType.equals(ConfigType.TEST_INT) || configType.equals(ConfigType.TEST_INT_2)){
            json.put("id", 1L);
        }else if(configType.equals(ConfigType.TEST_NULL)){
            json.put("id", null);
        }else{
            json.put("id", 1);
        }
        param2 = json.getObject("id", classParam);
    }

    private void configureTestCastDate(Class classParam){
        long millis = System.currentTimeMillis();
        JSONObject json = new JSONObject();

        switch (configType){
            case TEST_DATE:
                json.put("date", millis);
                param1 = new Date(millis);
                break;
            case TEST_SQL_DATE:
                json.put("date", millis);
                param1 = new java.sql.Date(millis);
                break;
            case TEST_SQL_DATE_STRING:
                json.put("date", Long.toString(millis));
                param1 = new java.sql.Date(millis);
                break;
            case TEST_SQL_DATE_NULL:
            case TEST_TIMESTAMP_NULL:
                json.put("date", null);
                break;
            case TEST_SQL_DATE_UTIL:
                json.put("date", new Date(millis));
                param1 = new java.sql.Date(millis);
                break;
            case TEST_SQL_DATE_SQL:
                json.put("date", new java.sql.Date(millis));
                param1 = new java.sql.Date(millis);
                break;
            case TEST_TIMESTAMP:
                json.put("date", millis);
                param1 = new Timestamp(millis);
                break;
            case TEST_TIMESTAMP_STRING:
                json.put("date", Long.toString(millis));
                param1 = new Timestamp(millis);
                break;
            case TEST_TIMESTAMP_NUMBER:
                json.put("date", new BigDecimal(Long.toString(millis)));
                param1 = new Timestamp(millis);
                break;
            case TEST_TIMESTAMP_UTIL:
                json.put("date", new Date(millis));
                param1 = new Timestamp(millis);
                break;
            case TEST_TIMESTAMP_SQL_DATE:
                json.put("date", new java.sql.Date(millis));
                param1 = new Timestamp(millis);
                break;
        }

        param2 = json.getObject("date", classParam);
    }

    private void configureTestCastDate2(){
        long millis = System.currentTimeMillis();

        if(configType.equals(ConfigType.TEST_SQL_DATE_SQL_2)){
            java.sql.Date date = new java.sql.Date(millis);

            param1 = date;
            param2 = TypeUtils.castToSqlDate(date);
        }else{
            Timestamp date = new Timestamp(millis);

            param1 = date;
            param2 = TypeUtils.castToTimestamp(date);
        }
    }

    private void configureTestCastCalendar(Class classParam){
        long millis = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        JSONObject json = new JSONObject();
        json.put("date", calendar);

        if(configType.equals(ConfigType.TEST_SQL_DATE_CALENDAR)){
            param1 = new java.sql.Date(millis);
        }else{
            param1 = new Timestamp(millis);
        }

        param2 = json.getObject("date", classParam);
    }

    private void configureError(Class classParam){
        JSONObject json = new JSONObject();
        JSONException error = null;

        try {
            switch(configType){
                case TEST_SQL_DATE_ERROR:
                    json.put("date", 0);
                    json.getObject("date", classParam);
                    break;
                case TEST_TIMESTAMP_NOT_ERROR:
                    json.put("date", -1);
                    json.getObject("date", classParam);
                    param3 = (Timestamp) json.getObject("date", Timestamp.class);
                    break;
                case TEST_AB_ERROR:
                    A a = new A();
                    json.put("value", a);
                    json.getObject("value", classParam);
                    break;
                case TEST_ERROR:
                    json.put("id", 1);
                    TypeUtils.castToJavaBean(json, classParam, ParserConfig.getGlobalInstance());
                    break;
            }
        } catch (JSONException e) {
            error = e;
        }

        param1 = error;
    }

    private void configureTestCastValue(Class classParam){
        B b = new B();

        JSONObject json = new JSONObject();
        json.put("value", b);

        param1 = b;
        param2 = json.getObject("value", classParam);
    }

    private void configureError2(Class classParam) throws NoSuchMethodException {
        JSONObject json = new JSONObject();
        json.put("id", 1);

        Method method = TypeUtilsParameterizedTest.class.getMethod("f", classParam);

        Throwable error = null;
        try {
            TypeUtils.cast(json, method.getGenericParameterTypes()[0], ParserConfig.getGlobalInstance());
        } catch (JSONException ex) {
            error = ex;
        }

        param1 = error;
    }

    /*
    * Includes:
    *   test_0
    *   test_1
    *   test_cast_Integer
    *   test_cast_Integer_2
    *   test_cast_to_long
    *   test_cast_to_Long
    *   test_cast_to_short
    *   test_cast_to_Short
    *   test_cast_to_byte
    *   test_cast_to_Byte
    *   test_cast_to_BigInteger
    *   test_cast_to_BigDecimal
    *   test_cast_to_boolean
    *   test_cast_to_Boolean
    *   test_cast_null
    *   test_cast_to_String
    *   test_cast_to_Date
    *   test_cast_to_SqlDate
    *   test_cast_to_SqlDate_string
    *   test_cast_to_SqlDate_null
    *   test_cast_to_SqlDate_null2
    *   test_cast_to_SqlDate_util_Date
    *   test_cast_to_SqlDate_sql_Date
    *   test_cast_to_SqlDate_sql_Date2
    *   test_cast_to_SqlDate_calendar
    *   test_cast_to_Timestamp
    *   test_cast_to_Timestamp_string
    *   test_cast_to_Timestamp_number
    *   test_cast_to_Timestamp_null
    *   test_cast_to_Timestamp_null2
    *   test_cast_to_Timestamp_1970_01_01_00_00_00
    *   test_cast_to_BigDecimal_same
    *   test_cast_to_BigInteger_same
    *   test_cast_Array
    *   test_cast_to_Timestamp_util_Date
    *   test_cast_to_Timestamp_sql_Date
    *   test_cast_to_Timestamp_sql_Timestamp
    *   test_cast_to_Timestamp_calendar
    *   test_cast_ab
    *   test_cast_ab_1
    * */
    @Test
    public void testEquals(){
        Assume.assumeTrue(testType.equals(TestType.TEST_EQUALS));
        Assert.assertEquals(param1, param2);
    }

    /*
     * Includes:
     *   test_2
     *   test_3
     * */
    @Test
    public void testDoubleEquals(){
        Assume.assumeTrue(testType.equals(TestType.TEST_DOUBLE_EQUALS));

        Assert.assertEquals(param1, param2);
        Assert.assertEquals(param3, param4);
    }

    /*
    * Includes:
    *   test_cast_to_SqlDate_error
    *   test_cast_ab_error
    *   test_error
    *   test_error_2
    * */
    @Test
    public void testNotNull() {
        Assume.assumeTrue(testType.equals(TestType.TEST_NOT_NULL));
        Assert.assertNotNull(param1);
    }

    /*
    * Includes:
    *   test_cast_to_Timestamp_not_error
    * */
    @Test
    public void testNullEquals() {
        Assume.assumeTrue(testType.equals(TestType.TEST_NULL_EQUALS));
        Assert.assertNull(param1);
        Assert.assertEquals(param2, param3);
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

    public static class A implements TypeUtilsParameterizedTest.IA {

    }

    public static interface IA {

    }

    public static class B extends TypeUtilsParameterizedTest.A {

    }

    public static class C extends TypeUtilsParameterizedTest.B {

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