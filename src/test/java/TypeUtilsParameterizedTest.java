import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

@RunWith(Parameterized.class)
@SuppressWarnings("rawtypes")
public class TypeUtilsParameterizedTest {

    private final Type type; //Test type -- used to execute the correct test for the combination of params

    //TEST PARAMS
    private Object param1;
    private Object param2;
    private Object param3;
    private Object param4;

    enum Type{
        TEST_0,
        TEST_2,
        TEST_3,
        TEST_CAST,
        TEST_CALENDAR,
        TEST_CAST_ERROR,
        TEST_ERROR,
        TEST_ERROR_2,
        TEST_CAST_TO_BIGDECIMAL,
        TEST_CAST_TIMEZONE,
        TEST_CAST_TO_SQLDATE,
        TEST_CAST_TO_TIMESTAMP,
        TEST_ARRAY,
        TEST_CAST_TO_BIGINTEGER,
        TEST_NOT_ERROR
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters(){

        HashMap map;
        JSONObject json;
        long millis;
        java.sql.Date date;
        BigDecimal bd;
        BigInteger bi;
        Timestamp timestamp;
        B b;

        Object[][] params = {
        /*0*/    {Type.TEST_0, map=new HashMap(), map, Map.class, null}, //test_0
        /*1*/    {Type.TEST_0, json=new JSONObject(), json, Map.class, null}, //test_1
        /*2*/    {Type.TEST_2, 1L, "panlei", User.class, null}, //test_2
        /*3*/    {Type.TEST_CAST, 1L, "id", 1, int.class}, //test_cast_Integer
        /*4*/    {Type.TEST_CAST, 1L, "id", 1, Integer.class}, //test_cast_Integer_2
        /*5*/    {Type.TEST_CAST, 1, "id", 1L, long.class}, //test_cast_to_long
        /*6*/    {Type.TEST_CAST, 1, "id", 1L, Long.class}, //test_cast_to_Long
        /*7*/    {Type.TEST_CAST, 1, "id", (short) 1, short.class}, //test_cast_to_short
        /*8*/    {Type.TEST_CAST, 1, "id", (short) 1, Short.class}, //test_cast_to_Short
        /*9*/    {Type.TEST_CAST, 1, "id", (byte) 1, byte.class}, //test_cast_to_byte
        /*10*/    {Type.TEST_CAST, 1, "id", (byte) 1, Byte.class}, //test_cast_to_Byte
        /*12*/    {Type.TEST_CAST, 1, "id", new BigInteger("1"), BigInteger.class}, //test_cast_to_BigInteger
        /*13*/    {Type.TEST_CAST, 1, "id", new BigDecimal("1"), BigDecimal.class}, //test_cast_to_BigDecimal
        /*14*/    {Type.TEST_CAST, 1, "id", Boolean.TRUE, boolean.class}, //test_cast_to_boolean
        /*15*/    {Type.TEST_CAST, 1, "id", Boolean.TRUE, Boolean.class}, //test_cast_to_Boolean
        /*16*/    {Type.TEST_CAST, null, "id", null, Boolean.class}, //test_cast_null
        /*17*/    {Type.TEST_CAST, 1, "id", "1", String.class}, //test_cast_to_String
        /*18*/    {Type.TEST_CAST, millis=System.currentTimeMillis(), "date", new Date(millis), Date.class}, //test_cast_to_Date
        /*19*/    {Type.TEST_CAST, millis=System.currentTimeMillis(), "date", new java.sql.Date(millis), java.sql.Date.class}, //test_cast_to_SqlDate
        /*20*/    {Type.TEST_CAST, Long.toString(millis=System.currentTimeMillis()), "date", new java.sql.Date(millis), java.sql.Date.class}, //test_cast_to_SqlDate_string
        /*21*/    {Type.TEST_CAST, null, "date", null, java.sql.Date.class}, //test_cast_to_SqlDate_null
        /*22*/    {Type.TEST_CAST_TO_SQLDATE, null, null, null, null}, //test_cast_to_SqlDate_null2
        /*23*/    {Type.TEST_CAST, new Date(millis=System.currentTimeMillis()), "date", new java.sql.Date(millis), java.sql.Date.class}, //test_cast_to_SqlDate_util_Date
        /*24*/    {Type.TEST_CAST, new java.sql.Date(millis=System.currentTimeMillis()), "date", new java.sql.Date(millis), java.sql.Date.class}, //test_cast_to_SqlDate_sql_Date
        /*25*/    {Type.TEST_CAST_TO_SQLDATE, date=new java.sql.Date(System.currentTimeMillis()), date, null, null}, //test_cast_to_SqlDate_sql_Date2
        /*26*/    {Type.TEST_CALENDAR, millis=System.currentTimeMillis(), "date", new java.sql.Date(millis), java.sql.Date.class}, //test_cast_to_SqlDate_calendar
        /*27*/    {Type.TEST_CAST_ERROR, "date", 0, java.sql.Date.class, null}, //test_cast_to_SqlDate_error
        /*28*/    {Type.TEST_CAST, millis=System.currentTimeMillis(), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class}, //test_cast_to_Timestamp
        /*29*/    {Type.TEST_CAST, Long.toString(millis=System.currentTimeMillis()), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class}, //test_cast_to_Timestamp_string
        /*30*/    {Type.TEST_CAST, new BigDecimal(Long.toString(millis=System.currentTimeMillis())), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class}, //test_cast_to_Timestamp_number
        /*31*/    {Type.TEST_CAST, null, "date", null, java.sql.Timestamp.class}, //test_cast_to_Timestamp_null
        /*32*/    {Type.TEST_CAST_TO_TIMESTAMP, null, null, null, null}, //test_cast_to_Timestamp_null2
        /*33*/    {Type.TEST_CAST_TIMEZONE, "Asia/Shanghai", new Timestamp(0), "1970-01-01 08:00:00", null}, //test_cast_to_Timestamp_1970_01_01_00_00_00
        /*34*/    {Type.TEST_CAST_TO_BIGDECIMAL, bd=new BigDecimal("123"), bd, null, null}, //test_cast_to_BigDecimal_same
        /*35*/    {Type.TEST_CAST_TO_BIGINTEGER, bi=new BigInteger("123"), bi, null, null}, //test_cast_to_BigInteger_same
        /*36*/    {Type.TEST_ARRAY, Integer[].class, new ArrayList(), null, null}, //test_cast_Array
        /*37*/    {Type.TEST_CAST, new Date(millis=System.currentTimeMillis()), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class}, //test_cast_to_Timestamp_util_Date
        /*38*/    {Type.TEST_CAST, new java.sql.Date(millis=System.currentTimeMillis()), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class}, //test_cast_to_Timestamp_sql_Date
        /*39*/    {Type.TEST_CAST_TO_TIMESTAMP, timestamp=new java.sql.Timestamp(System.currentTimeMillis()), timestamp, null, null}, //test_cast_to_Timestamp_sql_Timestamp
        /*40*/    {Type.TEST_CALENDAR, millis=System.currentTimeMillis(), "date", new java.sql.Timestamp(millis), java.sql.Timestamp.class}, //test_cast_to_Timestamp_calendar
        /*41*/    {Type.TEST_NOT_ERROR, "date", -1, java.sql.Timestamp.class, new Timestamp(-1L)}, //test_cast_to_Timestamp_not_error
        /*42*/    {Type.TEST_CAST, b=new B(), "value", b, A.class}, //test_cast_ab
        /*43*/    {Type.TEST_CAST, b=new B(), "value", b, IA.class}, //test_cast_ab_1
        /*44*/    {Type.TEST_CAST_ERROR, "value", new A(), B.class, null}, //test_cast_ab_error
        /*45*/    {Type.TEST_ERROR, "id", 1, C.class, null}, //test_error
        /*46*/    {Type.TEST_ERROR_2, "id", 1, "f", List.class}, //test_error_2
        /*47*/    {Type.TEST_3, 1L, "panlei", User.class, null}, //test_3
        };

        return Arrays.asList(params);
    }

    public TypeUtilsParameterizedTest(Type type, Object objParam, Object objParam2, Object objParam3, Object objParam4) throws NoSuchMethodException {
        this.type = type;

        switch(type){
            case TEST_2: case TEST_3:
                configureTest2(objParam, objParam2, objParam3);
                break;

            case TEST_CAST: case TEST_CALENDAR:
                configureTestCast(objParam, objParam2, objParam3, objParam4);
                break;

            case TEST_CAST_ERROR:case TEST_NOT_ERROR:
                configureTestCastError(objParam, objParam2, objParam3, objParam4);
                break;

            case TEST_ERROR:case TEST_ERROR_2:
                configureTestError(objParam, objParam2, objParam3, objParam4);
                break;

            case TEST_CAST_TIMEZONE:
                configureTestTimezone(objParam, objParam2, objParam3);
                break;

            default:
                this.param1 = objParam;
                this.param2 = objParam2;
                this.param3 = objParam3;
                this.param4 = objParam4;
                break;
        }
    }

    //----------------------------------------------------- CONFIG -------------------------------------------------

    public void configureTest2(Object objParam, Object objParam2, Object objParam3){
        JSONObject localParam = new JSONObject();

        localParam.put("id", 1);
        localParam.put("name", objParam2);

        //Params
        this.param1 = objParam;
        this.param2 = localParam;
        this.param3 = objParam2;
        this.param4 = objParam3;
    }

    public void configureTestCast(Object objParam, Object objParam2, Object objParam3, Object objParam4){
        JSONObject localParam = new JSONObject();

        if(type.equals(Type.TEST_CALENDAR)){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis((Long) objParam);

            localParam.put((String) objParam2, calendar);
        }else{
            localParam.put((String) objParam2, objParam);
        }

        //Params
        this.param1 = objParam3; //object1
        this.param2 = localParam; //object2
        this.param3 = objParam2;
        this.param4 = objParam4;
    }

    public void configureTestCastError(Object objParam, Object objParam2, Object objParam3, Object objParam4) {
        JSONObject localParam = new JSONObject();
        localParam.put((String) objParam, objParam2);

        this.param1 = localParam;
        this.param2 = objParam;
        this.param3 = objParam3;
        this.param4 = objParam4;
    }

    public void configureTestError(Object objParam, Object objParam2, Object objParam3, Object objParam4) throws NoSuchMethodException {
        JSONObject localParam = new JSONObject();
        localParam.put((String) objParam, objParam2);
        this.param1 = localParam;
        this.param3 = ParserConfig.getGlobalInstance();

        if(type.equals(Type.TEST_ERROR)){
            this.param2 = objParam3;
        }else{
            Method method = TypeUtilsParameterizedTest.class.getMethod((String) objParam3, (Class<?>) objParam4);
            this.param2 = method.getGenericParameterTypes()[0];
        }
    }

    public void configureTestTimezone(Object objParam, Object objParam2, Object objParam3) {
        JSON.defaultTimeZone = TimeZone.getTimeZone((String) objParam);
        this.param1 = objParam2;
        this.param2 = objParam3;
    }


    //-------------------------------------------------- TESTS --------------------------------------------------
    /*
    * Includes:
    *   test_0
    *   test_1
    * */
    @Test
    public void test_0() {
        Assume.assumeTrue(type.equals(Type.TEST_0));
        Assert.assertTrue(param1 == TypeUtils.castToJavaBean(param2, (Class<?>)param3));
    }

    @Test
    public void test_2() {
        Assume.assumeTrue(type.equals(Type.TEST_2));

        User user = (User) TypeUtils.castToJavaBean(param2, (Class<?>) param4);

        Assert.assertEquals(param1, user.getId());
        Assert.assertEquals(param3, user.getName());
    }

    /*
    * Includes:
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
    *   test_cast_to_Date   +
    *   test_cast_to_SqlDate    +
    *   test_cast_to_SqlDate_string +
    *   test_cast_to_SqlDate_null
    *   test_cast_to_SqlDate_util_Date  +
    *   test_cast_to_SqlDate_sql_Date   +
    *   test_cast_to_SqlDate_calendar
    *   test_cast_to_Timestamp  +
    *   test_cast_to_Timestamp_string   +
    *   test_cast_to_Timestamp_number   +
    *   test_cast_to_Timestamp_null
    *   test_cast_to_Timestamp_util_Date    +
    *   test_cast_to_Timestamp_sql_Date +
    *   test_cast_ab    -
    *   test_cast_ab_1  -
    *   test_cast_to_SqlDate_sql_Date2
    *   test_cast_to_Timestamp_sql_Timestamp
    *   test_cast_to_SqlDate_calendar
    *   test_cast_to_Timestamp_calendar
    * */
    @Test
    public void test_cast(){
        Assume.assumeTrue(type.equals(Type.TEST_CAST) || type.equals(Type.TEST_CALENDAR));

        JSONObject json = (JSONObject) param2;

        Assert.assertEquals(param1, json.getObject((String) param3, (Class<?>) param4));
    }

    @Test
    public void test_cast_to_SqlDate() {
        Assume.assumeTrue(type.equals(Type.TEST_CAST_TO_SQLDATE));

        Assert.assertEquals(param1, TypeUtils.castToSqlDate(param2));
    }

    @Test
    public void test_cast_to_Timestamp() {
        Assume.assumeTrue(type.equals(Type.TEST_CAST_TO_TIMESTAMP) || type.equals(Type.TEST_CAST_TIMEZONE));

        Assert.assertEquals(param1, TypeUtils.castToTimestamp(param2));
    }

    @Test
    public void test_cast_Array() {
        Assume.assumeTrue(type.equals(Type.TEST_ARRAY));

        Assert.assertEquals(param1, TypeUtils.cast(param2, (Class<?>) param1, (ParserConfig) param3).getClass());
    }

    /*
     * Includes:
     *   test_cast_to_SqlDate_error
     *   test_cast_ab_error
     */
    @Test
    public void test_cast_error() {
        Assume.assumeTrue(type.equals(Type.TEST_CAST_ERROR));

        JSONObject json = (JSONObject) param1;

        JSONException error = null;
        try {
            json.getObject((String) param2, (Class<?>) param3);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    @Test
    public void test_error() {
        Assume.assumeTrue(type.equals(Type.TEST_ERROR));

        JSONException error = null;
        try {
            TypeUtils.castToJavaBean((JSONObject) param1, (Class<?>) param2, (ParserConfig) param3);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    @Test
    public void test_error_2() {
        Assume.assumeTrue(type.equals(Type.TEST_ERROR_2));

        Throwable error = null;
        try {
            TypeUtils.cast(param1, (java.lang.reflect.Type) param2, (ParserConfig) param3);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    @Test
    public void test_cast_to_BigDecimal_same(){
        Assume.assumeTrue(type.equals(Type.TEST_CAST_TO_BIGDECIMAL));

        Assert.assertEquals(param1, TypeUtils.castToBigDecimal(param2));
    }

    @Test
    public void test_cast_to_BigInteger_same(){
        Assume.assumeTrue(type.equals(Type.TEST_CAST_TO_BIGINTEGER));
        Assert.assertEquals(param1, TypeUtils.castToBigInteger(param2));
    }

    /*
     * Includes:
     *   test_cast_to_Timestamp_not_error
     *   test_not_error
     */
    @Test
    public void test_not_error() {
        Assume.assumeTrue(type.equals(Type.TEST_NOT_ERROR));

        JSONObject json = (JSONObject) param1;
        JSONException error = null;
        try {
            json.getObject((String) param2, (Class<?>)param3);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNull(error);
        Assert.assertEquals(param4, json.getObject((String) param2, (Class<?>)param3));
    }

    @Test
    public void test_3() {
        Assume.assumeTrue(type.equals(Type.TEST_3));

        User user = (User) JSON.toJavaObject((JSON) param2, (Class<?>) param4);

        Assert.assertEquals(param1, user.getId());
        Assert.assertEquals(param3, user.getName());
    }

    //--------------------------------------------- SUPPORT CLASSES ------------------------------------------------
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
