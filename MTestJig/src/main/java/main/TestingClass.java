package main;

import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.List;

public class TestingClass {

  public static int maxDifference(List<Integer> a) {
    int result = -1;

    for(int i = a.size()-1; i>0 ;i--) {
      List<Integer> list1 = a.subList(0, i-1);
      List<Integer> list2 = a.subList(i, a.size()-1);
      for(int j = 0;j< list1.size()-1;j++) {
        for(int k = 0;k<list2.size()-1;k++) {
          System.out.println(list1.get(j)+" **** "+list2.get(k));
          if(list2.get(k) > list1.get(j)){
            int tmp = list2.get(k) - list1.get(j);
            if(tmp>result) {
              result = tmp;
            }
          }
        }
      }
    }
    return result;
  }
  
   public static void main(String[] args) {


   }


  

}
