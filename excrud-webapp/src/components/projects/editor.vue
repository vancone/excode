<template>
  <div class="editor-container">
    <el-row style="height:100%">
       <el-col :span="4" style="height:100%;min-width:180px">
        <div class="second-left-pane">
          <el-menu
            style="background:transparent;border-right:none;width:100%"
            default-active="projects"
            :router=true
            @open="handleOpen"
            @close="handleClose"
          >
            <el-menu-item index="/projects">
              <i class="el-icon-menu"></i>
              <span slot="title">mod::deployment</span>
            </el-menu-item>
            <el-menu-item index="/hosts">
              <i class="el-icon-menu"></i>
              <span slot="title">mod::spring-boot</span>
            </el-menu-item>
            <el-menu-item index="3">
              <i class="el-icon-menu"></i>
              <span slot="title">mod::vue-element-admin</span>
            </el-menu-item>
            <el-menu-item index="/settings">
              <i class="el-icon-menu"></i>
              <span slot="title">mod::website-template</span>
            </el-menu-item>
          </el-menu>
          <div style="position:fixed;left: 0px;bottom: 0px">
            <p class="copyright">v0.0.1</p>
          </div>
        </div>
      </el-col>
      <el-col :span="20">
        <h2 class="projects-title">Projects2</h2>
        <el-table :data="tableData" class="project-table">
          <el-table-column prop="date" label="Project name"></el-table-column>
          <el-table-column prop="name" label="Creator" width="200"></el-table-column>
          <el-table-column prop="address" label="Modified time" width="200"></el-table-column>
        </el-table>
      </el-col>

    </el-row>
  </div>
</template>

<script>
export default {
  name: 'EditorPage',
  data () {
    return {
      project: {
        _id: { $oid: '5ee241f858797a338da4f3fa' },
        languages: ['en-US', 'zh-CN'],
        updatedTime: '2020-06-11 22:38:48',
        groupId: 'com.mekcone',
        artifactId: 'mall',
        version: '0.0.1',
        name: {
          defaultValue: 'MekCone Mall',
          words: [
            { lang: 'zh-CN', value: '锥云商城' },
            { lang: 'zh-TW', value: '錐雲商城' },
            { lang: 'jp', value: 'MekCone デパート' },
            { lang: 'th', value: 'MekCone มอลล์' }
          ]
        },
        description: {
          defaultValue: 'MekCone Mall Project',
          words: [{ lang: 'zh-CN', value: '锥云商城后台项目' }]
        },
        moduleSet: {
          apiDocumentModule: {
            use: true,
            keywords: [
              { type: 'create', value: '创建' },
              { type: 'retrieve', value: '获取' },
              { type: 'retrieveList', value: '获取所有' },
              { type: 'update', value: '更新' },
              { type: 'delete', value: '删除' }
            ]
          },
          relationalDatabaseModule: {
            use: false,
            databases: [
              {
                type: 'mysql',
                timezone: 'GMT%2B8',
                host: 'localhost:3306',
                name: 'mekcone_mall',
                username: 'root',
                password: '123456',
                tables: [
                  {
                    name: 'goods',
                    description: '商品对象',
                    columns: [
                      {
                        name: 'goods_id',
                        type: 'int',
                        primaryKey: true,
                        filter: false,
                        detail: false
                      },
                      {
                        name: 'goods_name',
                        type: 'varchar',
                        length: 50,
                        primaryKey: false,
                        filter: false,
                        detail: false
                      },
                      {
                        name: 'catalogue_id',
                        type: 'varchar',
                        length: 50,
                        primaryKey: false,
                        filter: true,
                        detail: false
                      },
                      {
                        name: 'goods_price',
                        type: 'double',
                        primaryKey: false,
                        filter: false,
                        detail: false
                      },
                      {
                        name: 'goods_description',
                        type: 'varchar',
                        length: 50,
                        primaryKey: false,
                        filter: false,
                        detail: true
                      }
                    ]
                  }
                ]
              }
            ]
          },
          springBootModule: {
            extensions: [
              { _id: 'lombok', use: true },
              { _id: 'swagger2', use: true },
              { _id: 'cross-origin', use: true }
            ],
            properties: {
              applicationName: 'Mall',
              pageSize: 15,
              serverPort: 9090,
              crossOrigin: {
                allowedHeaders: [],
                allowedMethods: [],
                allowedOrigins: ['*']
              }
            },
            use: false,
            applicationPropertiesParser: { properties: [] },
            configs: [],
            controllers: [],
            entities: [],
            mybatisMappers: [],
            services: [],
            serviceImpls: []
          }
        },
        _class: 'com.mekcone.excrud.codegen.model.project.Project'
      },
      tableData: [
        {
          date: 'MekCone Mall',
          name: 'Tenton Lien',
          address: '13:55, Jun. 22, 2020'
        },
        {
          date: 'MekCone Blog',
          name: '王小虎',
          address: '13:55, Jun. 22, 2020'
        },
        {
          date: '2016-05-01',
          name: '王小虎',
          address: '13:55, Jun. 22, 2020'
        },
        {
          date: '2016-05-03',
          name: '王小虎',
          address: '13:55, Jun. 22, 2020'
        }
      ],
      data: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    }
  },
  methods: {
    handleNodeClick (data) {
      console.log(data)
    },
    reloadModuleTree () {
      this.data = []
      for (var module in this.project.moduleSet) {
        var item = {}
        let moduleName = module
        moduleName = module.substring(0, moduleName.length - 6)
        if (moduleName.length > 0) {
          moduleName = moduleName.charAt(0).toUpperCase() + moduleName.substring(1)
        }
        var i = 1
        while (i < moduleName.length) {
          if (moduleName.charAt(i) >= 'A' && moduleName.charAt(i) <= 'Z') {
            moduleName = moduleName.substring(0, i) + ' ' + moduleName.substring(i)
            i += 2
          } else {
            i++
          }
        }
        item.label = moduleName
        if (this.project.moduleSet[module].extensions !== undefined) {
          item.children = []
          for (var extension in this.project.moduleSet[module].extensions) {
            item.children.push({label: this.project.moduleSet[module].extensions[extension]._id})
          }
        }
        console.log(JSON.stringify(item))
        this.data.push(item)
      }
    }
  },
  computed: {
    newVersion () {
      return this.project.version
    }
  },
  mounted: function () {
    this.project.version = '20'
    this.reloadModuleTree()
  },
  watch: {
    newVersion (val) {
      /* this.data = [
        {
          label: 'hahaha'
        }
      ] */
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1,
h2 {
  font-weight: normal;
}
.editor-container {
  height: 100%;
}
.projects-title {
  text-align: left;
  font-size: 18px;
  margin-top: 40px;
  margin-left: 20px;
  margin-bottom: 20px;
}
.project-table {
  margin-left: 20px;
  width: calc(100% - 140px);
  background: none;
}
.button-create {
  background-color: #195f71;
  color: white;
  border-radius: 2px;
  height: 30px;
  width: 150px;
  outline: none;
  border: none;
  margin-top: 40px;
  cursor: pointer;
}
.el-tree {
  background: transparent;
}
.second-left-pane {
  border-right: solid 1px #e5e5e5;
  background: #f5f5f5;
  height: 100%;
  text-align: left;
  display: inline-block;
  width: 100%;
}
</style>
