package com.unis.kotlin.entity

import org.litepal.crud.LitePalSupport

class GamesEntity : LitePalSupport() {

    private var id: Int = 0
    private var v: Int = 0
    private var newVersion: Int = 0//记录上一个版本
    private var gameId: Int = 0
    private var p: String? = null
    private var icon: String? = null
    private var packname: String? = null
    private var name: String? = null
    private var account: String? = null//判断用户的游戏
    private var isInstallGame: Boolean = false//测试游戏是否已安装
    private var isDownGame: Boolean = false//是否开始下载
    private var isLocal: Boolean = false//判断游戏是否存在本地
    private var isNewGame: Boolean = false//测试是否有新版本


}