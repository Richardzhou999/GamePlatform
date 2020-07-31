package com.unis.gameplatfrom.api;


import com.unis.gameplatfrom.api.result.BaseCustomListResult;
import com.unis.gameplatfrom.entity.GamesEntity;
import com.unis.gameplatfrom.entity.GamesListEntity;
import com.unis.gameplatfrom.entity.LoginEntity;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by wulei on 2016/11/28.
 */

@SuppressWarnings("DefaultFileTemplate")
public interface PublicApiInterface {

    /**
     * 获取游戏列表
     */
    @FormUrlEncoded
    @POST("/gamelist")
    Observable<BaseCustomListResult<GamesEntity>> getGameList(@Field("uuid") String uuid);

    /**
     * 获取游戏列表
     */
    @FormUrlEncoded
    @POST("/gamelist")
    Observable<BaseCustomListResult<GamesListEntity>> getGameList1(@Field("uuid") String uuid);


    /**
     * 登陆
     */
    @FormUrlEncoded
    @POST("/hz_login")
    Observable<LoginEntity> passwordLogin(@Field("acc") String account, @Field("pwd") String password
            , @Field("netaddress") String netaddress, @Field("type") int type, @Field("key") String key);

}
