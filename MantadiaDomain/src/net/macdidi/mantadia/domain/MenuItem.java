package net.macdidi.mantadia.domain;

/**
 * 菜單
 * 
 * @author macdidi
 */
public class MenuItem implements java.io.Serializable {

    // 編號、菜單種類編號、名稱、價格、備註、圖片編號、菜單種類名稱、圖片檔案名稱
    private int id;
    private int menuTypeId;
    private String name;
    private int price;
    private String note;
    private int imageId;

    private String menuTypeName;
    private String imageFileName;

    public MenuItem() {

    }

    /**
     * 建立菜單物件
     * 
     * @param id 編號
     * @param menuTypeId 菜單種類編號
     * @param name 名稱
     * @param price 價格
     * @param note 備註
     * @param imageId 圖片編號
     */
    public MenuItem(int id, int menuTypeId, String name, int price,
            String note, int imageId) {
        this.id = id;
        this.menuTypeId = menuTypeId;
        this.name = name;
        this.price = price;
        this.note = note;
        this.imageId = imageId;
    }

    /**
     * 建立菜單物件
     * 
     * @param id 編號
     * @param menuTypeId 菜單種類編號
     * @param name 名稱
     * @param price 價格
     * @param note 備註
     * @param imageId 圖片編號
     * @param menuTypeName 菜單種類名稱
     * @param imageFileName 圖片檔案名稱
     */
    public MenuItem(int id, int menuTypeId, String name, int price,
            String note, int imageId, String menuTypeName,
            String imageFileName) {
        this(id, menuTypeId, name, price, note, imageId);
        this.menuTypeName = menuTypeName;
        this.imageFileName = imageFileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMenuTypeId() {
        return menuTypeId;
    }

    public void setMenuTypeId(int menuTypeId) {
        this.menuTypeId = menuTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getNote() {
        return note == null ? "" : note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getMenuTypeName() {
        return menuTypeName;
    }

    public void setMenuTypeName(String menuTypeName) {
        this.menuTypeName = menuTypeName;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    @Override
    public String toString() {
        return id + " - " + menuTypeId + " - " + name + " - " + price
                + " - " + getNote() + " - " + getImageId() + " - "
                + getMenuTypeName() + " - " + getImageFileName();
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof MenuItem)) {
            return false;
        }
        
        return (((MenuItem)obj).id == this.id);
    }

}
