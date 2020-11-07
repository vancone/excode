<template>
  <div class="project-properties">
    <a-modal v-model="visible" title="Project Properties" on-ok="handleOk">
      <template slot="footer">
        <a-button key="back" class="button-cancel" @click="handleCancel">
          Cancel
        </a-button>
        <a-button key="submit" class="button-ok" :loading="loading" @click="handleOk">
          Save
        </a-button>
      </template>
      <a-form-model :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
    <a-form-model-item label="Name">
      <a-input v-model="form.name"/>
    </a-form-model-item>

    <a-form-model-item label="Version">
      <a-input v-model="form.version" />
    </a-form-model-item>
    <!-- <a-form-model-item label="Activity zone">
      <a-select v-model="form.region" placeholder="please select your zone">
        <a-select-option value="shanghai">
          Zone one
        </a-select-option>
        <a-select-option value="beijing">
          Zone two
        </a-select-option>
      </a-select>
    </a-form-model-item>
    <a-form-model-item label="Activity time">
      <a-date-picker
        v-model="form.date1"
        show-time
        type="date"
        placeholder="Pick a date"
        style="width: 100%;"
      />
    </a-form-model-item>
    <a-form-model-item label="Instant delivery">
      <a-switch v-model="form.delivery" />
    </a-form-model-item>
    <a-form-model-item label="Activity type">
      <a-checkbox-group v-model="form.type">
        <a-checkbox value="1" name="type">
          Online
        </a-checkbox>
        <a-checkbox value="2" name="type">
          Promotion
        </a-checkbox>
        <a-checkbox value="3" name="type">
          Offline
        </a-checkbox>
      </a-checkbox-group>
    </a-form-model-item>
    <a-form-model-item label="Resources">
      <a-radio-group v-model="form.resource">
        <a-radio value="1">
          Sponsor
        </a-radio>
        <a-radio value="2">
          Venue
        </a-radio>
      </a-radio-group>
    </a-form-model-item>
    <a-form-model-item label="Activity form">
      <a-input v-model="form.desc" type="textarea" />
    </a-form-model-item> -->
    <!-- <a-form-model-item :wrapper-col="{ span: 14, offset: 4 }">
      <a-button type="primary" @click="onSubmit" class="button-ok">
        Save
      </a-button>
      <a-button style="margin-left: 10px;" class="button-cancel">
        Cancel
      </a-button>
    </a-form-model-item> -->
  </a-form-model>
    </a-modal>
  </div>
</template>

<script>
import { saveProject } from '@/api/project'
export default {
  name: 'ProjectProperties',
  data () {
    return {
      labelCol: { span: 3 },
      wrapperCol: { span: 20 },
      form: {
        name: '',
        version: ''
      },
      loading: false,
      visible: false
    }
  },
  methods: {
    onSubmit () {
      console.log('submit!', this.form)
      // saveProject(this.form).then((res) => {
      //   this.form = res.data.content
      // })
    },
    showModal () {
      this.visible = true
    },
    handleOk (e) {
      this.loading = true
      // alert(JSON.stringify(this.form))
      saveProject(this.form).then((res) => {
        this.$notification.open({
          message: 'Notification Title',
          description: 'This is the content of the notification. This is the content of the notification. This is the content of the notification.',
          onClick: () => {
            console.log('Notification Clicked!')
          }
        })
      })
      setTimeout(() => {
        this.visible = false
        this.loading = false
      }, 1000)
    },
    handleCancel (e) {
      this.visible = false
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.project-properties {
  padding-top: 20px;
  height: 100%;
}
.project-properties h1 {
  margin-left: 100px;
  color: #bbb;
}
/deep/ label {
  color: #bbb;
}
/deep/ .ant-input {
  background: #45494a;
  border: solid 1px #646464;
  color: #bbb;
}
.button-ok {
  background: #365880;
  border: solid 1px #4c708c;
  color: #bbb;
  width: 80px;
}
.button-cancel {
  background: #4c5052;
  border: solid 1px #5e6060;
  color: #bbb;
  width: 80px;
}
/deep/ .ant-modal-header {
  background: #3b3b3b;
  border-bottom: solid 1px #515151;
}
/deep/ .ant-modal-body {
  background: #323232;
}
/deep/ .ant-modal-footer {
  background: #323232;
  border-top: solid 1px #323232;
}
/deep/ .ant-modal-title {
  color: #bbb;
}
/deep/ .ant-modal-close-icon {
  color: #a9a9a9;
}
</style>
