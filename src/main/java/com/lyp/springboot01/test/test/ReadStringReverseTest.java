package com.lyp.springboot01.test.test;

import java.util.Scanner;

/**
 * 将读取的字符串反转
 */
public class ReadStringReverseTest {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String inputString = sc.nextLine();
    StringBuffer sb = new StringBuffer(inputString);
    System.out.println(sb.reverse().toString());
    while (sc.hasNext()) {
      inputString = sc.nextLine();
      String temp = new StringBuffer(inputString).reverse().toString();
      System.out.println(temp);
    }
  }
}
