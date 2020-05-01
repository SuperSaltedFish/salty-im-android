package me.zhixingye.im.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import me.zhixingye.im.service.BasicService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ServiceAccessor {

    private static final Map<Class<? extends BasicService>, BasicService> SERVICE_MAP =
            Collections.synchronizedMap(new HashMap<Class<? extends BasicService>, BasicService>());

    @SuppressWarnings("unchecked")
    public static <T extends BasicService> T get(Class<T> c) {
        return (T) SERVICE_MAP.get(c);
    }

    public static <T extends BasicService> void register(Class<T> c, T serviceImpl) {
        SERVICE_MAP.put(c, serviceImpl);
    }
}
