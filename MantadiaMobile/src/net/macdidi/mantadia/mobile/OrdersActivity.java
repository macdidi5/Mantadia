package net.macdidi.mantadia.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.macdidi.mantadia.domain.DataCollection;
import net.macdidi.mantadia.domain.OrderItem;
import net.macdidi.mantadia.library.util.HttpClientUtil;
import net.macdidi.mantadia.library.util.TurtleUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 訂單Activity元件
 * 
 * @author macdidi
 */
public class OrdersActivity extends Activity {

    // 餐桌編號、人數、增加人數、減少人數、新增菜單、確定與取消元件
    private EditText orders_table_id;
    private EditText orders_number;
    private ImageButton orders_add_number;
    private ImageButton orders_minus_number;
    private Button orders_add_menuitem;
    private Button orders_ok;
    private Button orders_cancel;
    // 訂單明細元件
    private ListView orders_items;

    // 訂單明細資料物件
    private List<HashMap<String, String>> data = 
            new ArrayList<HashMap<String, String>>();
    // 訂單明細元件使用的Adapter物件
    private OrderAdapter orderAdapter;
    
    // 新增或修改訂單模式
    private String action;
    private Intent intent;

    // 餐桌編號、訂單編號和是否為新增訂單
    private String tablesId;
    private int ordersNumber;
    private boolean isNew = true;

    // 新增或修改訂單模式編號
    private static final int ADD_MENU_ITEM = 0;
    private static final int EDIT_MENU_ITEM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);

        // 讀取餐桌編號
        intent = getIntent();
        tablesId = intent.getStringExtra("tables_id");

        processViews();
        processControllers();

        // 取得新增或修改訂單模式
        action = intent.getAction();

        // 修改訂單
        if (action.equals("net.macdidi.mantadia.EDIT_ORDERS")) {
            // 讀取訂單編號、人數與狀態
            String ordersId = intent.getStringExtra("orders_id");
            String ordersNumber = intent.getStringExtra("orders_number");
            String statusName = intent.getStringExtra("status_name");

            // 如果是不是新單，把訂單設定為不可以修改
            if (!statusName.equals("新單")) {
                orders_number.setEnabled(false);
                orders_add_menuitem.setEnabled(false);
                orders_ok.setEnabled(false);
                orders_add_number.setEnabled(false);
                orders_minus_number.setEnabled(false);
                isNew = false;
            }

            // 設定訂單編號
            orders_number.setText(ordersNumber);
            this.ordersNumber = Integer.parseInt(ordersNumber);
            // 取得訂單明細
            getOrderItems(ordersId);
        }

        // 建立訂單明細元件使用的Adapter物件
        orderAdapter = new OrderAdapter(this);
        // 設定訂單明細元件使用的Adapter物件
        orders_items.setAdapter(orderAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent intent) {

        // 確認菜單
        if (resultCode == Activity.RESULT_OK) {
            // 讀取菜單編號、名稱、數量與備註
            String menuItemIdValue = intent.getStringExtra("menu_item_id");
            String menuItemNameValue = 
                    intent.getStringExtra("menu_item_name");
            String menuItemNumberValue = 
                    intent.getStringExtra("menu_item_number");
            String menuItemNoteValue = 
                    intent.getStringExtra("menu_item_note");

            // 新增菜單
            if (requestCode == ADD_MENU_ITEM) {
                // 建立一筆菜單資料
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("serial", Integer.toString(data.size() + 1));
                map.put("id", menuItemIdValue);
                map.put("name", menuItemNameValue);
                map.put("number", menuItemNumberValue);
                map.put("note", menuItemNoteValue);
                
                // 加入一筆菜單
                data.add(map);
            }
            // 修改菜單
            else if (requestCode == EDIT_MENU_ITEM) {
                // 讀取序號
                String serialValue = intent.getStringExtra("serial");

                for (int i = 0; i < data.size(); i++) {
                    String serialData = data.get(i).get("serial");

                    // 如果是修改菜單的序號
                    if (serialValue.equals(serialData)) {
                        // 建立一筆菜單資料
                        HashMap<String, String> map = 
                                new HashMap<String, String>();
                        map.put("serial", serialValue);
                        map.put("id", menuItemIdValue);
                        map.put("name", menuItemNameValue);
                        map.put("number", menuItemNumberValue);
                        map.put("note", menuItemNoteValue);
                        
                        // 修改這筆菜單
                        data.set(i, map);
                        
                        break;
                    }
                }
            }

            // 通知菜單資料已改變
            orderAdapter.notifyDataSetChanged();
        }

    }

    private void processViews() {
        orders_table_id = (EditText) findViewById(R.id.orders_table_id);
        orders_number = (EditText) findViewById(R.id.orders_number);
        orders_add_number = (ImageButton) 
                findViewById(R.id.orders_add_number);
        orders_minus_number = (ImageButton) 
                findViewById(R.id.orders_minus_number);
        orders_add_menuitem = (Button) 
                findViewById(R.id.orders_add_menuitem);
        orders_items = (ListView) findViewById(R.id.orders_items);
        orders_ok = (Button) findViewById(R.id.orders_ok);
        orders_cancel = (Button) findViewById(R.id.orders_cancel);

        orders_table_id.setText(tablesId);
    }

    private void processControllers() {
        // 註冊新增菜單按鈕事件
        orders_add_menuitem.setOnClickListener(new AddMenuListnner());

        // 註冊增加、減少人數，確定、取消按鈕事件
        ButtonListnner listener = new ButtonListnner();
        orders_add_number.setOnClickListener(listener);
        orders_minus_number.setOnClickListener(listener);
        orders_ok.setOnClickListener(listener);
        orders_cancel.setOnClickListener(listener);
    }

    // 新增菜單監聽類別
    private class AddMenuListnner implements OnClickListener {
        @Override
        public void onClick(View view) {
            // 啟動菜單Activity元件
            Intent intent = new Intent();
            // 設定Action為新增菜單
            intent.setAction("net.macdidi.mantadia.ADD_MENU_ITEM");
            startActivityForResult(intent, ADD_MENU_ITEM);
        }
    }

    // 增加、減少人數，確定、取消監聽類別
    private class ButtonListnner implements OnClickListener {

        @Override
        public void onClick(View view) {
            int id = view.getId();

            // 增加人數
            if (id == R.id.orders_add_number) {
                ordersNumber++;
                orders_number.setText(Integer.toString(ordersNumber));
            }
            // 減少人數
            else if (id == R.id.orders_minus_number) {
                if (ordersNumber > 0) {
                    ordersNumber--;
                    orders_number.setText(Integer.toString(ordersNumber));
                }
            }
            // 確認訂單
            else if (id == R.id.orders_ok) {
                // 如果訂單資料通過檢查
                if (checkOrder()) {
                    // 避免重複選擇確定按鈕
                    orders_ok.setEnabled(false);
                    // 傳送訂單資料到網頁應用程式
                    processOrder();
                    // 設定結果為確定
                    setResult(Activity.RESULT_OK, getIntent());
                    finish();
                }
            }
            // 取消訂單
            else if (id == R.id.orders_cancel) {
                // 設定結果為取消
                setResult(Activity.RESULT_CANCELED, getIntent());
                finish();
            }
        }

    }

    // 檢查訂單資料
    private boolean checkOrder() {
        boolean result = true;
        String personNum = orders_number.getText().toString();
        String message = "";

        // 檢查人數
        if (personNum == null || personNum.length() == 0
                || personNum.equals("0")) {
            message += getString(R.string.orders_input_number_etxt) + "\n";
        }

        // 檢查菜單
        if (data.size() == 0) {
            message += getString(R.string.orders_add_menuitem_etxt) + "\n";
        }

        // 顯示錯誤訊息對話框
        if (message.length() > 0) {
            result = false;
            AlertDialog.Builder ab = new AlertDialog.Builder(
                    OrdersActivity.this);
            ab.setMessage(message);
            ab.setTitle(getString(android.R.string.dialog_alert_title));
            ab.setIcon(android.R.drawable.ic_dialog_alert);
            ab.setCancelable(false);
            ab.setPositiveButton(getString(android.R.string.ok), null);
            ab.show();
        }

        return result;
    }

    // 確認訂單後傳送訂單資訊給網頁應用程式
    private void processOrder() {
        // 讀取訂單編號、人數與使用者編號
        String ordersId = intent.getStringExtra("orders_id");
        String ordersNumber = orders_number.getText().toString();
        String userId = TurtleUtil.getUserId(this);

        // 如果是修改訂單
        if (action.equals("net.macdidi.mantadia.EDIT_ORDERS")) {
            // 傳送修改訂單請求的URL
            String url = HttpClientUtil.MOBILE_URL
                    + "UpdateOrdersServlet.do?ordersId=" + ordersId
                    + "&ordersNumber=" + ordersNumber;
            // 傳送請求
            HttpClientUtil.sendPost(url);
        }
        else {
            // 取得目前日期時間
            String ordersTime = TurtleUtil
                    .getFormatDate("yyyy-MM-dd HH:mm");

            // 建立訂單的HTTP請求資料
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("ordersTime", ordersTime));
            parameters.add(new BasicNameValuePair("userId", userId));
            parameters.add(new BasicNameValuePair("tablesId", tablesId));
            parameters.add(new BasicNameValuePair("ordersNumber",
                    ordersNumber));

            // 傳送新增訂單請求的URL
            String url = HttpClientUtil.MOBILE_URL + "AddOrdersServlet.do";
            // 傳送請求並取得訂單編號
            ordersId = HttpClientUtil.sendPost(url, parameters);
        }

        // 傳送訂單明細資料到網頁應用程式
        processOrderItem(ordersId);
        
        // 傳送訂單資訊異動請求的URL
        String urlNotify = HttpClientUtil.MOBILE_URL
                + "OrderNotifyServlet.do?ordersId=" + ordersId
                + "&type=ORDERS";
        // 傳送請求
        HttpClientUtil.sendPost(urlNotify);
    }

    private void processOrderItem(String ordersId) {
        for (int i = 0; i < data.size(); i++) {
            HashMap<String, String> map = data.get(i);

            // 讀取菜單編號、數量與備註
            String menuItemId = map.get("id");
            String number = map.get("number");
            String note = map.get("note");

            // 建立訂單明細的HTTP請求資料
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("menuItemId", menuItemId));
            parameters.add(new BasicNameValuePair("ordersId", ordersId));
            parameters.add(new BasicNameValuePair("number", number));
            parameters.add(new BasicNameValuePair("note", note));

            // 傳送新增訂單明細請求的URL
            String url = HttpClientUtil.MOBILE_URL
                    + "AddOrderItemServlet.do";
            // 傳送請求
            HttpClientUtil.sendPost(url, parameters);
        }
    }

    // 提供給ListView元件使用的訂單明細資料包裝類別
    private class OrderAdapter extends BaseAdapter {

        // 畫面包裝元件
        private LayoutInflater inflater;

        public OrderAdapter(Context context) {
            // 取得畫面包裝元件
            inflater = LayoutInflater.from(context);
        }

        // 傳回項目數量
        @Override        
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView,
                ViewGroup parent) {

            // 每一個項目的畫面配置元件
            RelativeLayout orderItemView;

            if (convertView == null) {
                // 建立項目的畫面配置元件
                orderItemView = (RelativeLayout) inflater.inflate(
                        R.layout.orders_view, null);
            }
            else {
                // 取得項目的畫面配置元件
                orderItemView = (RelativeLayout) convertView;
            }

            // 建立畫面元件包裝物件
            ViewHolder viewHolder = new ViewHolder();

            // 讀取菜單序號、名稱、數量、刪除與修改按鈕元件
            viewHolder.order_item_serial = (TextView) orderItemView
                    .findViewById(R.id.order_item_serial);
            viewHolder.order_item_name = (TextView) orderItemView
                    .findViewById(R.id.order_item_name);
            viewHolder.order_item_number = (TextView) orderItemView
                    .findViewById(R.id.order_item_number);
            viewHolder.order_item_delete = (ImageButton) orderItemView
                    .findViewById(R.id.order_item_delete);
            viewHolder.order_item_update = (ImageButton) orderItemView
                    .findViewById(R.id.order_item_update);

            // 設定菜單序號、名稱與數量
            viewHolder.order_item_serial.setText(data.get(position).get(
                    "serial"));
            viewHolder.order_item_name.setText(data.get(position).get(
                    "name"));
            viewHolder.order_item_number.setText(data.get(position).get(
                    "number"));

            // 如果是新增訂單
            if (isNew) {
                // 註冊刪除菜單項目按鈕事件
                viewHolder.order_item_delete
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // 詢問是否刪除菜單項目的對話框
                            AlertDialog.Builder ab = new AlertDialog.Builder(
                                    OrdersActivity.this);
                            ab.setMessage(getString(R.string.delete_confirm_txt));
                            ab.setTitle(getString(android.R.string.dialog_alert_title));
                            ab.setIcon(android.R.drawable.ic_dialog_alert);
                            ab.setCancelable(false);
                            
                            // 確定刪除按鈕
                            ab.setPositiveButton(
                                getString(android.R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int id) {
                                        // 刪除菜單項目
                                        data.remove(position);
                                        // 通知菜單資料已改變
                                        orderAdapter.notifyDataSetChanged();
                                    }
                                });
                            
                            // 取消刪除按鈕
                            ab.setNegativeButton(
                                getString(android.R.string.cancel),
                                null);
                            
                            // 顯示對話框
                            ab.show();
                        }

                    });

                // 註冊修改菜單項目按鈕事件
                viewHolder.order_item_update
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // 準備啟動修改菜單元件
                            Intent intent = new Intent();
                            intent.setAction("net.macdidi.mantadia.EDIT_MENU_ITEM");
                            
                            // 設定流水號、菜單編號、名稱、數量與備註
                            intent.putExtra("serial", 
                                    data.get(position).get("serial"));
                            intent.putExtra("id", 
                                    data.get(position).get("id"));
                            intent.putExtra("name", 
                                    data.get(position).get("name"));
                            intent.putExtra("number", 
                                    data.get(position).get("number"));
                            intent.putExtra("note", 
                                    data.get(position).get("note"));
                            
                            // 啟動修改菜單元件
                            startActivityForResult(intent,
                                    EDIT_MENU_ITEM);
                        }

                    });
            }

            return orderItemView;
        }

    }

    private class ViewHolder {
        public TextView order_item_serial;
        public TextView order_item_name;
        public ImageButton order_item_delete;
        public ImageButton order_item_update;
        public TextView order_item_number;
    }
    
    // 跟網頁應用程式請求指定訂單編號的所有訂單明細後加入畫面元件
    private void getOrderItems(String ordersId) {
        // 傳送取得訂單明細請求的URL
        String url = HttpClientUtil.MOBILE_URL
                + "GetOrderItemsServlet.do?ordersId=" + ordersId;
        // 傳送請求並取得XML格式的訂單明細資料
        String result = HttpClientUtil.sendPost(url);
        
        // 準備從XML轉換為物件
        Serializer serializer = new Persister();

        try {
            // 將伺服器回應的XML資訊轉換為物件
            DataCollection<OrderItem> response = 
                    serializer.read(DataCollection.class, result);
            List<OrderItem> list = response.getList();
            data.clear();
            
            if (list.size() > 0) {
                // 菜單流水號
                int serial = 1;
                
                for (OrderItem orderItem : list) {
                    // 建立一筆菜單資料
                    HashMap<String, String> record = 
                            new HashMap<String, String>();
                    
                    record.put("serial", Integer.toString(orderItem.getSerial()));
                    record.put("id", Integer.toString(orderItem.getMenuItemId()));
                    record.put("name", orderItem.getMenuItemName());
                    record.put("number", Integer.toString(orderItem.getNumber()));
                    record.put("note", orderItem.getNote());

                    // 加入菜單資料
                    data.add(record);
                }
            }        
        }
        catch (Exception e) {
            Log.d(getClass().getName(), e.toString());
        }
    }    

}
