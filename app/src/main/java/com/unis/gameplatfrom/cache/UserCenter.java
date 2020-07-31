package com.unis.gameplatfrom.cache;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.umeng.analytics.MobclickAgent;
import com.unis.gameplatfrom.utils.JSONUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.unis.gameplatfrom.utils.udateapk.DownloadAPk.APK_UPGRADE;


/**
 * Created by wulei on 2016-09-12.
 */

@SuppressWarnings("DefaultFileTemplate")
public class UserCenter {
    public static final String SP_NAME = "user_center";
    public static final String SP_TOKEN = "sp_token";
    public static final String SP_USER_INFO = "sp_user_info";
    public static final String SP_PUBKEY = "sp_pubkey";
    public static final String SP_WEBURL = "sp_weburl";
    public static final String SP_HOST = "sp_host";


    public static final String SP_ACCOUNT = "sp_account";
    public static final String SP_USERID = "sp_userid";
    public static final String SP_PASSWORD = "sp_password";
    public static final String SP_USERNAME = "sp_username";
    public static final String SP_USERHEAD = "sp_userhead";

    private static UserCenter instance;

    private String token;

    private String pubkey;

    private String webUrl;

    private String account;

    private String userid;

    private String game_account;

    private String password;

    private String userName;
    private String userHead;




    private String host;//环境域名





    private UserCenter() {

        try {
            this.token = SPUtils.getInstance(SP_NAME).getString(SP_TOKEN);
            this.host = SPUtils.getInstance(SP_NAME).getString(SP_HOST);
            this.account = SPUtils.getInstance(SP_NAME).getString(SP_ACCOUNT);
            this.userid = SPUtils.getInstance(SP_NAME).getString(SP_USERID);
            this.userName = SPUtils.getInstance(SP_NAME).getString(SP_USERNAME);
            this.userHead = SPUtils.getInstance(SP_NAME).getString(SP_USERHEAD);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UserCenter getInstance() {
        if (instance == null) {
            instance = new UserCenter();
        }
        return instance;
    }

    public String getHost() {
        if (host == null || host.equals("") || host.isEmpty()){
//            return "https://api.mollyfantasy.universal-space.cn";
            return "http://mmtk.shiyugame.com";//测试
        }
        return host;
    }

    public void setHost(String host) {
        this.host = host;
        SPUtils.getInstance(SP_NAME).put(SP_HOST,host);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        SPUtils.getInstance(SP_NAME).put(SP_TOKEN, token);
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
        SPUtils.getInstance(SP_NAME).put(SP_ACCOUNT, account);
    }

    public String getUserid() {
        return  SPUtils.getInstance(SP_NAME).getString(SP_USERID);
    }

    public void setUserid(String userid) {

        SPUtils.getInstance(SP_NAME).put(SP_USERID, userid);
    }

    public void delectUserid(){
        SPUtils.getInstance(SP_NAME).remove(SP_USERID);
    }

    public String getGame_account(){
        return game_account;
    }

    public void setGameAccount(String account){
        this.game_account = account;
        SPUtils.getInstance(SP_NAME).put(SP_ACCOUNT, account);
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        SPUtils.getInstance(SP_NAME).put(SP_USERNAME, userName);
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
        SPUtils.getInstance(SP_NAME).put(SP_USERHEAD, userHead);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        SPUtils.getInstance(SP_NAME).put(SP_PASSWORD, password);
    }

    public String getPubkey() {
        return pubkey;
    }

    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
        SPUtils.getInstance(SP_NAME).put(SP_PUBKEY,pubkey);
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
        SPUtils.getInstance(SP_NAME).put(SP_WEBURL,webUrl);
    }




    public void save_uuid(Context context, String uuid){


        String filePath = null;

        FileOutputStream outStream = null;
        boolean hasSDCard =Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if (hasSDCard) {

            filePath = Environment.getExternalStorageDirectory() + "/UNIS_GameData";


        } else {

            filePath = Environment.getDownloadCacheDirectory() + "/UNIS_GameData";

        }

        try {

            File uidFiles = new File(filePath);
            if(!uidFiles.exists()){
                uidFiles.mkdirs();
            }

            File uidFile = new File(filePath,"gameUid.txt");
            if (!uidFile.exists()){
                uidFile.createNewFile();//创建文件TXT
            }

            outStream = new FileOutputStream(uidFile);

            outStream.write(uuid.getBytes());
            outStream.flush();

            } catch (Exception e) {

            e.printStackTrace();

        }finally {

            try {
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
            }
        }
    }

    public void save_gameId(int gameId){


        String filePath = null;

        FileOutputStream outStream = null;
        boolean hasSDCard =Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if (hasSDCard) {

            filePath = Environment.getExternalStorageDirectory() + "/UNIS_GameData";


        } else {

            filePath = Environment.getDownloadCacheDirectory() + "/UNIS_GameData";

        }

        String uuid = getToken();
        if(uuid.isEmpty()){
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\"uuid\""+":"+"\""+uuid+"\"");
        builder.append(",");
        builder.append("\"gameid\""+":"+"\""+gameId+"\"");
        builder.append("}");



        try {

            File uidFiles = new File(filePath);
            if(!uidFiles.exists()){
                uidFiles.mkdirs();
            }

            File uidFile = new File(filePath,"gameUid.txt");
            if (!uidFile.exists()){
                uidFile.createNewFile();//创建文件TXT
            }

            outStream = new FileOutputStream(uidFile);

            outStream.write(builder.toString().getBytes());
            outStream.flush();

        } catch (Exception e) {

            e.printStackTrace();

        }finally {

            try {
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
            }
        }

    }

    public void delete_uuid(){


        String filePath = null;

        boolean hasSDCard =Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if (hasSDCard) {

            filePath = Environment.getExternalStorageDirectory() + "/UNIS_GameData/gameUid.txt";


        } else {

            filePath = Environment.getDownloadCacheDirectory() + "/UNIS_GameData/gameUid.txt";

        }


        try {

            File uidFiles = new File(filePath);
            if(uidFiles.exists() && uidFiles.isFile()){
                uidFiles.delete();
            }


        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void deleteGameFile(String pageName){

        String filePath = null;
        boolean hasSDCard =Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if (hasSDCard) {

            filePath = Environment.getExternalStorageDirectory() + "/UNIS_GameData/"+pageName;


        } else {

            filePath = Environment.getDownloadCacheDirectory() + "/UNIS_GameData/"+pageName;

        }


        File uidFiles = new File(filePath);
        if(uidFiles.exists()) {
            deleteDir(uidFiles);
        }
    }

    public void deleteDownFile(){

        String filePath = null;
        FileOutputStream outStream = null;
        boolean hasSDCard =Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if (hasSDCard) {

            filePath = Environment.getExternalStorageDirectory() + "/Download/apk";


        } else {

            filePath = Environment.getDownloadCacheDirectory() + "/Download/apk";

        }


        File uidFiles = new File(filePath);
        if(uidFiles.exists()) {
            deleteDir(uidFiles);
        }


    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private  boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }





   /**
     * 是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return EmptyUtils.isNotEmpty(token);
    }

    public void logout() {
        SPUtils.getInstance(SP_NAME).clear();
        this.token = "";
        this.pubkey = "";
        this.webUrl = "";


        MobclickAgent.onProfileSignOff();
    }

}
