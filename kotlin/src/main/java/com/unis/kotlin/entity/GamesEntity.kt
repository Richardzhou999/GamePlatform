package com.unis.kotlin.entity

import org.litepal.crud.LitePalSupport

class GamesEntity : LitePalSupport() {

     var id: Int = 0
     var v: Int = 0
     var newVersion: Int = 0//记录上一个版本
     var gameId: Int = 0
     var p: String? = null
     var icon: String? = null
     var packname: String? = null
     var name: String? = null
     var account: String? = null//判断用户的游戏
     var isInstallGame: Boolean = false//测试游戏是否已安装
     var isDownGame: Boolean = false//是否开始下载
     var isLocal: Boolean = false//判断游戏是否存在本地
     var isNewGame: Boolean = false//测试是否有新版本

     var process: Int = 0//进度条


}