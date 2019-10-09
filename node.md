```text
服务发现
客户端负载均衡
熔断器
服务网关
分布式配置中心
```

```text
new SpringApplication():

setInitializers
设置Spring所有的Initializer
搜索jar包下面所有的META-INF/spring.factories,并加载Initializer


run:
this.prepareContext(context, environment, listeners, ex, printedBanner);
this.refreshContext(context);
     postProcessBeanFactory:加载autoconfigure注解的地方 (RedisAutoConfiguration
     MybatisAutoConfiguration都需要本地配置文件存在相关配置项)   

this.afterRefresh(context, ex);

```