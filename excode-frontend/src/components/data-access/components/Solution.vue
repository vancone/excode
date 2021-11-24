<template>
  <div class="solution-wrapper">
    <div class="solution-select">
      <span style="margin: 0 20px 0 0">Server-Side Solution</span>
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
        <el-option
          label="Python - Flask"
          value="python-flask"
          disabled
        ></el-option>
        <el-option label="Go - Gin" value="go-gin" disabled></el-option>
        <el-option
          label="PHP - Laravel"
          value="php-laravel"
          disabled
        ></el-option>
        <el-option
          label="JavaScript - NodeJS"
          value="nodejs"
          disabled
        ></el-option>
      </el-select>
    </div>

    <div class="block-wrapper">
      <el-descriptions
        class="margin-top"
        title="POM"
        :column="1"
        size="small"
        border
      >
        <template #extra>
          <el-button type="primary" size="mini">Edit</el-button>
        </template>
        <el-descriptions-item>
          <template #label> Group ID </template>
          {{ pomInfo.groupId }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label> Artifact ID </template>
          {{ pomInfo.artifactId }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label> Version </template>
          {{ pomInfo.version }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label> ORM Type </template>
          {{ pomInfo.ormType }}
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <div class="block-wrapper">
      <el-descriptions
        class="margin-top"
        title="Connections"
        :column="1"
        size="small"
        border
      >
        <template #extra>
          <el-button type="primary" size="mini">Edit</el-button>
        </template>
        <el-descriptions-item>
          <template #label> MySQL </template>
          {{ connections.mysql }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label> MongoDB </template>
          {{ connections.mongodb }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label> Redis </template>
          {{ connections.redis }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label> RabbitMQ </template>
          {{ connections.rabbitmq }}
        </el-descriptions-item>
      </el-descriptions>
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
          <el-switch v-model="extensionTableData" />
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";

export default defineComponent({
  name: "Solution",
  setup() {
    const serverSideSolution = ref("");
    const pomInfo = {
      groupId: "com.vancome.excode.example",
      artifactId: "example",
      version: "0.1.0",
      ormType: "MyBatis with Annotation",
    };
    const connections = {
      mysql: "mysql://root:******@127.0.0.1:3306/mydb",
      mongodb: "",
      redis: "",
      rabbitmq: "",
    };
    const extensionTableData = [
      {
        key: "Lombok",
        value: true,
      },
      {
        key: "Swagger",
        value: "OFF",
      },
      {
        key: "Jasypt",
        value: "OFF",
      },
    ];

    return {
      serverSideSolution,
      pomInfo,
      connections,
      extensionTableData,
    };
  },
});
</script>

<style scoped>
h1 {
  font-size: 16px;
  font-weight: 500;
}
.solution-wrapper {
  padding: 20px;
  height: calc(100% - 40px);
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
  margin-top: 0px;
}
.block-wrapper {
  background: white;
  padding: 20px;
  border: solid 1px #ddd;
  border-radius: 5px;
  margin: 20px 0 20px 0;
}
</style>
