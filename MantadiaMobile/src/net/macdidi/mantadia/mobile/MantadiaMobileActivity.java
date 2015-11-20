package net.macdidi.mantadia.mobile;

import net.macdidi.mantadia.domain.User;
import net.macdidi.mantadia.library.util.HttpClientUtil;
import net.macdidi.mantadia.library.util.TurtleUtil;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 主畫面Activity元件，登入
 * 
 * @author macdidi
 */
public class MantadiaMobileActivity extends Activity {

    // 帳號、密碼、登入、結束與設定元件
    private EditText login_account;
    private EditText login_password;
    private Button login_ok;
    private Button login_exit;
    private Button login_config;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // 設定執行緒運作模式
        StrictMode.ThreadPolicy policy = 
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // 檢查是否開啟網路
        checkNetwork();
        // 設定伺服器資訊
        HttpClientUtil.setMobile_URL(this);

        processViews();
        processControllers();
    }

    private void processViews() {
        setTitle(R.string.login_title_txt);
        login_account = (EditText) findViewById(R.id.login_account);
        login_password = (EditText) findViewById(R.id.login_password);
        login_ok = (Button) findViewById(R.id.login_ok);
        login_exit = (Button) findViewById(R.id.login_exit);
        login_config = (Button) findViewById(R.id.login_config);
    }

    private void processControllers() {
        ButtonListener listener = new ButtonListener();

        login_ok.setOnClickListener(listener);
        login_exit.setOnClickListener(listener);
        login_config.setOnClickListener(listener);
    }

    private class ButtonListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            Intent intent;

            switch (id) {
            // 設定
            case R.id.login_config:
                // 啟動設定元件
                intent = new Intent(MantadiaMobileActivity.this,
                        ConfigActivity.class);
                startActivity(intent);
                break;
            // 執行登入
            case R.id.login_ok:
                // 檢查登入是否成功
                if (checkLogin() && isLogin()) {
                    // 啟動選單主畫面元件
                    intent = new Intent(MantadiaMobileActivity.this,
                            MainMenuActivity.class);
                    startActivity(intent);
                }
                break;
            // 結束
            case R.id.login_exit:
                finish();
                break;
            }
        }
    }

    private boolean isLogin() {
        // 讀取輸入的帳號與密碼
        String account = login_account.getText().toString();
        String password = login_password.getText().toString();

        // 執行登入檢查的URL
        String url = HttpClientUtil.MOBILE_URL + "LoginServlet.do?account="
                + account + "&password=" + password;
        // 傳送URL並接收回應
        String result = HttpClientUtil.sendGet(url);

        boolean loginSuccess = true;
        // 準備從XML轉換為物件
        Serializer serializer = new Persister();

        try {
            // 將伺服器回應的XML資訊轉換為物件
            User user = serializer.read(User.class, result);
            
            if (user.getId() == 0) {
                // 登入失敗
                loginErrorDialog(getString(R.string.login_error_retry_txt));
                loginSuccess = false;                
            }
            else {
                // 設定使用者帳戶資訊
                TurtleUtil.setAccount(MantadiaMobileActivity.this, 
                        Integer.toString(user.getId()), user.getName());
            }
        }
        catch (Exception e) {
            Log.d(getClass().getName(), e.toString());
            loginErrorDialog(result);
            loginSuccess = false;
        }
        
        return loginSuccess;
    }

    // 檢查帳號與密碼元件的輸入內容
    private boolean checkLogin() {
        // 錯誤訊息
        StringBuffer errorMsg = new StringBuffer();
        // 讀取輸入的帳號與密碼
        String account = login_account.getText().toString();
        String password = login_password.getText().toString();

        // 沒有輸入帳號
        if (account.equals("")) {
            errorMsg.append(getString(R.string.login_error_account_txt)
                    + "\n");
        }

        // 沒有輸入密碼
        if (password.equals("")) {
            errorMsg.append(getString(R.string.login_error_password_txt)
                    + "\n");
        }

        // 如果有任何錯誤訊息就顯示對話框
        if (errorMsg.length() > 0) {
            loginErrorDialog(errorMsg.toString());
            return false;
        }
        else {
            return true;
        }
    }

    // 顯示錯誤訊息對話框
    private void loginErrorDialog(String message) {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage(message);
        ab.setCancelable(false);
        ab.setPositiveButton(getString(android.R.string.ok), null);
        ab.show();
    }

    // 檢查網路連線
    private void checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) 
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info == null || !info.isConnected()) {
            // 顯示沒有開啟網路連線對話框
            AlertDialog.Builder ab = new AlertDialog.Builder(
                    MantadiaMobileActivity.this);
            ab.setMessage(R.string.login_error_network_txt);
            ab.setTitle(android.R.string.dialog_alert_title);
            ab.setIcon(android.R.drawable.ic_dialog_alert);
            ab.setCancelable(false);
            ab.setPositiveButton(getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            ab.show();
        }
    }

}