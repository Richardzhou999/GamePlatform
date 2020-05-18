package com.unis.gameplatfrom.api;



import com.unis.gameplatfrom.api.result.BaseResult;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


/**
 * Created by wulei on 16/5/27.
 */
@SuppressWarnings("DefaultFileTemplate")
public class HttpResultFunc<T extends BaseResult> implements Function<T, T> {
    @Override
    public T apply(@NonNull T baseResult) throws Exception {
        if (!baseResult.isSuccess()) {
            throw new ApiException(baseResult.getCode(), baseResult.getMessage());
        }
        return baseResult;
    }

}
