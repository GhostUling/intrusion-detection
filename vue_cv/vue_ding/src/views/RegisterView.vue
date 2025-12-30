<template>
    <div class="home">
        <el-row type="flex" class="row-bg" justify="center">
            <div class="bg-img">
            </div>
            <el-col :span="2">
                <div class="grid-content bg-purple"></div>
            </el-col>
            <el-col :span="10">
                <div class="register-div">
                    <!-- 注册表单 -->
                    <el-form :model="registerForm" status-icon :rules="rules" ref="registerForm" label-width="80px"
                        class="register-form register-label">
                        <div style="text-align: center">
                            <h1>
                                <pre>用户注册</pre>
                            </h1>
                        </div>
                        <el-form-item label="姓名" prop="name" class="my-input">
                            <el-input v-model="registerForm.name"></el-input>
                        </el-form-item>
                        <el-form-item label="用户名" prop="username" class="my-input">
                            <el-input v-model="registerForm.username"></el-input>
                        </el-form-item>
                        <el-form-item label="密码" prop="password" class="my-input">
                            <el-input type="password" v-model="registerForm.password" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="确认密码" prop="checkPassword" class="my-input">
                            <el-input type="password" v-model="registerForm.checkPassword" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="邮箱" prop="email" class="my-input">
                            <el-input v-model="registerForm.email"></el-input>
                        </el-form-item>
                        <el-form-item label="电话" prop="phone" class="my-input">
                            <el-input v-model="registerForm.phone"></el-input>
                        </el-form-item>
                        <el-form-item class="register-btn" label-width="0">
                            <el-button type="primary" @click="register('registerForm')">注册</el-button>
                            <el-button @click="toLogin()">返回登录</el-button>
                        </el-form-item>
                    </el-form>

                </div>
            </el-col>

        </el-row>
    </div>
</template>

<script>
// 引入请求工具
import request from '@/utils/request.js'

export default {
    data() {
        // 验证邮箱格式
        var validateEmail = (rule, value, callback) => {
            const reg = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
            if (value === '') {
                callback(new Error('邮箱不能为空'));
            } else if (!reg.test(value)) {
                callback(new Error('请输入正确的邮箱'));
            } else {
                callback();
            }
        };
        
        // 验证手机号格式
        var validatePhone = (rule, value, callback) => {
            const regTel = /^1[3-9]\d{9}$/;
            if (value === '') {
                callback(new Error('手机号不能为空'));
            } else if (!regTel.test(value)) {
                callback(new Error('请输入正确的手机号'))
            } else {
                callback()
            }
        };
        
        // 验证两次密码是否一致
        var validateCheckPassword = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入密码'));
            } else if (value !== this.registerForm.password) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };
        
        return {
            registerForm: {
                name: '',
                username: '',
                password: '',
                checkPassword: '',
                email: '',
                phone: '',
            },
            rules: {
                name: [
                    { required: true, message: '姓名不能为空', trigger: 'blur' },
                    { min: 2, max: 10, message: '长度在2到10个字符', trigger: 'blur' }
                ],
                username: [
                    { required: true, message: '用户名不能为空', trigger: 'blur' },
                    { min: 4, max: 16, message: '长度在4到16个字符', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '密码不能为空', trigger: 'blur' },
                    { min: 6, max: 16, message: '长度在6到16个字符', trigger: 'blur' }
                ],
                checkPassword: [
                    { required: true, message: '请确认密码', trigger: 'blur' },
                    { validator: validateCheckPassword, trigger: 'blur' }
                ],
                email: [
                    { required: true, message: '邮箱不能为空', trigger: 'blur' },
                    { validator: validateEmail, trigger: 'blur' }
                ],
                phone: [
                    { required: true, message: '电话不能为空', trigger: 'blur' },
                    { validator: validatePhone, trigger: 'blur' }
                ]
            }
        };
    },
    methods: {
        // 注册方法
        register(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    // 构建注册参数，移除确认密码字段
                    let params = {
                        name: this.registerForm.name,
                        username: this.registerForm.username,
                        password: this.registerForm.password,
                        email: this.registerForm.email,
                        phone: this.registerForm.phone
                    }
                    
                    // 发送注册请求
                    request.post('/user/register', params).then(res => {
                        // code为0代表注册成功
                        if (res.code == 0) {
                            this.$message.success("注册成功，即将跳转到登录页面")
                            
                            // 定时器 2s之后跳转到登录页面
                            setTimeout(() => {
                                this.$router.push('/login')
                            }, 2000)
                        } else {
                            // code不为0代表失败，弹出提示信息
                            let msg = res.msg || '注册失败，请稍后重试'
                            this.$message.error(msg)
                        }
                    }).catch(error => {
                        console.error('注册请求失败:', error)
                        this.$message.error('注册失败，请检查网络连接')
                    })
                } else {
                    this.$message.error('请完善表单信息')
                    return false;
                }
            });
        },
        
        // 跳转到登录页面
        toLogin() {
            this.$router.push('/login')
        }
    }
}
</script>

<style scoped>
.my-input {
    width: 400px;
}

button {
    width: 150px;
}

.register-btn {
    text-align: center;
}

.register-div {
    margin-top: 15%;
    width: 770px;
}

.register-form {
    text-align: center;
    width: 70%;
    background-color: rgba(255, 255, 255, 0.20);
    padding: 30px;
    border-radius: 15px;
}

.el-form-item__label {
    color: azure;
}

.bg-img {
    position: fixed;
    top: 0;
    left: 0;
    height: 100%;
    width: 100%;
    background-image: url('../assets/login.png');
    background-size: cover;
    background-position: center;
    z-index: -10;
}

.register-label .el-form-item__label {
    color: aliceblue;
}

/* 调整标题样式 */
h1 pre {
    color: white;
    font-size: 24px;
    font-family: "Microsoft YaHei", "SimHei", sans-serif;
    margin: 10px 0 20px 0;
}
</style>