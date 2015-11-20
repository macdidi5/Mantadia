package net.macdidi.mantadia.emenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SelectedMenuItemView extends RelativeLayout {

    // 菜單編號、圖片檔案名稱與圖形元件
    private int menuItemId;
    private String fileName;
    private ImageView imageView;
    
    public SelectedMenuItemView(Context context) {
        super(context);
        // 載入畫面配置資源
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.selected_menu_item, this);
    }
    
    public void setData(int menuItemId, String fileName) {
        this.menuItemId = menuItemId;
        this.fileName = fileName;
        // 取得圖形元件
        imageView = (ImageView) findViewById(R.id.selected_menu_item_image);
        // 設定已選擇菜單的圖片縮圖
        MenuActivity.pictureToImageView(fileName, imageView, 160, 160);
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public String getFileName() {
        return fileName;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
    
}
