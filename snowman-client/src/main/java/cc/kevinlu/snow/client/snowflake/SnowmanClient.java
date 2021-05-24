package cc.kevinlu.snow.client.snowflake;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>The client that generates the snowflake ID</p>
 * <p>It only has one function called generate, client can use it to get some records.</p>
 * <p>When the client calls it, they should provide the code of the group they belong to.</p>
 * <p>For Example:</p>
 * <pre>
 *    {@code @Autowired}
 *     private SnowmanClient client;
 *     
 *     public void method(String groupCode, String instanceCode) {
 *         List&lt;Long&gt; idList = client.generate(groupCode, instanceCode);
 *     }
 * </pre>
 * 
 * @author chuan
 */
@FeignClient(name = "snowman", contextId = "snowman-client")
public interface SnowmanClient {

    /**
     * generate function
     * 
     * @param groupCode code of group
     * @param instanceCode code of instance
     * @return The list is populated by ID
     */
    @GetMapping(value = "/generate/{code}/{instance}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<Object> generate(@PathVariable(name = "code") String groupCode,
                          @PathVariable(name = "instance") String instanceCode);

}
