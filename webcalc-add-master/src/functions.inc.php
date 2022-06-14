<?php

function add($x, $y): int
{
    if ($x === null || $y === null) {
        throw new BadMethodCallException('parameters are null');
    }

    if(!is_int($x) || (!is_int($x))) {
        throw new InvalidArgumentException('add function only accepts integers as parameters');

    }

	return $x+$y;
}

function isInteger($input): bool
{
    if ($input[0] == '-') {
        return ctype_digit(substr($input, 1));
    }
    return (ctype_digit(strval($input)));
}

