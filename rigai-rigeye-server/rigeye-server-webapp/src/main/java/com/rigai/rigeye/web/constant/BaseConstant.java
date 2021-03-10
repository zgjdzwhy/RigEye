package com.rigai.rigeye.web.constant;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenxing
 *         Created by ChenXing on 2017/11/2.
 */

public class BaseConstant {
    public static final String REQUEST_USER_ID_KEY="customerId";
    public static final String REQUEST_USER_NAME_KEY="customerName";
    public static final String TRUE_USER_KEY = "trueCustomerId";
    public static final String DATABASE_DEL="1";
    public static final String DATABASE_ALIVE="0";
    public static final String SPLIT_SIGN=",";
    //指标运算名称
    public static final String SUM = "SUM";
    public static final String COUNT = "COUNT";
    public static final String MAX = "MAX";
    public static final String MIN = "MIN";

    public static final String[] NUM_OPERATOR = {
            SUM,
            MAX,
            MIN,
    };

    //字符串可进行的筛选运算符
    public static final String[] STR_FILTER_OPERATOR = {
        "==",
        "!=",
        "包含",
        "不包含",
        "IS NULL",
        "NOT NULL"
    };

    //数字可进行的筛选运算符
    public static final String[] NUM_FILTER_OPERATOR = {
        ">",
        ">=",
        "==",
        "<=",
        "<",
        "!=",
        "IS NULL",
        "NOT NULL"
    };

    public static final String LINE_NAME = "_line";
    public static final String ERROR_KEY = "_error_key";
    //数据源类型
    public static final int[] DATASOURCE_TYPES = {
            1,//kafka数据源
    };

    public static final String BLANK = "";
    public static final String DATASET_CHECK_ERROR_MESSAGE = "与切分模型中字段类型不符，请修改切分模型或数据集！";
    public static final String DATASET_CHECK_NOT_EXISTS_MESSAGE = "在切分模型中不存在，请修改切分模型或数据集！";

    //过滤条件之间的关系，| or &
    public static final String OR = "|";
    public static final String AND = "&";

    //监控任务的监控状态
    public static final int[] MONITOR_STATUS = {
            0,//所有
            1,//1=正常
            2 //异常
    };

    /**
     * dataTask的key在redis中的前缀
     */
    public static final String PREFIX_DATAT_ASK = "data_task_";


    /**
     * 任务状态
     */
    public static final int CREATE = 1;
    public static final int WAITING = 2;
    public static final int RUNNING = 3;
    public static final int FAILED = 4;
    public static final int STOPPED = 5;

    /**
     * influxDB中数据表前缀名字和数据库名称.
     * 连接信息
     */
    public static final String INFLUX_DATA_SET_TABLE_PREFIX = "data_set_";
    public static final String INFLUX_DIVIDE_DATA_SET= "data_divide_set";
    public static  String INFLUXDB_NAME ;
    public static  String INFLUXDB_URL ;
    public static  String INFLUXDB_USER ;
    public static  String INFLUXDB_PASS ;

    /**
     * 启动任务消费位置
     */
    public static final String[] START_MODEL = {
            "latest",
            "earliest",
            "time:"
    };

    /**异常检测相关配置key*/
    public static final String EXCEPTION_CONTENT = "exception_content";
    public static final String EXCEPTION_FILTER = "exception_filter";

    public static final String UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final int[] TASK_RUN_STEP = {
        1,2,3
    };
    /**Es配置**/
    public static String ES_IP;
    public static int ES_PORT;

    static {
        Config config = ConfigService.getConfig("influxdb");
        INFLUXDB_URL = config.getProperty("influx.url","");
        INFLUXDB_USER = config.getProperty("influx.user","");
        INFLUXDB_PASS = config.getProperty("influx.pass","");
        INFLUXDB_NAME = config.getProperty("influx.dbname","");

        Config es = ConfigService.getConfig("es");
        ES_IP = es.getProperty("es.ip","");
        ES_PORT = es.getIntProperty("es.port",0);

    }
}
