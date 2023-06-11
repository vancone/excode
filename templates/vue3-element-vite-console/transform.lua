function generateIndexHtml()
    files = {}
    files['index.html'] = string.gsub(source, "${pageTitle}", project.Name)
    return files
end

function generateRouter()
    files = {}
    models = project.Models
    local finalSource = ''
    for i = 1, #models do
        local modelName = models[i].Name
        local ModelName = modelName:gsub("^%l",string.upper)
        finalSource = finalSource ..
                "  {\n" ..
                "    path: '/" ..  modelName .. "',\n" ..
                "    name: '" .. ModelName .. "',\n" ..
                "    component: TableView,\n" ..
                "    meta: {\n" ..
                "      title: '" .. ModelName .. "',\n" ..
                "      activeMenu: '" ..  modelName .. "',\n" ..
                "      icon: 'el-icon-data-analysis',\n" ..
                "    }\n" ..
                "  },\n"
    end
    finalSource = string.gsub(source, "${routes}", finalSource)
    files['index.ts'] = finalSource
    return files
end