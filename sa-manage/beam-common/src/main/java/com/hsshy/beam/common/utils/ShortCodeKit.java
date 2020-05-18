package com.hsshy.beam.common.utils;

import java.util.Arrays;
import java.util.Stack;

/**
 * 将自增id转成短链接,
 * 同时短连接可逆回自增id,
 * 相邻自增id无规律，防破解。
 * 基于Feistel的特性转为Base62。
 *
 * @author hs
 * @create 2017-11-02
 **/
public class ShortCodeKit {

    /**
     * Feistel密码结构：如果permutedId(a)=b，那么必然会有permutedId(b)=a
     *
     * @param id
     * @return
     */
    public static Long permutedId(Long id) {
        Long l1 = (id >> 16) & 65535;
        Long r1 = id & 65535;

        for (int i = 0; i < 2; i++) {
            Long l2 = r1;
            Long r2 = l1 ^ (int) (roundFunction(r1) * 65535);
            l1 = l2;
            r1 = r2;
        }
        return ((r1 << 16) + l1);
    }




    public static Double roundFunction(Long val) {
        return ((131239 * val + 15534) % 714025) / 714025.0;
    }

    /**
     * 调整字符顺序可增加混淆
     */
    private static char[] charSet =
            "0WqPQRI7yCDE31VONvSXnxY2bcdJK8zoLMZa9AmBjklpTUu45FGrst6Hwefghi".toCharArray();

    /**
     * 将10进制转化为62进制
     *
     * @param number
     * @param length 转化成的62进制长度，不足length长度的话高位补0，否则不改变什么
     * @return
     */
    public static String convertDecimalToBase62(long number, int length) {
        Long rest = number;
        Stack<Character> stack = new Stack<Character>();
        StringBuilder result = new StringBuilder(0);
        while (rest != 0) {
            stack.add(charSet[new Long((rest - (rest / 62) * 62)).intValue()]);
            rest = rest / 62;
        }
        for (; !stack.isEmpty(); ) {
            result.append(stack.pop());
        }
        int resultLength = result.length();
        StringBuilder temp0 = new StringBuilder();
        for (int i = 0; i < length - resultLength; i++) {
            temp0.append('0');
        }
        return temp0.toString() + result.toString();
    }

    /**
     * 将62进制转换成10进制数
     *
     * @param str
     * @return
     */
    public static String convertBase62ToDecimal(String str) {
        int multiple = 1;
        long result = 0;
        Character c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(str.length() - i - 1);
            result += getCharValue(c) * multiple;
            multiple = multiple * 62;
        }
        return result + "";
    }

    /**
     * 获取字符下标
     *
     * @param c
     * @return
     */
    private static int getCharValue(Character c) {
        for (int i = 0; i < charSet.length; i++) {
            if (c == charSet[i]) {
                return i;
            }
        }
        return -1;
    }


    public static void main(String[] args) {
        //转码测试
        Long id = permutedId(3L);
        Long oid = permutedId(id);
        System.out.println(id);
        System.out.println(oid);
        String s = convertDecimalToBase62(id, 8);
        System.out.println(s);
        String r = convertBase62ToDecimal(s);
        System.out.println(r);
        System.out.println(permutedId(Long.parseLong(r)));


    }
}
