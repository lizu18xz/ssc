package com.fayayo.service;

import com.fayayo.bean.SCCUser;

/**
 * @author dalizu on 2019/9/25.
 * @version v1.0
 * @desc
 */
public interface UserService {


    SCCUser login(String username, String password);

    boolean userExistInCache(String token);


}
