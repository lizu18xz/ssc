package com.fayayo.service;

import com.fayayo.bean.SCCUser;
import com.fayayo.utils.RedisStringUtils;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author dalizu on 2019/9/25.
 * @version v1.0
 * @desc
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private RedisStringUtils redisStringUtils;

    @Autowired
    private Gson gson;


    @Override
    public SCCUser login(String username, String password) {
        if(username==null ||password==null){
            return null;
        }

        Map<String,String>o=sqlSessionTemplate.selectOne("userMapper.queryUserByUserName",
                ImmutableMap.of("Username",username,"Password",password));

        if(CollectionUtils.isEmpty(o)){
            return null;
        }else {
            SCCUser sccUser=convertDb2Bean(o);

            //更新数据库信息
            updateDbUser(sccUser);

            //存入redis
            updateCacheUser(sccUser);

            return sccUser;
        }
    }

    private final int EXPIRE_SECONDS = 60*60*24;
    private void updateCacheUser(SCCUser user) {
        user.setUniqueId(Long.parseLong(UUID.randomUUID().toString().replace("-","").substring(0,10)));

        redisStringUtils.setKey(RedisStringUtils.USER_CACHE_PREFIX+user.getUniqueId(),
                gson.toJson(user),EXPIRE_SECONDS);

    }

    private void updateDbUser(SCCUser user) {

        //更新javaBean对象的最新登录时间
        user.setLastLoginDateTime(DateFormatUtils.format(new Date(),"yyyyMMdd HH:mm:ss"));

        //保存到数据库
        String[] dateTime =  user.getLastLoginDateTime().split(" ");

        sqlSessionTemplate.update("userMapper.updateUserLoginTime",ImmutableMap.of("UserName",user.getUserName(),
                "LastLoginDate",dateTime[0],"LastLoginTime",dateTime[1]));

    }

    @Override
    public boolean userExistInCache(String token) {
        if(token == null || "".equals(token)){
            return false;
        }
        String value = redisStringUtils.get(RedisStringUtils.USER_CACHE_PREFIX+token);
        if(!StringUtils.isEmpty(value)){
            //重新激活
            redisStringUtils.setKey(RedisStringUtils.USER_CACHE_PREFIX+token,
                    value,EXPIRE_SECONDS);
            return true;
        }
        return false;
    }


    private SCCUser convertDb2Bean(Map<String,String> res){
        return new SCCUser(res.get("username")
                ,res.get("lastlogindate"),res.get("lastlogintime"));
    }
}
