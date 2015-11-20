package net.macdidi.mantadia.dao;

import java.util.List;

import net.macdidi.mantadia.domain.MenuType;

/**
 * 菜單種類DAO介面
 * 
 * @author macdidi
 */
public interface MenuTypeDao {
    /**
     * 新增菜單種類
     * 
     * @param menuType 新增菜單種類物件
     * @return 新增的菜單種類物件編號
     */
    public int add(MenuType menuType);

    /**
     * 刪除菜單種類
     * 
     * @param menuType 刪除菜單種類物件
     * @return 是否刪除成功 
     */
    public boolean delete(MenuType menuType);

    /**
     * 修改菜單種類
     * 
     * @param menuType 修改菜單種類物件
     * @return 是否修改成功 
     */
    public boolean update(MenuType menuType);

    /**
     * 取得所有菜單種類
     * 
     * @return 包含所有菜單種類的List物件
     */
    public List<MenuType> getAll();

    /**
     * 取得指定編號的菜單種類
     * 
     * @param id 菜單種類物件編號
     * @return 菜單種類物件，如果指定的編號不存在傳回null
     */
    public MenuType get(int id);
}
