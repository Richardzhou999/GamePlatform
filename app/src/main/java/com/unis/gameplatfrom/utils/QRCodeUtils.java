package com.unis.gameplatfrom.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

public class QRCodeUtils {

    /**
     * 生成二维码
     *
     * @param text
     * @param widthAndHeight
     * @return
     */
    public static Bitmap createQRCode(String text, int widthAndHeight) {
        try {
            // 需要引入core包
            QRCodeWriter writer = new QRCodeWriter();

            if (text == null || "".equals(text) || text.length() < 1) {
                return null;
            }

            // 把输入的文本转为二维码
            BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
                    widthAndHeight, widthAndHeight);

            System.out.println("w:" + martix.getWidth() + "h:"
                    + martix.getHeight());

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight, hints);
            int[] pixels = new int[widthAndHeight * widthAndHeight];
            for (int y = 0; y < widthAndHeight; y++) {
                for (int x = 0; x < widthAndHeight; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthAndHeight + x] = 0xff000000;
                    } else {
                        pixels[y * widthAndHeight + x] = 0xffffffff;
                    }

                }
            }

            Bitmap bitmap = Bitmap.createBitmap(widthAndHeight, widthAndHeight,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, widthAndHeight, 0, 0, widthAndHeight, widthAndHeight);

            return bitmap;

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


}
