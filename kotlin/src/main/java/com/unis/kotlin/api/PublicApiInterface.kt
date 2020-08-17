package com.unis.kotlin.api

import com.unis.kotlin.api.result.BaseCustomListResult
import com.unis.kotlin.entity.GamesEntity
import com.unis.kotlin.entity.LoginEntity
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PublicApiInterface {

    /**
     * 登陆
     */
    @FormUrlEncoded
    @POST("/hz_login")
    fun passwordLogin(@Field("acc") account: String, @Field("pwd") password: String,
                      @Field("netaddress") netaddress:String,@Field("type") type:Int,
                      @Field("key") key:String):Observable<LoginEntity>

    /**
     * 获取游戏列表
     */
    @FormUrlEncoded
    @POST("/gamelist")
    fun getGameList(@Field("uuid") uuid: String): Observable<BaseCustomListResult<GamesEntity>>


}

