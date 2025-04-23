/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.text.ParseException;

import static utils.DateUtils.DATE_FORMAT;

/**
 *
 * @author soflavre
 */
public class ValidationUtils {

    public static boolean validRequiredString(String input){
        if (input == null){
        return false;
        }
            String trimmedString = input.trim();
        return !trimmedString.isEmpty();
        
    }
     public static boolean validInteger(Integer input, int minvalue){
        if (input == null){
            return false;
        }
        
        return input >= minvalue;
          
     }
     

     public static boolean validInteger(Integer input, int minvalue, int maxvalue){
        if (input == null){
            return false;
        }
        
        return input <= maxvalue 
                && input >= minvalue;
          
     }
     
     public static boolean validDate(String input){
         if (input == null){
            return false;
        }
         
         try {
             DATE_FORMAT.parse(input);
             return true;
         } catch(ParseException e) {
             return false;
         }
         
         
     }
}
