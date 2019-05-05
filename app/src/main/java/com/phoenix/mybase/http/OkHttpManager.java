package com.phoenix.mybase.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 参考文章：https://blog.csdn.net/lmj623565791/article/details/47911083
 *
 * @author Phoenix
 */
public class OkHttpManager {
    private static OkHttpManager instance;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private Gson mGson;

    private OkHttpManager() {
        mOkHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    public static OkHttpManager getInstance() {
        if (instance == null) {
            synchronized (OkHttpManager.class) {
                if (instance == null) {
                    instance = new OkHttpManager();
                }
            }
        }
        return instance;
    }


    //*************对外公布的方法************


    public static Response getAsync(String url) throws IOException {
        return getInstance()._getAsync(url);
    }


    public static String getAsString(String url) throws IOException {
        return getInstance()._getAsString(url);
    }

    public static <T> void getAsync(String url, ResultCallback<T> callback) {
        getInstance()._getAsync(url, callback);
    }

    public static Response post(String url, Param... params) throws IOException {
        return getInstance()._post(url, params);
    }

    public static String postAsString(String url, Param... params) throws IOException {
        return getInstance()._postAsString(url, params);
    }

    public static <T> void postAsync(String url, final ResultCallback<T> callback, Param... params) {
        getInstance()._postAsync(url, callback, params);
    }

    public static Response post(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        return getInstance()._post(url, files, fileKeys, params);
    }

    public static Response post(String url, File file, String fileKey) throws IOException {
        return getInstance()._post(url, file, fileKey);
    }

    public static Response post(String url, File file, String fileKey, Param... params) throws IOException {
        return getInstance()._post(url, file, fileKey, params);
    }

    public static <T> void postAsync(String url, ResultCallback<T> callback, File[] files, String[] fileKeys, Param... params) {
        getInstance()._postAsync(url, callback, files, fileKeys, params);
    }


    public static <T> void postAsync(String url, ResultCallback<T> callback, File file, String fileKey) {
        getInstance()._postAsync(url, callback, file, fileKey);
    }

    public static <T> void postAsync(String url, ResultCallback<T> callback, File file, String fileKey, Param... params) throws IOException {
        getInstance()._postAsync(url, callback, file, fileKey, params);
    }

    /**
     * 同步的get请求
     *
     * @param url 请求的Url
     * @return 响应
     * @throws IOException 异常
     */
    private Response _getAsync(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        return call.execute();
    }

    /**
     * 同步get请求
     *
     * @param url 请求的url
     * @return 请求结果，作为String返回
     * @throws IOException 异常
     */
    private String _getAsString(String url) throws IOException {
        Response response = _getAsync(url);
        ResponseBody body = response.body();
        return body == null ? null : body.string();
    }

    /**
     * 异步get请求
     *
     * @param url      请求的url
     * @param callback 请求结果的回调
     */
    private <T> void _getAsync(String url, ResultCallback<T> callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        deliverResult(callback, request);
    }

    /**
     * 同步的post请求
     *
     * @param url    请求的url
     * @param params 请求参数的数组
     * @return 请求的结果
     * @throws IOException 异常
     */
    private Response _post(String url, Param... params) throws IOException {
        Request request = buildPostRequest(url, params);
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 同步的post请求
     *
     * @param url    请求的url
     * @param params 请求的参数
     * @return 请求到的String
     * @throws IOException 异常
     */
    private String _postAsString(String url, Param... params) throws IOException {
        Response response = _post(url, params);
        ResponseBody body = response.body();
        return body == null ? null : body.string();
    }

    /**
     * 异步的post请求
     *
     * @param url      请求的url
     * @param callback 请求的回调
     * @param params   请求的参数
     * @param <T>      返回的bean类型
     */
    private <T> void _postAsync(String url, ResultCallback<T> callback, Param... params) {
        Request request = buildPostRequest(url, params);
        deliverResult(callback, request);
    }

    /**
     * 基于POST的同步文件上传
     *
     * @param url      请求的url
     * @param files    文件
     * @param fileKeys 文件的key
     * @param params   请求参数
     * @return 请求的结果
     * @throws IOException 异常
     */
    private Response _post(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 基于POST的同步文件上传
     *
     * @param url     请求的url
     * @param file    文件
     * @param fileKey 文件的key
     * @return 请求的结果
     * @throws IOException 异常
     */
    private Response _post(String url, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 基于POST的同步文件上传
     *
     * @param url     请求的url
     * @param file    文件
     * @param fileKey 文件的key
     * @param params  请求参数
     * @return 请求的结果
     * @throws IOException 异常
     */
    private Response _post(String url, File file, String fileKey, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 异步基于post的文件上传
     *
     * @param url      请求的url
     * @param callback 接受结果的回调
     * @param files    文件
     * @param fileKeys 文件的key
     */
    private <T> void _postAsync(String url, ResultCallback<T> callback, File[] files, String[] fileKeys, Param... params) {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        deliverResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件不带参数上传
     *
     * @param url      请求的url
     * @param callback 接受结果的回调
     * @param file     文件
     * @param fileKey  文件key
     */
    private <T> void _postAsync(String url, ResultCallback<T> callback, File file, String fileKey) {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        deliverResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件且携带其他form参数上传
     *
     * @param url      请求的url
     * @param callback 接受结果的回调
     * @param file     文件
     * @param fileKey  文件key
     * @param params   请求参数
     * @throws IOException 异常
     */
    private <T> void _postAsync(String url, ResultCallback<T> callback, File file, String fileKey, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        deliverResult(callback, request);
    }

    /**
     * 异步下载
     *
     * @param url        下载的连接
     * @param desFileDir 下载的路径
     * @param callback   下载结果的回调
     */
    private <T> void _downloadAsync(String url, String desFileDir, ResultCallback<T> callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(desFileDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    int len;
                    byte[] buf = new byte[2048];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    sendSuccessResultCallback(file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(request, e, callback);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 根据路径获取文件名称
     *
     * @param path 路径
     * @return 文件名称
     */
    private String getFileName(String path) {
        int index = path.lastIndexOf("/");
        return index < 0 ? path : path.substring(index + 1, path.length());
    }

    /**
     * 创建表单
     *
     * @param url      请求的地址
     * @param files    表单文件
     * @param fileKeys 文件key
     * @param params   表单参数
     * @return 请求Request
     */
    private Request buildMultipartFormRequest(String url, File[] files, String[] fileKeys, Param[] params) {
        params = validateParam(params);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (Param p : params) {
            builder.addPart(MultipartBody.Part.createFormData(p.key, p.value));
        }
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                RequestBody body = RequestBody.create(MediaType.parse(fileName), files[i]);
                builder.addFormDataPart(fileKeys[i], files[i].getName(), body);
            }
        }

        RequestBody body = builder.build();
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }

    private Param[] validateParam(Param... params) {
        if (params == null) {
            return new Param[0];
        } else {
            return params;
        }
    }

    /**
     * 构建请求参数
     *
     * @param url    请求的url
     * @param params 请求参数的数组
     * @return 请求
     */
    private Request buildPostRequest(String url, Param... params) {
        if (params == null) {
            params = new Param[0];
        }

        FormBody.Builder builder = new FormBody.Builder();
        for (Param p : params) {
            builder.add(p.key, p.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    /**
     * 传输请求结果
     *
     * @param callback 请求结果回调
     * @param request  请求
     */
    private <T> void deliverResult(ResultCallback<T> callback, Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(request, e, callback);
                Log.d("GJH", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    ResponseBody body = response.body();
                    String s = body.string();
                    Log.d("GJH", "onResponse: " + s);
                    T t = mGson.fromJson(s, callback.mType);
                    sendSuccessResultCallback(t, callback);
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }

    /**
     * 发送失败的网络请求
     *
     * @param request  请求
     * @param e        异常
     * @param callback 失败的结果回调
     */
    private void sendFailedStringCallback(Request request, Exception e, ResultCallback callback) {
        mHandler.post(() -> {
            if (callback != null) {
                callback.onError(request, e);
            }
        });
    }

    /**
     * 发送成功的回调
     *
     * @param t        请求的bean
     * @param callback 回调
     */
    private void sendSuccessResultCallback(Object t, ResultCallback callback) {
        mHandler.post(() -> {
            if (callback != null) {
                callback.onResponse(t);
            }
        });
    }

    /**
     * 返回结果
     *
     * @param <T>
     */
    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperClassTypeParameter(getClass());
        }

        static Type getSuperClassTypeParameter(Class<?> subClass) {
            Type superClass = subClass.getGenericSuperclass();
            if (superClass instanceof Class) {
                throw new RuntimeException("Missing type parameter");
            }
            ParameterizedType parameterizedType = (ParameterizedType) superClass;
            return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
        }

        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(T response);
    }

    /**
     * Post请求的参数
     */
    public static class Param {
        String key;
        String value;

        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
