package net.macdidi.mantadia.emenu;

import java.io.File;
import java.util.List;

import net.macdidi.mantadia.domain.OrderItem;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrderItemAdapter extends ArrayAdapter<OrderItem> {

    // 畫面配置檔資源編號
    private int resource;
    // 訂單明細物件資料
    private List<OrderItem> items;
    // Activity元件
    private OrderInfoCallBack callBack;
    
    public OrderItemAdapter(Context  context, int resource, 
            List<OrderItem> items, OrderInfoCallBack callBack) {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
        this.callBack = callBack;
    }
    
    @Override
    public int getCount() {
        return items.size();
    }
    
    public List<OrderItem> getItems() {
        return items;
    }
    
    @Override
    public View getView(final int position, View convertView, 
            ViewGroup parent) {
        RelativeLayout orderItemView;
        // 取得指定位置編號的訂單明細物件
        final OrderItem item = getItem(position);
        
        if (convertView == null) {
            // 建立畫面配置元件
            orderItemView = new RelativeLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater) 
                    getContext().getSystemService(inflater);
            // 載入畫面配置資源
            li.inflate(resource, orderItemView, true);
        }
        else {
            orderItemView = (RelativeLayout) convertView;
        }
        
        // 取得菜單圖片、名稱、數量、刪除、增加與減少數量元件
        ImageView order_confirm_image = (ImageView)
                orderItemView.findViewById(R.id.order_confirm_image);
        TextView order_confirm_name = (TextView)
                orderItemView.findViewById(R.id.order_confirm_name);
        final TextView order_confirm_number = (TextView)
                orderItemView.findViewById(R.id.order_confirm_number);
        ImageButton order_confirm_delete = (ImageButton)
                orderItemView.findViewById(R.id.order_confirm_delete);
        ImageButton order_confirm_add = (ImageButton)
                orderItemView.findViewById(R.id.order_confirm_add);
        ImageButton order_confirm_minus = (ImageButton)
                orderItemView.findViewById(R.id.order_confirm_minus);

        // 儲存圖片檔案的目錄
        String imageDir = new File(
                Environment.getExternalStorageDirectory(), 
                MantadiaEMenuActivity.PICTURE_FOLDER).getAbsolutePath();
        // 設定菜單的圖片縮圖
        MenuActivity.pictureToImageView(
                imageDir + "/" + item.getImageFileName(), 
                order_confirm_image, 160, 160);
        // 設定菜單名稱與數量
        order_confirm_name.setText(item.getMenuItemName());
        order_confirm_number.setText(Integer.toString(item.getNumber()));
        
        // 增加與減少數量按鈕的監聽物件
        OnClickListener numberListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 讀取目前數量
                int number = Integer.parseInt(
                        order_confirm_number.getText().toString());
                int id = view.getId();
                
                // 增加數量
                if (id == R.id.order_confirm_add) {
                    number++;
                }
                // 減少數量
                else if (id == R.id.order_confirm_minus) {
                    if (number > 1) {
                        number --;
                    }
                }
                
                // 設定數量
                order_confirm_number.setText(Integer.toString(number));
                item.setNumber(number);
                
                // 呼叫Activity元件重新計算訂單金額的方法
                callBack.processOrderInfo();
            }
        };
        
        // 註冊增加與減少數量按鈕
        order_confirm_add.setOnClickListener(numberListener);
        order_confirm_minus.setOnClickListener(numberListener);
        
        // 註冊刪除菜單按鈕
        order_confirm_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                items.remove(position);
                notifyDataSetChanged();
                // 呼叫Activity元件重新計算訂單金額的方法
                callBack.processOrderInfo();
            }
        });
        
        return orderItemView;
    }
    
    // 與Activity元件互動用的介面
    public interface OrderInfoCallBack {
        public void processOrderInfo();
    }

}
