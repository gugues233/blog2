package org.example.springboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.springboot.common.annotation.Anonymous;
import org.example.springboot.domain.Result;
import org.example.springboot.domain.dto.BlogDto;
import org.example.springboot.domain.entity.Blog;
import org.example.springboot.domain.vo.BlogVo;
import org.example.springboot.service.IBlogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 博客前端控制器
 * </p>
 */
@RestController
@RequestMapping("/blog")
@Tag(name = "博客", description = "博客")
public class BlogController {
    @Resource
    private IBlogService blogService;

    /**
     * 添加、修改博客
     *
     * @param blog 博客
     * @return 结果
     */
    @PostMapping
    @Operation(summary = "添加、修改博客", description = "添加、修改博客", method = "POST")
    public Result<Void> save(@RequestBody Blog blog) {
        blogService.saveOrUpdate(blog);
        return Result.success();
    }

    /**
     * 删除博客
     *
     * @param ids ID列表
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    @Operation(summary = "删除博客", description = "删除博客", method = "DELETE")
    public Result<Void> removeBatchByIds(@PathVariable List<Long> ids) {
        blogService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 查询博客列表
     *
     * @param dto 博客
     * @return 结果
     */
    @Anonymous
    @GetMapping("/list")
    @Operation(summary = "查询博客列表", description = "查询博客列表", method = "GET")
    public Result<List<BlogVo>> getList(BlogDto dto) {
        List<BlogVo> list = blogService.getList(dto);
        return Result.success(list);
    }

    /**
     * 查询博客分页
     *
     * @param dto 博客
     * @return 结果
     */
    @Anonymous
    @GetMapping("/page")
    @Operation(summary = "查询博客分页", description = "查询博客分页", method = "GET")
    public Result<IPage<BlogVo>> getPage(BlogDto dto) {
        IPage<BlogVo> page = blogService.getPage(dto);
        return Result.success(page);
    }

    /**
     * 查询博客
     *
     * @param dto 博客
     * @return 结果
     */
    @Anonymous
    @GetMapping
    @Operation(summary = "查询博客", description = "查询博客", method = "GET")
    public Result<BlogVo> getOne(BlogDto dto) {
        BlogVo vo = blogService.getOne(dto);
        return Result.success(vo);
    }
}
