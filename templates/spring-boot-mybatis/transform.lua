function greet()
    print(project.Name)
    print("External Module [ spring-boot ] starts")
end

function test1(name)
    print("Lua print: " .. name)
    return "hahaha"
end

function generateEntities()
    print("Start to generate entities...")
    models = project.Models
    files = {}
    for i = 1, #models do
        -- print(models[i].Name)
        local finalSource = string.gsub(source, "${modelName}", models[i].Name:gsub("^%l",string.upper))
        local fieldCode = ""
        for k = 1, #models[i].Fields do
            fieldCode = fieldCode.."    private String "..models[i].Fields[k].Name..";\n"
        end
        finalSource = finalSource.gsub(finalSource, "${fields}", fieldCode)
        files[(models[i].Name:gsub("^%l",string.upper))..".java"] = finalSource
    end
    return files
end

function generateMappers()
    print("Start to generate mappers...")
    models = project.Models
    files = {}
    for i = 1, #models do
        local modelName = models[i].Name
        local ModelName = modelName:gsub("^%l",string.upper)
        local finalSource = string.gsub(source, "${ModelName}", ModelName)
        finalSource = string.gsub(finalSource, "${modelName}", modelName)
        files[ModelName.."Mapper.java"] = finalSource
    end
    return files
end

function generateMapperXmlFiles()
    print("Start to generate mapper xml files...")
    models = project.Models
    files = {}
    for i = 1, #models do
        local modelName = models[i].Name
        local ModelName = modelName:gsub("^%l",string.upper)
        local finalSource = string.gsub(source, "${ModelName}", ModelName)
        finalSource = string.gsub(finalSource, "${modelName}", modelName)
        finalSource = string.gsub(finalSource, "${tableName}", models[i].TablePrefix..modelName)

        -- Fill in result map fields
        local resultMapFieldsCode = ""
        for k = 1, #models[i].Fields do
            local field = models[i].Fields[k]
            if (field.Primary)
            then
                resultMapFieldsCode = resultMapFieldsCode.."        <id column=\""..field.Name.."\" property=\""..field.Name.."\" jdbcType=\"VARCHAR\"/>\n"
            else
                resultMapFieldsCode = resultMapFieldsCode.."        <result column=\""..field.Name.."\" property=\""..field.Name.."\" jdbcType=\"VARCHAR\"/>\n"
            end
        end
        finalSource = finalSource.gsub(finalSource, "${resultMapFields}", resultMapFieldsCode)

        -- Fill in basic params and MyBatis params
        local basicParamsCode = ""
        local mybatisParamsCode = ""
        for k = 1, #models[i].Fields do
            local field = models[i].Fields[k]
            if k > 1
            then
                basicParamsCode = basicParamsCode..", "
                mybatisParamsCode = mybatisParamsCode..", "
            end
            basicParamsCode = basicParamsCode.."`"..field.Name.."`"
            mybatisParamsCode = mybatisParamsCode.."#{"..field.Name.."}"
        end
        finalSource = finalSource.gsub(finalSource, "${basicParams}", basicParamsCode)
        finalSource = finalSource.gsub(finalSource, "${mybatisParams}", mybatisParamsCode)

        -- Fill in update fields
        local updateFieldsCode = ""
        for k = 1, #models[i].Fields do
            local field = models[i].Fields[k]
            if k > 1
            then
                updateFieldsCode = updateFieldsCode..", "
            end
            if (field.Primary ~= false) then
                updateFieldsCode = updateFieldsCode..field.Name.." = #{"..field.Name.."}"
            end
        end
        finalSource = finalSource.gsub(finalSource, "${updateFields}", updateFieldsCode)

        files[ModelName.."Mapper.xml"] = finalSource
    end
    return files
end

function generateServices()
    print("Start to generate services...")
    models = project.Models
    files = {}
    for i = 1, #models do
        local modelName = models[i].Name
        local ModelName = modelName:gsub("^%l",string.upper)
        local finalSource = string.gsub(source, "${ModelName}", ModelName)
        finalSource = string.gsub(finalSource, "${modelName}", modelName)
        files[ModelName.."Service.java"] = finalSource
    end
    return files
end

function generateServiceImpls()
    print("Start to generate service impls...")
    models = project.Models
    files = {}
    for i = 1, #models do
        local modelName = models[i].Name
        local ModelName = modelName:gsub("^%l",string.upper)
        local finalSource = string.gsub(source, "${ModelName}", ModelName)
        finalSource = string.gsub(finalSource, "${modelName}", modelName)
        files[ModelName.."ServiceImpl.java"] = finalSource
    end
    return files
end

function generateControllers()
    print("Start to generate controllers...")
    models = project.Models
    files = {}
    for i = 1, #models do
        local modelName = models[i].Name
        local ModelName = modelName:gsub("^%l",string.upper)
        local finalSource = string.gsub(source, "${ModelName}", ModelName)
        finalSource = string.gsub(finalSource, "${modelName}", modelName)
        files[ModelName.."Controller.java"] = finalSource
    end
    return files
end