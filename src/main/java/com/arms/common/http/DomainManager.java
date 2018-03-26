package com.arms.common.http;

import java.net.MalformedURLException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.arms.core.configure.ConfigResolver;
import com.arms.core.configure.DefaultConfigResolver;

/**
 * 域名解析工具类
 * zhangchaojie
 */
public class DomainManager {

    private String                        appEnv;

    private Configuration                 configuration;

    private static final Logger           logger = Logger.getLogger(DomainManager.class);

    private volatile static DomainManager domainManager;

    public static DomainManager getInstance() {
        if (domainManager == null) {
            synchronized (DomainManager.class) {
                if (domainManager == null) {
                    domainManager = new DomainManager();
                }
            }
        }
        return domainManager;
    }

    private DomainManager() {
        getAppEnv();
    }

    public String getAppEnv() {
        if (StringUtils.isBlank(appEnv)) {
            appEnv = getConfiguration().getString("app.env");
        }
        return appEnv;
    }

    public String getDomain(String key) {
        return getConfiguration().getString(key);
    }

    private Configuration getConfiguration() {
        if (configuration == null) {
            ConfigResolver configResolver = new DefaultConfigResolver();

            try {
                configuration = configResolver.getAppConfig();

            } catch (ConfigurationException e) {
                logger.error("", e);
            } catch (MalformedURLException e) {
                logger.error("", e);
            }
        }
        return configuration;
    }
}
