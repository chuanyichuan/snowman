package cc.kevinlu.snow.server.generate.worker;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan
 */
@Slf4j
public class RandomWorker {

    public static String getRandomUUID() {
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        char first = "abcdef".charAt((int) (Math.random() * 6));
        return first + uuid.substring(1);
    }

}
