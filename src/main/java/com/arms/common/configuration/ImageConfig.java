package com.arms.common.configuration;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import com.arms.common.util.ClassLoaderUtil;
import org.apache.commons.lang.StringUtils;

public class ImageConfig {
    // 云集后台文件路径 TODO 主域名去掉或放入配置中
    public static final String                fileSrc                = "http://www.taotie.com"
                                                                       + "/_files";

    private static final String               userFilePath           = getExtendUrl()
                                                                       + "_userFiles" + "/";

    private static final String               userFileSrc            = "/_userFiles" + "/";

    private static final String               defaultFileSrc         = userFileSrc + "default"
                                                                       + "/";

    // 香评缩略图使用的尺寸
    private static final Map<String, Integer> COMMENT_THUMB_SIZE_MAP = new HashMap<String, Integer>() {
                                                                         {
                                                                             put("width", 230);
                                                                             put("height", 170);
                                                                         }
                                                                     };
    // 话题缩略图使用的尺寸
    private static final Map<String, Integer> TOPIC_THUMB_SIZE_MAP   = new HashMap<String, Integer>() {
                                                                         {
                                                                             put("width", 220);
                                                                             put("height", 160);
                                                                         }
                                                                     };
    // 小头像
    private static final int                  AVATAR_S_SIZE          = 30;
    // 中头像
    private static final int                  AVATAR_M_SIZE          = 50;
    // 大头像（完整）
    private static final int                  AVATAR_L_SIZE          = 160;

    /* 物理路径 */
    public static String getUserFilePath() {
        return userFilePath;
    }

    public static String getTempDirPath() {
        return userFilePath + "temps" + File.separator;
    }

    public static String getAvatarDirPath() {
        return userFilePath + "avatar" + File.separator;
    }

    public static String getAvatarDirPath(int size) {
        return getAvatarDirPath() + size + File.separator;
    }

    public static String getPreviewDirPath() {
        return userFilePath + "preview" + File.separator;
    }

    public static String getCommentDirPath() {
        return userFilePath + "comment" + File.separator;
    }

    public static String getTopicDirPath() {
        return userFilePath + "topic" + File.separator;
    }

    /* URL 路径 */
    public static String getUserFileSrc() {
        return userFileSrc;
    }

    public static String getTempDirSrc() {
        return userFileSrc + "temps" + "/";
    }

    public static String getAvatarDirSrc() {
        return userFileSrc + "avatar" + "/";
    }

    public static String getAvatarDirSrc(int size) {
        return getAvatarDirSrc() + size + "/";
    }

    public static String getPreviewDirSrc() {
        return userFileSrc + "preview" + "/";
    }

    public static String getCommentDirSrc() {
        return userFileSrc + "comment" + "/";
    }

    public static String getTopicDirSrc() {
        return userFileSrc + "topic" + "/";
    }

    /* 图片地址 */
    public static String getAvatarImageSrc(String imageName, int size) {
        if (StringUtils.isBlank(imageName) || imageName.equals("null")) {
            return defaultFileSrc + "avatar" + size + ".png";
        }
        return getAvatarDirSrc(size) + imageName;
    }

    public static String getCommentImageSrc(String imageName) {
        if (StringUtils.isBlank(imageName) || imageName.equals("null")) {
            return "";
        }
        return getPreviewDirSrc() + imageName;
    }

    public static String getTopicImageSrc(String imageName) {
        if (StringUtils.isBlank(imageName) || imageName.equals("null")) {
            return defaultFileSrc + "topic-preview.png";
        }
        return getPreviewDirSrc() + imageName;
    }

    public static String getTradeImageSrc(String imageName) {
        if (StringUtils.isBlank(imageName) || imageName.equals("null"))
            return defaultFileSrc + "topic-preview.png";
        return getTopicDirSrc() + imageName;
    }

    public static String getImageName(String imagePath) {
        int separatorNum1 = imagePath.lastIndexOf("/");
        int separatorNum2 = imagePath.lastIndexOf("\\");
        int result = 0;
        if (separatorNum1 > separatorNum2) {
            result = separatorNum1;
        } else {
            result = separatorNum2;
        }
        return imagePath.substring(result + 1, imagePath.length());
    }

    public static String getExtendUrl() {
        try {
            String extendUrl = ClassLoaderUtil.getExtendURL("../../../../");
            return extendUrl;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
