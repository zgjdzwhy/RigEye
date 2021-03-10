package com.rigai.rigeye.common;

/**
 * @author yh
 * @date 2018/8/20 11:08
 */
public class BaseConstant {
    /**
     * dataTask的key在redis中的前缀
     */
    public static final String PREFIX_DATA_TASK = "data_task_";

    public static final String LINE = "_line";
    public static final String OR = "|";
    public static final String AND = "&";

    /** 切分异常的日志，需要3个字段进行统计 */
    public static final int EXCEPTION_FIELD_NUM = 3;

    public static final String SYS_TIME = "_sysTime";

    /**窗口时间，时间秒*/
    public static final int WINDOW_TIME = 60;

    /**
     * influxDB中数据表前缀名字和数据库名称
     */
    public static final String INFLUX_DATA_SET_TABLE_PREFIX = "data_set_";
    /**数据集切分表前缀*/
    public static final String INFLUX_DIVIDE_DATA_SET= "data_divide_set";


    /**异常检测相关配置key*/
    public static final String EXCEPTION_CONTENT = "exception_content";
    public static final String EXCEPTION_FILTER = "exception_filter";

    public static final String EXCEPTION_STR = "exception";
    public static final String ERROR_STR = "error";
    public static final String BLANK = "";

    /**Es清洗的日志详情索引名称前缀，完整的索引名称：aegis + appName + id */
    public static final String ES_INDEX_NAME = "aegis-{0}-{1}";

    /**Redis中消费者组前缀*/
    public static final String GROUP_PREFIX = "aegis_group_";
}
