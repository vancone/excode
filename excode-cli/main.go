/*
Copyright © 2023 NAME HERE <EMAIL ADDRESS>

*/
package main

import (
	"encoding/xml"
	"excode-cli/entity"
	"fmt"
	"io/ioutil"
)

func main() {
	bytes, err := ioutil.ReadFile("../examples/mall.xml")
	if err != nil {
		fmt.Println("failed to read xml file:", err)
		return
	}
	var project entity.Project
	err = xml.Unmarshal(bytes, &project)
	if err != nil {
		return
	}
	fmt.Println(project)
	//cmd.Execute()
}
