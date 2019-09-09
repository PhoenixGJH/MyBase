package com.phoenix.base.network.converter;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.phoenix.base.network.exception.ApiException;
import com.phoenix.base.network.response.BaseResponseBean;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    public CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            if (result instanceof BaseResponseBean) {
                BaseResponseBean baseResponseBean = (BaseResponseBean) result;
                if (baseResponseBean.isCodeInvalid()) {
                    throw new ApiException(baseResponseBean.getCode(), baseResponseBean.getMessage());
                }
            }
            return result;
        } finally {
            value.close();
        }
    }
}
