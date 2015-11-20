package net.macdidi.mantadia.domain;

/**
 * 使用者
 * 
 * @author macdidi
 */
public class User {

    // 編號、帳號、密碼、姓名、性別、職務、備註、圖片編號、圖片檔名
    private int id;
    private String account;
    private String password;
    private String name;
    private int gender;
    private int role;
    private String note;
    private int imageId;
    private String imageFileName;

    public User() {

    }

    /**
     * 建立User物件
     * 
     * @param id 編號
     * @param account 帳號
     * @param password 密碼
     * @param name 姓名
     * @param gender 性別
     * @param role 職物
     * @param note 備註
     * @param imageId 圖片編號
     */
    public User(int id, String account, String password, String name,
            int gender, int role, String note, int imageId) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.role = role;
        this.note = note;
        this.imageId = imageId;
    }

    /**
     * 建立User物件
     * 
     * @param id 編號
     * @param account 帳號
     * @param password 密碼
     * @param name 姓名
     * @param gender 性別
     * @param role 職物
     * @param note 備註
     * @param imageId 圖片編號
     * @param imageFileName 圖片檔案名稱
     */
    public User(int id, String account, String password, String name,
            int gender, int role, String note, int imageId,
            String imageFileName) {
        this(id, account, password, name, gender, role, note, imageId);
        this.imageFileName = imageFileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public String getGenderName() {
        return gender == 0 ? "女" : "男";
    }

    /**
     * 設定性別
     * 
     * @param gender 性別，0：女，1：男
     */
    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getRole() {
        return role;
    }

    public String getRoleName() {
        String result = "";

        if (role >= 0 && role <= 3) {
            String[] roleNames = getRoleNameList();
            result = roleNames[role];
        }

        return result;
    }

    public static String[] getRoleNameList() {
        return new String[] { "管理員", "收銀員", "服務生", "廚師" };
    }

    /**
     * 設定職務
     * 
     * @param role 職務編號，0：管理員，1：收銀員，2：服務生，3：廚師
     */
    public void setRole(int role) {
        this.role = role;
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

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String toString() {
        return id + " - " + account + " - " + password + " - " + name
                + " - " + getGenderName() + " - " + getRoleName() + " - "
                + getNote() + " - " + getImageFileName();
    }

}
