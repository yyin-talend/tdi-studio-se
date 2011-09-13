package org.talend.jpalo;

import static java.util.Arrays.asList;

import java.nio.charset.Charset;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.csvreader.CsvReader;

public class palodata {

	
	public final static int PALO_SPLASH_DISABLE=0;
	public final static int PALO_SPLASH_DEFAULT=1;
	public final static int PALO_SPLASH_BASE_SET=3;
	public final static int PALO_SPLASH_BASE_ADD=2;

		
	private paloconnection plConn;
	private palodimensions plDims;
	private long lDatabaseId;
	private ArrayList<paloelements> alPaloElements = new ArrayList<paloelements>();
	
	private Hashtable <String[], palodatavalue> htPaloResultData= new Hashtable<String[], palodatavalue>();
	private palocube plCube;
	private String[][] strDimensionElementsArray;
	private List<Collection<String>> lstDimensionElements = new LinkedList<Collection<String>>();
	private Iterator<List<String>> itDimensionElements;
	private int iBatchSize, iDimensionElementLength;
	
	public palodata(paloconnection plConn, palodatabase plDB, palocube paloCube){
		super();
		this.plConn = plConn;
		this.lDatabaseId = plDB.getDatabaseId();
		this.plCube =paloCube;
		
		
	}
	
	public palodata(paloconnection plConn, palodatabase plDB, palocube paloCube, ArrayList<paloelements> alPaloElements, String[][] strDimensionElementsArray, int iBatchSize){
		super();
		this.plConn = plConn;
		this.lDatabaseId = plDB.getDatabaseId();
		this.plCube =paloCube;
		this.alPaloElements = alPaloElements;
		this.strDimensionElementsArray = strDimensionElementsArray;
		this.iBatchSize=iBatchSize;
		htPaloResultData= new Hashtable<String[], palodatavalue>();
		buildDimElemIteratorFromArray();
	}
	
	private void buildDimElemIteratorFromArray(){
		this.iDimensionElementLength = strDimensionElementsArray.length;
		for(int i=0;i<strDimensionElementsArray.length;i++){
			lstDimensionElements.add(asList(strDimensionElementsArray[i]));
		}
		itDimensionElements = palohelpers.finiteCartesianProduct(lstDimensionElements).iterator();
	}

	public boolean getResults()  throws paloexception{
		int iRowCounter = 0;
		List<String> lstDimensionElementArray = new LinkedList<String>();
		List<String> x;
		htPaloResultData= new Hashtable<String[], palodatavalue>();
		while(itDimensionElements.hasNext() && iRowCounter < iBatchSize){
			
			x = (List<String>)itDimensionElements.next();
			for(int i=0;i<x.size();i++)lstDimensionElementArray.add(x.get(i));
			iRowCounter++;
		}
		
		int iPos=0;
		int iCoordPos=0;
		StringBuilder sbCoordinates = new StringBuilder();
		
		for(String strCoordElement : lstDimensionElementArray){
			if(iPos>0) sbCoordinates.append(",");
			paloelement plElm = alPaloElements.get(iCoordPos).getElement(strCoordElement);
			if(null==plElm)sbCoordinates.append("-1");
			else sbCoordinates.append(plElm.getElementIdentifier());
			iPos++;
			iCoordPos++;
			if(iCoordPos>=iDimensionElementLength){
				iCoordPos=0;
				sbCoordinates.append(":");
				iPos=0;
			}
		}
		
		if(iRowCounter>0){
		
			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			qparams.add(new BasicNameValuePair("sid", plConn.getPaloToken()));
			qparams.add(new BasicNameValuePair("database", String.valueOf(this.lDatabaseId)));
			qparams.add(new BasicNameValuePair("cube", String.valueOf(plCube.getCubeId())));
			qparams.add(new BasicNameValuePair("paths", sbCoordinates.toString()));
		
			try{
				HttpEntity entity = this.plConn.sendToServer(qparams, "/cell/values");
				CsvReader csv = new CsvReader(entity.getContent(), Charset.forName("UTF-8"));
				csv.setDelimiter(';');
				csv.setTextQualifier('"');
				int iCoordElem=0;
				while(csv.readRecord()){
					String strArrCoord[] = new String[iDimensionElementLength];
					for(int i=0;i<iDimensionElementLength;i++){
						strArrCoord[i]=lstDimensionElementArray.get(iCoordElem++);
					}
					
					if(palohelpers.StringToInt(csv.get(0))==palodatavalue.PALO_NUMERIC){
						if(palohelpers.StringToInt(csv.get(1))>0){
							htPaloResultData.put(strArrCoord, new palodatavalue(strArrCoord, palohelpers.StringToDouble(csv.get(2))));
						}else{
							htPaloResultData.put(strArrCoord, new palodatavalue(strArrCoord, 0d));
						}
					}else if(palohelpers.StringToInt(csv.get(0))==palodatavalue.PALO_STRING){
						htPaloResultData.put(strArrCoord, new palodatavalue(strArrCoord, csv.get(2)));
					}
				}
				csv.close();
				entity.consumeContent();
			}catch(Exception e){
				throw new paloexception(e.getMessage());
			}
		}

		if(iRowCounter<=iBatchSize && iRowCounter > 0) return true;
		else return false;

	}
	
	public Hashtable <String[], palodatavalue> getResultHashTable(){
		return htPaloResultData;
	}
	
	public palodatavalue getValue(String[] strDimensionElementArray, boolean bRefreshElements) throws paloexception{
		long[] lDimensionElementIdentifierArray = new long[strDimensionElementArray.length];
		if(null==plDims || bRefreshElements) plDims = plCube.getDimensions();
		for(int i=0;i<strDimensionElementArray.length;i++){
			paloelement plElm = plDims.getDimension(i).getElements(false).getElement(strDimensionElementArray[i]);
			if(null==plElm)lDimensionElementIdentifierArray[i] = -1;
			else lDimensionElementIdentifierArray[i] = plElm.getElementIdentifier();
		}
		return getValue(lDimensionElementIdentifierArray);
	}

	
	public palodatavalue getValue(long[] lDimensionElementIdentifierArray) throws paloexception{
		
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("sid", plConn.getPaloToken()));
		qparams.add(new BasicNameValuePair("database", String.valueOf(this.lDatabaseId)));
		qparams.add(new BasicNameValuePair("cube", String.valueOf(plCube.getCubeId())));
		
		StringBuilder sbCoordinates = new StringBuilder();
		int iPos=0;
		for(long lDimensionElementIdentifier : lDimensionElementIdentifierArray){
			if(iPos>0) sbCoordinates.append(",");
			sbCoordinates.append(lDimensionElementIdentifier);
			iPos++;
		}
		
		qparams.add(new BasicNameValuePair("path", sbCoordinates.toString()));
		palodatavalue rcDataValue=null;
		
		try{
			HttpEntity entity = this.plConn.sendToServer(qparams, "/cell/value");
			CsvReader csv = new CsvReader(entity.getContent(), Charset.forName("UTF-8"));
			csv.setDelimiter(';');
			csv.setTextQualifier('"');
			while(csv.readRecord()){
			
				if(palohelpers.StringToInt(csv.get(0))==palodatavalue.PALO_NUMERIC){
					if(palohelpers.StringToInt(csv.get(1))>0){
						rcDataValue = new palodatavalue(palohelpers.StringToDouble(csv.get(2)));
					}else{
						rcDataValue = new palodatavalue(0d);
					}
				}else if(palohelpers.StringToInt(csv.get(0))==palodatavalue.PALO_STRING){
					rcDataValue = new palodatavalue(csv.get(2));
				}
			}				
			csv.close();
			entity.consumeContent();
			return rcDataValue;
		}catch(Exception e){
			throw new paloexception(e.getMessage());
		}
	}
	
}
