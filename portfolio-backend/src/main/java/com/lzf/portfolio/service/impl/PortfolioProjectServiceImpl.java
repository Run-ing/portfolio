package com.lzf.portfolio.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzf.portfolio.dto.PortfolioProjectRequest;
import com.lzf.portfolio.entity.PortfolioProject;
import com.lzf.portfolio.enums.ProjectStatus;
import com.lzf.portfolio.mapper.PortfolioProjectMapper;
import com.lzf.portfolio.service.PortfolioProjectService;
import com.lzf.portfolio.vo.PortfolioProjectVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 作品业务服务实现。
 *
 * <p>负责数据库实体、管理端请求参数、前台响应对象之间的转换。</p>
 */
@Service
public class PortfolioProjectServiceImpl implements PortfolioProjectService {

    private final PortfolioProjectMapper mapper;

    public PortfolioProjectServiceImpl(PortfolioProjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 查询前台可见作品：
     * 1. 过滤已下线作品；
     * 2. 推荐作品优先；
     * 3. 排序值越大越靠前；
     * 4. 最后按创建时间倒序兜底。
     */
    @Override
    public List<PortfolioProjectVO> listVisibleProjects() {
        return mapper.selectList(new LambdaQueryWrapper<PortfolioProject>()
                        .ne(PortfolioProject::getStatus, ProjectStatus.OFFLINE)
                        .orderByDesc(PortfolioProject::getRecommend)
                        .orderByDesc(PortfolioProject::getSortOrder)
                        .orderByDesc(PortfolioProject::getCreatedAt))
                .stream()
                .map(this::toVO)
                .toList();
    }

    /**
     * 查询作品详情，不存在时抛出异常，由全局异常处理器转换为 404 响应。
     */
    @Override
    public PortfolioProjectVO getProject(Long id) {
        return toVO(findById(id));
    }

    /**
     * 创建作品。事务保证写入和后续查询处于同一个业务操作中。
     */
    @Override
    @Transactional
    public PortfolioProjectVO createProject(PortfolioProjectRequest request) {
        PortfolioProject project = new PortfolioProject();
        applyRequest(project, request);
        mapper.insert(project);
        return getProject(project.getId());
    }

    /**
     * 更新作品。先确认作品存在，再覆盖请求中的业务字段。
     */
    @Override
    @Transactional
    public PortfolioProjectVO updateProject(Long id, PortfolioProjectRequest request) {
        PortfolioProject project = findById(id);
        applyRequest(project, request);
        mapper.updateById(project);
        return getProject(id);
    }

    /**
     * 删除作品。第一版直接物理删除，后续如需要审计可调整为软删除。
     */
    @Override
    @Transactional
    public void deleteProject(Long id) {
        PortfolioProject project = findById(id);
        mapper.deleteById(project.getId());
    }

    /**
     * 按 ID 查询实体，不存在时统一抛出 NoSuchElementException。
     */
    private PortfolioProject findById(Long id) {
        PortfolioProject project = mapper.selectById(id);
        if (project == null) {
            throw new NoSuchElementException("project not found: " + id);
        }
        return project;
    }

    /**
     * 把管理端请求参数写入数据库实体。
     */
    private void applyRequest(PortfolioProject project, PortfolioProjectRequest request) {
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCoverUrl(request.getCoverUrl());
        project.setTechStack(join(request.getTechStack()));
        project.setTags(join(request.getTags()));
        project.setDeployType(request.getDeployType());
        project.setProjectUrl(request.getProjectUrl());
        project.setSourceUrl(request.getSourceUrl());
        project.setStatus(request.getStatus());
        project.setRecommend(Boolean.TRUE.equals(request.getRecommend()));
        project.setSortOrder(Objects.requireNonNullElse(request.getSortOrder(), 0));
    }

    /**
     * 把数据库实体转换为前端响应对象。
     */
    private PortfolioProjectVO toVO(PortfolioProject project) {
        return new PortfolioProjectVO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCoverUrl(),
                split(project.getTechStack()),
                split(project.getTags()),
                project.getDeployType(),
                project.getProjectUrl(),
                project.getSourceUrl(),
                project.getStatus(),
                project.getRecommend(),
                project.getSortOrder(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }

    /**
     * 把接口中的数组转换成数据库保存用的逗号分隔字符串。
     */
    private String join(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        return values.stream()
                .filter(StringUtils::hasText)
                .map(String::trim)
                .distinct()
                .reduce((left, right) -> left + "," + right)
                .orElse("");
    }

    /**
     * 把数据库中的逗号分隔字符串转换成接口返回用的数组。
     */
    private List<String> split(String value) {
        if (!StringUtils.hasText(value)) {
            return List.of();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .toList();
    }
}
