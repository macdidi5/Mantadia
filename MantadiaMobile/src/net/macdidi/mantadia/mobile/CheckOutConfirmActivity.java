package net.macdidi.mantadia.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.macdidi.mantadia.domain.DataCollection;
import net.macdidi.mantadia.domain.OrderItem;
import net.macdidi.mantadia.library.util.HttpClientUtil;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 結帳確認Activity元件
 * 
 * @author macdidi
 */
public class CheckOutConfirmActivity extends Activity {

    // 餐桌編號、訂單編號、人數、日期時間、使用者名稱、訂單金額合計元件
    private TextView checkout_tables_id;
    private TextView checkout_orders_id;
    private TextView checkout_orders_number;
    private TextView checkout_orders_time;
    private TextView checkout_user_name;
    private TextView checkout_total_amount;

    // 訂單明細、確定與取消元件
    private ListView checkout_order_items;
    private Button checkout_confirm_ok;
    private Button checkout_confirm_cancel;

    private Intent intent;

    // 訂單明細Adapter與資料物件
    private SimpleAdapter adapter;
    private List<HashMap<String, String>> data = 
            new ArrayList<HashMap<String, String>>();

    private String ordersId;
    private String tablesId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_confirm);

        intent = getIntent();

        processViews();
        processControllers();

        // 取得與設定訂單明細
        getOrderItems(ordersId);
    }

    private void processViews() {
        checkout_tables_id = (TextView) findViewById(R.id.checkout_tables_id);
        checkout_orders_id = (TextView) findViewById(R.id.checkout_orders_id);
        checkout_orders_number = (TextView) 
                findViewById(R.id.checkout_orders_number);
        checkout_orders_time = (TextView) 
                findViewById(R.id.checkout_orders_time);
        checkout_user_name = (TextView) findViewById(R.id.checkout_user_name);
        checkout_total_amount = (TextView) 
                findViewById(R.id.checkout_total_amount);
        checkout_order_items = (ListView) 
                findViewById(R.id.checkout_order_items);
        checkout_confirm_ok = (Button) findViewById(R.id.checkout_confirm_ok);
        checkout_confirm_cancel = (Button) 
                findViewById(R.id.checkout_confirm_cancel);

        // 讀取訂單編號與餐桌編號
        ordersId = intent.getStringExtra("orders_id");
        tablesId = intent.getStringExtra("tables_id");

        // 設定餐桌編號、訂單編號、人數、日期時間與使用者名稱
        checkout_tables_id.setText(tablesId);
        checkout_orders_id.setText(ordersId);
        checkout_orders_number.setText(
                intent.getStringExtra("orders_number"));
        checkout_orders_time.setText(intent.getStringExtra("orders_time"));
        checkout_user_name.setText(intent.getStringExtra("user_name"));
    }

    private void processControllers() {
        // 註冊確定與取消按鈕元件監聽事件
        ButtonListnner listener = new ButtonListnner();
        checkout_confirm_ok.setOnClickListener(listener);
        checkout_confirm_cancel.setOnClickListener(listener);
    }

    // 確定與取消按鈕元件監聽類別
    private class ButtonListnner implements OnClickListener {

        @Override
        public void onClick(View view) {
            int id = view.getId();

            // 確定
            if (id == R.id.checkout_confirm_ok) {
                // 傳送結帳請求的URL
                String url = HttpClientUtil.MOBILE_URL
                        + "CheckOutServlet.do?ordersId=" + ordersId
                        + "&tablesId=" + tablesId;
                // 傳送結帳請求
                HttpClientUtil.sendPost(url);
                
                // 傳送更新訂單通知請求的URL
                String urlNotify = HttpClientUtil.MOBILE_URL
                        + "OrderNotifyServlet.do?ordersId=" + ordersId
                        + "&type=CHECKOUT";
                // 傳送更新訂單通知請求
                HttpClientUtil.sendPost(urlNotify);

                setResult(Activity.RESULT_OK, getIntent());
                Toast.makeText(CheckOutConfirmActivity.this,
                        R.string.checkout_success_txt, Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            // 取消
            else if (id == R.id.checkout_confirm_cancel) {
                setResult(Activity.RESULT_CANCELED, getIntent());
                finish();
            }
        }

    }

    private void getOrderItems(String ordersId) {
        // 傳送取得訂單明細請求的URL
        String url = HttpClientUtil.MOBILE_URL
                + "GetOrderItemsServlet.do?ordersId=" + ordersId;
        // 傳送請求並取得回傳的資料
        String result = HttpClientUtil.sendPost(url);
        
        // 準備從XML轉換為物件
        Serializer serializer = new Persister();

        try {
            // 將伺服器回應的XML資訊轉換為物件
            DataCollection<OrderItem> response = 
                    serializer.read(DataCollection.class, result);
            List<OrderItem> list = response.getList();
            data.clear();
            int total = 0;
            
            if (list.size() > 0) {
                for (OrderItem orderItem : list) {
                    // 建立一筆訂單明細資料
                    HashMap<String, String> record = 
                            new HashMap<String, String>();
                    record.put("serial", Integer.toString(orderItem.getSerial()));
                    record.put("name", orderItem.getMenuItemName());
                    record.put("number", Integer.toString(orderItem.getNumber()));
                    record.put("price", Integer.toString(orderItem.getMenuItemPrice()));
                    record.put("amount", Integer.toString(orderItem.getAmount()));

                    // 加入一筆訂單明細資料
                    data.add(record);
                    // 加總訂單金額
                    total = total + orderItem.getAmount();
                }
            }
            
            // 設定訂單明細資料給ListView元件
            String[] keys = { "serial", "name", "number", "price", "amount" };
            int[] viewIds = { R.id.checkout_confirm_serial,
                    R.id.checkout_confirm_name, R.id.checkout_confirm_number,
                    R.id.checkout_confirm_price, R.id.checkout_confirm_amount };
            adapter = new SimpleAdapter(this, data,
                    R.layout.checkout_confirm_view, keys, viewIds);
            checkout_order_items.setAdapter(adapter);

            // 設定訂單金額合計
            checkout_total_amount
                    .setText(getString(R.string.checkout_total_txt) + total);            
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d(getClass().getName(), e.toString());
        }
    }

}
