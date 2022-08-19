package com.lmk.mqtt.config;

import com.lmk.mqtt.rabbit.exchange.ExchangeEnum;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    /**
     * rabbitMQ服务器的地址
     */
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private Integer port;
    /**
     * rabbitMQ用户名
     */
    @Value("${spring.rabbitmq.username}")
    private String username;
    /**
     * rabbitMQ密码
     */
    @Value("${spring.rabbitmq.password}")
    private String password;
    /**
     * rabbitMQ虚拟机 默认 /
     */
    @Value("${spring.rabbitmq.virtual-host:/}")
    private String virtualHost;


    @Autowired
    @Qualifier("rabbitAdmin")
    private RabbitAdmin rabbitAdmin;

    /*
    @Autowired
    @Qualifier("rabbitMqMessageListener")
    private RabbitMqMessageListener rabbitMqMessageListener;

    @Autowired
    @Qualifier("primarySimpleMessageListenerContainer")
    private SimpleMessageListenerContainer simpleMessageListenerContainer;
    */

    @Bean
    public TopicExchange mqTopicExchange() {
        ExchangeEnum exchangeEnum = ExchangeEnum.MQ_TOPIC_EXCHANGE;
        return new TopicExchange(exchangeEnum.getExchangeName(), exchangeEnum.isDurable(), false, null);
    }

    @Bean(name = "connectionFactory")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost(this.host);
        cachingConnectionFactory.setPort(this.port);
        cachingConnectionFactory.setUsername(this.username);
        cachingConnectionFactory.setPassword(this.password);
        cachingConnectionFactory.setVirtualHost(this.virtualHost);
        return cachingConnectionFactory;
    }

    /*@Bean(name = "primarySimpleMessageListenerContainer")
    public SimpleMessageListenerContainer primaryMessageListenerContainer(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new
                SimpleMessageListenerContainer(connectionFactory);
        container.setDefaultRequeueRejected(false);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setMessageListener(rabbitMqMessageListener);
        container.addQueueNames(QueueEnum.DEFAULT_QUEUE.getQueueName());
        container.start();
        return container;
    }

    @Bean(name = "queue")
    public Queue defaultQueue() {
        QueueEnum queueEnum = QueueEnum.DEFAULT_QUEUE;
        return new Queue(
                queueEnum.getQueueName(),
                queueEnum.isDurable(),
                queueEnum.isExclusive(),
                queueEnum.isAutoDelete()
        );
    }

    @Bean(name = "bind")
    public Binding binding() {
        return new Binding(
                QueueEnum.DEFAULT_QUEUE.getQueueName(),
                Binding.DestinationType.QUEUE,
                ExchangeEnum.MQ_TOPIC_EXCHANGE.getExchangeName(),
                MqttConstant.ROUTING_KEY,
                null
        );
    }
    */
    @Bean(name = "jsonMessageConverter")
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name = "rabbitAdmin")
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        rabbitAdmin.declareExchange(mqTopicExchange());
        //rabbitAdmin.declareQueue(defaultQueue());
        //rabbitAdmin.declareBinding(binding());
        return rabbitAdmin;
    }

    @Bean("rabbitTemplate")
    public RabbitTemplate defaultRabbitTemplate(ConnectionFactory connectionFactory) {
        CachingConnectionFactory cachingConnectionFactory = (CachingConnectionFactory) connectionFactory;
        cachingConnectionFactory.setVirtualHost(this.virtualHost);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

}
