/* package codechef; // don't place package name! */

import java.util.*;
import java.lang.*;
import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */
class Codechef
{
    
public static void main(String args[]){
    
StringTokenizer st = new StringTokenizer("my,name,,is,khan",",");
while (st.hasMoreTokens()) {
System.out.println("-"+st.nextToken());
}
String sample="01001111001101";
System.out.println(sample.length());
int possition=getCountOfMaxSuccesiveOnes(sample);
System.out.println("-----Consecutive ones Total Number:"+possition);
}
private static int getCountOfMaxSuccesiveOnes(String str){
    int count=0;
    int max=0;
    for(int i=0;i<str.length();i++){
        System.out.println("value of i:"+i);
        count=0;
        while(i<str.length()&&str.charAt(i)=='1'){
            count++;
            i++;
            System.out.println("Value of inner loop i:"+i);
        }
        
        if(count>max){
                max=count;
            }
    }
    return max;
}
}