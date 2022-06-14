package com.qub.webcalcsqaure;

public class Square {

    public static int square(Integer x) {
        try {
            return x * x;
        }
        catch (IllegalArgumentException e) {

            throw new IllegalArgumentException("Please enter an Integer");
        }
    }
}
