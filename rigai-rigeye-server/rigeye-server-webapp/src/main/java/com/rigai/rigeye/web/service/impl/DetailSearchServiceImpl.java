package com.rigai.rigeye.web.service.impl;

import com.rigai.rigeye.common.dto.Measure;
import com.rigai.rigeye.common.model.DataSetRule;
import com.rigai.rigeye.common.service.DataSetRuleService;
import com.rigai.rigeye.web.bean.common.PageVO;
import com.rigai.rigeye.web.bean.vo.*;
import com.rigai.rigeye.web.bean.vo.*;
import com.rigai.rigeye.web.constant.BaseConstant;
import com.rigai.rigeye.web.service.DetailSearchService;
import com.rigai.rigeye.web.util.InfluxDBUtil;
import com.em.fx.common.bean.Result;
import com.em.fx.common.bean.constant.RspCode;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yh
 * @date 2018/9/5 11:19
 */
@Service
public class DetailSearchServiceImpl implements DetailSearchService {
    private Logger logger = LoggerFactory.getLogger(DetailSearchServiceImpl.class);
    @Autowired
    DataSetRuleService dataSetRuleService;

    @Override
    public Result searchTaskRunDetail(TaskRunDetailParamVO taskRunDetailParamVO) {
        TaskRunDetailResultVO taskRunDetailResultVO = new TaskRunDetailResultVO();

        Map<String, Object> dimData = null;
        //step=2 数据切分查询.注意influx时间戳是19位，而参数传的精确到秒是13位，故需要补足9个0
        if (taskRunDetailParamVO.getStep() == BaseConstant.TASK_RUN_STEP[1]) {
            String sql = new StringBuilder().append("select sum(total) from ").append(BaseConstant.INFLUX_DIVIDE_DATA_SET).append(" where \"taskId\"='").append(taskRunDetailParamVO.getTaskId()).append("'").append(" and time > ").append(taskRunDetailParamVO.getMinTime()).append("000000 and time < ").append(taskRunDetailParamVO.getMaxTime()).append("000000 ").append(" group by type, time(").append(taskRunDetailParamVO.getIntervalInSec()).append("s)").toString();
            QueryResult queryResult = InfluxDBUtil.excuteQueryForInflux(sql);
            dimData = secondStep(queryResult);
        }else if (taskRunDetailParamVO.getStep() == BaseConstant.TASK_RUN_STEP[0]){
            //step=1 数据拉取查询
            String sql = new StringBuilder().append("select sum(total) from ").append(BaseConstant.INFLUX_DIVIDE_DATA_SET).append(" where \"taskId\" ='").append(taskRunDetailParamVO.getTaskId()).append("'").append(" and time > ").append(taskRunDetailParamVO.getMinTime()).append("000000 and time < ").append(taskRunDetailParamVO.getMaxTime()).append("000000 ").append(" group by time(").append(taskRunDetailParamVO.getIntervalInSec()).append("s)").toString();
            QueryResult queryResult = InfluxDBUtil.excuteQueryForInflux(sql);
            dimData = firstStep(queryResult);
        }

        //修改详情数据的key
        //设置返回结果
        taskRunDetailResultVO.setDimData(dimData);
        taskRunDetailResultVO.setDimensions(dimData != null ? dimData.keySet().parallelStream().collect(Collectors.toList()) : null);
        taskRunDetailResultVO.setDateKey("date");
        taskRunDetailResultVO.setIntervalInSec(taskRunDetailParamVO.getIntervalInSec());
        List<String> measures = new ArrayList<>();
        measures.add("sum");
        taskRunDetailResultVO.setMeasures(measures);

        return new Result(RspCode.SUCCESS,null,taskRunDetailResultVO);
    }

    private Map<String, Object> firstStep(QueryResult queryResult){
        Map<String, Object> dimData = new HashMap<>(2);
        if (queryResult != null) {
            List<QueryResult.Result> list = queryResult.getResults();
            list.forEach(result -> {
                if (result.getSeries() != null) {
                    result.getSeries().forEach(series -> {
                        ArrayList items = new ArrayList();
                        int timeIndex = series.getColumns().indexOf("time");
                        int sumIndex = series.getColumns().indexOf("sum");
                        if (series.getValues() != null) {
                            dealTaskDetail(series, items, timeIndex, sumIndex);
                            dimData.put("正常", items);
                        }
                    });
                }
            });
        }
        return dimData;
    }

    private Map<String, Object> secondStep(QueryResult queryResult){
        Map<String, Object> dimData = new HashMap<>(2);
        if (queryResult != null) {
            List<QueryResult.Result> list = queryResult.getResults();
            list.forEach(result -> {
                if (result.getSeries() != null) {
                    result.getSeries().forEach(series -> {
                        ArrayList items = new ArrayList();
                        String type = series.getTags().get("type");
                        type = "good".equals(type) ? "正常" : "异常";
                        int timeIndex = series.getColumns().indexOf("time");
                        int sumIndex = series.getColumns().indexOf("sum");
                        if (series.getValues() != null) {
                            dealTaskDetail(series, items, timeIndex, sumIndex);
                            dimData.put(type, items);
                        }
                    });
                }
            });
        }
        return dimData;
    }

    private void dealTaskDetail(QueryResult.Series series, List<Map> items, int timeIndex, int sumIndex) {
        series.getValues().forEach(objects -> {
            String time = (String) objects.get(timeIndex);
            //已经加了8小时
            Date date = InfluxDBUtil.utcToCST(time, BaseConstant.UTC_DATE_FORMAT);
            Double sum = (Double) objects.get(sumIndex);
            Map<String, Object> item = new HashMap<>(2);
            if (date != null) {
                item.put("date", date.getTime());
                item.put("sum", sum == null?0:sum);
                items.add(item);
            }
        });
    }

    @Override
    public Result searchBadDivideDetail(TaskRunDetailParamVO taskRunDetailParamVO) {
        ArrayList items = new ArrayList();
        //step=2 数据切分查询.注意influx时间戳是19位，而参数传的精确到秒是13位，故需要补足9个0。现在只有数据切分有错误详情。
        if (taskRunDetailParamVO.getStep() == BaseConstant.TASK_RUN_STEP[1]) {
            String sql = new StringBuilder().append("select sum(total) , last(last_msg) as msg from ").append(BaseConstant.INFLUX_DIVIDE_DATA_SET).append(" where \"taskId\"='").append(taskRunDetailParamVO.getTaskId()).append("'").append(" and \"type\"= 'bad'").append(" and time > ").append(taskRunDetailParamVO.getMinTime()).append("000000 and time < ").append(taskRunDetailParamVO.getMaxTime()).append("000000 ").append(" group by last_line, time(").append(taskRunDetailParamVO.getIntervalInSec()).append("s)").toString();
            QueryResult queryResult = InfluxDBUtil.excuteQueryForInflux(sql);
            items = parseBadDivide(queryResult);
        }
        return new Result(RspCode.SUCCESS,null,items);
    }

    private ArrayList parseBadDivide(QueryResult queryResult){
        ArrayList items = new ArrayList();
        if (queryResult != null) {
            List<QueryResult.Result> list = queryResult.getResults();
            list.forEach(result -> {
                if (result.getSeries() != null) {
                    result.getSeries().forEach(series -> {
                        String lastLine = series.getTags().get("last_line");
                        int timeIndex = series.getColumns().indexOf("time");
                        int sumIndex = series.getColumns().indexOf("sum");
                        int msgIndex = series.getColumns().indexOf("msg");
                        if (series.getValues() != null) {
                            series.getValues().parallelStream().filter(objects -> objects.get(sumIndex) != null && objects.get(msgIndex) != null).forEach(objects -> {
                                String time = (String) objects.get(timeIndex);
                                //已经加了8小时
                                Date date = InfluxDBUtil.utcToCST(time, BaseConstant.UTC_DATE_FORMAT);
                                Double sum = (Double) objects.get(sumIndex);
                                String msg = (String) objects.get(msgIndex);
                                TaskBadDivideResultVO item = new TaskBadDivideResultVO();
                                if (date != null) {
                                    item.setTime(date.getTime());
                                    item.setCount(sum.longValue());
                                    item.setDetail(msg);
                                    item.setPattern("类型转换异常");
                                    item.setSampleText(lastLine);
                                    items.add(item);
                                }
                            });
                        }
                    });
                }
            });
        }
        return items;
    }

    @Override
    public Result searchExceptionLogList(ExceptionLogParamVO exceptionLogParamVO) {
        try(RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(BaseConstant.ES_IP, BaseConstant.ES_PORT)))) {
            List<ExceptionLogResultVO> exceptionLogResultVOS = new ArrayList<>();

            TermsAggregationBuilder app = AggregationBuilders.terms("app").field("_appName.keyword");
            TermsAggregationBuilder level = AggregationBuilders.terms("level").field("level.keyword");
            TermsAggregationBuilder template = AggregationBuilders.terms("temp").field("_error_key.keyword");

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(app.subAggregation(level.subAggregation(template)));
            //只查询聚合值，不需要详细数据，size置为0
            searchSourceBuilder.size(0);
            //时间段筛选
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("@timestamp").gte(exceptionLogParamVO.getStartTime()).lte(exceptionLogParamVO.getEndTime());
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().filter(rangeQueryBuilder);

            SearchRequest searchRequest = new SearchRequest();
            //是否根据appName查询
            if (StringUtils.isNotEmpty(exceptionLogParamVO.getAppName())){
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("_appName.keyword",exceptionLogParamVO.getAppName());
                boolQueryBuilder.filter(termQueryBuilder);
                //设置查询的索引和type。索引使用通配符匹配
                searchRequest.indices(String.format("*%s*", exceptionLogParamVO.getAppName().toLowerCase()));
                searchRequest.types(exceptionLogParamVO.getAppName().toLowerCase());
            }
            searchSourceBuilder.query(boolQueryBuilder);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);

            Aggregations aggregations = searchResponse.getAggregations();
            if (aggregations != null){
                Aggregation appA = aggregations.get("app");
                ParsedStringTerms appTerms = (ParsedStringTerms)appA;
                appTerms.getBuckets().forEach(o -> {
                    ((ParsedStringTerms)o.getAggregations().get("level")).getBuckets().forEach(o1 -> {
                        ((ParsedStringTerms)o1.getAggregations().get("temp")).getBuckets().forEach(o2 -> {
                            exceptionLogResultVOS.add(new ExceptionLogResultVO(o.getKeyAsString(),o1.getKeyAsString(),o2.getKeyAsString(),o2.getDocCount()));
                        });
                    });
                });
             }
            //对查询结果内存分页
            int total = exceptionLogResultVOS.size();
            int start = (exceptionLogParamVO.getPage() - 1) * exceptionLogParamVO.getPageSize();
            start = start < 0 ? 0 : (start > total ? total : start);
            int pages =  total % exceptionLogParamVO.getPageSize() == 0 ? total/exceptionLogParamVO.getPageSize() : total/exceptionLogParamVO.getPageSize() + 1;
            int end = total - start > exceptionLogParamVO.getPageSize() ? start + exceptionLogParamVO.getPageSize() : total;
            List sub = exceptionLogResultVOS.subList(start, end);
            PageVO pageVO = new PageVO(sub, pages, (long) total);
            return new Result(RspCode.SUCCESS,null, pageVO);
        }catch (Exception e){
            logger.error("get es list error {}",e);
            e.printStackTrace();
            return new Result(RspCode.EXCEPTION, "获取异常日志列表错误",null);
        }
    }

    @Override
    public Result searchLogDetail(LogDetailParamVO logDetailParamVO) {
        try(RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(BaseConstant.ES_IP, BaseConstant.ES_PORT)))) {
            List<LogDetailResultVO> logDetailResultVOS = new ArrayList<>();

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            //查询数量
            searchSourceBuilder.size(logDetailParamVO.getPageSize());
            searchSourceBuilder.from((logDetailParamVO.getPage() -1)*logDetailParamVO.getPageSize());
            //时间段
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("@timestamp").gte(logDetailParamVO.getStartTime()).lte(logDetailParamVO.getEndTime());
            boolQueryBuilder.filter(rangeQueryBuilder);
            //appName查询
            if (StringUtils.isNotEmpty(logDetailParamVO.getAppName())){
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("_appName.keyword",logDetailParamVO.getAppName());
                boolQueryBuilder.filter(termQueryBuilder);
            }
            //level日志等级
            if (StringUtils.isNotEmpty(logDetailParamVO.getLevel())){
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("level.keyword",logDetailParamVO.getLevel());
                boolQueryBuilder.filter(termQueryBuilder);
            }
            //_error_key 模板名称
            if (StringUtils.isNotEmpty(logDetailParamVO.getTemplate())){
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("_error_key.keyword",logDetailParamVO.getTemplate());
                boolQueryBuilder.filter(termQueryBuilder);
            }
            //日志内容
            if (StringUtils.isNotEmpty(logDetailParamVO.getLog())){
                WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("_line.keyword", String.format("*%s*", logDetailParamVO.getLog()));
                boolQueryBuilder.filter(wildcardQueryBuilder);
            }

            SearchRequest searchRequest = new SearchRequest();
            //有appName时查询对应的索引
            if (StringUtils.isNotEmpty(logDetailParamVO.getAppName())){
                //设置查询的索引和type
                searchRequest.indices(String.format("*%s*", logDetailParamVO.getAppName().toLowerCase()));
                searchRequest.types(logDetailParamVO.getAppName().toLowerCase());
            }
            searchSourceBuilder.query(boolQueryBuilder);
            SortBuilder sortBuilder = SortBuilders.fieldSort("@timestamp").order(SortOrder.DESC);
            searchSourceBuilder.sort(sortBuilder);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);

            SearchHits searchHits = searchResponse.getHits();
            long total = searchHits.getTotalHits();
            for (SearchHit hit : searchHits) {
                Map<String, Object> map = hit.getSource();
                String appName = (String) map.get("_appName");
                String level = (String)map.get("level");
                String template = (String)map.get("_error_key");
                String log = (String) map.get("_line");
                String id = hit.getId();

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                Date date = df.parse((String)map.get("@timestamp"));
                LogDetailResultVO logDetailResultVO = new LogDetailResultVO(id, appName, level, log, template, date);
                logDetailResultVOS.add(logDetailResultVO);
            }
            //logDetailResultVOS.sort((o1, o2) -> o1.getTime().getTime() > o2.getTime().getTime() ? -1: 1);
            //对结果分页
            int pages = (int) (total%logDetailParamVO.getPageSize() == 0 ? total/logDetailParamVO.getPageSize() : total/logDetailParamVO.getPageSize() + 1);
            PageVO pageVO = new PageVO(logDetailResultVOS, pages, total);
            return new Result(RspCode.SUCCESS,null, pageVO);
        }catch (Exception e){
            logger.error("get es list error {}",e);
            e.printStackTrace();
            return new Result(RspCode.EXCEPTION, "获取日志明细列表错误",null);
        }
    }

    @Override
    public Result searchDataSetAggrationDetail(DataSetAggrateParamVO dataSetAggrateParamVO){

        //查询数据集，获取指标名称
        DataSetRule rule = dataSetRuleService.getById(dataSetAggrateParamVO.getDatasetId());
        List<Measure> measures = rule.getDatasetRule().getMeasures();
        StringBuilder sql = new StringBuilder("select ");
        for (int i = 0; i < measures.size(); i++) {
            sql.append("sum(\"").append(measures.get(i).getMeasureLabel()).append("\") as ").append(measures.get(i).getMeasureLabel());
            if (i< measures.size() -1){
                sql.append(",");
            }
        }
        sql.append(" from ").append(BaseConstant.INFLUX_DATA_SET_TABLE_PREFIX).append(dataSetAggrateParamVO.getDatasetId())
            .append(" where time > ").append(dataSetAggrateParamVO.getMinTime()).append("000000 and time < ").append(dataSetAggrateParamVO.getMaxTime()).append("000000")
            .append(" group by time(").append(dataSetAggrateParamVO.getIntervalInSec()).append("s)");

        QueryResult queryResult = InfluxDBUtil.excuteQueryForInflux(sql.toString());
        List data = parseDataSetAggrate(queryResult, measures);

        //填充数据
        DataSetAggrateResultVO dataSetAggrateResultVO = new DataSetAggrateResultVO();
        dataSetAggrateResultVO.setData(data);
        dataSetAggrateResultVO.setDateKey("date");
        dataSetAggrateResultVO.setDims(new ArrayList<>());
        dataSetAggrateResultVO.setIntervalInSec(dataSetAggrateParamVO.getIntervalInSec());
        dataSetAggrateResultVO.setMeasures(measures.parallelStream().map(Measure::getMeasureLabel).collect(Collectors.toList()));
        return new Result(RspCode.SUCCESS,null,dataSetAggrateResultVO);
    }

    public List parseDataSetAggrate(QueryResult queryResult, List<Measure> measures){
        ArrayList data = new ArrayList();
        if (queryResult != null){
            List<QueryResult.Result> results = queryResult.getResults();
            for (QueryResult.Result result : results) {
                if (result.getSeries() != null) {
                    for (QueryResult.Series series : result.getSeries()) {
                        List<String> columns = series.getColumns();
                        int timeIndex = series.getColumns().indexOf("time");
                        //查询指标的下标
                        List<Integer> index = new ArrayList<>();
                        for (Measure measure : measures) {
                            index.add(columns.indexOf(measure.getMeasureLabel()));
                        }

                        //按顺序取出指标结果
                        if (series.getValues() != null) {
                            series.getValues().parallelStream().forEach(objects -> {
                                Map<String, Object> item = new HashMap<>(4);
                                String time = (String) objects.get(timeIndex);
                                //已经加了8小时
                                Date date = InfluxDBUtil.utcToCST(time, BaseConstant.UTC_DATE_FORMAT);
                                if (date != null) {
                                    item.put("date", date.getTime());
                                    for (int i = 0; i < measures.size(); i++) {
                                        item.put(measures.get(i).getMeasureLabel(), objects.get(index.get(i)) == null ? 0 : objects.get(index.get(i)));
                                    }
                                    data.add(item);
                                }
                            });
                        }
                    }
                }
            }
        }
        return data;
    }
}
