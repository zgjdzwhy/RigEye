package com.rigai.rigeye.common.dao.influx;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/9/10.
 */

@Component
public class InfluxFactory {

    @Value("${influx.url}")
    private String influxDBUrl;
    @Value("${influx.user}")
    private String influxDBUser;
    @Value("${influx.password}")
    private String influxDBPassword;
    @Value("${influx.dbname}")
    private String dbName;

    private InfluxDB influxDB;

    @PostConstruct
    public void initFromSpring(){
        influxDB= InfluxDBFactory.connect(influxDBUrl, influxDBUser,influxDBPassword);
    }

    public InfluxDB getInfluxDB() {
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
            return influxDB = InfluxDBFactory.connect(influxDBUrl, influxDBUser,influxDBPassword);
        }
    }

    public void setInfluxDB(InfluxDB influxDB) {
        this.influxDB = influxDB;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
