package com.rigai.rigeye.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.rigai.rigeye.common.dto.DataSourceDTO;
import com.rigai.rigeye.common.dto.Filter;
import com.rigai.rigeye.common.dto.Measure;
import com.rigai.rigeye.common.model.DataSetRule;
import com.rigai.rigeye.common.model.DataTask;
import com.rigai.rigeye.common.service.DataSetRuleService;
import com.rigai.rigeye.common.service.DataSourceService;
import com.rigai.rigeye.common.service.DataTaskService;
import com.rigai.rigeye.web.bean.vo.*;
import com.rigai.rigeye.web.bean.vo.*;
import com.rigai.rigeye.web.constant.BaseConstant;
import com.rigai.rigeye.web.service.DataSetHandleService;
import com.em.fx.common.bean.Result;
import com.em.fx.common.bean.constant.RspCode;
import com.em.fx.redis.dao.RedisDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author yh
 * @date 2018/8/13 15:03
 */
@Service
public class DataSetHandleServiceImpl implements DataSetHandleService {
    @Autowired
    DataTaskService dataTaskService;
    @Autowired
    DataSetRuleService dataSetRuleService;
    @Autowired
    DataSourceService dataSourceService;
    @Autowired
    RedisDao redisDao;

    @Override
    public Result saveOrUpdate(DataSetRule dataSetRule) {
        DataSetRule rule = new DataSetRule();
        rule.setName(dataSetRule.getName());
        rule.setUserId(dataSetRule.getUserId());
        List<DataSetRule > dataSetRules = dataSetRuleService.getByObj(rule);
        if (dataSetRule.getId() > 0){
            if (dataSetRules != null && dataSetRules.size() > 0){
                //存在
                boolean f = dataSetRules.get(0).getId().equals(dataSetRule.getId());
                if (!f) {
                    Result result = new Result(RspCode.EXCEPTION);
                    result.setMessage("数据集名称已存在！");
                    return result;
                }
            }
            //更新时间
            dataSetRule.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
            dataSetRuleService.updateDataSet(dataSetRule);
        }else {
            if (dataSetRules != null && dataSetRules.size() > 0){
                Result result = new Result(RspCode.EXCEPTION);
                result.setMessage("数据集名称已存在！");
                return result;
            }
            dataSetRule.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
            dataSetRule.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
            dataSetRuleService.save(dataSetRule);
        }

        //保存完成后，更新redis中数据集配置信息
        DataTask dataTask = dataTaskService.getById(dataSetRule.getTaskId());
        DataTaskRedisVO dataTaskRedisVO = new DataTaskRedisVO();
        //设置基本参数
        BeanUtils.copyProperties(dataTask, dataTaskRedisVO);
        //查询数据源
        DataSourceDTO dataSourceDTO = dataSourceService.getDataSourceDTOById(Long.valueOf(dataTask.getDataSourceId()));
        dataTaskRedisVO.setDataSourceDTO(dataSourceDTO);
        if (dataTask.getDataSinkId() != -1) {
            //明细数据输出数据源
            DataSourceDTO dataSink = dataSourceService.getDataSourceDTOById(Long.valueOf(dataTask.getDataSinkId()));
            dataTaskRedisVO.setDataSink(dataSink);
        }
        //设置数据集。每次全量更新所有的数据集
        DataSetRule queryRule = new DataSetRule();
        queryRule.setUserId(dataSetRule.getUserId());
        queryRule.setTaskId(dataSetRule.getTaskId());
        dataTaskRedisVO.setDataSetRule(dataSetRuleService.getByObj(queryRule));
        //写入redis
        redisDao.setStr(BaseConstant.PREFIX_DATAT_ASK + dataTask.getId(), JSON.toJSONString(dataTaskRedisVO));

        Map<String, Object> map = new HashMap<>(4);
        map.put("id",dataSetRule.getId());
        Result result = new Result(RspCode.SUCCESS,null);
        result.setData(map);
        return result;
    }

    @Override
    public DataSetOperationVO chartConfig(DataSetModelVO model) {
        List<String> strField = new ArrayList<>();
        List<String> numField = new ArrayList<>();
        List<String> dateField = new ArrayList<>();

        List<String> dimensionsValuesList = new ArrayList<>();
        dimensionsValuesList.add("无");
        List<String> sampleDimsValuesList = new ArrayList<>();
        sampleDimsValuesList.add("无");
        model.getModel().forEach(dataSetModel -> {
            dimensionsValuesList.add(dataSetModel.getName());
            sampleDimsValuesList.add(dataSetModel.getName());
            switch (dataSetModel.getType()){
                case "string":
                    strField.add(dataSetModel.getName());
                    break;
                case "date":
                    dateField.add(dataSetModel.getName());
                    break;
                case "long":
                case "double":
                    numField.add(dataSetModel.getName());
                    break;
                default:
                    break;
            }
        });
        //维度
        DimensionsAndSampleDimsVO dimensions = new DimensionsAndSampleDimsVO(3,0,"维度",dimensionsValuesList);
        //采样字段
        DimensionsAndSampleDimsVO sampleDims = new DimensionsAndSampleDimsVO(3,0,"采样字段",sampleDimsValuesList);

        //筛选条件
        List<MeasureAndFilterColumnVO> filtersColumnsList = new ArrayList<>();
        MeasureAndFilterColumnVO no = new MeasureAndFilterColumnVO("无",new ArrayList<>());
        filtersColumnsList.add(no);
        handleFiltersAndMeasures(strField, filtersColumnsList, BaseConstant.STR_FILTER_OPERATOR);
        handleFiltersAndMeasures(dateField, filtersColumnsList, BaseConstant.STR_FILTER_OPERATOR);
        handleFiltersAndMeasures(numField, filtersColumnsList, BaseConstant.NUM_FILTER_OPERATOR);
        MeasuresAndFiltersVO filters = new MeasuresAndFiltersVO(filtersColumnsList,"筛选",10, 0);

        //指标
        List<MeasureAndFilterColumnVO> measuresColumnsList = new ArrayList<>();
        handleFiltersAndMeasures(Arrays.asList(BaseConstant.NUM_OPERATOR), measuresColumnsList, numField.toArray(new String[]{ }));
        handleFiltersAndMeasures(Arrays.asList(BaseConstant.COUNT), measuresColumnsList, new String[]{ BaseConstant.LINE_NAME});
        MeasuresAndFiltersVO measures = new MeasuresAndFiltersVO(measuresColumnsList,"指标",3,1);

        //返回结果
        DataSetOperationVO data = new DataSetOperationVO(dateField, dimensions,sampleDims,filters,measures);
        return data;
    }

    @Override
    public List<DataSetCheckVO> getDataSetChecks(Long taskId) {
        List<DataSetCheckVO> dataSetCheckVOS = new ArrayList<>();
        //查询任务
        DataTask dataTask = dataTaskService.getById(taskId);
        //获取切分模型，可以进行的各种操作
        if (dataTask != null && StringUtils.isNotEmpty(dataTask.getModel())){
            List<DataSetClearModelVO> dataSetClearModelVOS = JSON.parseObject(dataTask.getModel(),new TypeReference<ArrayList<DataSetClearModelVO>>(){});
            DataSetModelVO dataSetModelVO = new DataSetModelVO();
            dataSetModelVO.setModel(dataSetClearModelVOS);
            DataSetOperationVO chart = chartConfig(dataSetModelVO);

            //查询数据集
            DataSetRule rule = new DataSetRule();
            rule.setTaskId(taskId);
            List<DataSetRule> dataSetRules = dataSetRuleService.getByObj(rule);

            final boolean editable = dataTask.getTaskStatus() != 2;

            //判断已保存的数据集的操作，是否在切分模型允许的操作范围内
            if (dataSetRules != null ){
                dataSetRules.forEach(dataSetRule -> {
                    if (dataSetRule.getDatasetRule() != null) {
                        DataSetCheckVO dataSetCheckVO = new DataSetCheckVO(dataSetRule.getId(), dataSetRule.getName(), editable, dataSetRule.getStatus(), BaseConstant.BLANK);
                        //验证date
                        String date = dataSetRule.getDatasetRule().getDate();
                        if (!chart.getDates().contains(date)) {
                            dataSetCheckVO.setError("时间字段字段:[" + date + "]," + BaseConstant.DATASET_CHECK_NOT_EXISTS_MESSAGE);
                        }
                        //验证filter
                        List<Filter> filters = dataSetRule.getDatasetRule().getFilters();
                        if (filters != null){
                            for (Filter filter:filters) {
                                //判断字段是否存在，计算是否正确
                                boolean f = false;
                                boolean exp = false;
                                for (MeasureAndFilterColumnVO filterColumnVO:chart.getFilters().getColumns()) {
                                    if (filterColumnVO.getKey().equals(filter.getField())){
                                        f = true;
                                        if (filterColumnVO.getValues().contains(filter.getExpression())){
                                            exp = true;
                                        }
                                        break;
                                    }
                                }
                                if (!f){
                                    dataSetCheckVO.setError("筛选字段:[" + filter.getField() + "]," + BaseConstant.DATASET_CHECK_NOT_EXISTS_MESSAGE);
                                }else if (!exp){
                                    dataSetCheckVO.setError("筛选字段:[" + filter.getField() + "]," + BaseConstant.DATASET_CHECK_ERROR_MESSAGE);
                                }
                            }
                        }

                        //验证指标
                        List<Measure> measures = dataSetRule.getDatasetRule().getMeasures();
                        if (measures != null){
                            for (Measure measure:measures) {
                                //判断字段是否存在，计算是否正确
                                boolean f = false;
                                boolean exp = false;
                                for (MeasureAndFilterColumnVO measureColumnVO:chart.getMeasures().getColumns()) {
                                    if (measureColumnVO.getValues().contains(measure.getMeasureA())){
                                        f = true;
                                        if (measure.getAggregates().contains(measureColumnVO.getKey())){
                                            exp = true;
                                        }
                                        break;
                                    }
                                }
                                if (!f){
                                    dataSetCheckVO.setError("指标字段:[" + measure.getMeasureA() + "]," + BaseConstant.DATASET_CHECK_NOT_EXISTS_MESSAGE);
                                }else if (!exp){
                                    dataSetCheckVO.setError("指标字段:[" + measure.getMeasureA() + "]," + BaseConstant.DATASET_CHECK_ERROR_MESSAGE);
                                }
                            }
                        }
                        //添加到列表
                        dataSetCheckVOS.add(dataSetCheckVO);
                    }
                });
            }
        }
        return dataSetCheckVOS;
    }

    private static void handleFiltersAndMeasures(List<String> fields, List<MeasureAndFilterColumnVO> columnsList, String[] operator) {
        fields.forEach(s -> {
            MeasureAndFilterColumnVO measureAndFilterColumnVO = new MeasureAndFilterColumnVO();
            measureAndFilterColumnVO.setKey(s);
            measureAndFilterColumnVO.setValues(Arrays.asList(operator));
            columnsList.add(measureAndFilterColumnVO);
        });
    }

}
