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

import java.util.Collections;
import java.util.List;

/**
 * Protocol. (API/SPI, Singleton, ThreadSafe)
 * 说下为什么很多支持SPI的接口都需要生成自适应类对象？
 * dubbo框架开发了一个功能：调用getAdaptiveExtension()，返回配置url中与某个key对应的扩展实现对象。具体实现就是取url中的值，
 * 再去加载或者去找对应的对象。这个具体的实现代码就是为每个SPI接口动态生成的自适应类对象的内部逻辑。
 * 也就是每一个SPI接口都会动态生成一个自适应类对象，主要作用就是解析url，返回配置的对应对象或者默认对象。
 *
 * 对Protocol,Exporter,Invoker的关系总结，详见AbstractProtocol。
 *
 * spi扩展加载的时候，如果调用getAdaptiveExtension()方法，会取接口头部@SPI("我是默认扩展名称")的value值，
 * 作为动态生成的自适应类中String string = uRL.getParameter("proxy", "我是默认扩展名称");的名称获取返回对应的扩展类。
 * 例如Protocol生成的自适应代理类Protocol$Adaptive源码如下：
 * package org.apache.dubbo.rpc;
 *
 * import java.util.List;
 * import org.apache.dubbo.common.URL;
 * import org.apache.dubbo.common.extension.ExtensionLoader;
 * import org.apache.dubbo.rpc.Exporter;
 * import org.apache.dubbo.rpc.Invoker;
 * import org.apache.dubbo.rpc.Protocol;
 * import org.apache.dubbo.rpc.RpcException;
 *
 * public class Protocol$Adaptive implements Protocol {
 *     public void destroy() {
 *         throw new UnsupportedOperationException("The method public abstract void org.apache.dubbo.rpc.Protocol.destroy() of interface org.apache.dubbo.rpc.Protocol is not adaptive method!");
 *     }
 *
 *     public int getDefaultPort() {
 *         throw new UnsupportedOperationException("The method public abstract int org.apache.dubbo.rpc.Protocol.getDefaultPort() of interface org.apache.dubbo.rpc.Protocol is not adaptive method!");
 *     }
 *
 *     public List getServers() {
 *         throw new UnsupportedOperationException("The method public default java.util.List org.apache.dubbo.rpc.Protocol.getServers() of interface org.apache.dubbo.rpc.Protocol is not adaptive method!");
 *     }
 *
 *     public Exporter export(Invoker invoker) throws RpcException {
 *         String string;
 *         if (invoker == null) {
 *             throw new IllegalArgumentException("org.apache.dubbo.rpc.Invoker argument == null");
 *         }
 *         if (invoker.getUrl() == null) {
 *             throw new IllegalArgumentException("org.apache.dubbo.rpc.Invoker argument getUrl() == null");
 *         }
 *         URL uRL = invoker.getUrl();
 *         *注意Protocol比较特别，url默认有这个属性，所以直接调用getProtocol()方法获取，其他的不一样
 *         String string2 = string = uRL.getProtocol() == null ? "dubbo" : uRL.getProtocol();
 *         if (string == null) {
 *             throw new IllegalStateException(new StringBuffer().append("Failed to get extension (org.apache.dubbo.rpc.Protocol) name from url (").append(uRL.toString()).append(") use keys([protocol])").toString());
 *         }
 *         Protocol protocol = (Protocol)ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(string);
 *         return protocol.export(invoker);
 *     }
 *
 *     public Invoker refer(Class clazz, URL uRL) throws RpcException {
 *         String string;
 *         if (uRL == null) {
 *             throw new IllegalArgumentException("url == null");
 *         }
 *         URL uRL2 = uRL;
 *         String string2 = string = uRL2.getProtocol() == null ? "dubbo" : uRL2.getProtocol();
 *         if (string == null) {
 *             throw new IllegalStateException(new StringBuffer().append("Failed to get extension (org.apache.dubbo.rpc.Protocol) name from url (").append(uRL2.toString()).append(") use keys([protocol])").toString());
 *         }
 *         Protocol protocol = (Protocol)ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(string);
 *         return protocol.refer(clazz, uRL);
 *     }
 * }
 */
@SPI("dubbo")
public interface Protocol {

    /**
     * Get default port when user doesn't config the port.
     *
     * @return default port
     */
    int getDefaultPort();

    /**
     * Export service for remote invocation: <br>
     * 1. Protocol should record request source address after receive a request:
     * RpcContext.getContext().setRemoteAddress();<br>
     * 2. export() must be idempotent, that is, there's no difference between invoking once and invoking twice when
     * export the same URL<br>
     * 3. Invoker instance is passed in by the framework, protocol needs not to care <br>
     *
     * @param <T>     Service type
     * @param invoker Service invoker
     * @return exporter reference for exported service, useful for unexport the service later
     * @throws RpcException thrown when error occurs during export the service, for example: port is occupied
     */
    @Adaptive
    <T> Exporter<T> export(Invoker<T> invoker) throws RpcException;

    /**
     * Refer a remote service: <br>
     * 1. When user calls `invoke()` method of `Invoker` object which's returned from `refer()` call, the protocol
     * needs to correspondingly execute `invoke()` method of `Invoker` object <br>
     * 2. It's protocol's responsibility to implement `Invoker` which's returned from `refer()`. Generally speaking,
     * protocol sends remote request in the `Invoker` implementation. <br>
     * 3. When there's check=false set in URL, the implementation must not throw exception but try to recover when
     * connection fails.
     *
     * @param <T>  Service type
     * @param type Service class
     * @param url  URL address for the remote service
     * @return invoker service's local proxy
     * @throws RpcException when there's any error while connecting to the service provider
     */
    @Adaptive
    <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException;

    /**
     * Destroy protocol: <br>
     * 1. Cancel all services this protocol exports and refers <br>
     * 2. Release all occupied resources, for example: connection, port, etc. <br>
     * 3. Protocol can continue to export and refer new service even after it's destroyed.
     */
    void destroy();

    /**
     * Get all servers serving this protocol
     *
     * @return
     */
    default List<ProtocolServer> getServers() {
        return Collections.emptyList();
    }

}