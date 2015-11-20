package net.macdidi.mantadia.emenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuItemView extends RelativeLayout {

    // 菜單編號與圖片檔案名稱
    private int menuItemId;
    private String fileName;
    
    public MenuItemView(Context context) {
        super(context);
        // 載入畫面配置資源
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.menu_item, this);
    }
    
    public void setData(int menuItemId, String name, String fileName) {
        this.menuItemId = menuItemId;
        this.fileName = fileName;
        
        // 讀取與設定菜單名稱
        TextView menu_item_name = (TextView) 
                findViewById(R.id.menu_item_name);
        menu_item_name.setText(name);
        
        // 取得與設定圖形元件
        ImageView menu_item_image = (ImageView) 
                findViewById(R.id.menu_item_image);
        Bitmap bitmap = BitmapFactory.decodeFile(fileName);
        menu_item_image.setImageBitmap(bitmap);
    }
    
    public int getMenuItemId() {
        return menuItemId;
    }

    public String getFileName() {
        return fileName;
    }
    
}
