package org.example.springboot.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.springboot.domain.dto.BlogTagDto;
import org.example.springboot.domain.entity.Blog;
import org.example.springboot.domain.entity.BlogTag;
import org.example.springboot.domain.entity.Tag;
import org.example.springboot.domain.vo.BlogTagVo;
import org.example.springboot.mapper.BlogTagMapper;
import org.example.springboot.service.IBlogService;
import org.example.springboot.service.IBlogTagService;
import org.example.springboot.service.ITagService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 博客标签关系服务实现类
 * </p>
 */
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements IBlogTagService {
    @Resource
    private IBlogService blogService;
    @Resource
    private ITagService tagService;

    @Override
    public List<BlogTagVo> getList(BlogTagDto dto) {
        List<BlogTag> blogTagList = getWrapper(dto).list();
        if (CollectionUtil.isEmpty(blogTagList)) {
            return List.of();
        }
        // 博客
        List<Long> blogIdList = blogTagList.stream().map(BlogTag::getBlogId).toList();
        List<Blog> blogList = blogService.listByIds(blogIdList);
        Map<Long, Blog> blogMap = blogList.stream().collect(Collectors.toMap(Blog::getId, item -> item));
        // 标签
        List<Long> tagIdList = blogTagList.stream().map(BlogTag::getTagId).toList();
        List<Tag> tagList = tagService.listByIds(tagIdList);
        Map<Long, Tag> tagMap = tagList.stream().collect(Collectors.toMap(Tag::getId, item -> item));
        // 组装VO
        return blogTagList.stream().map(item -> {
            BlogTagVo vo = new BlogTagVo();
            BeanUtils.copyProperties(item, vo);
            vo.setBlog(blogMap.getOrDefault(item.getBlogId(), Blog.builder().title("已删除").build()));
            vo.setTag(tagMap.getOrDefault(item.getTagId(), Tag.builder().name("已删除").build()));
            return vo;
        }).toList();
    }

    @Override
    public IPage<BlogTagVo> getPage(BlogTagDto dto) {
        Page<BlogTag> info = getWrapper(dto).page(new Page<>(dto.getPageNo(), dto.getPageSize()));
        if (CollectionUtil.isEmpty(info.getRecords())) {
            return new Page<>(dto.getPageNo(), dto.getPageSize(), 0);
        }
        // 博客
        List<Long> blogIdList = info.getRecords().stream().map(BlogTag::getBlogId).toList();
        List<Blog> blogList = blogService.listByIds(blogIdList);
        Map<Long, Blog> blogMap = blogList.stream().collect(Collectors.toMap(Blog::getId, item -> item));
        // 标签
        List<Long> tagIdList = info.getRecords().stream().map(BlogTag::getTagId).toList();
        List<Tag> tagList = tagService.listByIds(tagIdList);
        Map<Long, Tag> tagMap = tagList.stream().collect(Collectors.toMap(Tag::getId, item -> item));
        // 组装VO
        return info.convert(item -> {
            BlogTagVo vo = new BlogTagVo();
            BeanUtils.copyProperties(item, vo);
            vo.setBlog(blogMap.getOrDefault(item.getBlogId(), Blog.builder().title("已删除").build()));
            vo.setTag(tagMap.getOrDefault(item.getTagId(), Tag.builder().name("已删除").build()));
            return vo;
        });
    }

    @Override
    public BlogTagVo getOne(BlogTagDto dto) {
        BlogTag one = getWrapper(dto).one();
        if (one == null) {
            return null;
        }
        // 博客
        Blog blog = Optional.ofNullable(blogService.getById(one.getBlogId())).orElse(Blog.builder().title("已删除").build());
        // 标签
        Tag tag = Optional.ofNullable(tagService.getById(one.getTagId())).orElse(Tag.builder().name("已删除").build());
        // 组装VO
        BlogTagVo vo = new BlogTagVo();
        BeanUtils.copyProperties(one, vo);
        vo.setBlog(blog);
        vo.setTag(tag);
        return vo;
    }

    /**
     * 组装查询包装器
     *
     * @param dto 博客标签关系
     * @return 结果
     */
    private LambdaQueryChainWrapper<BlogTag> getWrapper(BlogTagDto dto) {
        Map<String, Object> params = dto.getParams();
        // 创建时间
        Object startCreateTime = params == null ? null : params.get("startCreateTime");
        Object endCreateTime = params == null ? null : params.get("endCreateTime");
        return lambdaQuery()
                .eq(dto.getId() != null, BlogTag::getId, dto.getId())
                .eq(dto.getBlogId() != null, BlogTag::getBlogId, dto.getBlogId())
                .eq(dto.getTagId() != null, BlogTag::getTagId, dto.getTagId())
                .between(ObjectUtil.isAllNotEmpty(startCreateTime, endCreateTime), BlogTag::getCreateTime, startCreateTime, endCreateTime);
    }
}
