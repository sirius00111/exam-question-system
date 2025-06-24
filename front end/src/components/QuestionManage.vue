<template>
  <div class="question-manage-body">
    <div class="toolbar">
      <el-button type="success" @click="handleAddQuestion">新增题目</el-button>
      <el-button type="danger" @click="handleDeleteSelectedQuestions">批量删除</el-button>
      <el-input
        class="searchbox"
        v-model="questionInformation"
        style="width: 240px"
        placeholder="请输入题目信息"
      />
      <el-button type="primary" @click="handleSearchQuestions">搜索题目</el-button>
      <el-select
        class="question-selector"
        v-model="questionTypeValue"
        placeholder="题目类型"
        style="width: 100px"
        @change="handleQuestionTypeChange"
      >
        <el-option
          v-for="item in questionTypeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select
        class="question-selector"
        v-model="questionDifficultyValue"
        placeholder="难度选择"
        style="width: 100px"
        @change="handleQuestionDifficultyChange"
      >
        <el-option
          v-for="item in questionDifficultyOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </div>
    <el-table
      ref="questionTable"
      :data="questionListData"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50" />
      <el-table-column prop="uid" label="题号" width="60" />
      <el-table-column prop="type" label="题目类型" width="80">
        <template #default="scope">
          <!-- 根据题目类型设置不同颜色的标签 -->
          <el-tag v-if="scope.row.type === 'single_choice'" type="primary" effect="light">单选题</el-tag>
          <el-tag v-else-if="scope.row.type === 'multiple_choice'" type="primary" effect="dark">多选题</el-tag>
          <el-tag v-else-if="scope.row.type === 'true_false'" type="primary" effect="plain">判断题</el-tag>
          <el-tag v-else-if="scope.row.type === 'short_answer'" type="success" effect="dark">简答题</el-tag>
          <el-tag v-else>{{ scope.row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="题干内容" width="200" show-overflow-tooltip />
      <el-table-column prop="options" label="选项" width="180" show-overflow-tooltip />
      <el-table-column prop="answer" label="答案" width="100" show-overflow-tooltip />
      <el-table-column prop="explanation" label="解析" width="180" show-overflow-tooltip />
      <el-table-column prop="difficulty" label="难度" width="100">
        <template #default="scope">
          <!-- 根据难度设置不同颜色的标签 -->
          <el-tag v-if="scope.row.difficulty === 1" type="success">easy</el-tag>
          <el-tag v-else-if="scope.row.difficulty === 2" type="warning">middle</el-tag>
          <el-tag v-else-if="scope.row.difficulty === 3" type="danger">hard</el-tag>
          <el-tag v-else>{{ scope.row.difficulty }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="tags" label="标签" width="100" show-overflow-tooltip />
      <el-table-column prop="createdAt" label="创建时间" width="120" show-overflow-tooltip />
      <el-table-column prop="updatedAt" label="更新时间" width="120" show-overflow-tooltip />
      <el-table-column label="操作" width="140">
        <template #default="scope">
          <el-button
            size="small"
            text
            type="primary"
            @click="handleEditQuestion(scope.$index, scope.row)"
          >编辑</el-button>
          <el-button
            size="small"
            text
            type="danger"
            @click="handleDeleteQuestion(scope.$index, scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPageNumber"
        v-model:page-size="pageSize"
        :page-sizes="[10, 15, 20]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="questionTotalCount"
        @size-change="handlePageSizeChange"
        @current-change="handleCurrentPageNumberChange"
      />
    </div>
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500"
      :close-on-click-modal="false"
      :show-close="false"
      :close-on-press-escape="false"
    >
      <el-form :model="questionTemp" label-width="auto">
        <el-form-item label="题目类型">
          <el-select
            v-model="questionTemp.type"
            placeholder="请选择题目类型"
            :disabled="dialogTitle === '编辑题目'"
            @change="handleTypeChangedWhenAdd"
          >
            <el-option label="单选题" value="single_choice" />
            <el-option label="多选题" value="multiple_choice" />
            <el-option label="判断题" value="true_false" />
            <el-option label="简答题" value="short_answer" />
          </el-select>
        </el-form-item>
        <el-form-item label="题目内容">
          <el-input v-model="questionTemp.description" type="textarea" placeholder="请输入题目内容" />
        </el-form-item>
        <el-form-item
          label="选项A"
          v-if="questionTemp.type=='single_choice'||questionTemp.type=='multiple_choice'"
        >
          <el-input v-model="optionATemp" placeholder="请输入选项信息" />
        </el-form-item>
        <el-form-item
          label="选项B"
          v-if="questionTemp.type=='single_choice'||questionTemp.type=='multiple_choice'"
        >
          <el-input v-model="optionBTemp" placeholder="请输入选项信息" />
        </el-form-item>
        <el-form-item
          label="选项C"
          v-if="questionTemp.type=='single_choice'||questionTemp.type=='multiple_choice'"
        >
          <el-input v-model="optionCTemp" placeholder="请输入选项信息" />
        </el-form-item>
        <el-form-item
          label="选项D"
          v-if="questionTemp.type=='single_choice'||questionTemp.type=='multiple_choice'"
        >
          <el-input v-model="optionDTemp" placeholder="请输入选项信息" />
        </el-form-item>
        <el-form-item label="答案">
          <el-input
            v-model="questionTemp.answer"
            v-if="questionTemp.type=='short_answer'"
            placeholder="请提供题目答案"
            type="textarea"
          />
          <el-select
            v-model="questionTemp.answer"
            v-if="questionTemp.type=='true_false'"
            placeholder="请提供题目答案"
          >
            <el-option label="false" value="false" />
            <el-option label="true" value="true" />
          </el-select>
          <el-radio-group v-model="questionTemp.answer" v-if="questionTemp.type=='single_choice'">
            <el-radio value="A">A</el-radio>
            <el-radio value="B">B</el-radio>
            <el-radio value="C">C</el-radio>
            <el-radio value="D">D</el-radio>
          </el-radio-group>
          <el-checkbox-group
            v-model="multipleChoiceAnswer"
            v-if="questionTemp.type=='multiple_choice'"
          >
            <el-checkbox value="A" name="type">A</el-checkbox>
            <el-checkbox value="B" name="type">B</el-checkbox>
            <el-checkbox value="C" name="type">C</el-checkbox>
            <el-checkbox value="D" name="type">D</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="解析">
          <el-input v-model="questionTemp.explanation" type="textarea" placeholder="请提供题目解析" />
        </el-form-item>
        <el-form-item label="难度">
          <el-select v-model="questionTemp.difficulty" placeholder="请选择题目难度">
            <el-option label="1-easy" value="1" />
            <el-option label="2-middle" value="2" />
            <el-option label="3-hard" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="标签">
          <el-input v-model="questionTemp.tags" placeholder="请输入题目标签" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleConfirm">Confirm</el-button>
          <el-button @click="handleCancel">Cancel</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script lang="ts" name="QuestionManage" setup>
import type { Question } from "@/types/question";
import request from "@/utils/request";
import { ElMessage, ElMessageBox, type TableInstance } from "element-plus";
import { onMounted, ref } from "vue";

// 题目总数量
const questionTotalCount = ref<number>(0);
// 当前页码
const currentPageNumber = ref<number>(1);
// 每页显示的数量
const pageSize = ref<number>(10);

// 定义 questionTable 变量，用于存储 Table 实例
const questionTable = ref<TableInstance>();
// 题目列表
const questionListData = ref<Array<Question>>([]);
// 题目信息
const questionInformation = ref<string>("");
// 题目零时变量（用于修改与新增操作）
const questionTemp = ref<Question>({});
// 辅助数据结构（处理多选题的答案）
const multipleChoiceAnswer = ref<Array<string>>([]);
// 辅助数据结构（处理选择题的选项）
const optionATemp = ref<string>("");
const optionBTemp = ref<string>("");
const optionCTemp = ref<string>("");
const optionDTemp = ref<string>("");
// 题目类型选项
const questionTypeOptions = [
  { value: "single_choice", label: "单选题" },
  { value: "multiple_choice", label: "多选题" },
  { value: "true_false", label: "判断题" },
  { value: "short_answer", label: "简答题" },
  { value: "", label: "全部" },
];
// 题目类型值
const questionTypeValue = ref<string>("");
// 题目难度选项
const questionDifficultyOptions = [
  { value: 1, label: "easy" },
  { value: 2, label: "middle" },
  { value: 3, label: "hard" },
  { value: 0, label: "all" },
];
// 题目难度值
const questionDifficultyValue = ref<string>("");

// 对话框可视化
const dialogVisible = ref<boolean>(false);
// 编辑和新增公用一个对话框，以示区分
const dialogTitle = ref<string>("");

// 存储当前选中的行数据
const selectedRows = ref<Array<Question>>([]);
// 存储选中问题的 uid
const selectedUids = ref<Array<number | undefined>>([]);

// 获取数量与获取分页两个异步请求可能导致数据不一致，下为改进后的代码
// 获取数量
async function loadTotalCount() {
  request
    .get("/question/getQuestionsCount", {
      params: {
        information: questionInformation.value,
        type: questionTypeValue.value,
        difficulty: questionDifficultyValue.value,
      },
    })
    .then((res) => {
      questionTotalCount.value = res.data;
    });
}
// 获取分页数据
async function loadPageData() {
  request
    .get("/question/getQuestions", {
      params: {
        currentPageNumber: currentPageNumber.value,
        pageSize: pageSize.value,
        information: questionInformation.value,
        type: questionTypeValue.value,
        difficulty: questionDifficultyValue.value,
      },
    })
    .then((res) => {
      questionListData.value = res.data;
    });
}
// 封装为一个函数load
async function load() {
  try {
    // 先获取总数
    await loadTotalCount();
    // 再获取分页数据（基于最新的总数）
    await loadPageData();
  } catch (error) {
    console.error("加载数据失败:", error);
  }
}

// 辅助函数:字符串转换为多选题答案数组
function parseMultipleChoiceAnswer(answer: any): Array<string> {
  return answer.split(",");
}
// 辅助函数:多选题答案数组转换为字符串
function formatMultipleChoiceAnswer(answer: Array<string>): string {
  return answer.sort().join(",");
}

// 编辑题目
function handleEditQuestion(index: number, row: Question) {
  console.log("编辑操作：", index, row);
  // 使用 JSON.parse 和 JSON.stringify 进行深拷贝，确保数据的独立性
  questionTemp.value = JSON.parse(JSON.stringify(row));
  console.log("questionTemp:", questionTemp.value);
  dialogVisible.value = true;
  dialogTitle.value = "编辑题目";
  if (questionTemp.value.type === "multiple_choice") {
    multipleChoiceAnswer.value = parseMultipleChoiceAnswer(
      questionTemp.value.answer
    );
  }
  // 编辑时解析 options 数据并赋值给各个选项变量
  if (
    questionTemp.value.type === "single_choice" ||
    questionTemp.value.type === "multiple_choice"
  ) {
    if (typeof questionTemp.value.options === "string") {
      try {
        const options = JSON.parse(questionTemp.value.options as string);
        optionATemp.value = options.A || "";
        optionBTemp.value = options.B || "";
        optionCTemp.value = options.C || "";
        optionDTemp.value = options.D || "";
      } catch (error) {
        console.error("解析选项数据失败:", error);
      }
    } else {
      console.warn("questionTemp.value.options 不是字符串类型");
    }
  }
}
// 新增题目
function handleAddQuestion() {
  dialogVisible.value = true;
  dialogTitle.value = "新增题目";
  console.log("新增题目");
}
// 新增题目事切换题目类型直接清空就题目信息
function handleTypeChangedWhenAdd() {
  const typeBeforeChange = questionTemp.value.type;
  questionTemp.value = {};
  optionATemp.value = "";
  optionBTemp.value = "";
  optionCTemp.value = "";
  optionDTemp.value = "";
  questionTemp.value.type = typeBeforeChange;
  multipleChoiceAnswer.value = [];
}
// 处理提交表单事件
async function handleConfirm() {
  try {
    await ElMessageBox.confirm("确定要提交表单吗?", {
      confirmButtonText: "OK",
      cancelButtonText: "Cancel",
      showClose: false,
      closeOnClickModal: false,
      closeOnPressEscape: false,
    });
    await doneSubmit(); // 等待 doneSubmit 执行完成
    ElMessage({
      type: "success",
      message: "提交成功",
    });
    await load(); // 再执行 load
  } catch (error) {
    if (error == "cancel") {
      revokeSubmit();
      ElMessage({
        type: "error",
        message: "取消提交",
      });
    }
  }
}
// 提交表单
async function doneSubmit() {
  if (questionTemp.value.type === "multiple_choice") {
    questionTemp.value.answer = formatMultipleChoiceAnswer(
      multipleChoiceAnswer.value
    );
  }
  // 单选题或多选题时，将选项组合成 JSON 格式
  if (
    questionTemp.value.type === "single_choice" ||
    questionTemp.value.type === "multiple_choice"
  ) {
    questionTemp.value.options = JSON.stringify({
      A: optionATemp.value,
      B: optionBTemp.value,
      C: optionCTemp.value,
      D: optionDTemp.value,
    });
  }
  if (dialogTitle.value === "编辑题目") {
    await request
      .put("/question/updateQuestionByUid", questionTemp.value)
      .then((res: any) => {
        if (res.code === 200) {
          return;
        } else {
          ElMessage({
            type: "error",
            message: "操作异常,请查看后端日志",
          });
        }
      });
  } else if (dialogTitle.value === "新增题目") {
    // 2025-06-09 15:10 为统一接口改写
    const questionTempArray = <Array<Question>>[questionTemp.value];
    await request
      .post("/question/addQuestions", questionTempArray)
      .then((res: any) => {
        if (res.code === 200) {
          return;
        } else {
          ElMessage({
            type: "error",
            message: "操作异常,请查看后端日志",
          });
        }
      });
  }
  console.log("处理提交表单事件:", questionTemp.value);
  // 提交表单后，重置表单与辅助多选题答案数组
  questionTemp.value = {};
  optionATemp.value = "";
  optionBTemp.value = "";
  optionCTemp.value = "";
  optionDTemp.value = "";
  multipleChoiceAnswer.value = [];
  dialogVisible.value = false;
}
// 取消提交表单
function revokeSubmit() {
  console.log("取消提交表单事件");
}

// 处理删除表单事件
function handleCancel() {
  console.log("处理删除表单事件", questionTemp.value);
  // 取消表单后，重置表单与辅助多选题答案数组
  questionTemp.value = {};
  optionATemp.value = "";
  optionBTemp.value = "";
  optionCTemp.value = "";
  optionDTemp.value = "";
  multipleChoiceAnswer.value = [];
  dialogVisible.value = false;
}

// 处理选中项变化的事件
function handleSelectionChange(selection: Array<Question>) {
  selectedRows.value = selection;
  // 提取选中行的 uid
  selectedUids.value = selection.map((row) => row.uid);
  console.log("选中的 uid:", selectedUids.value);
}
// 删除题目
async function handleDeleteQuestion(index: number, row: Question) {
  // 清除表格的选中状态
  if (questionTable.value) {
    questionTable.value.clearSelection();
  }
  selectedRows.value = [];
  selectedUids.value = [row.uid];
  console.log("删除操作：", index, row, selectedUids.value);
  try {
    await ElMessageBox.confirm("确定要删除选中的题目吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "error",
      showClose: false,
      closeOnClickModal: false,
      closeOnPressEscape: false,
    });
    // 调用删除接口
    await request
      .delete("/question/deleteQuestionsByUidList", {
        data: selectedUids.value,
      })
      .then((res: any) => {
        if (res.code === 200) {
          ElMessage.success("删除成功");
        } else {
          ElMessage({
            type: "error",
            message: res.data,
          });
        }
      });
    selectedUids.value = [];
    load(); // 重新加载数据
  } catch (error) {
    if (error == "cancel") {
      ElMessage.error("取消删除");
    }
  }
}
// 批量删除
async function handleDeleteSelectedQuestions() {
  console.log("批量删除", selectedUids.value);
  try {
    await ElMessageBox.confirm("确定要删除选中的题目吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "error",
      showClose: false,
      closeOnClickModal: false,
      closeOnPressEscape: false,
    });
    // 调用删除接口
    await request
      .delete("/question/deleteQuestionsByUidList", {
        data: selectedUids.value,
      })
      .then((res: any) => {
        if (res.code === 200) {
          ElMessage.success("删除成功");
        } else {
          ElMessage({
            type: "error",
            message: res.data,
          });
        }
      });
    load(); // 重新加载数据
    // 清除表格的选中状态
    if (questionTable.value) {
      questionTable.value.clearSelection();
    }
    selectedRows.value = [];
    selectedUids.value = [];
  } catch (error) {
    if (error == "cancel") {
      ElMessage.error("取消删除");
    }
  }
}

// 搜索题目
function handleSearchQuestions() {
  console.log("搜索题目");
  load();
}
// 题目类型改变
function handleQuestionTypeChange() {
  console.log("题目类型改变：", questionTypeValue.value);
  load();
}
// 题目难度改变
function handleQuestionDifficultyChange() {
  console.log("题目难度改变：", questionDifficultyValue.value);
  load();
}
// 页码改变
function handleCurrentPageNumberChange() {
  console.log("页码改变：", currentPageNumber.value);
  load();
}
// 每页数量改变
function handlePageSizeChange() {
  console.log("每页数量改变：", pageSize.value);
  load();
}

// 挂载的时候加载数据
onMounted(async () => {
  load();
});
</script>

<style scoped>
.question-manage-body {
  width: 100%;
  height: 100%;
  background-color: #d8ffd1b7; /* 设置背景颜色 */
}
.toolbar {
  margin-left: 4%;
  margin-top: 0.5%;
  margin-bottom: 1%;
}
.searchbox {
  margin-left: 2%;
  margin-right: 0.5%;
}
.question-selector {
  margin-left: 0.5%;
}
.pagination {
  margin-top: 1%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>