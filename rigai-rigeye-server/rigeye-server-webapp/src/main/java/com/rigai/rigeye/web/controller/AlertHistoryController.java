package com.rigai.rigeye.web.controller;

import com.rigai.rigeye.alert.bean.AlertHistoryDetailInfo;
import com.rigai.rigeye.alert.service.AlertApiService;
import com.rigai.rigeye.common.bean.AlertHistoryQueryParam;
import com.rigai.rigeye.web.bean.common.PageInfoVO;
import com.rigai.rigeye.web.bean.common.PageVO;
import com.em.fx.common.bean.Result;
import com.em.fx.common.bean.constant.RspCode;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/9/4.
 */

@RestController
@RequestMapping("/alert/history")
public class AlertHistoryController {


    private Logger logger= LoggerFactory.getLogger(AlertHistoryController.class);

    @Autowired
    AlertApiService alertApiService;

    @PostMapping("/list")
    public Result detailList(@RequestBody @Valid PageInfoVO<AlertHistoryQueryParam> pageInfoVO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            FieldError fieldError= bindingResult.getFieldError();
            logger.info("list alert task wrong param {} ", fieldError.getDefaultMessage());
            return new Result(RspCode.PARAMS_ERROR, null);
        }
        logger.info("/alert/history/list get param{}",pageInfoVO);
        PageInfo<AlertHistoryDetailInfo> list = alertApiService.pageGetAlertHistoryDetailInfo(pageInfoVO.getInfo(),pageInfoVO.getPage(),pageInfoVO.getPageSize());
        if(list==null){
            return new Result(RspCode.SUCCESS);
        }
        Collections.sort(list.getList());
        PageVO<AlertHistoryDetailInfo> pageVO=new PageVO<>(list.getList(),list.getPages(),list.getTotal());
        Result result=new Result(RspCode.SUCCESS);
        result.setData(pageVO);
        return result;
    }
}
