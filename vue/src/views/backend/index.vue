<template>
  <el-container>
    <el-aside class="hidden-sm-and-down">
      <el-menu :default-active='menu' router>
        <div class="title">{{ title }}</div>
        <div v-for="(item, index) in menuList" :key="index">
          <el-sub-menu v-if="item?.children?.length > 0"
                       v-show='!item?.meta?.hidden && item.meta.roles.includes(user.role)'
                       :index='item.path'>
            <template #title>
              <el-icon>
                <component :is="item.meta?.icon"/>
              </el-icon>
              <span>{{ item.meta?.name }}</span>
            </template>
            <el-menu-item v-for='(item, index) in item.children'
                          v-show='!item?.meta?.hidden && item.meta.roles.includes(user.role)'
                          :key='index' :index='item.path'
                          @click='handleClickMenu(item.path)'>
              <el-icon>
                <component :is="item.meta?.icon"/>
              </el-icon>
              {{ item.meta.name }}
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item v-else v-show='!item?.meta?.hidden && item.meta.roles.includes(user.role)' :index="item.path"
                        @click='handleClickMenu(item.path)'>
            <el-icon>
              <component :is="item.meta?.icon"/>
            </el-icon>
            <span>{{ item.meta?.name }}</span>
          </el-menu-item>
        </div>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <el-breadcrumb :separator-icon="ArrowRight">
          <el-breadcrumb-item :to="{ path: '/backend' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{ menu }}</el-breadcrumb-item>
        </el-breadcrumb>

        <el-dropdown>
          <span class="header-dropdown">
            <el-avatar :src="avatar" alt="" @error="() => true">
              <img alt="" src="@/assets/imgs/profile.png"/>
            </el-avatar>
            <span class="header-username">{{ user.username }}</span>
            <el-icon class="el-icon--right">
              <ArrowDown/>
            </el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click.native.prevent='toFrontend'><span>前台</span></el-dropdown-item>
              <el-dropdown-item @click.native.prevent='toCenter'><span>个人中心</span></el-dropdown-item>
              <el-dropdown-item @click.native.prevent='handleLogout'><span>退出登录</span></el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>

      <el-main>
        <router-view/>
      </el-main>

      <el-footer class="hidden-md-and-up">
        <el-menu :default-active='menu' mode="horizontal" router>
          <div v-for="(item, index) in menuList" :key="index">
            <el-sub-menu v-if="item?.children?.length > 0"
                         v-show='!item?.meta?.hidden && item.meta.roles.includes(user.role)'
                         :index='item.path'>
              <template #title>
                <el-icon>
                  <component :is="item.meta?.icon"/>
                </el-icon>
                <span>{{ item.meta?.name }}</span>
              </template>
              <el-menu-item v-for='(item, index) in item.children'
                            v-show='!item?.meta?.hidden && item.meta.roles.includes(user.role)'
                            :key='index' :index='item.path'
                            @click='handleClickMenu(item.path)'>
                <el-icon>
                  <component :is="item.meta?.icon"/>
                </el-icon>
                {{ item.meta.name }}
              </el-menu-item>
            </el-sub-menu>
            <el-menu-item v-else v-show='!item?.meta?.hidden && item.meta.roles.includes(user.role)' :index="item.path"
                          @click='handleClickMenu(item.path)'>
              <el-icon>
                <component :is="item.meta?.icon"/>
              </el-icon>
              <span>{{ item.meta?.name }}</span>
            </el-menu-item>
          </div>
        </el-menu>
      </el-footer>
    </el-container>
  </el-container>
</template>

<script setup>
import {computed, reactive, ref} from "vue";
import {useRouter} from "vue-router";
import {ArrowRight} from "@element-plus/icons-vue";

const router = useRouter()

const title = ref(import.meta.env.VITE_APP_TITLE || '后台管理系统')
const menu = ref('/index')
const menuList = ref(router.options.routes || [])
const user = reactive(JSON.parse(localStorage.getItem('user')) || {
  avatar: ''
})
const avatar = computed(() => import.meta.env.VITE_APP_BASE_API + user.avatar)

const handleClickMenu = (item) => {
  console.log(item)
}

const toFrontend = () => {
  router.push('/')
}

const toCenter = () => {
  router.push('/center')
}

const handleLogout = () => {
  localStorage.clear()
  router.push('/login')
}
</script>

<style lang="scss" scoped>
.el-container {
  height: 100%;

  .el-aside {
    width: 256px;
    height: 100%;

    .el-menu {
      height: 100%;

      .title {
        font-family: '微软雅黑', sans-serif;
        font-size: 26px;
        font-weight: bolder;
        height: 60px;
        line-height: 60px;
        text-align: center;
      }
    }

    &::-webkit-scrollbar {
      display: none;
    }
  }

  .el-container {
    .el-header {
      box-shadow: 0 0 12px rgba(0, 0, 0, 0.12);
      display: flex;
      align-items: center;
      justify-content: space-between;

      .el-dropdown {
        .header-dropdown {
          display: flex;
          align-items: center;
          user-select: none;

          .header-username {
            margin-left: 10px;
            margin-right: 10px;
          }
        }
      }
    }

    .el-main {
      background-color: #F3F8FF;
      padding: 16px;
    }
  }
}
</style>
