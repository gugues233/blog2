<template>
  <div>
    <el-card>
       <el-input v-model="queryParams.username" clearable placeholder="请输入用户名" style="width: 200px; margin-right: 10px"></el-input>
       <el-input v-model="queryParams.password" clearable placeholder="请输入密码" style="width: 200px; margin-right: 10px"></el-input>
       <el-input v-model="queryParams.name" clearable placeholder="请输入姓名" style="width: 200px; margin-right: 10px"></el-input>
       <el-input v-model="queryParams.avatar" clearable placeholder="请输入头像" style="width: 200px; margin-right: 10px"></el-input>
       <el-select v-model='queryParams.status' clearable placeholder='请选择状态'
                  style='width: 200px; margin-right: 10px'>
         <el-option v-for='item in statusList' :key='item.value' :label='item.label' :value='item.value'></el-option>
       </el-select>
       <el-input v-model="queryParams.role" clearable placeholder="请输入角色(0管理员、1用户)" style="width: 200px; margin-right: 10px"></el-input>
       <el-input v-model="queryParams.phone" clearable placeholder="请输入电话" style="width: 200px; margin-right: 10px"></el-input>
       <el-input v-model="queryParams.email" clearable placeholder="请输入邮箱" style="width: 200px; margin-right: 10px"></el-input>
       <el-input v-model="queryParams.balance" clearable placeholder="请输入余额" style="width: 200px; margin-right: 10px"></el-input>
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
      <vue3-json-excel :fields="userFields" :json-data="userList" name='用户信息列表.xls'>
        <el-button icon="Download" plain style="margin-left: 10px">导出</el-button>
      </vue3-json-excel>
    </el-card>

    <el-card>
      <el-table v-loading="loading" :data="userList" stripe @selection-change="handleSelectionChange">
        <el-table-column align="center" type="selection" width="55"></el-table-column>
        <el-table-column align="center" label="序号" type="index" width="70"></el-table-column>
        <el-table-column align="center" label="用户名" prop="username"></el-table-column>
        <el-table-column align="center" label="密码" prop="password"></el-table-column>
        <el-table-column align="center" label="姓名" prop="name"></el-table-column>
        <el-table-column align="center" label="头像" prop="avatar"></el-table-column>
        <el-table-column align="center" label="状态(0禁用、1正常)" prop="status"></el-table-column>
        <el-table-column align="center" label="角色(0管理员、1用户)" prop="role"></el-table-column>
        <el-table-column align="center" label="电话" prop="phone"></el-table-column>
        <el-table-column align="center" label="邮箱" prop="email"></el-table-column>
        <el-table-column align="center" label="余额" prop="balance"></el-table-column>
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
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.data.username" autocomplete="new"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.data.password" autocomplete="new"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.data.name" autocomplete="new"></el-input>
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <el-input v-model="form.data.avatar" autocomplete="new"></el-input>
        </el-form-item>
        <el-form-item v-if="form.data.id" label='状态' prop='status'>
          <el-select v-model='form.data.status' clearable placeholder='请选择状态'>
            <el-option v-for='item in statusList' :key='item.value' :label='item.label' :value='item.value'></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="角色(0管理员、1用户)" prop="role">
          <el-input v-model="form.data.role" autocomplete="new"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.data.phone" autocomplete="new"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.data.email" autocomplete="new"></el-input>
        </el-form-item>
         <el-form-item label="余额" prop="balance">
           <el-input v-model="form.data.balance" autocomplete="new"></el-input>
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
import {getUserOne, getUserPage, removeUserBatchByIds, saveUser} from '@/api/user';

export default {
  name: "UserView",
  data() {
    return {
      loading: true,
      queryParams: {
        pageNo: 1,
        pageSize: 20,
        username: '',
        password: '',
        name: '',
        avatar: '',
        status: '',
        role: '',
        phone: '',
        email: '',
        balance: null
      },
      ids: [],
      single: true,
      multiple: true,
      userList: [],
      total: 0,
      userFields: {
        序号: {
          field: 'id',
          callback: (id) => {
            const index = this.userList.findIndex(item => item.id === id)
            return index + 1
          }
        },
        主键ID: 'id',
        用户名: 'username',
        密码: 'password',
        姓名: 'name',
        头像: 'avatar',
        状态: 'status',
        角色: 'role',
        电话: 'phone',
        邮箱: 'email',
        余额: 'balance'
      },
      statusList: [
        { label: '占位符1', value: '1' },
        { label: '占位符2', value: '2' },
        { label: '占位符3', value: '3' }
      ],
      form: {
        visible: false,
        title: '',
        data: {}
      },
      rules: {
        username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        password: [{required: true, message: '请输入密码', trigger: 'blur'}],
        name: [{required: true, message: '请输入姓名', trigger: 'blur'}],
        avatar: [{required: true, message: '请输入头像', trigger: 'blur'}],
        status: [{required: true, message: '请输入状态(0禁用、1正常)', trigger: 'blur'}],
        role: [{required: true, message: '请输入角色(0管理员、1用户)', trigger: 'blur'}],
        phone: [{required: true, message: '请输入电话', trigger: 'blur'}],
        email: [{required: true, message: '请输入邮箱', trigger: 'blur'}],
        balance: [{required: true, message: '请输入余额', trigger: 'blur'}]
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
      getUserPage(this.queryParams).then(res => {
        this.userList = res.data?.records || []
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
        title: '添加用户信息',
        data: {
          username: '',
          password: '',
          name: '',
          avatar: '',
          status: '',
          role: '',
          phone: '',
          email: '',
          balance: null,
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
      getUserOne(params).then(res => {
        if (res.code !== 200) return
        this.form = {
          visible: true,
          title: '编辑用户信息',
          data: {
            ...res.data
          }
        }
      })
    },
    handleSave() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        saveUser(this.form.data).then(res => {
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
      removeUserBatchByIds(params).then(res => {
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
        username: '',
        password: '',
        name: '',
        avatar: '',
        status: '',
        role: '',
        phone: '',
        email: '',
        balance: null
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
