package me.zhixingye.im.service;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public interface StorageService {
    boolean putToConfigurationPreferences(String key, String value);

    String getFromConfigurationPreferences(String key);

    boolean putToUserPreferences(String key, String value);

    String getFromUserPreferences(String key);
}
