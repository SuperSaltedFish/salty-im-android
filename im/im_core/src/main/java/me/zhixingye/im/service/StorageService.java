package me.zhixingye.im.service;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public interface StorageService extends BasicService {
    boolean putToConfigurationPreferences(String key, String value);

    String getFromConfigurationPreferences(String key);

    boolean putToUserPreferences(String key, String value);

    String getFromUserPreferences(String key);
}
