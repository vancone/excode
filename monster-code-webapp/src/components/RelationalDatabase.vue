<template>
  <a-table class="rdb-table" :columns="columns" :data-source="data">
    <a-select slot="allowNull" slot-scope="text, record" :default-value="record.allowNull">
      <a-select-option value="true"> True </a-select-option>
      <a-select-option value="false"> False </a-select-option>
    </a-select>
  </a-table>
</template>

<script>
export default {
  data () {
    return {
      columns: [
        {
          title: 'Column Name',
          dataIndex: 'name',
          key: 'name',
          scopedSlots: { customRender: 'name' }
        },
        {
          title: 'Data Type',
          dataIndex: 'dataType',
          key: 'dataType'
        },
        {
          title: 'Length',
          key: 'length'
        },
        {
          title: 'Default Value',
          dataIndex: 'defaultValue',
          key: 'defaultValue'
        },
        {
          title: 'Allow Null',
          key: 'allowNull',
          scopedSlots: { customRender: 'allowNull' }
        },
        {
          title: 'Primary Key',
          key: 'primaryKey'
        },
        {
          title: 'Description',
          key: 'description'
        }
      ],
      data: [
        {
          name: 'a',
          dataType: 'varchar',
          allowNull: 'true'
        },
        {
          name: 'b',
          dataType: 'varchar',
          allowNull: 'false'
        }
      ]
    }
  }
}
</script>

<style scoped>
.rdb-table {
  margin: 20px;
}
</style>
