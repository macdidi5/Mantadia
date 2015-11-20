package net.macdidi.mantadia.library.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;

/**
 * HTTP請求與回應公用程式
 * 
 * @author macdidi
 */
public class HttpClientUtil {

	// 網頁應用程式伺服器IP位址、埠號、應用程式主目錄與路徑
    public static String SERVER_HOST = "192.168.1.1";
    public static String SERVER_PORT = "8080";
    public static String SERVER_CONTEXT = "MantadiaServer";
    public static String MOBILE_SERVICE = "mobile";

    // 網頁應用程式伺服器URL
    public static String SERVER_URL = "http://" + SERVER_HOST + ":"
            + SERVER_PORT + "/" + SERVER_CONTEXT + "/";
    // 網頁應用程式伺服器行動裝置服務URL
    public static String MOBILE_URL = SERVER_URL + MOBILE_SERVICE + "/";

    /**
     * 設定伺服器IP位址與埠號
     * 
     * @param host 伺服器IP位址
     * @param port 伺服器埠號
     */
    public static void setMobile_URL(String host, String port) {
        SERVER_HOST = host;
        SERVER_PORT = port;
        SERVER_URL = "http://" + SERVER_HOST + ":" + SERVER_PORT + "/"
                + SERVER_CONTEXT + "/";
        MOBILE_URL = SERVER_URL + MOBILE_SERVICE + "/";
    }

    /**
     * 設定Preference儲存的伺服器IP位址與埠號
     * 
     * @param context Android Context物件 
     */
    public static void setMobile_URL(Context context) {
    	// 讀取Preference儲存的伺服器IP位址與埠號
        SERVER_HOST = TurtleUtil.getServerIP(context);
        SERVER_PORT = TurtleUtil.getServerPort(context);
        
        setMobile_URL(SERVER_HOST, SERVER_PORT);
    }

    /**
     * 傳送HTTP POST請求
     * 
     * @param url 傳送請求的URL
     * @return HTTP回應的內容
     */
    public static String sendPost(String url) {
        return getResult(new HttpPost(url));
    }

    /**
     * 傳送HTTP POST請求
     * 
     * @param url 傳送請求的URL
     * @param url 傳送請求的資料
     * @return HTTP回應的內容
     */
    public static String sendPost(String url, List<NameValuePair> parameters) {
        UrlEncodedFormEntity entity = null;

        try {
        	// 建立包裝請求資料的Entity物件，指定資料編碼為UTF8
            entity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
        }
        catch (UnsupportedEncodingException e) {
            return null;
        }

        // 建立HTTP POST請求物件
        HttpPost request = new HttpPost(url);
        request.setEntity(entity);

        return getResult(request);
    }

    /**
     * 傳送HTTP GET請求
     * 
     * @param url 傳送請求的URL
     * @return HTTP回應的內容
     */
    public static String sendGet(String url) {
        return getResult(new HttpGet(url));
    }

    // 傳送請求後，讀取伺服器回應的資料
    private static String getResult(HttpRequestBase base) {
        String result = null;

        try {
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 3000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 3000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            
            // 執行請求與取得回應物件
            HttpResponse response = httpClient.execute(base);

            // 判斷HTTP請求是否成功
            if (response.getStatusLine().getStatusCode() == 200) {
            	// 讀取HTTP回應的內容
                result = EntityUtils.toString(response.getEntity());
            }
        }
        catch (ConnectTimeoutException cte) {
        	result = "Network Exception: " + cte;
        }
        catch (ClientProtocolException cpe) {
            result = "Network Exception: " + cpe;
        }
        catch (IOException ioe) {
            result = "Network Exception: " + ioe;
        }

        return result;
    }

    /**
     * 傳送HTTP GET請求，取得位元型態的回傳資料
     * 
     * @param url 傳送請求的URL
     * @return HTTP回應的內容
     */
    public static byte[] sendGetForByte(String url) {
        return getByteResult(new HttpGet(url));
    }

    // 傳送請求後，讀取伺服器回應的位元型態資料
    private static byte[] getByteResult(HttpRequestBase base) {
        byte[] result = null;

        try {
            HttpResponse response = new DefaultHttpClient().execute(base);

            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toByteArray(response.getEntity());
            }
        }
        catch (ClientProtocolException cpe) {

        }
        catch (IOException ioe) {

        }

        return result;
    }

}
