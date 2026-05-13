package com.lzf.portfolio.controller;

import com.lzf.portfolio.common.ApiResponse;
import com.lzf.portfolio.service.PortfolioProjectService;
import com.lzf.portfolio.vo.PortfolioProjectVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台作品查询接口。
 *
 * <p>只负责对外展示作品，不包含管理端新增、修改、删除能力。</p>
 */
@RestController
@RequestMapping("/api/portfolio/projects")
public class PortfolioProjectController {

    private final PortfolioProjectService service;

    public PortfolioProjectController(PortfolioProjectService service) {
        this.service = service;
    }

    /**
     * 查询前台可见作品列表，下线作品由服务层过滤。
     */
    @GetMapping
    public ApiResponse<List<PortfolioProjectVO>> listProjects() {
        return ApiResponse.success(service.listVisibleProjects());
    }

    /**
     * 根据作品 ID 查询作品详情。
     */
    @GetMapping("/{id}")
    public ApiResponse<PortfolioProjectVO> getProject(@PathVariable Long id) {
        return ApiResponse.success(service.getProject(id));
    }
}
