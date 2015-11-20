package net.macdidi.mantadia.library.db;

import java.util.ArrayList;
import java.util.List;

import net.macdidi.mantadia.domain.MenuItem;
import net.macdidi.mantadia.domain.MenuType;
import net.macdidi.mantadia.domain.Tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 資料庫類別
 * @author macdidi
 */
public class MobileDB {
    
	// 資料庫名稱
	private static final String DATABASE_NAME = "mantadia.db";
	// 菜單、菜單種類與餐桌表格名稱
    private static final String MENU_ITEM_NAME = "menuitem";
    private static final String MENU_TYPE_NAME = "menutype";
    private static final String TABLES_NAME = "tables";
    
    // 資料庫版本編號，調整資料庫架構的時候遞增這個數字
    private static final int VERSION = 1;
    
    // 建立菜單表格敘述
    private static final String CREATE_MENU =
            "CREATE TABLE " + MENU_ITEM_NAME + " (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "price LONG NOT NULL, " + 
            "note TEXT, " + 
            "menutypeid INTEGER, " +
            "menutypename TEXT, " + 
            "imagefilename TEXT);";

    // 建立菜單種類表格敘述
    private static final String CREATE_MENU_TYPE =
            "CREATE TABLE " + MENU_TYPE_NAME + " (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "note TEXT);";
    
    // 建立餐桌表格敘述
    private static final String CREATE_TABLES =
            "CREATE TABLE " + TABLES_NAME + " (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "note TEXT);";  
    
    private SQLiteDatabase db;
    private MobileHelper mobileHelper;
    
    private static MobileDB mobileDB = null;
    
    // 菜單欄位
    private static final String[] MENU_ITEM_COLUMNS = 
        {"_id", "name", "price", "note", "menutypeid", 
         "menutypename", "imagefilename"};
    
    // 菜單種類欄位
    private static final String[] MENU_TYPE_COLUMNS = 
        {"_id", "name", "note"};
    
    private MobileDB(Context context) {
    	mobileHelper = new MobileHelper(
    	        context, DATABASE_NAME, null, VERSION);
        db = mobileHelper.getWritableDatabase();
    }    
    
    /**
     * 取得資料庫物件
     * 
     * @param context Android Context物件 
     * @return 資料庫元件物件
     */
    public static MobileDB getMobileDB(Context context) {
        if (mobileDB == null) {
        	mobileDB = new MobileDB(context);
        }

        if (!mobileDB.db.isOpen()) {
        	mobileDB.db = mobileDB.mobileHelper.getWritableDatabase();
        }
        
        return mobileDB;
    }    
    
    /**
     * 關閉資料庫物件
     */
    public void close() {
        db.close();
    }
    
    /**
     * 新增菜單
     * 
     * @param id 編號
     * @param name 名稱
     * @param price 價格
     * @param note 備註
     * @param menuTypeId 菜單種類編號
     * @param menuTypeName 菜單種類名稱
     * @param imageFileName 圖片檔案名稱
     * @return 新增資料的編號
     */
    public long insertMenuItem(int id, String name, int price, String note, 
            int menuTypeId, String menuTypeName, String imageFileName) {
        ContentValues cv = new ContentValues();
        
        cv.put("_id", id);
        cv.put("name", name);
        cv.put("price", price);
        cv.put("note", note);
        cv.put("menutypeid", menuTypeId);
        cv.put("menutypename", menuTypeName);
        cv.put("imagefilename", imageFileName);        
        
        return db.insert(MENU_ITEM_NAME, null, cv);
    }        

    /**
     * 刪除所有菜單
     * 
     * @return 是否刪除成功
     */
    public boolean deleteAllMenuItem(){
        return db.delete(MENU_ITEM_NAME, null , null) > 0;
    }
    
    /**
     * 取得所有菜單
     * 
     * @return 包含所有菜單資料的List物件
     */
    public List<MenuItem> getAllMenuItem() {
        List<MenuItem> items = new ArrayList<MenuItem>();
        // 查詢所有菜單資料
        Cursor result = db.query(MENU_ITEM_NAME, MENU_ITEM_COLUMNS, 
                null, null, null, null, null);
        
        if (result.moveToFirst()) {
            do {
            	// 讀取並建立菜單物件
                MenuItem item = cursorToMenuItem(result);
                items.add(item);
            } while (result.moveToNext());
            
            result.close();
        }
        
        return items;
    }

    /**
     * 取得指定種類的菜單
     * 
     * @param menuTypeId 菜單種類編號
     * @return 包含所有定種類的菜單資料的List物件
     */
    public List<MenuItem> getMenuItemByType(int menuTypeId) {
        List<MenuItem> items = new ArrayList<MenuItem>();
        String whereStr = "menutypeid=" + menuTypeId;
        Cursor result = db.query(MENU_ITEM_NAME, MENU_ITEM_COLUMNS, 
                whereStr, null, null, null, null);
        
        if (result.moveToFirst()) {
            do {
                MenuItem item = cursorToMenuItem(result);
                items.add(item);
            } while (result.moveToNext());
            
            result.close();
        }
        
        return items;
    }
    
    /**
     * 取得指定編號的菜單
     * 
     * @param id 菜單編號
     * @return 指定編號的菜單物件，如果沒有這個編號傳回null
     */
    public MenuItem getMenuItem(int id) {
        MenuItem item = null;
        String whereStr = "_id=" + id;
        // 查詢指定編號的菜單資料
        Cursor result = db.query(MENU_ITEM_NAME, MENU_ITEM_COLUMNS, 
                whereStr, null, null, null, null);
        
        if (result.moveToFirst()) {
            do {
            	// 讀取與建立菜單物件
                item = cursorToMenuItem(result);
            } while (result.moveToNext());
            
            result.close();
        }
        
        return item;
    }    
    
    // 讀取菜單資料並傳回菜單物件
    private MenuItem cursorToMenuItem(Cursor result) {
        MenuItem item = new MenuItem();
        
        item.setId(result.getInt(0));
        item.setName(result.getString(1));
        item.setPrice(result.getInt(2));
        item.setNote(result.getString(3));
        item.setMenuTypeId(result.getInt(4));
        item.setMenuTypeName(result.getString(5));
        item.setImageFileName(result.getString(6));
        
        return item;
    }
        
    /**
     * 新增菜單種類
     * 
     * @param id 編號
     * @param name 名稱
     * @param note 備註
     * @return 新增資料的編號
     */
    public long insertMenuType(int id, String name, String note) {
        ContentValues cv = new ContentValues();
        
        cv.put("_id", id);
        cv.put("name", name);
        cv.put("note", note);       
        
        return db.insert(MENU_TYPE_NAME, null, cv);
    }  
    
    /**
     * 刪除所有菜單種類
     * 
     * @return 是否刪除成功
     */
	public boolean deleteAllMenuType(){
	    return db.delete(MENU_TYPE_NAME, null , null) > 0;
	}     

	/**
     * 取得所有菜單種類
     * 
     * @return 包含所有菜單種類資料的List物件
     */
	public List<MenuType> getAllMenuType() {
	    List<MenuType> items = new ArrayList<MenuType>();
		// 查詢所有菜單種類資料
        Cursor result = db.query(MENU_TYPE_NAME, 
                MENU_TYPE_COLUMNS, null, null, null, null, null);
        
        if (result.moveToFirst()) {
            do {
            	// 讀取並建立菜單種類物件
                MenuType item = new MenuType();
                
                item.setId(result.getInt(0));
                item.setName(result.getString(1));
                item.setNote(result.getString(2));
                
                items.add(item);
            } while (result.moveToNext());
            
            result.close();
        }
        
        return items;
	}
	
    /**
     * 新增餐桌
     * 
     * @param id 編號
     * @param note 備註
     * @return 新增資料的編號
     */
    public long insertTables(int id, String note) {
        ContentValues cv = new ContentValues();
        
        cv.put("_id", id);
        cv.put("note", note);       
        
        return db.insert(TABLES_NAME, null, cv);
    }    
       
    /**
     * 刪除餐桌
     * 
     * @return 是否刪除成功
     */
    public boolean deleteAllTables(){
        return db.delete(TABLES_NAME, null , null) > 0;
    }    

    /**
     * 取得所有餐桌
     * 
     * @return 包含所有餐桌資料的List物件
     */
    public List<Tables> getAllTables() {
        List<Tables> items = new ArrayList<Tables>();
        // 查詢所有餐桌資料
        Cursor result = db.query(MENU_TYPE_NAME, 
                MENU_TYPE_COLUMNS, null, null, null, null, null);
        
        if (result.moveToFirst()) {
            do {
            	// 讀取並建立餐桌物件
                Tables item = new Tables();
                
                item.setId(result.getInt(0));
                item.setNote(result.getString(1));
                
                items.add(item);
            } while (result.moveToNext());
            
            result.close();
        }
        
        return items;
    }
    
    // SQLite資料庫輔助類別
    private static class MobileHelper extends SQLiteOpenHelper {
    	public MobileHelper(Context context, String name, 
    	        CursorFactory factory, int version){
            super(context, name, factory, version);
        }
        
        @Override
        public void onCreate(SQLiteDatabase db) {
        	// 建立菜單、菜單種類與餐桌表格
            db.execSQL(CREATE_MENU);
            db.execSQL(CREATE_MENU_TYPE);
            db.execSQL(CREATE_TABLES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
                int newVersion) {
        	// 刪除菜單、菜單種類與餐桌表格
            db.execSQL("DROP TABLE IF EXISTS " + MENU_ITEM_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + MENU_TYPE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLES_NAME);

            onCreate(db);
        }
    }
    
}
