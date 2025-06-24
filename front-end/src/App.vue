<template>
  <div class="app">
    <!-- 页面的头部 -->
    <div class="header">
      <Header />
    </div>
    <!-- 页面的主体 -->
    <div class="body">
      <!-- 侧栏的选择菜单 -->
      <div class="aside">
        <Aside />
      </div>
      <!-- 用于实现页面内容的切换 -->
      <div class="main">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts" name="App">
// 导入组件
import Header from './components/Header.vue'
import Aside from './components/Aside.vue'
</script>

<style scoped>
.app {
  display: grid;
  width: 100%;
  height: 100%;
  grid-template-rows: 1fr 9fr;
}
.header {
  width: 100%;
  height: 100%; 
}
.body{
  width: 100%;
  height: 100%;
  display: grid;
  margin-top: 1.5%;
  grid-template-columns: min-content 9fr; 
}
.aside {
  width: 100%;
  height: 100%;
}
.main {
  width: 100%;
  height: 100%;
}
</style>

<style>
/* 设置 html 和 body 元素占满整个屏幕 */
html, body {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
}
</style>