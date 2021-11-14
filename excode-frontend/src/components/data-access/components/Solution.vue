<template>
  <div class="solution-select">
    <span style="margin: 0 20px 0 20px">Server-Side Solution</span>
    <el-select v-model="serverSideSolution" size="small">
      <el-option
        label="Java - Spring Boot"
        value="java-spring-boot"
      ></el-option>
      <el-option
        label="Python - Django"
        value="python-django"
        disabled
      ></el-option>
      <el-option label="Go - Gin" value="go-gin" disabled></el-option>
      <el-option label="PHP - Laravel" value="php-laravel" disabled></el-option>
      <el-option label="NodeJS" value="nodejs" disabled></el-option>
    </el-select>
  </div>
  <div class="property-block">
    <h1>POM</h1>
    <el-table
      :data="pomTableData"
      :show-header="false"
      border
      style="width: 100%"
    >
      <el-table-column prop="key" width="180" style="background: #f5f7fa" />
      <el-table-column prop="value" />
    </el-table>
  </div>

  <div class="property-block">
    <h1>Middleware</h1>
    <el-table
      :data="middlewareTableData"
      :show-header="false"
      border
      style="width: 100%"
    >
      <el-table-column prop="key" width="180" />
      <el-table-column prop="value" />
    </el-table>
  </div>

  <div class="property-block">
    <h1>Extensions</h1>
    <el-table
      :data="extensionTableData"
      :show-header="false"
      border
      style="width: 100%"
    >
      <el-table-column prop="key" width="180" />
      <el-table-column prop="value" />
      <el-table-column>
        <el-switch v-model="extensionTableData.value" />
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'Solution',
  data () {
    return {
      serverSideSolution: '',
      pomTableData: [
        {
          key: 'Group ID',
          value: 'com.vancome.excode.example'
        },
        {
          key: 'Artifact ID',
          value: 'mall'
        },
        {
          key: 'Version',
          value: '0.1.0'
        },
        {
          key: 'ORM Type',
          value: 'MyBatis with Annotation'
        }
      ],
      middlewareTableData: [
        {
          key: 'MySQL',
          value: 'mysql://root:******@127.0.0.1:3306/mydb'
        },
        {
          key: 'MongoDB',
          value: ''
        },
        {
          key: 'Redis',
          value: ''
        },
        {
          key: 'RabbitMQ',
          value: ''
        }
      ],
      extensionTableData: [
        {
          key: 'Lombok',
          value: true
        },
        {
          key: 'Swagger',
          value: 'OFF'
        },
        {
          key: 'Jasypt',
          value: 'OFF'
        }
      ]
    }
  },
  methods: {
  }
}
</script>

<style scoped>
h1 {
  font-size: 16px;
  font-weight: 500;
}
.property-block {
  margin-bottom: 25px;
  margin-left: 20px;
  margin-right: 20px;
}
:deep(.el-table__row td) {
  border-right: solid 1px #ebeef5;
  border-bottom: solid 1px #ebeef5;
  height: 40px;
}
.solution-select {
  margin-top: 20px;
}
:deep(.el-select-dropdown__item span) {
  font-family: Arial, Helvetica, sans-serif;
}

</style>
