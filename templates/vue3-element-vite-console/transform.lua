function generateIndexHtml()
    files = {}
    files['index.html'] = string.gsub(source, "${pageTitle}", project.Name)
    return files
end

function generateRouter()
    files = {}
    models = project.Models
    local finalSource = ''
    local imports = ''
    for i = 1, #models do
        local modelName = models[i].Name
        local ModelName = modelName:gsub("^%l",string.upper)
        finalSource = finalSource ..
                "  {\n" ..
                "    path: '/" ..  modelName .. "',\n" ..
                "    name: '" .. ModelName .. "',\n" ..
                "    component: " .. ModelName .. ",\n" ..
                "    meta: {\n" ..
                "      title: '" .. ModelName .. "',\n" ..
                "      activeMenu: '" ..  modelName .. "',\n" ..
                "      icon: 'el-icon-data-analysis',\n" ..
                "    }\n" ..
                "  },\n"
        imports = imports .. "import " .. ModelName .. " from '~/views/" .. ModelName .. ".vue';\n"
    end
    finalSource = string.gsub(source, "${routes}", finalSource)
    finalSource = string.gsub(finalSource, "${modelName}", models[1].Name)
    finalSource = string.gsub(finalSource, "${imports}", imports)
    files['index.ts'] = finalSource
    return files
end

function generateTableViews()
    files = {}
    models = project.Models
    local finalSource = ''
    for i = 1, #models do
        local modelName = models[i].Name
        local ModelName = modelName:gsub("^%l",string.upper)
        finalSource = string.gsub(source, "${modelName}", modelName)
        finalSource = string.gsub(finalSource, "${ModelName}", ModelName)
        local columns = ''
        for k = 1, #models[i].Fields do
            if models[i].Fields[k].Primary ~= true then
                columns = columns .. "    { prop: '" .. models[i].Fields[k].Name .. "' },\n"
            end
        end
        finalSource = string.gsub(finalSource, "${columns}", columns)
        files[ModelName..'.vue'] = finalSource
    end
    
    return files
end