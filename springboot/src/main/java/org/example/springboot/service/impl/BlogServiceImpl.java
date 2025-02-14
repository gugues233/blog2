package org.example.springboot.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.springboot.common.BaseContext;
import org.example.springboot.domain.dto.BlogDto;
import org.example.springboot.domain.entity.Blog;
import org.example.springboot.domain.entity.User;
import org.example.springboot.domain.vo.BlogVo;
import org.example.springboot.mapper.BlogMapper;
import org.example.springboot.service.IBlogService;
import org.example.springboot.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 博客服务实现类
 * </p>
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {
    @Resource
    private IUserService userService;

    @Override
    public boolean saveOrUpdate(Blog entity) {
        if (entity.getId() == null) {
            User user = BaseContext.getUser();
            entity.setUserId(user.getId());
            return super.save(entity);
        }
        return super.saveOrUpdate(entity);
    }

    @Override
    public List<BlogVo> getList(BlogDto dto) {
        List<Blog> blogList = getWrapper(dto).list();
        if (CollectionUtil.isEmpty(blogList)) {
            return List.of();
        }
        // 作者
        List<Long> userIdList = blogList.stream().map(Blog::getUserId).toList();
        List<User> userList = userService.listByIds(userIdList);
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, item -> item));
        // 组装VO
        return blogList.stream().map(item -> {
            BlogVo vo = new BlogVo();
            BeanUtils.copyProperties(item, vo);
            vo.setUser(userMap.getOrDefault(item.getUserId(), User.builder().name("已删除").build()));
            return vo;
        }).toList();
    }

    @Override
    public IPage<BlogVo> getPage(BlogDto dto) {
        Page<Blog> info = getWrapper(dto).page(new Page<>(dto.getPageNo(), dto.getPageSize()));
        if (CollectionUtil.isEmpty(info.getRecords())) {
            return new Page<>(dto.getPageNo(), dto.getPageSize(), 0);
        }
        // 作者
        List<Long> userIdList = info.getRecords().stream().map(Blog::getUserId).toList();
        List<User> userList = userService.listByIds(userIdList);
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, item -> item));
        // 组装VO
        return info.convert(item -> {
            BlogVo vo = new BlogVo();
            BeanUtils.copyProperties(item, vo);
            vo.setUser(userMap.getOrDefault(item.getUserId(), User.builder().name("已删除").build()));
            return vo;
        });
    }

    @Override
    public BlogVo getOne(BlogDto dto) {
        Blog one = getWrapper(dto).one();
        if (one == null) {
            return null;
        }
        // 作者
        User user = Optional.ofNullable(userService.getById(one.getUserId())).orElse(User.builder().name("已删除").build());
        // 组装VO
        BlogVo vo = new BlogVo();
        BeanUtils.copyProperties(one, vo);
        vo.setUser(user);
        return vo;
    }

    /**
     * 组装查询包装器
     *
     * @param dto 博客
     * @return 结果
     */
    private LambdaQueryChainWrapper<Blog> getWrapper(BlogDto dto) {
        Map<String, Object> params = dto.getParams();
        // 创建时间
        Object startCreateTime = params == null ? null : params.get("startCreateTime");
        Object endCreateTime = params == null ? null : params.get("endCreateTime");
        return lambdaQuery()
                .eq(dto.getId() != null, Blog::getId, dto.getId())
                .like(StrUtil.isNotBlank(dto.getTitle()), Blog::getTitle, dto.getTitle())
                .like(StrUtil.isNotBlank(dto.getContent()), Blog::getContent, dto.getContent())
                .eq(dto.getUserId() != null, Blog::getUserId, dto.getUserId())
                .between(ObjectUtil.isAllNotEmpty(startCreateTime, endCreateTime), Blog::getCreateTime, startCreateTime, endCreateTime);
    }
}
