package net.macdidi.mantadia.emenu;

import java.util.ArrayList;
import java.util.List;

import net.macdidi.mantadia.domain.MenuItem;
import net.macdidi.mantadia.domain.OrderItem;
import net.macdidi.mantadia.domain.Orders;
import net.macdidi.mantadia.library.db.MobileDB;
import net.macdidi.mantadia.library.util.HttpClientUtil;
import net.macdidi.mantadia.library.util.TurtleUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmOrderActivity extends Activity 
        implements OrderItemAdapter.OrderInfoCallBack {
    
    // 訂單明細元件
    private ListView order_list;
    private TextView order_info;
    
    // 已選擇菜單資料
    private ArrayList<MenuItem> menuItem;
    private OrderItemAdapter orderItemAdapter;
    
    // 桌號與人數
    private int tablesId;
    private int ordersNumber;
    
    // 訂單與訂單明細資料
    private Orders orders;
    private List<OrderItem> orderItems;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_order);
        
        // 設定執行緒運作模式
        StrictMode.ThreadPolicy policy = 
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        // 讀取已經選擇的菜單資料、桌號與人數
        Intent intent  = getIntent();
        menuItem = (ArrayList<MenuItem>)
                intent.getExtras().getSerializable("java.util.ArrayList");
        tablesId = intent.getIntExtra("tablesId", -1);
        ordersNumber = intent.getIntExtra("ordersNumber", 0);
        
        processViews();
        processModel();
        
        // 計算與顯示訂單金額
        processOrderInfo();
    }
    
    public void clickConfirmOrder(View view) {
        // 儲存訂單
        processOrder();
        // 顯示訂單已傳送
        Toast.makeText(this, getString(R.string.confirm_order_done_txt), 
                Toast.LENGTH_LONG).show();
        setResult(Activity.RESULT_OK, getIntent());
        finish();
    }
    
    private void processViews() {
        order_list = (ListView) findViewById(R.id.order_list);
        order_info = (TextView) findViewById(R.id.order_info);
    }
    
    private void processModel() {
        MobileDB db = MobileDB.getMobileDB(ConfirmOrderActivity.this);
        
        // 建立與設定訂單物件
        orders = new Orders();
        orders.setTablesId(tablesId);
        orders.setNumber(ordersNumber);
        orders.setUserId(Integer.parseInt(TurtleUtil.getUserId(this)));
        orders.setTime(TurtleUtil.getFormatDate("yyyy-MM-dd HH:mm"));
        
        // 建立訂單明細包裝物件
        orderItems = new ArrayList<OrderItem>();
        
        for (MenuItem item : menuItem) {
            // 讀取指定編號的菜單物件
            item = db.getMenuItem(item.getId()); 
            
            // 建立與設定訂單明細物件
            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItemId(item.getId());
            orderItem.setMenuItemName(item.getName());
            orderItem.setMenuItemPrice(item.getPrice());
            orderItem.setNumber(1);
            orderItem.setImageFileName(item.getImageFileName());
            
            // 加入訂單明細物件
            orderItems.add(orderItem);
        }
        
        orderItemAdapter = new OrderItemAdapter(
                this, R.layout.confirm_order_view, orderItems, this);
        order_list.setAdapter(orderItemAdapter);
    }
    
    private void processOrder() {
        // 建立設定請求參數的List物件
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        // 設定訂單日期時間、使用者編號、桌號與訂單號碼
        parameters.add(new BasicNameValuePair("ordersTime", 
                orders.getTime()));
        parameters.add(new BasicNameValuePair("userId", 
                Integer.toString(orders.getUserId())));
        parameters.add(new BasicNameValuePair("tablesId", 
                Integer.toString(orders.getTablesId())));
        parameters.add(new BasicNameValuePair("ordersNumber",
                Integer.toString(orders.getNumber())));

        // 新增訂單的請求URL
        String url = HttpClientUtil.MOBILE_URL + "AddOrdersServlet.do";
        // 傳送請求並取得新增的訂單編號
        String ordersId = HttpClientUtil.sendPost(url, parameters);

        // 處理訂單明細
        processOrderItem(ordersId);
        
        // 傳送更新訂單通知請求的URL
        String urlNotify = HttpClientUtil.MOBILE_URL
                + "OrderNotifyServlet.do?ordersId=" + ordersId
                + "&type=ORDERS";
        // 傳送更新訂單通知請求
        HttpClientUtil.sendPost(urlNotify);
    }

    private void processOrderItem(String ordersId) {
        for (OrderItem orderItem : orderItems) {
            // 建立設定請求參數的List物件
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            // 設定菜單編號、訂單編號、數量與備註
            parameters.add(new BasicNameValuePair("menuItemId", 
                    Integer.toString(orderItem.getMenuItemId())));
            parameters.add(new BasicNameValuePair("ordersId", ordersId));
            parameters.add(new BasicNameValuePair("number", 
                    Integer.toString(orderItem.getNumber())));
            parameters.add(new BasicNameValuePair("note", 
                    orderItem.getNote()));

            // 傳送新增訂單明細請求的URL
            String url = HttpClientUtil.MOBILE_URL
                    + "AddOrderItemServlet.do";
            // 傳送新增訂單明細請求
            HttpClientUtil.sendPost(url, parameters);            
        }
    }
    
    // 計算與顯示訂單金額
    public void processOrderInfo() {
        List<OrderItem> items = orderItemAdapter.getItems();
        int total = 0;
        
        for (OrderItem item : items) {
            total += (item.getMenuItemPrice() * item.getNumber());
        }
        
        order_info.setText(String.format("訂單金額：$%d", total));
    }

}
