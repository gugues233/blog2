package org.example.springboot.service;

import org.example.springboot.domain.Result;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 统计服务类
 * </p>
 */
public interface IStatisticsService {
    /**
     * 统计博客信息
     *
     * @param year 年份
     * @return 结果
     */
    List<Map<String, Object>> blog(Integer year);

    /**
     * 仪表盘统计
     *
     * @return 结果
     */
    Map<String, Long> getDashboardInfo();
}
