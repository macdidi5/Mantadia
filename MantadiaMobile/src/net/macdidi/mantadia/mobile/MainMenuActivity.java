package net.macdidi.mantadia.mobile;

import java.util.ArrayList;
import java.util.HashMap;

import net.macdidi.mantadia.library.util.TurtleUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * 主選單Activity元件
 * 
 * @author macdidi
 */
public class MainMenuActivity extends Activity {

    // 選單文字
    private String[] menuItems = 
        { "查詢餐桌", "換桌", "結帳", "資料同步", "設定", "結束" };
    // 選單圖示
    private int[] menuIcons = { R.drawable.menu_query,
            R.drawable.menu_change, R.drawable.menu_pay,
            R.drawable.menu_data, R.drawable.menu_config,
            R.drawable.menu_exit };
    // 顯示選單的元件
    private ListView menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        processViews();
        processControllers();

        if (TurtleUtil.getAutoUpdate(MainMenuActivity.this)) {
            // 啟動更新資料元件
            startActivity(new Intent("net.macdidi.mantadia.SYNC_START"));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 使用者按下退回鍵
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 登出
            logout();
        }

        return true;
    }

    private void processViews() {
        menu = (ListView) findViewById(R.id.menu);

        // 建立選單資料物件
        ArrayList<HashMap<String, Object>> myList = 
                new ArrayList<HashMap<String, Object>>();

        // 設定選單資料物件
        for (int i = 0; i < menuItems.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("main_menu_image", menuIcons[i]);
            map.put("main_menu_title", menuItems[i]);
            myList.add(map);
        }

        // 建立選單元件使用的Adapter物件
        SimpleAdapter sa = new SimpleAdapter(this, myList,
                R.layout.main_menu_view, new String[] { "main_menu_image",
                        "main_menu_title" }, new int[] {
                        R.id.main_menu_image, R.id.main_menu_title });

        // 設定選單元件使用的Adapter物件
        menu.setAdapter(sa);
    }

    private void processControllers() {
        menu.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                Intent intent = new Intent();

                switch (position) {
                // 查詢餐桌
                case 0:
                    intent.setClass(MainMenuActivity.this,
                            TableStatusActivity.class);
                    startActivity(intent);
                    break;
                // 換桌
                case 1:
                    intent.setClass(MainMenuActivity.this,
                            ChangeTableActivity.class);
                    startActivity(intent);
                    break;
                // 結帳
                case 2:
                    intent.setClass(MainMenuActivity.this,
                            CheckOutActivity.class);
                    startActivity(intent);
                    break;
                // 資料同步
                case 3:
                    intent.setAction("net.macdidi.mantadia.SYNC_WAIT");
                    startActivity(intent);
                    break;
                // 設定
                case 4:
                    intent.setClass(MainMenuActivity.this,
                            ConfigActivity.class);
                    startActivity(intent);
                    break;
                // 結束
                case 5:
                    // 登出
                    logout();
                    break;
                }
            }
        });
    }

    private void logout() {
        // 詢問是否結束的對話框
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage(R.string.main_menu_exit_txt);
        ab.setTitle(getString(android.R.string.dialog_alert_title));
        ab.setIcon(android.R.drawable.ic_dialog_alert);
        ab.setCancelable(false);
        ab.setPositiveButton(android.R.string.ok,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    TurtleUtil.setAccount(MainMenuActivity.this, "", "");
                    finish();
                }
            });
        ab.setNegativeButton(android.R.string.cancel, null);
        ab.show();
    }

}