/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world.util;

/**
 *
 * @author Administrator
 */
public class NLTimer {
    public static long time = 0;

    public static void push(){
        time = System.nanoTime();
    }

    public static void pop(){
        float time_diff = (float)( System.nanoTime() - time ) / (1000*1000);
        System.out.println( "NLTimer : " + Float.toString(time_diff) + " ms elasped");
    }

    public static void pop(String message){
        float time_diff = (float)( System.nanoTime() - time ) / (1000*1000);
        System.out.println( "NLTimer : " + Float.toString(time_diff) + " ms elasped. (" + message + ")" );
    }
}
