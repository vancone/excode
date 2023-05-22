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
	"log"
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
	//fmt.Println(project)

	if project.Templates != nil {
		for _, template := range project.Templates {
			//fmt.Println(template.Type)
			bytes, err = ioutil.ReadFile(fmt.Sprintf("../templates/%s/config.json", template.Type))
			if err != nil {
				fmt.Println("failed to read xml file:", err)
				return
			}
			templateStr := util.ParseDynamicParams(string(bytes), project, template)
			var templateConfig entity.TemplateConfig
			err = json.Unmarshal([]byte(templateStr), &templateConfig)
			if err != nil {
				return
			}
			fmt.Println(templateConfig)
			generate(templateConfig, project, template)
		}
	}

	//cmd.Execute()
}

func generate(config entity.TemplateConfig, project entity.Project, template entity.Template) {
	fmt.Println("generating...")
	baseUrl := "../output" // + time.Now().Format("20060102150405")
	err := util.CreateDir(baseUrl)
	if err != nil {
		panic(err)
	}
	traverseStructure(config.Structure, baseUrl, config.Name, project, template)
}

func traverseStructure(structure entity.Structure, baseUrl string, templateName string,
	project entity.Project, template entity.Template) {
	fmt.Println(structure.Name)
	if structure.Type == "dir" {
		baseUrl += "/" + structure.Name
		err := util.CreateDir(baseUrl)
		if err != nil {
			panic(err)
		}
	} else if structure.Type == "file" {
		if structure.Transformer != "" {
			ExecLuaScript(templateName)
		}
		dstFileName := baseUrl + "/" + structure.Name
		srcFileName := fmt.Sprintf("../templates/%s/%s", templateName, structure.Source)
		if structure.Dynamic {
			templateFile, err := ioutil.ReadFile(srcFileName)
			if err != nil {
				log.Printf("Failed to read template file: %s", srcFileName)
				return
			}
			templateFile = []byte(util.ParseDynamicParams(string(templateFile), project, template))
			err = ioutil.WriteFile(dstFileName, templateFile, 0666)
			if err != nil {
				log.Printf("Failed to write file: %s", dstFileName)
			}
		} else {
			err := util.CopyFile(dstFileName, srcFileName)
			if err != nil {
				panic(err)
			}
		}
	}

	if structure.Children != nil {
		for _, child := range structure.Children {
			traverseStructure(child, baseUrl, templateName, project, template)
		}
	}
}

func ExecLuaScript(templateName string) {
	L := lua.NewState()
	defer L.Close()
	if err := L.DoFile(fmt.Sprintf("../templates/%s/transform.lua", templateName)); err != nil {
		panic(err)
	}
	f := L.GetGlobal("test1")
	L.Push(f)
	table := lua.LTable{}
	L.Push(lua.LString(templateName))
	L.Call(1, 1)
	fmt.Println(L.Get(-1))
}
