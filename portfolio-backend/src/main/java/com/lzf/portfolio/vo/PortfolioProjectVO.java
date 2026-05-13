package com.lzf.portfolio.vo;

import com.lzf.portfolio.enums.DeployType;
import com.lzf.portfolio.enums.ProjectStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 返回给前端的作品视图对象。
 *
 * <p>这里把数据库中的逗号分隔字段转换为数组，方便前端直接渲染技术栈和标签。</p>
 */
public record PortfolioProjectVO(
        /** 作品 ID。 */
        Long id,
        /** 作品名称。 */
        String name,
        /** 作品简介。 */
        String description,
        /** 封面图 URL。 */
        String coverUrl,
        /** 技术栈数组。 */
        List<String> techStack,
        /** 标签数组。 */
        List<String> tags,
        /** 部署类型。 */
        DeployType deployType,
        /** 作品访问地址。 */
        String projectUrl,
        /** 源码地址。 */
        String sourceUrl,
        /** 作品状态。 */
        ProjectStatus status,
        /** 是否推荐。 */
        Boolean recommend,
        /** 排序值。 */
        Integer sortOrder,
        /** 创建时间。 */
        LocalDateTime createdAt,
        /** 更新时间。 */
        LocalDateTime updatedAt
) {
}
