package me.zhixingye.im.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import me.zhixingye.im.service.BasicService;

/**
 * Created by zhixingye on 2020年04月28日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ServiceAccessor {

    private static final Map<Class<? extends BasicService>, BasicService> SERVICE_MAP = Collections.synchronizedMap(new HashMap<>());

    @SuppressWarnings("unchecked")
    public static <T extends BasicService> T get(Class<T> c) {
        return (T) SERVICE_MAP.get(c);
    }

    public static <T extends BasicService> void register(Class<T> c, T serviceImpl) {
        SERVICE_MAP.put(c, serviceImpl);
    }
}
