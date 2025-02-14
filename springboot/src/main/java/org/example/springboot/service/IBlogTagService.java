package org.example.springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.springboot.domain.dto.BlogTagDto;
import org.example.springboot.domain.entity.BlogTag;
import org.example.springboot.domain.vo.BlogTagVo;

import java.util.List;

/**
 * <p>
 * 博客标签关系服务类
 * </p>
 */
public interface IBlogTagService extends IService<BlogTag> {
    /**
     * 查询博客标签关系列表
     *
     * @param dto 博客标签关系
     * @return 结果
     */
    List<BlogTagVo> getList(BlogTagDto dto);

    /**
     * 查询博客标签关系分页
     *
     * @param dto 博客标签关系
     * @return 结果
     */
    IPage<BlogTagVo> getPage(BlogTagDto dto);

    /**
     * 查询博客标签关系
     *
     * @param dto 博客标签关系
     * @return 结果
     */
    BlogTagVo getOne(BlogTagDto dto);
}
