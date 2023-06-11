package util

import (
	"excode-cli/entity"
	"fmt"
	"log"
	"reflect"
	"regexp"
	"strings"
	"time"
)

func ParseDynamicParams(content string, project entity.Project, template entity.Template) string {
	// Disable greedy match
	re, _ := regexp.Compile("(?U)\\$\\{.*\\}")
	all := re.FindAll([]byte(content), -1)
	s := reflect.ValueOf(&project).Elem()
	for _, v := range all {
		paramName := string(v)[2 : len(string(v))-1]
		if len(paramName) == 0 {
			log.Printf("Invalid param name: %s", string(v))
			continue
		}

		if paramName == "date" {
			content = strings.Replace(content, "${date}", time.Now().Format("2006/01/02"), -1)
		}

		if strings.Index(paramName, "project.") == 0 {
			paramName = strings.Replace(paramName, "project.", "", -1)
			if s.FieldByName(paramName).IsValid() {
				fmt.Println("Parse param", s.FieldByName(paramName).Interface())
				content = strings.Replace(content, string(v), s.FieldByName(paramName).String(), -1)
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
						content = strings.Replace(content, string(v), property.Value, -1)
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
	return content
}
