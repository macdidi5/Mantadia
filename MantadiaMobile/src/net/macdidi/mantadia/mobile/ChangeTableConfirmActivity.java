package net.macdidi.mantadia.mobile;

import java.util.List;

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
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 換桌確認Activity元件
 * 
 * @author macdidi
 */
public class ChangeTableConfirmActivity extends Activity {

    // 餐桌編號、訂單編號、人數、日期時間、使用者名稱、選擇空桌、確定與取消元件
    private TextView tables_id;
    private TextView orders_id;
    private TextView orders_number;
    private TextView orders_time;
    private TextView orders_user_name;
    private Spinner empty_tables;
    private Button change_table_confirm_ok;
    private Button change_table_confirm_cancel;

    private Intent intent;
    private String ordersId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_table_confirm);

        intent = getIntent();

        processViews();
        processControllers();
        processModel();
    }

    private void processViews() {
        tables_id = (TextView) findViewById(R.id.tables_id);
        orders_id = (TextView) findViewById(R.id.orders_id);
        orders_number = (TextView) findViewById(R.id.orders_number);
        orders_time = (TextView) findViewById(R.id.orders_time);
        orders_user_name = (TextView) findViewById(R.id.orders_user_name);
        empty_tables = (Spinner) findViewById(R.id.empty_tables);
        change_table_confirm_ok = (Button) 
                findViewById(R.id.change_table_confirm_ok);
        change_table_confirm_cancel = (Button) 
                findViewById(R.id.change_table_confirm_cancel);

        // 讀取訂單編號
        ordersId = intent.getStringExtra("orders_id");
        
        // 設定餐桌編號、訂單編號、人數、日期時間與使用者名稱元件內容
        tables_id.setText(intent.getStringExtra("tables_id"));
        orders_id.setText(intent.getStringExtra("orders_id"));
        orders_number.setText(intent.getStringExtra("orders_number"));
        orders_time.setText(intent.getStringExtra("orders_time"));
        orders_user_name.setText(intent.getStringExtra("user_name"));
    }

    private void processControllers() {
        // 註冊確定與取消元件監聽事件
        ButtonListnner listener = new ButtonListnner();
        change_table_confirm_ok.setOnClickListener(listener);
        change_table_confirm_cancel.setOnClickListener(listener);
    }

    private void processModel() {
        // 傳送取得空桌資訊請求的URL
        String url = HttpClientUtil.MOBILE_URL
                + "TableStatusServlet.do?type=EMPTY";
        // 傳送請求並取得回傳的資料
        String result = HttpClientUtil.sendPost(url);
        
        // 準備從XML轉換為物件
        Serializer serializer = new Persister();

        try {
            // 將伺服器回應的XML資訊轉換為物件
            DataCollection<Tables> response = 
                    serializer.read(DataCollection.class, result);
            List<Tables> list = response.getList();
            
            // 沒有空桌可以選擇
            if (list.size() == 0) {
                // 顯示錯誤訊息對話框
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle(getString(android.R.string.dialog_alert_title));
                adb.setMessage(R.string.change_table_confirm_no_empty_tables_etxt);
                adb.setIcon(android.R.drawable.ic_dialog_info);

                adb.setPositiveButton(getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                setResult(Activity.RESULT_CANCELED, getIntent());
                                finish();
                            }
                        });
                adb.show();
            }
            // 有空桌可以選擇
            else {
                // 建立與設定空桌陣列資料
                String[] tablesIds = new String[list.size()];
                
                for (int i = 0; i < list.size(); i++) {
                    tablesIds[i] = Integer.toString(list.get(i).getId());
                }
                
                // 設定空桌資料給ListView元件
                int changeTableView = R.layout.change_table_view;
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        ChangeTableConfirmActivity.this, changeTableView,
                        tablesIds);
                adapter.setDropDownViewResource(changeTableView);
                empty_tables.setAdapter(adapter);                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d(getClass().getName(), e.toString());
        }
    }

    private class ButtonListnner implements OnClickListener {

        @Override
        public void onClick(View view) {
            int id = view.getId();

            // 確定
            if (id == R.id.change_table_confirm_ok) {
                processChangeTable();
                setResult(Activity.RESULT_OK, getIntent());
                finish();
            }
            // 取消
            else if (id == R.id.change_table_confirm_cancel) {
                setResult(Activity.RESULT_CANCELED, getIntent());
                finish();
            }
        }

    }

    // 傳送換桌請求給網頁應用程式
    private void processChangeTable() {
        // 讀取餐桌編號
        String oldTablesId = intent.getStringExtra("tables_id");
        // 讀取換桌編號
        String newTablesId = (String) empty_tables.getSelectedItem();
        // 傳送換桌請求的URL
        String url = HttpClientUtil.MOBILE_URL
                + "ChangeTableServlet.do?ordersId=" + ordersId
                + "&oldTablesId=" + oldTablesId + "&newTablesId="
                + newTablesId;
        // 傳送換桌請求
        HttpClientUtil.sendPost(url);

        // 傳送更新訂單通知請求的URL
        String urlNotify = HttpClientUtil.MOBILE_URL
                + "OrderNotifyServlet.do?ordersId=" + ordersId
                + "&type=ORDERS";
        // 傳送更新訂單通知請求
        HttpClientUtil.sendPost(urlNotify);

        Toast.makeText(this, R.string.change_table_confirm_success_txt,
                Toast.LENGTH_SHORT).show();
    }

}
