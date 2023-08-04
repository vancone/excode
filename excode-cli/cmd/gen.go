package cmd

import (
	"encoding/json"
	"encoding/xml"
	"excode-cli/entity"
	"excode-cli/util"
	"fmt"
	"github.com/spf13/cobra"
	lua "github.com/yuin/gopher-lua"
	"io/ioutil"
	luar "layeh.com/gopher-luar"
	"log"
	"os"
	"time"
)

var genCmd = &cobra.Command{
	Use:   "gen",
	Short: "Generate project based on templates",
	Long:  `Generate project based on templates`,
	Run: func(cmd *cobra.Command, args []string) {
		projectDir := "./"
		if len(args) == 1 {
			projectDir = args[0] + "/"
		}
		bytes, err := ioutil.ReadFile(projectDir + "excode.xml")
		if err != nil {
			log.Println("failed to read xml file:", err)
			return
		}
		var project entity.Project
		err = xml.Unmarshal(bytes, &project)
		if err != nil {
			return
		}

		log.Println("Scanning project: name = " + project.Name + ", version = " + project.Version)
		log.Printf("%d model(s) and %d template(s) found\n", len(project.Models), len(project.Templates))

		if project.Templates != nil {
			for _, template := range project.Templates {
				if !template.Enabled {
					continue
				}
				bytes, err = ioutil.ReadFile(fmt.Sprintf("%s/templates/%s/config.json", util.GetExecutableDir(), template.Name))
				if err != nil {
					log.Println("failed to read xml file:", err)
					return
				}
				templateStr := util.ParseDynamicParams(string(bytes), project, template)
				var templateConfig entity.TemplateConfig
				err = json.Unmarshal([]byte(templateStr), &templateConfig)
				if err != nil {
					return
				}
				generate(templateConfig, project, template, projectDir)
			}
		}
	},
}

func init() {
	rootCmd.AddCommand(genCmd)
}

func generate(config entity.TemplateConfig, project entity.Project, template entity.Template, projectDir string) {
	log.Println("========== Template [ " + template.Name + " ] ==========")
	log.Printf("%d plugin(s) and %d property(s) found\n", len(template.Plugins), len(template.Properties))
	startTime := time.Now()
	baseUrl := projectDir + "output" // + time.Now().Format("20060102150405")
	err := util.CreateDir(baseUrl)
	if err != nil {
		panic(err)
	}

	// Add git ignore file
	gitIgnoreContent := "output/\n"
	ioutil.WriteFile(projectDir+".gitignore", []byte(gitIgnoreContent), 0666)

	traverseStructure(config.Structure, baseUrl, config.Name, project, template)
	timeCost := time.Since(startTime)
	wd, err := os.Getwd()
	if err == nil {
		log.Println("Output directory: " + wd + string(os.PathSeparator) + baseUrl + string(os.PathSeparator) + template.Name)
	}
	log.Println("Generating template completed. Time cost:", timeCost)
}

func traverseStructure(structure entity.Structure, baseUrl string, templateName string,
	project entity.Project, template entity.Template) {
	if structure.Type == "dir" {
		baseUrl += "/" + structure.Name
		err := util.CreateDir(baseUrl)
		if err != nil {
			panic(err)
		}
		if structure.Generator != "" {
			ret := ExecLuaScript(templateName, structure.Generator, project, template, structure.Source)
			table := ret.(*lua.LTable)
			table.ForEach(func(L1 lua.LValue, L2 lua.LValue) {
				dstFileName := baseUrl + "/" + L1.String()
				err = ioutil.WriteFile(dstFileName, []byte(L2.String()), 0666)
			})
		}
		if len(structure.InitialCopy) > 0 {
			src := util.GetExecutableDir() + "/templates/" + templateName + "/" + structure.InitialCopy
			log.Printf("Initial copy: from %s to %s", src, baseUrl)
			err := util.CopyDir(baseUrl, src)
			if err != nil {
				log.Printf("Failed to copy directory (%s): %s", structure.InitialCopy, err)
				return
			}
		}
	} else if structure.Type == "file" {

		dstFileName := baseUrl + "/" + structure.Name
		srcFileName := fmt.Sprintf("%s/templates/%s/%s", util.GetExecutableDir(), templateName, structure.Source)
		if structure.Dynamic {
			templateFile, err := ioutil.ReadFile(srcFileName)
			if err != nil {
				log.Printf("Failed to read template file: %s", srcFileName)
				return
			}
			templateFile = []byte(util.ParseDynamicParams(string(templateFile), project, template))
			err = ioutil.WriteFile(dstFileName, templateFile, 0666)
			if err != nil {
				log.Printf("Failed to write file: %s", dstFileName)
			}
		} else if structure.Transformer != "" {
			ret := ExecLuaScript(templateName, structure.Transformer, project, template, structure.Source)
			if ret != nil && ret.String() != "nil" {
				table := ret.(*lua.LTable)
				table.ForEach(func(L1 lua.LValue, L2 lua.LValue) {
					if L1 != nil && L2 != nil {
						dstFileName := baseUrl + "/" + L1.String()
						err := ioutil.WriteFile(dstFileName, []byte(L2.String()), 0666)
						if err != nil {
							log.Printf("Failed to write tranformed files(%s): %s", dstFileName, err)
						}
					}
				})
			}
		} else {
			err := util.CopyFile(dstFileName, srcFileName)
			if err != nil {
				panic(err)
			}
		}
	}

	if structure.Children != nil {
		for _, child := range structure.Children {
			traverseStructure(child, baseUrl, templateName, project, template)
		}
	}
}

func ExecLuaScript(templateName string, funcName string, project entity.Project, template entity.Template, source string) lua.LValue {
	L := lua.NewState()
	defer L.Close()

	if err := L.DoFile(fmt.Sprintf("%s/templates/%s/transform.lua", util.GetExecutableDir(), templateName)); err != nil {
		panic(err)
	}

	L.SetGlobal("project", luar.New(L, project))
	L.SetGlobal("template", luar.New(L, template))

	properties := map[string]string{}
	for _, item := range template.Properties {
		properties[item.Name] = item.Value
	}
	L.SetGlobal("properties", luar.New(L, properties))

	plugins := map[string]bool{}
	for _, item := range template.Plugins {
		if item.Enabled {
			plugins[item.Name] = true
		}
	}
	L.SetGlobal("plugins", luar.New(L, plugins))

	enums := map[string]entity.Enum{}
	for _, item := range project.Enums {
		enums[item.Name] = item
	}
	L.SetGlobal("enums", luar.New(L, enums))

	L.SetGlobal("go_jasypt_encrypt", L.NewFunction(util.JasyptEncrypt))
	L.SetGlobal("go_read_template_file", L.NewFunction(util.ReadTemplateFile))
	L.SetGlobal("go_add_indent", L.NewFunction(util.AddIndent))
	L.SetGlobal("go_json_format", L.NewFunction(util.JsonFormat))

	if source != "" {
		srcFileName := fmt.Sprintf("%s/templates/%s/%s", util.GetExecutableDir(), templateName, source)
		sourceBytes, err := ioutil.ReadFile(srcFileName)
		if err != nil {
			log.Printf("Failed to read source file before executing Lua script: %s", srcFileName)
			return nil
		}
		sourceStr := util.ParseDynamicParams(string(sourceBytes), project, template)
		L.SetGlobal("source", lua.LString(sourceStr))
	}

	err := L.CallByParam(lua.P{
		Fn:      L.GetGlobal(funcName),
		NRet:    1,
		Protect: true,
	})
	if err != nil {
		log.Printf("Failed to execute lua function %s: %s", funcName, err)
		return nil
	}
	return L.Get(-1)
}
