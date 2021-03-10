package com.rigai.rigeye.web.util;

import com.rigai.rigeye.web.constant.BaseConstant;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/9/28.
 */

public class InfluxFactory {

    private static InfluxDB influxDB = InfluxDBFactory.connect(BaseConstant.INFLUXDB_URL, BaseConstant.INFLUXDB_USER, BaseConstant.INFLUXDB_PASS);

    public static InfluxDB safeGetInfluxDB() {
        Pong pong=null;
        if(influxDB!=null){
            pong=influxDB.ping();
        }
        if(pong!=null&&pong.isGood()){
            return influxDB;
        }else {
            if(influxDB!=null){
                influxDB.close();
                influxDB=null;
            }
            return influxDB = InfluxDBFactory.connect(BaseConstant.INFLUXDB_URL, BaseConstant.INFLUXDB_USER, BaseConstant.INFLUXDB_PASS);
        }
    }

    public static InfluxDB getInfluxDB() {
        return influxDB = InfluxDBFactory.connect(BaseConstant.INFLUXDB_URL, BaseConstant.INFLUXDB_USER, BaseConstant.INFLUXDB_PASS);
    }

    public static void setInfluxDB(InfluxDB influxDB) {
        InfluxFactory.influxDB = influxDB;
    }
}
