package com.rigai.rigeye.web.util;

import com.dianping.cat.Cat;
import com.rigai.rigeye.web.constant.BaseConstant;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yh
 * @date 2018/9/4 10:23
 */
public class InfluxDBUtil {
    private static final Logger logger= LoggerFactory.getLogger(InfluxDBUtil.class);

    /**
     * 查询任务切分是否出现异常数据
     * @param taskId
     * @param type
     * @return true=正常
     */
    public static boolean countBadDivide(long taskId, String type){
        InfluxDB influxDB = InfluxFactory.safeGetInfluxDB();
        //查询数据
        String sql = "select total from " + BaseConstant.INFLUX_DIVIDE_DATA_SET +
                " where \"taskId\" = '" + taskId +
                "' and \"type\" = '" + type +
                "' and time > now()-1h limit 1";
        logger.info("query influx with sql:{}",sql);
        Query query = new Query(sql, BaseConstant.INFLUXDB_NAME);
        QueryResult queryResult;
        try {
            queryResult = influxDB.query(query);
        }catch (Exception e){
            logger.error("countBadDivide error. {}", e);
            Cat.logError("countBadDivide error", e);
            return false;
        }
        for (QueryResult.Result result : queryResult.getResults()) {
            //没有查到异常数据说明监控状态正常.查到说明有切分异常的数据，监控状态不正常。
            if (result.getSeries() != null) {
                return false;
            }
        }
        return true;
    }

    public static QueryResult excuteQueryForInflux(String sql){
        InfluxDB influxDB = InfluxFactory.safeGetInfluxDB();
        try {
            influxDB = InfluxDBFactory.connect(BaseConstant.INFLUXDB_URL, BaseConstant.INFLUXDB_USER, BaseConstant.INFLUXDB_PASS);
            Query query = new Query(sql, BaseConstant.INFLUXDB_NAME);
            return influxDB.query(query);
        }catch (Exception e){
            logger.error("query influx error. {}", e);
            Cat.logError("query influx error", e);
        }
        return null;
    }

    public static Date utcToCST(String utcStr, String format){
        try {
            Date date = new SimpleDateFormat(format).parse(utcStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
            return calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
