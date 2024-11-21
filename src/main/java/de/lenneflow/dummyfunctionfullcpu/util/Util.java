package de.lenneflow.dummyfunctionfullcpu.util;

import java.util.ArrayList;
import java.util.List;

public class Util {

    static boolean forceStop = false;

    public static List<Integer> calculatePrimeNumbersBetween(int end){
        int i =0;
        int num =0;
        List<Integer>  primeNumbers = new ArrayList<>();


        for (i = 1; i <= end; i++)
        {
            if(forceStop){
                break;
            }
            int counter=0;
            for(num =i; num>=1; num--)
            {
                if(i%num==0)
                {
                    counter = counter + 1;
                }
            }
            if (counter ==2)
            {
                //Appended the Prime number to the String
                primeNumbers.add(i);
            }
        }
        return primeNumbers;
    }

    public static void stopCalculation(){
        forceStop = true;
    }
}
