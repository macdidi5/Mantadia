package net.macdidi.mantadia.kitchen.gui;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.macdidi.mantadia.domain.OrderItem;
import net.macdidi.mantadia.domain.Orders;
import net.macdidi.mantadia.kitchen.model.KitchenModel;
import net.macdidi.mantadia.kitchen.view.KitchenView;

/**
 * 廚房畫面
 * 
 * @author macdidi
 */
public class KitchenGUI {

    private OrderPane[] ops = new OrderPane[6];
    private Map<Orders, List<OrderItem>> data;
    private KitchenModel model;
    private KitchenView view;

    private GridPane root;

    public KitchenGUI(final KitchenModel model, final KitchenView view,
            final Stage primaryStage) {
        this.model = model;
        this.view = view;

        // 建立儲存訂單資訊的Map物件
        data = new TreeMap<>();
        // 建立畫面
        constructGUI();

        // 建立主畫面並設定寬與高
        Scene scene = new Scene(root, 900, 800);
        // 取得與設定樣式
        String css = KitchenGUI.class.getResource("mystyle.css")
                .toExternalForm();
        scene.getStylesheets().add(css);
        
        // 設定應用程式標題、主畫面與不可以變動視窗大小
        primaryStage.setTitle("Mantadia Kitchen");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        // 關閉視窗
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent t) {
                try {
                    // 移除用戶端
                    model.removeKitchenView(KitchenGUI.this.view);
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }

                // 結束應用程式
                System.exit(0);
            }

        });

        // 顯示應用程式畫面
        primaryStage.show();
    }

    private void constructGUI() {
        // 建立應用程式畫面的主要畫面配置
        root = new GridPane();
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10, 10, 10, 10));

        // 建立六個訂單資訊面板
        for (int i = 0; i < ops.length; i++) {
            ops[i] = new OrderPane();
        }

        // 加入訂單資訊面板
        root.addRow(0, ops[0].getOrderPane(), ops[1].getOrderPane(),
                ops[2].getOrderPane());
        root.addRow(1, ops[3].getOrderPane(), ops[4].getOrderPane(),
                ops[5].getOrderPane());
    }

    /**
     * 加入訂單
     */
    public void addOrders(Orders orders, List<OrderItem> orderItem) {
        if (data.containsKey(orders)) {
            data.remove(orders);
        }
        
        data.put(orders, orderItem);
        refresh();
    }

    /**
     * 移除訂單
     */
    public void removeOrders(Orders orders) {
        data.remove(orders);
        refresh();
    }

    private void refresh() {
        // 清除所有訂單資訊面板內容
        for (OrderPane op : ops) {
            op.clearOrders();
        }

        // 重新設定訂單資訊面板
        Set<Orders> set = data.keySet();
        int i = 0;

        for (Orders orders : set) {
            if (i < ops.length) {
                ops[i].setOrders(orders, data.get(orders));
                i++;
            }
        }
    }

    /**
     * 訂單資訊面板類別
     * 
     * @author macdidi5
     */
    private class OrderPane {
        // 訂單資訊面板主要畫面配置
        private BorderPane orderPane = new BorderPane();
        // 訂單編號與桌號資訊
        private HBox infoPane = new HBox();
        // 操作按鈕
        private HBox orderControlPane = new HBox();
        // 訂單明細資料
        private ObservableList<VBox> items = 
                FXCollections.observableArrayList();
        private ListView<VBox> orderItems = new ListView<>(items);
        private Button done = new Button("完成");
        private Button prepare = new Button("準備中");
        // 訂單編號元件與資料
        private Label orderInfoLabel = new Label("");
        private StringProperty infoProp = new SimpleStringProperty();

        private boolean enable;
        // 訂單物件
        private Orders orders;

        public OrderPane() {
            // 連結訂單編號資料
            orderInfoLabel.textProperty().bind(infoProp);

            // 加入訂單編號與桌號資訊
            infoPane.getChildren().addAll(orderInfoLabel);
            infoPane.getStyleClass().addAll("hbox");

            // 設定完成與準備中按鈕元件的寬度
            HBox.setHgrow(done, Priority.ALWAYS);
            HBox.setHgrow(prepare, Priority.ALWAYS);
            done.setMaxWidth(Double.MAX_VALUE);
            prepare.setMaxWidth(Double.MAX_VALUE);

            // 加入按鈕元件
            orderControlPane.getChildren().addAll(done, prepare);
            orderControlPane.getStyleClass().addAll("hbox");

            // 加入所有元件到主要畫面配置
            orderPane.setTop(infoPane);
            orderPane.setBottom(orderControlPane);
            orderPane.setCenter(orderItems);
            
            // 設定訂單面板的寬、高與樣式
            orderPane.setPrefHeight(400);
            orderPane.setPrefWidth(300);
            orderPane.getStyleClass().addAll("order-panel");            

            // 設定訂單明細樣式
            orderItems.getStyleClass().addAll("order-item-panel");
            // 設定訂單明細為不可選擇
            orderItems.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Object>() {
                    @Override
                    public void changed(ObservableValue<?> observable,
                            Object oldvalue, Object newValue) {
                        Platform.runLater(new Runnable() {
                            public void run() {
                                orderItems.getSelectionModel().select(-1);
                            }
                        });
                    }
                });

            // 使用者選擇「完成」按鈕
            done.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    try {
                        // 通知伺服器把訂單設定為「完成」
                        model.setOrderDone(orders.getId());
                    }
                    catch (RemoteException re) {
                        re.printStackTrace();
                    }

                    // 兩秒後移除畫面上的訂單資訊
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            removeOrders(orders);
                        }
                    });
                }
            });

            // 使用者選擇「準備中」按鈕
            prepare.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    try {
                        orders.setStatus(1);
                        // 通知伺服器把訂單設定為「準備中」
                        model.setOrderPrepare(orders.getId());
                    }
                    catch (RemoteException re) {
                        re.printStackTrace();
                    }

                    // 設定顏色
                    orderPane.setStyle("-fx-border-color: violet;");
                }
            });

            configButton();
        }

        /**
         * 設定訂單與明細
         * 
         * @param orders 訂單
         * @param orderItem 訂單明細
         */
        public void setOrders(Orders orders, List<OrderItem> orderItem) {
            this.orders = orders;
            // 設定訂單編號與桌號
            infoProp.set(String.format("訂單:  %d   桌號:  %d", 
                    orders.getId(), orders.getTablesId()));
            // 清除訂單明細
            items.clear();

            // 加入訂單明細
            for (OrderItem item : orderItem) {
                items.add(getItemLabel(item.getMenuItemName(),
                        item.getNumber(), item.getNote()));
            }

            // 設定顏色
            if (orders.getStatus() == 1) {
                orderPane.setStyle("-fx-border-color: violet;");
            }

            enable = true;
            configButton();
        }

        /**
         * 清除訂單
         */
        public void clearOrders() {
            orders = null;
            infoProp.set("");
            items.clear();
            enable = false;
            configButton();
            orderPane.setStyle("-fx-border-color: grey;");
        }

        public BorderPane getOrderPane() {
            return orderPane;
        }

        /**
         * 建立訂單明細資訊容器
         * 
         * @param menuitem 菜單名稱
         * @param amount 數量
         * @param note 備註
         * @return 訂單明細容器
         */
        private VBox getItemLabel(String menuitem, int number, String note) {
            // 建立垂直排列容器
            VBox result = new VBox();
            result.setMaxWidth(Double.MAX_VALUE);

            // 加入菜單名稱與數量
            Label itemLabel = new Label(menuitem + "    ( " + number + " )");
            itemLabel.setId("order-item-label");
            result.getChildren().add(itemLabel);

            // 加入備註
            if (note != null && note.length() > 0) {
                Label noteLabel = new Label(note);
                noteLabel.setMaxWidth(Double.MAX_VALUE);
                noteLabel.setAlignment(Pos.BASELINE_RIGHT);
                noteLabel.setId("order-item-note-label");
                result.getChildren().add(noteLabel);
            }

            return result;
        }

        private void configButton() {
            done.setDisable(!enable);
            prepare.setDisable(!enable);
        }
    }

}
