package com.prcsteel.ec.service.impl;

import com.google.gson.Gson;
import com.prcsteel.ec.service.ActiveMQService;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Created by Rolyer on 2016/5/23.
 */
@Service("activeMQService")
public class ActiveMQServiceImpl implements ActiveMQService {

    @Resource
    private JmsTemplate jmsTemplate;

    public ActiveMQServiceImpl() {
    }

    public ActiveMQServiceImpl(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void send(final Object o) {
        jmsTemplate.send(session -> {
            final TextMessage message = session.createTextMessage();
            message.setText(new Gson().toJson(o));
            return message;
        });
    }

    @Override
    public void send(final String destination, final Object o) {
        jmsTemplate.send(destination, session -> {
            final TextMessage message = session.createTextMessage();
            message.setText(new Gson().toJson(o));
            return message;
        });
    }

}