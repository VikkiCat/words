package com.epam.rd.autotasks.words;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static int countEqualIgnoreCaseAndSpaces(String[] words, String sample) {
        if(sample == null || words == null){
            return 0;
        }
        sample = sample.stripLeading();
        sample = sample.stripTrailing();

        if (sample.length() == 0 || words.length == 0){
            return 0;
        }
        int count = 0;
        for (String word:
             words) {
            word = word.stripLeading();
            word = word.stripTrailing();
            if (word.equalsIgnoreCase(sample)){
                count++;
            }
        }

        return count;
    }

    public static String[] splitWords(String text) {

        if (text == null) return null;
        if (text.isEmpty() || text.matches("^[ ,.;:!?]+$")){
            return null;
        }
        text = text.replaceAll("\\p{Punct}+", " ").trim();
        Pattern p1 = Pattern.compile("[, .;:!?]+");
        String [] sArr = p1.split(text);
        if (sArr[0].isEmpty()){
            return null;
        }
        return sArr;
    }

    public static String convertPath(String path, boolean toWin) {
        if (path == null || path.isEmpty()) return null;
        String pattern = "((.+/.+\\\\.+)|" +
                "(.+\\\\.+/.+)|(.+~.+)|(C:/.+)|" +
                "(~\\\\.+)|(~/~)|(~{2,})|(.+C:\\\\)|" +
                "(C:\\\\.+C:\\\\))";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(path);
        if (m.matches()) {return null;}
        if ((checkWinPath(path) && toWin) || (checkUnixPath(path) && !toWin)) return path;
        String resultPath;

        if(toWin){
            resultPath = convertToWin(path);
        } else {
            resultPath = convertToUnix(path);
        }
        return resultPath;
    }
    private static boolean checkWinPath(String path){
        return path.matches("(C:\\\\)?([\\w\\.\\-\\s\\\\]*)");
    }
    private static boolean checkUnixPath(String path){
        return path.matches("(/|~)?([\\w\\.\\-\\s/]*)");
    }
    private static String convertToWin(String path){
        String result = path;
        if (path.startsWith("~")){
            path = path.replace("~", "C:\\User");
        } else if (path.startsWith("/")) {
            path = path.replaceFirst("/", "C:\\\\");
        }
        return path.replace("/", "\\");
    }
    private static String convertToUnix(String path){
        String result = path;
        if (path.startsWith("C:\\User")){
            path = path.replace("C:\\User", "~");
        } else if (path.startsWith("C:\\")) {
            path = path.replace("C:\\", "/");
        }
        return path.replace("\\", "/");
    }

    public static String joinWords(String[] words) {
        if (words == null || words.length == 0) {
            return null;
        }
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (String i:
             words) {
            if (!i.isEmpty()){
                joiner.add(i);
            }
        }
        if (joiner.length()==2){
            return null;
        }
        return joiner.toString();
    }

    public static void main(String[] args) {
        System.out.println("Test 1: countEqualIgnoreCaseAndSpaces");
        String[] words = new String[]{" WordS    \t", "words", "w0rds", "WOR  DS", };
        String sample = "words   ";
        int countResult = countEqualIgnoreCaseAndSpaces(words, sample);
        System.out.println("Result: " + countResult);
        int expectedCount = 2;
        System.out.println("Must be: " + expectedCount);

        System.out.println("Test 2: splitWords");
        String text = "   ,, first, second!!!! third";
        String[] splitResult = splitWords(text);
        System.out.println("Result : " + Arrays.toString(splitResult));
        String[] expectedSplit = new String[]{"first", "second", "third"};
        System.out.println("Must be: " + Arrays.toString(expectedSplit));

        System.out.println("Test 3: convertPath");
        String unixPath = "/some/unix/path";
        String convertResult = convertPath(unixPath, true);
        System.out.println("Result: " + convertResult);
        String expectedWinPath = "C:\\some\\unix\\path";
        System.out.println("Must be: " + expectedWinPath);

        System.out.println("Test 4: joinWords");
        String[] toJoin = new String[]{"go", "with", "the", "", "FLOW"};
        String joinResult = joinWords(toJoin);
        System.out.println("Result: " + joinResult);
        String expectedJoin = "[go, with, the, FLOW]";
        System.out.println("Must be: " + expectedJoin);
    }
}