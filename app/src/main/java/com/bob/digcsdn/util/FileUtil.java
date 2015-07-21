package com.bob.digcsdn.util;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by bob on 15-6-11.
 */
public class FileUtil {
    public static String filePath = android.os.Environment
            .getExternalStorageDirectory() + "/DigCSDN";//文件存储路径

    public static String getFileName(String fileName) {
        //去除url中多余的字符，用来得到简单的文件名
        fileName = fileName.replaceAll("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]", "");
        return fileName + ".png";
    }

    /**
     * 将普通的文件内容写入sd卡对应的目录中
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
     *
     * @param bmp
     * @param url
     * @return
     */
    public static boolean write2SdCard(Bitmap bmp, String url) {
        try {
            File file = new File(filePath);//获取存储目录
            if (!file.exists()) {
                file.mkdirs();
            }
            InputStream is = bitmap2InputStream(bmp);

            FileOutputStream fileOutputStream = new FileOutputStream(filePath
                    + "/" + getFileName(url));
            byte[] buffer = new byte[512];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, count);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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

    public static String getFileContent(Context context, String fileName) {//应该是文件的全限定名
        String content = "";
        try {
            // 把数据从文件中读入内存
            InputStream is = context.getResources().getAssets().open(fileName);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int i = is.read(buffer, 0, buffer.length);
            while (i > 0) {
                bs.write(buffer, 0, i);
                i = is.read(buffer, 0, buffer.length);
            }
            content = new String(bs.toByteArray(), Charset.forName("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return content;
    }
}
