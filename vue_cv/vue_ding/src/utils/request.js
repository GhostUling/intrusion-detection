import axios from "axios";

//创建一个axios对象
const request = axios.create({
  baseURL: 'http://localhost:8080',
  //timeout: 5000
})

/* request 拦截器
可以在请求发送前对请求做一些处理
比如统一加token，对请求参数统一加密 */
request.interceptors.request.use(config => {
  config.headers['Content-Type'] = 'application/json;charset=utf-8';
  return config
},error =>{
  return Promise.reject(error)
});

//response拦截器：可以在接口响应后统一处理结果
request.interceptors.response.use(response =>{
  //response.data即为后端返回的Result
  let res = response.data
  if(typeof res ==='string'){
    res = res?JSON.parse(res):res
  }
  return res;
},
  error =>{
    console.log('err'+error);
    return Promise.reject(error)
  }
 )
export default request