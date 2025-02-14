package org.example.springboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.springboot.domain.vo.BlogTagVo;
import org.example.springboot.domain.Result;
import org.example.springboot.domain.entity.BlogTag;
import org.example.springboot.domain.dto.BlogTagDto;
import org.example.springboot.service.IBlogTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 博客标签关系前端控制器
 * </p>
 */
@RestController
@RequestMapping("/blogTag")
@Tag(name = "博客标签关系", description = "博客标签关系")
public class BlogTagController {
    @Resource
    private IBlogTagService blogTagService;

    /**
     * 添加、修改博客标签关系
     *
     * @param blogTag 博客标签关系
     * @return 结果
     */
    @PostMapping
    @Operation(summary = "添加、修改博客标签关系", description = "添加、修改博客标签关系", method = "POST")
    public Result<Void> save(@RequestBody BlogTag blogTag) {
        blogTagService.saveOrUpdate(blogTag);
        return Result.success();
    }

    /**
     * 删除博客标签关系
     *
     * @param ids ID列表
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    @Operation(summary = "删除博客标签关系", description = "删除博客标签关系", method = "DELETE")
    public Result<Void> removeBatchByIds(@PathVariable List<Long> ids) {
        blogTagService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 查询博客标签关系列表
     *
     * @param dto 博客标签关系
     * @return 结果
     */
    @GetMapping("/list")
    @Operation(summary = "查询博客标签关系列表", description = "查询博客标签关系列表", method = "GET")
    public Result<List<BlogTagVo>> getList(BlogTagDto dto) {
        List<BlogTagVo> list = blogTagService.getList(dto);
        return Result.success(list);
    }

    /**
     * 查询博客标签关系分页
     *
     * @param dto 博客标签关系
     * @return 结果
     */
    @GetMapping("/page")
    @Operation(summary = "查询博客标签关系分页", description = "查询博客标签关系分页", method = "GET")
    public Result<IPage<BlogTagVo>> getPage(BlogTagDto dto) {
        IPage<BlogTagVo> page = blogTagService.getPage(dto);
        return Result.success(page);
    }

    /**
     * 查询博客标签关系
     *
     * @param dto 博客标签关系
     * @return 结果
     */
    @GetMapping
    @Operation(summary = "查询博客标签关系", description = "查询博客标签关系", method = "GET")
    public Result<BlogTagVo> getOne(BlogTagDto dto) {
        BlogTagVo vo = blogTagService.getOne(dto);
        return Result.success(vo);
    }
}
