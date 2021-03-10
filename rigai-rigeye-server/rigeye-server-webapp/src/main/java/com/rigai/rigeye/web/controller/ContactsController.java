package com.rigai.rigeye.web.controller;

import com.rigai.rigeye.common.model.Contacts;
import com.rigai.rigeye.common.service.ContactsService;
import com.rigai.rigeye.web.bean.common.PageInfoVO;
import com.rigai.rigeye.web.bean.common.PageVO;
import com.em.fx.common.bean.Result;
import com.em.fx.common.bean.constant.RspCode;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/14.
 */

@RestController
@RequestMapping("/contacts")
public class ContactsController {

    @Autowired
    ContactsService contactsService;

    private Logger logger= LoggerFactory.getLogger(ContactsController.class);

    @GetMapping("/sync")
    public Result sync(){
        boolean success=contactsService.syncContacts();
        if(success){
            return new Result(RspCode.SUCCESS);
        }else {
            return new Result(RspCode.EXCEPTION);
        }
    }

    @PostMapping("/list")
    public Result listContactsService(@RequestBody @Valid PageInfoVO pageInfoVO, BindingResult bindingResult) {
        //如果绑定结果有误则返回参数错误
        if (bindingResult.hasErrors()) {
            FieldError fieldError= bindingResult.getFieldError();
            logger.info("list contacts wrong param {} ", fieldError.getDefaultMessage());
            return new Result(RspCode.PARAMS_ERROR, null);
        }
        PageInfo<Contacts> contactsPageInfo = contactsService.pageGet(null, pageInfoVO.getPage(), pageInfoVO.getPageSize());
        PageVO<Contacts> pageVO = new PageVO<>(contactsPageInfo.getList(),contactsPageInfo.getPages(),contactsPageInfo.getTotal());
        Result result=new Result(RspCode.SUCCESS);
        result.setData(pageVO);
        return result;
    }

}
