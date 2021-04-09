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
package org.apache.dubbo.common.bytecode;

import org.apache.dubbo.common.utils.ClassUtils;
import org.apache.dubbo.common.utils.ReflectUtils;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.apache.dubbo.common.constants.CommonConstants.MAX_PROXY_COUNT;

/**
 * getProxy(...)方法生成动态代理类的源码：
 * package org.apache.dubbo.common.bytecode;
 *
 * import com.alibaba.dubbo.rpc.service.EchoService;
 * import java.lang.reflect.InvocationHandler;
 * import java.lang.reflect.Method;
 * import java.util.Map;
 * import java.util.Set;
 * import java.util.SortedSet;
 * import org.apache.dubbo.common.bytecode.ClassGenerator;
 * import org.apache.dubbo.metadata.InstanceMetadataChangedListener;
 * import org.apache.dubbo.metadata.MetadataInfo;
 * import org.apache.dubbo.metadata.MetadataService;
 * import org.apache.dubbo.rpc.service.Destroyable;
 *
 * //每个proxyX类的主要区别在于，实现的接口不同，如这个类实现了MetadataService接口
 * public class proxy0 implements ClassGenerator.DC, Destroyable, EchoService, MetadataService {
 *     public static Method[] methods;
 *     private InvocationHandler handler;
 *
 *     public String version() {
 *         Object[] objectArray = new Object[]{};
 *         Object object = this.handler.invoke(this, methods[0], objectArray);
 *         return (String)object;
 *     }
 *
 *     public String serviceName() {
 *         Object[] objectArray = new Object[]{};
 *         Object object = this.handler.invoke(this, methods[1], objectArray);
 *         return (String)object;
 *     }
 *
 *     public SortedSet getSubscribedURLs() {
 *         Object[] objectArray = new Object[]{};
 *         Object object = this.handler.invoke(this, methods[2], objectArray);
 *         return (SortedSet)object;
 *     }
 *
 *     public SortedSet getExportedURLs(String string, String string2, String string3) {
 *         Object[] objectArray = new Object[]{string, string2, string3};
 *         Object object = this.handler.invoke(this, methods[3], objectArray);
 *         return (SortedSet)object;
 *     }
 *
 *     public SortedSet getExportedURLs(String string, String string2, String string3, String string4) {
 *         Object[] objectArray = new Object[]{string, string2, string3, string4};
 *         Object object = this.handler.invoke(this, methods[4], objectArray);
 *         return (SortedSet)object;
 *     }
 *
 *     public SortedSet getExportedURLs() {
 *         Object[] objectArray = new Object[]{};
 *         Object object = this.handler.invoke(this, methods[5], objectArray);
 *         return (SortedSet)object;
 *     }
 *
 *     public SortedSet getExportedURLs(String string) {
 *         Object[] objectArray = new Object[]{string};
 *         Object object = this.handler.invoke(this, methods[6], objectArray);
 *         return (SortedSet)object;
 *     }
 *
 *     public SortedSet getExportedURLs(String string, String string2) {
 *         Object[] objectArray = new Object[]{string, string2};
 *         Object object = this.handler.invoke(this, methods[7], objectArray);
 *         return (SortedSet)object;
 *     }
 *
 *     public Set getExportedServiceURLs() {
 *         Object[] objectArray = new Object[]{};
 *         Object object = this.handler.invoke(this, methods[8], objectArray);
 *         return (Set)object;
 *     }
 *
 *     public String getServiceDefinition(String string) {
 *         Object[] objectArray = new Object[]{string};
 *         Object object = this.handler.invoke(this, methods[9], objectArray);
 *         return (String)object;
 *     }
 *
 *     public String getServiceDefinition(String string, String string2, String string3) {
 *         Object[] objectArray = new Object[]{string, string2, string3};
 *         Object object = this.handler.invoke(this, methods[10], objectArray);
 *         return (String)object;
 *     }
 *
 *     public MetadataInfo getMetadataInfo(String string) {
 *         Object[] objectArray = new Object[]{string};
 *         Object object = this.handler.invoke(this, methods[11], objectArray);
 *         return (MetadataInfo)object;
 *     }
 *
 *     public Map getMetadataInfos() {
 *         Object[] objectArray = new Object[]{};
 *         Object object = this.handler.invoke(this, methods[12], objectArray);
 *         return (Map)object;
 *     }
 *
 *     public void exportInstanceMetadata(String string) {
 *         Object[] objectArray = new Object[]{string};
 *         Object object = this.handler.invoke(this, methods[13], objectArray);
 *     }
 *
 *     public Map getInstanceMetadataChangedListenerMap() {
 *         Object[] objectArray = new Object[]{};
 *         Object object = this.handler.invoke(this, methods[14], objectArray);
 *         return (Map)object;
 *     }
 *
 *     public String getAndListenInstanceMetadata(String string, InstanceMetadataChangedListener instanceMetadataChangedListener) {
 *         Object[] objectArray = new Object[]{string, instanceMetadataChangedListener};
 *         Object object = this.handler.invoke(this, methods[15], objectArray);
 *         return (String)object;
 *     }
 *
 *     public Object $echo(Object object) {
 *         Object[] objectArray = new Object[]{object};
 *         Object object2 = this.handler.invoke(this, methods[16], objectArray);
 *         return object2;
 *     }
 *
 *     public void $destroy() {
 *         Object[] objectArray = new Object[]{};
 *         Object object = this.handler.invoke(this, methods[17], objectArray);
 *     }
 *
 *     public proxy0() {
 *     }
 *
 *     public proxy0(InvocationHandler invocationHandler) {
 *         this.handler = invocationHandler;
 *     }
 * }
 *
 *
 *
 *
 * //这个类实现了DemoService接口
 * package org.apache.dubbo.common.bytecode;
 *
 * import com.alibaba.dubbo.rpc.service.EchoService;
 * import java.lang.reflect.InvocationHandler;
 * import java.lang.reflect.Method;
 * import java.util.concurrent.CompletableFuture;
 * import org.apache.dubbo.common.bytecode.ClassGenerator;
 * import org.apache.dubbo.demo.DemoService;
 * import org.apache.dubbo.rpc.service.Destroyable;
 *
 * public class proxy1 implements ClassGenerator.DC, Destroyable, EchoService, DemoService {
 *     public static Method[] methods;
 *     private InvocationHandler handler;
 *
 *     public String sayHello(String string) {
 *         Object[] objectArray = new Object[] { string };
 *         Object object = this.handler.invoke(this, methods[0], objectArray);
 *         return (String) object;
 *     }
 *
 *     public CompletableFuture sayHelloAsync(String string) {
 *         Object[] objectArray = new Object[] { string };
 *         Object object = this.handler.invoke(this, methods[1], objectArray);
 *         return (CompletableFuture) object;
 *     }
 *
 *     public Object $echo(Object object) {
 *         Object[] objectArray = new Object[] { object };
 *         Object object2 = this.handler.invoke(this, methods[2], objectArray);
 *         return object2;
 *     }
 *
 *     public void $destroy() {
 *         Object[] objectArray = new Object[] {};
 *         Object object = this.handler.invoke(this, methods[3], objectArray);
 *     }
 *
 *     public proxy1() {
 *     }
 *
 *     public proxy1(InvocationHandler invocationHandler) {
 *         this.handler = invocationHandler;
 *     }
 * }
 */
public abstract class Proxy {
    public static final InvocationHandler RETURN_NULL_INVOKER = (proxy, method, args) -> null;
    public static final InvocationHandler THROW_UNSUPPORTED_INVOKER = new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            throw new UnsupportedOperationException("Method [" + ReflectUtils.getName(method) + "] unimplemented.");
        }
    };
    private static final AtomicLong PROXY_CLASS_COUNTER = new AtomicLong(0);
    private static final String PACKAGE_NAME = Proxy.class.getPackage().getName();
    private static final Map<ClassLoader, Map<String, Object>> PROXY_CACHE_MAP = new WeakHashMap<ClassLoader, Map<String, Object>>();

    private static final Object PENDING_GENERATION_MARKER = new Object();

    protected Proxy() {
    }

    /**
     * Get proxy.
     *
     * @param ics interface class array.
     * @return Proxy instance.
     */
    public static Proxy getProxy(Class<?>... ics) {
        return getProxy(ClassUtils.getClassLoader(Proxy.class), ics);
    }

    /**
     * Get proxy.
     *
     * @param cl  class loader.
     * @param ics interface class array.
     * @return Proxy instance.
     */
    public static Proxy getProxy(ClassLoader cl, Class<?>... ics) {
        if (ics.length > MAX_PROXY_COUNT) {
            throw new IllegalArgumentException("interface limit exceeded");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ics.length; i++) {
            String itf = ics[i].getName();
            if (!ics[i].isInterface()) {
                throw new RuntimeException(itf + " is not a interface.");
            }

            Class<?> tmp = null;
            try {
                tmp = Class.forName(itf, false, cl);
            } catch (ClassNotFoundException e) {
            }

            if (tmp != ics[i]) {
                throw new IllegalArgumentException(ics[i] + " is not visible from class loader");
            }

            sb.append(itf).append(';');
        }

        // use interface class name list as key.
        String key = sb.toString();

        // get cache by class loader.
        final Map<String, Object> cache;
        synchronized (PROXY_CACHE_MAP) {
            cache = PROXY_CACHE_MAP.computeIfAbsent(cl, k -> new HashMap<>());
        }

        Proxy proxy = null;
        synchronized (cache) {
            do {
                Object value = cache.get(key);
                if (value instanceof Reference<?>) {
                    proxy = (Proxy) ((Reference<?>) value).get();
                    if (proxy != null) {
                        return proxy;
                    }
                }

                if (value == PENDING_GENERATION_MARKER) {
                    try {
                        cache.wait();
                    } catch (InterruptedException e) {
                    }
                } else {
                    cache.put(key, PENDING_GENERATION_MARKER);
                    break;
                }
            }
            while (true);
        }

        long id = PROXY_CLASS_COUNTER.getAndIncrement();
        String pkg = null;
        ClassGenerator ccp = null, ccm = null;
        try {
            ccp = ClassGenerator.newInstance(cl);

            Set<String> worked = new HashSet<>();
            List<Method> methods = new ArrayList<>();

            for (int i = 0; i < ics.length; i++) {
                if (!Modifier.isPublic(ics[i].getModifiers())) {
                    String npkg = ics[i].getPackage().getName();
                    if (pkg == null) {
                        pkg = npkg;
                    } else {
                        if (!pkg.equals(npkg)) {
                            throw new IllegalArgumentException("non-public interfaces from different packages");
                        }
                    }
                }
                ccp.addInterface(ics[i]);

                for (Method method : ics[i].getMethods()) {
                    String desc = ReflectUtils.getDesc(method);
                    if (worked.contains(desc) || Modifier.isStatic(method.getModifiers())) {
                        continue;
                    }
                    if (ics[i].isInterface() && Modifier.isStatic(method.getModifiers())) {
                        continue;
                    }
                    worked.add(desc);

                    int ix = methods.size();
                    Class<?> rt = method.getReturnType();
                    Class<?>[] pts = method.getParameterTypes();

                    StringBuilder code = new StringBuilder("Object[] args = new Object[").append(pts.length).append("];");
                    for (int j = 0; j < pts.length; j++) {
                        code.append(" args[").append(j).append("] = ($w)$").append(j + 1).append(";");
                    }
                    code.append(" Object ret = handler.invoke(this, methods[").append(ix).append("], args);");
                    if (!Void.TYPE.equals(rt)) {
                        code.append(" return ").append(asArgument(rt, "ret")).append(";");
                    }

                    methods.add(method);
                    ccp.addMethod(method.getName(), method.getModifiers(), rt, pts, method.getExceptionTypes(), code.toString());
                }
            }

            if (pkg == null) {
                pkg = PACKAGE_NAME;
            }

            // create ProxyInstance class.
            String pcn = pkg + ".proxy" + id;
            ccp.setClassName(pcn);
            ccp.addField("public static java.lang.reflect.Method[] methods;");
            ccp.addField("private " + InvocationHandler.class.getName() + " handler;");
            ccp.addConstructor(Modifier.PUBLIC, new Class<?>[]{InvocationHandler.class}, new Class<?>[0], "handler=$1;");
            ccp.addDefaultConstructor();
            Class<?> clazz = ccp.toClass();
            clazz.getField("methods").set(null, methods.toArray(new Method[0]));

            // create Proxy class.
            String fcn = Proxy.class.getName() + id;
            ccm = ClassGenerator.newInstance(cl);
            ccm.setClassName(fcn);
            ccm.addDefaultConstructor();
            ccm.setSuperClass(Proxy.class);
            ccm.addMethod("public Object newInstance(" + InvocationHandler.class.getName() + " h){ return new " + pcn + "($1); }");
            Class<?> pc = ccm.toClass();
            proxy = (Proxy) pc.newInstance();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            // release ClassGenerator
            if (ccp != null) {
                ccp.release();
            }
            if (ccm != null) {
                ccm.release();
            }
            synchronized (cache) {
                if (proxy == null) {
                    cache.remove(key);
                } else {
                    cache.put(key, new WeakReference<Proxy>(proxy));
                }
                cache.notifyAll();
            }
        }
        return proxy;
    }

    private static String asArgument(Class<?> cl, String name) {
        if (cl.isPrimitive()) {
            if (Boolean.TYPE == cl) {
                return name + "==null?false:((Boolean)" + name + ").booleanValue()";
            }
            if (Byte.TYPE == cl) {
                return name + "==null?(byte)0:((Byte)" + name + ").byteValue()";
            }
            if (Character.TYPE == cl) {
                return name + "==null?(char)0:((Character)" + name + ").charValue()";
            }
            if (Double.TYPE == cl) {
                return name + "==null?(double)0:((Double)" + name + ").doubleValue()";
            }
            if (Float.TYPE == cl) {
                return name + "==null?(float)0:((Float)" + name + ").floatValue()";
            }
            if (Integer.TYPE == cl) {
                return name + "==null?(int)0:((Integer)" + name + ").intValue()";
            }
            if (Long.TYPE == cl) {
                return name + "==null?(long)0:((Long)" + name + ").longValue()";
            }
            if (Short.TYPE == cl) {
                return name + "==null?(short)0:((Short)" + name + ").shortValue()";
            }
            throw new RuntimeException(name + " is unknown primitive type.");
        }
        return "(" + ReflectUtils.getName(cl) + ")" + name;
    }

    /**
     * get instance with default handler.
     *
     * @return instance.
     */
    public Object newInstance() {
        return newInstance(THROW_UNSUPPORTED_INVOKER);
    }

    /**
     * get instance with special handler.
     *
     * @return instance.
     */
    abstract public Object newInstance(InvocationHandler handler);
}
