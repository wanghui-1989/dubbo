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
package org.apache.dubbo.rpc.cluster;

import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.extension.SPI;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.support.FailoverCluster;

/**
 * Cluster. (SPI, Singleton, ThreadSafe)
 * <p>
 * <a href="http://en.wikipedia.org/wiki/Computer_cluster">Cluster</a>
 * <a href="http://en.wikipedia.org/wiki/Fault-tolerant_system">Fault-Tolerant</a>
 *
 * package org.apache.dubbo.rpc.cluster;
 *
 * import org.apache.dubbo.common.URL;
 * import org.apache.dubbo.common.extension.ExtensionLoader;
 * import org.apache.dubbo.rpc.Invoker;
 * import org.apache.dubbo.rpc.RpcException;
 * import org.apache.dubbo.rpc.cluster.Cluster;
 * import org.apache.dubbo.rpc.cluster.Directory;
 *
 * public class Cluster$Adaptive
 * implements Cluster {
 *     public Invoker join(Directory directory) throws RpcException {
 *         if (directory == null) {
 *             throw new IllegalArgumentException("org.apache.dubbo.rpc.cluster.Directory argument == null");
 *         }
 *         if (directory.getUrl() == null) {
 *             throw new IllegalArgumentException("org.apache.dubbo.rpc.cluster.Directory argument getUrl() == null");
 *         }
 *         URL uRL = directory.getUrl();
 *         String string = uRL.getParameter("cluster", "failover");
 *         if (string == null) {
 *             throw new IllegalStateException(new StringBuffer().append("Failed to get extension (org.apache.dubbo.rpc.cluster.Cluster) name from url (").append(uRL.toString()).append(") use keys([cluster])").toString());
 *         }
 *         Cluster cluster = (Cluster)ExtensionLoader.getExtensionLoader(Cluster.class).getExtension(string);
 *         return cluster.join(directory);
 *     }
 *
 *     public Cluster getCluster(String string) {
 *         throw new UnsupportedOperationException("The method public static org.apache.dubbo.rpc.cluster.Cluster org.apache.dubbo.rpc.cluster.Cluster.getCluster(java.lang.String) of interface org.apache.dubbo.rpc.cluster.Cluster is not adaptive method!");
 *     }
 *
 *     public Cluster getCluster(String string, boolean bl) {
 *         throw new UnsupportedOperationException("The method public static org.apache.dubbo.rpc.cluster.Cluster org.apache.dubbo.rpc.cluster.Cluster.getCluster(java.lang.String,boolean) of interface org.apache.dubbo.rpc.cluster.Cluster is not adaptive method!");
 *     }
 * }
 *
 */
@SPI(Cluster.DEFAULT)
public interface Cluster {
    String DEFAULT = FailoverCluster.NAME;

    /**
     * Merge the directory invokers to a virtual invoker.
     *
     * @param <T>
     * @param directory
     * @return cluster invoker
     * @throws RpcException
     */
    @Adaptive
    <T> Invoker<T> join(Directory<T> directory) throws RpcException;

    static Cluster getCluster(String name) {
        return getCluster(name, true);
    }

    static Cluster getCluster(String name, boolean wrap) {
        if (StringUtils.isEmpty(name)) {
            name = Cluster.DEFAULT;
        }
        return ExtensionLoader.getExtensionLoader(Cluster.class).getExtension(name, wrap);
    }
}