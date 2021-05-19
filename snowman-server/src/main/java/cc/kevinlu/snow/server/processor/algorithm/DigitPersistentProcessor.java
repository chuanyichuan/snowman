package cc.kevinlu.snow.server.processor.algorithm;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.kevinlu.snow.server.config.Constants;
import cc.kevinlu.snow.server.data.mapper.BatchMapper;
import cc.kevinlu.snow.server.data.mapper.DigitMapper;
import cc.kevinlu.snow.server.data.model.DigitDO;
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
public class DigitPersistentProcessor implements PersistentProcessor<Long> {

    @Autowired
    private DigitMapper    digitMapper;
    @Autowired
    private BatchMapper    batchMapper;
    @Autowired
    private RedisProcessor redisProcessor;

    @Override
    public void asyncToCache(AsyncCacheBO asyncCacheBO) {
        List<Long> recordList = batchMapper.selectIdFromDigit(asyncCacheBO.getInstanceId(),
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
        long from = idList.get(0);
        long to = idList.get(idList.size() - 1);
        DigitDO digit = new DigitDO();
        digit.setChunk(idList.size());
        digit.setFromValue(from);
        digit.setToValue(to);
        digit.setServiceInstanceId(instanceId);
        digit.setStatus(status);
        digit.setGmtCreated(new Date());
        digitMapper.insertSelective(digit);
    }
}
