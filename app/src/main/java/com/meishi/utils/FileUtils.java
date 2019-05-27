package com.meishi.utils;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 负责申请内存空间的封装类
 */
public class FileUtils {
    private static String mSDCardFolderPath;
    private static String mImgFolderPath;
    private static String mApkFolderPath;
    private static String mCacheFolderPath;
    private static String mLogFolderPath;
    private static String mUserIconFolderPath;

    public static FileUtils getInstance(){
        return FileUtilsHolder.instance;
    }

    public void init(){
        mImgFolderPath = getImgFolderPath();
        mApkFolderPath = getApkFolderPath();
        mCacheFolderPath = getCacheFolderPath();
        mLogFolderPath = getLogFolderPath();
        mUserIconFolderPath = getmUserIconFolderPath();

        mkdirs(mImgFolderPath);
        mkdirs(mApkFolderPath);
        mkdirs(mCacheFolderPath);
        mkdirs(mLogFolderPath);
        mkdirs(mUserIconFolderPath);
    }

    private String getSDCardFolderPath(){
        if(TextUtils.isEmpty(mSDCardFolderPath)){
            mSDCardFolderPath = Environment.getExternalStorageDirectory().getPath()+"/Meishi";
        }

        return mSDCardFolderPath;
    }

    public File mkdirs(@NonNull String path){
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }

        return file;
    }

    public String getCacheFolderPath(){
        if(TextUtils.isEmpty(mCacheFolderPath)){
            mCacheFolderPath = getSDCardFolderPath()+"/Cache/";
        }

        return mCacheFolderPath;
    }

    public String getImgFolderPath(){
        if(TextUtils.isEmpty(mImgFolderPath)){
            mImgFolderPath = getSDCardFolderPath()+"/Img/";
        }

        return mImgFolderPath;
    }

    public String getApkFolderPath(){
        if (TextUtils.isEmpty(mApkFolderPath)){
            mApkFolderPath = getSDCardFolderPath()+"/Apk/";
        }

        return mApkFolderPath;
    }

    public String getLogFolderPath(){
        if (TextUtils.isEmpty(mLogFolderPath)){
            mLogFolderPath = getSDCardFolderPath()+"/Log/";
        }
        return mLogFolderPath;
    }

    public String getmUserIconFolderPath(){
        if(TextUtils.isEmpty(mUserIconFolderPath)){
            mUserIconFolderPath = getSDCardFolderPath()+"/img/";
        }
        return mUserIconFolderPath;
    }

    public File getCacheFolder(){
        return mkdirs(getCacheFolderPath());
    }
//    public File getUserIconFolder(String filename){
//        return mkdirs(getmUserIconFolderPath()+filename);
//    }
    public File getUserIconFolder(){
        return mkdirs(getmUserIconFolderPath());
    }


    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    public int CopySdcardFile(File fromFile, File toFile)
    {

        try
        {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;

        } catch (Exception ex)
        {
            System.out.println("CopySdcardFile："+ex);
            return -1;
        }
    }
    private static class FileUtilsHolder{
        private static FileUtils instance = new FileUtils();
    }


}
