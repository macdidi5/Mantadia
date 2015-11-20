package net.macdidi.mantadia.domain;

/**
 * 訂單明細
 * 
 * @author macdidi
 */
public class OrderItem implements java.io.Serializable {

    // 編號、訂單編號、菜單編號、數量、狀態、備註、菜單名稱、價格、數量、序號、圖片檔名
    private int id;
    private int ordersId;
    private int menuItemId;
    private int number;
    private int stauts;
    private String note;

    private String menuItemName;
    private int menuItemPrice;
    private int amount;
    private int serial;
    private String imageFileName;

    public OrderItem() {

    }

    /**
     * 新增訂單明細物件
     * 
     * @param id 編號
     * @param ordersId 訂單編號
     * @param menuItemId 菜單編號
     * @param number 數量
     * @param stauts 狀態
     * @param note 備註
     */
    public OrderItem(int id, int ordersId, int menuItemId, int number,
            int stauts, String note) {
        this.id = id;
        this.ordersId = ordersId;
        this.menuItemId = menuItemId;
        this.number = number;
        this.stauts = stauts;
        this.note = note;
    }

    /**
     * 新增訂單明細物件
     * 
     * @param id 編號
     * @param ordersId 訂單編號
     * @param menuItemId 菜單編號
     * @param number 數量
     * @param stauts 狀態
     * @param note 備註
     * @param menuItemName 菜單名稱
     */
    public OrderItem(int id, int ordersId, int menuItemId, int number,
            int stauts, String note, String menuItemName) {
        this(id, ordersId, menuItemId, number, stauts, note);
        this.menuItemName = menuItemName;
    }

    /**
     * 新增訂單明細物件
     * 
     * @param id 編號
     * @param ordersId 訂單編號
     * @param menuItemId 菜單編號
     * @param number 數量
     * @param stauts 狀態
     * @param note 備註
     * @param menuItemName 菜單名稱
     * @param menuItemPrice 菜單價格
     * @param amount 金額
     * @param serial 序號
     */
    public OrderItem(int id, int ordersId, int menuItemId, int number,
            int stauts, String note, String menuItemName,
            int menuItemPrice, int amount, int serial) {
        this(id, ordersId, menuItemId, number, stauts, note, menuItemName);
        this.menuItemPrice = menuItemPrice;
        this.amount = amount;
        this.serial = serial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getStauts() {
        return stauts;
    }

    public void setStauts(int stauts) {
        this.stauts = stauts;
    }

    public String getNote() {
        return note == null ? "" : note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public int getMenuItemPrice() {
        return menuItemPrice;
    }

    public void setMenuItemPrice(int menuItemPrice) {
        this.menuItemPrice = menuItemPrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

}
