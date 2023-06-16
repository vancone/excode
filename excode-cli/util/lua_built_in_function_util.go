package util

import (
	"fmt"
	lua "github.com/yuin/gopher-lua"
	"io/ioutil"
	"log"
	"reflect"
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
	paramMap := L.ToUserData(3)
	if paramMap != nil {
		v := reflect.ValueOf(paramMap.Value)
		fmt.Println(reflect.TypeOf(paramMap.Value))
		if v.Kind() == reflect.Ptr {
			v = v.Elem()
		}
		if v.Kind() != reflect.Struct {
			fmt.Println("data is not a struct")
			//return
		}
		for i := 0; i < v.NumField(); i++ {
			field := v.Field(i)
			fmt.Printf("Field %d: %s = %v\n", i, v.Type().Field(i).Name, field.Interface())
		}
	}

	bytes, err := ioutil.ReadFile("templates/" + templateName + "/" + filePath)
	if err != nil {
		log.Println(err.Error())
		L.Push(lua.LString(""))
	} else {
		L.Push(lua.LString(bytes))
	}
	return 1
}
