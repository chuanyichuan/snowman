package cc.kevinlu.snow.server.processor.algorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.kevinlu.snow.server.config.Constants;
import cc.kevinlu.snow.server.data.mapper.BatchMapper;
import cc.kevinlu.snow.server.data.model.UuidDO;
import cc.kevinlu.snow.server.pojo.PersistentBO;
import cc.kevinlu.snow.server.pojo.enums.StatusEnums;
import cc.kevinlu.snow.server.processor.pojo.AsyncCacheBO;
import cc.kevinlu.snow.server.processor.redis.RedisProcessor;
import cc.kevinlu.snow.server.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * persistent data to db or redis
 * 
 * @author chuan
 */
@Slf4j
@Component
public class UuidPersistentProcessor implements PersistentProcessor<String> {

    @Autowired
    private BatchMapper    batchMapper;
    @Autowired
    private RedisProcessor redisProcessor;

    @Override
    public void asyncToCache(AsyncCacheBO asyncCacheBO) {
        List<Long> recordList = batchMapper.selectIdFromUuid(asyncCacheBO.getInstanceId(),
                StatusEnums.USABLE.getStatus());
        if (CollectionUtils.isEmpty(recordList)) {
            log.debug("async to cache empty!");
            return;
        }
        String key = String.format(Constants.CACHE_ID_PATTERN, asyncCacheBO.getGroupId(), asyncCacheBO.getInstanceId(),
                asyncCacheBO.getMode());
        redisProcessor.del(key);
        redisProcessor.lSet(key, recordList);
    }

    @Override
    public void syncToDb(PersistentBO<String> persistent) {
        long instanceId = persistent.getInstanceId();
        List<String> idList = persistent.getIdList();
        int status = persistent.getUsed() ? 1 : 0;
        int chunk = idList.size();
        Date date = new Date();
        List<UuidDO> records = new ArrayList<>();
        int index = 1;
        for (String id : idList) {
            UuidDO uuid = new UuidDO();
            uuid.setChunk(chunk);
            uuid.setServiceInstanceId(instanceId);
            uuid.setGValue(id);
            uuid.setStatus(status);
            uuid.setGmtCreated(date);
            records.add(uuid);
            if (index++ % Constants.BATCH_INSERT_SIZE == 0) {
                batchMapper.insertUuid(records);
                records.clear();
            }
        }
        if (!CollectionUtils.isEmpty(records)) {
            batchMapper.insertUuid(records);
        }
    }

}
