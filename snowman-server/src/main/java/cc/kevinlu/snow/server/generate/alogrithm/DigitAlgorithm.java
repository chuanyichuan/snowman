package cc.kevinlu.snow.server.generate.alogrithm;

import cc.kevinlu.snow.server.data.mapper.DigitMapper;
import cc.kevinlu.snow.server.data.model.DigitDO;
import cc.kevinlu.snow.server.generate.AbstractAlgorithm;
import cc.kevinlu.snow.server.processor.AlgorithmProcessor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chuan
 */
@Slf4j
public class DigitAlgorithm extends AbstractAlgorithm<Long> {

    private final DigitMapper digitMapper;

    public DigitAlgorithm(AlgorithmProcessor algorithmProcessor, DigitMapper digitMapper) {
        super(algorithmProcessor);
        this.digitMapper=digitMapper;
    }

    @Override
    protected List<Long> generateDistributedId(long groupId, long instanceId, long fromValue, int chunk) {

        List<Long> idList = new ArrayList<>(chunk);
        for (long i = fromValue; i < fromValue + chunk; i++) {
            idList.add(i);
        }
        return idList;
    }
    @Override
    public void persistentDB(long instanceId, List<Long> idList) {
        long from = idList.get(0);
        long to = idList.get(idList.size() - 1);
        DigitDO digit = new DigitDO();
        digit.setChunk(idList.size());
        digit.setFromValue(from);
        digit.setToValue(to);
        digit.setServiceInstanceId(instanceId);
        digit.setGmtCreated(new Date());
        digitMapper.insertSelective(digit);
    }

}
