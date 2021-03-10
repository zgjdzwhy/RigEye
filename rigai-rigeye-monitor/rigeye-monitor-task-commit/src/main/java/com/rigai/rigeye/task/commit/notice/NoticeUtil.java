package com.rigai.rigeye.task.commit.notice;

import com.alibaba.fastjson.JSONObject;
import com.rigai.rigeye.task.commit.bean.notice.mail.BaseResult;
import com.rigai.rigeye.task.commit.bean.notice.mail.MailInfo;
import com.rigai.rigeye.task.commit.bean.notice.sms.SMSResult;
import com.rigai.rigeye.task.commit.bean.notice.sms.ShotMessageInfo;
import com.rigai.rigeye.task.commit.bean.notice.weixin.WeiXinRep;
import com.rigai.rigeye.task.commit.bean.notice.weixin.WeiXinReqInfo;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenxing
 * Created by ChenXing on 2018/9/14.
 */
@Component("noticeUtil")
public class NoticeUtil {

    @Value("${SMSUrl}")
    private String smsUrl;
    @Value("${mailUrl}")
    private String mailUrl;
    @Value("${weiXinUrl}")
    private String weiXinUrl;

    @Autowired
    private CloseableHttpClient httpClient;

    private static final String WINXIN_USER_STR="touser";
    private static final String WINXIN_CONTENT_STR="content";

    private Logger logger= LoggerFactory.getLogger(NoticeUtil.class);

    public SMSResult sendMessage(ShotMessageInfo param) throws IOException {
        HttpPost req=new HttpPost(smsUrl);
        System.out.println(JSONObject.toJSONString(param));
        StringEntity entity=new StringEntity(JSONObject.toJSONString(param), Charset.forName("utf-8"));
        entity.setContentType("application/json");
        req.setEntity(entity);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String result = httpClient.execute(req, responseHandler);
        logger.debug("send shot message with param： {}---platform return result： {}",param,result);
        return JSONObject.parseObject(result, SMSResult.class);
    }

    public BaseResult sendMail(MailInfo mailInfo) throws IOException {
        HttpPost req=new HttpPost(mailUrl);
        StringEntity entity=new StringEntity(JSONObject.toJSONString(mailInfo), Charset.forName("utf-8"));
        entity.setContentType("application/json");
        req.setEntity(entity);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String result = httpClient.execute(req, responseHandler);
        logger.debug("send mail with param： {}---platform return result： {}",mailInfo,result);
        return JSONObject.parseObject(result, BaseResult.class);
    }

    /**
     * 发送微信
     * @param param 包含联系人和报警内容，联系人使用邮箱
     * @return
     * @throws IOException
     */
    public WeiXinRep sendWeiXin(WeiXinReqInfo param) throws IOException {
        HttpPost req=new HttpPost(weiXinUrl);
        List<NameValuePair> pairs = new ArrayList<>(2);
        pairs.add(new BasicNameValuePair(WINXIN_USER_STR,param.getTousers()));
        pairs.add(new BasicNameValuePair(WINXIN_CONTENT_STR,param.getContent()));
        req.setEntity(new UrlEncodedFormEntity(pairs));
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String result = httpClient.execute(req, responseHandler);
        logger.debug("send weixin with param： {}---platform return result： {}",param,result);
        return JSONObject.parseObject(result, WeiXinRep.class);
    }
}
