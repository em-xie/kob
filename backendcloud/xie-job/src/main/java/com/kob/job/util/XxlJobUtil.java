package com.kob.job.util;



import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @作者：xie
 * @时间：2023/1/30 19:48
 */
public class XxlJobUtil {
    private static String cookie="";


    /**
     * 查询现有的任务（可以关注这个整个调用链，可以自己模仿着写其他的拓展接口）
     * @param url
     * @param requestInfo
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static JSONObject pageList(String url, JSONObject requestInfo) throws HttpException, IOException {
        String path = "/api/jobinfo/pageList";
        String targetUrl = url + path;
        HttpClient httpClient = new HttpClient();
        PostMethod post = new PostMethod(targetUrl);
        post.setRequestHeader("cookie", cookie);
        RequestEntity requestEntity = new StringRequestEntity(requestInfo.toString(), "application/json", "utf-8");
        post.setRequestEntity(requestEntity);
        httpClient.executeMethod(post);
        JSONObject result = new JSONObject();
        result = getJsonObject(post, result);
        System.out.println(result.toJSONString(1));
        return result;
    }



    /**
     * 新增/编辑任务
     * @param url
     * @param requestInfo
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static JSONObject addJob(String url,JSONObject requestInfo) throws HttpException, IOException {
        String path = "/api/jobinfo/save";
        String targetUrl = url + path;
        HttpClient httpClient = new HttpClient();
        PostMethod post = new PostMethod(targetUrl);
        post.setRequestHeader("cookie", cookie);
        RequestEntity requestEntity = new StringRequestEntity(requestInfo.toString(), "application/json", "utf-8");
        post.setRequestEntity(requestEntity);
        httpClient.executeMethod(post);
        JSONObject result = new JSONObject();
        result = getJsonObject(post, result);
        System.out.println(result.toJSONString());
        return result;
    }

    /**
     * 删除任务
     * @param url
     * @param id
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static JSONObject deleteJob(String url,int id) throws HttpException, IOException {
        String path = "/api/jobinfo/delete?id="+id;
        return doGet(url,path);
    }

    /**
     * 开始任务
     * @param url
     * @param id
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static JSONObject startJob(String url,int id) throws HttpException, IOException {
        String path = "/api/jobinfo/start?id="+id;
        return doGet(url,path);
    }

    /**
     * 停止任务
     * @param url
     * @param id
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static JSONObject stopJob(String url, int id) throws HttpException, IOException {
        String path = "/api/jobinfo/stop?id="+id;
        return doGet(url,path);
    }

    public static JSONObject doGet(String url,String path) throws HttpException, IOException {
        String targetUrl = url + path;
        HttpClient httpClient = new HttpClient();
        HttpMethod get = new GetMethod(targetUrl);
        get.setRequestHeader("cookie", cookie);
        httpClient.executeMethod(get);
        JSONObject result = new JSONObject();
        result = getJsonObject(get, result);
        return result;
    }

    private static JSONObject getJsonObject(HttpMethod postMethod, JSONObject result) throws IOException {
        if (postMethod.getStatusCode() == HttpStatus.SC_OK) {
            InputStream inputStream = postMethod.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String str;
            while((str = br.readLine()) != null){
                stringBuffer.append(str);
            }
            String response = new String(stringBuffer);
            br.close();

            return (JSONObject) JSONObject.parse(response);
        } else {
            return null;
        }


    }

    /**
     * 登录
     * @param url
     * @param userName
     * @param password
     * @return
     * @throws HttpException
     * @throws IOException
     */

    public static String login(String url, String userName, String password) throws HttpException, IOException {
        String path = "/api/jobinfo/login?userName="+userName+"&password="+password;
        String targetUrl = url + path;
        HttpClient httpClient = new HttpClient();
        HttpMethod get = new GetMethod((targetUrl));
        httpClient.executeMethod(get);
        if (get.getStatusCode() == 200) {
            Cookie[] cookies = httpClient.getState().getCookies();
            StringBuffer tmpcookies = new StringBuffer();
            for (Cookie c : cookies) {
                tmpcookies.append(c.toString() + ";");
            }
            cookie = tmpcookies.toString();
        } else {
            try {
                cookie = "";
            } catch (Exception e) {
                cookie="";
            }
        }
        return cookie;
    }
}
