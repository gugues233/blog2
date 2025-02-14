<template>
  <div>
    <el-row>
      <el-col :span='12'>
        <el-card>
          <div style='margin: 15px; text-align: center'>
            <el-upload :action='url' :headers='headers' :on-success='handleAvatarSuccess'
                       :show-file-list='false' class='avatar-uploader'>
              <img v-if='user.avatar' :src='getUrl(user.avatar)' alt='' class='avatar' />
              <i v-else class='el-icon-plus avatar-uploader-icon'></i>
            </el-upload>
          </div>
          <el-form ref='userFormRef' :model='user' :rules='rules' label-width='80px'>
            <el-form-item label='用户名' prop='username'>
              <el-input v-model='user.username' autocomplate='new' disabled placeholder='用户名'></el-input>
            </el-form-item>
            <el-form-item label='姓名' prop='name'>
              <el-input v-model='user.name' autocomplete='new' placeholder='姓名'></el-input>
            </el-form-item>
            <el-form-item label='电话' prop='phone'>
              <el-input v-model='user.phone' autocomplete='new' placeholder='电话'></el-input>
            </el-form-item>
            <el-form-item label='邮箱' prop='email'>
              <el-input v-model='user.email' autocomplete='new' placeholder='邮箱'></el-input>
            </el-form-item>
            <div style='text-align: center; margin-bottom: 20px'>
              <el-button type='primary' @click='handleSubmit'>保 存</el-button>
            </div>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span='12'>
        <el-card>
          <el-form ref='passwordFormRef' :model='user' :rules='rules' label-width='80px'>
            <el-form-item label='原密码' prop='password'>
              <el-input v-model='user.password' placeholder='原密码' show-password></el-input>
            </el-form-item>
            <el-form-item label='新密码' prop='newPassword'>
              <el-input v-model='user.newPassword' placeholder='新密码' show-password></el-input>
            </el-form-item>
            <el-form-item label='确认密码' prop='confirmPassword'>
              <el-input v-model='user.confirmPassword' placeholder='确认密码' show-password></el-input>
            </el-form-item>
            <div style='text-align: center; margin-bottom: 20px'>
              <el-button type='primary' @click='handleUpdatePassword'>确认修改</el-button>
            </div>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { saveUser } from '@/api/user'
import { getByToken, updatePassword } from '@/api/web'

export default {
  name: 'UserCenterView',
  data () {
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'))
      } else if (value !== this.user.newPassword) {
        callback(new Error('确认密码错误'))
      } else {
        callback()
      }
    }
    return {
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      url: import.meta.env.VITE_APP_BASE_API + '/files/upload',
      form: {
        visible: false,
        title: '',
        data: {}
      },
      headers: {
        token: JSON.parse(localStorage.getItem('user') || '{}').token
      },
      rules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入电话', trigger: 'blur' }],
        email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
        password: [{ required: true, message: '请输入原始密码', trigger: 'blur' }],
        newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
        confirmPassword: [{ validator: validatePassword, required: true, trigger: 'blur' }]
      }
    }
  },
  created () {
    this.getInfo()
  },
  computed: {
    getUrl () {
      return (path) => {
        return import.meta.env.VITE_APP_BASE_API + path
      }
    }
  },
  methods: {
    getInfo () {
      getByToken().then(res => {
        if (res.code === 200) {
          this.user = res.data
          localStorage.setItem('user', JSON.stringify(this.user))
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleSubmit () {
      this.$refs.userFormRef.validate(valid => {
        if (!valid) return
        saveUser(this.user).then(res => {
          if (res.code === 200) {
            this.$message.success('修改成功！')
            this.$emit('update:user')
          } else {
            this.$message.error(res.msg)
          }
        }).finally(() => {
          this.getInfo()
        })
      })
    },
    handleUpdatePassword () {
      this.$refs.passwordFormRef.validate(valid => {
        if (!valid) return
        updatePassword(this.user).then(res => {
          if (res.code === 200) {
            localStorage.removeItem('user')
            this.$message.success('修改密码成功！请重新登录')
            this.$router.push('/login')
          } else {
            this.$message.error(res.msg)
          }
        })
      })
    },
    handleAvatarSuccess (response) {
      this.user.avatar = response.data
    }
  }
}
</script>

<style lang='scss' scoped>
$border-radius: 50%;
$avatar-size: 120px;

.el-card, {
  margin: 10px 10px;
}

:deep(.el-form-item__label) {
  font-weight: bold;
}

.el-upload {
  border-radius: $border-radius;
}

:deep(.avatar-uploader) {
  .el-upload {
    border: 1px dashed #d9d9d9;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    border-radius: $border-radius;

    &:hover {
      border-color: #409EFF;
    }
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: $avatar-size;
    height: $avatar-size;
    line-height: $avatar-size;
    text-align: center;
    border-radius: $border-radius;
    transition: none;
  }

  &:hover .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
  }
}

.avatar {
  width: $avatar-size;
  height: $avatar-size;
  display: block;
  border-radius: $border-radius;
}
</style>
