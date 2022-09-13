package com.wisely.sso.common;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 二维码工具类
 */
public class QrCodeHelper {

    private QrCodeHelper() {
    }


    /**
     * 白边处理
     *
     * @param matrix
     * @param white
     * @return
     */
    private static BitMatrix reSize(BitMatrix matrix, int white) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1 + white * 2;
        int resHeight = rec[3] + 1 + white * 2;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        //resMatrix.setRegion(-2,-2,resWidth, resHeight);
        resMatrix.clear();

        for (int i = 0; i < resWidth - white * 2; i++) {
            for (int j = 0; j < resHeight - white * 2; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i + white, j + white);
            }
        }
        return resMatrix;
    }

    /**
     * 返回二维码的字节数组
     *
     * @param text
     * @param width
     * @param height
     * @return
     * @throws WriterException
     * @throws IOException
     */
    public static byte[] getQRImageBytes(String text, int width, int height) throws WriterException, IOException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        bitMatrix = reSize(bitMatrix, 5);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    /**
     * 返回二维码的图片
     *
     * @param text
     * @param width
     * @param height
     * @return
     * @throws WriterException
     * @throws IOException
     */
    public static BufferedImage getQRImage(String text, int width, int height) throws WriterException, IOException {
        byte[] bytes = getQRImageBytes(text, width, height);
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        return ImageIO.read(in);
    }

}
