package com.auto.di.guan.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class GzipUtil {

    public static final String TAG = "GzipUtil";
    /**
     * 使用 gzip压缩
     * @param src  需要压缩的字符串
     * @return 压缩后的字符串 并结合 Base64编码
     */
    public static  String gzip(String src){
        String dest = null;
        if (TextUtils.isEmpty(src)){
            return dest;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gout = null;
        try {
            gout = new GZIPOutputStream(bos);
            gout.write(src.getBytes("utf-8"));
            gout.close();
            byte[] buffer = bos.toByteArray();
            dest = Base64.encodeToString(buffer,Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            dest = null;
        }finally {
            try {

                if (null != bos){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dest;
    }

    /**
     * 使用 gzip解压缩
     * @param str 原字符串
     * @return 加压缩后的字符串
     */
    public static String ungzip(String str){
        String dest = null;
        if (TextUtils.isEmpty(str)){
            return dest;
        }
        ByteArrayOutputStream bout = null;
        ByteArrayInputStream bin = null;
        GZIPInputStream zin = null;

        try {
            byte[] compressed = Base64.decode(str,Base64.DEFAULT);
            bout = new ByteArrayOutputStream();
            bin = new ByteArrayInputStream(compressed);
            zin = new GZIPInputStream(bin);
            byte[] buffer = new byte[1024];
            int len  =-1;
            while ((len = zin.read(buffer))!=-1){
                bout.write(buffer,0,len);
            }
            bout.flush();
            dest = bout.toString();
        } catch (IOException e) {
            e.printStackTrace();
            dest = null;
        }finally {
            try {
                if (null != zin){
                    zin.close();
                }
                if (null != bin){
                    bin.close();
                }
                if (null != bout){
                    bout.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dest;
    }

    /**
     * 解压缩
     * @param src 需要解压缩的字符串
     * @return 解压后的字符串
     */
    public static String decompress(String src){
        String decompressStr= null;
        if (null == src){
            return  decompressStr;
        }
        ByteArrayOutputStream bout = null;
        ByteArrayInputStream bint = null;
        ZipInputStream zin = null;
        byte[] compressed = Base64.decode(src,Base64.DEFAULT);
        bout = new ByteArrayOutputStream();
        bint = new ByteArrayInputStream(compressed);
        zin = new ZipInputStream(bint);
        try {
            zin.getNextEntry();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len=zin.read(buffer))!=-1){
                bout.write(buffer,0,len);
            }
            bout.flush();
            //bout.close();
            decompressStr = bout.toString();
        } catch (IOException e) {
            e.printStackTrace();
            decompressStr = null;
        }finally {

            try {
                if (null != zin){
                    zin.close();
                }
                if (null != bint){
                    bint.close();
                }
                if (null != bout){
                    bout.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        LogUtils.e(TAG, "decompress: compressStr="+src+";deCompressStr ="+decompressStr);

        return decompressStr;
    }

    /**
     * 压缩
     * @param src 压缩前的字符串
     * @return 压缩后的字符串
     */
    public static String compress(String src){
        String compressStr = null;
        if (null == src ){
            return  null;
        }

        ByteArrayOutputStream bos = null;
        ZipOutputStream zos = null;
        byte[] compressed = null;


        try {
            bos = new ByteArrayOutputStream();
            zos = new ZipOutputStream(bos);
            zos.putNextEntry(new ZipEntry("0"));
            zos.write(src.getBytes("utf-8"));
            zos.closeEntry();
            compressed = bos.toByteArray();
            //Base 64 编码
            compressStr = Base64.encodeToString(compressed,Base64.DEFAULT);
//            Log.e(TAG, "compress: str="+compressStr);
        } catch (IOException e) {
            e.printStackTrace();
            compressStr = null;
        }finally {

            try {
                if (zos != null){
                    zos.close();
                }
                if (bos != null){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        LogUtils.e(TAG, "compress:\n deCompressStr="+src+"\ncompressStr=\n"+compressStr);
        return  compressStr;
    }
}
