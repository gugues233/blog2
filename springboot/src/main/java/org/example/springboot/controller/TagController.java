package org.example.springboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.example.springboot.domain.Result;
import org.example.springboot.domain.dto.TagDto;
import org.example.springboot.domain.entity.Tag;
import org.example.springboot.domain.vo.TagVo;
import org.example.springboot.service.ITagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 标签前端控制器
 * </p>
 */
@RestController
@RequestMapping("/tag")
@io.swagger.v3.oas.annotations.tags.Tag(name = "标签", description = "标签")
public class TagController {
    @Resource
    private ITagService tagService;

    /**
     * 添加、修改标签
     *
     * @param tag 标签
     * @return 结果
     */
    @PostMapping
    @Operation(summary = "添加、修改标签", description = "添加、修改标签", method = "POST")
    public Result<Void> save(@RequestBody Tag tag) {
        tagService.saveOrUpdate(tag);
        return Result.success();
    }

    /**
     * 删除标签
     *
     * @param ids ID列表
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    @Operation(summary = "删除标签", description = "删除标签", method = "DELETE")
    public Result<Void> removeBatchByIds(@PathVariable List<Long> ids) {
        tagService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 查询标签列表
     *
     * @param dto 标签
     * @return 结果
     */
    @GetMapping("/list")
    @Operation(summary = "查询标签列表", description = "查询标签列表", method = "GET")
    public Result<List<TagVo>> getList(TagDto dto) {
        List<TagVo> list = tagService.getList(dto);
        return Result.success(list);
    }

    /**
     * 查询标签分页
     *
     * @param dto 标签
     * @return 结果
     */
    @GetMapping("/page")
    @Operation(summary = "查询标签分页", description = "查询标签分页", method = "GET")
    public Result<IPage<TagVo>> getPage(TagDto dto) {
        IPage<TagVo> page = tagService.getPage(dto);
        return Result.success(page);
    }

    /**
     * 查询标签
     *
     * @param dto 标签
     * @return 结果
     */
    @GetMapping
    @Operation(summary = "查询标签", description = "查询标签", method = "GET")
    public Result<TagVo> getOne(TagDto dto) {
        TagVo vo = tagService.getOne(dto);
        return Result.success(vo);
    }
}
