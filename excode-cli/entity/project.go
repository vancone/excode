package entity

type Project struct {
	Name       string     `xml:"name"`
	Version    string     `xml:"version"`
	Deployment Deployment `xml:"deployment"`
	Models     []Model    `xml:"models>model"`
	Templates  []Template `xml:"templates>template"`
}

type Deployment struct {
	Env []Env `xml:"env"`
}

type Env struct {
	Profile    string     `xml:"profile,attr"`
	Middleware Middleware `xml:"middleware"`
}

type Middleware struct {
	Mysql []Mysql `xml:"mysql"`
	Redis []Redis `xml:"redis"`
}

type Mysql struct {
	Name              string `xml:"name,attr"`
	Host              string `xml:"host,attr"`
	Port              int    `xml:"port,attr"`
	Database          string `xml:"database,attr"`
	User              string `xml:"user,attr"`
	Password          string `xml:"password,attr"`
	EncryptedPassword string `xml:"encryptedPassword"`
}

type Redis struct {
	Name              string `xml:"name,attr"`
	Host              string `xml:"host,attr"`
	Port              int    `xml:"port,attr"`
	Database          string `xml:"database,attr"`
	Password          string `xml:"password,attr"`
	EncryptedPassword string `xml:"encryptedPassword"`
}

type Model struct {
	Name        string  `xml:"name,attr"`
	Source      string  `xml:"source,attr"`
	TablePrefix string  `xml:"tablePrefix,attr"`
	Fields      []Field `xml:"field"`
}

type Field struct {
	Name          string `xml:"name,attr"`
	Type          string `xml:"type,attr"`
	DbType        string `xml:"dbType,attr"`
	Length        int    `xml:"length,attr"`
	Primary       bool   `xml:"primary,attr"`
	AutoIncrement bool   `xml:"autoIncrement,attr"`
	NotNull       bool   `xml:"notNull,attr"`
	Comment       string `xml:"comment,attr"`
}

type Template struct {
	Type       string     `xml:"type,attr"`
	Enabled    bool       `xml:"enabled,attr"`
	Plugins    []Plugin   `xml:"plugins>plugin"`
	Properties []Property `xml:"properties>property"`
}

type Plugin struct {
	Name    string `xml:"name,attr"`
	Enabled bool   `xml:"enabled,attr"`
}

type Property struct {
	Name  string `xml:"name,attr"`
	Value string `xml:"value,attr"`
}
