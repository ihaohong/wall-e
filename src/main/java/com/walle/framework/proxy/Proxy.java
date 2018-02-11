package com.walle.framework.proxy;

/**
 * @author haohong
 */
public interface Proxy {
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
