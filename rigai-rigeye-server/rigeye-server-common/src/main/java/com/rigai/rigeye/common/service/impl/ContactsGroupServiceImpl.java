package com.rigai.rigeye.common.service.impl;

import com.rigai.rigeye.common.dao.mysql.ContactsGroupDao;
import com.rigai.rigeye.common.model.ContactsGroup;
import com.rigai.rigeye.common.service.ContactsGroupService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.em.fx.core.bussiness.impls.BaseBussinessImpl;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Service("contactsGroupService")
public class ContactsGroupServiceImpl extends BaseBussinessImpl<ContactsGroup> implements ContactsGroupService {

	@Autowired
	private ContactsGroupDao contactsGroupDao;


}
