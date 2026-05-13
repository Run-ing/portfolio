package com.lzf.portfolio.controller;

import com.lzf.portfolio.common.ApiResponse;
import com.lzf.portfolio.dto.PortfolioProjectRequest;
import com.lzf.portfolio.service.PortfolioProjectService;
import com.lzf.portfolio.vo.PortfolioProjectVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端作品维护接口。
 *
 * <p>第一版暂不做管理页面，通过这些接口维护作品数据。</p>
 */
@RestController
@RequestMapping("/api/admin/portfolio/projects")
public class AdminPortfolioProjectController {

    private final PortfolioProjectService service;

    public AdminPortfolioProjectController(PortfolioProjectService service) {
        this.service = service;
    }

    /**
     * 新增作品，入参会先经过 Bean Validation 校验。
     */
    @PostMapping
    public ApiResponse<PortfolioProjectVO> createProject(@Valid @RequestBody PortfolioProjectRequest request) {
        return ApiResponse.success(service.createProject(request));
    }

    /**
     * 修改指定作品的完整信息。
     */
    @PutMapping("/{id}")
    public ApiResponse<PortfolioProjectVO> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody PortfolioProjectRequest request
    ) {
        return ApiResponse.success(service.updateProject(id, request));
    }

    /**
     * 删除指定作品。
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProject(@PathVariable Long id) {
        service.deleteProject(id);
        return ApiResponse.success(null);
    }
}
