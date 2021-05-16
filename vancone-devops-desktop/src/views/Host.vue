<template>
  <div class="host">
    <host-tree />
    <div class="host-basic-info">
      <h1>MekCone Server 1</h1>
      <p>IP: 10.0.0.1</p>
      <p>OS: Ubuntu Server 20.04 LTS</p>
      <div class="tags">
        <el-tag v-for="item in tags" :key="item.text" :type="getTagStatus(item.status)" size="mini">{{item.text}}</el-tag>
      </div>
      <el-button :type="buttonType" @click="connect">{{ buttonText }}</el-button>
    </div>
  </div>
</template>

<script>
import HostTree from '@/components/HostTree.vue'

export default {
  name: 'Host',
  components: {
    HostTree
  },
  data () {
    return {
      buttonType: 'primary',
      buttonText: 'Connect',
      tags: {
        docker: {
          text: 'Docker',
          status: 'running'
        },
        elasticsearch: {
          text: 'Elasticsearch',
          status: 'stop'
        },
        mysql: {
          text: 'MySQL',
          status: 'unknown'
        },
        nginx: {
          text: 'Nginx',
          status: 'unknown'
        },
        rabbitmq: {
          text: 'RabbitMQ',
          status: 'unknown'
        },
        redis: {
          text: 'Redis',
          status: 'unknown'
        },
        zookeeper: {
          text: 'ZooKeeper',
          status: 'unknown'
        }
      }
    }
  },
  methods: {
    connect () {
      this.buttonType = 'success'
      this.buttonText = 'Connected'
      // console.log(this.$refs.button_connect.set('type', 'success'))
    },
    getTagStatus (status) {
      switch (status) {
        case 'unknown': return 'info'
        case 'running': return 'success'
        case 'stop': return 'danger'
      }
    }
  }
}
</script>

<style scoped>
.host {
  height: 100%;
  /* margin-left: -10px; */
  /* overflow: auto; */
}
.host-basic-info {
  /* height:100%; */
  display: inline-block;
  padding-left: 30px;
}

.host-basic-info h1 {
  font-size: 20px;
}
.tags {
  padding-bottom: 20px;
}
.tags .el-tag {
  margin-right: 5px;
}
</style>
