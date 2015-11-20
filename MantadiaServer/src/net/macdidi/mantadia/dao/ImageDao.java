package net.macdidi.mantadia.dao;

import java.util.List;

import net.macdidi.mantadia.domain.Image;

/**
 * 圖片DAO介面
 * 
 * @author macdidi
 */
public interface ImageDao {
    /**
     * 新增圖片
     * 
     * @param image 新增圖片物件
     * @return 新增的圖片物件編號
     */
    public int add(Image image);

    /**
     * 刪除圖片
     * 
     * @param image 刪除圖片物件
     * @return 是否刪除成功 
     */
    public boolean delete(Image image);

    /**
     * 修改圖片
     *
     * @param image 修改圖片物件
     * @return 是否修改成功 
     */
    public boolean update(Image image);
    
    /**
     * 取得所有圖片
     * 
     * @return 包含所有圖片的List物件
     */
    public List<Image> getAll();

    /**
     * 取得指定編號的圖片
     * 
     * @param id 圖片物件編號
     * @return 圖片物件
     */
    public Image get(int id);
}
