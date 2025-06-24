<template>
  <div class="myAside">
    <el-menu
      background-color="#b9f3b7"
      text-color="#0f2000"
      active-text-color="#aa1960"
      :collapse="isCollapse"
    >
      <el-menu-item index="1" @click="handleToHome">
        <el-icon>
          <HomeFilled />
        </el-icon>
        <template #title>&emsp;首页&emsp;</template>
      </el-menu-item>
      <el-menu-item index="2" @click="handleToAIQuestionSet">
        <el-icon>
          <MagicStick />
        </el-icon>
        <template #title>&emsp;ai出题&emsp;</template>
      </el-menu-item>
      <el-menu-item index="3" @click="handleToQuestionManage">
        <el-icon>
          <Files />
        </el-icon>
        <template #title>&emsp;题库管理&emsp;</template>
      </el-menu-item>
      <el-menu-item index="4" @click="toggleCollapse">
        <el-icon>
          <Fold />
        </el-icon>
        <template #title>&emsp;菜单折叠&emsp;</template>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup name="Aside" lang="ts">
import router from "@/router";
import { ref } from "vue";
// 定义折叠状态
const isCollapse = ref(false);

// 切换折叠状态的方法
function toggleCollapse() {
  isCollapse.value = !isCollapse.value;
}

// 路由跳转各个子页面
function handleToHome(){
  router.push("/home");
}
function handleToAIQuestionSet(){
  router.push("/ai-question-set"); 
}
function handleToQuestionManage(){
  router.push("/question-manage"); 
}
</script>

<style scoped>
.myAside {
  height: 100%;
  width: auto;
  background-color: #b9f3b7;
}
</style>