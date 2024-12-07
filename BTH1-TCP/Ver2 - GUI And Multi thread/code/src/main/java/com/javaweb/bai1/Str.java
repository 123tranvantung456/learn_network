package com.javaweb.bai1;
import java.util.HashMap;
import java.util.Map;

public class Str {
    public static String reverseString(String str) {
        char[] chars = str.toCharArray();
        int left = 0, right = chars.length - 1;
        while (left < right) {
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;
            left++;
            right--;
        }
        return new String(chars);
    }

    public static String toUpperCase(String str) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 'a' && chars[i] <= 'z') {
                chars[i] = (char) (chars[i] - 32);
            }
        }
        return new String(chars);
    }

    public static String toLowerCase(String str) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 'A' && chars[i] <= 'Z') {
                chars[i] = (char) (chars[i] + 32);
            }
        }
        return new String(chars);
    }

    public static String mixCase(String str) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 'a' && chars[i] <= 'z') {
                chars[i] = (char) (chars[i] - 32);
            } else if (chars[i] >= 'A' && chars[i] <= 'Z') {
                chars[i] = (char) (chars[i] + 32);
            }
        }
        return new String(chars);
    }

    public static int countWords(String str) {
        if (str == null || str.isEmpty()) return 0;
        String[] words = str.split("\\s+");
        return words.length;
    }

    public static Map<Character, Integer> countVowels(String str) {
        str = toLowerCase(str);
        Map<Character, Integer> vowelCountMap = new HashMap<Character, Integer>();
        vowelCountMap.put('a', 0);
        vowelCountMap.put('e', 0);
        vowelCountMap.put('i', 0);
        vowelCountMap.put('o', 0);
        vowelCountMap.put('u', 0);

        for (char c : str.toCharArray()) {
            if (vowelCountMap.containsKey(c)) {
                vowelCountMap.put(c, vowelCountMap.get(c) + 1);
            }
        }

        return vowelCountMap;
    }
}
