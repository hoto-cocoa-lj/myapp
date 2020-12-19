package com.slq.myapp.api;

import android.util.Log;
import android.view.textclassifier.TextLinks;
import android.widget.Toast;

import com.slq.myapp.api.ApiConfig;
import com.slq.myapp.util.MyappStringUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {
    public final static String CONTENTTYPE="contentType";
    public final static String charsetPostString="application/json;charset=utf-8";
    public static void postRequest(String url, Map map,Callback cb){
        OkHttpClient client=new OkHttpClient.Builder().build();
        String jsonStr = new JSONObject(map).toString();
        RequestBody requestBody = RequestBody.create(MediaType.parse(charsetPostString), jsonStr);
        Request request = new Request.Builder().url(ApiConfig.BASE_URL + url)
                .addHeader(CONTENTTYPE, charsetPostString).post(requestBody).build();
        Call call=client.newCall(request);
        call.enqueue(cb);
    }
    public static void postRequest(String url,Map<String, Object> headerMap, Map map,Callback cb){
        OkHttpClient client=new OkHttpClient.Builder().build();
        String jsonStr = new JSONObject(map).toString();
        RequestBody requestBody = RequestBody.create(MediaType.parse(charsetPostString), jsonStr);
        Request.Builder builder = new Request.Builder().url(ApiConfig.BASE_URL + url)
                .addHeader(CONTENTTYPE, charsetPostString);
        for(String key:headerMap.keySet()){
            builder.addHeader(key,(String)headerMap.get(key));
        }
        Request request=builder.post(requestBody).build();
        Call call=client.newCall(request);
        call.enqueue(cb);
    }

    public static void getRequest(String url, Callback cb){
        OkHttpClient client=new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(ApiConfig.BASE_URL + url)
                .addHeader(CONTENTTYPE, charsetPostString).get().build();
        Call call=client.newCall(request);
        call.enqueue(cb);
    }
    public static void getRequest(String url,Map<String, Object> headerMap, Callback cb){
        OkHttpClient client=new OkHttpClient.Builder().build();
        Request.Builder builder = new Request.Builder().url(ApiConfig.BASE_URL + url)
                .addHeader(CONTENTTYPE, charsetPostString);
        for(String key:headerMap.keySet()){
            //builder.addHeader(key,(String)headerMap.get(key)));
        }
        Request request = builder.get().build();
        Call call=client.newCall(request);
        call.enqueue(cb);
    }

    public static String appendUrl(String url,Map<String,Object> map){
        if(map!=null && !map.isEmpty()){
            StringBuffer sb=new StringBuffer();
            for(String k:map.keySet()){
                if(MyappStringUtils.isEmpty(sb.toString())){
                    sb.append("?");
                }else{
                    sb.append("&");
                }
                sb.append(k).append("=").append(map.get(k));
            }
            url=url+sb.toString();
        }
        return url;
    }
}
