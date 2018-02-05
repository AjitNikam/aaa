package com.transunion.adata.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.transunion.adata.dao.inter.McaDao;
import com.transunion.adata.model.RationCard;

@Component(value="test")
public class Test {

	List<String> anchorList=new ArrayList<String>();

	@Autowired
	McaDao mcaDaoImpl;	

	public void processAndClickLink(HtmlPage htmlPage5,List<HtmlAnchor> anchors2,int m,String districtName,String townName,String fpsName){
		try{
			htmlPage5=anchors2.get(m).click();

			HtmlTable table4 = htmlPage5.getHtmlElementById("ctl00_ContentPlaceHolder1_gvDetails");

			Document doc5=Jsoup.parse(table4.asXml());

			//List<HtmlAnchor> anchors4 = htmlPage5.getAnchors();
			//listData(anchors4);

			String number="1";
			int derivedNumber=Integer.parseInt(number);
			boolean result=true;
			String specialNumber="...";

			//Processing first page data as opened after clicking url
			while(number.equalsIgnoreCase("1")){
				if(number.equalsIgnoreCase("1")){	
					System.out.println("From page 1..............................");
					//processed opened document
					processWebPage(doc5,districtName,townName,fpsName);
					derivedNumber=derivedNumber+1;
					number=String.valueOf(derivedNumber);
				}
			}

			while(result==true){
				//processing remaining pages by clicking age number
				if(number.length()>=2 && number.charAt(number.length()-1)=='1'){														
					number=specialNumber;
				}												

				result=processAnchorLinks(number,derivedNumber,htmlPage5,districtName,townName,fpsName);
				number=String.valueOf(derivedNumber-1);
				derivedNumber=derivedNumber+1;
				number=String.valueOf(derivedNumber);
				System.out.println("new Number is : " +number);

			}
		}
		catch(Exception e){
			System.out.println("Error messAGE : "+e.getMessage());
			//e.printStackTrace();
		}
	}

	public boolean processAnchorLinks(String number,int derivedNumber,HtmlPage htmlPage4,String districtName,String townName,String fpsName) throws IOException{
		try{
			HtmlPage htmlPage5=null;
			HtmlAnchor anchor1 = htmlPage4.getAnchorByText(number);
			String pageNumber=findPageNUmber(anchor1.toString());

			System.out.println("Page number from anchor is : "+pageNumber+",  Current number to priocess is : "+derivedNumber);
			int currentpageNumber=Integer.valueOf(pageNumber);

			//get list of anchors from HTML page...then while clicking anchor first check derived number by using for loop is in anchor or not
			if(derivedNumber>currentpageNumber){
				List<HtmlAnchor> list=htmlPage4.getAnchors();
				for(int i=0;i<list.size();i++){

					if(list.get(i).toString().contains(String.valueOf(derivedNumber))){
						System.out.println("value of anchor is : "+list.get(i).toString()+", number to match is : "+String.valueOf(derivedNumber));
						htmlPage5=list.get(i).click();

						HtmlTable table4 = htmlPage5.getHtmlElementById("ctl00_ContentPlaceHolder1_gvDetails");

						Document doc5=Jsoup.parse(table4.asXml());
						//processed opened document
						processWebPage(doc5,districtName,townName,fpsName);	

						return true;
					}
				}
			}
			System.out.println("Anchor 1 from page 2................... : "+anchor1);

			htmlPage5=anchor1.click();

			HtmlTable table4 = htmlPage5.getHtmlElementById("ctl00_ContentPlaceHolder1_gvDetails");

			Document doc5=Jsoup.parse(table4.asXml());
			//processed opened document
			processWebPage(doc5,districtName,townName,fpsName);	

			return true;
		}
		catch(ElementNotFoundException e){
			System.out.println("Error messAGE : "+e.getMessage());
			//e.printStackTrace();
		}
		return false;
	}

	public int processFpsLinks(String derivedFpsNumber,int fpsPageNumber,HtmlPage htmlPage4,String districtName,String townName,String fpsName){
		try{
			HtmlPage htmlPage5=null;
			HtmlAnchor anchor1 = htmlPage4.getAnchorByText(derivedFpsNumber);
			String pageNumber=findPageNUmber(anchor1.toString());

			System.out.println("Page number from anchor is : "+pageNumber+",  Current number to priocess is : "+derivedFpsNumber);

			System.out.println("Anchor 1 from page 2................... : "+anchor1);

			htmlPage5=anchor1.click();
			//get FPS Anchors list from next page
			List<HtmlAnchor> fpsLinkList=htmlPage5.getAnchors();

			HtmlTable table4 = htmlPage5.getHtmlElementById("ctl00_ContentPlaceHolder1_gvDetails");

			Document doc5=Jsoup.parse(table4.asXml());
 
			//processed ration card links from fPS clicking
			for(int i=0;i<fpsLinkList.size();i++){
				if(fpsLinkList.get(i).toString().contains("RCCount_Beneficiary.aspx?FPSCode")){
					System.out.println("NExt FPS page Names ......: "+fpsLinkList.get(i).getTextContent());
					processAndClickLink(htmlPage5, fpsLinkList, i,districtName,townName,fpsName);		
				}
			}
		}
		catch(ElementNotFoundException e){
			System.out.println("Error messAGE : "+e.getMessage());
			//e.printStackTrace();
			return fpsPageNumber;
		}
		catch (IOException e) {
			System.out.println("Error messAGE : "+e.getMessage());
			//e.printStackTrace();
			return fpsPageNumber;
		}
		return fpsPageNumber;
	}

	public void processWebPage(Document doc,String districtName,String townName,String fpsName){

		try{

			Elements divs = doc.select(".mGrid");	
			if (divs != null && divs.size() > 0) {

				Elements members = doc.select(" table > tbody > tr");
				int totalLoop = members.size();	
				System.out.println("Total loop: " +totalLoop);

				RationCard rationCard=null;
				if(totalLoop>10 && totalLoop<52){
					for(int indx=2;indx<totalLoop-1;indx++){
						while((doc.select("table > tbody > tr:nth-child(" + indx + ") > td:nth-child(1)"))==null){
							continue;
						}
						if(indx>=52){
							continue;
						}
						if ((doc.select("table > tbody > tr:nth-child(" + indx + ") > td:nth-child(1)"))==null) {
							continue;
						}
						rationCard=new RationCard();

						rationCard.setSrNo(doc
								.select("table > tbody > tr:nth-child(" + indx
										+ ") > td:nth-child(1)").first().text());

						rationCard.setRationNo(doc
								.select("table > tbody > tr:nth-child(" + indx
										+ ") > td:nth-child(2)").first().text());
						rationCard.setCategory(doc
								.select("table > tbody > tr:nth-child(" + indx
										+ ") > td:nth-child(3)").first().text());
						rationCard.setName(doc
								.select("table > tbody > tr:nth-child(" + indx
										+ ") > td:nth-child(4)").first().text());				
						rationCard.setFatherOrHusbandname(doc
								.select("table > tbody > tr:nth-child(" + indx
										+ ") > td:nth-child(5)").first().text());
						rationCard.setHofName(doc
								.select("table > tbody > tr:nth-child(" + indx
										+ ") > td:nth-child(6)").first().text());
						rationCard.setGender(doc
								.select("table > tbody > tr:nth-child(" + indx
										+ ") > td:nth-child(7)").first().text());
						rationCard.setAge(doc
								.select("table > tbody > tr:nth-child(" + indx
										+ ") > td:nth-child(8)").first().text());
						rationCard.setDistrictName(districtName);
						rationCard.setTownName(townName);
						rationCard.setFpsName(fpsName);

						System.out.println("Ration details .............: "+rationCard.toString());
						if(rationCard.getRationNo().isEmpty() || rationCard.getRationNo()==null){
							continue;	
						}
						//mcaDaoImpl.insertWestBengalRationCard(rationCard);						
					}
				}

				else {


					for (int indx = 2; indx < members.size(); indx++) {

						while((doc.select("table > tbody > tr:nth-child(" + indx + ") > td:nth-child(1)"))==null){
							continue;
						}
						if(indx>=52){
							continue;
						}
						if ((doc.select("table > tbody > tr:nth-child(" + indx + ") > td:nth-child(1)"))==null) {
							continue;
						}
						rationCard=new RationCard();

						rationCard.setSrNo(doc
								.select("table > tbody > tr:nth-child(" + indx
										+ ") > td:nth-child(1)").first().text());

						rationCard.setRationNo(doc
								.select("table > tbody > tr:nth-child(" + indx
										+ ") > td:nth-child(2)").first().text());
						rationCard.setCategory(doc
								.select("table > tbody > tr:nth-child(" + indx
										+ ") > td:nth-child(3)").first().text());
						rationCard.setName(doc
								.select("table > tbody > tr:nth-child(" + indx
										+ ") > td:nth-child(4)").first().text());				
						rationCard.setFatherOrHusbandname(doc
								.select("table > tbody > tr:nth-child(" + indx
										+ ") > td:nth-child(5)").first().text());
						rationCard.setHofName(doc
								.select("table > tbody > tr:nth-child(" + indx
										+ ") > td:nth-child(6)").first().text());
						rationCard.setGender(doc
								.select("table > tbody > tr:nth-child(" + indx
										+ ") > td:nth-child(7)").first().text());
						rationCard.setAge(doc
								.select("table > tbody > tr:nth-child(" + indx
										+ ") > td:nth-child(8)").first().text());
						rationCard.setDistrictName(districtName);
						rationCard.setTownName(townName);
						rationCard.setFpsName(fpsName);

						System.out.println("Ration details .............: "+rationCard.toString());
						if(rationCard.getRationNo().isEmpty() || rationCard.getRationNo()==null){
							continue;	
						}
						//mcaDaoImpl.insertWestBengalRationCard(rationCard);
					}
				}
			}			
		}
		catch(Exception e){
			System.out.println("Error messAGE : "+e.getMessage());
			//e.printStackTrace();
		}
	}

	public String findPageNUmber(String anchor){

		String[] words = anchor.split(",");

		String pageNumber=words[1];
		//System.out.println("page Number : "+pageNumber);
		String pageNumber1=pageNumber.replaceAll("[^0-9]", "");	       
		//System.out.println("Numbers are .....................: " + pageNumber1);
		return pageNumber1;
	}

	public Set<HtmlAnchor> printAnchorListData(List<HtmlAnchor> anchors){

		Set<HtmlAnchor> perfectAnchorList=new HashSet<HtmlAnchor>();
		for(int i=0;i<anchors.size();i++){
			if(anchors.get(i).toString().contains("ctl00$ContentPlaceHolder1$gvDetails")){
				//System.out.println("Anchors ...... : " +anchors.get(i).toString());
				perfectAnchorList.add(anchors.get(i));
			}
		}
		return perfectAnchorList;
	}

	public void listData(List<HtmlAnchor> anchors){
		for(int i=0;i<anchors.size();i++){
			if(anchors.get(i).toString().contains("ctl00$ContentPlaceHolder1$gvDetails")){
				System.out.println("Anchors ...... : " +anchors.get(i).toString());
			}
		}
	}

	public List<HtmlAnchor> getAnchorListData(List<HtmlAnchor> anchors){

		List<HtmlAnchor> perfectAnchorList=new ArrayList<HtmlAnchor>();
		for(int i=0;i<anchors.size();i++){
			if(anchors.get(i).toString().contains("ctl00$ContentPlaceHolder1$gvDetails")){
				System.out.println("Anchors ...... : " +anchors.get(i).toString());
				perfectAnchorList.add(anchors.get(i));
			}
		}
		return perfectAnchorList;
	}


	public List<HtmlAnchor> addAndReturnAnchorList(List<HtmlAnchor> list){

		List<HtmlAnchor> perfectAnchorList=new ArrayList<HtmlAnchor>();
		for(int i=0;i<list.size();i++){
			if(list.get(i).toString().contains("ctl00$ContentPlaceHolder1$gvDetails")){
				System.out.println("Anchors ...... : " +list.get(i).toString());
				perfectAnchorList.add(list.get(i));
			}
		}
		return perfectAnchorList;

	}
	
	/*//create table for west bengal ration card
	 *void insertWestBengalRationCard(RationCard rationCard);
	 *public void insertWestBengalRationCard(RationCard rationCard) {
		String sql = "INSERT INTO westbengal.cards " 			
				+ "( district_name,town_name,fps_name,ration_no,category,name,father_or_husband_name,hof_name,"
				+ "gender,age)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		getJdbcTemplate().update(
				sql,
				new Object[] {					
						
						rationCard.getDistrictName(),
						rationCard.getTownName(),
						rationCard.getFpsName(),
						rationCard.getRationNo(),
						rationCard.getCategory(),
						rationCard.getName(),
						rationCard.getFatherOrHusbandname(),
						rationCard.getHofName(),
						rationCard.getGender(),
						rationCard.getAge()
				});	
		System.out.println("Ration Card details inserted successfully  : "+rationCard.getRationNo());
		
	}
	 CREATE TABLE westbengal.cards
(
  district_name character varying,
  town_name character varying,
  fps_name character varying,
  ration_no character varying,
  category character varying,
  name character varying,
  father_or_husband_name character varying,
  hof_name character varying,
  gender character varying,
  age character varying,
  id_serial serial NOT NULL
);


CREATE INDEX westbengal_district_name_idx
  ON westbengal.cards
  USING btree
  (district_name COLLATE pg_catalog."default");
	 * 
	 * */

}
