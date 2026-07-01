# 架构设计文档 - Token Shop

> **版本**: v1.3.0  
> **更新日期**: 2026-06-30

## 1. 系统架构概述

### 1.1 架构风格
采用分层架构模式，自底向上分为：
- **基础设施层(Infrastructure)**: 数据库、缓存、消息队列等
- **领域层(Domain)**: 核心业务逻辑和领域模型
- **应用层(Application)**: 应用服务、用例实现
- **接口层(Interface)**: RESTful API、前端界面

### 1.2 技术架构图

```
┌─────────────────────────────────────────────────────────┐
│                    前端层 (Vue3 + Element Plus)           │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐ │
│  │  用户模块  │  │ Token模块  │  │  交易模块  │  │  钱包模块  │ │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘ │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐ │
│  │  收藏模块  │  │  评价模块  │  │ 数据看板  │  │ 管理后台  │ │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘ │
└─────────────────────────────────────────────────────────┘
                            ↕ HTTP/REST API
┌─────────────────────────────────────────────────────────┐
│                  后端层 (SpringBoot + Java8)              │
│  ┌────────────────────────────────────────────────────┐  │
│  │              接口层 (Controller)                     │  │
│  └────────────────────────────────────────────────────┘  │
│                            ↕                              │
│  ┌────────────────────────────────────────────────────┐  │
│  │              应用层 (Service)                        │  │
│  └────────────────────────────────────────────────────┘  │
│                            ↕                              │
│  ┌────────────────────────────────────────────────────┐  │
│  │              领域层 (Domain)                         │  │
│  └────────────────────────────────────────────────────┘  │
│                            ↕                              │
│  ┌────────────────────────────────────────────────────┐  │
│  │            基础设施层 (Repository)                    │  │
│  └────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────┐
│                    数据层                                 │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │  MySQL   │  │  Redis   │  │  OSS存储  │              │
│  └──────────┘  └──────────┘  └──────────┘              │
└─────────────────────────────────────────────────────────┘
```

## 2. 领域模型设计

### 2.1 核心领域实体

#### 用户领域 (User Domain)
- **用户 (User)**: 用户基本信息
  - 用户ID、用户名、手机号、邮箱、密码、头像、状态
- **用户地址 (UserAddress)**: 用户收货地址
  - 地址ID、用户ID、收货人、手机号、详细地址

#### Token领域 (Token Domain)
- **Token商品 (TokenProduct)**: Token商品信息
  - 商品ID、名称、描述、价格、库存、分类、卖家ID、状态
- **Token分类 (TokenCategory)**: Token分类信息
  - 分类ID、分类名称、父级ID、排序

#### 交易领域 (Order Domain)
- **订单 (Order)**: 订单信息
  - 订单ID、订单号、买家ID、卖家ID、总金额、状态、创建时间
- **订单明细 (OrderItem)**: 订单商品明细
  - 明细ID、订单ID、TokenID、数量、单价、小计

#### 钱包领域 (Wallet Domain)
- **钱包 (Wallet)**: 用户钱包
  - 钱包ID、用户ID、余额、冻结金额、状态
- **交易记录 (Transaction)**: 资金流水
  - 记录ID、用户ID、类型(充值/消费/提现/退款)、金额、余额、描述、时间

#### 互动领域 (Interaction Domain) - 新增
- **收藏 (Favorite)**: 用户收藏
  - 收藏ID、用户ID、TokenID、Token名称、Token图片、创建时间
- **评价 (Review)**: 用户评价
  - 评价ID、用户ID、用户名、TokenID、Token名称、评分、内容、创建时间

### 2.2 领域关系图

```
用户 (1) ────── (N) Token商品
用户 (1) ────── (1) 钱包
用户 (1) ────── (N) 订单
用户 (1) ────── (N) 收藏
用户 (1) ────── (N) 评价
订单 (1) ────── (N) 订单明细
订单明细 (N) ────── (1) Token商品
钱包 (1) ────── (N) 交易记录
收藏 (N) ────── (1) Token商品
评价 (N) ────── (1) Token商品
```

## 3. 应用模型设计

### 3.1 应用服务

#### 用户服务 (UserService)
- 用户注册
- 用户登录
- 用户信息查询
- 用户信息更新
- 实名认证

#### Token服务 (TokenService)
- 创建Token商品
- 查询Token列表 (支持搜索、筛选、排序)
- 查询Token详情
- 更新Token信息
- 下架Token商品

#### 订单服务 (OrderService)
- 创建订单
- 查询订单列表
- 查询订单详情
- 取消订单
- 确认收货

#### 钱包服务 (WalletService)
- 钱包充值
- 钱包提现
- 查询余额
- 查询交易记录

#### 支付服务 (PaymentService) - 新增
- 创建支付宝支付订单
- 创建微信支付订单
- 支付宝支付回调处理
- 微信支付回调处理

#### 收藏服务 (FavoriteService) - 新增
- 添加收藏
- 取消收藏
- 查询收藏列表

#### 评价服务 (ReviewService) - 新增
- 发表评价
- 查询评价列表
- 评价统计

### 3.2 用例图

```
┌──────────────────────────────────────────────┐
│              普通用户 (买家)                    │
│  - 浏览Token                                  │
│  - 搜索/筛选Token                              │
│  - 购买Token                                  │
│  - 收藏Token                                  │
│  - 评价Token                                  │
│  - 管理订单                                   │
│  - 管理钱包                                   │
│  - 查看数据看板                               │
└──────────────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│              卖家                              │
│  - 发布Token                                  │
│  - 管理Token                                  │
│  - 查看订单                                   │
│  - 管理钱包                                   │
│  - 查看销售数据                               │
└──────────────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│              管理员                            │
│  - 用户管理                                   │
│  - Token审核                                  │
│  - 订单管理                                   │
│  - 数据统计                                   │
│  - 系统配置                                   │
└──────────────────────────────────────────────┘
```

## 4. 基础设施模型

### 4.1 数据库设计

#### 用户表 (t_user)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR(50) | 用户名 |
| phone | VARCHAR(20) | 手机号 |
| email | VARCHAR(100) | 邮箱 |
| password | VARCHAR(255) | 密码(BCrypt加密) |
| avatar | VARCHAR(500) | 头像URL |
| status | TINYINT | 状态: 0-禁用 1-正常 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### Token商品表 (t_token_product)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(100) | 商品名称 |
| description | TEXT | 商品描述 |
| price | DECIMAL(10,2) | 价格 |
| stock | INT | 库存 |
| category_id | BIGINT | 分类ID |
| seller_id | BIGINT | 卖家ID |
| image | VARCHAR(500) | 商品图片 |
| status | TINYINT | 状态: 0-下架 1-上架 2-审核中 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### 订单表 (t_order)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| order_no | VARCHAR(32) | 订单号 |
| buyer_id | BIGINT | 买家ID |
| seller_id | BIGINT | 卖家ID |
| total_amount | DECIMAL(10,2) | 总金额 |
| status | TINYINT | 状态: 0-待支付 1-已支付 2-已发货 3-已完成 4-已取消 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### 订单明细表 (t_order_item)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| order_id | BIGINT | 订单ID |
| token_id | BIGINT | Token商品ID |
| token_name | VARCHAR(100) | Token名称 |
| quantity | INT | 数量 |
| unit_price | DECIMAL(10,2) | 单价 |
| subtotal | DECIMAL(10,2) | 小计 |

#### 钱包表 (t_wallet)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID |
| balance | DECIMAL(10,2) | 余额 |
| frozen_amount | DECIMAL(10,2) | 冻结金额 |
| status | TINYINT | 状态: 0-冻结 1-正常 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### 交易记录表 (t_transaction)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID |
| type | TINYINT | 类型: 1-充值 2-消费 3-提现 4-退款 |
| amount | DECIMAL(10,2) | 金额 |
| balance | DECIMAL(10,2) | 交易后余额 |
| description | VARCHAR(255) | 描述 |
| order_no | VARCHAR(32) | 关联订单号 |
| created_at | DATETIME | 创建时间 |

#### 收藏表 (t_favorite) - 新增
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID |
| token_id | BIGINT | Token商品ID |
| token_name | VARCHAR(100) | Token名称 |
| token_image | VARCHAR(500) | Token图片 |
| created_at | DATETIME | 创建时间 |

#### 评价表 (t_review) - 新增
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID |
| username | VARCHAR(50) | 用户名 |
| token_id | BIGINT | Token商品ID |
| token_name | VARCHAR(100) | Token名称 |
| rating | INT | 评分: 1-5 |
| content | TEXT | 评价内容 |
| created_at | DATETIME | 创建时间 |

### 4.2 缓存设计
- **用户信息缓存**: Redis Hash, 过期时间24小时
- **Token列表缓存**: Redis List, 过期时间1小时
- **热点Token详情缓存**: Redis Hash, 过期时间30分钟

### 4.3 API设计规范

#### RESTful API规范
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/v1/users | 用户注册 |
| POST | /api/v1/users/login | 用户登录 |
| GET | /api/v1/users/profile | 获取用户信息 |
| GET | /api/v1/tokens | 查询Token列表 |
| GET | /api/v1/tokens/{id} | 查询Token详情 |
| POST | /api/v1/tokens | 创建Token |
| PUT | /api/v1/tokens/{id} | 更新Token |
| PUT | /api/v1/tokens/{id}/offline | 下架Token |
| GET | /api/v1/orders | 查询订单列表 |
| GET | /api/v1/orders/{id} | 查询订单详情 |
| POST | /api/v1/orders | 创建订单 |
| PUT | /api/v1/orders/{id}/cancel | 取消订单 |
| GET | /api/v1/wallet | 获取钱包信息 |
| POST | /api/v1/wallet/recharge | 钱包充值 |
| POST | /api/v1/wallet/withdraw | 钱包提现 |
| GET | /api/v1/wallet/transactions | 查询交易记录 |
| POST | /api/v1/payment/create | 创建支付订单 |
| POST | /api/v1/payment/alipay/notify | 支付宝回调 |
| POST | /api/v1/payment/wechat/notify | 微信回调 |

#### 统一响应格式
```json
{
  "code": 200,
  "message": "成功",
  "data": {},
  "timestamp": 1234567890
}
```

#### 错误码规范
| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |
| 1001 | 用户不存在 |
| 1002 | 密码错误 |
| 2001 | Token不存在 |
| 2002 | 库存不足 |
| 3001 | 订单不存在 |
| 4001 | 余额不足 |

## 5. 安全设计

### 5.1 认证与授权
- 使用JWT (JSON Web Token) 进行身份认证 (版本 0.11.5)
- 基于RBAC (Role-Based Access Control) 的权限控制
- 角色: 普通用户、卖家、管理员

### 5.2 数据安全
- 密码使用Spring Security BCrypt加密存储
- 敏感数据传输使用HTTPS
- 数据库敏感字段加密存储

### 5.3 接口安全
- 接口限流防刷
- SQL注入防护
- XSS攻击防护
- CSRF令牌验证

## 6. 部署架构

### 6.1 开发环境
- 前端: localhost:5173
- 后端: localhost:8080
- 数据库: localhost:3306 (token_shop)
- 缓存: localhost:6379

### 6.2 生产环境
- Nginx反向代理
- SpringBoot应用集群
- MySQL主从复制
- Redis集群
- 负载均衡

## 7. 版本更新记录

### v1.2.0 (2026-06-30)
- 新增支付服务 (PaymentService)
- 新增支付宝支付 API 接口
- 新增微信支付 API 接口
- 新增支付回调处理
- 新增二次元主题 UI 设计
- 新增星空背景动画
- 新增毛玻璃卡片效果
- 项目文件夹重命名为 token-shop

### v1.1.0 (2026-06-30)
- 新增收藏功能 (Favorite)
- 新增评价功能 (Review)
- 新增数据看板页面
- 优化首页布局
- JWT升级到0.11.5
- 密码加密改用Spring Security BCrypt

### v1.0.0 (2026-06-29)
- 初始版本
- 用户、Token、订单、钱包核心功能
