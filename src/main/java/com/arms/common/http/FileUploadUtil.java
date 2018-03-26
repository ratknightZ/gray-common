//package com.arms.common.http;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.nio.charset.Charset;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Random;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.alibaba.fastjson.JSON;
//import com.arms.common.vo.JsonVO;
//import org.apache.commons.fileupload.FileItem;
//import org.apache.commons.fileupload.FileUploadException;
//import org.apache.commons.fileupload.disk.DiskFileItemFactory;
//import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.apache.commons.io.IOUtils;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.gson.Gson;
//
///**
// * ImageUploadUtil
// */
//
//public class FileUploadUtil {
//    //private String uploadPath = "/Users/ben/Downloads/test";
//
//    private final static String FILE_UPLOAD_URL     = "http://file.taotie.com/saveFile";
//
//    private static final String URL_UPLOAD_FILE_URL = "http://file.taotie.com/saveUrl";
//
//    private static final Logger logger              = LoggerFactory.getLogger(FileUploadUtil.class);
//
//    private static final String CHARSET_ENCODING    = "UTF-8";
//
//    private static String getUniqueFileName(String filename) {
//        String uniqueFileName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
//            .replaceAll("\\ ", "_").replaceAll("\\:", "-")
//                                + "__"
//                                + new Random().nextInt(10000)
//                                + filename.substring(filename.lastIndexOf("."), filename.length());
//        return uniqueFileName;
//    }
//
//    public static String uploadForIE(HttpServletRequest request, HttpServletResponse response,
//                                     String dirPath) throws ServletException, IOException {
//
//        request.setCharacterEncoding("utf-8"); // 设置编码
//        // 获得磁盘文件条目工厂
//        DiskFileItemFactory factory = new DiskFileItemFactory();
//        // 获取文件需要上传到的路径
//        String path = request.getSession(true).getServletContext().getRealPath("/upload");
//
//        // 如果没以下两行设置的话，上传大的 文件 会占用 很多内存，
//        // 设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
//        /**
//         * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上， 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tem
//         * 格式的 然后再将其真正写到 对应目录的硬盘上
//         */
//        factory.setRepository(new File(path));
//        // 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
//        factory.setSizeThreshold(1024 * 1024);
//
//        // 高水平的API文件上传处理
//        ServletFileUpload upload = new ServletFileUpload(factory);
//
//        try {
//            // 暂时只支持单文件上传
//            List<FileItem> list = upload.parseRequest(request);
//
//            String fileName = getUniqueFileName(list.get(0).getName());
//            String filePath = dirPath + fileName;
//
//            //for (FileItem item : list) {
//
//            list.get(0).write(new File(filePath));
//            //}
//            return fileName;
//
//        } catch (FileUploadException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//
//            // e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static String uploadImage(String url, double x, double y, double w, double h) {
//        MultipartEntity reqEntity = new MultipartEntity();
//
//        try {
//            reqEntity.addPart("url", new StringBody(url, Charset.forName(CHARSET_ENCODING)));
//
//            reqEntity.addPart("x", new StringBody(x + "", Charset.forName(CHARSET_ENCODING)));
//
//            reqEntity.addPart("y", new StringBody(y + "", Charset.forName(CHARSET_ENCODING)));
//            reqEntity.addPart("w", new StringBody(w + "", Charset.forName(CHARSET_ENCODING)));
//
//            reqEntity.addPart("h", new StringBody(h + "", Charset.forName(CHARSET_ENCODING)));
//        } catch (UnsupportedEncodingException e) {
//            logger.error("", e);
//        }
//
//        String fileNameJson = HttpClientUtil.httpFileRequest(URL_UPLOAD_FILE_URL, reqEntity, null);
//
//        return fileNameJson;
//    }
//
//    public static String uploadImage(HttpServletRequest request, HttpServletResponse response,
//                                     String dirPath) throws ServletException, IOException {
//        request.setCharacterEncoding("utf-8"); // 设置编码
//        //PrintWriter writer = null;
//        InputStream is = null;
//        FileOutputStream fos = null;
//
//        /*try {
//        	writer = response.getWriter();
//        } catch (IOException ex) {
//        }*/
//
//        String fileName = request.getHeader("X-File-Name");
//
//        try {
//            is = request.getInputStream();
//
//            File file = new File(dirPath, fileName);
//
//            fos = new FileOutputStream(file);
//            IOUtils.copy(is, fos);
//
//            MultipartEntity reqEntity = new MultipartEntity();
//
//            reqEntity.addPart("file", new FileBody(file));
//
//            String fileNameJson = HttpClientUtil.httpFileRequest(FILE_UPLOAD_URL, reqEntity, null);
//
//            JsonVO jsonVO = new Gson().fromJson(fileNameJson, JsonVO.class);
//
//            file.delete();
//            String data = (String) jsonVO.getData();
//            //TODO 换标临时修改域名
//            data = data.replace("file.yunjee.com", "file.taotie.com");
//            return data;
//        } catch (FileNotFoundException ex) {
//            return null;
//        } catch (IOException ex) {
//            return null;
//        } finally {
//            try {
//                if (fos != null)
//                    fos.close();
//
//                if (is != null)
//                    is.close();
//
//            } catch (IOException ignored) {
//            }
//        }
//
//        //writer.flush();
//        //writer.close();
//
//    }
//
//}
