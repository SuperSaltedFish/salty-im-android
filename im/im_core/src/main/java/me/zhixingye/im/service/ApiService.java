package me.zhixingye.im.service;

import me.zhixingye.im.api.BasicApi;

/**
 * Created by zhixingye on 2020年04月28日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public interface ApiService extends BasicService {
    <T extends BasicApi> T createApi(Class<T> c);
}
