package com.fayayo.service;

import com.fayayo.bean.Param;
import com.fayayo.bean.SCCSystem;
import com.fayayo.bean.SCCUser;

import java.util.List;

/**
 * @author dalizu on 2019/9/27.
 * @version v1.0
 * @desc
 */
public interface SysService {


    SCCUser queryUserInfo(String token);

    List<SCCSystem> querySysInfo();

    List<Param> queryParamInfo(String sysId, String envName);

    int updateParam(String paramId,String paramValue);

}
