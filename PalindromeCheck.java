package com.study.corejava;

import java.util.Scanner;

public class PalindromeCheck {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan=new Scanner(System.in);
		
		System.out.println("Enter the number you want to check as pallindrome or not: ");
		int number=scan.nextInt();
		
		System.out.println("Value of number to check pallindrome or not is :"+number);
		
		String convertedNumber=String.valueOf(number);
		System.out.println("Converted number string is :" +convertedNumber);
		String nearestPallindrome=getNearestPalindromeNumber(convertedNumber);
		
		System.out.println("Value of nearest pallindrome is :" +nearestPallindrome);
		scan.close();
	}
		public static String getNearestPalindromeNumber(String numberString){
			
			char[] stringarray=numberString.toCharArray();
			int j=stringarray.length-1;
			StringBuffer nearestPallindromeValue=new StringBuffer();
			System.out.println("String array length is :"+stringarray.length);
			for(int i=0;i<=stringarray.length/2;i++){
				
				if(i>=stringarray.length){
					continue;
				}
				System.out.println("value of array :i,j" +stringarray[i]+stringarray[j]);
				
				
				if(!(stringarray[i]==stringarray[j])){
					System.out.println("Both values are not equal s : 0th and last position elements are"+stringarray[i]+stringarray[j]);
					if(stringarray[i]<stringarray[j]){
						stringarray[j]=stringarray[i];
					}
					else if(stringarray[i]>stringarray[j])
					{
						stringarray[j]=stringarray[i];
					}
				}
				
				j--;
			}
			for(int i=0;i<=stringarray.length;i++){
				if(i>=stringarray.length){
					continue;
				}
				System.out.println("nearest pallindrome number is : "+stringarray[i]);
				nearestPallindromeValue.append(stringarray[i]);
			}	
			
			return nearestPallindromeValue.toString();
		}
		

	

}
