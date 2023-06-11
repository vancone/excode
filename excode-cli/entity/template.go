package entity

type TemplateConfig struct {
	Name      string            `json:"name"`
	Version   string            `json:"version"`
	Metadata  map[string]string `json:"metadata"`
	Structure Structure         `json:"structure"`
}

type Structure struct {
	Name        string      `json:"name"`
	Type        string      `json:"type"`
	Source      string      `json:"source"`
	Dynamic     bool        `json:"dynamic"`
	Transformer string      `json:"transformer"`
	Generator   string      `json:"generator"`
	Children    []Structure `json:"children"`
	InitialCopy string      `json:"initialCopy"`
}
