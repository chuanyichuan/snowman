package cc.kevinlu.snow.server.processor.algorithm;

import cc.kevinlu.snow.server.pojo.PersistentBO;
import cc.kevinlu.snow.server.processor.pojo.AsyncCacheBO;

/**
 * @author chuan
 */
public interface PersistentProcessor<T> {

    void asyncToCache(AsyncCacheBO asyncCacheBO);

    void syncToDb(PersistentBO<T> persistent);
}
