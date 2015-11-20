package net.macdidi.mantadia.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.macdidi.mantadia.domain.DataCollection;
import net.macdidi.mantadia.domain.Tables;
import net.macdidi.mantadia.library.util.HttpClientUtil;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * 結帳Activity元件
 * 
 * @author macdidi
 */
public class CheckOutActivity extends Activity {

    // 餐桌狀態元件
    private ListView table_status;
    // 餐桌狀態元件使用的Adapter物件
    private SimpleAdapter adapter;
    // 餐桌狀態元件使用的資料
    private List<HashMap<String, String>> data = 
            new ArrayList<HashMap<String, String>>();

    private boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_status);

        processViews();
        processControllers();

        // 讀取與設定餐桌狀態資訊
        getTableNotEmpty();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            firstTime = false;
            // 讀取與設定餐桌狀態資訊
            getTableNotEmpty();
        }

    }

    private void processViews() {
        table_status = (ListView) findViewById(R.id.table_status);
    }

    private void processControllers() {
        // 註冊選擇餐桌監聽事件
        table_status.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // 選擇的餐桌資訊
                HashMap<String, String> item = (HashMap<String, String>) 
                        adapter.getItem(position);
                // 準備啟動確認結帳元件
                Intent intent = new Intent(CheckOutActivity.this,
                        CheckOutConfirmActivity.class);
                
                // 選擇的餐桌資訊所有資料名稱
                Set<String> keys = item.keySet();

                // 設定選擇的餐桌資訊所有資料到Intent物件
                for (String key : keys) {
                    intent.putExtra(key, item.get(key));
                }

                // 啟動確認結帳元件
                startActivityForResult(intent, 0);
            }

        });
    }

    // 取得有客人的餐桌資訊
    private void getTableNotEmpty() {
        // 傳送取得有客人餐桌資訊請求的URL
        String url = HttpClientUtil.MOBILE_URL
                + "TableStatusServlet.do?type=NOT_EMPTY";
        // 傳送請求並取得回傳的資料
        String result = HttpClientUtil.sendGet(url);

        // 準備從XML轉換為物件
        Serializer serializer = new Persister();

        try {
            // 將伺服器回應的XML資訊轉換為物件
            DataCollection<Tables> response = 
                    serializer.read(DataCollection.class, result);
            List<Tables> list = response.getList();
            
            // 沒有餐桌可以選擇
            if (list.size() == 0) {
                // 確認結帳後返回如果沒有餐桌，就直接結束
                if (!firstTime) {
                    finish();
                }
                
                // 顯示錯誤訊息對話框
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle(getString(android.R.string.dialog_alert_title));
                adb.setMessage(R.string.checkout_no_orders_etxt);
                adb.setIcon(android.R.drawable.ic_dialog_info);

                adb.setPositiveButton(getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                finish();
                            }
                        });
                adb.show();
            }
            // 有餐桌可以選擇
            else {
                data.clear();
                
                for (Tables tables : list) {
                    // 建立餐桌資訊
                    HashMap<String, String> record = 
                            new HashMap<String, String>();

                    // 設定餐桌編號、訂單編號、日期時間、使用者名稱、人數與狀態
                    record.put("tables_id", 
                            Integer.toString(tables.getId()));
                    record.put("orders_id", tables.getOrders().getId() == 0 
                            ? "" : Integer.toString(tables.getOrders().getId()));
                    record.put("orders_time", tables.getOrders().getTime());
                    record.put("user_name", tables.getOrders().getUserName());
                    record.put("orders_number", tables.getOrders().getNumber() == 0 
                            ? "" : Integer.toString(tables.getOrders().getNumber()));
                    record.put("status_name", tables.getOrders().getStatusName());

                    // 加入一筆餐桌資訊
                    data.add(record);
                }
                
                // 設定餐桌資料給ListView元件
                String[] keys = { "tables_id", "orders_id", "orders_time",
                        "user_name", "orders_number", "status_name" };
                int[] viewIds = { R.id.tables_id, R.id.orders_id,
                        R.id.orders_time, R.id.user_name, R.id.orders_number,
                        R.id.status_name };
                adapter = new SimpleAdapter(this, data,
                        R.layout.table_status_view, keys, viewIds);
                table_status.setAdapter(adapter);           
            }
        }
        catch (Exception e) {
            Log.d(getClass().getName(), e.toString());
        }
    }

}
