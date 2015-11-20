package net.macdidi.mantadia.kitchen.main;

import java.rmi.Naming;

import javafx.application.Application;
import javafx.stage.Stage;
import net.macdidi.mantadia.kitchen.model.KitchenModel;
import net.macdidi.mantadia.kitchen.view.KitchenView;
import net.macdidi.mantadia.kitchen.view.KitchenViewImpl;

public class KitchenMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        // RMI伺服器名稱
        String server = "localhost";

        // 建立RMI服務URL
        String url = "rmi://" + server + ":" + 
                     KitchenModel.RMI_SERVICE_PORT + "/" + 
        		     KitchenModel.RMI_SERVICE_NAME;

        try {
            // 取得伺服器Model物件
            KitchenModel model = (KitchenModel) Naming.lookup(url);
            // 建立用戶端View物件
            KitchenView view = new KitchenViewImpl(model, primaryStage);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 啟動JavaFX應用程式
        launch(args);
    }

}
