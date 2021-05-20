package cc.kevinlu.snow.server.processor.task;

import org.apache.commons.lang3.StringUtils;

import cc.kevinlu.snow.server.config.Constants;
import cc.kevinlu.snow.server.listener.pojo.PreGenerateBO;
import cc.kevinlu.snow.server.processor.redis.RedisProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan
 */
@Slf4j
public class RedisMessageSender implements Runnable {

    private final static ThreadLocal<PreGenerateBO> THREAD_LOCAL = new ThreadLocal<>();
    private PreGenerateBO                           preGenerateBO;

    private RedisProcessor                          redisProcessor;

    public RedisMessageSender(RedisProcessor redisProcessor, PreGenerateBO preGenerateBO) {
        this.redisProcessor = redisProcessor;
        THREAD_LOCAL.set(preGenerateBO);
        this.preGenerateBO = preGenerateBO;
    }

    /**
     * the max times for regenerate message
     */
    private static final int REGENERATE_TIMES_LIMIT = 3;

    @Override
    public void run() {
        //        PreGenerateBO preGenerateBO = THREAD_LOCAL.get();
        if (StringUtils.isAnyBlank(preGenerateBO.getGroup(), preGenerateBO.getInstance())) {
            log.warn("Reject Illegal Call!");
            return;
        }
        if (preGenerateBO.getTimes() > REGENERATE_TIMES_LIMIT) {
            log.warn("Times overflow!");
            return;
        }
        redisProcessor.sendMessage(Constants.CHECK_CHUNK_TOPIC, preGenerateBO);
        //        THREAD_LOCAL.remove();
    }
}
