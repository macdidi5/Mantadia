package net.macdidi.mantadia.mobile;

import net.macdidi.mantadia.library.util.HttpClientUtil;
import net.macdidi.mantadia.library.util.TurtleUtil;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * 設定Activity元件
 * 
 * @author macdidi
 */
public class ConfigActivity extends PreferenceActivity implements
        OnSharedPreferenceChangeListener {

    // 伺服器IP位址、埠號與自動更新元件
    private EditTextPreference serverIP;
    private EditTextPreference serverPort;
    private CheckBoxPreference autoUpdate;

    // 設定資訊物件
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 載入設定畫面資源
        addPreferencesFromResource(R.xml.mypreference);

        // 取得伺服器IP位址、埠號與自動更新元件
        serverIP = (EditTextPreference) getPreferenceScreen()
                .findPreference(TurtleUtil.KEY_SERVER_IP);
        serverPort = (EditTextPreference) getPreferenceScreen()
                .findPreference(TurtleUtil.KEY_SERVER_PORT);
        autoUpdate = (CheckBoxPreference) getPreferenceScreen()
                .findPreference(TurtleUtil.KEY_AUTO_UPDATE);

        // 取得設定資訊物件
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 設定伺服器IP位址、埠號與自動更新說明
        serverIP.setSummary(serverIP.getText());
        serverPort.setSummary(serverPort.getText());
        autoUpdate.setSummary(
                    prefs.getBoolean(TurtleUtil.KEY_AUTO_UPDATE, false) ? 
                    getString(R.string.config_server_sync_on_txt) :
                    getString(R.string.config_server_sync_off_txt));

        // 註冊資訊更改監聽物件
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(
            SharedPreferences sharedPreferences, String key) {
        // 設定伺服器IP位址說明
        if (key.equals(TurtleUtil.KEY_SERVER_IP)) {
            serverIP.setSummary(serverIP.getText());
            HttpClientUtil.setMobile_URL(serverIP.getText(),
                    serverPort.getText());
        }
        // 設定埠號說明
        else if (key.equals(TurtleUtil.KEY_SERVER_PORT)) {
            serverPort.setSummary(serverPort.getText());
            HttpClientUtil.setMobile_URL(serverIP.getText(),
                    serverPort.getText());
        }
        // 設定自動更新說明
        else if (key.equals(TurtleUtil.KEY_AUTO_UPDATE)) {
            autoUpdate.setSummary(
                    prefs.getBoolean(TurtleUtil.KEY_AUTO_UPDATE, false) ? 
                    getString(R.string.config_server_sync_on_txt) :
                    getString(R.string.config_server_sync_off_txt));
        }
    }

}
