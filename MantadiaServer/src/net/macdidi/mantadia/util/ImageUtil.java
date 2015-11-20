package net.macdidi.mantadia.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;

/**
 * 圖片公用類別
 * 
 * @author macdidi
 */
public class ImageUtil {
    /**
     * 儲存上傳檔案的路徑
     */
    public static final String UPLOAD_FOLDER = 
            "C:\\Mantadia\\uploads";

    /**
     * 取得日期時間的檔名
     * 
     * @param fn 原始檔案名稱
     * @return 產生的檔案名稱
     */
    public static String getUniqueName(String fn) {
        // 上傳檔案名稱中「.」的位置
        int dot = fn.lastIndexOf(".");
        String sn = "";

        if (dot != -1) {
            // 取得上傳檔案名稱的副檔名
            sn = fn.substring(dot);
        }

        // 建立產生檔案名稱的日期物件
        Date now = new Date();
        // 設定檔案名稱格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(now) + sn;
    }

    /**
     * 儲存原圖20%的縮圖
     * 
     * @param of 原始圖片檔案
     * @param sf 縮圖檔案
     */
    public static void saveSmallImage(File of, File sf) {
        if (of.exists()) {
            try {
                // 讀取原始檔案
                BufferedImage original = ImageIO.read(of);
                // 取得20%的圖片
                BufferedImage small = reduce(original, 0.2);
                // 儲存縮圖
                ImageIO.write(small, "jpg", sf);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取得縮圖
     */
    private static BufferedImage reduce(BufferedImage original, double scale) {
        // 計算縮圖的寬與高
        int w = new Double(original.getWidth() * scale).intValue();
        int h = new Double(original.getHeight() * scale).intValue();
        // 建立指定寬與高的圖片物件
        Image rescaled = original.getScaledInstance(
                w, h, Image.SCALE_AREA_AVERAGING);
        // 建立指定寬與高的圖片暫存區
        BufferedImage result = new BufferedImage(
                w, h, BufferedImage.TYPE_INT_RGB);
        // 產生縮圖物件
        Graphics2D g = result.createGraphics();
        g.drawImage(rescaled, 0, 0, null);
        g.dispose();
        return result;
    }

    /**
     * 取得上傳檔案的名稱
     * 
     * @param part Multipart檔案物件
     * @return 上傳檔案的名稱
     */
    public static String extractFileName(Part part) {
        // 讀取HTTP Header中的資訊
        String contentDisp = part.getHeader("content-disposition");
        // 使用「;」拆解
        String[] items = contentDisp.split(";");
        
        for (String s : items) {
            // 是否為上傳檔案名稱的HTTP Header資訊 
            if (s.trim().startsWith("filename")) {
                // 回傳上傳檔案名稱
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        
        return "";
    }

}
