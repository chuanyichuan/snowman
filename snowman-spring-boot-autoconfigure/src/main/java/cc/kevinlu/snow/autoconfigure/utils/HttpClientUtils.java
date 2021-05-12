package cc.kevinlu.snow.autoconfigure.utils;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

/**
 * @author chuan
 */
public class HttpClientUtils {
    protected final Log            log = LogFactory.getLog(HttpClientUtils.class);

    private static HttpClientUtils instance;
    protected Charset              charset;

    private HttpClientUtils() {
    }

    public static HttpClientUtils getInstance() {
        return getInstance(Charset.defaultCharset());
    }

    /**
     * 
     * @param charset
     *          character charset
     * @return HttpClientUtils instance
     */
    public static HttpClientUtils getInstance(Charset charset) {
        if (instance == null) {
            instance = new HttpClientUtils();
        }
        instance.setCharset(charset);
        return instance;
    }

    /**
     * 
     * @param charset
     *          set character charset
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    /**
     * post请求
     * @param url
     *          url
     * @return result
     * @throws Exception exception
     */
    public String doPost(String url) throws Exception {
        return doPost(url, null, null);
    }

    /**
     * 
     * @param url url
     * @param params params
     * @return result
     * @throws Exception connect exception
     */
    public String doPost(String url, Map<String, Object> params) throws Exception {
        return doPost(url, params, null);
    }

    /**
     * 
     * @param url url
     * @param params params
     * @param header header
     * @return result
     * @throws Exception connect exception
     */
    public String doPost(String url, Map<String, Object> params, Map<String, String> header) throws Exception {
        String body = null;
        try {
            // Post请求
            log.debug(" protocol: POST");
            log.debug("      url: " + url);
            HttpPost httpPost = new HttpPost(url.trim());
            // 设置参数
            log.debug("   params: " + JSON.toJSONString(params));
            httpPost.setEntity(new UrlEncodedFormEntity(map2NameValuePairList(params), charset));
            // 设置Header
            if (header != null && !header.isEmpty()) {
                log.debug("   header: " + JSON.toJSONString(header));
                for (Iterator<Entry<String, String>> it = header.entrySet().iterator(); it.hasNext();) {
                    Entry<String, String> entry = (Entry<String, String>) it.next();
                    httpPost.setHeader(new BasicHeader(entry.getKey(), entry.getValue()));
                }
            }
            // 发送请求,获取返回数据
            body = execute(httpPost);
        } catch (Exception e) {
            throw e;
        }
        log.debug("   result: " + body);
        return body;
    }

    /**
     * postJson请求
     * 
     * @param url url
     * @param params params
     * @return result
     * @throws Exception connect exception
     */
    public String doPostJson(String url, Map<String, Object> params) throws Exception {
        return doPostJson(url, params, null);
    }

    /**
     *
     * @param url url
     * @param params params
     * @param header header
     * @return result
     * @throws Exception connect exception
     */
    public String doPostJson(String url, Map<String, Object> params, Map<String, String> header) throws Exception {
        String json = null;
        if (params != null && !params.isEmpty()) {
            for (Iterator<Entry<String, Object>> it = params.entrySet().iterator(); it.hasNext();) {
                Entry<String, Object> entry = (Entry<String, Object>) it.next();
                Object object = entry.getValue();
                if (object == null) {
                    it.remove();
                }
            }
            json = JSON.toJSONString(params);
        }
        return postJson(url, json, header);
    }

    /**
     * 
     * @param url url
     * @param json params json
     * @return result
     * @throws Exception connect exception
     */
    public String doPostJson(String url, String json) throws Exception {
        return doPostJson(url, json, null);
    }

    /**
     *
     * @param url url
     * @param json params json
     * @param header header
     * @return result
     * @throws Exception connect exception
     */
    public String doPostJson(String url, String json, Map<String, String> header) throws Exception {
        return postJson(url, json, header);
    }

    /**
     *
     * @param url url
     * @param json params json
     * @param header header
     * @return result
     * @throws Exception connect exception
     */
    private String postJson(String url, String json, Map<String, String> header) throws Exception {
        String body = null;
        try {
            // Post请求
            log.debug(" protocol: POST");
            log.debug("      url: " + url);
            HttpPost httpPost = new HttpPost(url.trim());
            // 设置参数
            log.debug("   params: " + json);
            httpPost.setEntity(new StringEntity(json, ContentType.DEFAULT_TEXT.withCharset(charset)));
            httpPost.setHeader(new BasicHeader("Content-Type", "application/json"));
            log.debug("     type: JSON");
            // 设置Header
            if (header != null && !header.isEmpty()) {
                log.debug("   header: " + JSON.toJSONString(header));
                for (Iterator<Entry<String, String>> it = header.entrySet().iterator(); it.hasNext();) {
                    Entry<String, String> entry = (Entry<String, String>) it.next();
                    httpPost.setHeader(new BasicHeader(entry.getKey(), entry.getValue()));
                }
            }
            // 发送请求,获取返回数据
            body = execute(httpPost);
        } catch (Exception e) {
            throw e;
        }
        log.debug("  result: " + body);
        return body;
    }

    /**
     * get请求
     * 
     * @param url url
     * @return result
     * @throws Exception connect exception
     */
    public String doGet(String url) throws Exception {
        return doGet(url, null, null);
    }

    /**
     * 
     * @param url url
     * @param header header
     * @return result
     * @throws Exception connect exception
     */
    public String doGet(String url, Map<String, String> header) throws Exception {
        return doGet(url, null, header);
    }

    /**
     *
     * @param url url
     * @param params params
     * @param header header
     * @return result
     * @throws Exception connect exception
     */
    public String doGet(String url, Map<String, Object> params, Map<String, String> header) throws Exception {
        String body = null;
        try {
            // Get请求
            log.debug("protocol: GET");
            HttpGet httpGet = new HttpGet(url.trim());
            // 设置参数
            if (params != null && !params.isEmpty()) {
                String str = EntityUtils.toString(new UrlEncodedFormEntity(map2NameValuePairList(params), charset));
                String uri = httpGet.getURI().toString();
                if (uri.indexOf("?") >= 0) {
                    httpGet.setURI(new URI(httpGet.getURI().toString() + "&" + str));
                } else {
                    httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + str));
                }
            }
            log.debug("     url: " + httpGet.getURI());
            // 设置Header
            if (header != null && !header.isEmpty()) {
                log.debug("   header: " + header);
                for (Iterator<Entry<String, String>> it = header.entrySet().iterator(); it.hasNext();) {
                    Entry<String, String> entry = (Entry<String, String>) it.next();
                    httpGet.setHeader(new BasicHeader(entry.getKey(), entry.getValue()));
                }
            }
            // 发送请求,获取返回数据
            body = execute(httpGet);
        } catch (Exception e) {
            throw e;
        }
        log.debug("  result: " + body);
        return body;
    }

    /**
     * 
     * @param requestBase HttpRequestBase
     * @return result
     * @throws Exception execute exception
     */
    private String execute(HttpRequestBase requestBase) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String body = null;
        try {
            CloseableHttpResponse response = httpclient.execute(requestBase);
            try {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    body = EntityUtils.toString(entity, charset.toString());
                }
                EntityUtils.consume(entity);
            } catch (Exception e) {
                throw e;
            } finally {
                response.close();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            httpclient.close();
        }
        return body;
    }

    /**
     * 
     * @param params params
     * @return result
     */
    private List<NameValuePair> map2NameValuePairList(Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator<String> it = params.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                if (params.get(key) != null) {
                    String value = String.valueOf(params.get(key));
                    list.add(new BasicNameValuePair(key, value));
                }
            }
            return list;
        }
        return null;
    }

}
