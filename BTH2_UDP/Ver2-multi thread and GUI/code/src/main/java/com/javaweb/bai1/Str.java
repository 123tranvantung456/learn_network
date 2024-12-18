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

    public static Map<String, Integer> countDuplicateWords(String str) {
        if (str == null || str.isEmpty()) return new HashMap<>();

        Map<String, Integer> wordCountMap = new HashMap<>();
        String[] words = str.split("\\s+");

        for (String word : words) {
            word = word.toLowerCase(); // Chuyển sang chữ thường để đếm không phân biệt chữ hoa chữ thường
            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
        }

        return wordCountMap;
    }

    public static String reverseWords(String str) {
        if (str == null || str.isEmpty()) return str;

        String[] words = str.split("\\s+");
        StringBuilder reversedString = new StringBuilder();

        // Duyệt từ từ cuối đến đầu
        for (int i = words.length - 1; i >= 0; i--) {
            reversedString.append(words[i]).append(" ");
        }

        // Loại bỏ dấu cách thừa cuối cùng
        return reversedString.toString().trim();
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