package com.phoenix.base.network.response;

import com.phoenix.base.network.exception.ApiException;
import com.phoenix.base.network.exception.CustomException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import retrofit2.Response;

public class ResponseTransformer {
    public static <T> ObservableTransformer<Response<T>, T> handleResult() {
        return upstream -> upstream
                .onErrorResumeNext(new ErrorResumeFunction<>())
                .flatMap(new ResponseFunction<>());
    }

    private static class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends Response<T>>> {

        @Override
        public ObservableSource<? extends Response<T>> apply(Throwable throwable) throws Exception {
            return Observable.error(CustomException.handleException(throwable));
        }
    }

    private static class ResponseFunction<T> implements Function<Response<T>, ObservableSource<T>> {

        @Override
        public ObservableSource<T> apply(Response<T> tResponse) throws Exception {
            int code = tResponse.code();
            String message = tResponse.message();
            if (code == 200) {
                T body = tResponse.body();
                if (body == null) {
                    return Observable.error(new ApiException(CustomException.UNKNOWN, "获取数据出错"));
                }
                return Observable.just(body);
            }
            return Observable.error(new ApiException(code, message));
        }
    }
}
