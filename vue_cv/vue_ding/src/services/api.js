import axios from 'axios'

// 创建 axios 实例
const api = axios.create({
    baseURL: 'http://localhost:8080/user',
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// 响应拦截器
api.interceptors.response.use(
    response => {
        return response.data
    },
    error => {
        console.error('API 请求错误:', error)
        return Promise.reject(error)
    }
)

// 用户相关接口
export const userApi = {
    // 获取所有用户
    getUsers() {
        return api.get('/selectAll')
    },

    // 获取单个用户
    getUser(id) {
        return api.get(`/select/${id}`)
    },

    // 创建用户
    createUser(data) {
        return api.post('/create', data)
    },

    // 更新用户
    updateUser(id, data) {
        return api.put(`/update/${id}`, data)
    },

    // 删除用户
    deleteUser(id) {
        return api.delete(`/delete/${id}`)
    },

    // 搜索用户
    searchUsers(params) {
        return api.get('/search', { params })
    }
}

export default api
