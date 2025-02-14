package org.example.springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.springboot.domain.dto.TagDto;
import org.example.springboot.domain.entity.Tag;
import org.example.springboot.domain.vo.TagVo;

import java.util.List;

/**
 * <p>
 * 标签服务类
 * </p>
 */
public interface ITagService extends IService<Tag> {
    /**
     * 查询标签列表
     *
     * @param dto 标签
     * @return 结果
     */
    List<TagVo> getList(TagDto dto);

    /**
     * 查询标签分页
     *
     * @param dto 标签
     * @return 结果
     */
    IPage<TagVo> getPage(TagDto dto);

    /**
     * 查询标签
     *
     * @param dto 标签
     * @return 结果
     */
    TagVo getOne(TagDto dto);
}
