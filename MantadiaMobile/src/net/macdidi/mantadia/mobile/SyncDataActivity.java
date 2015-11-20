package net.macdidi.mantadia.mobile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.macdidi.mantadia.domain.DataCollection;
import net.macdidi.mantadia.domain.Image;
import net.macdidi.mantadia.library.util.HttpClientUtil;
import net.macdidi.mantadia.library.util.UpdateMobileUtil;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * 資料同步Activity元件
 * 
 * @author macdidi
 */
public class SyncDataActivity extends Activity {

    // 資料、圖片同步進度，確定與取消按鈕元件
    private ProgressBar sync_data_db;
    private ProgressBar sync_data_image;
    private Button sync_data_ok;
    private Button sync_data_cancel;

    private boolean isDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sync_data);

        processViews();
        processControllers();

        // 如果是自動執行同步
        if (getIntent().getAction().equals("net.macdidi.mantadia.SYNC_START")) {
            processSync();
        }
    }

    private void processViews() {
        sync_data_db = (ProgressBar) findViewById(R.id.sync_data_db);
        sync_data_image = (ProgressBar) findViewById(R.id.sync_data_image);
        sync_data_ok = (Button) findViewById(R.id.sync_data_ok);
        sync_data_cancel = (Button) findViewById(R.id.sync_data_cancel);
    }

    private void processControllers() {
        // 註冊確定與取消按鈕監聽事件
        ButtonListener listener = new ButtonListener();
        sync_data_ok.setOnClickListener(listener);
        sync_data_cancel.setOnClickListener(listener);
    }

    // 確定與取消按鈕監聽類別
    private class ButtonListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            // 確定
            if (view == sync_data_ok) {
                if (!isDone) {
                    processSync();
                }
                else {
                    finish();
                }
            }
            // 取消
            else if (view == sync_data_cancel) {
                finish();
            }
        }
    }

    private void processSync() {
        // 移除取消按鈕
        sync_data_cancel.setVisibility(View.GONE);
        sync_data_ok.setEnabled(false);
        sync_data_ok.setText(getString(R.string.sync_working_txt));

        // 啟動同步資料與圖片檔案
        new SyncData().execute();
        new SyncFile().execute();
    }

    // 同步伺服器資料
    private class SyncData extends AsyncTask<String, Integer, String> {

        private int step = 0;
        
        @Override
        protected String doInBackground(String... params) {
            sync_data_db.setMax(3);
            sync_data_db.setProgress(step);

            // 更新菜單資料
            UpdateMobileUtil.updateMenuItem(SyncDataActivity.this);
            publishProgress();

            // 更新菜單種類資料
            UpdateMobileUtil.updateMenuType(SyncDataActivity.this);
            publishProgress();

            // 更新餐桌資料
            UpdateMobileUtil.updateTables(SyncDataActivity.this);
            publishProgress();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (isDone) {
                sync_data_ok.setEnabled(true);
                sync_data_ok.setText(getString(R.string.sync_done_txt));
            }
            
            isDone = true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            sync_data_db.setProgress(++step);
        }
    }

    // 同步伺服器圖片檔案
    private class SyncFile extends AsyncTask<String, Integer, String> {
        
        private int step = 0;
        
        @Override
        protected String doInBackground(String... params) {
            // 儲存圖片檔案的目錄
            File imageDir = new File(
                    Environment.getExternalStorageDirectory(), 
                    "/matadia/images/");

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
            
            // 設定進度元件
            sync_data_image.setMax(serverImageList.length);
            sync_data_image.setProgress(step);

            // 刪除圖片檔案目錄下伺服器已經不存在的檔案
            for (String fn : localImageList) {
                if (Arrays.binarySearch(serverImageList, fn) < 0) {
                    new File(imageDir, fn).delete();
                }
            }

            // 下載伺服器的圖片檔案
            for (String fn : serverImageList) {
                if (Arrays.binarySearch(localImageList, fn) < 0) {
                    FileOutputStream fos = null;
                    BufferedOutputStream bos = null;
                    
                    try {
                        // 傳送取得指定名稱圖片請求的URL
                        String url = HttpClientUtil.MOBILE_URL
                                + "UpdateImageServlet.do?fileName=" + fn;
                        // 傳送請求並取得回傳的圖片資料
                        byte[] imageBytes = HttpClientUtil.sendGetForByte(url);
                        
                        // 儲存圖片的輸出串流物件
                        fos = new FileOutputStream(new File(imageDir, fn));
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

                publishProgress();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (isDone) {
                sync_data_ok.setEnabled(true);
                sync_data_ok.setText(getString(R.string.sync_done_txt));
            }
            
            isDone = true;            
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            sync_data_image.setProgress(++step);
        }

    }

    private String[] getImageItem() {        
        String[] fileNames = new String[0];
        // 傳送取得所有圖片資料請求的URL
        String url = HttpClientUtil.MOBILE_URL + "ImageFileNameServlet.get";
        // 傳送求與取得回傳的資料
        String result = HttpClientUtil.sendPost(url);
        
        // 準備從XML轉換為物件
        Serializer serializer = new Persister();

        try {
            // 將伺服器回應的XML資訊轉換為物件
            DataCollection<Image> response = 
                    serializer.read(DataCollection.class, result);
            List<Image> list = response.getList();
            
            if (list.size() > 0) {
                fileNames = new String[list.size()];
                
                // 儲存所有圖片檔案名稱
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
