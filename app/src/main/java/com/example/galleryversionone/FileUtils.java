package com.example.galleryversionone;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

public class FileUtils {

    public static InputStream getThumbnailInputStream(ContentResolver cr, Long imageId) {
        Bitmap originalBitmap, scaleDownBitmap;

        originalBitmap = MediaStore.Images.Thumbnails.getThumbnail(
                cr, imageId,
                MediaStore.Images.Thumbnails.MICRO_KIND,
                null);
        System.out.println("before compression "+originalBitmap.getAllocationByteCount());
        scaleDownBitmap = scaleDown(originalBitmap, 100, false);
        System.out.println("after scale down "+scaleDownBitmap.getAllocationByteCount());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        scaleDownBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        System.out.println("after compression "+baos.toByteArray().length);
        return is;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio;
        int width, height;
        Bitmap newBitmap;
        ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        /* ratio should be less than 1 to get scale down image */
        if (ratio >= 1.0) {
            return realImage;
        }
        width = Math.round((float) ratio * realImage.getWidth());
        height = Math.round((float) ratio * realImage.getHeight());
        newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        return newBitmap;
    }

    public static String formatFileSize(long size) {
        String hrSize = null;

        double b = size;
        double k = size / 1024.0;
        double m = ((size / 1024.0) / 1024.0);
        double g = (((size / 1024.0) / 1024.0) / 1024.0);
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else if (k > 1) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }

        return hrSize;
    }
}
