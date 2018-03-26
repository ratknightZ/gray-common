package com.arms.common.image;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.arms.common.util.EditorFliter;
import net.coobird.thumbnailator.Thumbnails;

/**
 * 图片操作
 * 
 * @author Nero
 * 
 */
public class ImageOperateUtil {

    private static String IMAGE_NEW_UPLOAD_sPATH = null;

    /**
     * 获取图片信息返回BufferedImage;
     * 
     * @param strOriginalFilePath
     *            图片路径，可以是http:// 也可是指定具体目录的路径
     * @return
     */
    public static BufferedImage getBufferdImage(String strOriginalFilePath) {
        try {

            String reg = "http://.*";
            BufferedImage imageBuffer = null;
            if (!strOriginalFilePath.matches(reg)) {
                File file = new File(strOriginalFilePath);
                imageBuffer = ImageIO.read(file);
                return imageBuffer;
            }
            // BufferedImage imageBuffered = HttpClientUtil.getInstance()
            // .getImgAsStream(strOriginalFilePath);
            // return imageBuffered;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 旋转图片为指定角度
     * 
     * @param bufferedimage
     *            目标图像
     * @param degree
     *            旋转角度
     * @return
     */
    public static BufferedImage rotateImage(final BufferedImage bufferedimage, final int degree) {
        int w = bufferedimage.getWidth();
        int h = bufferedimage.getHeight();
        int type = bufferedimage.getColorModel().getTransparency();
        BufferedImage img;
        Graphics2D graphics2d;
        (graphics2d = (img = new BufferedImage(w, h, type)).createGraphics()).setRenderingHint(
            RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
        graphics2d.drawImage(bufferedimage, 0, 0, null);
        graphics2d.dispose();
        return img;
    }

    /**
     * 水平翻转图片
     */
    public static BufferedImage flipImage(final BufferedImage imageBuffered) {
        int w = imageBuffered.getWidth();
        int h = imageBuffered.getHeight();
        int type = imageBuffered.getColorModel().getTransparency();
        BufferedImage img = new BufferedImage(w, h, type);
        Graphics2D graphics2d = img.createGraphics();
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        /*
         * img - 要绘制的指定图像。如果 img 为 null，则此方法不执行任何操作。 dx1 - 目标矩形第一个角的 x 坐标。 dy1 -
         * 目标矩形第一个角的 y 坐标。 dx2 - 目标矩形第二个角的 x 坐标。 dy2 - 目标矩形第二个角的 y 坐标。 sx1 -
         * 源矩形第一个角的 x 坐标。 sy1 - 源矩形第一个角的 y 坐标。 sx2 - 源矩形第二个角的 x 坐标。 sy2 -
         * 源矩形第二个角的 y 坐标。 observer - 当缩放并转换了更多图像时要通知的对象。
         */

        graphics2d.drawImage(imageBuffered, 0, 0, w, h, w, 0, 0, h, null);
        graphics2d.dispose();
        return img;
    }

    /**
     * 输出图片
     * 
     * @param strOutPutFilePath
     *            图片输出位置
     * @param imageBuffer
     *            图片
     * @param type
     *            输出格式
     */
    public static void outPutImage(String strOutPutFilePath, BufferedImage imageBuffer,
                                   String imgType) {
        try {
            OutputStream output = new FileOutputStream(new File(strOutPutFilePath));
            ImageIO.write(imageBuffer, imgType, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int[] getLocation(String patternLocation, double zoom) {
        if (patternLocation == null || patternLocation.equals(""))
            return null;
        Integer locationAx = 0;
        Integer locationAy = 0;
        Integer locationBx = 0;
        Integer locationBy = 0;
        String pLocation[] = patternLocation.split(";");

        String pLocationA[] = pLocation[0].split(","); // 坐标点A
        String pLocationA0 = pLocationA[0];
        String pLocationA1 = pLocationA[1];
        Double pLA0 = Double.parseDouble(pLocationA0);// zoom;

        Double pLA1 = Double.parseDouble(pLocationA1);// zoom;
        locationAx = pLA0.intValue();
        locationAy = pLA1.intValue();
        if (pLA0.doubleValue() > locationAx.doubleValue()) {
            locationAx = locationAx + 1;
        }
        if (pLA1.doubleValue() > locationAy.doubleValue()) {
            locationAy = locationAy + 1;
        }

        String pLocationB[] = pLocation[1].split(","); // 坐标点B
        String pLocationB0 = pLocationB[0];
        String pLocationB1 = pLocationB[1];
        Double pLB0 = Double.parseDouble(pLocationB0);// zoom;
        Double pLB1 = Double.parseDouble(pLocationB1);// zoom;
        locationBx = pLB0.intValue();
        locationBy = pLB1.intValue();
        if (pLB0.doubleValue() > locationBx.doubleValue()) {
            locationBx = locationBx + 1;
        }
        if (pLB1.doubleValue() > locationBy.doubleValue()) {
            locationBy = locationBy + 1;
        }

        int[] i = { locationAx, locationAy, locationBx, locationBy };
        return i;
    }

    // 等比缩放的长宽计算, 返回一个最接近原图的缩放之后的长宽
    public static int[] tellDimensions(int originalW, int originalH, int width, int height) {
        int newWidth = 0;
        int newHeight = 0;
        double rate;
        if (originalW > width || originalH > height) {
            if (originalW - width > originalH - height) {
                newWidth = width;
                rate = (double) newWidth / originalW;
                newHeight = (int) (originalH * rate);
            } else {
                newHeight = height;
                rate = (double) newHeight / originalH;
                newWidth = (int) (originalW * rate);
            }
        } else {
            newWidth = originalW;
            newHeight = originalH;
        }
        return new int[] { newWidth, newHeight };
    }

    private static String getFormatName(Object o) {
        try {
            // Create an image input stream on the image
            ImageInputStream iis = ImageIO.createImageInputStream(o);

            // Find all image readers that recognize the image format
            Iterator iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()) {
                // No readers found
                return null;
            }

            // Use the first reader
            ImageReader reader = (ImageReader) iter.next();

            // Close stream
            iis.close();

            // Return the format name
            return reader.getFormatName();
        } catch (IOException e) {
            //
        }

        // The image could not be read
        return null;
    }

    public static String cutThumbNails(String strOriginalFilePath, String strOutPutFilePath, int x,
                                       int y, int width, int height, int tagertWidth,
                                       int targetHeight) {
        String defaultType = "jpg";
        if ((strOriginalFilePath != null) && (strOriginalFilePath.length() > 0)) {
            int i = strOriginalFilePath.lastIndexOf('.');

            if ((i > -1) && (i < (strOriginalFilePath.length() - 1))) {
                defaultType = strOriginalFilePath.substring(i + 1);
            }
        }
        try {
            Thumbnails.of(new File(strOriginalFilePath)).sourceRegion(x, y, width, height)
                .size(tagertWidth, targetHeight).outputFormat(defaultType)
                .toFile(strOutPutFilePath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strOutPutFilePath;

    }

    public static String resizeThumbNails(String strOriginalFilePath, String strOutPutFilePath,
                                          int tagertWidth, int targetHeight) {

        String defaultType = "jpg";
        if ((strOriginalFilePath != null) && (strOriginalFilePath.length() > 0)) {
            int i = strOriginalFilePath.lastIndexOf('.');

            if ((i > -1) && (i < (strOriginalFilePath.length() - 1))) {
                defaultType = strOriginalFilePath.substring(i + 1);
            }
        }

        try {
            Thumbnails.of(new File(strOriginalFilePath)).size(tagertWidth, targetHeight)
                .outputFormat(defaultType).toFile(strOutPutFilePath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strOutPutFilePath;
    }

    public static boolean isStandard(String strOriginalFilePath, int minWidth, int minHeight,
                                     int maxWidth, int maxHeight) {
        BufferedImage buff = getBufferdImage(strOriginalFilePath);

        int imgW = buff.getWidth();
        int imgH = buff.getHeight();
        System.out.println("图片宽为：" + imgW + ",长为：" + imgH);
        if (imgH < minHeight || imgW < maxWidth || imgH > maxHeight || imgW > maxWidth) {
            System.out.println("图片不符合裁切标准！");
            return false;
        }
        return true;
    }

    public static String centerCutThumbNails(String strOriginalFilePath, String strOutPutFilePath,
                                             int w, int h) {
        BufferedImage buff = getBufferdImage(strOriginalFilePath);
        double imgW = buff.getWidth();
        double imgH = buff.getHeight();

        String defaultType = "jpg";

        if ((strOriginalFilePath != null) && (strOriginalFilePath.length() > 0)) {
            int i = strOriginalFilePath.lastIndexOf('.');

            if ((i > -1) && (i < (strOriginalFilePath.length() - 1))) {
                defaultType = strOriginalFilePath.substring(i + 1);
            }
        }

        double targetW = 0.0;
        double targetH = 0.0;

        double deviationH = 0.0;
        double deviationW = 0.0;

        // 图片尺寸比目标尺寸小的时候不处理避免缩放
        if (imgW < w || imgH < h) {
            targetW = imgW;
            targetH = imgH;

        } else {

            double whradio = (double) w / (double) h;

            double hwradio = (double) h / (double) w;

            targetW = imgH * whradio;
            targetH = imgH;

            deviationW = (imgW - targetW) / 2;

            if (deviationW <= 0) {
                targetH = hwradio * imgW;
                targetW = imgW;
                deviationH = (imgH - targetH) / 2;
                deviationW = 0;
            }
        }

        try {
            Thumbnails
                .of(new File(strOriginalFilePath))
                .sourceRegion((int) (0 + deviationW), (int) (0 + deviationH), (int) targetW,
                    (int) targetH).size(w, h).outputFormat(defaultType).toFile(strOutPutFilePath);
        } catch (IOException e) {
            System.out.println(e);
        }

        return strOutPutFilePath;

    }

    public static HashMap<String, Integer> getImageSize(String strOriginalFilePath) {
        BufferedImage buff = getBufferdImage(strOriginalFilePath);

        int imgW = buff.getWidth();
        int imgH = buff.getHeight();
        System.out.println("图片宽为：" + imgW + ",长为：" + imgH);
        HashMap<String, Integer> imageSizeMap = new HashMap<String, Integer>();
        imageSizeMap.put("width", imgW);
        imageSizeMap.put("height", imgH);
        return imageSizeMap;
    }

    public static String getPreviewImg(String content, String strOriginalFileDir,
                                       String strOutPutFileDir, int maxWidth, int zoomWidth,
                                       int zoomHeight) {
        String previewImgString = EditorFliter.filterYunjeeImage(content);
        if (previewImgString != null) {
            previewImgString = previewImgString.substring(previewImgString.lastIndexOf("/") + 1,
                previewImgString.length());
            String strOriginalFilePath = strOriginalFileDir + "/" + previewImgString;
            String strOutPutFilePath = strOutPutFileDir + previewImgString;
            HashMap<String, Integer> imageSizeMap = getImageSize(strOriginalFilePath);
            int width = imageSizeMap.get("width");
            int height = imageSizeMap.get("height");
            double scale = (double) zoomWidth / (double) zoomHeight;
            int w, h;
            if (width > zoomWidth && height > zoomHeight) {
                if (width > height) {
                    w = (int) (height * scale);
                    h = height;
                } else {
                    w = width;
                    h = (int) (width / scale);
                }
                ImageOperateUtil.centerCutThumbNails(strOriginalFilePath, strOutPutFilePath, w, h);
            } else {
                previewImgString = null;
            }
        }
        return previewImgString;
    }

    public static void main(String[] args) {

        // resize("http://img04.taobaocdn.com/imgextra/i4/62004902/T27IBAXlXaXXXXXXXX_!!62004902.jpg",
        // "c://1.jpg", 120, 120);
        //      thumbNails("/Users/Nero/Pictures/2.JPG",
        //              "/Users/Nero/Pictures/3.JPG", 0,0,800,800, 160, 160);

        centerCutThumbNails("C:/Users/Public/Pictures/Sample Pictures/13.jpg",
            "C:/Users/Public/Pictures/Sample Pictures/123.jpg", 220, 160);

    }
}
