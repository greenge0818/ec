package com.prcsteel.ec.service.impl;

import com.prcsteel.ec.core.model.AMQMessage;
import com.prcsteel.ec.service.ActiveMQSinkService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Rolyer on 2016/5/23.
 */
@Service("activeMQSinkService")
public class ActiveMQSinkServiceImpl implements ActiveMQSinkService {
    private final List<AMQMessage> messages = new CopyOnWriteArrayList<>();

    @Override
    public void putMessage(AMQMessage message) {
        messages.add(0, message);
    }

    @Override
    public List<AMQMessage> getMessages() {
        return Collections.unmodifiableList(Arrays.asList(messages.toArray(new AMQMessage[messages.size()])));
    }
}