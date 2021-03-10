package com.rigai.rigeye.common.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.rigai.rigeye.common.constant.CommonConstant;

import java.util.Arrays;
import java.util.List;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/10/16.
 */

public class AuthorizationConfig {

    private static final String ADMIN_CONFIG_STR="admins";
    private static final String ENABLE_CONFIG_STR="authorizationEnable";
    private static String adminsStr;
    private static boolean authorizationEnable=false;
    private static List<String> admins=null;

    static {
        Config config= ConfigService.getAppConfig();
        adminsStr=config.getProperty(ADMIN_CONFIG_STR,null);
        authorizationEnable=config.getBooleanProperty(ENABLE_CONFIG_STR,authorizationEnable);
        if(adminsStr!=null){
            setAdmins();
            config.addChangeListener(changeEvent -> {
                for (String key : changeEvent.changedKeys()) {
                    ConfigChange change = changeEvent.getChange(key);
                    String propName=change.getPropertyName();
                    if(ADMIN_CONFIG_STR.equals(propName)){
                        adminsStr=change.getNewValue();
                        setAdmins();
                    }
                    if(ENABLE_CONFIG_STR.equals(propName)){
                        authorizationEnable=Boolean.valueOf(change.getNewValue());
                    }
                }
            });
        }
    }

    private static void setAdmins(){
        if(adminsStr!=null){
            admins= Arrays.asList(adminsStr.split(CommonConstant.SPLIT_SIGN));
        }
    }

    public static boolean isAdmin(String user){
        if(admins==null){
            return false;
        }
        for(String admin:admins){
            if(admin.equals(user)){
                return true;
            }
        }
        return false;
    }

    public static boolean isAuthorizationEnable(){
        return authorizationEnable;
    }
}
