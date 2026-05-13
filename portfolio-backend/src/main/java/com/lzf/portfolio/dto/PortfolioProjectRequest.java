package com.lzf.portfolio.dto;

import com.lzf.portfolio.enums.DeployType;
import com.lzf.portfolio.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 管理端创建或修改作品时提交的请求参数。
 */
public class PortfolioProjectRequest {

    /**
     * 作品名称，必填。
     */
    @NotBlank
    @Size(max = 100)
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
     * 技术栈。接口使用数组，服务层保存前会转换为逗号分隔字符串。
     */
    private List<String> techStack;

    /**
     * 标签。接口使用数组，服务层保存前会转换为逗号分隔字符串。
     */
    private List<String> tags;

    /**
     * 部署类型，默认外部地址。
     */
    @NotNull
    private DeployType deployType = DeployType.EXTERNAL;

    /**
     * 作品访问地址，必填。
     */
    @NotBlank
    @Size(max = 500)
    private String projectUrl;

    /**
     * 源码地址，可为空。
     */
    private String sourceUrl;

    /**
     * 作品状态，默认运行中。
     */
    @NotNull
    private ProjectStatus status = ProjectStatus.ONLINE;

    /**
     * 是否推荐展示。
     */
    private Boolean recommend = false;

    /**
     * 排序值，越大越靠前。
     */
    private Integer sortOrder = 0;

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

    public List<String> getTechStack() {
        return techStack;
    }

    public void setTechStack(List<String> techStack) {
        this.techStack = techStack;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
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
}
