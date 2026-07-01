import request from '@/utils/request'

// 获取钱包信息
export function getWalletInfo() {
  return request({
    url: '/wallet',
    method: 'get'
  })
}

// 钱包充值
export function recharge(data: {
  amount: number
  paymentMethod: string
}) {
  return request({
    url: '/wallet/recharge',
    method: 'post',
    data
  })
}

// 钱包提现
export function withdraw(data: {
  amount: number
  bankCard: string
}) {
  return request({
    url: '/wallet/withdraw',
    method: 'post',
    data
  })
}

// 获取交易记录
export function getTransactionList(params: {
  page?: number
  size?: number
  type?: number
}) {
  return request({
    url: '/wallet/transactions',
    method: 'get',
    params
  })
}
