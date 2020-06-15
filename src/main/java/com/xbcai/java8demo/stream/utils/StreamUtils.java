package com.xbcai.java8demo.stream.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StreamUtils {
    /**
     * 将字符串转换为字符流
     * @param str 字符串
     */
    public static Stream<Character> getCharacterByString(String str){
        List<Character> characterList = new ArrayList<>();
        for (Character character:str.toCharArray()){
            characterList.add(character);
        }
        return characterList.stream();
    }
}
