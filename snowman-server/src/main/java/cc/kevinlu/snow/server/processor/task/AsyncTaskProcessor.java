package cc.kevinlu.snow.server.processor.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import cc.kevinlu.snow.server.data.mapper.DigitMapper;
import cc.kevinlu.snow.server.data.mapper.SnowflakeMapper;
import cc.kevinlu.snow.server.data.mapper.UuidMapper;
import cc.kevinlu.snow.server.data.model.*;
import cc.kevinlu.snow.server.pojo.enums.StatusEnums;

/**
 * @author chuan
 */
@Component
@Async
public class AsyncTaskProcessor {

    @Autowired
    private UuidMapper             uuidMapper;
    @Autowired
    private DigitMapper            digitMapper;
    @Autowired
    private SnowflakeMapper        snowflakeMapper;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    public void uuidStatus(List records) {
        taskExecutor.execute(() -> {
            UuidDO uuidDO = new UuidDO();
            uuidDO.setStatus(StatusEnums.USED.getStatus());

            UuidDOExample example = new UuidDOExample();
            example.createCriteria().andIdIn(records);
            uuidMapper.updateByExampleSelective(uuidDO, example);
        });
    }

    public void snowflakeStatus(List records) {
        taskExecutor.execute(() -> {
            SnowflakeDO snowflakeDO = new SnowflakeDO();
            snowflakeDO.setStatus(StatusEnums.USED.getStatus());

            SnowflakeDOExample example = new SnowflakeDOExample();
            example.createCriteria().andIdIn(records);
            snowflakeMapper.updateByExampleSelective(snowflakeDO, example);
        });
    }

    public void digitStatus(Long id) {
        taskExecutor.execute(() -> {
            DigitDO digitDO = new DigitDO();
            digitDO.setStatus(StatusEnums.USED.getStatus());
            digitDO.setId(id);

            digitMapper.updateByPrimaryKeySelective(digitDO);
        });
    }

}
