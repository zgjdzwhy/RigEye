package com.rigai.rigeye.task.commit.influx;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.rigai.rigeye.common.bean.*;
import com.rigai.rigeye.common.constant.ComparisonOperator;
import com.rigai.rigeye.common.constant.ValueTypeDescription;
import com.rigai.rigeye.common.dao.influx.InfluxFactory;
import com.rigai.rigeye.common.exception.DataException;
import com.rigai.rigeye.task.commit.exception.InfluxDBQueryException;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author chenxing
 * Created by ChenXing on 2018/9/11.
 */

@Component
public class InfluxServiceImpl implements InfluxService{

    private Logger logger= LoggerFactory.getLogger(InfluxService.class);

    @Autowired
    private InfluxFactory influxFactory;

    private Long delay;

    private static final String SYSTEM_DELAY ="system.delay";

    @Override
    public TaskResult indexQuery(CommitTaskInfo info) {
        //TODO
        return null;
    }


    @Override
    public double indexQuery(AlertDataSetConstruct dataSetConstruct, AlertRuleConstruct rule) {
        StringBuilder builder=new StringBuilder("select ");
        String operatorStr=ValueTypeDescription.getValueTypeDescription(rule.getValueType());
        builder.append(operatorStr);
        builder.append("(\"");
        builder.append(rule.getNorm());
        builder.append("\") from ");
        builder.append(dataSetConstruct.getDataSet());
        builder.append(" where ");
        if(dataSetConstruct.getLatitudes()!=null&&dataSetConstruct.getLatitudes().size()>0){
            for(Latitude latitude:dataSetConstruct.getLatitudes()){
                if(latitude.getOperator()==null){
                    continue;
                }
                builder.append("\"");
                builder.append(latitude.getKey());
                builder.append("\"");
                builder.append(ComparisonOperator.getComparisonOperator(latitude.getOperator()));
                builder.append("\'");
                builder.append(latitude.getValue());
                builder.append("\'");
                builder.append(" and ");
            }
        }
        builder.append(" time>now()-");
        long period=rule.getPeriod()+delay;
        builder.append(period);
        builder.append("m");
        builder.append(" and time<now()-");
        builder.append(delay);
        builder.append("m");
        logger.info("query influx with statement :{}",builder);
        Query query=new Query(builder.toString(),influxFactory.getDbName());
        InfluxDB db=null;
        QueryResult.Result result;
        try{
            db=influxFactory.getInfluxDB();
            QueryResult queryResult=db.query(query);
             result=queryResult.getResults().get(0);
        }catch (Exception e){
            throw new InfluxDBQueryException("influx db query error statement:"+builder.toString(),e);
        }
        List<QueryResult.Series> series=result.getSeries();
        double value=0;
        if(series!=null){
            value=this.getValue(series.get(0),operatorStr);
        }
        return value;
    }


    private double getValue(QueryResult.Series series,String columnName){
        List<String> columns=series.getColumns();
        Integer index=null;
        for(int i=0;i<columns.size();i++){
            if (columnName.equalsIgnoreCase(columns.get(i))){
                index= i;
            }
        }
        if(index==null){
            throw new DataException("influx查询数据解析错误");
        }
        return (Double)series.getValues().get(0).get(index);
    }

    @PostConstruct
    public void addListener(){
        Config config = ConfigService.getAppConfig();
        delay=config.getLongProperty(SYSTEM_DELAY,delay);
        config.addChangeListener(changeEvent -> changeEvent.changedKeys().stream().filter(SYSTEM_DELAY::equals).forEach(key -> {
            ConfigChange change = changeEvent.getChange(key);
            delay = Long.valueOf(change.getNewValue());
        }));
    }
}
