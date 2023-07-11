package com.vv.bi.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vv
 * @Description redisson 配置类
 * @date 2023/7/11-19:56
 */
@Configuration
//注意，此处应当与 业务redis分开填写，这里需修改 TODO
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

    /**
     * 端口
     */
    private Integer port;
    /**
     * 主机
     */
    private String host;
    /**
     * 密码
     */
    private String password;
    /**
     * 数据库
     */
    private Integer database;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer()
                .setDatabase(database)
                .setAddress("redis"+"://"+ host +":" + port);
                //.setPassword(password);
        return Redisson.create(config);
    }
}


