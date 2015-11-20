package net.macdidi.mantadia.dao;

import java.util.List;

import net.macdidi.mantadia.domain.Tables;

/**
 * 餐桌DAO介面
 * 
 * @author macdidi
 */
public interface TablesDao {
    /**
     * 新增餐桌
     * 
     * @param tables 新增餐桌物件
     * @return 新增的餐桌物件編號
     */
    public int add(Tables tables);

    /**
     * 刪除餐桌
     * 
     * @param tables 刪除餐桌種類物件
     * @return 是否刪除成功 
     */
    public boolean delete(Tables tables);

    /**
     * 修改餐桌
     * 
     * @param tables 修改餐桌種類物件
     * @return 是否修改成功 
     */
    public boolean update(Tables tables);

    /**
     * 取得所有餐桌
     * 
     * @return 包含所有餐桌的List物件
     */
    public List<Tables> getAll();

    /**
     * 取得指定編號的餐桌
     * 
     * @param id 餐桌物件編號
     * @return 餐桌物件，如果指定的編號不存在傳回null
     */
    public Tables get(int id);

    /**
     * 取得所有餐桌資訊
     * 
     * @return 包含所有餐桌資訊的List物件
     */
    public List<Tables> getAllStatus();

    /**
     * 取得可以換桌的餐桌資訊
     * 
     * @return 包含所有可以換桌的餐桌資訊的List物件
     */
    public List<Tables> getTablesChange();

    /**
     * 取得有客人的餐桌資訊
     * 
     * @return 包含所有有客人的餐桌資訊的List物件
     */
    public List<Tables> getTablesNotEmpty();

    /**
     * 取得沒有客人的餐桌
     * 
     * @return 包含所有有客人的餐桌資訊的List物件
     */
    public List<Tables> getTablesEmpty();

    /**
     * 設定餐桌狀態
     * 
     * @param id 餐桌物件編號
     * @param status 餐桌狀態，0：空桌，1:有客人
     */
    public void setTablesStatus(int id, int status);
}
