/*
Copyright © 2023 NAME HERE <EMAIL ADDRESS>

*/
package main

import (
	"encoding/xml"
	"excode-cli/entity"
	"fmt"
	"github.com/aarzilli/golua/lua"
	"io/ioutil"
)

func main() {
	bytes, err := ioutil.ReadFile("../examples/e-school.xml")
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
	l := lua.NewState()
	l.DoFile("../templates/spring-boot.lua")
	l.GetGlobal("greet")
	l.Call(0, 0)
	//cmd.Execute()
}
