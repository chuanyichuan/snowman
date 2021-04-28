package cc.kevinlu.snow.server.generate.alogrithm;

import cc.kevinlu.snow.server.data.mapper.SnowflakeMapper;
import cc.kevinlu.snow.server.data.model.SnowflakeDO;
import cc.kevinlu.snow.server.generate.AbstractAlgorithm;
import cc.kevinlu.snow.server.processor.AlgorithmProcessor;
import cc.kevinlu.snow.server.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chuan
 */
@Slf4j
public class SnowflakeAlgorithm extends AbstractAlgorithm<Long> {

    private final SnowflakeMapper snowflakeMapper;

    public SnowflakeAlgorithm(AlgorithmProcessor algorithmProcessor, SnowflakeMapper snowflakeMapper) {
        super(algorithmProcessor);
        this.snowflakeMapper = snowflakeMapper;
    }

    @Override
    protected List<Long> generateDistributedId(long groupId, long instanceId, long fromValue, int chunk) {
        if (chunk <= 0) {
            throw new IndexOutOfBoundsException("size should not equals zero!");
        }
        List<Long> idList = new ArrayList<>(chunk);
        SnowflakeIdWorker worker = new SnowflakeIdWorker(groupId, instanceId);
        for (int i = 0; i < chunk; i++) {
            idList.add(worker.nextId());
        }
        return idList;
    }

    @Override
    public void persistentDB(long instanceId, List<Long> idList) {
        int chunk = idList.size();
        Date date = new Date();
        SnowflakeDO snowflake;
        for (Long id : idList) {
            snowflake = new SnowflakeDO();
            snowflake.setChunk(chunk);
            snowflake.setServiceInstanceId(instanceId);
            snowflake.setGValue(id);
            snowflake.setGmtCreated(date);
            snowflakeMapper.insertSelective(snowflake);
        }
    }
}
