package com.fayayo.service;

import com.fayayo.bean.Param;
import com.fayayo.bean.SCCSystem;
import com.fayayo.bean.SCCUser;
import com.fayayo.utils.RedisStringUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author dalizu on 2019/9/27.
 * @version v1.0
 * @desc
 */
@Service
public class SysServiceImpl implements SysService {

    @Autowired
    RedisStringUtils redisStringUtils;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private Gson gson;

    @Override
    public SCCUser queryUserInfo(String token) {

        String value = redisStringUtils.get(RedisStringUtils.USER_CACHE_PREFIX+token);
        SCCUser sccUser = gson.fromJson(value,SCCUser.class);

        return sccUser;
    }

    @Override
    public List<SCCSystem> querySysInfo() {

        List<Map<String,Object>>dbRes=sqlSessionTemplate.selectList("sysMapper.querySysInfo");

        return convertdb2Obj(dbRes);
    }

    @Override
    public List<Param> queryParamInfo(String sysId, String envName) {

        List<Map<String,Object>>res= sqlSessionTemplate.selectList("sysMapper.queryParamInfo",
                ImmutableMap.of("SysId",sysId,"EnvName",envName));

        if(CollectionUtils.isEmpty(res)){
            return null;
        }


        List<Param>result=Lists.newArrayList();
        res.forEach(k->{

            int id=((Integer) k.get("id")).intValue();
            String paramKey = (String) k.get("paramKey");
            String paramValue = (String) k.get("paramValue") ;

            result.add(new Param(id,paramKey,paramValue));

        });

        return result;
    }

    @Override
    public int updateParam(String paramId, String paramValue) {

        int affectCount =sqlSessionTemplate.update("sysMapper.updateParam",
                ImmutableMap.of("Id",paramId,"ParamValue",paramValue));


        return affectCount;
    }

    private List<SCCSystem> convertdb2Obj(List<Map<String,Object>> dbRes){

        if(CollectionUtils.isEmpty(dbRes)){
            return null;
        }

        List<SCCSystem> result = Lists.newArrayList();
        dbRes.forEach(k ->{
            result.add(new SCCSystem(((Integer)k.get("sysid")).intValue(),k.get("sysname").toString()));
        });
        return result;
    }

}
