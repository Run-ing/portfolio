CREATE DATABASE IF NOT EXISTS lzf_studio DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE lzf_studio;

CREATE TABLE IF NOT EXISTS portfolio_project
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(100) NOT NULL COMMENT '项目名称',
    description TEXT COMMENT '项目简介',
    cover_url VARCHAR(500) COMMENT '项目封面图片URL',
    tech_stack VARCHAR(500) COMMENT '技术栈，多个使用逗号分隔',
    tags VARCHAR(500) COMMENT '项目标签，多个使用逗号分隔',
    deploy_type VARCHAR(30) NOT NULL DEFAULT 'EXTERNAL' COMMENT '部署类型：LOCAL-当前服务器；EXTERNAL-外部地址',
    project_url VARCHAR(500) NOT NULL COMMENT '项目访问地址',
    source_url VARCHAR(500) COMMENT '项目源码地址',
    status VARCHAR(30) NOT NULL DEFAULT 'ONLINE' COMMENT '项目状态：ONLINE-运行中；DEVELOPING-开发中；OFFLINE-已下线',
    is_recommend TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否推荐项目：0-否；1-是',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序值，越大越靠前',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_portfolio_project_status_sort (status, is_recommend, sort_order)
) COMMENT = '个人作品集-在线作品表';

INSERT INTO portfolio_project
    (name, description, cover_url, tech_stack, tags, deploy_type, project_url, source_url, status, is_recommend, sort_order)
VALUES
    ('AI 聊天助手', '基于大模型的在线聊天工具，支持多轮对话和提示词模板。', '/covers/ai-chat.png', 'Vue3,Spring Boot,MySQL', 'AI,工具,全栈', 'EXTERNAL', 'https://example.com/chat', 'https://github.com/example/ai-chat', 'ONLINE', 1, 30),
    ('数据看板', '面向日常运营指标的轻量级可视化看板。', '/covers/dashboard.png', 'Vue3,ECharts,Spring Boot', '数据,可视化,工具', 'LOCAL', '/apps/dashboard', NULL, 'ONLINE', 1, 20),
    ('接口管理器', '用于沉淀接口文档、调试记录和环境变量的内部工具。', '/covers/api-manager.png', 'TypeScript,Spring Boot,MyBatis-Plus', '后端,效率,工具', 'EXTERNAL', 'https://example.com/api-manager', NULL, 'DEVELOPING', 0, 10);
