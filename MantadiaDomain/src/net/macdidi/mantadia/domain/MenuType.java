package net.macdidi.mantadia.domain;

/**
 * 菜單種類
 * 
 * @author macdidi
 */
public class MenuType {

    // 編號、名稱、備註
    private int id;
    private String name;
    private String note;

    public MenuType() {

    }

    /**
     * 建立菜單種類物件
     * 
     * @param id 編號
     * @param name 名稱
     * @param note 備註
     */
    public MenuType(int id, String name, String note) {
        this.id = id;
        this.name = name;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note == null ? "" : note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String toString() {
        return id + " - " + name + " - " + getNote();
    }

}
