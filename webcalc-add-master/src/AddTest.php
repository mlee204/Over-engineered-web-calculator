<?php
use PHPUnit\Framework\TestCase;

include "functions.inc.php";

final class AddTest extends TestCase {

    public function testAddFunctionValidParameters(): void
    {
        $this->assertEquals(10, add(5,5));
        $this->assertEquals(60, add(-40,100));
        $this->assertEquals(-49, add(-30,-19));
        $this->assertEquals(11113, add(11111,2));
        $this->assertEquals(89, add(72,17));
    }

    public function testAddFunctionValidParametersNotEquals(): void
    {
        $this->assertNotEquals(22, add(5,5));
        $this->assertNotEquals(3, add(5,5));
        $this->assertNotEquals(1, add(5,5));
        $this->assertNotEquals(66, add(5,5));
        $this->assertNotEquals(22, add(5,5));
    }

    public function testAddFunctionMissingParametersThrowError(): void
    {
        // expect ArgumentCountError when all add with no parameters
        $this->expectException(ArgumentCountError::class);
        add();
    }

    public function testAddFunctionParametersSetToNullThrowError(): void
    {
        // expect BadMethodCallException when x or y or both parameter is equal to null
        $this->expectException(BadMethodCallException::class);
        $this->expectExceptionMessage('parameters are null');
        add(null, null);
        add(44, null);
        add(null, 39);

    }

    public function testAddFunctionParametersAreStringsThrowError(): void
    {
        // expect InvalidArgumentException when one or more or parameter are not Integers
        $this->expectException(InvalidArgumentException::class);
        $this->expectExceptionMessage('add function only accepts integers as parameters');
        add("hello", "Bad");
        add("7.5", "7.5");

    }

    public function testAddFunctionParametersAreDoubleThrowError(): void
    {
        // expect InvalidArgumentException when one or more or parameter are not Integers
        $this->expectException(InvalidArgumentException::class);
        $this->expectExceptionMessage('add function only accepts integers as parameters');
        add(7.5, 5.5);
        add(7.5, 5);
        add(-3, -5.3);


    }

    public function testAddFunctionParametersAreArrayThrowError(): void
    {
        // expect InvalidArgumentException when one or more or parameter are not Integers
        $this->expectException(InvalidArgumentException::class);
        $this->expectExceptionMessage('add function only accepts integers as parameters');
        add([3], [2]);
        add([7], [5]);


    }

}

