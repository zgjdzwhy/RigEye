package com.rigai.rigeye.web.controller;

import com.dianping.cat.Cat;
import com.rigai.rigeye.common.dto.DataSourceDTO;
import com.rigai.rigeye.common.kafka.KafkaUtils;
import com.rigai.rigeye.common.model.DataSource;
import com.rigai.rigeye.common.service.DataSourceClusterService;
import com.rigai.rigeye.common.service.DataSourceService;
import com.rigai.rigeye.web.bean.common.PageInfoVO;
import com.rigai.rigeye.web.bean.common.PageVO;
import com.rigai.rigeye.web.bean.vo.DataSourceVO;
import com.rigai.rigeye.web.bean.vo.LogCrawParamVO;
import com.rigai.rigeye.web.constant.BaseConstant;
import com.em.fx.common.bean.Result;
import com.em.fx.common.bean.constant.RspCode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/6.
 */

@RestController
public class DataSourceController {

    @Autowired
    DataSourceService dataSourceService;
    @Autowired
    DataSourceClusterService dataSourceClusterService;

    @Autowired
    KafkaUtils kafkaUtils;

    private Logger logger= LoggerFactory.getLogger(DataSourceController.class);

    /**
     * 获取数据源列表
     * @param pageInfoVO 分页信息
     * @param bindingResult 绑定结果
     * @return
     */
    @PostMapping("/dataSource/list")
    public Result listDataSource(@Valid @RequestBody PageInfoVO<String> pageInfoVO,BindingResult bindingResult, HttpServletRequest request){
        //如果绑定结果有误则返回参数错误
        if(bindingResult.hasErrors()){
            FieldError fieldError= bindingResult.getFieldError();
            logger.info("get data source wrong param {} ",fieldError.getDefaultMessage());
            return new Result(RspCode.PARAMS_ERROR,null);
        }
        logger.info("get data source with param {} ",pageInfoVO);
        String topic=pageInfoVO.getInfo();
        DataSource param = new DataSource();
        //如果topic参数不为空
        if(!StringUtils.isBlank(topic)){
            param.setTopic(topic);
        }
        String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
        param.setUserId(userId);
        //查询数据
        PageInfo<DataSourceDTO> dataSourceList;
        try{
            PageHelper.orderBy("create_time desc");
            dataSourceList=dataSourceService.pageGetDataSourceDTO(param,pageInfoVO.getPage(),pageInfoVO.getPageSize());
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("get data source error param is:"+pageInfoVO.toString(),e);
            return new Result(RspCode.UNKNOWN_ERROR);
        }
        List<DataSourceDTO> dataSourceDTOs=dataSourceList.getList();
        List<DataSourceVO> dataSourceVOs;
        //将数据封装至VO，剔除部分不显示给前端的数据
        if(dataSourceDTOs!=null&&dataSourceDTOs.size()>0){
            dataSourceVOs=new ArrayList<>(dataSourceDTOs.size());
            dataSourceDTOs.parallelStream().forEachOrdered(dataSourceDTO -> {
                DataSourceVO temp=new DataSourceVO();
                BeanUtils.copyProperties(dataSourceDTO,temp);
                dataSourceVOs.add(temp);
            });
        }else {
            dataSourceVOs=null;
        }
        //封装回传信息
        Result result=new Result(RspCode.SUCCESS);
        PageVO<DataSourceVO> pageVO=new PageVO<>(dataSourceVOs,dataSourceList.getPages(),dataSourceList.getTotal());
        pageVO.setList(dataSourceVOs);
        result.setData(pageVO);
        return result;
    }

    @PostMapping("/dataSource/delete")
    public Result deleteDataSource(Integer id, HttpServletRequest request){
        if(id!=null&&id>0){
            String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
            try{
                DataSource dataSource=new DataSource();
                dataSource.setUserId(userId);
                dataSource.setId(id);
                dataSourceService.delete(dataSource);
                return new Result(RspCode.SUCCESS);
            }catch (Exception e){
                logger.error(ExceptionUtils.getStackTrace(e));
                Cat.logError("delete data source "+id+" error",e);
                return new Result(RspCode.UNKNOWN_ERROR);
            }
        }else {
            logger.info("delete data source wrong param {} ",id);
            return new Result(RspCode.PARAMS_ERROR);
        }
    }

    @PostMapping("/dataSource/update")
    public Result updateDataSource(@Valid @RequestBody DataSourceVO dataSourceVO, BindingResult bindingResult, HttpServletRequest request){
        //如果绑定结果有误则返回参数错误
        if(bindingResult.hasErrors()||dataSourceVO.getId()==null||dataSourceVO.getId()<0){
            FieldError fieldError= bindingResult.getFieldError();
            logger.info("update data source wrong Zparam {} ",fieldError.getDefaultMessage());
            return new Result(RspCode.PARAMS_ERROR,null);
        }
        String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
        DataSource dataSource=new DataSource();
        BeanUtils.copyProperties(dataSourceVO,dataSource);
        dataSource.setUserId(userId);
        dataSource.setUpdateTime(Calendar.getInstance().getTime());
        try{
            dataSourceService.update(dataSource);
            return new Result(RspCode.SUCCESS);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("update data source error param"+dataSourceVO.toString(),e);
            return new Result(RspCode.UNKNOWN_ERROR);
        }
    }

    @PostMapping("/dataSource/add")
    public Result addDataSource(@Valid @RequestBody DataSourceVO dataSourceVO, BindingResult bindingResult, HttpServletRequest request){
        //如果绑定结果有误则返回参数错误
        if(bindingResult.hasErrors()){
            FieldError fieldError= bindingResult.getFieldError();
            logger.info("add data source wrong param {} ",fieldError.getDefaultMessage());
            return new Result(RspCode.PARAMS_ERROR,null);
        }
        String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
        DataSource dataSource=new DataSource();
        BeanUtils.copyProperties(dataSourceVO,dataSource);
        dataSource.setUserId(userId);
        dataSource.setCreateTime(Calendar.getInstance().getTime());
        try{
            dataSourceService.insert(dataSource);
            return new Result(RspCode.SUCCESS);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("update data source error param"+dataSourceVO.toString(),e);
            return new Result(RspCode.UNKNOWN_ERROR);
        }
    }

    @GetMapping("/dataSource/getTypes")
    public Result getDataSourceTypes(){
        try{
            Result result = new Result(RspCode.SUCCESS);
            result.setData(BaseConstant.DATASOURCE_TYPES);
            return result;
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("get data source types error",e);
            return new Result(RspCode.UNKNOWN_ERROR);
        }
    }

    @GetMapping("/dataSource/getByType/{typeId}")
    public Result getDataSourceByType(@PathVariable Integer typeId){
        try{
            Result result = new Result(RspCode.SUCCESS);
            DataSource dataSource = new DataSource();
            dataSource.setType(typeId);
            List<DataSourceDTO> list = dataSourceService.getDTOByObj(dataSource);
            if (list != null){
                List<DataSourceVO> dataSourceVOS =  list.parallelStream().map(dataSource1 -> {
                    DataSourceVO dataSourceVO = new DataSourceVO();
                    BeanUtils.copyProperties(dataSource1, dataSourceVO);
                    return dataSourceVO;
                }).collect(Collectors.toList());
                result.setData(dataSourceVOS);
            }
            return result;
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("get data source by type error",e);
            return new Result(RspCode.UNKNOWN_ERROR);
        }
    }

    @PostMapping("/dataSource/crawLog")
    public Result logCrawPreview(@Valid @RequestBody LogCrawParamVO logCrawParamVO, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()){
            logger.info("craw log wrong param {} ",logCrawParamVO);
            return new Result(RspCode.PARAMS_ERROR);
        }
        try{
            String userId = (String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
            DataSource dataSource = new DataSource();
            dataSource.setId(logCrawParamVO.getId());
            dataSource.setUserId(userId);
            List<DataSourceDTO> dataSourceDTOs = dataSourceService.getDTOByObj(dataSource);
            if (dataSourceDTOs == null || dataSourceDTOs.size() < 1){
                return new Result(RspCode.EXCEPTION,"数据源不存在！");
            }
            //查询数据源
            DataSourceDTO dto = dataSourceDTOs.get(0);
            //根据数据源抓取日志
            List<String> logs = new ArrayList<>();
            //kafka数据源
            if (logCrawParamVO.getType() == BaseConstant.DATASOURCE_TYPES[0]) {
                logs = kafkaUtils.readKafka(dto.getClusterIp(),Arrays.asList(dto.getTopic().split(",")),dto.getConsumerGroup());
            }
            if (logs.size() > 0){
               return new Result(RspCode.SUCCESS,"抓取成功！",logs);
            }
            return new Result(RspCode.NO_RESULT, "日志抓取失败,请确保目标中存在数据!");
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("craw log error.",e);
            return new Result(RspCode.EXCEPTION);
        }
    }
}
