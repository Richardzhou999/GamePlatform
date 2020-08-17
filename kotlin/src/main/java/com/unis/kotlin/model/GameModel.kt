package com.unis.kotlin.model

import com.unis.kotlin.api.PublicApiInterface
import com.unis.kotlin.api.RetrofitWrapper
import com.unis.kotlin.api.result.BaseCustomListResult
import com.unis.kotlin.constant.GameConstant
import com.unis.kotlin.entity.GamesEntity
import io.reactivex.Observable

class GameModel : GameConstant.Model{


    override fun getGameList(uuid: String): Observable<BaseCustomListResult<GamesEntity>> {
        return RetrofitWrapper.get().create(PublicApiInterface::class.java)
                .getGameList(uuid)
    }


}