package util

import (
	"fmt"
	"io/ioutil"
	"os"
	"path/filepath"
	"strings"
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

func CopyDir(dst string, src string) error {
	var files []string

	err := filepath.Walk(src, func(path string, info os.FileInfo, err error) error {
		if info.IsDir() {
			dstPath := strings.Replace(strings.Replace(path, "\\", "/", -1), src, dst, 1)
			CreateDir(dstPath)
		} else {
			files = append(files, path)
		}
		return nil
	})
	if err != nil {
		return err
	}
	for _, file := range files {
		srcPath := strings.Replace(file, "\\", "/", -1)
		dstPath := strings.Replace(srcPath, src, dst, 1)
		err = CopyFile(dstPath, srcPath)
		if err != nil {
			return err
		}
	}
	return nil
}
