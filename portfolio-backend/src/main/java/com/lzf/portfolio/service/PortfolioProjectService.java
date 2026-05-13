package com.lzf.portfolio.service;

import com.lzf.portfolio.dto.PortfolioProjectRequest;
import com.lzf.portfolio.vo.PortfolioProjectVO;

import java.util.List;

/**
 * 作品业务服务。
 */
public interface PortfolioProjectService {

    /**
     * 查询前台可见作品列表。
     */
    List<PortfolioProjectVO> listVisibleProjects();

    /**
     * 查询单个作品详情。
     */
    PortfolioProjectVO getProject(Long id);

    /**
     * 创建作品。
     */
    PortfolioProjectVO createProject(PortfolioProjectRequest request);

    /**
     * 更新作品。
     */
    PortfolioProjectVO updateProject(Long id, PortfolioProjectRequest request);

    /**
     * 删除作品。
     */
    void deleteProject(Long id);
}
