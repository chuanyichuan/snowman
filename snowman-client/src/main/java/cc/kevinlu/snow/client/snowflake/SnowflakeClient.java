package cc.kevinlu.snow.client.snowflake;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * the client for generate snowflake id<br/>
 * <p>It only has one function called generate, client can use it to get some records.</p>
 * <p>when the client calls it, they should provide the code of the group they belong to.</p>
 * <p>For Example:
 * <br>
 * <pre>
 *    {@code @Autowired}
 *     private SnowflakeClient client;
 *     
 *     public void method(String groupCode) {
 *         List<Long> idList = client.generate(groupCode);
 *     }
 * </pre>
 * </p>
 * 
 * @author chuan
 */
public interface SnowflakeClient {

    /**
     * generate function
     * 
     * @param groupCode
     * @return
     */
    @GetMapping(value = "/generate/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<Long> generate(@PathVariable(name = "code") String groupCode);

}
