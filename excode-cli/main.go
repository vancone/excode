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
	"reflect"
	"regexp"
	"strings"
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
			templateStr := string(bytes)
			// Disable greedy match
			re, _ := regexp.Compile("(?U)\\$\\{.*\\}")
			all := re.FindAll([]byte(templateStr), -1)
			s := reflect.ValueOf(&project).Elem()
			for _, v := range all {
				fmt.Println("all ", string(v))
				paramName := string(v)[2 : len(string(v))-1]
				if len(paramName) == 0 {
					log.Printf("Invalid param name: %s", string(v))
					continue
				}

				if strings.Index(paramName, "project.") == 0 {
					paramName = strings.Replace(paramName, "project.", "", -1)
					if s.FieldByName(paramName).IsValid() {
						fmt.Println("Parse param", s.FieldByName(paramName).Interface())
					} else {
						log.Printf("Unrecognized param: %s", paramName)
						continue
					}
				} else if strings.Index(paramName, "template.") == 0 {
					paramName = strings.Replace(paramName, "template.", "", -1)
					if strings.Contains(paramName, "properties.") {
						paramName = strings.Replace(paramName, "properties.", "", -1)
						propertyFoundFlag := false
						for _, property := range template.Properties {
							if property.Name == paramName {
								templateStr = strings.Replace(templateStr, string(v), property.Value, -1)
								propertyFoundFlag = true
								break
							}
						}
						if !propertyFoundFlag {
							log.Printf("Param %s not found in template's properties", paramName)
						}
					}
				}

			}

			var templateConfig entity.TemplateConfig
			err = json.Unmarshal([]byte(templateStr), &templateConfig)
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
	baseUrl := "../output" // + time.Now().Format("20060102150405")
	err := util.CreateDir(baseUrl)
	if err != nil {
		panic(err)
	}
	traverseStructure(config.Structure, baseUrl, config.Name)
}

func traverseStructure(structure entity.Structure, baseUrl string, templateName string) {
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
		srcFileName := fmt.Sprintf("../templates/%s/%s", templateName, structure.Source)
		dstFileName := baseUrl + "/" + structure.Name
		err := util.CopyFile(dstFileName, srcFileName)
		if err != nil {
			panic(err)
		}
	}

	if structure.Children != nil {
		for _, child := range structure.Children {
			traverseStructure(child, baseUrl, templateName)
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
	L.Push(lua.LString(templateName))
	L.Call(1, 1)
	fmt.Println(L.Get(-1))
}
