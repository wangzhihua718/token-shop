-- Token Shop 数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS token_shop DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE token_shop;

-- 用户表
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    password VARCHAR(255) NOT NULL COMMENT '密码(加密)',
    avatar VARCHAR(500) COMMENT '头像URL',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用 1-正常',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- Token分类表
CREATE TABLE IF NOT EXISTS t_token_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父级ID',
    sort INT DEFAULT 0 COMMENT '排序',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Token分类表';

-- Token商品表
CREATE TABLE IF NOT EXISTS t_token_product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '商品ID',
    name VARCHAR(100) NOT NULL COMMENT '商品名称',
    description TEXT COMMENT '商品描述',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存',
    category_id BIGINT COMMENT '分类ID',
    seller_id BIGINT COMMENT '卖家ID',
    image VARCHAR(500) COMMENT '商品图片',
    status TINYINT DEFAULT 0 COMMENT '状态: 0-下架 1-上架 2-审核中',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category (category_id),
    INDEX idx_seller (seller_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Token商品表';

-- 订单表
CREATE TABLE IF NOT EXISTS t_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    order_no VARCHAR(32) NOT NULL UNIQUE COMMENT '订单号',
    buyer_id BIGINT NOT NULL COMMENT '买家ID',
    seller_id BIGINT NOT NULL COMMENT '卖家ID',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '总金额',
    status TINYINT DEFAULT 0 COMMENT '状态: 0-待支付 1-已支付 2-已发货 3-已完成 4-已取消',
    pay_expire_time DATETIME COMMENT '支付过期时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_buyer (buyer_id),
    INDEX idx_seller (seller_id),
    INDEX idx_order_no (order_no),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单明细表
CREATE TABLE IF NOT EXISTS t_order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '明细ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    token_id BIGINT NOT NULL COMMENT 'Token商品ID',
    token_name VARCHAR(100) COMMENT 'Token名称',
    quantity INT NOT NULL COMMENT '数量',
    unit_price DECIMAL(10,2) NOT NULL COMMENT '单价',
    subtotal DECIMAL(10,2) NOT NULL COMMENT '小计',
    INDEX idx_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- 钱包表
CREATE TABLE IF NOT EXISTS t_wallet (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '钱包ID',
    user_id BIGINT NOT NULL UNIQUE COMMENT '用户ID',
    balance DECIMAL(10,2) DEFAULT 0.00 COMMENT '余额',
    frozen_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '冻结金额',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-冻结 1-正常',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='钱包表';

-- 交易记录表
CREATE TABLE IF NOT EXISTS t_transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    type TINYINT NOT NULL COMMENT '类型: 1-充值 2-消费 3-提现 4-退款',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    balance DECIMAL(10,2) NOT NULL COMMENT '交易后余额',
    description VARCHAR(255) COMMENT '描述',
    order_no VARCHAR(32) COMMENT '关联订单号',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user (user_id),
    INDEX idx_order (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易记录表';

-- 插入初始分类数据
INSERT INTO t_token_category (name, parent_id, sort) VALUES
('通用Token', 0, 1),
('API Token', 0, 2),
('会员Token', 0, 3),
('数据Token', 0, 4);

-- 插入测试用户数据 (密码: 123456)
INSERT INTO t_user (username, phone, email, password, status) VALUES
('admin', '13800138000', 'admin@tokenshop.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1),
('testuser', '13800138001', 'test@tokenshop.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1);

-- 插入测试Token商品数据
INSERT INTO t_token_product (name, description, price, stock, category_id, seller_id, status) VALUES
('API调用Token - 1000次', '包含1000次API调用次数，适用于开发测试', 99.00, 100, 2, 1, 1),
('会员Token - 月度', '月度会员专属Token，享受会员特权', 19.90, 999, 3, 1, 1),
('数据访问Token - 标准版', '标准版数据访问权限，有效期30天', 49.00, 50, 1, 1, 1);

-- 购物车表
CREATE TABLE IF NOT EXISTS t_cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '购物车ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    token_id BIGINT NOT NULL COMMENT 'Token商品ID',
    token_name VARCHAR(100) COMMENT 'Token名称',
    token_image VARCHAR(500) COMMENT 'Token图片',
    price DECIMAL(10,2) NOT NULL COMMENT '单价',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    selected TINYINT DEFAULT 1 COMMENT '是否选中: 0-未选中 1-选中',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user (user_id),
    INDEX idx_token (token_id),
    UNIQUE KEY uk_user_token (user_id, token_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 收藏表
CREATE TABLE IF NOT EXISTS t_favorite (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    token_id BIGINT NOT NULL COMMENT 'Token商品ID',
    token_name VARCHAR(100) COMMENT 'Token名称',
    token_image VARCHAR(500) COMMENT 'Token图片',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user (user_id),
    INDEX idx_token (token_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 评价表
CREATE TABLE IF NOT EXISTS t_review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评价ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    username VARCHAR(50) COMMENT '用户名',
    token_id BIGINT NOT NULL COMMENT 'Token商品ID',
    token_name VARCHAR(100) COMMENT 'Token名称',
    rating INT NOT NULL COMMENT '评分: 1-5',
    content TEXT COMMENT '评价内容',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user (user_id),
    INDEX idx_token (token_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

-- 插入测试钱包数据
INSERT INTO t_wallet (user_id, balance) VALUES
(1, 1000.00),
(2, 500.00);

-- 插入测试评价数据
INSERT INTO t_review (user_id, username, token_id, token_name, rating, content) VALUES
(2, 'testuser', 1, 'API调用Token - 1000次', 5, '非常好用的Token，API响应速度快，文档齐全，强烈推荐！'),
(2, 'testuser', 2, '会员Token - 月度', 4, '性价比很高，适合开发测试使用。');
