package com.dyc.publishFile.model;




/**
 * 产品服务器表实体
 *
 * @author wqm
 * @time 2018-8-16 14:41:35
 */
//region your codes 1
public class ProductServer  {
//endregion your codes 1

	//region your codes 3
	private static final long serialVersionUID = 233593271145603778L;
	//endregion your codes 3

	//region property name constants
	public static final String PROP_ID = "id";
	public static final String PROP_SERVER_NAME = "serverName";
	public static final String PROP_SERVER_IP = "serverIp";
	public static final String PROP_VERSION = "version";
	public static final String PROP_MACHINE_ROOM = "machineRoom";
	public static final String PROP_STATUS = "status";
	public static final String PROP_PRODUCT_ID = "productId";
	public static final String PROP_CREATE_TIME = "createTime";
	public static final String PROP_UPDATE_TIME = "updateTime";
	public static final String PROP_PRODUCT_NAME = "productName";
	public static final String PROP_PUBLISH_USERNAME = "publishUsername";
	//endregion
	
	
	//region properties
	/** 主键 */
	private Integer id;
	/** 服务器名称 */
	private String serverName;
	/** 服务器ip */
	private String serverIp;
	/** 版本号 */
	private String version;
	/** 机房 */
	private String machineRoom;
	/** 状态 1：未安装 2：安装中 3已安装 4安装失败 */
	private String status;
	/** 产品id */
	private Integer productId;
	/** 创建时间 */
	private java.util.Date createTime;
	/** 更新时间 */
	private java.util.Date updateTime;
	//endregion

	/** 产品名称*/
	private String productName;
	private String publishUsername;
	private String filePath;//

	//region constuctors
	public ProductServer(){
	}

	public ProductServer(Integer id){
		this.id = id;
	}
	//endregion


	//region getters and setters
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer value) {
		this.id = value;
	}
	public String getServerName() {
		return this.serverName;
	}

	public void setServerName(String value) {
		this.serverName = value;
	}
	public String getServerIp() {
		return this.serverIp;
	}

	public void setServerIp(String value) {
		this.serverIp = value;
	}
	
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String value) {
		this.version = value;
	}
	public String getMachineRoom() {
		return this.machineRoom;
	}

	public void setMachineRoom(String value) {
		this.machineRoom = value;
	}
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String value) {
		this.status = value;
	}
	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer value) {
		this.productId = value;
	}
	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(java.util.Date value) {
		this.updateTime = value;
	}
	//endregion

	//region your codes 2
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPublishUsername() {
		return publishUsername;
	}

	public void setPublishUsername(String publishUsername) {
		this.publishUsername = publishUsername;
	}
	//endregion your codes 2


	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}