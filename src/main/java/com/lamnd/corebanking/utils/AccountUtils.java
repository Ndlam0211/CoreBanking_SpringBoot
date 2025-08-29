package com.lamnd.corebanking.utils;

import java.time.Year;

public class AccountUtils {
    /**
     *  Generate a random 10-digit account number: currentYear + random 6 digits
     */
    public static String generateAccountNumber() {
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        // Generate a random 6-digit number between min and max
        int randomSixDigit = (int) Math.floor(Math.random() * (max - min + 1) + min);

        // convert currentYear and randomSixDigit to String and concatenate them
        return String.valueOf(currentYear) + String.valueOf(randomSixDigit);
    }
}
