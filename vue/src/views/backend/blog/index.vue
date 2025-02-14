<template>
  <div>
    <el-card>
      <el-input v-model="queryParams.title" clearable placeholder="请输入标题"
                style="width: 200px; margin-right: 10px"></el-input>
      <el-input v-model="queryParams.content" clearable placeholder="请输入内容"
                style="width: 200px; margin-right: 10px"></el-input>
      <el-select v-model='queryParams.userId' clearable filterable placeholder='请选择作者'
                 style='width: 200px; margin-right: 10px'>
        <el-option v-for='item in userList' :key='item.id' :label='item.name' :value='item.id'>
        </el-option>
      </el-select>
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
      <vue3-json-excel :fields="blogFields" :json-data="blogList" name='博客列表.xls'>
        <el-button icon="Download" plain style="margin-left: 10px">导出</el-button>
      </vue3-json-excel>
    </el-card>

    <el-card>
      <el-table v-loading="loading" :data="blogList" stripe @selection-change="handleSelectionChange">
        <el-table-column align="center" type="selection" width="55"></el-table-column>
        <el-table-column align="center" label="序号" type="index" width="70"></el-table-column>
        <el-table-column align="center" label="标题" prop="title"></el-table-column>
        <el-table-column align="center" label="内容" prop="content"></el-table-column>
        <el-table-column align="center" label="作者" prop="user.name"></el-table-column>
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

    <el-dialog v-model="form.visible" :title="form.title" destroy-on-close width="80%">
      <el-form ref="formRef" :model="form.data" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.data.title" autocomplete="new"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <v-md-editor v-model="form.data.content"></v-md-editor>
        </el-form-item>
        <el-form-item label="作者" prop="userId">
          <el-select v-model='form.data.userId' clearable filterable placeholder='请选择作者'>
            <el-option v-for='item in userList' :key='item.id' :label='item.name' :value='item.id'>
            </el-option>
          </el-select>
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
import {getBlogOne, getBlogPage, removeBlogBatchByIds, saveBlog} from '@/api/blog';
import {getUserList} from '@/api/user'
import {useWangEditor} from "wangeditor5-for-vue3";
import {ref, shallowReactive} from "vue";

export default {
  name: "BlogView",
  data () {
    return {
      loading: true,
      queryParams: {
        pageNo: 1,
        pageSize: 20,
        title: '',
        content: '',
        userId: null
      },
      ids: [],
      single: true,
      multiple: true,
      userList: [],
      blogList: [],
      total: 0,
      blogFields: {
        序号: {
          field: 'id',
          callback: (id) => {
            const index = this.blogList.findIndex(item => item.id === id)
            return index + 1
          }
        },
        主键ID: 'id',
        标题: 'title',
        内容: 'content',
        作者ID: 'userId'
      },
      form: {
        visible: false,
        title: '',
        data: {}
      },
      rules: {
        title: [{required: true, message: '请输入标题', trigger: 'blur'}],
        content: [{required: true, message: '请输入内容', trigger: 'blur'}],
        userId: [{required: true, message: '请输入作者ID', trigger: 'blur'}]
      }
    }
  },
  created () {
    this.getPage()
  },
  computed: {
    getUrl () {
      return (path) => {
        return import.meta.env.VITE_APP_BASE_API + path
      }
    }
  },
  methods: {
    getPage () {
      this.loading = true
      getUserList({}).then(res => {
        this.userList = res.data || []
      })
      getBlogPage(this.queryParams).then(res => {
        this.blogList = res.data?.records || []
        this.total = res.data?.total || 0
        this.loading = false
      })
    },
    showAdd () {
      this.$nextTick(() => {
        if (!this.$refs.formRef) return
        this.$refs.formRef.resetFields()
      })
      this.form = {
        visible: true,
        title: '添加博客',
        data: {
          title: '',
          content: '',
          userId: null,
          remark: ''
        }
      }
    },
    showEdit (row) {
      this.$nextTick(() => {
        if (!this.$refs.formRef) return
        this.$refs.formRef.resetFields()
      })
      const params = {id: row.id || this.ids[0]}
      getBlogOne(params).then(res => {
        if (res.code !== 200) return
        this.form = {
          visible: true,
          title: '编辑博客',
          data: {
            ...res.data
          }
        }
      })
    },
    handleSave () {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        saveBlog(this.form.data).then(res => {
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
    handleDelete (id) {
      const params = id || this.ids
      removeBlogBatchByIds(params).then(res => {
        if (res.code === 200) {
          this.$message.success('删除成功！')
        } else {
          this.$message.error(res.msg)
        }
      }).finally(() => {
        this.getPage()
      })
    },
    handleSelectionChange (selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    resetQuery () {
      this.queryParams = {
        pageNo: 1,
        pageSize: 20,
        title: '',
        content: '',
        userId: null
      }
      this.getPage()
    },
    handleSizeChange (val) {
      this.queryParams.pageSize = val
      this.getPage()
    },
    handleCurrentChange (val) {
      this.queryParams.pageNo = val
      this.getPage()
    }
  },
  setup () {
    const {opts, handle, instance, clearContent, syncContent, reloadEditor} = useWangEditor({
      reloadDelary: 365,
      toolbar: {},
      editable: {
        config: {
          placeholder: '请开始你的表演'
        }
      }
    });
    const formData = shallowReactive({html: ''});
    const editor = ref(null);

    return {handle, formData, editor};
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
