<template>
  <div class="ai-question-set-body">
    <el-card
      v-loading="loading"
      class="ai-question-set-container"
      style="width: 400px"
      shadow="always"
    >
      <template #header>
        <div class="ai-title">
          <img src="/deepseek.svg" alt="AI 图标" class="ai-icon" />
          AI出题
        </div>
        <div class="deepseek-text">by deepseek</div>
      </template>
      <el-form :model="settingForm" label-width="auto">
        <el-form-item label="关键词">
          <el-input v-model="settingForm.keyWord" placeholder="请输入关键词（必填）" />
        </el-form-item>
        <el-form-item label="难度选择">
          <el-select v-model="settingForm.difficulty" placeholder="请选择难度">
            <el-option label="1-easy" value="1" />
            <el-option label="2-middle" value="2" />
            <el-option label="3-hard" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="数量选择">
          <el-select v-model="settingForm.number" placeholder="请选择数量">
            <el-option label="5" value="5" />
            <el-option label="10" value="10" />
            <el-option label="15" value="15" />
          </el-select>
        </el-form-item>
        <el-form-item label="类型选择">
          <el-select v-model="settingForm.type" placeholder="请选择类型">
            <el-option label="单选题" value="single_choice" />
            <el-option label="多选题" value="multiple_choice" />
            <el-option label="判断题" value="true_false" />
            <el-option label="简答题" value="short_answer" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="button-container">
          <el-button type="primary" plain @click="generateQuestions">出题</el-button>
          <el-button type="success" plain :disabled="!generated" @click="previewQuestionList">预览</el-button>
        </div>
      </template>
    </el-card>
    <el-dialog
      v-model="dialogVisible"
      :close-on-click-modal="false"
      :show-close="false"
      :close-on-press-escape="false"
    >
      <el-table
        :data="questionListData"
        ref="questionTable"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" />
        <el-table-column prop="description" label="题干内容" width="200" show-overflow-tooltip />
        <el-table-column prop="options" label="选项" width="180" show-overflow-tooltip />
        <el-table-column prop="answer" label="答案" width="100" show-overflow-tooltip />
        <el-table-column prop="explanation" label="解析" width="180" show-overflow-tooltip />
      </el-table>
      <template #footer>
        <el-button type="primary" plain @click="handleClosePreView">关闭预览</el-button>
        <el-button type="success" plain @click="handleAddSelectedQuestions">批量添加</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" name="AIQuestionSet" setup>
import type { Question } from "@/types/question";
import request from "@/utils/request";
import { ElMessage, type TableInstance } from "element-plus";
import { ref } from "vue";
// 向ai出题的表单
const settingForm = ref({
  keyWord: "", // 提示词
  difficulty: 1, // 难度选择：1-简单（默认），2-中等，3-困难
  type: "single_choice", // 题目类型：单选（默认），多选，判断，简答
  number: "5",  // 出题数目，默认五个
});

// 是否已经生成
const generated = ref(false);

// 是否加载中（从后端获取AI题目）
const loading = ref(false);
// 题目列表
const questionListData = ref<Array<Question>>([]);
// 是否显示对话框
const dialogVisible = ref(false);
// 定义 questionTable 变量，用于存储 Table 实例
const questionTable = ref<TableInstance>();
// 存储当前选中的行数据
const selectedRows = ref<Array<Question>>([]);

// 生成题目
function generateQuestions() {
  if (!settingForm.value.keyWord) {
    ElMessage.error("请输入提示词");
    return;
  } else {
    loading.value = true;
    ElMessage.warning("正在生成题目，请稍等...");
    setTimeout(() => {
      ElMessage.warning("请不要离开页面...");
    }, 2000);
    console.log(settingForm.value);
    request
      .get("/deepseek/generateQuestionsByAI", {
        params: {
          keyWord: settingForm.value.keyWord,
          difficulty: settingForm.value.difficulty,
          type: settingForm.value.type,
          number: settingForm.value.number,
        },
      })
      .then((res: any) => {
        if (res.code == 200) {
          ElMessage.success("题目生成成功,点击预览按钮以查看");
          questionListData.value = res.data;
          generated.value = true;
          loading.value = false;
        } else {
          ElMessage.error("题目生成失败,请查看后台信息");
          loading.value = false;
        }
      });
  }
}

// 预览题目
function previewQuestionList() {
    dialogVisible.value = true;
}
// 处理选中项变化的事件
function handleSelectionChange(selection: Array<Question>) {
  selectedRows.value = selection;
}
// 处理批量添加
function handleAddSelectedQuestions() {
  // 这里可以将选中的题目添加到本地题库中
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请选择要添加的题目");
    return;
  } else {
    request
      .post("/question/addQuestions", selectedRows.value)
      .then((res: any) => {
        if (res.code == 200) {
          ElMessage.success("添加成功!你可以前往题库查看");
          dialogVisible.value = false;
        } else {
          ElMessage.error("题目添加失败");
        }
      });
  }
}
// 处理关闭预览
function handleClosePreView() {
  selectedRows.value = [];
  dialogVisible.value = false;
}
</script>

<style scoped>
.ai-question-set-body {
  width: 100%;
  height: 100%;
  background-color: #d8ffd1b7; /* 设置背景颜色 */
  display: flex; /* 使用 Flexbox 布局 */
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}
.ai-title {
  font-size: 24px; /* 设置较大的字体 */
  text-align: left; /* 靠左对齐 */
}
.deepseek-text {
  font-size: 14px; /* 设置较小的字体 */
  text-align: right; /* 靠右对齐 */
}
.ai-icon {
  width: 24px;
  height: 24px;
}
.button-container {
  display: flex;
  justify-content: flex-end; /* 让子元素靠右对齐 */
  gap: 10px; /* 设置按钮之间的间距 */
}
</style>