function getModelName(model)
    local modelName = model.Name
    local ModelName = modelName:gsub('^%l',string.upper)
    return modelName, ModelName
end

function generatePom()
    local files = {}
    local otherDependencies = ''
    local finalSource = source

    local redisInjected = false
    for i = 1, #(project.Deployment.Env) do
        local env = project.Deployment.Env[i]
        -- Add Redis dependency
        if #(project.Deployment.Env[1].Middleware.Redis) > 0 and redisInjected == false then
            otherDependencies = otherDependencies .. '\n\n' ..
                go_add_indent(go_read_template_file(template.Name, 'content/pom/redis.xml'), 8)
            redisInjected = true
        end
    end

    if plugins['vancone-passport-sdk'] == true then
        otherDependencies = otherDependencies .. '\n\n' ..
            go_add_indent(go_read_template_file(template.Name, 'content/pom/vancone-passport-sdk.xml'), 8)
    end

    if plugins['swagger'] == true then
        otherDependencies = otherDependencies .. '\n\n' ..
            go_add_indent(go_read_template_file(template.Name, 'content/pom/swagger.xml'), 8)
    end

    finalSource = string.gsub(finalSource, '${otherDependencies}', otherDependencies)
    files['pom.xml'] = finalSource
    return files
end

function generateEntities()
    local models = project.Models
    local files = {}
    for i = 1, #models do
        local modelName, ModelName = getModelName(models[i])
        local finalSource = string.gsub(source, '${modelName}', ModelName)
        local fieldCode = ''
        local customImports = ''
        local stdImports = ''
        for k = 1, #models[i].Fields do
            local field = models[i].Fields[k]
            local type = 'String'
            if field.Type == 'number' then
                type = 'Long'
            elseif field.Type == 'date' then
                type = 'LocalDate'
                stdImports = stdImports .. 'import java.time.LocalDate;\n'
            elseif field.Type == 'datetime' then
                type = 'LocalDateTime'
            else
                if string.find(field.Type, 'enum:') == 1 then
                    type = string.sub(field.Type, 6):gsub('^%l',string.upper) .. 'Enum'
                    customImports = customImports .. 'import ' .. properties['project.groupId'] .. '.' .. properties['project.artifactId'] .. '.enums.' .. string.sub(field.Type, 6):gsub('^%l',string.upper) .. 'Enum;\n'
                end
            end
            fieldCode = fieldCode .. 'private ' .. type .. ' ' .. field.Name .. ';\n'
        end
        finalSource = finalSource.gsub(finalSource, '${fields}', go_add_indent(fieldCode, 4))
        finalSource = finalSource.gsub(finalSource, '${customImports}', customImports)
        finalSource = finalSource.gsub(finalSource, '${stdImports}', stdImports)
        files[ModelName .. '.java'] = finalSource
    end
    return files
end

function generateMappers()
    local models = project.Models
    local files = {}
    for i = 1, #models do
        local modelName, ModelName = getModelName(models[i])
        local finalSource = string.gsub(source, '${ModelName}', ModelName)
        finalSource = string.gsub(finalSource, '${modelName}', modelName)
        if models[i].Mappings ~= nil then
            local mappingCodes = ''
            local otherEntityImportCodes = ''
            for k = 1, #models[i].Mappings do
                local mapping = models[i].Mappings[k]
                if mapping.Type == 'many2many' then
                    otherEntityImportCodes = 'import ' .. properties['project.groupId'] .. '.' .. properties['project.artifactId'] .. '.entity.' .. mapping.Model:gsub('^%l',string.upper) .. ';'

                    --POST
                    mappingCodes = mappingCodes .. '    boolean add' .. mapping.Model:gsub('^%l',string.upper) ..
                        '(String ' .. models[i].name .. 'Id, String ' .. mapping.Model .. 'Id);\n'
                    
                    -- GET
                    mappingCodes = mappingCodes .. '    List<' .. mapping.Model:gsub('^%l',string.upper) .. '> query' .. mapping.Model:gsub('^%l',string.upper) .. 'sBy' .. ModelName .. 'Id' ..
                        '(String ' .. models[i].name .. 'Id);\n'

                    -- DELETE
                    mappingCodes = mappingCodes .. '    void remove' .. mapping.Model:gsub('^%l',string.upper) ..
                        '(String ' .. models[i].name .. 'Id, String ' .. mapping.Model .. 'Id);\n'
                end
            end
            finalSource = string.gsub(finalSource, '${otherMethods}', mappingCodes)
            finalSource = string.gsub(finalSource, '${otherEntityImports}', otherEntityImportCodes)
        end
        
        finalSource = string.gsub(finalSource, '${otherMethods}', '')
        finalSource = string.gsub(finalSource, '${otherEntityImports}', '')
        files[ModelName .. 'Mapper.java'] = finalSource
    end
    return files
end

function generateMapperXmlFiles()
    local models = project.Models
    local files = {}
    for i = 1, #models do
        local modelName, ModelName = getModelName(models[i])
        local finalSource = string.gsub(source, '${ModelName}', ModelName)
        finalSource = string.gsub(finalSource, '${modelName}', modelName)
        finalSource = string.gsub(finalSource, '${tableName}', models[i].TablePrefix .. modelName)

        -- Fill in result map fields
        local resultMapFieldsCode = ''
        for k = 1, #models[i].Fields do
            local field = models[i].Fields[k]
            if field.Primary then
                resultMapFieldsCode = resultMapFieldsCode .. '<id column="' .. field.Name .. '" property="' .. field.Name .. '" jdbcType="VARCHAR"/>\n'
            else
                if string.find(field.Type, 'enum:') == 1 then
                    resultMapFieldsCode = resultMapFieldsCode .. '<result column="' .. field.Name .. '" property="' .. field.Name .. '" jdbcType="VARCHAR" typeHandler="org.apache.ibatis.type.EnumOrdinalTypeHandler" />\n'
                else
                    resultMapFieldsCode = resultMapFieldsCode .. '<result column="' .. field.Name .. '" property="' .. field.Name .. '" jdbcType="VARCHAR"/>\n'
                end
            end
        end
        resultMapFieldsCode = resultMapFieldsCode .. '<result column="created_time" property="createdTime" jdbcType="TIMESTAMP"/>\n'
        resultMapFieldsCode = resultMapFieldsCode .. '<result column="updated_time" property="updatedTime" jdbcType="TIMESTAMP"/>'
        finalSource = finalSource.gsub(finalSource, '${resultMapFields}', go_add_indent(resultMapFieldsCode, 8))

        -- Fill in query params and MyBatis params
        local queryParamsCode = ''
        local insertParamsCode = ''
        local mybatisParamsCode = ''
        local isFirstInsertField = true
        for k = 1, #models[i].Fields do
            local field = models[i].Fields[k]
            if k > 1 then
                queryParamsCode = queryParamsCode .. ', '
            end

            queryParamsCode = queryParamsCode .. '`' .. field.Name .. '`'
            if field.Primary ~= true then
                if isFirstInsertField == true then
                    isFirstInsertField = false
                else
                    insertParamsCode = insertParamsCode .. ', '
                    mybatisParamsCode = mybatisParamsCode .. ', '
                end
                insertParamsCode = insertParamsCode .. '`' .. field.Name .. '`'
                if string.find(field.Type, 'enum:') == 1 then
                    mybatisParamsCode = mybatisParamsCode .. '#{' .. field.Name .. ', jdbcType=' .. field.DbType .. ', javaType=' .. properties['project.groupId'] .. '.' .. properties['project.artifactId'] .. '.enums.' .. string.sub(field.Type, 6):gsub('^%l',string.upper) .. 'Enum, typeHandler=org.apache.ibatis.type.EnumOrdinalTypeHandler}'
                else
                    mybatisParamsCode = mybatisParamsCode .. '#{' .. field.Name .. '}'
                end
                
            end
        end
        queryParamsCode = queryParamsCode .. ', `created_time`, `updated_time`'
        finalSource = finalSource.gsub(finalSource, '${queryParams}', queryParamsCode)
        finalSource = finalSource.gsub(finalSource, '${insertParams}', insertParamsCode)
        finalSource = finalSource.gsub(finalSource, '${mybatisParams}', mybatisParamsCode)

        -- Fill in update fields
        local updateFieldsCode = ''
        local isFirstField = true
        for k = 1, #models[i].Fields do
            local field = models[i].Fields[k]
            if (field.Primary == false) then
                if isFirstField then
                    isFirstField = false
                else
                    updateFieldsCode = updateFieldsCode .. ', '
                end
                if string.find(field.Type, 'enum:') == 1 then
                    updateFieldsCode = updateFieldsCode .. field.Name .. ' = #{' .. field.Name .. ', jdbcType=' .. field.DbType .. ', javaType=' .. properties['project.groupId'] .. '.' .. properties['project.artifactId'] .. '.enums.' .. string.sub(field.Type, 6):gsub('^%l',string.upper) .. 'Enum, typeHandler=org.apache.ibatis.type.EnumOrdinalTypeHandler}'
                else
                    updateFieldsCode = updateFieldsCode .. field.Name .. ' = #{' .. field.Name .. '}'
                end
            end
        end
        finalSource = finalSource.gsub(finalSource, '${updateFields}', updateFieldsCode)

        local deleteMethod = '    <delete id="delete">\n' ..
                            '        DELETE FROM ' .. models[i].Name .. ' WHERE id = #{id}\n' ..
                            '    </delete>'
        if models[i].LogicDelete == true then
            deleteMethod = '    <update id="delete">\n' ..
                            '        UPDATE ' .. models[i].Name .. ' SET deleted = 1 WHERE id = #{id} AND deleted = 0\n' ..
                            '    </update>\n'
        end
        finalSource = finalSource.gsub(finalSource, '${deleteMethod}', deleteMethod)

        if models[i].Mappings ~= nil then
            local mappingCodes = ''
            for k = 1, #models[i].Mappings do
                local mapping = models[i].Mappings[k]
                if mapping.Type == 'many2many' then

                    -- POST
                    mappingCodes = mappingCodes .. '    <insert id="add' .. mapping.Model:gsub('^%l',string.upper) .. '">\n' ..
                        '        INSERT INTO ' .. models[i].TablePrefix .. models[i].name .. '_' .. mapping.Model .. ' (`' .. models[i].name .. '_id`, `' .. mapping.Model .. '_id`)\n' ..
                        '        VALUES (#{'.. models[i].name .. 'Id}, #{' .. mapping.Model .. 'Id});\n' ..
                        '    </insert>\n\n'
                    
                    -- GET
                    mappingCodes = mappingCodes .. '    <select id="query' .. mapping.Model:gsub('^%l',string.upper) .. 'sBy' .. ModelName .. 'Id" resultType="' .. properties['project.groupId'] .. '.' .. properties['project.artifactId'] .. '.entity.' .. mapping.Model:gsub('^%l',string.upper) .. '">\n' ..
                        '        SELECT * FROM ' .. models[i].TablePrefix .. mapping.Model .. ' WHERE id IN (SELECT ' .. mapping.Model .. '_id FROM ' .. models[i].TablePrefix .. models[i].name .. '_' .. mapping.Model .. ' WHERE ' .. modelName .. '_id = #{' .. modelName .. 'Id})\n' ..
                        '    </select>\n\n'

                    -- DELETE
                    mappingCodes = mappingCodes .. '    <delete id="remove' .. mapping.Model:gsub('^%l',string.upper) .. '">\n' ..
                        '        DELETE FROM ' .. models[i].TablePrefix .. models[i].name .. '_' .. mapping.Model .. ' WHERE ' .. modelName .. '_id = #{' .. modelName .. 'Id} AND ' .. mapping.Model ..'_id = #{' .. mapping.Model .. 'Id};\n' ..
                        '    </delete>'
                
                end
            end
            finalSource = string.gsub(finalSource, '${otherMethods}', mappingCodes)
        end

        finalSource = string.gsub(finalSource, '${otherMethods}', '')
        files[ModelName .. 'Mapper.xml'] = finalSource
    end
    return files
end

function generateServices()
    local models = project.Models
    local files = {}
    for i = 1, #models do
        local modelName, ModelName = getModelName(models[i])
        local finalSource = string.gsub(source, '${ModelName}', ModelName)
        finalSource = string.gsub(finalSource, '${modelName}', modelName)

        if models[i].Mappings ~= nil then
            local mappingCodes = ''
            local otherEntityImportCodes = ''
            for k = 1, #models[i].Mappings do
                local mapping = models[i].Mappings[k]
                if mapping.Type == 'many2many' then
                    otherEntityImportCodes = 'import ' .. properties['project.groupId'] .. '.' .. properties['project.artifactId'] .. '.entity.' .. mapping.Model:gsub('^%l',string.upper) .. ';'

                    -- POST
                    mappingCodes = mappingCodes .. '    void add' .. mapping.Model:gsub('^%l',string.upper) ..
                        '(String ' .. models[i].name .. 'Id, String ' .. mapping.Model .. 'Id);\n'

                    -- GET
                    mappingCodes = mappingCodes .. '    ResponsePage<' .. mapping.Model:gsub('^%l',string.upper) .. '> query' .. mapping.Model:gsub('^%l',string.upper) .. 'sBy' .. ModelName .. 'Id' ..
                        '(String ' .. models[i].name .. 'Id, int pageNo, int pageSize);\n'

                    -- DELETE
                    mappingCodes = mappingCodes .. '    void remove' .. mapping.Model:gsub('^%l',string.upper) ..
                        '(String ' .. models[i].name .. 'Id, String ' .. mapping.Model .. 'Id);\n'
                end
            end
            
            finalSource = string.gsub(finalSource, '${otherEntityImports}', otherEntityImportCodes)
            finalSource = string.gsub(finalSource, '${otherMethods}', mappingCodes)
        end
        
        finalSource = string.gsub(finalSource, '${otherMethods}', '')
        finalSource = string.gsub(finalSource, '${otherEntityImports}\r\n', '')
        files[ModelName .. 'Service.java'] = finalSource
    end
    return files
end

function generateServiceImpls()
    local models = project.Models
    local files = {}
    for i = 1, #models do
        local modelName, ModelName = getModelName(models[i])
        local finalSource = string.gsub(source, '${ModelName}', ModelName)
        finalSource = string.gsub(finalSource, '${modelName}', modelName)
        local otherEntityImportCodes = ''
        if models[i].Mappings ~= nil then
            local mappingCodes = ''
            for k = 1, #models[i].Mappings do
                local mapping = models[i].Mappings[k]
                if mapping.Type == 'many2many' then
                    otherEntityImportCodes = 'import ' .. properties['project.groupId'] .. '.' .. properties['project.artifactId'] .. '.entity.' .. mapping.Model:gsub('^%l',string.upper) .. ';'

                    -- POST
                    mappingCodes = mappingCodes .. '    @Override\n' ..
                        '    public void add' .. mapping.Model:gsub('^%l',string.upper) ..
                        '(String ' .. models[i].name .. 'Id, String ' .. mapping.Model .. 'Id) {\n' ..
                        '        ' .. modelName .. 'Mapper.add' .. mapping.Model:gsub('^%l',string.upper) ..'(' .. models[i].name .. 'Id, ' .. mapping.Model ..'Id);\n' ..
                        '    }\n'
                    
                    -- GET
                    mappingCodes = mappingCodes .. '    @Override\n' ..
                        '    public ResponsePage<' .. mapping.Model:gsub('^%l',string.upper) .. '> query' .. mapping.Model:gsub('^%l',string.upper) .. 'sBy' .. ModelName .. 'Id' ..
                        '(String ' .. models[i].name .. 'Id, int pageNo, int pageSize) {\n' ..
                        '        PageHelper.startPage(pageNo, pageSize);\n' ..
                        '        return new ResponsePage<>(new PageInfo<>(' .. modelName .. 'Mapper.query' .. mapping.Model:gsub('^%l',string.upper) ..'sBy' .. ModelName .. 'Id(' .. models[i].name .. 'Id)));\n' ..
                        '    }\n'
                    
                    -- DELETE
                    mappingCodes = mappingCodes .. '    @Override\n' ..
                    '    public void remove' .. mapping.Model:gsub('^%l',string.upper) ..
                    '(String ' .. models[i].name .. 'Id, String ' .. mapping.Model .. 'Id) {\n' ..
                    '        ' .. modelName .. 'Mapper.remove' .. mapping.Model:gsub('^%l',string.upper) ..'(' .. models[i].name .. 'Id, ' .. mapping.Model ..'Id);\n' ..
                    '    }\n'
                    
                    finalSource = string.gsub(finalSource, '${otherEntityImports}', otherEntityImportCodes)
                end
            end

            finalSource = string.gsub(finalSource, '${otherMethods}', mappingCodes)
        end
        finalSource = string.gsub(finalSource, '${otherEntityImports}\r\n', '')
        finalSource = string.gsub(finalSource, '${otherMethods}', '')
        files[ModelName .. 'ServiceImpl.java'] = finalSource
    end
    return files
end

function generateControllers()
    local models = project.Models
    local files = {}
    for i = 1, #models do
        local modelName, ModelName = getModelName(models[i])
        local finalSource = string.gsub(source, '${ModelName}', ModelName)
        finalSource = string.gsub(finalSource, '${modelName}', modelName)

        if models[i].Mappings ~= nil then
            local mappingCodes = ''
            for k = 1, #models[i].Mappings do
                local mapping = models[i].Mappings[k]
                if mapping.Type == 'many2many' then
                    -- POST
                    mappingCodes = mappingCodes .. '    @PostMapping("/api/' .. properties['project.artifactId'] .. '/' .. modelName .. '/{' .. modelName .. 'Id}/' .. mapping.Model .. '/{' .. mapping.Model .. 'Id}")\n' ..
                        '    public Response add' .. mapping.Model:gsub('^%l',string.upper) ..
                        '(@PathVariable String ' .. models[i].name .. 'Id, @PathVariable String ' .. mapping.Model .. 'Id) {\n' ..
                        '        ' .. modelName .. 'Service.add' .. mapping.Model:gsub('^%l',string.upper) ..'(' .. models[i].name .. 'Id, ' .. mapping.Model ..'Id);\n' ..
                        '        return Response.success();\n' ..
                        '    }\n\n'
                    
                    -- GET
                    mappingCodes = mappingCodes .. '    @GetMapping("/api/' .. properties['project.artifactId'] .. '/' .. modelName .. '/{' .. modelName .. 'Id}/' .. mapping.Model .. '")\n' ..
                        '    public Response query' .. mapping.Model:gsub('^%l',string.upper) .. 'sBy' .. ModelName .. 'Id' ..
                        '(@PathVariable String ' .. models[i].name .. 'Id,\n' ..
                        '                                             @RequestParam(defaultValue = "1") int pageNo,\n' ..
                        '                                             @RequestParam(defaultValue = "5") int pageSize) {\n' ..
                        '        return Response.success(' .. modelName .. 'Service.query' .. mapping.Model:gsub('^%l',string.upper) .. 'sBy' .. ModelName ..'Id(' .. models[i].name .. 'Id, pageNo, pageSize));\n' ..
                        '    }\n'
                    
                    -- DELETE
                    mappingCodes = mappingCodes .. '    @DeleteMapping("/api/' .. properties['project.artifactId'] .. '/' .. modelName .. '/{' .. modelName .. 'Id}/' .. mapping.Model .. '/{' .. mapping.Model .. 'Id}")\n' ..
                        '    public Response remove' .. mapping.Model:gsub('^%l',string.upper) ..
                        '(@PathVariable String ' .. models[i].name .. 'Id, @PathVariable String ' .. mapping.Model .. 'Id) {\n' ..
                        '        ' .. modelName .. 'Service.remove' .. mapping.Model:gsub('^%l',string.upper) ..'(' .. models[i].name .. 'Id, ' .. mapping.Model ..'Id);\n' ..
                        '        return Response.success();\n' ..
                        '    }\n\n'
                end
            end
            finalSource = string.gsub(finalSource, '${otherMethods}', mappingCodes)
        end
        
        finalSource = string.gsub(finalSource, '${otherMethods}', '')

        files[ModelName .. 'Controller.java'] = finalSource
    end
    return files
end

function generateSqlStatements()
    local models = project.Models
    local finalSource = ''
    local bridgeTables = {}
    for i = 1, #models do
        local modelName = models[i].Name
        finalSource = finalSource .. '-- ' .. models[i].TablePrefix .. modelName .. '\n'
        finalSource = finalSource .. 'DROP TABLE IF EXISTS `' .. models[i].TablePrefix .. modelName .. '`;\n'
        finalSource = finalSource .. 'CREATE TABLE IF NOT EXISTS `' .. models[i].TablePrefix .. modelName .. '` (\n'
        for k = 1, #models[i].Fields do
            local field = models[i].Fields[k]

            -- Fill in field name
            finalSource = finalSource .. '    `' .. field.Name .. '` '

            -- Fill in field type
            local type = field.DbType
            if type == 'VARCHAR' then
                local length = 255
                if field.Length > 0 then length = field.Length end
                type = type .. '(' .. length .. ')'
            end
            finalSource = finalSource .. type

            -- Fill in primary key / null attributes
            if field.Primary then
                finalSource = finalSource .. ' PRIMARY KEY NOT NULL'
                if field.AutoIncrement then
                    finalSource = finalSource .. ' AUTO_INCREMENT'
                end
            elseif field.NotNull then
                finalSource = finalSource .. ' NOT NULL'
            end
            finalSource = finalSource .. ',\n'
        end
        finalSource = finalSource .. '    `created_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n'
        finalSource = finalSource .. '    `updated_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP'
        if models[i].LogicDelete == true then
            finalSource = finalSource .. ',\n    `deleted` BIT DEFAULT 0'
        end
        finalSource = finalSource .. '\n);\n\n'

        if models[i].Mappings ~= nil then
            for k = 1, #models[i].Mappings do
                local mapping = models[i].Mappings[k]
                if mapping.Type == 'many2many' then
                    local tableName = models[i].TablePrefix .. modelName .. '_' .. mapping.Model
                    finalSource = finalSource .. '-- ' .. tableName .. '\n' ..
                            'DROP TABLE IF EXISTS `' .. tableName .. '`;\n' ..
                            'CREATE TABLE IF NOT EXISTS `' .. tableName .. '` (\n' ..
                            '    `' .. modelName .. '_id` BIGINT NOT NULL,\n' ..
                            '    `' .. mapping.Model .. '_id` BIGINT NOT NULL,\n' ..
                            '    `created_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n' ..
                            '    PRIMARY KEY (' .. modelName .. '_id, ' .. mapping.Model .. '_id)\n' ..
                            ');\n\n'
                end
            end
        end
    end
    local files = {}
    files[project.Name .. '.sql'] = finalSource
    return files
end

function generateConfig()
    local files = {}
    if plugins['swagger'] == true then
        content = go_read_template_file(template.Name, 'content/config/SwaggerConfig.java')
        content = string.gsub(content, '${template.properties.project.groupId}', properties['project.groupId'])
        content = string.gsub(content, '${template.properties.project.artifactId}', properties['project.artifactId'])
        content = string.gsub(content, '${project.version}', project.Version)

        local title = project.Name
        if properties['swagger.title'] ~= nil then
            title = properties['swagger.title']
        end

        local description = 'Standard API Document Powered by Swagger'
        if properties['swagger.description'] ~= nil then
            description = properties['swagger.description']
        end

        content = string.gsub(content, '${swagger.title}', title)
        content = string.gsub(content, '${swagger.description}', description)

        files['SwaggerConfig.java'] = content
    end
    return files
end

function generateCrossOriginConfig()
    models = project.Models
    local files = {}
    local finalSource = source
    for i = 1, #template.Properties do
        if template.Properties[i].Name == 'cross-origin.allowed-headers' then
            local value = template.Properties[i].Value
            if value == '*' then
                finalSource = string.gsub(finalSource, '${addAllowedHeaders}', 'corsConfiguration.addAllowedHeader("*");')
            else
                -- TODO: split function should be used here
                local headers = string.sub(',', 1)
                local tmp = ''
                for k = 1, #headers do
                    tmp = tmp .. '\n    corsConfiguration.addAllowedHeader("' .. headers[k] .. '");'
                end
                finalSource = string.gsub(finalSource, '${addAllowedHeaders}', tmp)
            end
        end
    end
    -- Remove redundant params
    finalSource = string.gsub(finalSource, '${addAllowedHeaders}', '')
    files['CrossOriginConfig.java'] = finalSource
    return files
end

function generateServiceTests()
    local models = project.Models
    local files = {}
    for i = 1, #models do
        local modelName, ModelName = getModelName(models[i])
        local finalSource = string.gsub(source, '${ModelName}', ModelName)
        finalSource = string.gsub(finalSource, '${modelName}', modelName)
        files[ModelName .. 'ServiceTest.java'] = finalSource
    end
    return files
end

function generatePostmanCollection()
    local finalSource = go_read_template_file(template.Name, 'content/PostmanCollectionOuter.json')
    local models = project.Models
    local itemSource = ''
    for i = 1, #models do
        local modelName, ModelName = getModelName(models[i])
        itemSource = itemSource .. '{"name": "' .. ModelName .. '", "item": [${subItems}]}' .. (i ~= #models and ',' or '')
        local subItemSource = ''
        local requestUrl = 'http://localhost:' .. properties['server.port'] .. '/api/' .. properties['project.artifactId'] .. '/' .. modelName
        local paths = '"api", "' .. properties['project.artifactId'] .. '", "' .. modelName .. '"'
        local paginationParams = '{ "key": "pageSize", "value": "5" }, { "key": "pageNo", "value": "1" }'
        local requestBody = '{"id": 1, "name": "Tom"}'
        local requestTables = {
            { name = 'Query ' .. ModelName, method = 'GET', query = paginationParams, body = '', url = requestUrl, path = paths },
            { name = 'Create ' .. ModelName, method = 'POST', query = '', body = requestBody, url = requestUrl, path = paths },
            { name = 'Update ' .. ModelName, method = 'PUT', query = '', body = requestBody, url = requestUrl, path = paths },
            { name = 'Delete ' .. ModelName, method = 'DELETE', query = '', body = '', url = requestUrl .. '/1', path = paths .. ', "1"' }
        }

        for k = 1, #requestTables do
            local table = requestTables[k]
            subItemSource = subItemSource .. source .. ','
            subItemSource = string.gsub(subItemSource, '${requestName}', table['name'])
            subItemSource = string.gsub(subItemSource, '${requestMethod}', table['method'])
            subItemSource = string.gsub(subItemSource, '${query}', table['query'])
            subItemSource = string.gsub(subItemSource, '${requestBody}', table['body'])
            subItemSource = string.gsub(subItemSource, '${requestUrl}', table['url'])
            subItemSource = string.gsub(subItemSource, '${paths}', table['path'])
        end

        subItemSource = string.gsub(subItemSource, '${requestHost}', 'localhost')
        subItemSource = string.gsub(subItemSource, '${port}', properties['server.port'])
        itemSource = string.gsub(itemSource, '${subItems}', subItemSource)
    end

    finalSource = string.gsub(finalSource, '${items}', itemSource)
    local files = {}
    files[project.Name .. '.postman_collection.json'] = go_json_format(finalSource)
    return files
end

function generateProperties()
    local files = {}

    for i = 1, #(project.Deployment.Env) do
        local env = project.Deployment.Env[i]
        local finalSource = ''

        -- Add MySQL properties
        if env.Middleware.Mysql ~= nil and #(env.Middleware.Mysql) > 0 then
            local mysql = env.Middleware.Mysql[1]
            local mysqlProperties = go_read_template_file(template.Name, 'content/properties/mysql.properties', mysql)
            mysqlProperties = string.gsub(mysqlProperties, '${mysql.EncryptedPassword}', go_jasypt_encrypt(mysql.Password))
            finalSource = finalSource .. mysqlProperties .. '\n\n'
        end

        -- Add Redis properties
        if env.Middleware.Redis ~= nil and #(env.Middleware.Redis) > 0 then
            local redis = env.Middleware.Redis[1]
            local redisProperties = go_read_template_file(template.Name, 'content/properties/redis.properties', redis)
            redisProperties = string.gsub(redisProperties, '${redis.EncryptedPassword}', go_jasypt_encrypt(redis.Password))
            finalSource = finalSource .. redisProperties .. '\n\n'
        end

        -- Add Passport SDK properties
        if plugins['vancone-passport-sdk'] == true then
            local baseUrl = (env.Profile == 'pro' or env.Profile == 'prod') and 'https://passport.vancone.com' or 'http://passport.beta.vancone.com'
            local passportSdkProperties = go_read_template_file(template.Name, 'content/properties/vancone-passport-sdk.properties')
            passportSdkProperties = string.gsub(passportSdkProperties, '${passport.baseUrl}', baseUrl)
            passportSdkProperties = string.gsub(passportSdkProperties, '${passport.accessKeyId}', go_jasypt_encrypt(properties['vancone.passport.service-account.access-key-id']))
            passportSdkProperties = string.gsub(passportSdkProperties, '${passport.secretAccessKey}', go_jasypt_encrypt(properties['vancone.passport.service-account.secret-access-key']))
            finalSource = finalSource .. passportSdkProperties .. '\n\n'
        end

        -- Add Passport SDK properties
        if plugins['swagger'] == true then
            finalSource = finalSource .. 'spring.mvc.pathmatch.matching-strategy=ant_path_matcher\n'..
                            'vancone.passport.permitUriPrefix=/swagger-ui,/swagger-resources,/v3/api-docs\n\n'
        end

        files['application-' .. env.Profile .. '.properties'] = finalSource
    end

    return files
end

function generateEnums()
    local files = {}
    if project.Enums ~= nil then
        for i = 1, #project.Enums do
            local enum = project.Enums[i]
            local finalSource = source
            finalSource = string.gsub(finalSource, '${enumName}', enum.Name:gsub('^%l',string.upper))
            local options = ''
            for k = 1, #enum.Options do
                options = options .. enum.Options[k].Name .. ', '
            end
            finalSource = string.gsub(finalSource, '${options}', options)
            files[enum.Name:gsub('^%l',string.upper) .. 'Enum.java'] = finalSource
        end
    end
    return files
end

function generateNginxConf()
    local files = {}
    local port = '8080'
    if properties['server.port'] ~= '' then
        port = properties['server.port']
    end
    files[properties['project.artifactId'] .. '.conf'] = string.gsub(source, '${port}', port)
    return files
end