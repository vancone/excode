package util

import (
	lua "github.com/yuin/gopher-lua"
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
