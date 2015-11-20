package net.macdidi.mantadia.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.sql.DataSource;

import net.macdidi.mantadia.domain.Image;
import net.macdidi.mantadia.util.ImageUtil;

/**
 * DAO實作
 * 
 * @author macdidi
 */
@Named
@ApplicationScoped
public class ImageDaoImpl implements ImageDao {

    @Resource(name = "jdbc/mantadiaDB")
    private DataSource dataSource;    

    /**
     * 新增圖片
     * 
     * @param image 新增圖片物件
     * @return 新增的圖片物件編號
     */
    @Override
    public int add(Image image) {
        // 新增圖片資料的SQL敘述
        String sql = "INSERT INTO image VALUES(0, ?, ?)";
        int result = -1;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            // 設定檔案名稱與備註
            pstmt.setString(1, image.getFileName());
            pstmt.setString(2, image.getNote());

            // 執行新增
            int rc = pstmt.executeUpdate();

            if (rc > 0) {
                // 讀取資料庫產生的編號
                ResultSet rs = pstmt.getGeneratedKeys();

                if (rs.next()) {
                    result = rs.getInt(1);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // 回傳編號
        return result;
    }

    /**
     * 刪除圖片
     * 
     * @param image 刪除圖片物件
     * @return 是否刪除成功 
     */
    @Override
    public boolean delete(Image image) {
        // 刪除圖片資料的SQL敘述
        String sql = "DELETE FROM image WHERE id=?";
        boolean result = false;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定圖片編號
            pstmt.setInt(1, image.getId());
            // 執行刪除
            int rc = pstmt.executeUpdate();

            if (rc > 0) {
                // 刪除圖片
                deleteImageFile(image);
                result = true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 修改圖片
     *
     * @param image 修改圖片物件
     * @return 是否修改成功 
     */
    @Override
    public boolean update(Image image) {
        // 取得指定編號的圖片物件
        Image oldImage = get(image.getId());
        // 修改圖片資料的SQL敘述
        String sql = "UPDATE image SET filename=?, note=? WHERE id=?";
        boolean result = false;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定圖片檔案名稱、備註與編號
            pstmt.setString(1, image.getFileName());
            pstmt.setString(2, image.getNote());
            pstmt.setInt(3, image.getId());

            // 執行更新
            int rc = pstmt.executeUpdate();

            if (!oldImage.getFileName().equals(image.getFileName())) {
                // 刪除圖片檔案
                deleteImageFile(oldImage);
            }

            if (rc > 0) {
                result = true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void deleteImageFile(Image image) {
        // 建立要刪除的原始檔案物件
        File imageFile = new File(
                ImageUtil.UPLOAD_FOLDER, image.getFileName());
        // 建立要刪除的縮檔案物件
        File smallImageFile = new File(ImageUtil.UPLOAD_FOLDER, "s"
                + image.getFileName());

        if (imageFile.exists()) {
            // 刪除原始檔案
            imageFile.delete();
        }

        if (smallImageFile.exists()) {
            // 刪除縮圖檔案
            smallImageFile.delete();
        }
    }

    /**
     * 取得所有圖片
     * 
     * @return 包含所有圖片的List物件
     */
    @Override
    public List<Image> getAll() {
        // 讀取圖片資料的SQL敘述
        String sql = "SELECT * FROM image";
        ArrayList<Image> result = new ArrayList<Image>();

        try (Connection con = dataSource.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // 讀取圖片編號、檔案名稱與備註
                int id = rs.getInt(1);
                String fileName = rs.getString(2);
                String note = rs.getString(3);

                // 建立圖片物件
                Image image = new Image(id, fileName, note);
                result.add(image);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 取得指定編號的圖片
     * 
     * @param id 圖片物件編號
     * @return 圖片物件，如果指定的編號不存在就傳回null
     */
    @Override
    public Image get(int id) {
        // 讀取圖片資料的SQL敘述
        String sql = "SELECT * FROM image WHERE id=?";
        Image result = null;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定圖片編號
            pstmt.setInt(1, id);
            // 執行查詢
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // 讀取檔案名稱與備註
                String fileName = rs.getString(2);
                String note = rs.getString(3);

                // 建立回傳的圖片物件
                result = new Image(id, fileName, note);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

}
