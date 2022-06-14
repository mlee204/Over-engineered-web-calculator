package main

import (
	"html/template"
	"net/http"
	"path"
)

func error500(w http.ResponseWriter, data []string) {

	lp := path.Join("templates", "500.html")
	tmpl, err := template.ParseFiles(lp)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return

	}
	templateData := map[string]interface{}{"ErrorMessage": data}
	if err := tmpl.Execute(w, templateData); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}

}
