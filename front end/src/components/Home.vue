<template>
  <div class="home-body">
    <div class="home-body-left">
      <div ref="questionsTypeChartRef" style="width: 400px; height: 300px"></div>
      <div ref="questionsDifficultyChartRef" style="width: 400px; height: 300px"></div>
    </div>
    <div class="home-body-main">
      <el-scrollbar height="650px">
        <div class="markdown-body" v-html="mdContent"></div>
      </el-scrollbar>
    </div>
    <div class="home-body-right">
      <div class="tech-stack-title">技术栈</div>
      <img class="tech-stack-img" src="/src/assets/imgs/DeepSeek.png" />
      <br />
      <img class="tech-stack-img" src="/src/assets/imgs/Echarts.png" />
      <br />
      <img class="tech-stack-img" src="/src/assets/imgs/ElementPlus.png" />
      <br />
      <img class="tech-stack-img" src="/src/assets/imgs/MyBatis.png" />
      <br />
      <img class="tech-stack-img" src="/src/assets/imgs/MySQL.png" />
      <br />
      <img class="tech-stack-img" src="/src/assets/imgs/SpringAI.png" />
      <br />
      <img class="tech-stack-img" src="/src/assets/imgs/SpringBoot.png" />
      <br />
      <img class="tech-stack-img" src="/src/assets/imgs/Vue.js.png" />
      <br />
    </div>
  </div>
</template>

<script lang="ts" name="Home" setup>
// 导入 echarts 主模块（使用命名空间引入方式）
import * as echarts from "echarts";
// 导入 ECharts 类型，用于代码补全和类型检查
import type { ECharts } from "echarts";
import request from "@/utils/request";

// ---------------------------------------------
// -------------该部分由chatgpt完成--------------
// ---------------------------------------------
import { ref, onMounted } from "vue";
import { marked } from "marked";
// 可选：防止 XSS
import DOMPurify from "dompurify";
// markdown 内容
const mdContent = ref("");
// 加载 markdown 文件（vite 支持 import text 文件）
import markdownText from "@/assets/learning.md?raw";
onMounted(async () => {
  const rawHtml: any = marked(markdownText);
  // 可选：防止 XSS
  mdContent.value = DOMPurify.sanitize(rawHtml);
  // ---------------------------------------------
  // -------------该部分由chatgpt完成--------------
  // ---------------------------------------------

  const questionStats = ref({
    easyNumber: 0,
    mediumNumber: 0,
    hardNumber: 0,
    singleChoiceNumber: 0,
    multipleChoiceNumber: 0,
    trueFalseNumber: 0,
    shortAnswerNumber: 0,
  });
  // 此处可以直接使用 await，不需要再用 then
  try {
    const res:any = await request.get("/question/getQuestionStats");
    if (res.code === 200) {
      questionStats.value = res.data;
    }
  } catch (error) {
    console.error("获取题目统计信息失败:", error);
  }

  // 初始化题型统计图表
  if (questionsTypeChartRef.value) {
    // 初始化 ECharts 实例，传入 DOM 元素
    questionsTypeChart = echarts.init(questionsTypeChartRef.value);
    // 图表的配置项
    const questionsTypeOption = {
      tooltip: {
        trigger: "item", // 鼠标悬停时触发 tooltip
      },
      legend: {
        top: "5%",
        left: "center",
      },
      series: [
        {
          name: "题型数量", // 图例项名称
          type: "pie", // 图表类型为饼图
          radius: ["40%", "70%"], // 设置为环形图（内外半径）
          avoidLabelOverlap: false, // 防止标签重叠
          padAngle: 5,
          // 控制每个扇区的样式
          itemStyle: {
            borderRadius: 10, // 扇区圆角
          },
          // 标签显示设置
          label: {
            show: false,
            position: "center",
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 30,
              fontWeight: "bold",
            },
          },
          labelLine: {
            show: false,
          },
          // 饼图数据
          data: [
            { value: questionStats.value.singleChoiceNumber, name: "单选题" },
            { value: questionStats.value.multipleChoiceNumber, name: "多选题" },
            { value: questionStats.value.trueFalseNumber, name: "判断题" },
            { value: questionStats.value.shortAnswerNumber, name: "简答题" },
          ],
        },
      ],
    };
    // 使用配置项设置图表内容
    questionsTypeChart.setOption(questionsTypeOption);
  }

  // 初始化难度统计图表
  if (questionsDifficultyChartRef.value) {
    // 初始化 ECharts 实例，传入 DOM 元素
    questionsDifficultyChart = echarts.init(questionsDifficultyChartRef.value);
    // 图表的配置项
    const questionsDifficultyOption = {
      tooltip: {
        trigger: "item", // 鼠标悬停时触发 tooltip
      },
      legend: {
        top: "5%",
        left: "center",
      },
      series: [
        {
          name: "难度数量", // 图例项名称
          type: "pie", // 图表类型为饼图
          radius: ["40%", "70%"], // 设置为环形图（内外半径）
          avoidLabelOverlap: false, // 防止标签重叠
          padAngle: 5,
          // 控制每个扇区的样式
          itemStyle: {
            borderRadius: 10, // 扇区圆角
          },
          // 标签显示设置
          label: {
            show: false,
            position: "center",
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 30,
              fontWeight: "bold",
            },
          },
          labelLine: {
            show: false,
          },
          // 饼图数据
          data: [
            { value: questionStats.value.easyNumber, name: "easy" },
            { value: questionStats.value.mediumNumber, name: "middle" },
            { value: questionStats.value.hardNumber, name: "hard" },
          ],
        },
      ],
    };
    // 使用配置项设置图表内容
    questionsDifficultyChart.setOption(questionsDifficultyOption);
  }
});

// 创建 DOM 元素的引用，用于挂载图表
const questionsTypeChartRef = ref<HTMLDivElement | null>(null);
const questionsDifficultyChartRef = ref<HTMLDivElement | null>(null);

// 定义图表实例变量（用于后续操作，比如更新、销毁图表等）
let questionsTypeChart: ECharts | null = null;
let questionsDifficultyChart: ECharts | null = null;
</script>

<style scoped>
.home-body {
  width: 100%;
  height: 100%;
  background-color: #d8ffd1b7; /* 设置背景颜色 */
  display: grid;
  grid-template-columns: 1fr 2fr 1fr; /* 三列布局 */
}
/* 可选：加一点 Markdown 渲染样式 */
.markdown-body {
  line-height: 1.6;
  font-size: 16px;
  padding: 1em;
}
.markdown-body h1,
.markdown-body h2 {
  margin-top: 1em;
}
.tech-stack-img {
  max-height: 50px;
}
/* 新增技术栈标题样式 */
.tech-stack-title {
  font-size: 30px; /* 设置字体大小 */
  font-weight: bold; /* 设置字体加粗 */
  color: #000000; /* 设置字体颜色 */
  text-align: center; /* 设置文本居中 */
  margin-bottom: 15px; /* 设置底部间距 */
}
</style>