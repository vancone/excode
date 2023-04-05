/*
Copyright © 2023 Tenton Lien

*/
package main

import (
	"encoding/json"
	"encoding/xml"
	"excode-cli/entity"
	"excode-cli/util"
	"fmt"
	lua "github.com/yuin/gopher-lua"
	"io/ioutil"
	"time"
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

	if project.Templates != nil {
		for _, template := range project.Templates {
			bytes, err = ioutil.ReadFile("../templates/" + template.Type + "spring-boot-2.json")
			if err != nil {
				fmt.Println("failed to read xml file:", err)
				return
			}
			var templateConfig entity.TemplateConfig
			err = json.Unmarshal(bytes, &templateConfig)
			if err != nil {
				return
			}
			fmt.Println(templateConfig)
			generate(templateConfig, project)
		}
	}

	//cmd.Execute()
}

func generate(config entity.TemplateConfig, project entity.Project) {
	fmt.Println("generating...")
	baseUrl := "../output-" + time.Now().Format("20060102150405")
	err := util.CreateDir(baseUrl)
	if err != nil {
		panic(err)
	}
	traverseStructure(config.Structure, baseUrl)
}

func traverseStructure(structure entity.Structure, baseUrl string) {
	fmt.Println(structure.Name)
	if structure.Type == "dir" {
		baseUrl += "/" + structure.Name
		err := util.CreateDir(baseUrl)
		if err != nil {
			panic(err)
		}
	} else if structure.Type == "file" {
		err := util.CopyFile(baseUrl+"/"+structure.Name, "../templates/spring-boot/"+structure.Source)
		if err != nil {
			panic(err)
		}
	}

	if structure.Children != nil {
		for _, child := range structure.Children {
			traverseStructure(child, baseUrl)
		}
	}
}

func ExecLuaScript() {
	L := lua.NewState()
	defer L.Close()
	if err := L.DoFile("../templates/spring-boot/spring-boot.lua"); err != nil {
		panic(err)
	}
	f := L.GetGlobal("greet")
	L.Push(f)
	L.Call(0, 0)
}
