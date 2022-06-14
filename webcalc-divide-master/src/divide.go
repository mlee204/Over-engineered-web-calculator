package main

import "errors"

func divide(a int, b int) (int, error) {

	// error handling to catch divide by zero
	if b == 0 {
		return 0, errors.New("can not divide by zero")
	}
	var result = a / b

	return result, nil
}
