package com.tp.adata.model;

public class ExcelTest {
	
	private String methodname;
	private String attribute;
	private String type;
	private String locator;
	
	
	public ExcelTest(String methodname, String attribute, String type,
			String locator) {
		super();
		this.methodname = methodname;
		this.attribute = attribute;
		this.type = type;
		this.locator = locator;
	}
	public String getMethodname() {
		return methodname;
	}
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLocator() {
		return locator;
	}
	public void setLocator(String locator) {
		this.locator = locator;
	}
	@Override
	public String toString() {
		return "ExcelTest [methodname=" + methodname + ", attribute="
				+ attribute + ", type=" + type + ", locator=" + locator + "]";
	}
	
	

}
