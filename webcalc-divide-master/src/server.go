package main

import (
	"encoding/json"
	"log"
	"net/http"
	"strconv"
)

type Message struct {
	Error  bool   `json:"error"`
	String string `json:"string"`
	Answer int    `json:"answer"`
	Status int    `json:"status"`
}

type ServiceDiscovery struct {
	Service        string `json:"service"`
	Param1         string `json:"url parameter 1"`
	Parma2         string `json:"url parameter 2"`
	Operator       string `json:"operator"`
	Parma1DataType string `json:"parameter 1 data type"`
	Parma2DataType string `json:"parameter 2 data type"`
	Returns        string `json:"returns"`
	ReturnType     string `json:"return type"`
}

func main() {
	err := http.ListenAndServe(":8080", handler())
	if err != nil {
		log.Fatal(err)
	}
}

func handler() http.Handler {
	r := http.NewServeMux()
	r.HandleFunc("/", webserver)
	r.HandleFunc("/service-discovery", serviceDiscovery)
	return r
}

func webserver(w http.ResponseWriter, r *http.Request) {
	output := Message{false, "", 0, 200}
	stringMessages := ""
	status := 200
	errorOutput := false

	w.Header().Set("Content-Type", "application/json")
	w.Header().Set("Access-Control-Allow-Origin", "*")

	// handle 404 page not found
	if r.URL.Path != "/" {
		status = 404
		stringMessages = "Page not found: check url parameter syntax is correct"
		output = Message{true, stringMessages, 0, status}
		w.WriteHeader(status)
		js, _ := json.Marshal(output)
		w.Write(js)
		return
	}

	x := r.URL.Query().Get("x")
	y := r.URL.Query().Get("y")

	// error check for for x and y parameters
	if x == "" {
		stringMessages += "Url Param 'x' is missing "
		status = 400
		errorOutput = true
	}

	if y == "" {
		stringMessages += "Url Param 'y' is missing "
		status = 400
		errorOutput = true
	}

	// error check for for x and y data type
	xInt, err := strconv.Atoi(x)
	if err != nil && x != "" {
		stringMessages += "parameter x(" + x + ") is not a valid Integer "
		status = 400
		errorOutput = true
	}

	yInt, err := strconv.Atoi(y)
	if err != nil && y != "" {
		stringMessages += "parameter y(" + y + ") is not a valid Integer "
		status = 400
		errorOutput = true
	}

	// can't divide by zero
	result, err := divide(xInt, yInt)
	if err != nil && !errorOutput {
		zeroMessage := "can't divide by 0 "
		stringMessages += zeroMessage
		errorOutput = true
		status = 400
	}

	if !errorOutput {
		stringMessages = x + "/" + y + "=" + strconv.Itoa(result)
	}

	output = Message{errorOutput, stringMessages, result, status}

	js, err := json.Marshal(output)
	if err != nil {
		return
	}

	w.WriteHeader(status)
	w.Write(js)
}

func serviceDiscovery(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.Header().Set("Access-Control-Allow-Origin", "*")

	output := ServiceDiscovery{
		"divide",
		"x",
		"y",
		"/",
		"Integer",
		"Integer",
		"x/y",
		"Integer",
	}

	js, err := json.Marshal(output)
	if err != nil {
		return
	}

	w.WriteHeader(200)
	w.Write(js)
}
