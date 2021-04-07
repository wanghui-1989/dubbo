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
package org.apache.dubbo.remoting.zookeeper;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;
import org.apache.dubbo.remoting.Constants;

/**
 * package org.apache.dubbo.remoting.zookeeper;
 *
 * import org.apache.dubbo.common.URL;
 * import org.apache.dubbo.common.extension.ExtensionLoader;
 * import org.apache.dubbo.remoting.zookeeper.ZookeeperClient;
 * import org.apache.dubbo.remoting.zookeeper.ZookeeperTransporter;
 *
 * public class ZookeeperTransporter$Adaptive
 * implements ZookeeperTransporter {
 *     public ZookeeperClient connect(URL uRL) {
 *         if (uRL == null) {
 *             throw new IllegalArgumentException("url == null");
 *         }
 *         URL uRL2 = uRL;
 *         String string = uRL2.getParameter("client", uRL2.getParameter("transporter", "curator"));
 *         if (string == null) {
 *             throw new IllegalStateException(new StringBuffer().append("Failed to get extension (org.apache.dubbo.remoting.zookeeper.ZookeeperTransporter) name from url (").append(uRL2.toString()).append(") use keys([client, transporter])").toString());
 *         }
 *         ZookeeperTransporter zookeeperTransporter = (ZookeeperTransporter)ExtensionLoader.getExtensionLoader(ZookeeperTransporter.class).getExtension(string);
 *         return zookeeperTransporter.connect(uRL);
 *     }
 * }
 *
 */
@SPI("curator")
public interface ZookeeperTransporter {

    @Adaptive({Constants.CLIENT_KEY, Constants.TRANSPORTER_KEY})
    ZookeeperClient connect(URL url);

}
