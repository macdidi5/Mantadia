package net.macdidi.mantadia.emenu;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * 選擇餐桌Activity元件
 * 
 * @author macdidi
 */
public class SelectTableActivity extends Activity {

    // 空桌與人數元件
    private Spinner empty_tables;
    private EditText orders_number;
    
    // 訂單人數
    private int ordersNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_table);

        processViews();
        processModel();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == Activity.RESULT_OK) {
            finish();
        }
    }
    
    private void processViews() {
        empty_tables = (Spinner) findViewById(R.id.empty_tables);
        orders_number = (EditText) findViewById(R.id.orders_number);
    }
    
    // 增加與減少人數
    public void clickOrdersNumber(View view) {
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
    }
    
    // 確定與取消
    public void okAndCancel(View view) {
        int id = view.getId();

        // 確定
        if (id == R.id.select_table_ok) {
            // 啟動菜單元件
            Intent intent = new Intent(SelectTableActivity.this,
                    MenuActivity.class);
            // 讀取使用者選擇的桌號
            String tablesIdStr = (String) empty_tables.getSelectedItem();
            // 設定傳送的桌號與人數資料
            intent.putExtra("tablesId", Integer.parseInt(tablesIdStr));
            intent.putExtra("ordersNumber", ordersNumber);
            startActivityForResult(intent , 0);
        }
        // 取消
        else if (id == R.id.select_table_cancel) {
            finish();
        }        
    }

    private void processModel() {
        // 傳送餐桌狀態資訊請求的URL
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
                // 顯示對話框
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
                // 建立所有桌號的字串陣列
                String[] tablesIds = new String[list.size()];
                
                // 將桌號儲存到字串陣列
                for (int i = 0; i < list.size(); i++) {
                    tablesIds[i] = Integer.toString(list.get(i).getId());
                }
                
                // 選擇桌號使用的畫面配置資源
                int changeTableView = R.layout.select_table_view;
                // 包裝桌號資料的Adapter物件
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        SelectTableActivity.this, changeTableView,
                        tablesIds);
                // 設定桌號選單的畫面配置資源
                adapter.setDropDownViewResource(changeTableView);
                // 設定選擇桌號元件使用的Adapter物件
                empty_tables.setAdapter(adapter);                
            }
        }
        catch (Exception e) {
            Log.d(getClass().getName(), e.toString());
        }
    }

}
