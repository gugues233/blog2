package org.example.springboot.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.springboot.domain.dto.TagDto;
import org.example.springboot.domain.entity.Tag;
import org.example.springboot.domain.vo.TagVo;
import org.example.springboot.mapper.TagMapper;
import org.example.springboot.service.ITagService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标签服务实现类
 * </p>
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {
    @Override
    public List<TagVo> getList(TagDto dto) {
        List<Tag> tagList = getWrapper(dto).list();
        if (CollectionUtil.isEmpty(tagList)) {
            return List.of();
        }
        // 组装VO
        return tagList.stream().map(item -> {
            TagVo vo = new TagVo();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).toList();
    }

    @Override
    public IPage<TagVo> getPage(TagDto dto) {
        Page<Tag> info = getWrapper(dto).page(new Page<>(dto.getPageNo(), dto.getPageSize()));
        if (CollectionUtil.isEmpty(info.getRecords())) {
            return new Page<>(dto.getPageNo(), dto.getPageSize(), 0);
        }
        // 组装VO
        return info.convert(item -> {
            TagVo vo = new TagVo();
            BeanUtils.copyProperties(item, vo);
            return vo;
        });
    }

    @Override
    public TagVo getOne(TagDto dto) {
        Tag one = getWrapper(dto).one();
        if (one == null) {
            return null;
        }
        // 组装VO
        TagVo vo = new TagVo();
        BeanUtils.copyProperties(one, vo);
        return vo;
    }

    /**
     * 组装查询包装器
     *
     * @param dto 标签
     * @return 结果
     */
    private LambdaQueryChainWrapper<Tag> getWrapper(TagDto dto) {
        Map<String, Object> params = dto.getParams();
        // 创建时间
        Object startCreateTime = params == null ? null : params.get("startCreateTime");
        Object endCreateTime = params == null ? null : params.get("endCreateTime");
        return lambdaQuery()
                .eq(dto.getId() != null, Tag::getId, dto.getId())
                .like(StrUtil.isNotBlank(dto.getName()), Tag::getName, dto.getName())
                .between(ObjectUtil.isAllNotEmpty(startCreateTime, endCreateTime), Tag::getCreateTime, startCreateTime, endCreateTime);
    }
}
