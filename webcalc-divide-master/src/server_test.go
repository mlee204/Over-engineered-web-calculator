package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestWebserver(t *testing.T) {
	tt := []struct {
		name             string
		x                string
		y                string
		urlError         string
		expectedResponse string
		expectedStatus   int
	}{
		{
			name:             "divide 10 by 2",
			x:                "10",
			y:                "2",
			expectedResponse: `{"error":false,"string":"10/2=5","answer":5,"status":200}`,
			expectedStatus:   http.StatusOK,
		},
		{
			name:             "missing url parameters",
			x:                "",
			y:                "",
			expectedResponse: `{"error":true,"string":"Url Param 'x' is missing Url Param 'y' is missing ","answer":0,"status":400}`,
			expectedStatus:   http.StatusBadRequest,
		},
		{
			name:             "missing url x parameters",
			x:                "",
			y:                "5",
			expectedResponse: `{"error":true,"string":"Url Param 'x' is missing ","answer":0,"status":400}`,
			expectedStatus:   http.StatusBadRequest,
		},
		{
			name:             "missing url y parameters",
			x:                "5",
			y:                "",
			expectedResponse: `{"error":true,"string":"Url Param 'y' is missing ","answer":0,"status":400}`,
			expectedStatus:   http.StatusBadRequest,
		},
		{
			name:             "Wrong data type check string",
			x:                "wrong",
			y:                "data",
			expectedResponse: `{"error":true,"string":"parameter x(wrong) is not a valid Integer parameter y(data) is not a valid Integer ","answer":0,"status":400}`,
			expectedStatus:   http.StatusBadRequest},
		{
			name:             "check for invalid url/ 404 catch",
			urlError:         "/notfound",
			expectedResponse: `{"error":true,"string":"Page not found: check url parameter syntax is correct","answer":0,"status":404}`,
			expectedStatus:   http.StatusNotFound,
		},
		{
			name:             "divide by zero error",
			x:                "10",
			y:                "0",
			expectedResponse: `{"error":true,"string":"can't divide by 0 ","answer":0,"status":400}`,
			expectedStatus:   http.StatusBadRequest,
		},
	}

	for _, tc := range tt {
		t.Run(tc.name, func(t *testing.T) {
			request, err := http.NewRequest("GET", "/?x="+tc.x+"&y="+tc.y, nil)

			if tc.urlError != "" {
				request, err = http.NewRequest("GET", "/"+tc.urlError, nil)
			}

			if err != nil {
				t.Fatalf("could not create request: %v", err)
			}
			responseRecorder := httptest.NewRecorder()
			webserver(responseRecorder, request)

			response := responseRecorder.Result()
			defer response.Body.Close()

			responseBody, err := ioutil.ReadAll(response.Body)
			if err != nil {
				t.Fatalf("could not read response: %v", err)
			}

			// check response http code
			if response.StatusCode != tc.expectedStatus {
				t.Errorf("expected status %v; got %v", tc.expectedStatus, response.StatusCode)
			}
			// check content type
			if response.Header.Get("Content-Type") != "application/json" {
				t.Errorf("expected Header Content-Type %v; got %v", "application/json", response.Header.Get("Content-Type"))
			}
			// check JSON response
			_, err = json.Marshal(string(bytes.TrimSpace(responseBody)))
			if err != nil {
				t.Fatalf("expected an JSON; got %s", responseBody)
			}

			// check JSON matches
			if msg := string(bytes.TrimSpace(responseBody)); msg != tc.expectedResponse {
				t.Errorf("expected JSON response %q; got %q", tc.expectedResponse, msg)
			}

		})
	}
}
func TestRouting(t *testing.T) {

	tt := []struct {
		name             string
		x                string
		y                string
		urlError         string
		expectedResponse string
		expectedStatus   int
	}{
		{
			name:             "divide 10 by 2",
			x:                "10",
			y:                "2",
			expectedResponse: `{"error":false,"string":"10/2=5","answer":5,"status":200}`,
			expectedStatus:   http.StatusOK,
		},
		{
			name:             "missing url parameters",
			x:                "",
			y:                "",
			expectedResponse: `{"error":true,"string":"Url Param 'x' is missing Url Param 'y' is missing ","answer":0,"status":400}`,
			expectedStatus:   http.StatusBadRequest,
		},
		{
			name:             "Wrong data type check",
			x:                "wrong",
			y:                "data",
			expectedResponse: `{"error":true,"string":"parameter x(wrong) is not a valid Integer parameter y(data) is not a valid Integer ","answer":0,"status":400}`,
			expectedStatus:   http.StatusBadRequest},
		{
			name:             "check for invalid url/ 404 catch",
			urlError:         "/notfound",
			expectedResponse: `{"error":true,"string":"Page not found: check url parameter syntax is correct","answer":0,"status":404}`,
			expectedStatus:   http.StatusNotFound,
		},
		{
			name:             "divide by zero error",
			x:                "10",
			y:                "0",
			expectedResponse: `{"error":true,"string":"can't divide by 0 ","answer":0,"status":400}`,
			expectedStatus:   http.StatusBadRequest,
		},
	}

	for _, tc := range tt {
		t.Run(tc.name, func(t *testing.T) {

			server := httptest.NewServer(handler())
			defer server.Close()

			response, err := http.Get(fmt.Sprintf("%s/?x=%s&y=%s", server.URL, tc.x, tc.y))

			if tc.urlError != "" {
				response, err = http.Get(fmt.Sprintf("%s/%s", server.URL, tc.urlError))
			}

			// check request received a response
			if err != nil {
				t.Fatalf("could not send GET request: %v", err)
			}
			defer response.Body.Close()

			// check response http code
			if response.StatusCode != tc.expectedStatus {
				t.Errorf("expected status OK; got %v", response.Status)
			}

			b, err := ioutil.ReadAll(response.Body)
			if err != nil {
				t.Fatalf("could not read response: %v", err)
			}

			// check content type
			if response.Header.Get("Content-Type") != "application/json" {
				t.Errorf("expected status %v; got %v", tc.expectedStatus, response.StatusCode)
			}

			// check JSON response
			_, err = json.Marshal(string(bytes.TrimSpace(b)))
			if err != nil {
				t.Fatalf("expected an JSON; got %s", b)
			}

			// check JSON matches
			if msg := string(bytes.TrimSpace(b)); msg != tc.expectedResponse {
				t.Errorf("expected JSON response %q; got %q", tc.expectedResponse, msg)
			}
		})
	}
}
