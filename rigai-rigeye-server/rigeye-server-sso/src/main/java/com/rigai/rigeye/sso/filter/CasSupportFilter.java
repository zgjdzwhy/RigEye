package com.rigai.rigeye.sso.filter;

import com.alibaba.fastjson.JSONObject;
import com.rigai.rigeye.common.constant.CommonConstant;
import com.rigai.rigeye.sso.bean.User;
import com.rigai.rigeye.sso.constant.CasConstant;
import com.rigai.rigeye.sso.utils.CasUrlConstructor;
import com.em.fx.common.bean.Result;
import com.em.fx.redis.dao.RedisDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenxing Created by ChenXing on 2017/11/2.
 */
@Component
@Order(2)
public class CasSupportFilter implements Filter {


    @Value("${server.servlet.context-path}")
    private String serverPath;
    @Value("${gateWay}")
    private String gateWay;
    @Value("${noLoginUrl}")
    private String noLoginUrl;
    @Autowired
    RedisDao redisDao;
    @Value("${casServerLoginUrl}")
    private String loginUrl;
    @Value("${serverNameInCas}")
    private String serverName;
    @Value("${ticketTime}")
    private int ticketTime;

    private Logger logger = LoggerFactory.getLogger(CasSupportFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if(!serverName.contains("http")){
            serverName="http://"+serverName;
        }
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rsp = (HttpServletResponse) response;
        String[] gateWays = gateWay.split(CommonConstant.SPLIT_SIGN);
        // 完整url
        String urlAll = req.getRequestURI();
        // 获取相对url
        String requestURL = urlAll.substring(urlAll.indexOf(serverPath) + serverPath.length(), urlAll.length());
        logger.debug("get relative request url {}", requestURL);
        // 如果有jsessionid则去除
        if (requestURL.indexOf(CasConstant.CAS_SESSION_SUFFIX) > 0) {
            requestURL = requestURL.substring(0, requestURL.indexOf(CasConstant.CAS_SESSION_SUFFIX) - 1);
        }
        // options请求不带参数不能校验，所以直接通过
        if (req.getMethod().equals(CasConstant.OPTION_TYPE)) {
            chain.doFilter(request, response);
            return;
        }


        // 如果是cas直接拦截的门户地址
        for (String way : gateWays) {
            if (way.equals(requestURL)) {
                chain.doFilter(req, rsp);
                return;
            }
        }
        // 如果访问的是无须授权处理页面
        for (String way : noLoginUrl.split(CommonConstant.SPLIT_SIGN)) {
            logger.debug("compare no loginUrl way {} with requestURL {}", way, requestURL);
            if (way.equals(requestURL)) {
                chain.doFilter(req, rsp);
                return;
            }
        }
        String ticket = request.getParameter(CasConstant.TICKET_COOKIE_KEY);
        Cookie[] cookies = req.getCookies();
        if (ticket == null && cookies != null && cookies.length > 0) {
            for (Cookie c : cookies) {
                if (c.getName().equalsIgnoreCase(CasConstant.TICKET_COOKIE_KEY)) {
                    ticket = c.getValue();
                }
            }
        }
        // 如果有ticket则校验
        if (ticket != null) {
            logger.debug("there is ticket in cookie or request");
            String redisKey = CasConstant.TICKET_REDIS_KEY_PREFIX + ticket;
            if (redisDao.hasKey(redisKey)) {
                User user = (User) redisDao.get(redisKey);
                String userId = user.getUserId();
                String userName = user.getUserName();
                logger.debug("sso get user: {}", userId);
                redisDao.setObj(redisKey, user, ticketTime);
                req.setAttribute(CasConstant.REQUEST_USER_ID_KEY, "100000");
                req.setAttribute(CasConstant.REQUEST_USER_NAME_KEY, "统一用户");
                req.setAttribute(CasConstant.TRUE_USER_KEY,userId);
                chain.doFilter(req, rsp);
                return;
            }
            logger.debug("no ticket in redis");
        }
        logger.debug("there is no ticket in cookie or request");
        Result result = new Result();
        result.setCode("4000009");
        String url = CasUrlConstructor.constructRedirectUrl(loginUrl, "service", serverName + serverPath + "/index", true, true);
        logger.debug("redirect to url {}" + url);
        result.setMessage(url);
        logger.debug("not login filter response");
        rsp.getWriter().print(JSONObject.toJSONString(result));
        rsp.getWriter().close();
        return;
    }


    @Override
    public void destroy() {

    }
}
