package cc.kevinlu.snow.server.processor.algorithm;

import java.util.List;

import cc.kevinlu.snow.server.pojo.PersistentBO;
import cc.kevinlu.snow.server.processor.pojo.AsyncCacheBO;
import cc.kevinlu.snow.server.processor.pojo.RecordAcquireBO;

/**
 * @author chuan
 */
public interface PersistentProcessor<T> {

    void asyncToCache(AsyncCacheBO asyncCacheBO);

    void syncToDb(PersistentBO<T> persistent);

    List<T> getRecords(RecordAcquireBO acquireBO);
}
