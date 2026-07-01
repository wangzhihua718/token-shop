# Token Shop - BMAD方法驱动开发

## 项目概述
基于BMAD（Build More Architect Dreams）方法论设计的Token售卖平台，采用前后端分离架构。

> **最新版本**: v1.3.0  
> **更新日期**: 2026-06-30

## 技术栈
- **前端**: Vue3 + Element Plus + TypeScript + Vite
- **后端**: SpringBoot 2.7.18 + Java8 + MyBatis-Plus + MySQL
- **构建工具**: Maven (后端) + npm (前端)

## BMAD开发流程
1. **发现阶段(Discovery)**: 需求调研、市场分析
2. **规划阶段(Planning)**: PRD文档、��构设计、用户故事
3. **实施阶段(Implementation)**: 按故事卡逐步实现
4. **交付阶段(Delivery)**: 测试、部署、交付

## 项目结构
```
token-shop/
├── docs/                    # 文档目录
│   ├── prd/                # 产品需求文档
│   │   └── product_requirements.md
│   ├── architecture/        # 架构设计文档
│   │   └── architecture_design.md
│   ├── user-stories/        # 用户故事
│   │   └── user_stories.md
│   └── 项目总结.md
├── frontend/               # 前端项目 (Vue3 + Element Plus)
│   ├── src/
│   │   ├── api/            # API接口
│   │   ├── views/          # 页面组件
│   │   ├── stores/         # 状态管理
│   │   ├── router/         # 路由配置
│   │   └── utils/          # 工具类
│   ├── package.json
│   ├── vite.config.ts
│   └── tsconfig.json
└── backend/                # 后端项目 (SpringBoot + Java8)
    ├── src/main/java/com/tokenshop/
    │   ├── common/         # 公共类
    │   ├── config/         # 配置类
    │   ├── controller/     # 控制器
    │   ├── entity/         # 实体类
    │   ├── mapper/         # Mapper接口
    │   ├── service/        # 服务层
    │   └── util/           # 工具类
    ├── src/main/resources/
    │   ├── application.yml
    │   └── db/
    │       └── init.sql
    └── pom.xml
```

## 快速开始

### 环境要求
- JDK 8+ (实际运行环境 Java 24/25)
- Node.js 16+
- MySQL 5.7+
- Redis 5+
- Maven 3.6+

### 后端启动
```bash
cd backend
# 1. 初始化数据库
mysql -u root -p < src/main/resources/db/init.sql

# 2. 编译打包
mvn clean package -DskipTests

# 3. 启动服务
java -jar target/token-shop-backend-1.0.0.jar
```

### 前端启动
```bash
cd frontend
npm install
npm run dev
```

### 访问地址
- **前端**: http://localhost:5173
- **后端API**: http://localhost:8080
- **数据库**: localhost:3306 (token_shop)
- **Redis**: localhost:6379

## 版本更新日志

### v1.3.0 (2026-06-30)
**新增功能**:
- 全面UI重设计，采用现代毛玻璃风格
- 钱包页面全新��计，增加金额预设、支付方式选择
- 注册页面全新设计，增加动态背景装饰
- 登录页面优化，增加星空背景动画

**优化改进**:
- 修复充值接口bug，增加加载状态提示
- 所有页面统一设计风格
- 增加渐变色彩系统
- 优化表单交互体验
- 增加动态背景浮动效果

**技术变更**:
- 钱包页面增加 recharging/withdrawing 状态管理
- 表��增加输入验证和错误提示
- 支付方式选择改为可视化卡片

### v1.2.0 (2026-06-30)
**新增功能**:
- 接入支付宝支付 API (模拟实现)
- 接入微��支付 API (模拟实现)
- 支付回调处理接口
- 二次元主题 UI 设计
- 星空背景动画效果
- 毛玻璃卡片样式
- 渐变色彩系统

**优化改进**:
- 登录页面二次元风格改造
- 首页导航栏二次元风格
- 全局主题色系统 (粉色/紫色/蓝色渐变)
- 动态背景浮动效果
- 闪烁星星动画
- 项目文件夹重命名为 token-shop

**技术变更**:
- 新增 PaymentConfig 支付配置类
- 新增 PaymentService 支付服务
- 新增 PaymentController 支付控制器
- 前端全局 CSS 变量系统

### v1.1.0 (2026-06-30)
**新增功能**:
- 首页轮播图和数据统计展示
- Token收藏功能
- 用户评价系统
- 数据看板页面
- 高级搜索和筛选功能
- 卖家信息展示

**优化改进**:
- 首页布局优化，增加导航栏和页脚
- Token列表页卡片样式优化
- Token详情页增加评价和收藏
- 响应式设计适配移动端
- JWT升级到0.11.5版本
- 密码加密改用Spring Security BCrypt

**数据库变更**:
- 新增 `t_favorite` 收藏表
- 新增 `t_review` 评价表

### v1.0.0 (2026-06-29)
- 初始版本发布
- 用户注册/登录
- Token商品管理
- 订单管理
- 钱包管理
- 管理后台
