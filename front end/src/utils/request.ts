// 实现一个工具类用来向后端发送请求并接受后端返回的数据
import axios from "axios"
import { ElMessage } from "element-plus"

// 创建axios实例
const request = axios.create({
    baseURL: "http://localhost:8080",    // 设置基础的后端url
    timeout: 180000    // 设置请求超时时间(支持更多题目的生成)
})

// request拦截器(在请求发送前对请求实现一些处理)
request.interceptors.request.use(
    config => {
        config.headers["Content-Type"] = "application/json;charset=utf-8";
        return config
    }, error => {
        return Promise.reject(error)
    }
)

// response拦截器(可以在接口响应后做统一的处理)
request.interceptors.response.use(
    response => {
        let res = response.data
        // 兼容服务端返回的字符串数据
        if (typeof res === "string") {
            res = res ? JSON.parse(res) : res
        }
        return res
    }, error => {
        if (error.response.status === 404) {
            ElMessage.error("未找到请求接口")
        } else if (error.response.status === 500) {
            ElMessage.error("系统异常,请查看后端服务器")
        } else {
            // 根据后端定义的返回数据类型获取错误信息
            console.error(error.message)
        }
        return Promise.reject(error)
    }
)

// 将定义的axios实例对象向外暴露出去
export default request