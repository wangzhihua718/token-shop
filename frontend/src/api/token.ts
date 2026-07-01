import request from '@/utils/request'

// 获取Token列表
export function getTokenList(params: {
  page?: number
  size?: number
  categoryId?: number
  keyword?: string
  sortBy?: string
}) {
  return request({
    url: '/tokens',
    method: 'get',
    params
  })
}

// 获取Token详情
export function getTokenDetail(id: number) {
  return request({
    url: `/tokens/${id}`,
    method: 'get'
  })
}

// 创建Token
export function createToken(data: {
  name: string
  description: string
  price: number
  stock: number
  categoryId: number
}) {
  return request({
    url: '/tokens',
    method: 'post',
    data
  })
}

// 更新Token
export function updateToken(id: number, data: any) {
  return request({
    url: `/tokens/${id}`,
    method: 'put',
    data
  })
}

// 下架Token
export function offlineToken(id: number) {
  return request({
    url: `/tokens/${id}/offline`,
    method: 'put'
  })
}
