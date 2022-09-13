package com.wisely.sso.common;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.RandomHelper;
import com.wisely.framework.helper.ResourceHelper;
import com.google.common.collect.Lists;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public class VerifyImageHelper {

    private VerifyImageHelper() {
    }

    /**
     * 源文件宽度
     */
    private static int ORI_WIDTH = 296;
    /**
     * 源文件高度
     */
    private static int ORI_HEIGHT = 182;
    /**
     * 模板图宽度
     */
    private static int CUT_WIDTH = 50;
    /**
     * 模板图高度
     */
    private static int CUT_HEIGHT = 50;
    /**
     * 抠图凸起圆心
     */
    private static int circleR = 6;
    /**
     * 抠图内部矩形填充大小
     */
    private static int RECTANGLE_PADDING = 6;
    /**
     * 抠图的边框宽度
     */
    private static int SLIDER_IMG_OUT_PADDING = 1;

    /**
     *
     */
    public final static String SRC_IMAGE_KEY = "srcImage";
    /**
     *
     */
    public final static String MARK_IMAGE_KEY = "markImage";
    /**
     *
     */
    public final static String LOCATION_X_KEY = "locationX";
    /**
     *
     */
    public final static String LOCATION_Y_KEY = "locationY";


    /**
     * 根据传入的路径生成指定验证码图片
     *
     * @param srcImage
     * @return
     * @throws IOException
     */
    public static Model getVerifyImage(BufferedImage srcImage) throws IOException {

        AssertHelper.EX_VALIDATION.isNotEmpty(srcImage, "common.parameter_required.srcImage");

        int locationX = CUT_WIDTH + new Random().nextInt(srcImage.getWidth() - CUT_WIDTH * 2);
        int locationY = CUT_HEIGHT + new Random().nextInt(srcImage.getHeight() - CUT_HEIGHT) / 2;
        BufferedImage markImage = new BufferedImage(CUT_WIDTH, CUT_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        int[][] data = getBlockData();
        cutImgByTemplate(srcImage, markImage, data, locationX, locationY);

        return Model.builder()
                //放入背景图的加密信息
                .set(SRC_IMAGE_KEY, getImageBASE64(srcImage))
                //放入滑块图的加密信息
                .set(MARK_IMAGE_KEY, getImageBASE64(markImage))
                //放入抠图位置的X方向的信息，用于验证滑块位置是否正确
                .set(LOCATION_X_KEY, locationX)
                //放入抠图位置的Y方向的信息，用于前端控制定位信息
                .set(LOCATION_Y_KEY, locationY);
    }


    /**
     * 生成随机滑块形状
     * <p>
     * 0 透明像素
     * 1 滑块像素
     * 2 阴影像素
     *
     * @return int[][]
     */
    private static int[][] getBlockData() {

        int[][] data = new int[CUT_WIDTH][CUT_HEIGHT];
        Random random = new Random();
        //(x-a)²+(y-b)²=r²
        //x中心位置左右5像素随机
        double x1 = RECTANGLE_PADDING + (CUT_WIDTH - 2 * RECTANGLE_PADDING) / 2.0 - 5 + random.nextInt(10);
        //y 矩形上边界半径-1像素移动
        double y1_top = RECTANGLE_PADDING - random.nextInt(3);
        double y1_bottom = CUT_HEIGHT - RECTANGLE_PADDING + random.nextInt(3);
        double y1 = random.nextInt(2) == 1 ? y1_top : y1_bottom;


        double x2_right = CUT_WIDTH - RECTANGLE_PADDING - circleR + random.nextInt(2 * circleR - 4);
        double x2_left = RECTANGLE_PADDING + circleR - 2 - random.nextInt(2 * circleR - 4);
        double x2 = random.nextInt(2) == 1 ? x2_right : x2_left;
        double y2 = RECTANGLE_PADDING + (CUT_HEIGHT - 2 * RECTANGLE_PADDING) / 2.0 - 4 + random.nextInt(10);

        double po = Math.pow(circleR, 2);
        for (int i = 0; i < CUT_WIDTH; i++) {
            for (int j = 0; j < CUT_HEIGHT; j++) {
                //矩形区域
                boolean fill;
                if ((i >= RECTANGLE_PADDING && i < CUT_WIDTH - RECTANGLE_PADDING)
                        && (j >= RECTANGLE_PADDING && j < CUT_HEIGHT - RECTANGLE_PADDING)) {
                    data[i][j] = 1;
                    fill = true;
                } else {
                    data[i][j] = 0;
                    fill = false;
                }
                //凸出区域
                double d3 = Math.pow(i - x1, 2) + Math.pow(j - y1, 2);
                if (d3 < po) {
                    data[i][j] = 1;
                } else {
                    if (!fill) {
                        data[i][j] = 0;
                    }
                }
                //凹进区域
                double d4 = Math.pow(i - x2, 2) + Math.pow(j - y2, 2);
                if (d4 < po) {
                    data[i][j] = 0;
                }
            }
        }
        //边界阴影
        for (int i = 0; i < CUT_WIDTH; i++) {
            for (int j = 0; j < CUT_HEIGHT; j++) {
                //四个正方形边角处理
                for (int k = 1; k <= SLIDER_IMG_OUT_PADDING; k++) {
                    //左上、右上
                    if (i >= RECTANGLE_PADDING - k && i < RECTANGLE_PADDING
                            && ((j >= RECTANGLE_PADDING - k && j < RECTANGLE_PADDING)
                            || (j >= CUT_HEIGHT - RECTANGLE_PADDING - k && j < CUT_HEIGHT - RECTANGLE_PADDING + 1))) {
                        data[i][j] = 2;
                    }

                    //左下、右下
                    if (i >= CUT_WIDTH - RECTANGLE_PADDING + k - 1 && i < CUT_WIDTH - RECTANGLE_PADDING + 1) {
                        for (int n = 1; n <= SLIDER_IMG_OUT_PADDING; n++) {
                            if (((j >= RECTANGLE_PADDING - n && j < RECTANGLE_PADDING)
                                    || (j >= CUT_HEIGHT - RECTANGLE_PADDING - n && j <= CUT_HEIGHT - RECTANGLE_PADDING))) {
                                data[i][j] = 2;
                            }
                        }
                    }
                }

                if (data[i][j] == 1 && j - SLIDER_IMG_OUT_PADDING > 0 && data[i][j - SLIDER_IMG_OUT_PADDING] == 0) {
                    data[i][j - SLIDER_IMG_OUT_PADDING] = 2;
                }
                if (data[i][j] == 1 && j + SLIDER_IMG_OUT_PADDING > 0 && j + SLIDER_IMG_OUT_PADDING < CUT_HEIGHT && data[i][j + SLIDER_IMG_OUT_PADDING] == 0) {
                    data[i][j + SLIDER_IMG_OUT_PADDING] = 2;
                }
                if (data[i][j] == 1 && i - SLIDER_IMG_OUT_PADDING > 0 && data[i - SLIDER_IMG_OUT_PADDING][j] == 0) {
                    data[i - SLIDER_IMG_OUT_PADDING][j] = 2;
                }
                if (data[i][j] == 1 && i + SLIDER_IMG_OUT_PADDING > 0 && i + SLIDER_IMG_OUT_PADDING < CUT_WIDTH && data[i + SLIDER_IMG_OUT_PADDING][j] == 0) {
                    data[i + SLIDER_IMG_OUT_PADDING][j] = 2;
                }
            }
        }
        return data;
    }

    /**
     * 裁剪区块
     * 根据生成的滑块形状，对原图和裁剪块进行变色处理
     *
     * @param oriImage    原图
     * @param targetImage 裁剪图
     * @param blockImage  滑块
     * @param x           裁剪点x
     * @param y           裁剪点y
     */
    private static void cutImgByTemplate(BufferedImage oriImage, BufferedImage targetImage, int[][] blockImage, int x, int y) {
        for (int i = 0; i < CUT_WIDTH; i++) {
            for (int j = 0; j < CUT_HEIGHT; j++) {
                int _x = x + i;
                int _y = y + j;
                int rgbFlg = blockImage[i][j];
                int rgb_ori = oriImage.getRGB(_x, _y);
                // 原图中对应位置变色处理
                if (rgbFlg == 1) {
                    //抠图上复制对应颜色值
                    targetImage.setRGB(i, j, rgb_ori);
                    //原图对应位置颜色变化
                    oriImage.setRGB(_x, _y, rgb_ori & 0x363636);
                } else if (rgbFlg == 2) {
                    targetImage.setRGB(i, j, Color.WHITE.getRGB());
                    oriImage.setRGB(_x, _y, Color.GRAY.getRGB());
                } else if (rgbFlg == 0) {
                    //int alpha = 0;
                    targetImage.setRGB(i, j, rgb_ori & 0x00ffffff);
                }
            }

        }
    }


    /**
     * 随机获取一张图片对象
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static BufferedImage getRandomImage(String path, VerifyImageFilter imageFilter) throws IOException {

        AssertHelper.EX_SYSTEM.isNotBlank(path, "common.parameter_required.path");

        Resource[] resources = ResourceHelper.getResources(path);
        AssertHelper.EX_SYSTEM.isNotEmpty(resources, "getRandomImage.path_config_error.{0}", path);

        List<File> fileList = Lists.newArrayList();
        for (Resource resource : resources) {
            pickUpFiles(fileList, resource.getFile(), imageFilter);
        }

        File imageFile = RandomHelper.get(fileList);
        AssertHelper.EX_SYSTEM.isNotEmpty(imageFile, "getRandomImage.not_match_image_found");
        return ImageIO.read(imageFile);
    }

    private static void pickUpFiles(List<File> fileList, File file, VerifyImageFilter imageFilter) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                pickUpFiles(fileList, f, imageFilter);
            }
            return;
        }

        if (imageFilter.accept(file)) {
            fileList.add(file);
        }
    }


    public interface VerifyImageFilter {
        boolean accept(File file);
    }

    /**
     * 将IMG输出为文件
     *
     * @param image
     * @param file
     * @throws Exception
     */
    public static void writeImg(BufferedImage image, String file) throws Exception {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bao);
        FileOutputStream out = new FileOutputStream(new File(file));
        out.write(bao.toByteArray());
        out.close();
    }

    /**
     * 将图片转换为BASE64
     *
     * @param image
     * @return
     * @throws IOException
     */
    public static String getImageBASE64(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        byte[] bytes = out.toByteArray();//转换成字节
        String png_base64 = Base64.getEncoder().encodeToString(bytes);//转换成base64串
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
        return png_base64;
    }

    /**
     * 将BASE64字符串转换为图片
     *
     * @param base64String
     * @return
     */
    public static BufferedImage base64StringToImage(String base64String) {
        try {
            byte[] bytes1 = Base64.getDecoder().decode(base64String);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            return ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
