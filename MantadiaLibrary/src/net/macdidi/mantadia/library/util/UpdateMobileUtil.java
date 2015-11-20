package net.macdidi.mantadia.library.util;

import java.util.List;

import net.macdidi.mantadia.domain.DataCollection;
import net.macdidi.mantadia.domain.MenuItem;
import net.macdidi.mantadia.domain.MenuType;
import net.macdidi.mantadia.domain.Tables;
import net.macdidi.mantadia.library.db.MobileDB;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.Context;
import android.util.Log;

/**
 * 更新資料公用程式
 * 
 * @author macdidi
 */
public class UpdateMobileUtil {

    /**
     * 讀取伺服器的菜單資料後儲存到裝置
     * 
     * @param context Android Context物件 
     * @return 更新的資料筆數
     */
    public static int updateMenuItem(Context context) {
        int result = 0;
        // 請求菜單資料的URL
        String url = HttpClientUtil.MOBILE_URL
                + "UpdateMobileMenuItemServlet.do";
        // 傳送請求並取得回應的資料
        String data = HttpClientUtil.sendGet(url);
        
        // 準備從XML轉換為物件
        Serializer serializer = new Persister();

        try {
            // 將伺服器回應的XML資訊轉換為物件
            DataCollection<MenuItem> response = 
                    serializer.read(DataCollection.class, data);
            List<MenuItem> list = response.getList();
            
            if (list.size() > 0) {
            	// 建立資料庫存取元件
                MobileDB db = MobileDB.getMobileDB(context);
                // 刪除所有菜單資料
                db.deleteAllMenuItem();
                
                // 新增所有接收到的菜單資料
                for (MenuItem menuItem : list) {
                    db.insertMenuItem(menuItem.getId(), 
                            menuItem.getName(), 
                            menuItem.getPrice(), 
                            menuItem.getName(), 
                            menuItem.getMenuTypeId(),
                            menuItem.getMenuTypeName(),
                            menuItem.getImageFileName());
                }
                
                result = list.size();
            }
        }
        catch (Exception e) {
            Log.d("UpdateMobileUtil:updateMenuItem", e.toString());
        }
        
        return result;
    }

    /**
     * 讀取伺服器的菜單種類資料後儲存到裝置
     * 
     * @param context Android Context物件 
     * @return 更新的資料筆數
     */
    public static int updateMenuType(Context context) {
        int result = 0;
        // 請求菜單種類資料的URL
        String url = HttpClientUtil.MOBILE_URL
                + "UpdateMobileMenuTypeServlet.do";
        // 傳送請求並取得回應的資料
        String data = HttpClientUtil.sendGet(url);
        
        // 準備從XML轉換為物件
        Serializer serializer = new Persister();

        try {
            // 將伺服器回應的XML資訊轉換為物件
            DataCollection<MenuType> response = 
                    serializer.read(DataCollection.class, data);
            List<MenuType> list = response.getList();
            
            if (list.size() > 0) {
            	// 建立資料庫存取元件
                MobileDB db = MobileDB.getMobileDB(context);
                // 刪除所有菜單種類資料
                db.deleteAllMenuType();
                
                // 新增所有接收到的菜單種類資料
                for (MenuType menuType : list) {
                    db.insertMenuType(menuType.getId(), 
                            menuType.getName(), 
                            menuType.getNote());
                }
                
                result = list.size();
            }
        }
        catch (Exception e) {
            Log.d("UpdateMobileUtil:updateMenuType", e.toString());
        }
        
        return result;        
    }

    /**
     * 讀取伺服器的餐桌資料後儲存到裝置
     */
    public static int updateTables(Context context) {
        int result = 0;
        // 請求餐桌資料的URL
        String url = HttpClientUtil.MOBILE_URL
                + "UpdateMobileTablesServlet.do";
        // 傳送請求並取得回應的資料
        String data = HttpClientUtil.sendGet(url);
        
        // 準備從XML轉換為物件
        Serializer serializer = new Persister();

        try {
            // 將伺服器回應的XML資訊轉換為物件
            DataCollection<Tables> response = 
                    serializer.read(DataCollection.class, data);
            List<Tables> list = response.getList();
            
            if (list.size() > 0) {
            	// 建立資料庫存取元件
                MobileDB db = MobileDB.getMobileDB(context);
                // 刪除所有餐桌資料
                db.deleteAllTables();
                
                // 新增所有接收到的餐桌資料
                for (Tables tables : list) {
                    db.insertTables(tables.getId(), tables.getNote());
                }
                
                result = list.size();
            }
        }
        catch (Exception e) {
            Log.d("UpdateMobileUtil:updateTables", e.toString());
        }
        
        return result;
    }

}
