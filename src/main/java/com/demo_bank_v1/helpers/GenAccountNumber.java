package com.demo_bank_v1.helpers;

import java.util.Random;
// предоставление метода для генерации случайного номера учетной записи.
public class GenAccountNumber {

    public static int generateAccountNumber(){
        int accountNumber;
        Random random = new Random();
        int bound = 1000;
        accountNumber = bound * random.nextInt(bound);
        return accountNumber;
    }
}
