/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.rpc;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

import static org.apache.dubbo.rpc.Constants.PROXY_KEY;

/**
 * ProxyFactory. (API/SPI, Singleton, ThreadSafe)
 * 默认是javassist
 *
 * 生成的自适应类源码：
 * package org.apache.dubbo.rpc;
 *
 * import org.apache.dubbo.common.URL;
 * import org.apache.dubbo.common.extension.ExtensionLoader;
 * import org.apache.dubbo.rpc.Invoker;
 * import org.apache.dubbo.rpc.ProxyFactory;
 * import org.apache.dubbo.rpc.RpcException;
 *
 * public class ProxyFactory$Adaptive implements ProxyFactory {
 *     public Object getProxy(Invoker invoker) throws RpcException {
 *         if (invoker == null) {
 *             throw new IllegalArgumentException("org.apache.dubbo.rpc.Invoker argument == null");
 *         }
 *         if (invoker.getUrl() == null) {
 *             throw new IllegalArgumentException("org.apache.dubbo.rpc.Invoker argument getUrl() == null");
 *         }
 *         URL uRL = invoker.getUrl();
 *         String string = uRL.getParameter("proxy", "javassist");
 *         if (string == null) {
 *             throw new IllegalStateException(new StringBuffer().append("Failed to get extension (org.apache.dubbo.rpc.ProxyFactory) name from url (").append(uRL.toString()).append(") use keys([proxy])").toString());
 *         }
 *         ProxyFactory proxyFactory = (ProxyFactory)ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension(string);
 *         return proxyFactory.getProxy(invoker);
 *     }
 *
 *     public Object getProxy(Invoker invoker, boolean bl) throws RpcException {
 *         if (invoker == null) {
 *             throw new IllegalArgumentException("org.apache.dubbo.rpc.Invoker argument == null");
 *         }
 *         if (invoker.getUrl() == null) {
 *             throw new IllegalArgumentException("org.apache.dubbo.rpc.Invoker argument getUrl() == null");
 *         }
 *         URL uRL = invoker.getUrl();
 *         String string = uRL.getParameter("proxy", "javassist");
 *         if (string == null) {
 *             throw new IllegalStateException(new StringBuffer().append("Failed to get extension (org.apache.dubbo.rpc.ProxyFactory) name from url (").append(uRL.toString()).append(") use keys([proxy])").toString());
 *         }
 *         ProxyFactory proxyFactory = (ProxyFactory)ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension(string);
 *         return proxyFactory.getProxy(invoker, bl);
 *     }
 *
 *     public Invoker getInvoker(Object object, Class clazz, URL uRL) throws RpcException {
 *         if (uRL == null) {
 *             throw new IllegalArgumentException("url == null");
 *         }
 *         URL uRL2 = uRL;
 *         String string = uRL2.getParameter("proxy", "javassist");
 *         if (string == null) {
 *             throw new IllegalStateException(new StringBuffer().append("Failed to get extension (org.apache.dubbo.rpc.ProxyFactory) name from url (").append(uRL2.toString()).append(") use keys([proxy])").toString());
 *         }
 *         ProxyFactory proxyFactory = (ProxyFactory)ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension(string);
 *         return proxyFactory.getInvoker(object, clazz, uRL);
 *     }
 * }
 *
 */
@SPI("javassist")
public interface ProxyFactory {

    /**
     * create proxy.
     *
     * @param invoker
     * @return proxy
     */
    @Adaptive({PROXY_KEY})
    <T> T getProxy(Invoker<T> invoker) throws RpcException;

    /**
     * create proxy.
     *
     * @param invoker
     * @return proxy
     */
    @Adaptive({PROXY_KEY})
    <T> T getProxy(Invoker<T> invoker, boolean generic) throws RpcException;

    /**
     * create invoker.
     *
     * @param <T>
     * @param proxy 如：GreetingServiceImpl对象，即ref对象
     * @param type 如：interface org.apache.dubbo.demo.GreetingService
     * @param url 如：injvm://127.0.0.1/org.apache.dubbo.demo.GreetingService
     *            ?anyhost=true&application=demo-provider&bind.ip=192.168.2.3&bind.port=20880
     *            &deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&group=greeting
     *            &interface=org.apache.dubbo.demo.GreetingService&mapping-type=metadata
     *            &mapping.type=metadata&metadata-type=remote&methods=hello&pid=25062&qos.port=22222
     *            &release=&revision=1.0.0&side=provider&timeout=5000&timestamp=1615949370199&version=1.0.0
     * @return invoker
     */
    @Adaptive({PROXY_KEY})
    <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws RpcException;

}