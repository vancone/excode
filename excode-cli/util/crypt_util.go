package util

import (
	"errors"
	"github.com/Mystery00/go-jasypt"
	"github.com/Mystery00/go-jasypt/iv"
	"github.com/Mystery00/go-jasypt/salt"
	"log"
	"os"
)

const (
	// specify the algorithm
	algorithm = "PBEWithHMACSHA512AndAES_256"
)

var password string

func init() {
	const cryptKeyName = "VANCONE_CRYPT_KEY"
	password = os.Getenv(cryptKeyName)
	if password == "" {
		log.Printf("Failed to read environment variable '%s', you should set the variable first\n", cryptKeyName)
	}
}

func Encrypt(message string) (string, error) {
	if password == "" {
		return "", errors.New("password required")
	}
	// create a new instance of jasypt
	encryptor := jasypt.New(algorithm, jasypt.NewConfig(
		jasypt.SetPassword(password),
		jasypt.SetSaltGenerator(salt.RandomSaltGenerator{}),
		jasypt.SetIvGenerator(iv.RandomIvGenerator{}),
	))
	// encrypt the message
	return encryptor.Encrypt(message)
}

func Decrypt(encode string) (string, error) {
	if password == "" {
		return "", errors.New("password required")
	}
	// create a new instance of jasypt
	encryptor := jasypt.New(algorithm, jasypt.NewConfig(
		jasypt.SetPassword(password),
		jasypt.SetSaltGenerator(salt.RandomSaltGenerator{}),
		jasypt.SetIvGenerator(iv.RandomIvGenerator{}),
	))
	// decrypt the message
	return encryptor.Decrypt(encode)
}
