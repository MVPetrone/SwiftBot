package subprocesses;

import java.util.ArrayList;

public class Converters {
    public Converters() {	
	}

    public static String decToHex(int dec) {
        // divide by 16 until result is 0
        // store remainder on every occurence
        int n = dec;
        int r;
        ArrayList<String> result = new ArrayList<String>();
        while (n!=0) {
            r = n%16;
            char text;
            if (r>=10) {
                text = (char)((r-10)+'A');
            } else {
                text = (char)(r+'0');
            }
            result.add(0, Character.toString(text));
            n = n/16;
        }
        String hex = String.join("", result);
        return hex;
    }

    public static String decToBin(int dec) {
        // divide by 2 until result is 0
        int n = dec;
        int r;
        ArrayList<String> result = new ArrayList<String>();
        while (n!=0) {
            r = n%2;
            result.add(0, Integer.toString(r));
            n = n/2;
        }
        String bin = String.join("", result);
        return bin;
    }

    public static String decToOct(int dec) {
        // divide by 8 until result is 0
        int n = dec;
        int r;
        ArrayList<String> result = new ArrayList<String>();
        while (n!=0) {
            r = n%8;
            result.add(0, Integer.toString(r));
            n = n/8;
        }
        String oct = String.join("", result);
        return oct;
    }

    public static int Duration(int decimal) {
        return decToHex(decimal).length();
    }

    public static String Movements(int decimal) {
        return decToBin(decimal);
    }

    public static int Speed(int decimal) {
        int octal = Integer.valueOf(decToOct(decimal));
        int speed = octal<50 ? octal+50 : octal;
        speed = octal>100 ? 100 : speed;
        return speed;
    }
}
