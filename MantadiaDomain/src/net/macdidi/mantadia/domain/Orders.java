package net.macdidi.mantadia.domain;

/**
 * 訂單
 * 
 * @author macdidi
 */
public class Orders implements java.io.Serializable, Comparable<Orders> {

    // 編號、日期時間、使用者編號、餐桌編號、人數、狀態、備註、使用者姓名、總金額
	private int id;
	private String time;
	private int userId;
	private int tablesId;
	private int number;
	private int status;
	private String note;
	private String userName;
	private int amount;

	public Orders() {

	}

	/**
	 * 建立訂單物件
	 * 
	 * @param id 編號
	 * @param time 日期時間
	 * @param userId 使用者編號
	 * @param tablesId 餐桌編號
	 * @param number 人數
	 * @param status 狀態
	 * @param note 備註
	 */
	public Orders(int id, String time, int userId, int tablesId, int number,
			int status, String note) {
		this.id = id;
		this.time = time;
		this.userId = userId;
		this.tablesId = tablesId;
		this.number = number;
		this.status = status;
		this.note = note;
	}

	/**
	 * 建立訂單物件
	 * 
     * @param id 編號
     * @param time 日期時間
     * @param userId 使用者編號
     * @param tablesId 餐桌編號
     * @param number 人數
     * @param status 狀態
     * @param note 備註
	 * @param userName 使用者名稱
	 */
	public Orders(int id, String time, int userId, int tablesId, int number,
			int status, String note, String userName) {
		this(id, time, userId, tablesId, number, status, note);
		this.userName = userName;
	}
	
	/**
	 * 建立訂單物件
	 * 
     * @param id 編號
     * @param time 日期時間
     * @param userId 使用者編號
     * @param tablesId 餐桌編號
     * @param number 人數
     * @param status 狀態
     * @param note 備註
     * @param userName 使用者名稱
	 * @param amount 訂單金額
	 */
	public Orders(int id, String time, int userId, int tablesId, int number,
			int status, String note, String userName, int amount) {
		this(id, time, userId, tablesId, number, status, note, userName);
		this.amount = amount;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time == null ? "" : time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTablesId() {
		return tablesId;
	}

	public void setTablesId(int tablesId) {
		this.tablesId = tablesId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getStatusName() {
		String result = "";
		
		switch (status) {
		case 0:
			result = "新單";
			break;
		case 1:
			result = "準備中";
			break;
		case 2:
			result = "完成";
			break;
		case 3:
			result = "結帳";
			break;
		}
		
		return result;
	}

	/**
	 * 設定訂單狀態
	 * 
	 * @param status 訂單狀態，0：新單，1：準備中，2：完成，3：結帳 
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

	public String getUserName() {
		return userName == null ? "" : userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

    @Override
	public boolean equals(Object obj) {
		boolean result = false;
		
		if (obj != null && obj instanceof Orders) {
			Orders o = (Orders)obj;
			
			if (id == o.id) {
				result = true;
			}
		}
		
		return result;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public int compareTo(Orders orders) {
		return id - orders.id;
	}

	@Override
	public String toString() {
	    return String.format("%d - %s - %d - %d - %d - %s - %s - %s - %d", 
	            id, time, userId, tablesId, number, getStatusName(), note,
	            userName, amount);
	}
}
