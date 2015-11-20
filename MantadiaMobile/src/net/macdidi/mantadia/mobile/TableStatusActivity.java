package net.macdidi.mantadia.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.macdidi.mantadia.domain.DataCollection;
import net.macdidi.mantadia.domain.Tables;
import net.macdidi.mantadia.library.util.HttpClientUtil;
import net.macdidi.mantadia.library.util.TurtleUtil;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * 餐桌狀態Activity元件
 * 
 * @author macdidi
 */
public class TableStatusActivity extends Activity {

    // 餐桌狀態元件
    private ListView table_status;
    // 餐桌狀態元件使用的Adapter物件
    private SimpleAdapter adapter;
    // 餐桌狀態元件使用的資料
    private List<HashMap<String, String>> data = 
            new ArrayList<HashMap<String, String>>();

    // 新增與修改訂單編號
    private static final int ADD_ORDERS = 0;
    private static final int EDIT_ORDERS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_status);

        processViews();
        processControllers();

        // 讀取與設定餐桌狀態資料
        getTableStatus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            // 讀取餐桌狀態資訊並重新顯示
            getTableStatus();
            adapter.notifyDataSetChanged();
        }
    }

    private void processViews() {
        table_status = (ListView) findViewById(R.id.table_status);
    }

    private void processControllers() {
        table_status.setOnItemClickListener(new ProcessListener());
    }

    private class ProcessListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
            // 選擇的餐桌資訊
            HashMap<String, String> item = (HashMap<String, String>) 
                    adapter.getItem(position);
            // 選擇的訂單編號
            String ordersId = item.get("orders_id");

            // 空桌
            if (ordersId.equals("")) {
                // 使用新增模式啟動訂單元件
                Intent intent = new Intent();
                intent.setAction("net.macdidi.mantadia.ADD_ORDERS");
                // 設定要傳送的餐桌編號
                intent.putExtra("tables_id", item.get("tables_id"));
                startActivityForResult(intent, ADD_ORDERS);
            }
            // 有客人
            else {
                // 使用修改模式啟動訂單元件
                Intent intent = new Intent();
                intent.setAction("net.macdidi.mantadia.EDIT_ORDERS");
                // 設定要傳送的餐桌編號、訂單編號、人數與狀態
                intent.putExtra("tables_id", item.get("tables_id"));
                intent.putExtra("orders_id", item.get("orders_id"));
                intent.putExtra("orders_number", item.get("orders_number"));
                intent.putExtra("status_name", item.get("status_name"));
                startActivityForResult(intent, EDIT_ORDERS);
            }
        }

    }

    // 從伺服器取得餐桌狀態資訊
    private void getTableStatus() {
        // 清除餐桌狀態資料
        data.clear();

        // 餐桌資訊名稱
        String[] labelKeys = { "label_orders_id", "label_orders_time",
                "label_orders_username", "label_orders_number",
                "label_orders_status" };
        // 餐桌資訊元件資源編號
        int[] labelTextIds = { R.string.table_status_orders_id_txt,
                R.string.table_status_orders_time_txt,
                R.string.table_status_orders_username_txt,
                R.string.table_status_orders_number_txt,
                R.string.table_status_orders_status_txt };
        
        // 讀取餐桌狀態資訊的請求URL
        String url = HttpClientUtil.MOBILE_URL
                + "TableStatusServlet.do?type=ALL";
        // 送出請求並取得餐桌狀態資訊
        String result = HttpClientUtil.sendPost(url);        
        // 準備從XML轉換為物件
        Serializer serializer = new Persister();

        try {
            // 將伺服器回應的XML資訊轉換為物件
            DataCollection<Tables> response = 
                    serializer.read(DataCollection.class, result);
            // 取得所有餐桌資訊物件
            List<Tables> list = response.getList();
            
            // 設定餐桌資訊
            for (Tables tables : list) {
                HashMap<String, String> record = new HashMap<String, String>();

                record.put("tables_id", Integer.toString(tables.getId()));
                record.put("orders_id", tables.getOrders().getId() == 0 
                        ? "" : Integer.toString(tables.getOrders().getId()));
                record.put("orders_time", tables.getOrders().getTime());
                record.put("user_name", tables.getOrders().getUserName());
                record.put("orders_number", tables.getOrders().getNumber() == 0 
                        ? "" : Integer.toString(tables.getOrders().getNumber()));
                record.put("status_name", tables.getOrders().getStatusName());

                boolean isNotEmpty = tables.getOrders().getId() != 0;

                for (int i = 0; i < labelKeys.length; i++) {
                    record.put(labelKeys[i],
                            (isNotEmpty ? getString(labelTextIds[i]) : ""));
                }

                data.add(record);
            }
        }
        catch (Exception e) {
            Log.d(getClass().getName(), e.toString());
        }
        
        String[] keys = { "tables_id", "orders_id", "orders_time",
                "user_name", "orders_number", "status_name" };
        int[] ids = { R.id.tables_id, R.id.orders_id, R.id.orders_time,
                R.id.user_name, R.id.orders_number, R.id.status_name };
        int[] labelIds = { R.id.label_orders_id, R.id.label_orders_time,
                R.id.label_orders_username, R.id.label_orders_number,
                R.id.label_orders_status };
        
        // 建立餐桌狀態元件使用的Adapter物件
        adapter = new SimpleAdapter(this, data, R.layout.table_status_view,
                TurtleUtil.mergeArray(labelKeys, keys),
                TurtleUtil.mergeArray(labelIds, ids));
        // 設定餐桌狀態元件使用的Adapter物件
        table_status.setAdapter(adapter);
    }

}
