package net.macdidi.mantadia.mobile;

import java.util.ArrayList;
import java.util.List;

import net.macdidi.mantadia.domain.MenuItem;
import net.macdidi.mantadia.domain.MenuType;
import net.macdidi.mantadia.library.db.MobileDB;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

/**
 * 菜單Activity元件
 * 
 * @author macdidi
 */
public class MenuItemActivity extends Activity {

    // 選擇菜單種類、選擇菜單、數量、增加數量、減少數量元件
    private Spinner menu_type_name;
    private Spinner menu_item_name;
    private EditText menu_item_number;
    private ImageButton menu_item_add_number;
    private ImageButton menu_item_minus_number;
    // 備註、確定與取消按鈕元件
    private EditText menu_item_note;
    private Button menu_item_ok;
    private Button menu_item_cancel;

    private Intent intent;

    // 菜單種類資料
    private List<String> menuTypes;
    // 菜單數量
    private int menuNumber;
    // 菜單資料
    private List<String> menuItems;

    // 新增或修改菜單
    private String action;
    
    // 控制第一次菜單種類選擇事件
    private boolean skip = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_item);

        processViews();
        processControllers();
        processModel();

        intent = getIntent();
        action = intent.getAction();

		// 修改菜單
        if (action.equals("net.macdidi.mantadia.EDIT_MENU_ITEM")) {
            // 讀取菜單編號、名稱、數量與備註
            String id = intent.getStringExtra("id");
            String name = intent.getStringExtra("name");
            String number = intent.getStringExtra("number");
            String note = intent.getStringExtra("note");

            // 取得修改菜單的項目編號
            String search = id + " ~ " + name;
            int place = menuItems.indexOf(search);

            if (place != -1) {
                // 設定選擇元件的項目
                menu_item_name.setSelection(place, true);
            }

            // 設定數量與備註
            menu_item_number.setText(number);
            menuNumber = Integer.parseInt(number);
            menu_item_note.setText(note);
        }		
    }

	private void processViews() {
        menu_type_name = (Spinner) findViewById(R.id.menu_type_name);
        menu_item_name = (Spinner) findViewById(R.id.menu_item_name);
        menu_item_number = (EditText) findViewById(R.id.menu_item_number);
        menu_item_add_number = (ImageButton) 
                findViewById(R.id.menu_item_add_number);
        menu_item_minus_number = (ImageButton) 
                findViewById(R.id.menu_item_minus_number);
        menu_item_note = (EditText) findViewById(R.id.menu_item_note);
        menu_item_ok = (Button) findViewById(R.id.menu_item_ok);
        menu_item_cancel = (Button) findViewById(R.id.menu_item_cancel);

        menu_item_number.setText(Integer.toString(menuNumber));
    }

    private void processControllers() {
        // 註冊選擇菜單種類元件的監聽事件
        MenuTypeListener menuTypeListener = new MenuTypeListener();
        menu_type_name.setOnItemSelectedListener(menuTypeListener);

        // 註冊增加、減少數量、確定與取消按鈕的監聽事件
        ButtonListner listener = new ButtonListner();
        menu_item_add_number.setOnClickListener(listener);
        menu_item_minus_number.setOnClickListener(listener);
        menu_item_ok.setOnClickListener(listener);
        menu_item_cancel.setOnClickListener(listener);
    }

    private void processModel() {
        // 取得資料庫存取元件
        MobileDB db = MobileDB.getMobileDB(MenuItemActivity.this);

        // 讀取所有菜單種類
        List<MenuType> types = db.getAllMenuType();
        menuTypes = new ArrayList<String>();

        for (MenuType data : types) {
            menuTypes.add(data.getId() + " ~ " + data.getName());
        }

        // 設定選擇菜單種類元件使用的資料
        menuTypes.add(0, getString(R.string.menu_item_type_all_etxt));
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, menuTypes);
        typeAdapter.setDropDownViewResource(
                R.layout.my_spinner_dropdown_item);
        menu_type_name.setAdapter(typeAdapter);
        
        // 建立存放菜單資料的List物件
        menuItems = new ArrayList<String>();
        
        // 設定菜單元件顯示所有菜單資料
        processMenuItem(0);
    }

    private class MenuTypeListener implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                int position, long id) {
        	// 如果是第一次啟動菜單種類選擇事件
        	if (skip) {
        		skip = false;
        		return;
        	}
        	
            String item = menuTypes.get(position);
            processMenuType(item);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }
    
    // 選擇菜單種類以後，設定選擇菜單項目資料
    private void processMenuType(String menuType) {
        // 讀取菜單種類編號
        String[] menuTypeData = menuType.split(" ~ ");
        int menuTypeId = Integer.parseInt(menuTypeData[0]);
        // 設定菜單元件顯示指定種類的菜單資料
        processMenuItem(menuTypeId);
    }
    
    // 設定菜單元件使用的菜單資料
    private void processMenuItem(int menuTypeId) {
    	// 取得資料庫存取元件
        MobileDB db = MobileDB.getMobileDB(MenuItemActivity.this);
        List<MenuItem> items;

        if (menuTypeId == 0) {
            // 0表示讀取所有菜單資料
            items = db.getAllMenuItem();
        }
        else {
            // 讀取指定菜單種類編號的菜單資料 
            items = db.getMenuItemByType(menuTypeId);
        }

        // 清除原來的菜單資料
        menuItems.clear();

        // 加入讀取的菜單資料
        for (MenuItem data : items) {
            menuItems.add(data.getId() + " ~ " + data.getName());
        }

        // 設定選擇菜單元件使用的資料
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(
                MenuItemActivity.this,
                android.R.layout.simple_spinner_item, menuItems);
        itemAdapter.setDropDownViewResource(
                R.layout.my_spinner_dropdown_item);
        menu_item_name.setAdapter(itemAdapter);
    }
    
    private class ButtonListner implements OnClickListener {

        @Override
        public void onClick(View view) {
            int id = view.getId();

            // 增加數量
            if (id == R.id.menu_item_add_number) {
                menuNumber++;
                menu_item_number.setText(Integer.toString(menuNumber));
            }
            // 增加數量
            else if (id == R.id.menu_item_minus_number) {
                if (menuNumber > 0) {
                    menuNumber--;
                    menu_item_number.setText(Integer.toString(menuNumber));
                }
            }
            // 確定
            else if (id == R.id.menu_item_ok) {
                // 檢查菜單資料
                if (checkItem()) {
                    // 讀取菜單資料、數量與備註
                    String menuNameValue = menuItems.get(menu_item_name
                            .getSelectedItemPosition());
                    String menuNumberValue = menu_item_number.getText()
                            .toString();
                    String menuNoteValue = menu_item_note.getText()
                            .toString();

                    // 取得菜單編號與名稱
                    String[] menuNameData = menuNameValue.split(" ~ ");

                    // 設定菜單編號、名稱、數量與備註
                    intent.putExtra("menu_item_id", menuNameData[0]);
                    intent.putExtra("menu_item_name", menuNameData[1]);
                    intent.putExtra("menu_item_number", menuNumberValue);
                    intent.putExtra("menu_item_note", menuNoteValue);

                    // 設定執行結果
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
            // 取消
            else if (id == R.id.menu_item_cancel) {
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        }

    }

    // 檢查菜單資料是否正確
    private boolean checkItem() {
        boolean result = true;
        // 讀取菜單數量
        String menuNumValue = menu_item_number.getText().toString();

        if (menuNumValue == null || menuNumValue.length() == 0
                || menuNumValue.equals("0")) {
            result = false;
            // 顯示錯誤訊息對話框
            AlertDialog.Builder ab = new AlertDialog.Builder(
                    MenuItemActivity.this);
            ab.setMessage(R.string.menu_item_number_etxt);
            ab.setTitle(android.R.string.dialog_alert_title);
            ab.setIcon(android.R.drawable.ic_dialog_alert);
            ab.setCancelable(false);
            ab.setPositiveButton(android.R.string.ok, null);
            ab.show();
        }

        return result;
    }

}
