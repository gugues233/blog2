<template>
  <div>
    <el-card>
       <el-input v-model="queryParams.name" clearable placeholder="请输入标签名" style="width: 200px; margin-right: 10px"></el-input>
      <el-button icon="Search" plain style="margin-left: 10px" type="info" @click="getPage">查询</el-button>
      <el-button icon="Refresh" plain style="margin-left: 10px" type="warning" @click="resetQuery">重置</el-button>
    </el-card>

    <el-card>
      <el-button icon="Plus" plain type="primary" @click="showAdd">新增</el-button>
      <el-button :disabled="single" icon="Edit" plain type="success" @click="showEdit">修改
      </el-button>
      <el-popconfirm title="确认删除该行数据吗？" @confirm="handleDelete">
        <template #reference>
          <el-button :disabled="multiple" icon="Delete" plain style="margin-left: 10px" type="danger">
            批量删除
          </el-button>
        </template>
      </el-popconfirm>
      <vue3-json-excel :fields="tagFields" :json-data="tagList" name='标签列表.xls'>
        <el-button icon="Download" plain style="margin-left: 10px">导出</el-button>
      </vue3-json-excel>
    </el-card>

    <el-card>
      <el-table v-loading="loading" :data="tagList" stripe @selection-change="handleSelectionChange">
        <el-table-column align="center" type="selection" width="55"></el-table-column>
        <el-table-column align="center" label="序号" type="index" width="70"></el-table-column>
        <el-table-column align="center" label="标签名" prop="name"></el-table-column>
        <el-table-column align="center" label="操作" width="180">
          <template v-slot="{ row }">
            <el-button icon="Edit" plain type="primary" @click="showEdit(row)">编辑</el-button>
            <el-popconfirm title="确认删除该行吗？" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button icon="Delete" plain style="margin-left: 10px" type="danger">
                  删除
                </el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          :current-page="queryParams.pageNo"
          :page-size="queryParams.pageSize"
          :page-sizes="[20, 30, 40, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange">
      </el-pagination>
    </el-card>

    <el-dialog :title="form.title" v-model="form.visible" destroy-on-close width="40%">
      <el-form ref="formRef" :model="form.data" :rules="rules" label-width="80px">
        <el-form-item label="标签名" prop="name">
          <el-input v-model="form.data.name" autocomplete="new"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.data.remark" :rows="5" autocomplete="new" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="form.visible = false">取 消</el-button>
        <el-button type="primary" @click="handleSave">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {getTagOne, getTagPage, removeTagBatchByIds, saveTag} from '@/api/tag';

export default {
  name: "TagView",
  data() {
    return {
      loading: true,
      queryParams: {
        pageNo: 1,
        pageSize: 20,
        name: ''
      },
      ids: [],
      single: true,
      multiple: true,
      tagList: [],
      total: 0,
      tagFields: {
        序号: {
          field: 'id',
          callback: (id) => {
            const index = this.tagList.findIndex(item => item.id === id)
            return index + 1
          }
        },
        主键ID: 'id',
        标签名: 'name'
      },
      form: {
        visible: false,
        title: '',
        data: {}
      },
      rules: {
        name: [{required: true, message: '请输入标签名', trigger: 'blur'}]
      }
    }
  },
  created() {
    this.getPage()
  },
  computed: {
    getUrl() {
      return (path) => {
        return import.meta.env.VITE_APP_BASE_API + path
      }
    }
  },
  methods: {
    getPage() {
      this.loading = true
      getTagPage(this.queryParams).then(res => {
        this.tagList = res.data?.records || []
        this.total = res.data?.total || 0
        this.loading = false
      })
    },
    showAdd() {
      this.$nextTick(() => {
        if (!this.$refs.formRef) return
        this.$refs.formRef.resetFields()
      })
      this.form = {
        visible: true,
        title: '添加标签',
        data: {
          name: '',
          remark: ''
        }
      }
    },
    showEdit(row) {
      this.$nextTick(() => {
        if (!this.$refs.formRef) return
        this.$refs.formRef.resetFields()
      })
      const params = {id: row.id || this.ids[0]}
      getTagOne(params).then(res => {
        if (res.code !== 200) return
        this.form = {
          visible: true,
          title: '编辑标签',
          data: {
            ...res.data
          }
        }
      })
    },
    handleSave() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        saveTag(this.form.data).then(res => {
          if (res.code === 200) {
            this.$message.success('保存成功！')
            this.form.visible = false
          } else {
            this.$message.error(res.msg)
          }
        }).finally(() => {
          this.getPage()
        })
      })
    },
    handleDelete(id) {
      const params = id || this.ids
      removeTagBatchByIds(params).then(res => {
        if (res.code === 200) {
          this.$message.success('删除成功！')
        } else {
          this.$message.error(res.msg)
        }
      }).finally(() => {
        this.getPage()
      })
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    resetQuery() {
      this.queryParams = {
        pageNo: 1,
        pageSize: 20,
        name: ''
      }
      this.getPage()
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getPage()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNo = val
      this.getPage()
    }
  }
}
</script>

<style lang="scss" scoped>
.el-card, .el-pagination {
    margin: 10px 0;
}

.el-select, .el-date-editor {
    width: 100%;
}
</style>
