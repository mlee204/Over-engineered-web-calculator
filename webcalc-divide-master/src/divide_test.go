package main

import (
	"errors"
	"reflect"
	"testing"
)

func TestDivide(t *testing.T) {

	testAnswer := []struct {
		name   string
		x      int
		y      int
		result int
	}{
		{name: "divide normal", x: 12, y: 4, result: 3},
		{name: "divide negative numbers", x: -4, y: 4, result: -1},
		{name: "divide when y is zero should return 0", x: 12, y: 0, result: 0},
	}

	for _, tc := range testAnswer {
		t.Run(tc.name, func(t *testing.T) {
			got, _ := divide(tc.x, tc.y)
			if !reflect.DeepEqual(tc.result, got) {
				t.Errorf("expected: %v, got: %v", tc.result, got)
			}
		})
	}
	// testAnswer error handling
	testErrorHandling := []struct {
		name   string
		x      int
		y      int
		result error
	}{
		{name: "divide normal - error output nil", x: 12, y: 4, result: nil},
		{name: "divide negative numbers - error output null", x: -4, y: 4, result: nil},
		{name: "divide when y is zero - error output should return Error", x: 12, y: 0, result: errors.New("can not divide by zero")},
	}

	for _, tc := range testErrorHandling {
		t.Run(tc.name, func(t *testing.T) {
			_, got := divide(tc.x, tc.y)
			if !reflect.DeepEqual(tc.result, got) {
				t.Errorf("expected: %v, got: %v", tc.result, got)
			}
		})
	}
}
