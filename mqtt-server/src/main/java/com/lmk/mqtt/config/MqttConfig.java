package com.lmk.mqtt.config;

import com.lmk.mqtt.MqttChannelInboundHandler;
import com.lmk.mqtt.entity.MqttProperties;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.channels.Channel;

/**
 * @author lmk
 * @version 1.0.0
 * @ClassName MqttConfig.java
 * @Description TODO
 * @createTime 2022-06-26 11:12:44
 */
@Configuration
@EnableConfigurationProperties(MqttProperties.class)
public class MqttConfig {

    @Bean(name = "channelGroup")
    public ChannelGroup createChannelGroup(){
        return new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    @Bean(name = "bossGroup")
    public NioEventLoopGroup createBossGroup(){
        return new NioEventLoopGroup(1);
    }

    @Bean(name = "workGroup")
    public NioEventLoopGroup createWorkGroup(){
        return new NioEventLoopGroup();
    }


}
