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
        action="/api/upload"
        :on-success="handleUploadSuccess"
        :before-upload="beforeUpload"
        :show-file-list="false"
        :auto-upload="false"
    >
      <input type="file" style="display: none" @change="handleFileChange">
    </el-upload>
    <div v-if="recognitionResult" class="result-display">
      <p>识别结果: {{ recognitionResult }}</p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      recognitionResult: null
    };
  },
  methods: {
    openUpload() {
      console.log('openUpload method called');
      this.$refs.upload.$el.querySelector('input[type="file"]').click();
    },
    handleFileChange(event) {
      console.log('handleFileChange method called');
      this.$refs.upload.submit(event.target.files[0]);
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