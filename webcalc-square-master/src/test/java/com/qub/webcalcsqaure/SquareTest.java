package com.qub.webcalcsqaure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class SquareTest {

    private Square squareNumber;

    @BeforeEach
    public void setUp() throws Exception {
        squareNumber = new Square();
    }

    @Test
    @DisplayName("Test square function response = expected response ")
    void squareNormal() {
        assertEquals(64,  squareNumber.square(8));
        assertEquals(0,  squareNumber.square(0));
        assertEquals(1,  squareNumber.square(1));
    }
    @Test
    @DisplayName("Test square function with negative numbers = expected response ")
    void squareNegativeNumbers() {
        assertEquals(16,  squareNumber.square(-4));
        assertEquals(0,  squareNumber.square(-0));
        assertEquals(225,  squareNumber.square(-15));
    }
    @Test
    @DisplayName("Test square function != incorrect result response")
    void squareNotEqual() {
        assertNotEquals("bad",  squareNumber.square(-4));
        assertNotEquals(1,  squareNumber.square(-0));
        assertNotEquals("-255",  squareNumber.square(-15));
    }

}