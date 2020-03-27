package com.hui.redisbuss.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RedPacketUtil {
    
    public static List<Integer> divideRedPackage(Integer totalAmout, Integer totalPeopleNum) {
    
        List<Integer> amountList = new ArrayList<>();
        
        if (totalAmout > 0 && totalPeopleNum >0 ) {
            Integer restAmount = totalAmout;
            Integer restPeopleNum = totalPeopleNum;
            Random random = new Random();
            for (int i = 0 ; i < totalPeopleNum -1 ;i ++) {
                int amount = random.nextInt(restAmount/restPeopleNum*2-1) +1;
                restAmount -= amount;
                restPeopleNum --;
                amountList.add(amount);
            }
            amountList.add(restAmount);
        }
        return amountList;
    }
}
