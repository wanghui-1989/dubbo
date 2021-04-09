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

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invoker;

/**
 * This is the final Invoker type referenced by the RPC proxy on Consumer side.
 * <p>
 * A ClusterInvoker holds a group of normal invokers, stored in a Directory, mapping to one Registry.
 * The ClusterInvoker implementation usually provides LB or HA policies, like FailoverClusterInvoker.
 * <p>
 * In multi-registry subscription scenario, the final ClusterInvoker will referr to several sub ClusterInvokers, with each
 * sub ClusterInvoker representing one Registry. Take ZoneAwareClusterInvoker as an example, it is specially customized for
 * multi-registry use cases: first, pick up one ClusterInvoker, then do LB inside the chose ClusterInvoker.
 *
 * 被消费侧的proxy引用，是最终的invoker类型。
 * 一个ClusterInvoker持有一组普通的invoker，这些普通的invoker都是映射到同一个注册中心的，
 * 一般存储在属性字段Directory<T> directory中。这样看dubbo认为使用同一个注册中心的就是一个服务Cluster集群。
 * ClusterInvoker的实现类通常会提供负载均衡或者HA功能，像FailoverClusterInvoker。
 *
 * 在多注册中心订阅方案中，最终的ClusterInvoker将引用几个子ClusterInvoker，每个子ClusterInvoker代表一个注册表。
 * 以ZoneAwareClusterInvoker为例，它是针对多注册中心用例专门定制的：首先，选择一个ClusterInvoker，然后在所选的ClusterInvoker内进行LB。
 *
 * 从这看Invoke有两种，一是单机Invoker，一个是集群Invoker。集群Invoker是将多个单机invoker抽象成一个单机invoker而设计的，
 * 方便使用，屏蔽底层细节。
 *
 * @param <T>
 */
public interface ClusterInvoker<T> extends Invoker<T> {

    //集群使用的注册中心url
    URL getRegistryUrl();

    //注册到同一个注册中心的，一个service的所有普通invoker集合/目录。
    //如：HelloService在3台机器部署，都注册到同一个配置中心，那么这个目录就是这3个invoker。
    Directory<T> getDirectory();

    boolean isDestroyed();

    default boolean isServiceDiscovery() {
        Directory<T> directory = getDirectory();
        if (directory == null) {
            return false;
        }
        return directory.isServiceDiscovery();
    }

    default boolean hasProxyInvokers() {
        Directory<T> directory = getDirectory();
        if (directory == null) {
            return false;
        }
        return !directory.isEmpty();
    }
}
