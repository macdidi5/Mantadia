package net.macdidi.mantadia.domain;

/**
 * 圖片
 * 
 * @author macdidi
 */
public class Image {

    // 編號、檔案名稱、備註
    private int id;
    private String fileName;
    private String note;

    public Image() {

    }
    
    /**
     * 建立圖片物件
     * 
     * @param id 編號
     * @param fileName 檔案名稱
     * @param note 備註
     */
    public Image(int id, String fileName, String note) {
        this.id = id;
        this.fileName = fileName;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getNote() {
        return note == null ? "" : note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
