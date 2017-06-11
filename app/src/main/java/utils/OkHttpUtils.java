package utils;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/6/9.
 * 获取OkHttpClient单例
 */

public class OkHttpUtils {
    private static OkHttpClient okHttpClient;
    //传入缓存文件目录和缓存文件大小
    public static OkHttpClient getInstance(File cacheDirectory,long CacheSize){
        Cache cache=new Cache(cacheDirectory,CacheSize);
        okHttpClient=new OkHttpClient.Builder().cache(cache).build();
        return okHttpClient;

    }

}
