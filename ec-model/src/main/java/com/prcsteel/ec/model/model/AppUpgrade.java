package com.prcsteel.ec.model.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppUpgrade{

    boolean upgrade;// upgrade 是否升级，升级：true，不升级：false
    String name;// 应用名称
    int versionCodeAndroid;// 版本号
    int versionCodeIos;//ios版本号
    String upgradeDesc;// 升级描述
    String pathAndroid;// 下载地址
    String pathIos; //ios下载地址
    boolean forcedUpgrade;// forcedUpgrade 是否强制升级，强制升级：true，不强制升级：false
    String size;// 应用大小

    //	Logger logger = LoggerFactory.getLogger(this.getClass());
    public AppUpgrade() {
        Properties prop = new Properties();
        InputStream in = this.getClass().getResourceAsStream("/app-upgrade.properties");

        try {
            prop.load(in);
//			upgrade = prop.getProperty("upgrade");
            name = prop.getProperty("name");
            versionCodeAndroid = Integer.parseInt(prop.getProperty("versionCode_android"));
            versionCodeIos = Integer.parseInt(prop.getProperty("versionCode_ios"));
            upgradeDesc = prop.getProperty("upgradeDesc");
            pathAndroid = prop.getProperty("path_android");
            pathIos = prop.getProperty("path_ios");
            forcedUpgrade = Boolean.parseBoolean(prop.getProperty("forcedUpgrade"));
            size = prop.getProperty("size");
        } catch (IOException e) {
//			logger.error("生成安卓更新信息出错:"+e.getMessage());
        }
    }
    public boolean isUpgrade() {
        return upgrade;
    }
    public void setUpgrade(boolean upgrade) {
        this.upgrade = upgrade;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUpgradeDesc() {
        return upgradeDesc;
    }
    public void setUpgradeDesc(String upgradeDesc) {
        this.upgradeDesc = upgradeDesc;
    }
    public String getPathAndroid() {
        return pathAndroid;
    }
    public void setPathAndroid(String pathAndroid) {
        this.pathAndroid = pathAndroid;
    }
    public String getPathIos() {
        return pathIos;
    }
    public void setPathIos(String pathIos) {
        this.pathIos = pathIos;
    }
    public boolean isForcedUpgrade() {
        return forcedUpgrade;
    }
    public void setForcedUpgrade(boolean forcedUpgrade) {
        this.forcedUpgrade = forcedUpgrade;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public int getVersionCodeIos() {
        return versionCodeIos;
    }
    public void setVersionCodeIos(int versionCodeIos) {
        this.versionCodeIos = versionCodeIos;
    }
    public int getVersionCodeAndroid() {
        return versionCodeAndroid;
    }
    public void setVersionCodeAndroid(int versionCodeAndroid) {
        this.versionCodeAndroid = versionCodeAndroid;
    }

}
