package cc.kevinlu.snow.server.generate.alogrithm;

import cc.kevinlu.snow.server.data.mapper.UuidMapper;
import cc.kevinlu.snow.server.data.model.UuidDO;
import cc.kevinlu.snow.server.generate.AbstractAlgorithm;
import cc.kevinlu.snow.server.processor.AlgorithmProcessor;
import com.alibaba.nacos.common.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chuan
 */
@Slf4j
public class UuidAlgorithm extends AbstractAlgorithm<String> {
    private final UuidMapper uuidMapper;


    public UuidAlgorithm(AlgorithmProcessor algorithmProcessor, UuidMapper uuidMapper) {
        super(algorithmProcessor);
        this.uuidMapper = uuidMapper;
    }

    @Override
    protected List<String> generateDistributedId(long groupId, long instanceId, long fromValue,
                                                 int chunk) {
        List<String> idList = new ArrayList<>(chunk);
        for (int i = 0; i < chunk; i++) {
            idList.add(UuidUtils.generateUuid().replaceAll("-", ""));
        }
        return idList;
    }

    @Override
    public void persistentDB(long instanceId, List<String> idList) {
        int chunk = idList.size();
        Date date = new Date();
        for (String id : idList) {
            UuidDO uuid = new UuidDO();
            uuid.setChunk(chunk);
            uuid.setServiceInstanceId(instanceId);
            uuid.setGValue(id);
            uuid.setGmtCreated(date);
            uuidMapper.insertSelective(uuid);
        }
    }

}
