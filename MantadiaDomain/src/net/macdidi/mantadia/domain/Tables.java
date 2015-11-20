package net.macdidi.mantadia.domain;

/**
 * 餐桌
 * 
 * @author macdidi
 */
public class Tables {

    // 編號、狀態、備註、訂單
    private int id;
    private int status;
    private String note;
    private Orders orders;

    public Tables() {

    }

    /**
     * 建立餐桌物件
     * 
     * @param id 編號
     * @param status 狀態
     * @param note 備註
     */
    public Tables(int id, int status, String note) {
        this.id = id;
        this.status = status;
        this.note = note;
    }

    /**
     * 建立餐桌物件
     * 
     * @param id 編號
     * @param status 狀態
     * @param note 備註
     * @param orders 訂單
     */
    public Tables(int id, int status, String note, Orders orders) {
        this(id, status, note);
        this.orders = orders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusName() {
        return status == 0 ? "空桌" : "有客人";
    }

    /**
     * 設定餐桌狀態
     * 
     * @param status 餐桌狀態，0：空桌，1：有客人
     */
    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note == null ? "" : note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "<td>" + getId() + "</td>" +
               "<td>" + getStatusName() + "</td>" +
               "<td>" + (orders.getId() == 0 ? "" : orders.getId()) + "</td>" +
               "<td>" + (orders.getTime() == null ? "" : orders.getTime()) + "</td>" +
               "<td>" + (orders.getUserName() == null ? "" : orders.getUserName()) + "</td>" +
               "<td>" + (orders.getNumber() == 0 ? "" : orders.getNumber()) + "</td>" +
               "<td>" + orders.getStatusName() + "</td>"; 
    }

}
