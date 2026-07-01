import request from '@/utils/request'

// 用户注册
export function register(data: {
  username: string
  phone: string
  email: string
  password: string
}) {
  return request({
    url: '/users',
    method: 'post',
    data
  })
}

// 用户登录
export function login(data: {
  username: string
  password: string
}) {
  return request({
    url: '/users/login',
    method: 'post',
    data
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/users/profile',
    method: 'get'
  })
}

// 更新用户信息
export function updateUserInfo(data: any) {
  return request({
    url: '/users/profile',
    method: 'put',
    data
  })
}
