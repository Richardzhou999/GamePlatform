package com.unis.gameplatfrom.model;

import com.unis.gameplatfrom.api.PublicApiInterface;
import com.unis.gameplatfrom.api.RetrofitWrapper;
import com.unis.gameplatfrom.api.result.BaseCustomListResult;
import com.unis.gameplatfrom.constant.GameConstant;
import com.unis.gameplatfrom.entity.GamesEntity;

import io.reactivex.Observable;

public class GameModel implements GameConstant.Model {

    @Override
    public Observable<BaseCustomListResult<GamesEntity>> getGameList(String uuid) {
        return RetrofitWrapper.getInstance().create(PublicApiInterface.class).getGameList(uuid);
    }
}
