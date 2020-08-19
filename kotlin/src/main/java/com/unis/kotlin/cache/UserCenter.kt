package com.unis.kotlin.cache

import android.content.Context
import android.os.Environment
import com.blankj.utilcode.util.EmptyUtils
import com.blankj.utilcode.util.SPUtils
import com.umeng.analytics.MobclickAgent
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class UserCenter {

    val SP_NAME = "user_center"
    val SP_TOKEN = "sp_token"
    val SP_USER_INFO = "sp_user_info"
    val SP_PUBKEY = "sp_pubkey"
    val SP_WEBURL = "sp_weburl"
    val SP_HOST = "sp_host"


    val SP_ACCOUNT = "sp_account"
    val SP_USERID = "sp_userid"
    val SP_PASSWORD = "sp_password"
    val SP_USERNAME = "sp_username"
    val SP_USERHEAD = "sp_userhead"



    private var token: String? = null

    private var pubkey: String? = null

    private var webUrl: String? = null

    private var account: String? = null

    private var userid: String? = null

    private var game_account: String? = null

    private var password: String? = null

    private var userName: String? = null
    private var userHead: String? = null


    companion object {

        private var instant : UserCenter? = null

        get() {

            if(field == null){
                field = UserCenter()
            }
            return field
        }

        @Synchronized
        fun get() : UserCenter? {
           return instant!!
        }

    }

    fun getToken(): String {
        return token!!
    }

    fun setToken(token: String?) {
        this.token = token
        SPUtils.getInstance(SP_NAME).put(SP_TOKEN, token!!)
    }

    fun getAccount(): String {
        return account!!
    }

    fun setAccount(account: String) {
        this.account = account
        SPUtils.getInstance(SP_NAME).put(SP_ACCOUNT, account)
    }

    fun getUserid(): String {
        return SPUtils.getInstance(SP_NAME).getString(SP_USERID)
    }

    fun setUserid(userid: String) {

        SPUtils.getInstance(SP_NAME).put(SP_USERID, userid)
    }

    fun delectUserid() {
        SPUtils.getInstance(SP_NAME).remove(SP_USERID)
    }

    fun getGame_account(): String? {
        return game_account
    }

    fun setGameAccount(account: String) {
        this.game_account = account
        SPUtils.getInstance(SP_NAME).put(SP_ACCOUNT, account)
    }


    fun getUserName(): String {
        return userName!!
    }

    fun setUserName(userName: String?) {
        this.userName = userName
        SPUtils.getInstance(SP_NAME).put(SP_USERNAME, userName!!)
    }

    fun getUserHead(): String {
        return userHead!!
    }

    fun setUserHead(userHead: String?) {
        this.userHead = userHead
        SPUtils.getInstance(SP_NAME).put(SP_USERHEAD, userHead!!)
    }

    fun getPassword(): String {
        return password!!
    }

    fun setPassword(password: String) {
        this.password = password
        SPUtils.getInstance(SP_NAME).put(SP_PASSWORD, password)
    }

    fun getPubkey(): String {
        return pubkey!!
    }

    fun setPubkey(pubkey: String) {
        this.pubkey = pubkey
        SPUtils.getInstance(SP_NAME).put(SP_PUBKEY, pubkey)
    }

    fun getWebUrl(): String {
        return webUrl!!
    }

    fun setWebUrl(webUrl: String) {
        this.webUrl = webUrl
        SPUtils.getInstance(SP_NAME).put(SP_WEBURL, webUrl)
    }


    fun save_uuid(context: Context, uuid: String) {


        var filePath: String? = null

        var outStream: FileOutputStream? = null
        val hasSDCard = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

        if (hasSDCard) {

            filePath = Environment.getExternalStorageDirectory().toString() + "/UNIS_GameData"


        } else {

            filePath = Environment.getDownloadCacheDirectory().toString() + "/UNIS_GameData"

        }

        try {

            val uidFiles = File(filePath)
            if (!uidFiles.exists()) {
                uidFiles.mkdirs()
            }

            val uidFile = File(filePath, "gameUid.txt")
            if (!uidFile.exists()) {
                uidFile.createNewFile()//创建文件TXT
            }

            outStream = FileOutputStream(uidFile)

            outStream.write(uuid.toByteArray())
            outStream.flush()

        } catch (e: Exception) {

            e.printStackTrace()

        } finally {

            try {
                outStream?.close()
            } catch (e: IOException) {
            }

        }
    }

    fun save_gameId(gameId: Int) {


        var filePath: String? = null

        var outStream: FileOutputStream? = null
        val hasSDCard = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

        if (hasSDCard) {

            filePath = Environment.getExternalStorageDirectory().toString() + "/UNIS_GameData"


        } else {

            filePath = Environment.getDownloadCacheDirectory().toString() + "/UNIS_GameData"

        }

        val uuid = getToken()
        if (uuid.isEmpty()) {
            return
        }

        val builder = StringBuilder()
        builder.append("{")
        builder.append("\"uuid\":\"$uuid\"")
        builder.append(",")
        builder.append("\"gameid\":\"$gameId\"")
        builder.append("}")



        try {

            val uidFiles = File(filePath)
            if (!uidFiles.exists()) {
                uidFiles.mkdirs()
            }

            val uidFile = File(filePath, "gameUid.txt")
            if (!uidFile.exists()) {
                uidFile.createNewFile()//创建文件TXT
            }

            outStream = FileOutputStream(uidFile)

            outStream.write(builder.toString().toByteArray())
            outStream.flush()

        } catch (e: Exception) {

            e.printStackTrace()

        } finally {

            try {
                outStream?.close()
            } catch (e: IOException) {
            }

        }

    }

    fun delete_uuid() {


        var filePath: String? = null

        val hasSDCard = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

        if (hasSDCard) {

            filePath = Environment.getExternalStorageDirectory().toString() + "/UNIS_GameData/gameUid.txt"


        } else {

            filePath = Environment.getDownloadCacheDirectory().toString() + "/UNIS_GameData/gameUid.txt"

        }


        try {

            val uidFiles = File(filePath)
            if (uidFiles.exists() && uidFiles.isFile) {
                uidFiles.delete()
            }


        } catch (e: Exception) {

            e.printStackTrace()

        }

    }

    fun deleteGameFile(pageName: String) {

        var filePath: String? = null
        var filePath2: String? = null
        val hasSDCard = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

        if (hasSDCard) {

            filePath = Environment.getExternalStorageDirectory().toString() + "/UNIS_GameData/" + pageName
            filePath2 = Environment.getExternalStorageDirectory().toString() + "/GameData/" + pageName

        } else {

            filePath = Environment.getDownloadCacheDirectory().toString() + "/UNIS_GameData/" + pageName
            filePath2 = Environment.getExternalStorageDirectory().toString() + "/GameData/" + pageName
        }


        val uidFiles = File(filePath)
        val uidFiles2 = File(filePath2)
        if (uidFiles.exists()) {
            deleteDir(uidFiles)
        }
        if (uidFiles2.exists()) {
            deleteDir(uidFiles2)
        }

    }

    fun deleteDownFile() {

        var filePath: String? = null
        val outStream: FileOutputStream? = null
        val hasSDCard = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

        if (hasSDCard) {

            filePath = Environment.getExternalStorageDirectory().toString() + "/Download/apk"


        } else {

            filePath = Environment.getDownloadCacheDirectory().toString() + "/Download/apk"

        }


        val uidFiles = File(filePath)
        if (uidFiles.exists()) {
            deleteDir(uidFiles)
        }


    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    private fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.list()
            //递归删除目录中的子目录下
            for (i in children!!.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete()
    }


    /**
     * 是否登录
     *
     * @return
     */
    fun isLogin(): Boolean {
        return EmptyUtils.isNotEmpty(token)
    }

    fun logout() {
        SPUtils.getInstance(SP_NAME).clear()
        this.token = ""
        this.pubkey = ""
        this.webUrl = ""


        MobclickAgent.onProfileSignOff()
    }










}