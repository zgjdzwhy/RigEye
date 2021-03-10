package com.rigai.rigeye.web.controller;

import com.rigai.rigeye.common.model.DataSourceCluster;
import com.rigai.rigeye.common.service.DataSourceClusterService;
import com.em.fx.common.bean.Result;
import com.em.fx.common.bean.constant.RspCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/10/10.
 */

@Controller
@RequestMapping("dataSource/cluster")
public class DataSourceClusterController {
    @Resource
    DataSourceClusterService dataSourceClusterService;

    @GetMapping("/all")
    public @ResponseBody Result getAll(){
        List<DataSourceCluster> clusters= dataSourceClusterService.getAll();
        Result result=new Result(RspCode.SUCCESS);
        result.setData(clusters);
        return result;
    }

}
