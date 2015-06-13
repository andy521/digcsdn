package com.bob.digcsdn.util;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by bob on 15-6-11.
 */
public class FileUtil {
    public static String filePath = android.os.Environment
            .getExternalStorageState() + "/BobBlog";//文件存储路径


    public static String getFileName(String fileName) {
        //去除url中多余的字符，用来得到简单的文件名
        fileName = fileName.replaceAll("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]", "");
        return fileName + ".png";
    }

    /**
     * 写入sd卡对应的目录中
     */

    public static void write2SdCard(InputStream input, String fileName) {

        try {
            File file = new File(filePath);
            if (!file.exists()) {//不存在则创建文件夹
                file.mkdirs();
            }

            FileOutputStream fostream = new FileOutputStream(filePath + "/" + fileName);
            byte[] buffer = new byte[512];//设置512字节的缓冲区
            int count = 0;

            while ((count = input.read(buffer)) > 0) {
                fostream.write(buffer, 0, count);//将读取到的字节信息分批写入目标流中
            }

            fostream.flush();
            fostream.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 位图bitmap的存储
     * @param bmp
     * @param url
     * @return
     */
    public static boolean write2SdCard(Bitmap bmp, String url) {
        try{
            write2SdCard(bitmap2InputStream(bmp), getFileName(url));
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Bitmap转换为byte[]
     *
     * @param bm
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Bitmap转换成InputStream
     *
     * @param bm
     * @return
     */
    public static InputStream bitmap2InputStream(Bitmap bm) {
        return new ByteArrayInputStream(bitmap2Bytes(bm));
    }

    public static StringBuilder getFileContent(Context context, String fileName){//应该是文件的全限定名
        StringBuilder content= new StringBuilder();
        try {
            InputStream is= context.getResources().getAssets().open(fileName);

            byte[] buffer= new byte[1024];
            int count= 0;
            while((count= is.read(buffer))> 0){
                content.append(new String(buffer, 0, count, Charset.forName("utf-8")));
            }
        }catch (Exception e){
            e.printStackTrace();
            return content;//返回的是一个错误的content
        }
        return content;
    }
}
