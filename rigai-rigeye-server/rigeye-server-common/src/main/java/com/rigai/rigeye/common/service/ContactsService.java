package com.rigai.rigeye.common.service;


import com.em.fx.core.bussiness.BaseBussiness;
import com.rigai.rigeye.common.model.Contacts;

/**
 * @author Admin
 * @date 2018/08/01
 */
public interface ContactsService extends BaseBussiness<Contacts> {

    /**
     * 从CMDB同步联系人
     * @return
     */
    public boolean syncContacts();

}
