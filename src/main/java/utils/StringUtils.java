/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author soflavre
 */
public class StringUtils {
    public static String fillTo(String original, int targetLength) {
        if (original.length() > targetLength) {
            return original.substring(0, targetLength);
        }

        return String.format("%-" + targetLength + "s", original);
    }
}

