package com.transunion.adata.core;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WBRationScraper {

	@Autowired
	WBRationScraperWorker wbRationScraperWorker;

	public void start(){

		Document doc=null;
		String url="https://202.61.117.98/RCCount_District.aspx";

		try{
			doc = Jsoup.connect(url)
					.timeout(600000)
					.validateTLSCertificates(false)
					.get();

			WebClient webClient = null;
			webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER); 
			webClient.getOptions().setUseInsecureSSL(true);

			webClient.getOptions().setCssEnabled(true);
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setRedirectEnabled(true);
			webClient.getOptions().setTimeout(65000);
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			webClient.getOptions().setThrowExceptionOnScriptError(false);

			HtmlPage page = webClient.getPage(url);

			Elements elements=doc.select(".mGrid");
			Elements pageLinks = elements.select("a");
			HtmlPage htmlPage1=null;
			String districtName="";
			for(int i=0;i<pageLinks.size();i++){
				Element element=pageLinks.get(i);
				districtName=element.ownText();
				System.out.println("District Name............... : "+districtName);

				HtmlAnchor anchor = page.getAnchorByText(element.select("a").text()); 

				htmlPage1=anchor.click();

				List<HtmlAnchor> anchors = htmlPage1.getAnchors();

				String townName="";
				HtmlPage htmlPage3=null;
				for(int k=0;k<anchors.size();k++){
					if(anchors.get(k).toString().contains("RCCount_FPS.aspx?BlockCode")){
						townName=anchors.get(k).getTextContent();
						System.out.println("Town Name ....................: "+townName);	
						htmlPage3=anchors.get(k).click();
						List<HtmlAnchor> anchors2 = htmlPage3.getAnchors();	

						HtmlPage htmlPage5=null;
						String fpsName="";
						int fpsPageNumber=1;
						while(fpsPageNumber==1){
							for(int m=0;m<anchors2.size();m++){	

								if(anchors2.get(m).toString().contains("RCCount_Beneficiary.aspx?FPSCode")){

									fpsName=anchors2.get(m).getTextContent();
									System.out.println("Fps Name ................... : "+fpsName);
									wbRationScraperWorker.processAndClickLink(htmlPage5, anchors2, m,districtName,townName,fpsName);

								}
								if(m==anchors2.size()-1){
									fpsPageNumber=fpsPageNumber+1;
								}								
							}//end of for loop
						}//end of while loop
						
						//Checking the FPS page number and logic to click next anchor pages of FPS
						String derivedFpsNumber="";
						System.out.println("FPS : Anchors 2 size : "+anchors2.size());
						List<HtmlAnchor> correctAnchorLinksList=new ArrayList<HtmlAnchor>();
						for(int q=0;q<anchors2.size();q++){
							if(anchors2.get(q).toString().contains("ctl00$ContentPlaceHolder1$gvDetails','Page$")){
								System.out.println("Correct anchor links : "+anchors2.get(q).toString());
								correctAnchorLinksList.add(anchors2.get(q));
							}

						}
						while(fpsPageNumber<=correctAnchorLinksList.size()+1){
							derivedFpsNumber=String.valueOf(fpsPageNumber);									
							int fpsPageNumberFromMethod=wbRationScraperWorker.processFpsLinks(derivedFpsNumber,fpsPageNumber,htmlPage3,districtName,townName,fpsName);
							if(fpsPageNumberFromMethod>anchors2.size()){
								continue;
							}
							fpsPageNumber=fpsPageNumber+1;
							derivedFpsNumber=String.valueOf(derivedFpsNumber);
							System.out.println("new Number is : " +derivedFpsNumber);

						}

					}//end of fPS for loop
				}//end of town for loop
			}//end of district for loop
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
	}

}
