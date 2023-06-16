package util

import (
	lua "github.com/yuin/gopher-lua"
	"io/ioutil"
	"log"
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

	bytes, err := ioutil.ReadFile("templates/" + templateName + "/" + filePath)
	if err != nil {
		log.Println(err.Error())
		L.Push(lua.LString(""))
	} else {
		L.Push(lua.LString(bytes))
	}
	return 1
}
