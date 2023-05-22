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
        print(models[i].Name)
        files[(models[i].Name:gsub("^%l",string.upper))..".java"] = models[i].Name
    end
    return files
end
