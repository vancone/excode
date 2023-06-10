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

function generateSqlStatements()
    print("Start to generate SQL statements...")
    models = project.Models
    local finalSource = ""
    for i = 1, #models do
        local modelName = models[i].Name
        finalSource = finalSource.."CREATE TABLE IF NOT EXISTS `"..models[i].TablePrefix..modelName.."` (\n"
        for k = 1, #models[i].Fields do
            local field = models[i].Fields[k]

            -- Fill in field name
            finalSource = finalSource.."    `"..field.Name.."` "

            -- Fill in field type
            local type = field.DbType
            if type == "VARCHAR" then
                local length = 255
                if field.Length > 0 then length = field.Length end
                type = type.."("..length..")"
            end
            finalSource = finalSource..type

            -- Fill in primary key / null attributes
            if field.Primary then
                finalSource = finalSource.." PRIMARY KEY NOT NULL"
                if field.AutoIncrement then
                    finalSource = finalSource.." AUTO_INCREMENT"
                end
            elseif field.NotNull then
                finalSource = finalSource.." NOT NULL"
            end

            -- Line break
            if k == #models[i].Fields then
                finalSource = finalSource.."\n"
            else
                finalSource = finalSource..",\n"
            end
        end
        finalSource = finalSource..");\n\n"
    end
    files = {}
    files[project.Name..".sql"] = finalSource
    return files
end

function generateCrossOriginConfig()
    print("Start to generate cross origin config...")
    models = project.Models
    files = {}
    local finalSource = source
    for i = 1, #template.Properties do
        if template.Properties[i].Name == "cross-origin.allowed-headers" then
            local value = template.Properties[i].Value
            if value == "*" then
                finalSource = string.gsub(finalSource, "${addAllowedHeaders}", "corsConfiguration.addAllowedHeader(\"*\");")
            else
                -- TODO: split function should be used here
                local headers = string.sub(",", 1)
                local tmp = ""
                for k = 1, #headers do
                    tmp = tmp.."\n    corsConfiguration.addAllowedHeader(\""..headers[k].."\");"
                end
                finalSource = string.gsub(finalSource, "${addAllowedHeaders}", tmp)
            end
        end
    end
    -- Remove redundant params
    finalSource = string.gsub(finalSource, "${addAllowedHeaders}", "")
    files["CrossOriginConfig.java"] = finalSource
    return files
end

function generateServiceTests()
    print("Start to generate service tests...")
    models = project.Models
    files = {}
    for i = 1, #models do
        local modelName = models[i].Name
        local ModelName = modelName:gsub("^%l",string.upper)
        local finalSource = string.gsub(source, "${ModelName}", ModelName)
        finalSource = string.gsub(finalSource, "${modelName}", modelName)
        files[ModelName.."ServiceTest.java"] = finalSource
    end
    return files
end

function generatePostmanCollection()
    local finalSource = '{\n'..
        '    "info": {\n'..
        '        "_postman_id": "6677f9a2-0725-4a62-8472-221789071ee9",\n'..
        '        "name": "'..project.Name..'",\n'..
        '        "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"\n'..
        '    },\n'..
        '    "item": [\n${items}    ]\n'..
        '}'
    
    models = project.Models
    local itemSource = ''
    for i = 1, #models do
        local modelName = models[i].Name
        local ModelName = modelName:gsub("^%l",string.upper)
        -- local finalSource = string.gsub(source, "${ModelName}", ModelName)
        itemSource = itemSource..'        {\n'..
                                '            "name": "'..ModelName..'",\n'..
                                '            "item": [${subItems}]\n'..
                                '        }'
        if i == #models then
            itemSource = itemSource..'\n'
        else
            itemSource = itemSource..',\n'
        end
        local subItemSource = ''
        local requestUrl = 'http://localhost:'..properties['server.port']..'/api/'..properties['project.artifactId']..'/'..modelName
        local paths = '"api", "'..properties['project.artifactId']..'", "'..modelName..'"'

        -- GET
        subItemSource = subItemSource..'\n'..source..','
        subItemSource = string.gsub(subItemSource, "${requestName}", 'Query '..ModelName)
        subItemSource = string.gsub(subItemSource, "${requestMethod}", 'GET')
        subItemSource = string.gsub(subItemSource, '${query}', '{ "key": "pageSize", "value": "5" }, { "key": "pageNo", "value": "1" }')
        subItemSource = string.gsub(subItemSource, '${requestBody}', '')
        subItemSource = string.gsub(subItemSource, "${requestUrl}", requestUrl)
        subItemSource = string.gsub(subItemSource, "${paths}", paths)

        -- POST
        subItemSource = subItemSource..'\n'..source..','
        subItemSource = string.gsub(subItemSource, "${requestName}", 'Create '..ModelName)
        subItemSource = string.gsub(subItemSource, "${requestMethod}", 'POST')
        subItemSource = string.gsub(subItemSource, '${query}', '')
        subItemSource = string.gsub(subItemSource, '${requestBody}', '{\\"id\\": 1, \\"name\\": \\"Tom\\"}')
        subItemSource = string.gsub(subItemSource, "${requestUrl}", requestUrl)
        subItemSource = string.gsub(subItemSource, "${paths}", paths)

        -- PUT
        subItemSource = subItemSource..'\n'..source..','
        subItemSource = string.gsub(subItemSource, "${requestName}", 'Update '..ModelName)
        subItemSource = string.gsub(subItemSource, "${requestMethod}", 'PUT')
        subItemSource = string.gsub(subItemSource, '${query}', '')
        subItemSource = string.gsub(subItemSource, '${requestBody}', '')
        subItemSource = string.gsub(subItemSource, "${requestUrl}", requestUrl)
        subItemSource = string.gsub(subItemSource, "${paths}", paths)

        -- DELETE
        subItemSource = subItemSource..'\n'..source
        subItemSource = string.gsub(subItemSource, "${requestName}", 'Delete '..ModelName)
        subItemSource = string.gsub(subItemSource, "${requestMethod}", 'DELETE')
        subItemSource = string.gsub(subItemSource, '${query}', '')
        subItemSource = string.gsub(subItemSource, '${requestBody}', '')
        subItemSource = string.gsub(subItemSource, "${requestUrl}", requestUrl..'/1')
        subItemSource = string.gsub(subItemSource, "${paths}", paths..', "1"')

        subItemSource = string.gsub(subItemSource, "${requestHost}", 'localhost')
        subItemSource = string.gsub(subItemSource, "${port}", properties['server.port'])
        itemSource = string.gsub(itemSource, "${subItems}", subItemSource)
    end

    finalSource = string.gsub(finalSource, "${items}", itemSource)
    files = {}
    files[project.Name..".postman_collection.json"] = finalSource
    return files
end