import axios from "axios";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

// const request =axios.create({
//     baseURL: 'http://localhost:9090',
//     timeout:30000 //后台接口超时时间
// })

// const request = axios.create({
//     baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
//     timeout: 30000
// })


const request = axios.create({
    baseURL: '/api', // ✅ 確保有加上 /api 前綴
    timeout: 30000,
})


//request 拦截器
//可以自请求发送前对请求做一些处理
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    const user = localStorage.getItem("login-user");
    if(user) {
        config.headers['token'] = JSON.parse(user).token;
    }

    return config
}, error =>{
    return Promise.reject(error)
});

//response 拦截器
//可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        //兼容服務斷返回的字符串數據
        if (typeof res == 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    error => {
        if (error.response.status === 404) {
            ElMessage.error('未找到请求接口')
        } else if (error.response.status === 500) {
            ElMessage.error('系统异常，请查看后端控制台报错')
        } else {
            console.error(error.message)
        }
        return Promise.reject(error)
    }
)

export default request
