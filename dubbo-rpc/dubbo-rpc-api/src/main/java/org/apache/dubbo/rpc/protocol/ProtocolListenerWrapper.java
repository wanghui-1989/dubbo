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
package org.apache.dubbo.rpc.protocol;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.common.utils.UrlUtils;
import org.apache.dubbo.rpc.Exporter;
import org.apache.dubbo.rpc.ExporterListener;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.InvokerListener;
import org.apache.dubbo.rpc.Protocol;
import org.apache.dubbo.rpc.ProtocolServer;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.listener.ListenerExporterWrapper;
import org.apache.dubbo.rpc.listener.ListenerInvokerWrapper;

import java.util.Collections;
import java.util.List;

import static org.apache.dubbo.common.constants.CommonConstants.EXPORTER_LISTENER_KEY;
import static org.apache.dubbo.common.constants.CommonConstants.INVOKER_LISTENER_KEY;
import static org.apache.dubbo.common.constants.RegistryConstants.REGISTRY_CLUSTER_TYPE_KEY;

/**
 * ListenerProtocol
 */
@Activate(order = 200)
public class ProtocolListenerWrapper implements Protocol {

    private final Protocol protocol;

    public ProtocolListenerWrapper(Protocol protocol) {
        //protocol默认实现是DubboProtocol
        if (protocol == null) {
            throw new IllegalArgumentException("protocol == null");
        }
        this.protocol = protocol;
    }

    @Override
    public int getDefaultPort() {
        return protocol.getDefaultPort();
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        if (UrlUtils.isRegistry(invoker.getUrl())) {
            return protocol.export(invoker);
        }
        return new ListenerExporterWrapper<T>(protocol.export(invoker),
                Collections.unmodifiableList(ExtensionLoader.getExtensionLoader(ExporterListener.class)
                        .getActivateExtension(invoker.getUrl(), EXPORTER_LISTENER_KEY)));
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        if (UrlUtils.isRegistry(url)) {
            //url为注册中心，服务发现时
            //url=service-discovery-registry://127.0.0.1:2181/org.apache.dubbo.registry.RegistryService
            //?REGISTRY_CLUSTER=org.apache.dubbo.config.RegistryConfig&application=demo-consumer&dubbo=2.0.2
            //&mapping-type=metadata&pid=46094&qos.port=33333&registry=zookeeper&registry-type=service&timestamp=1618267262937
            //protocol为RegistryProtocol
            return protocol.refer(type, url);
        }

        Invoker<T> invoker = protocol.refer(type, url);
        if (StringUtils.isEmpty(url.getParameter(REGISTRY_CLUSTER_TYPE_KEY))) {
            invoker = new ListenerInvokerWrapper<>(invoker,
                    Collections.unmodifiableList(
                            ExtensionLoader.getExtensionLoader(InvokerListener.class)
                                    .getActivateExtension(url, INVOKER_LISTENER_KEY)));
        }
        return invoker;
    }

    @Override
    public void destroy() {
        protocol.destroy();
    }

    @Override
    public List<ProtocolServer> getServers() {
        return protocol.getServers();
    }
}
