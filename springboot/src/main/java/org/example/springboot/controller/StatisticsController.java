package org.example.springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.springboot.domain.Result;
import org.example.springboot.service.IStatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 统计前端控制器
 * </p>
 */
@RestController
@RequestMapping("/statistics")
@Tag(name = "统计", description = "统计")
public class StatisticsController {
    @Resource
    private IStatisticsService statisticsService;

    /**
     * 仪表盘统计
     *
     * @return 结果
     */
    @GetMapping("/dashboard/info")
    @Operation(summary = "仪表盘统计", description = "仪表盘统计", method = "GET")
    public Result<Map<String, Long>> getDashboardInfo() {
        Map<String, Long> dashboardInfo = statisticsService.getDashboardInfo();
        return Result.success(dashboardInfo);
    }

    /**
     * 统计博客
     *
     * @param year 年份
     * @return 结果
     */
    @GetMapping("/blog")
    @Operation(summary = "统计博客", description = "统计博客", method = "GET")
    public Result<List<Map<String, Object>>> blog(@RequestParam(required = false) Integer year) {
        List<Map<String, Object>> list = statisticsService.blog(year);
        return Result.success(list);
    }
}
