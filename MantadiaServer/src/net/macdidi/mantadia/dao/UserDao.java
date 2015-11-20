package net.macdidi.mantadia.dao;

import java.util.List;

import net.macdidi.mantadia.domain.User;

/**
 * 使用者DAO介面
 * 
 * @author macdidi
 */
public interface UserDao {
    /**
     * 新增使用者
     * 
     * @param tables 新增使用者物件
     * @return 新增的使用者物件編號
     */
    public int add(User user);

    /**
     * 刪除使用者
     * 
     * @param user 刪除使用者物件
     * @return 是否刪除成功 
     */
    public boolean delete(User user);

    /**
     * 修改使用者
     * 
     * @param user 修改使用者物件
     * @return 是否修改成功 
     */
    public boolean update(User user);

    /**
     * 取得所有使用者
     * 
     * @return 包含所有使用者的List物件
     */
    public List<User> getAll();

    /**
     * 取得指定編號的使用者
     * 
     * @param id 使用者物件編號
     * @return 使用者物件，如果指定的編號不存在傳回null
     */
    public User get(int id);

    /**
     * 取得使用者,使用帳號與密碼
     * 
     * @param account 帳號
     * @param password 密碼
     * @return 使用者物件，如果帳號不存在或密碼不對傳回null
     */
    public User get(String account, String password);
}
