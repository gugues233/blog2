package org.example.springboot.mapper;

import org.example.springboot.domain.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 博客Mapper接口
 * </p>
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

}
