package net.macdidi.mantadia.dao;

import java.util.List;

import net.macdidi.mantadia.domain.MenuItem;

/**
 * 菜單DAO介面
 * 
 * @author macdidi
 */
public interface MenuItemDao {
    /**
     * 新增菜單
     * 
     * @param menuItem 新增菜單物件
     * @return 新增的菜單物件編號
     */
    public int add(MenuItem menuItem);

    /**
     * 刪除菜單
     * 
     * @param menuItem 刪除菜單物件
     * @return 是否刪除成功 
     */
    public boolean delete(MenuItem menuItem);

    /**
     * 修改菜單
     *
     * @param menuItem 修改菜單物件
     * @return 是否修改成功 
     */
    public boolean update(MenuItem menuItem);

    /**
     * 取得所有菜單
     * 
     * @return 包含所有菜單的List物件
     */
    public List<MenuItem> getAll();

    /**
     * 取得指定編號的菜單
     * 
     * @param id 菜單物件編號
     * @return 菜單物件，如果指定的編號不存在傳回null
     */
    public MenuItem get(int id);
}
