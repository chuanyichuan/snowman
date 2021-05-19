package cc.kevinlu.snow.server.config;

/**
 * @author chuan
 */
public final class Constants {

    public static final Long   DEFAULT_TIMEOUT          = 3000L;

    public static final int    BATCH_INSERT_SIZE        = 500;

    public static final String CHECK_CHUNK_TOPIC        = "check_chunk_queue";

    public static final String CHECK_CHUNK_LOCK_PATTERN = "check_chunk_lock_%s";
}
