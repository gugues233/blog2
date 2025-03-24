<template>
  <div class="image-recognition-page">
    <el-button
        class="camera-button"
        type="primary"
        @click="openUpload"
    >
      <el-icon><CameraFilled /></el-icon>
    </el-button>

    <el-upload
        ref="upload"
        class="upload-demo"
        :action="uploadUrl"
        :on-success="handleUploadSuccess"
        :before-upload="beforeUpload"
        :show-file-list="false"
        :auto-upload="false"
    >
      <input type="file" style="display: none" @change="handleFileChange">
<!--    </el-upload>-->
<!--    <div v-if="recognitionResult" class="result-display">-->
<!--      <p>识别结果: {{ recognitionResult }}</p>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->
    </el-upload>
    <!-- 用于显示弹窗 -->
    <el-dialog
        v-model="isDialogVisible"
        title="识别结果"
        :close-on-click-modal="false"
        width="30%"
    >
      <p>{{ recognitionResult }}</p>
      <template #footer>
        <!-- 这里不显示默认的底部按钮 -->
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus';


export default {
  data() {
    return {
      recognitionResult: null,
      isDialogVisible: false // 控制弹窗是否显示
    };
  },
  computed: {
    uploadUrl() {
      return import.meta.env.VITE_APP_BASE_API + '/image/recognition';
    }
  },
  methods: {
    openUpload() {
      console.log('openUpload method called');
      this.$refs.upload.$el.querySelector('input[type="file"]').click();
    },

    handleFileChange(event) {
      const file = event.target.files[0];
      if (!file) return;

      const formData = new FormData();
      formData.append('file', file);

      fetch(this.uploadUrl, {
        method: 'POST',
        body: formData,
      })
          .then(response => response.json())
          // .then(data => {
          //   console.log('Upload success:', data);
          //   if (data.code === 200) {
          //     this.recognitionResult = data.data.result;
          //     // 使用 ElMessage 显示识别结果
          //     ElMessage({
          //       message: `识别结果: ${this.recognitionResult}`,
          //       type: 'success'
          //     });
          //   } else {
          //     console.error('Upload error:', data.msg);
          //     ElMessage({
          //       message: `上传失败: ${data.msg}`,
          //       type: 'error'
          //     });
          //   }
          // })
          // .catch(error => {
          //   console.error('Upload error:', error);
          //   ElMessage({
          //     message: `上传发生错误: ${error.message}`,
          //     type: 'error'
          //   });
          // });

          .then(data => {
            console.log('Upload success:', data);
            if (data.code === 200) {
              this.recognitionResult = data.data.result;
              this.isDialogVisible = true; // 识别成功时显示弹窗
            } else {
              console.error('Upload error:', data.msg);
            }
          })
          .catch(error => {
            console.error('Upload error:', error);
          });
    },


    handleUploadSuccess(response, file, fileList) {
      this.recognitionResult = response.result;
    },
    beforeUpload(file) {
      const isImage = file.type.indexOf('image') === 0;
      if (!isImage) {
        this.$message.error('上传的文件必须是图片');
        return false;
      }
      return true;
    }
  }
};
</script>

<style scoped>
.image-recognition-page {
  background: url('@/assets/imgs/ImageRecognition.jpeg') no-repeat center center fixed;
  -webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.camera-button {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  font-size: 60px;
  background-color: rgba(0, 123, 255, 0.8);
  border: none;
}

.camera-button i {
  color: white;
}

.result-display {
  margin-top: 20px;
  color: white;
}
</style>