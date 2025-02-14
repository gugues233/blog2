package org.example.springboot.mapper;

import org.example.springboot.domain.entity.BlogTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 博客标签关系Mapper接口
 * </p>
 */
@Mapper
public interface BlogTagMapper extends BaseMapper<BlogTag> {

}
