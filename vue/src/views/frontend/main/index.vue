<template>
  <div class="background-image-container">
    <el-row :gutter="10">
      <el-col :lg="6" :md="6" :sm="24" :xl="6" :xs="24">
        <el-card style="text-align: center">
          <el-avatar :src="getAvatar(detail.avatar)" size="large"></el-avatar>
          <h2>{{ detail.name || detail.username }}</h2>
          <span>
            {{ detail.remark || '这个人很懒，还没有添加简介' }}
          </span>
          <div v-if="user.id !== detail.id">
            <el-button :type="isAttention(detail.id) ? '' : 'success'" @click="handleAttention(detail.id)">
              {{ isAttention(detail.id) ? '取关' : '关注' }}
            </el-button>
          </div>
          <el-statistic :value="blogList.length" style="margin-top: 80px" title="文章"/>
          <el-divider/>
          <el-statistic :value="followerList.length" title="关注数"/>
          <el-divider/>
          <el-statistic :value="followedList.length" title="被关注数"/>
        </el-card>
      </el-col>
      <el-col :lg="4" :md="4" :sm="24" :xl="4" :xs="24">
        <el-card>
          <div v-for="item in followerList" :key="item.id" style="cursor: pointer; margin-bottom: 10px;"
               @click="selectChat(item.followed)">
            <el-avatar :src="getAvatar(item.followed.avatar)"/>
            <span style="margin-right: 10px">{{ item.followed.name || item.followed.username }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :lg="14" :md="14" :sm="24" :xl="14" :xs="24">
        <el-card>
          <template #header>
            {{ chatUser.name || chatUser.username }}
          </template>
          <p>
            <div v-for="msg in chatMessages" :key="msg.id" class="message-container">
              <div v-if="msg.senderId !== user.id" class="message-left">
                <el-avatar :src="getAvatar(msg.sender.avatar)"/>
                <span class="message-content">{{ msg.message }}</span>
              </div>
              <div v-else class="message-right">
                <el-avatar :src="getAvatar(msg.sender.avatar)"/>
                <span class="message-content">{{ msg.message }}</span>
              </div>
            </div>
          </p>
          <el-input v-model="newMessage" placeholder="输入消息" @keyup.enter="sendMessage"></el-input>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>

import {getUserOne} from "@/api/user.js";
import {getChatList, saveChat} from "@/api/chat.js";
import {getFollowList, removeFollowBatchByIds, saveFollow} from "@/api/follow.js";
import {getBlogList} from "@/api/blog.js";

export default {
  name: "MainView",
  data () {
    return {
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      queryParams: {
        id: this.$route.params.id
      },
      detail: {},
      blogList: [],
      followerList: [],
      followedList: [],
      chatList: [],
      selectedChatUserId: null,
      chatUser: {},
      newMessage: ''
    }
  },
  watch: {
    '$route.params.id': 'getDetail'
  },
  created () {
    this.getDetail()
    setInterval(() => {
      getChatList({}).then(res => {
        this.chatList = res.data || []
      })
    }, 5000)
  },
  computed: {
    getUrl () {
      return (path) => {
        return import.meta.env.VITE_APP_BASE_API + path
      }
    },
    getAvatar () {
      return (path) => {
        return path ? this.getUrl(path) : (path ? this.getUrl(path) : new URL('@/assets/imgs/profile.png', import.meta.url))
      }
    },
    filteredChatList () {
      return this.chatList.filter(chat => chat.senderId === this.user.id || chat.receiverId === this.user.id);
    },
    chatMessages () {
      return this.chatList.filter(chat =>
          (chat.senderId === this.user.id && chat.receiverId === this.chatUser.id) ||
          (chat.senderId === this.chatUser.id && chat.receiverId === this.user.id)
      )
    },
    isAttention () {
      return (id) => {
        return this.followerList.find(i => i.followedId === id)
      }
    }
  },
  methods: {
    getDetail () {
      this.queryParams.id = this.$route.params.id
      getUserOne(this.queryParams).then(res => {
        this.detail = res.data
      })
      getBlogList({
        userId: this.user.id
      }).then(res => {
        this.blogList = res.data || []
      })
      getFollowList({
        followerId: this.user.id
      }).then(res => {
        this.followerList = res.data || []
      })
      getFollowList({
        followedId: this.user.id
      }).then(res => {
        this.followedList = res.data || []
      })
      getChatList({}).then(res => {
        this.chatList = res.data || []
      })
    },
    selectChat(chat) {
      this.chatUser = chat
    },
    sendMessage() {
      if (!this.newMessage.trim()) return;
      const params = {
        receiverId: this.chatUser.id,
        message: this.newMessage
      }
      saveChat(params).then(res => {
        this.newMessage = ''
      }).finally(() => {
        getChatList({}).then(res => {
          this.chatList = res.data || []
        })
      })
    },
    handleAttention(id) {
      const attentionId = this.isAttention(id)?.id
      attentionId ? removeFollowBatchByIds(attentionId).then(res => {
        this.$message.success('取关成功！')
      }).finally(() => {
        this.getDetail()
      }) : saveFollow({followedId: this.detail.id}).then(res => {
        this.$message.success('关注成功！')
      }).finally(() => {
        this.getDetail()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.background-image-container {
  background-image: url('@/assets/imgs/xnwkj.webp');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  min-height: 100vh;
  padding: 20px;
}

.el-card {
  margin: 10px 10px;
}

:deep(.el-card__header) {
  text-align: center;
}

.message-container {
  display: flex;
  margin-bottom: 10px;
}

.message-left {
  background-color: #f2f2f2;
  padding: 10px;
  border-radius: 10px;
  max-width: 60%;
  display: flex;
  align-items: center;
}

.message-right {
  background-color: #1a7cff;
  color: white;
  padding: 10px;
  border-radius: 10px;
  max-width: 60%;
  margin-left: auto;
  display: flex;
  align-items: center;
}

.message-content {
  margin-left: 10px;
}
</style>
