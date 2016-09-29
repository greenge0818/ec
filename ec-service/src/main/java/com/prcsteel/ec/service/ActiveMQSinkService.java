package com.prcsteel.ec.service;

import com.prcsteel.ec.core.model.AMQMessage;

import java.util.List;

/**
 * Created by Rolyer on 2016/5/23.
 */
public interface ActiveMQSinkService {
    void putMessage(AMQMessage amqMessage);

    List<AMQMessage> getMessages();
}