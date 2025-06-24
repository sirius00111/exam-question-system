import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// 引入element-plus（最好使用中文的）
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
// 引入element-plus icon图表
// npm install @element-plus/icons-vue
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

app.use(router)
app.use(ElementPlus,{
    locale:zhCn,
})
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.mount('#app')