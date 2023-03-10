package entity

type Project struct {
	Name       string     `xml:"name"`
	Version    string     `xml:"version"`
	Middleware Middleware `xml:"middleware"`
	Models     []Model    `xml:"models>model"`
	Plugins    []Plugin   `xml:"plugins>plugin"`
}

type Middleware struct {
	MySql MySql `xml:"mysql"`
}

type MySql struct {
	Name string `xml:"name,attr"`
	Port int    `xml:"port,attr"`
}

type Model struct {
	Name   string  `xml:"name,attr"`
	Source string  `xml:"source,attr"`
	Fields []Field `xml:"field"`
}

type Field struct {
	Name    string `xml:"name,attr"`
	Type    string `xml:"type,attr"`
	Length  int    `xml:"length,attr"`
	Primary bool   `xml:"primary,attr"`
	Comment string `xml:"comment,attr"`
}

type Plugin struct {
	Name    string `xml:"name,attr"`
	Enabled bool   `xml:"enabled,attr"`
}
