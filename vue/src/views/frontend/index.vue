<template>
  <div>
    <el-menu :default-active="$route.path" mode="horizontal" router>
      <el-sub-menu v-if="user.token" index="#">
        <template #title>
          <el-avatar :src="getAvatar"/>
        </template>
        <el-menu-item v-if="user.role === '0'" index="/backend">返回后台</el-menu-item>
        <el-menu-item index="/center">个人中心</el-menu-item>
        <el-menu-item index="#" @click.native="handleLogout">退出登录</el-menu-item>
      </el-sub-menu>
      <el-menu-item v-else index="/login">登录</el-menu-item>
      <el-menu-item index="/image-recognition">图像识别</el-menu-item>
      <el-menu-item index="/discuss">讨论</el-menu-item>
      <el-menu-item :index="`/main/${user.id}`">个人主页</el-menu-item>
      <div style="flex-grow: 1"/>
      <el-menu-item index="/blog/null">发布文章</el-menu-item>
    </el-menu>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: "FrontendView",
  data () {
    return {
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      menu: '/ImageRecognition',
    }
  },
  computed: {
    getUrl () {
      return (path) => {
        return import.meta.env.VITE_APP_BASE_API + path
      }
    },
    getAvatar () {
      return this.user.avatar ? this.getUrl(this.user.avatar) : new URL('@/assets/imgs/profile.png', import.meta.url)
    }
  },
  methods: {
    toCenter () {
      this.$router.push('/center')
    },
    handleLogout () {
      this.$confirm('确认退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.clear()
        this.$router.push('/')
      })
    },
  }
}
</script>

<style lang="scss" scoped>
.el-menu {
  height: 80px;
  //background-color: #1a7cff;

  .el-menu-item {
    font-size: large;
    font-weight: bolder;
    //color: white;
    //background-color: #1a7cff;
  }
}
</style>
