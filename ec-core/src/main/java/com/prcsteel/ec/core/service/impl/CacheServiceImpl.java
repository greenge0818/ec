package com.prcsteel.ec.core.service.impl;

import com.prcsteel.ec.core.service.CacheService;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;

/**
 * Created by Green on 15-9-28.
 */
public class CacheServiceImpl implements CacheService {

    private static Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);
	
    private MemcachedClient client;
    
    @Value("${memcached.server.addressAndPort}")
    private String memcacheAddressAndPort;

    public void init() {
        if (client == null) {
            try {
                client = new MemcachedClient(AddrUtil.getAddresses(memcacheAddressAndPort));
            } catch (IOException e) {
            	logger.error("can't init memcached client!", e);
            }
        }
    }

    /**
     * 保存缓存数据
     *
     * @param key:健
     * @param exp:失效时间
     * @param o:值,可以是任意对象(Object)
     */
    @Override
    public Future<Boolean> set(String key, int exp, Object o) {
        client.delete(key);
        return client.set(key, exp, o);
    }

    /**
     * 获取缓存数据
     *
     * @param key:健
     * @return
     */
    @Override
    public Object get(String key) {
        Object o;
        try {
            o = client.get(key);
        } catch (CancellationException e) {
            return  null;
        }
        return o;

    }

    /**
     * 替换缓存中的数据
     *
     * @param key
     * @param exp
     * @param o
     * @return
     */
    @Override
    public Future<Boolean> replace(String key, int exp, Object o) {
        return client.replace(key, exp, o);
    }

    /**
     * 删除缓存
     *
     * @param key
     * @return
     */
    @Override
    public Future<Boolean> delete(String key) {
        return client.delete(key);
    }
    
    //只刷新时间，不对值作修改
    @Override
    public void touch(String key,Integer exp){
    	client.touch(key,exp);
    }

    /**
     * 添加缓存
     *
     * @param key     键
     * @param expired 失效时间
     * @param data    值
     */
    @Override
    public void add(String key, Integer expired, Object data) {
        client.add(key,expired,data);
    }

    /**
     * 得到缓存服务器客户端
     *
     * @return
     */
    public MemcachedClient getClient() {
        return client;
    }
    
    /**
     * 设置缓存服务器客户端
     *
     * @param client the client to set
     */
    public void setClient(MemcachedClient client) {
        this.client = client;
    }

}
