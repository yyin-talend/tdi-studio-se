package org.talend.jpalo.talendHelpers;

public class tPaloDimensionElements {

	private int iPosition;
	private String strElementName;
	private int iParentPosition;
	private int iLevel;
	
	private double dFactor;
	
	
	public tPaloDimensionElements(int iPosition, String strElementName, int iParentPosition, int iLevel){
		super();
		this.iPosition=iPosition;
		this.strElementName=strElementName;
		this.iParentPosition=iParentPosition;
		this.iLevel=iLevel;
		this.dFactor=1;
	}
	
	
	public tPaloDimensionElements(int iPosition, String strElementName, int iParentPosition, int iLevel, double dFactor){
		super();
		this.iPosition=iPosition;
		this.strElementName=strElementName;
		this.iParentPosition=iParentPosition;
		this.iLevel=iLevel;
		this.dFactor=dFactor;
	}
	
	public int getPosition(){
		return iPosition;
	}

	public String getElementName(){
		return strElementName;	
	}
	public int getParentPosition(){
		return iParentPosition;
	}
	public void setParentPosition(int iParentPosition){
		this.iParentPosition = iParentPosition;
	}
	public int getLevel(){
		return iLevel;
	}
	public void setLevel(int iLevel){
		this.iLevel=iLevel;
	}
	
	public double getFactor(){
		return dFactor;
	}
	public void setFactor(double dFactor){
		this.dFactor = dFactor;
	}
		
	public String toString(){
		StringBuilder sb= new StringBuilder();
		sb.append(iPosition);
		sb.append(" / ");
		sb.append(strElementName);
		sb.append(" / ");
		sb.append(iParentPosition);
		sb.append(" / ");
		sb.append(iLevel);
		sb.append(" / ");
		sb.append(dFactor);
		return sb.toString();
	}
	
}
