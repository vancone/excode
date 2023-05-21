package util

import (
	"fmt"
	"io/ioutil"
	"os"
)

func HasDir(path string) (bool, error) {
	_, err := os.Stat(path)
	if err == nil {
		return true, nil
	}
	if os.IsNotExist(err) {
		return false, nil
	}
	return false, err
}

func CreateDir(path string) error {
	exist, err := HasDir(path)
	if err != nil {
		fmt.Printf("Failed to check directory -> %v\n", err)
		return err
	}
	if exist {
		fmt.Println("Directory already exists")
	} else {
		err := os.MkdirAll(path, os.ModePerm)
		if err != nil {
			fmt.Printf("Faile to create directory -> %v\n", err)
			return err
		}
	}
	return nil
}

func CopyFile(dst string, src string) error {
	input, err := ioutil.ReadFile(src)
	if err != nil {
		fmt.Println(err)
		return err
	}

	err = ioutil.WriteFile(dst, input, 0644)
	if err != nil {
		fmt.Println("Error creating", dst)
		fmt.Println(err)
		return err
	}
	return nil
}
