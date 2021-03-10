package com.rigai.rigeye.common.service.impl;

import com.rigai.rigeye.common.dao.mysql.ContactsDao;
import com.rigai.rigeye.common.model.Contacts;
import com.rigai.rigeye.common.service.ContactsService;
import com.em.fx.core.bussiness.impls.BaseBussinessImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Admin
 * @date 2018/08/01
 */

@Service("contactsService")
public class ContactsServiceImpl extends BaseBussinessImpl<Contacts> implements ContactsService {

	@Autowired
	private ContactsDao contactsDao;

}
