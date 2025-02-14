package org.example.springboot.mapper;

import org.example.springboot.domain.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 标签Mapper接口
 * </p>
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}
