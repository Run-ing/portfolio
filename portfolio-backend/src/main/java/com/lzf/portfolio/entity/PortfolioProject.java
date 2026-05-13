package com.lzf.portfolio.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.lzf.portfolio.enums.DeployType;
import com.lzf.portfolio.enums.ProjectStatus;

import java.time.LocalDateTime;

/**
 * 作品数据库实体，对应 portfolio_project 表。
 *
 * <p>数据库中的 tech_stack 和 tags 使用逗号分隔字符串保存，接口层会转换成数组。</p>
 */
@TableName("portfolio_project")
public class PortfolioProject {

    /**
     * 主键 ID。
     */
    private Long id;

    /**
     * 作品名称。
     */
    private String name;

    /**
     * 作品简介。
     */
    private String description;

    /**
     * 封面图 URL。
     */
    private String coverUrl;

    /**
     * 技术栈，数据库中用逗号分隔保存。
     */
    private String techStack;

    /**
     * 标签，数据库中用逗号分隔保存。
     */
    private String tags;

    /**
     * 部署类型：当前服务器部署或外部地址。
     */
    private DeployType deployType;

    /**
     * 作品访问地址，前端点击访问时统一使用该字段。
     */
    private String projectUrl;

    /**
     * 源码地址，可为空。
     */
    private String sourceUrl;

    /**
     * 作品状态。
     */
    private ProjectStatus status;

    /**
     * 是否推荐。数据库字段名是 is_recommend，接口字段名保持为 recommend。
     */
    @TableField("is_recommend")
    private Boolean recommend;

    /**
     * 排序值，值越大越靠前。
     */
    private Integer sortOrder;

    /**
     * 创建时间。
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间。
     */
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getTechStack() {
        return techStack;
    }

    public void setTechStack(String techStack) {
        this.techStack = techStack;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public DeployType getDeployType() {
        return deployType;
    }

    public void setDeployType(DeployType deployType) {
        this.deployType = deployType;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
