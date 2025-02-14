package org.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.example.springboot.domain.Result;
import org.example.springboot.domain.entity.Blog;
import org.example.springboot.service.IBlogService;
import org.example.springboot.service.ICommentService;
import org.example.springboot.service.IStatisticsService;
import org.example.springboot.service.IUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 统计服务实现类
 * </p>
 */
@Service
public class StatisticsServiceImpl implements IStatisticsService {
    @Resource
    private IBlogService blogService;
    @Resource
    private IUserService userService;
    @Resource
    private ICommentService commentService;
    // TODO 统计浏览量

    @Override
    public List<Map<String, Object>> blog(Integer year) {
        // 构建查询条件
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        if (year != null) {
            queryWrapper.ge(Blog::getCreateTime, LocalDateTime.of(year, 1, 1, 0, 0))
                    .lt(Blog::getCreateTime, LocalDateTime.of(year + 1, 1, 1, 0, 0));
        }
        // 查询数据
        List<Blog> blogs = blogService.list(queryWrapper);
        // 初始化每个月的数据为0
        Map<Integer, Integer> monthlyCount = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            monthlyCount.put(i, 0);
        }
        // 填充实际数据
        for (Blog blog : blogs) {
            int month = blog.getCreateTime().get(ChronoField.MONTH_OF_YEAR);
            monthlyCount.put(month, monthlyCount.get(month) + 1);
        }
        // 转换为前端需要的格式
        return monthlyCount.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("month", entry.getKey());
                    map.put("count", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> getDashboardInfo() {
        // TODO Redis记录浏览量
        Map<String, Long> map = new LinkedHashMap<>();
        long blogCount = blogService.count();
        long userCount = userService.count();
        long commentCount = commentService.count();
        map.put("blogCount", blogCount);
        map.put("userCount", userCount);
        map.put("commentCount", commentCount);
        map.put("viewCount", 0L);
        return map;
    }
}
