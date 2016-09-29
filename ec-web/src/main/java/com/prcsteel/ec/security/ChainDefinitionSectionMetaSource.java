package com.prcsteel.ec.security;

import org.apache.shiro.config.Ini;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by rolyer on 15-7-18.
 */
public class ChainDefinitionSectionMetaSource implements FactoryBean<Ini.Section> {

    private String filterChainDefinitions;

    private static final String BASE_KEY = "/**";
    private static final String BASE_VALUE = "anon";

    public Ini.Section getObject() throws BeansException {

        Ini ini = new Ini();
        ini.load(filterChainDefinitions);
        Ini.Section section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        section.put(BASE_KEY, BASE_VALUE);
        return section;
    }

    /**
     *
     * 通过filterChainDefinitions对默认的url过滤定义
     *
     * @param filterChainDefinitions 默认的url过滤定义
     */
    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    public Class<?> getObjectType() {
        return this.getClass();
    }

    public boolean isSingleton() {
        return false;
    }
}
