package org.example.springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.springboot.domain.dto.BlogDto;
import org.example.springboot.domain.entity.Blog;
import org.example.springboot.domain.vo.BlogVo;

import java.util.List;

/**
 * <p>
 * 博客服务类
 * </p>
 */
public interface IBlogService extends IService<Blog> {
    /**
     * 查询博客列表
     *
     * @param dto 博客
     * @return 结果
     */
    List<BlogVo> getList(BlogDto dto);

    /**
     * 查询博客分页
     *
     * @param dto 博客
     * @return 结果
     */
    IPage<BlogVo> getPage(BlogDto dto);

    /**
     * 查询博客
     *
     * @param dto 博客
     * @return 结果
     */
    BlogVo getOne(BlogDto dto);
}
