package com.fayayo.config;

import com.fayayo.bean.DBParam;
import com.fayayo.bean.Param;
import com.fayayo.bean.RedisParam;
import com.fayayo.bean.RequestResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * @author dalizu on 2019/9/27.
 * @version v1.0
 * @desc 从配置中心初始化配置信息
 */
@Configuration
public class ParamConfiguration {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Gson gson;

    @Value("${system.id}")
    private int sysId;

    @Value("${system.name}")
    private String sysName;

    @Value("${system.env}")
    private String envName;

    @Value("${system.scc-server}")
    private String sccServer;

    private RedisParam redisParam;

    private DBParam dbParam;

    @PostConstruct
    public void init() throws Exception{

        //读取远程配置
        Map<String,String>result=initParams();

        //2初始化缓存连接
        redisParam=initRedisParam(result);


        //初始化数据库连接

        initDbParam(result);

    }

    private DBParam initDbParam(Map<String,String> result) throws Exception{
        for (int i=0;i<DB_KEY_TMPL.length;i++){
            if(StringUtils.isEmpty(result.get(DB_KEY_TMPL[i]))){
                throw new Exception("必要参数："+DB_KEY_TMPL[i]);
            }
        }
        String url = result.get(DB_KEY_TMPL[0]);
        String username = result.get(DB_KEY_TMPL[1]);
        String password = result.get(DB_KEY_TMPL[2]);
        return new DBParam(url,username,password);
    }
    private static final String[] DB_KEY_TMPL={
            "scc.db.url","scc.db.user","scc.db.password"
    };


    private static final String[] REDIS_KEY_TMPL={
            "scc.redis.ip","scc.redis.port","scc.redis.timeout"
    };

    private RedisParam initRedisParam(Map<String, String> result) throws Exception {

        for (int i=0;i<REDIS_KEY_TMPL.length;i++){
            if(StringUtils.isEmpty(result.get(REDIS_KEY_TMPL[i]))){
                throw new Exception("必要参数："+REDIS_KEY_TMPL[i]);
            }
        }
        String host = result.get(REDIS_KEY_TMPL[0]);
        int port = Integer.parseInt(result.get(REDIS_KEY_TMPL[1]));
        long timeout = Long.parseLong(result.get(REDIS_KEY_TMPL[2]));

        return new RedisParam(host,port, Duration.ofMillis(timeout));
    }

    public RedisParam getRedisParam() {
        return redisParam;
    }

    public void setRedisParam(RedisParam redisParam) {
        this.redisParam = redisParam;
    }

    public DBParam getDbParam() {
        return dbParam;
    }

    public void setDbParam(DBParam dbParam) {
        this.dbParam = dbParam;
    }

    private Map<String,String> initParams() throws Exception {

        RequestResult requestResult = restTemplate.postForObject("http://"+sccServer+"/queryparaminfo/"+sysId+"/"+envName,null,
                RequestResult.class);

        if(requestResult ==null){
            throw new Exception("初始化配置中心连接出错");
        }
        if("0".equals(requestResult.getStatus())){
            throw new Exception("初始化配置中心连接出错");
        }
        Map<String,String> paramMap = Maps.newHashMap();

        List<Param> params = Lists.newArrayList();

        new JsonParser().parse(requestResult.getData().toString())
                .getAsJsonArray().forEach(jsonElement -> {
            params.add(gson.fromJson(jsonElement,Param.class));
        });

        params.forEach(param->{
            paramMap.put(param.getKey(),param.getValue());

        });
        return paramMap;

    }


}
