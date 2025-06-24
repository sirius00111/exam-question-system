import AIQuestionSet from "@/components/AIQuestionSet.vue";
import Home from "@/components/Home.vue";
import QuestionManage from "@/components/QuestionManage.vue";
import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: "/", 
      redirect: "home" 
    },
    { path: "/home", 
      name: "home", 
      component: Home, 
      meta: { title: "首页" } 
    },
    {
      path: "/ai-question-set",
      name: "ai-question-set",
      component: AIQuestionSet,
      meta: { title: "AI题目生成" },
    },
    {
      path: "/question-manage",
      name: "question-manage",
      component: QuestionManage,
      meta: { title: "题目管理" },
    },
  ],
});

// 页面title
router.beforeEach((to, from, next) => {
  // 由于ts的检测语法问题，所以需要使用as断言
  const title = to.meta.title as string | undefined;
  document.title = title || "考试题库后台管理系统";
  next();
});

export default router;
