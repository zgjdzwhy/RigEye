package com.rigai.rigeye.common.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dianping.cat.Cat;
import com.rigai.rigeye.common.service.ModuleApiService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/10/11.
 */
@Service("moduleApiService")
public class ModuleApiServiceImpl implements ModuleApiService {

    @Value("${module.apiUrl}")
    private String apiUrl;

    @Value("${module.secret}")
    private String secret;

    @Value("${module.signature}")
    private String signature;

    private ResponseHandler<String> responseHandler = new BasicResponseHandler();

    private Logger logger= LoggerFactory.getLogger(ModuleApiServiceImpl.class);

    @Autowired
    private CloseableHttpClient closeableHttpClient;

    @Override
    public Set<String> getUserModule(String userId){
        HttpGet httpGet = new HttpGet(apiUrl+userId);
        httpGet.addHeader("secret",secret);
        httpGet.addHeader("signature",signature);
        String response = null;
        try {
            response = closeableHttpClient.execute(httpGet,responseHandler);
        } catch (IOException e) {
            logger.error("get user module from cmdb error ,user id:"+ userId+" error: "+ExceptionUtils.getStackTrace(e));
            Cat.logError("get user module from cmdb error ,user id:"+ userId,e);
            return null;
        }
        JSONObject responseJson=JSONObject.parseObject(response);
        JSONObject data=responseJson.getJSONObject("data");
        JSONArray moduleJsonArray=data.getJSONArray("content");
        Set<String> moduleSet=new HashSet<>(moduleJsonArray.size());
        for(int i=0;i<moduleJsonArray.size();i++){
            moduleSet.add(moduleJsonArray.getJSONObject(i).getString("name"));
        }
        return moduleSet;
    }
}
