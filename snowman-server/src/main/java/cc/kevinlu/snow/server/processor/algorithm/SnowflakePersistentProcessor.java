package cc.kevinlu.snow.server.processor.algorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.kevinlu.snow.server.config.Constants;
import cc.kevinlu.snow.server.data.mapper.BatchMapper;
import cc.kevinlu.snow.server.data.model.SnowflakeDO;
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
public class SnowflakePersistentProcessor implements PersistentProcessor<Long> {

    @Autowired
    private BatchMapper    batchMapper;
    @Autowired
    private RedisProcessor redisProcessor;

    @Override
    public void asyncToCache(AsyncCacheBO asyncCacheBO) {
        List<Long> recordList = batchMapper.selectIdFromSnowflake(asyncCacheBO.getInstanceId(),
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
    public void syncToDb(PersistentBO<Long> persistent) {
        long instanceId = persistent.getInstanceId();
        List<Long> idList = persistent.getIdList();
        int status = persistent.getUsed() ? 1 : 0;
        int chunk = idList.size();
        Date date = new Date();
        SnowflakeDO snowflake;
        List<SnowflakeDO> records = new ArrayList<>();
        int index = 1;
        for (Long id : idList) {
            snowflake = new SnowflakeDO();
            snowflake.setChunk(chunk);
            snowflake.setServiceInstanceId(instanceId);
            snowflake.setGValue(id);
            snowflake.setStatus(status);
            snowflake.setGmtCreated(date);
            records.add(snowflake);
            if (index++ % Constants.BATCH_INSERT_SIZE == 0) {
                batchMapper.insertSnowflake(records);
                records.clear();
            }
        }
        if (!CollectionUtils.isEmpty(records)) {
            batchMapper.insertSnowflake(records);
        }
    }
}
