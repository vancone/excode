/*
    public void buildVueAdminProject() {
        FileUtil.checkDirectory(frontendSourceBaseUrl + "/admin/static");

        // Copy template files
        String[] templateDirectories = {
                "/",
                "/build/",
                "/config/",
                "/src/",
                "/src/components/",
                "/src/router/"
        };

        for (String directory: templateDirectories) {
            File file = new File(System.getProperty("user.dir") + "/templates/vue" + directory);
            File[] files = file.listFiles();
            for (File f: files) {
                if (!f.getName().equals("package.json")) {
                    File outputFile = new File(frontendSourceBaseUrl + "/admin/" + directory + f.getName());
                    try {
                        if (!outputFile.exists()) {
                            Files.copy(f.toPath(), outputFile.toPath());
                        }
                    } catch (IOException e) {
                        LogUtil.error(e.getMessage());
                    }
                }
            }
        }

        // Generate index.html
        TemplateProccesor templateProccesor = new TemplateProccesor(System.getProperty("user.dir") + "/templates/vue/index.html");
        templateProccesor.insert("title", projectModelService.getProject().getArtifactId().capitalizedCamelStyle() + " Admin");
        FileUtil.write(frontendSourceBaseUrl + "/admin/index.html", templateProccesor.toString());

        //new ApiDocumentGenerator(this.project, frontendSourceBaseUrl);
        new PackageJSONGenerator(ProjectLoader.project, frontendSourceBaseUrl + "/admin");
        new RouterJSGennerator(ProjectLoader.project, frontendSourceBaseUrl + "/admin/src/router");
        new AppVueGenrator(ProjectLoader.project, frontendSourceBaseUrl + "/admin/src/App.vue");
        new PanelVueGenerator(ProjectLoader.project, frontendSourceBaseUrl + "/admin/src/components/Panel.vue");
        new DataTablesVueGenerator(ProjectLoader.project, frontendSourceBaseUrl + "/admin/src/components");
    }
}
*/
