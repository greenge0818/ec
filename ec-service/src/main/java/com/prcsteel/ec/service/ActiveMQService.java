package com.prcsteel.ec.service;

/**
 * Created by Rolyer on 2016/5/23.
 */
public interface ActiveMQService {
    void send(Object o);

    void send(String destination, Object o);
}