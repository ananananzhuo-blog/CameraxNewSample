package com.broaddeep.face.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author by  zhengshaorui on
 * Describe:
 */
public class BitmapUtils1 {

    /**
     * 镜像
     * @param bitmap
     * @return
     */
    public static Bitmap mirror(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.postScale(-1f,1f);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }

    /**
     * 旋转图片
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotate(Bitmap bitmap,float degress){
        Matrix matrix = new Matrix();
        matrix.postRotate(degress);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }

    public void saveMyBitmap(Bitmap bitmap,String path,String name) throws IOException {
        File directory = new File(path);
        if(!directory.exists()){
            directory.exists();
        }
        File file = new File(directory,name);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
