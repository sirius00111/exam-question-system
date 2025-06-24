<template>
  <div class="myHeader">
    <div class="chinese-text">
      <el-icon>
        <Monitor />
      </el-icon>&ensp;考试题库后台管理系统&emsp;
    </div>
    <div class="english-text">Exam Question Bank Backend Management System</div>
  </div>
</template>

<script setup name="Header" lang="ts">
</script>

<style scoped>
/* 头部标签样式 */
.myHeader {
  width: 100%;
  height: 100%;
  background-color: #63ec61;
  display: flex;
  flex-direction: column; /* 让子元素垂直排列 */
  justify-content: center;
  align-items: center;
  padding: 10px 0;
}
/* 中文文本 */
.chinese-text {
  font-family: "Microsoft YaHei", sans-serif;
  font-size: 24px;
  font-weight: bold;
  color: #333;
}
/* 英文文本 */
.english-text {
  font-family: Arial, sans-serif;
  font-size: 14px; /* 设置较小的字体大小 */
  color: #555; /* 设置英文颜色 */
  margin-top: 5px; /* 添加与中文的间距 */
}
</style>