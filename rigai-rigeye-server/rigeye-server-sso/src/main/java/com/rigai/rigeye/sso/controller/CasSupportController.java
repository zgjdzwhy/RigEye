package com.rigai.rigeye.sso.controller;

import com.rigai.rigeye.sso.bean.User;
import com.rigai.rigeye.sso.constant.CasConstant;
import com.em.fx.redis.dao.RedisDao;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

/**
 * @author chenxing
 * Created by ChenXing on 2017/10/31.
 */
@Controller
public class CasSupportController {

    private static Logger logger= LoggerFactory.getLogger(CasSupportController.class);
    @Autowired
    RedisDao redisDao;
    @Value("${redirectUrl}")
    private String redirectUrl;
    @Value("${frontIndex}")
    private String frontIndex;
    @Value("${ticketTime}")
    private int ticketTime;
    @Value("${casServerLogoutUrl}")
    private String casLogoutUrl;

    @RequestMapping(path = "index")
    public void index(HttpServletRequest request,HttpServletResponse response) {
    	User user = new User();
        String empId=request.getUserPrincipal().getName();
        AttributePrincipal principal = (AttributePrincipal)request.getUserPrincipal();
        Map attributes = principal.getAttributes();
        String userName=attributes.get("userName").toString();
        try {
			userName = URLDecoder.decode(userName,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
        if(empId==null){
            try{
                response.sendRedirect(redirectUrl);
            }catch (IOException e){
                logger.error("no ticket redirect to {} error",redirectUrl);
                logger.error("error: {}", ExceptionUtils.getStackTrace(e));
            }
        }else {
        	user.setUserId(empId);
        	user.setUserName(userName);
            String ticket= UUID.randomUUID().toString();
            redisDao.setObj(CasConstant.TICKET_REDIS_KEY_PREFIX+ticket, user, ticketTime);
            Cookie cookie=new Cookie(CasConstant.TICKET_COOKIE_KEY,ticket);
            response.addCookie(cookie);
            try {
                response.sendRedirect(frontIndex);
            } catch (IOException e) {
                logger.error("redirect to {} error",frontIndex);
                logger.error("error: {}", ExceptionUtils.getStackTrace(e));
            }
        }
    }


    @RequestMapping("/logout")
    public void logout(HttpServletResponse response, HttpServletRequest request){
        Cookie[] cookies= request.getCookies();
        String ticket=null;
        if(cookies!=null&&cookies.length>0){
            for(Cookie c:cookies){
                if(c.getName().equalsIgnoreCase(CasConstant.TICKET_COOKIE_KEY)){
                    ticket=c.getValue();
                }
            }
        }

        if(ticket!=null){
            String redisKey=CasConstant.TICKET_REDIS_KEY_PREFIX+ticket;
            if(redisDao.hasKey(redisKey)){
                redisDao.del(redisKey);
            }
        }
        try {
            if(request.getSession()!=null){
                request.getSession().invalidate();
            }
            String url=this.constructRedirectUrl(casLogoutUrl, "service", redirectUrl, true, true);
            logger.info("logout redirect : {}", url);
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String constructRedirectUrl(String casServerLoginUrl, String serviceParameterName, String serviceUrl, boolean renew, boolean gateway) {
        return casServerLoginUrl + (casServerLoginUrl.contains("?")?"&":"?") + serviceParameterName + "=" + urlEncode(serviceUrl) ;
    }

    private String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            throw new RuntimeException(var2);
        }
    }
}
