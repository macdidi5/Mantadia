package net.macdidi.mantadia.emenu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.macdidi.mantadia.domain.MenuItem;
import net.macdidi.mantadia.domain.MenuType;
import net.macdidi.mantadia.library.db.MobileDB;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends Activity {
    
    // 桌號、菜單種類名稱與菜單種類元件
    private TextView tables_id;
    private TextView menu_type_name;
    private ListView menu_type_list;
    
    // 瀏覽菜單與已選擇菜單元件
    private LinearLayout menu_item_gallery, selected_menu_item_gallery;
    
    // 菜單種類資料
    private List<String> menuTypes;
    // 已選擇菜單資料
    private ArrayList<MenuItem> selectedMenuItem;
    // 桌號與訂單號碼
    private int tablesId;
    private int ordersNumber;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        // 設定執行緒運作模式
        StrictMode.ThreadPolicy policy = 
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);        
        
        // 讀取餐桌編號與人數資料
        Intent intent = getIntent();
        tablesId = intent.getIntExtra("tablesId", -1);
        ordersNumber = intent.getIntExtra("ordersNumber", 0);
        
        processViews();
        processControllers();
        processModel();        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK, getIntent());
            finish();
        }
    }

    // 確定按鈕
    public void clickMenuOk(View view) {
        // 如果有加入菜單
        if (selectedMenuItem.size() > 0) {
            // 啟動確認訂單元件
            Intent intent = new Intent(this, ConfirmOrderActivity.class);
            // 設定選擇的菜單資料
            intent.putExtra("java.util.ArrayList", selectedMenuItem);
            // 設定傳送的桌號與人數資料
            intent.putExtra("tablesId", tablesId);
            intent.putExtra("ordersNumber", ordersNumber);
            startActivityForResult(intent , 0);
        }
        // 如果沒有加入菜單
        else {
            Toast.makeText(MenuActivity.this, 
                    getString(R.string.menu_add_menuitem_please_txt), 
                    Toast.LENGTH_SHORT).show();
        }
    }
    
    private void processViews() {
        tables_id = (TextView) findViewById(R.id.tables_id);
        menu_type_name = (TextView) findViewById(R.id.menu_type_name);
        menu_type_list = (ListView) findViewById(R.id.menu_type_list);
        menu_item_gallery = (LinearLayout) 
                findViewById(R.id.menu_item_gallery);
        selected_menu_item_gallery = (LinearLayout) 
                findViewById(R.id.selected_menu_item_gallery);
        tables_id.setText(Integer.toString(tablesId));
    }
    
    private void processControllers() {
        menu_type_list.setOnItemClickListener(new MenuTypeListener());
    }
    
    private void processModel() {
        // 儲存選擇菜單用的List物件
        selectedMenuItem = new ArrayList<MenuItem>();
        
        // 建立資料庫物件
        MobileDB db = MobileDB.getMobileDB(MenuActivity.this);

        // 取得所有菜單種類
        List<MenuType> types = db.getAllMenuType();
        menuTypes = new ArrayList<String>();

        for (MenuType data : types) {
            menuTypes.add(data.getId() + " ~ " + data.getName());
        }
        
        // 設定菜單種類資料給ListView元件
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
                R.layout.select_table_view, menuTypes);
        menu_type_list.setAdapter(typeAdapter);
        
        processMenuType(0);
    }
    
    // 選擇菜單種類監聽類別
    private class MenuTypeListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, 
                int position, long id) {
            processMenuType(position);
        }
    }
    
    private void processMenuType(int position) {
        // 移除所有菜單
        menu_item_gallery.removeAllViews();
        
        // 建立資料庫物件
        MobileDB db = MobileDB.getMobileDB(MenuActivity.this);
        
        // 儲存圖片的目錄
        String imageDir = new File(
                Environment.getExternalStorageDirectory(), 
                MantadiaEMenuActivity.PICTURE_FOLDER).getAbsolutePath();
        // 讀取選擇的菜單種類
        String item = menuTypes.get(position);
        String[] menuTypeData = item.split(" ~ ");
        int menuTypeId = Integer.parseInt(menuTypeData[0]);
        menu_type_name.setText(menuTypeData[1]);
        
        // 取得指定菜單種類的菜單
        List<MenuItem> items = db.getMenuItemByType(menuTypeId);
        
        processMenuItem(items, imageDir);   
        menu_item_gallery.invalidate();        
    }
    
    // 建立與設定菜單
    private void processMenuItem(List<MenuItem> items, String imageDir) {
        // 建立菜單項目使用的監聽物件
        AddMenuItemListener listener = new AddMenuItemListener();
        
        for (MenuItem item: items) {
            // 建立菜單元件
            MenuItemView menuItemView = new MenuItemView(MenuActivity.this);
            // 註冊選擇菜單
            menuItemView.setOnClickListener(listener);
            // 設定菜單元件的菜單編號與圖片檔案名稱
            menuItemView.setData(item.getId(), item.getName(), 
                    imageDir + "/" + item.getImageFileName());
            // 加入菜單元件
            menu_item_gallery.addView(menuItemView);
        }        
    }    
    
    // 建立菜單項目監聽類別
    private class AddMenuItemListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            // 取得與建立使用者選擇的菜單物件
            MenuItemView menuItemView = (MenuItemView)view;
            MenuItem menuItem = new MenuItem();
            menuItem.setId(menuItemView.getMenuItemId());
            
            // 如果還沒有選擇
            if (!selectedMenuItem.contains(menuItem)) {
                // 建立與設定已選擇的菜單元件
                SelectedMenuItemView selectedMenuItemView = 
                        new SelectedMenuItemView(MenuActivity.this);
                selectedMenuItemView.setData(menuItemView.getMenuItemId(), 
                        menuItemView.getFileName());
                // 加入已選擇菜單元件
                selected_menu_item_gallery.addView(selectedMenuItemView);
                selectedMenuItem.add(menuItem);
            }
            // 已經選擇
            else {
                Toast.makeText(MenuActivity.this, 
                        getString(R.string.menu_added_already_txt), 
                        Toast.LENGTH_SHORT).show();
            }
        }
        
    }
    
    // 讀取第一個參數指定的照片檔案，轉換為第二個ImageView元件的大小後，
    // 載入並顯示，在顯示大型照片檔案的時候，可以節省很多資源
    public static void pictureToImageView(String fileName, 
            ImageView imageView) {
        // 取得ImageView元件在畫面上的寬與高
        int targetWidth = imageView.getWidth();
        int targetHeight = imageView.getHeight();
    
        pictureToImageView(fileName, imageView, targetWidth, targetHeight);
    }
    
    // 把圖片轉換為圖形元件的大小
    public static void pictureToImageView(String fileName, 
            ImageView imageView, int targetWidth, int targetHeight) {
        // 建立調整照片用的Options物件
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 設定為只讀取大小的資訊
        options.inJustDecodeBounds = true;
        // 讀取指定照片檔案的資訊到Options物件
        BitmapFactory.decodeFile(fileName, options);
    
        // 取得照片檔案的寬與高
        int pictureWidth = options.outWidth;
        int pictureHeight = options.outHeight;
        // 比較ImageView和照片的大小後計算縮小比例
        int scaleFactor = Math.min(pictureWidth / targetWidth,
                pictureHeight / targetHeight);
    
        // 取消只讀取大小的資訊的設定
        options.inJustDecodeBounds = false;
        // 設定縮小比例
        options.inSampleSize = scaleFactor;
        // 設定為可以notyet
        options.inPurgeable = true;
    
        // 使用建立好的設定載入照片檔案
        Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);
        // 設定給參數指定的ImageView元件
        imageView.setImageBitmap(bitmap);        
    }

}
