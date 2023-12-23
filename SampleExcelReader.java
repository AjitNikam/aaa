package com.tp.adata.core;



import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.transunion.adata.model.ExcelTest;

public class SampleExcelReader {

	public static void main(String args[]) throws IOException{

		String fileInputpath="D:\\Users\\sample\\Desktop\\sampletest.xlsx";
		
		FileInputStream fis=null;		
		fis = new FileInputStream(fileInputpath);
		Workbook workbook = new XSSFWorkbook(fis);
		
		List<String> methodNameList=new ArrayList<String>();

		Sheet sheet1 = workbook.getSheetAt(0);
		int lastRowNumberFirstSheet=sheet1.getLastRowNum();
		System.out.println("Last row number size : " +lastRowNumberFirstSheet);

		String methodNamesFromFirstSheet="";
		for(int i=0;i<=lastRowNumberFirstSheet;i++){
			Row row=sheet1.getRow(i);
			methodNamesFromFirstSheet=row.getCell(1).toString();
			methodNameList.add(methodNamesFromFirstSheet);			
		}

		Sheet sheet2=workbook.getSheetAt(1);
		int lastRowNumberSecondSheet=sheet2.getLastRowNum();
		System.out.println("Last row number size : " +lastRowNumberSecondSheet);
		
		String attribute="";
		String type="";
		String locator="";
		String methodName="";

		List<ExcelTest> excelTestList=new ArrayList<ExcelTest>();

		for(int i=0;i<methodNameList.size();i++){

			methodName=methodNameList.get(i);
			System.out.println("Method name to match with second sheet : " +methodName);

			String previousMethodName="";
			for(int j=0;j<=lastRowNumberSecondSheet;j++){

				Row row=sheet2.getRow(j);
				
				//To Check the row is empty in excel sheet then continue to next row
				if(row==null){
					
					continue;
					
				}
				
				//if first cell from row not null then store in previous method name field
				if(row.getCell(0)!=null){
					
					previousMethodName=row.getCell(0).toString();
					
				}
				
				//if first cell from row not null and matches method name from first sheet				 
				if(row.getCell(0)!=null && methodName.equalsIgnoreCase(row.getCell(0).toString())){
					
					previousMethodName=row.getCell(0).toString();
					System.out.println("Method name : "+methodName+", previous method name : "+previousMethodName);					
					methodName=row.getCell(0).toString();
					attribute=row.getCell(1).toString();
					type=row.getCell(2).toString();
					locator=row.getCell(3).toString();
					ExcelTest excelTest=new ExcelTest(previousMethodName,attribute,type,locator);
					excelTestList.add(excelTest);
					
				}
				
				//if first cell from row is null and matches previous method name				 
				if(row.getCell(0)==null && previousMethodName.equalsIgnoreCase(methodName)){
					
					System.out.println("Method name : "+methodName+", previous method name------- : "+previousMethodName);
					attribute=row.getCell(1).toString();
					type=row.getCell(2).toString();
					locator=row.getCell(3).toString();
					ExcelTest excelTest=new ExcelTest(previousMethodName, attribute, type, locator);
					excelTestList.add(excelTest);
					
				}

			}
		}
		
		System.out.println("Excel Test List size : " +excelTestList.size());
		
		for(int i=0;i<excelTestList.size();i++){
			
			System.out.println("Excel Test List data : "+excelTestList.get(i).toString());
			
		}
	}
}
