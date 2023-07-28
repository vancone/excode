package util

import (
	"encoding/json"
	"fmt"
	lua "github.com/yuin/gopher-lua"
	"io/ioutil"
	"log"
	"reflect"
	"strings"
	"time"
)

func JasyptEncrypt(L *lua.LState) int {
	arg := L.ToString(1)
	decryptedValue, err := Encrypt(arg)
	if err != nil {
		log.Println(err.Error())
		L.Push(lua.LString(arg))
	} else {
		L.Push("ENC(" + lua.LString(decryptedValue) + ")")
	}
	return 1
}

func ReadTemplateFile(L *lua.LState) int {
	templateName := L.ToString(1)
	filePath := L.ToString(2)
	userData := L.ToUserData(3)
	paramMap := map[string]string{}
	if userData != nil {
		v := reflect.ValueOf(userData.Value)
		prefix := strings.ToLower(reflect.TypeOf(userData.Value).String())
		if strings.Contains(prefix, ".") {
			prefix = prefix[strings.Index(prefix, ".")+1:]
		}
		if v.Kind() == reflect.Ptr {
			v = v.Elem()
		}
		if v.Kind() != reflect.Struct {
			fmt.Println("data is not a struct")
			return 0
		}
		for i := 0; i < v.NumField(); i++ {
			field := v.Field(i)
			paramMap[prefix+"."+v.Type().Field(i).Name] = fmt.Sprintf("%v", field.Interface())
		}
	}

	bytes, err := ioutil.ReadFile(GetExecutableDir() + "/templates/" + templateName + "/" + filePath)
	if err != nil {
		log.Println(err.Error())
		L.Push(lua.LString(""))
	} else {
		content := string(bytes)
		content = strings.Replace(content, "${date}", time.Now().Format("2006/01/02"), -1)
		if len(paramMap) > 0 {
			for k, v := range paramMap {
				if strings.Contains(content, "${"+k+"}") {
					content = strings.Replace(content, "${"+k+"}", v, -1)
				}
			}
		}
		L.Push(lua.LString(content))
	}
	return 1
}

func AddIndent(L *lua.LState) int {
	arg := L.ToString(1)
	indentSpaceCount := L.ToInt(2)
	indent := strings.Repeat(" ", indentSpaceCount)
	lines := strings.Split(strings.ReplaceAll(arg, "\r\n", "\n"), "\n")
	result := ""
	for k, v := range lines {
		result += indent + v
		if k != len(lines)-1 {
			result += "\n"
		}
	}
	L.Push(lua.LString(result))
	return 1
}

func JsonFormat(L *lua.LState) int {
	arg := L.ToString(1)
	var result map[string]interface{}
	err := json.Unmarshal([]byte(arg), &result)
	if err != nil {
		return 0
	}
	formattedData, _ := json.MarshalIndent(result, "", "    ")
	L.Push(lua.LString(formattedData))
	return 1
}
