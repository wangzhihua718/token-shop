import request from '@/utils/request'

// 获取订单列表
export function getOrderList(params: {
  page?: number
  size?: number
  status?: number
}) {
  return request({
    url: '/orders',
    method: 'get',
    params
  })
}

// 获取订单详情
export function getOrderDetail(id: number) {
  return request({
    url: `/orders/${id}`,
    method: 'get'
  })
}

// 创建订单
export function createOrder(data: {
  tokenId: number
  quantity: number
}) {
  return request({
    url: '/orders',
    method: 'post',
    data
  })
}

// 取消订单
export function cancelOrder(id: number) {
  return request({
    url: `/orders/${id}/cancel`,
    method: 'put'
  })
}

// 确认收货
export function confirmOrder(id: number) {
  return request({
    url: `/orders/${id}/confirm`,
    method: 'put'
  })
}
