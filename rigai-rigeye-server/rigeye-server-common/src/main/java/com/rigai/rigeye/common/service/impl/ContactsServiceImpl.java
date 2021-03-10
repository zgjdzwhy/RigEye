package com.rigai.rigeye.common.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dianping.cat.Cat;
import com.rigai.rigeye.common.model.ContactsGroup;
import com.rigai.rigeye.common.service.ContactsGroupService;
import com.rigai.rigeye.common.dao.mysql.ContactsDao;
import com.rigai.rigeye.common.model.Contacts;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.rigai.rigeye.common.service.ContactsService;
import com.em.fx.core.bussiness.impls.BaseBussinessImpl;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Admin
 * @date 2018/08/01
 */

@Service("contactsService")
public class ContactsServiceImpl extends BaseBussinessImpl<Contacts> implements ContactsService {

	@Autowired
	private ContactsDao contactsDao;

	@Value("${contacts.apiUrl}")
	private String apiUrl;

	@Value("${contacts.secret}")
	private String secret;

	@Value("${contacts.signature}")
	private String signature;

	@Autowired
	private ContactsGroupService contactsGroupService;

	@Autowired
	private CloseableHttpClient closeableHttpClient;

	private ResponseHandler<String> responseHandler = new BasicResponseHandler();

	private Logger logger= LoggerFactory.getLogger(ContactsService.class);


	@Override
	@Transactional(rollbackFor =Exception.class)
	public boolean syncContacts() {
		ArrayList<Contacts> contactsInfo=new ArrayList<>();
		ArrayList<ContactsGroup> contactsGroups=new ArrayList<>();
		HttpGet httpGet = new HttpGet(apiUrl);
		httpGet.addHeader("secret",secret);
		httpGet.addHeader("signature",signature);
		String response = null;
		try {
			response = closeableHttpClient.execute(httpGet,responseHandler);
		} catch (IOException e) {
			logger.error("get contacts from cmdb error "+ExceptionUtils.getStackTrace(e));
			Cat.logError("get contacts from cmdb error ",e);
			return false;
		}
		JSONObject jsonObject= JSONObject.parseObject(response);
		JSONArray content=jsonObject.getJSONObject("data").getJSONArray("content");
		for(int j=0;j<content.size();j++){
			JSONArray modules=content.getJSONObject(j).getJSONArray("modules");
			for(int k=0;k<modules.size();k++){
				JSONObject module=modules.getJSONObject(k);
				ContactsGroup group=new ContactsGroup();
				group.setId(module.getInteger("id"));
				group.setGroupName(module.getString("name"));
				contactsGroups.add(group);
				JSONArray devs=module.getJSONArray("dev");
				for(int i=0;i<devs.size();i++){
					Contacts contacts=new Contacts();
					contacts.setGroupName(module.getString("name"));
					JSONObject dev=devs.getJSONObject(i);
					contacts.setMail(dev.getString("email"));
					contacts.setPhone(dev.getString("mobile"));
					contacts.setRealname(dev.getString("name"));
					contactsInfo.add(contacts);
				}
			}
		}
		this.deleteAll();
		this.insert(contactsInfo);
		contactsGroupService.deleteAll();
		contactsGroupService.insert(contactsGroups);
		return true;
	}
}
