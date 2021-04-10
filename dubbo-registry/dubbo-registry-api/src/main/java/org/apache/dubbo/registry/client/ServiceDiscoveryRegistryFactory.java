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
package org.apache.dubbo.registry.client;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.UrlUtils;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.support.AbstractRegistryFactory;

import static org.apache.dubbo.common.constants.RegistryConstants.REGISTRY_KEY;
import static org.apache.dubbo.registry.Constants.DEFAULT_REGISTRY;

public class ServiceDiscoveryRegistryFactory extends AbstractRegistryFactory {

    @Override
    protected Registry createRegistry(URL url) {
        if (UrlUtils.hasServiceDiscoveryRegistryProtocol(url)) {
            String protocol = url.getParameter(REGISTRY_KEY, DEFAULT_REGISTRY);
            url = url.setProtocol(protocol).removeParameter(REGISTRY_KEY);
        }
        //url为：zookeeper://127.0.0.1:2181/org.apache.dubbo.registry.RegistryService
        // ?application=demo-consumer&dubbo=2.0.2&enable-auto-migration=true&enable.auto.migration=true
        // &id=org.apache.dubbo.config.RegistryConfig&interface=org.apache.dubbo.registry.RegistryService
        // &mapping-type=metadata&mapping.type=metadata&pid=35523&qos.port=33333&registry-type=service&timestamp=1617693636877
        return new ServiceDiscoveryRegistry(url);
    }

}
