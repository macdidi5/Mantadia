package net.macdidi.mantadia.emenu;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.macdidi.mantadia.domain.DataCollection;
import net.macdidi.mantadia.domain.Image;
import net.macdidi.mantadia.domain.User;
import net.macdidi.mantadia.library.util.HttpClientUtil;
import net.macdidi.mantadia.library.util.TurtleUtil;
import net.macdidi.mantadia.library.util.UpdateMobileUtil;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;

public class MantadiaEMenuActivity extends Activity {
    
    // 帳號與密碼元件
    private EditText login_account;
    private EditText login_password;
    
    // 切換設定面版按鈕元件
    private ImageButton switch_panel;
    // 設定面版元件
    private TableLayout config_panel;
    
    // 伺服器IP位址與埠號元件
    private EditText server_ip;
    private EditText server_port;
    
    // 伺服器位址與埠號
    private String serverIp;
    private String serverPort;
    
    // 是否顯示設定面版
    private boolean configPanel;
    
    // 同步資料進度對話框
    private ProgressDialog progressDialog;
    
    // 圖片儲存目錄
    public static final String PICTURE_FOLDER = "/matadia/images/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        // 設定執行緒運作模式
        StrictMode.ThreadPolicy policy = 
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // 檢查是否開啟網路
        checkNetwork();

        processViews();
        
        // 讀取設定資訊
        readPreference();
    }

    private void processViews() {
        setTitle(R.string.login_title_txt);
        
        login_account = (EditText) findViewById(R.id.login_account);
        login_password = (EditText) findViewById(R.id.login_password);        
        switch_panel = (ImageButton) findViewById(R.id.switch_panel);
        config_panel = (TableLayout) findViewById(R.id.config_panel);
        server_ip = (EditText) findViewById(R.id.server_ip);
        server_port = (EditText) findViewById(R.id.server_port);
    }
    
    // 確定與結束按鈕
    public void okAndExit(View view) {
        savePrefernece();
        int id = view.getId();

        switch (id) {
        // 確定
        case R.id.login_ok:
            // 儲存設定資訊
            savePrefernece();

            // 檢查登入是否成功
            if (checkLogin() && isLogin()) {
                // 啟動同步資料
                new SyncData().execute();
            }
            
            break;
        // 結束
        case R.id.login_exit:
            finish();
            break;
        }        
    }            
    
    // 切換設定面版
    public void switchConfigPanel(View view) {
        if (view != null) {
            configPanel = !configPanel;
        }
        
        // 設定切換設定面板的按鈕圖示
        switch_panel.setImageResource(
                configPanel ? R.drawable.collapse : R.drawable.expand);
        // 顯示或隱藏設定面板
        config_panel.setVisibility(configPanel ? View.VISIBLE
                : View.INVISIBLE);
    }
    
    private boolean isLogin() {
        // 讀取輸入的帳號與密碼
        String account = login_account.getText().toString();
        String password = login_password.getText().toString();

        // 除送登入檢查請求的URL
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
                TurtleUtil.setAccount(MantadiaEMenuActivity.this, 
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
                    MantadiaEMenuActivity.this);
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
    
    private void readPreference() {
        SharedPreferences sp = 
                PreferenceManager.getDefaultSharedPreferences(this);
        
        // 讀取伺服器位址與埠號
        serverIp = sp.getString(TurtleUtil.KEY_SERVER_IP, "192.168.1.1");
        serverPort = sp.getString(TurtleUtil.KEY_SERVER_PORT, "8080");
        
        // 設定伺服器位址與埠號顯示元件
        server_ip.setText(serverIp);
        server_port.setText(serverPort);
    }
    
    private void savePrefernece() {
        // 讀取元件的伺服器位址與埠號
        serverIp = server_ip.getText().toString();
        serverPort = server_port.getText().toString();
        
        // 儲存伺服器位址與埠號
        SharedPreferences.Editor editor = 
                PreferenceManager.getDefaultSharedPreferences(this).edit();
        
        editor.putString(TurtleUtil.KEY_SERVER_IP, serverIp);
        editor.putString(TurtleUtil.KEY_SERVER_PORT, serverPort);
        
        editor.commit();
        
        // 設定伺服器資訊
        HttpClientUtil.setMobile_URL(serverIp, serverPort);
    }
    
    // 同步伺服器資料
    private class SyncData extends AsyncTask<String, Integer, String> {
        
        @Override
        protected void onPreExecute() {
            // 建立與設定進度對話框
            progressDialog = new ProgressDialog(MantadiaEMenuActivity.this);
            progressDialog.setMessage(getString(R.string.sync_working_txt));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // 顯示進度對話框
            progressDialog.show();            
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // 設定對話框進度
            progressDialog.incrementProgressBy(values[0]);
        }

        @Override
        protected String doInBackground(String... params) {
            // 同步菜單、菜單種類與餐桌資料
            UpdateMobileUtil.updateMenuItem(MantadiaEMenuActivity.this);
            UpdateMobileUtil.updateMenuType(MantadiaEMenuActivity.this);
            UpdateMobileUtil.updateTables(MantadiaEMenuActivity.this);
            
            // 儲存圖片的目錄
            File imageDir = new File(
                    Environment.getExternalStorageDirectory(), 
                    PICTURE_FOLDER);

            // 檢查與建立儲存圖片檔案的目錄
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }

            // 讀取圖片檔案目錄下所有檔案名稱
            String[] localImageList = imageDir.list();
            Arrays.sort(localImageList);
            
            // 取得伺服器所有圖片檔案名稱
            String[] serverImageList = getImageItem();
            Arrays.sort(serverImageList);

            // 刪除圖片檔案目錄下伺服器已經不存在的檔案
            for (String fileName : localImageList) {
                if (Arrays.binarySearch(serverImageList, fileName) < 0) {
                    new File(imageDir, fileName).delete();
                }
            }
            
            // 設定進度對話框的數量
            progressDialog.setMax(serverImageList.length);
            
            // 下載伺服器的圖片檔案
            for (String fileName : serverImageList) {
                if (Arrays.binarySearch(localImageList, fileName) < 0) {
                    downloadImage(imageDir, fileName);
                    // 設定進度
                    publishProgress(1);
                }
            }
            
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // 移除進度對話框 
            progressDialog.dismiss();
            
            // 啟動選擇桌號元件
            Intent intent = new Intent(MantadiaEMenuActivity.this,
                    SelectTableActivity.class);
            startActivityForResult(intent , 0);
        }
    }
    
    // 下載與儲存伺服器的圖片檔案
    private void downloadImage(File imageDir, String fileName) {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        
        try {
            // 傳送取得指定名稱圖片請求的URL
            String url = HttpClientUtil.MOBILE_URL
                    + "UpdateImageServlet.do?fileName=" + fileName;
            // 傳送請求並取得回傳的圖片資料
            byte[] imageBytes = HttpClientUtil.sendGetForByte(url);
            
            // 儲存圖片的輸出串流物件
            fos = new FileOutputStream(new File(imageDir, fileName));
            bos = new BufferedOutputStream(fos);
            
            // 儲存圖片
            bos.write(imageBytes);
            bos.close();
        }
        catch (IOException e) {
            Log.e("SyncDataActivity", "Error: " + e);
        }
        finally {
            if (bos != null) {
                try {
                    bos.close();
                }
                catch (IOException e) { }
            }
        }
    }
    
    // 讀取伺服器所有圖片檔案名稱
    private String[] getImageItem() {
        String[] fileNames = new String[0];
        String url = HttpClientUtil.MOBILE_URL + "ImageFileNameServlet.get";
        String result = HttpClientUtil.sendPost(url);
        
        // 準備從XML轉換為物件
        Serializer serializer = new Persister();

        try {
            // 將伺服器回應的XML資訊轉換為物件
            DataCollection<Image> response = 
                    serializer.read(DataCollection.class, result);
            List<Image> list = response.getList();
            
            if (list.size() > 0) {
                // 把所有圖片名稱儲存為字串陣列
                fileNames = new String[list.size()];
                
                for (int i = 0; i < list.size(); i++) {
                    fileNames[i] = list.get(i).getFileName();
                }
            }
        }
        catch (Exception e) {
            Log.d(getClass().getName(), e.toString());
        }            
        
        return fileNames;
    }
    
}
