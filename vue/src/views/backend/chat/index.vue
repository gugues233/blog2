<template>
  <div>
    <el-card>
      <el-select v-model='queryParams.senderId' clearable filterable placeholder='请选择发送者'
                 style='width: 200px; margin-right: 10px'>
        <el-option v-for='item in senderList' :key='item.id' :label='item.name' :value='item.id'>
        </el-option>
      </el-select>
      <el-select v-model='queryParams.receiverId' clearable filterable placeholder='请选择接收者'
                 style='width: 200px; margin-right: 10px'>
        <el-option v-for='item in receiverList' :key='item.id' :label='item.name' :value='item.id'>
        </el-option>
      </el-select>
      <el-input v-model="queryParams.message" clearable placeholder="请输入消息内容"
                style="width: 200px; margin-right: 10px"></el-input>
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
      <vue3-json-excel :fields="chatFields" :json-data="chatList" name='聊天列表.xls'>
        <el-button icon="Download" plain style="margin-left: 10px">导出</el-button>
      </vue3-json-excel>
    </el-card>

    <el-card>
      <el-table v-loading="loading" :data="chatList" stripe @selection-change="handleSelectionChange">
        <el-table-column align="center" type="selection" width="55"></el-table-column>
        <el-table-column align="center" label="序号" type="index" width="70"></el-table-column>
        <el-table-column align="center" label="发送者">
          <template v-slot="{ row }">
            {{ row.sender.name || row.sender.username }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="接收者">
          <template v-slot="{ row }">
            {{ row.receiver.name || row.receiver.username }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="消息内容" prop="message"></el-table-column>
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

    <el-dialog v-model="form.visible" :title="form.title" destroy-on-close width="40%">
      <el-form ref="formRef" :model="form.data" :rules="rules" label-width="80px">
        <el-form-item label="发送者" prop="senderId">
          <el-select v-model='form.data.senderId' clearable filterable placeholder='请选择发送者'>
            <el-option v-for='item in senderList' :key='item.id' :label='item.name' :value='item.id'>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="接收者" prop="receiverId">
          <el-select v-model='form.data.receiverId' clearable filterable placeholder='请选择接收者'>
            <el-option v-for='item in receiverList' :key='item.id' :label='item.name' :value='item.id'>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="消息内容" prop="message">
          <el-input v-model="form.data.message" autocomplete="new"></el-input>
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
import {getChatOne, getChatPage, removeChatBatchByIds, saveChat} from '@/api/chat';
import {getUserList} from '@/api/user';

export default {
  name: "ChatView",
  data () {
    return {
      loading: true,
      queryParams: {
        pageNo: 1,
        pageSize: 20,
        senderId: null,
        receiverId: null,
        message: ''
      },
      ids: [],
      single: true,
      multiple: true,
      senderList: [],
      receiverList: [],
      chatList: [],
      total: 0,
      chatFields: {
        序号: {
          field: 'id',
          callback: (id) => {
            const index = this.chatList.findIndex(item => item.id === id)
            return index + 1
          }
        },
        主键ID: 'id',
        发送者ID: 'senderId',
        接收者ID: 'receiverId',
        消息内容: 'message'
      },
      form: {
        visible: false,
        title: '',
        data: {}
      },
      rules: {
        senderId: [{required: true, message: '请输入发送者ID', trigger: 'blur'}],
        receiverId: [{required: true, message: '请输入接收者ID', trigger: 'blur'}],
        message: [{required: true, message: '请输入消息内容', trigger: 'blur'}]
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
        this.senderList = res.data || []
      })
      getUserList({}).then(res => {
        this.receiverList = res.data || []
      })
      getChatPage(this.queryParams).then(res => {
        this.chatList = res.data?.records || []
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
        title: '添加聊天',
        data: {
          senderId: null,
          receiverId: null,
          message: '',
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
      getChatOne(params).then(res => {
        if (res.code !== 200) return
        this.form = {
          visible: true,
          title: '编辑聊天',
          data: {
            ...res.data
          }
        }
      })
    },
    handleSave () {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        saveChat(this.form.data).then(res => {
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
      removeChatBatchByIds(params).then(res => {
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
        senderId: null,
        receiverId: null,
        message: ''
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
