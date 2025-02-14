package org.example.springboot.domain.vo;

import org.example.springboot.domain.entity.BlogTag;
import org.example.springboot.domain.entity.Blog;
import org.example.springboot.domain.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * <p>
 * 博客标签关系
 * </p>
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(name = "博客标签关系实体", description = "博客标签关系")
public class BlogTagVo extends BlogTag {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 占位符
     */
    @Schema(description = "占位符")
    private String placeholder;
    /**
     * 博客
     */
    @Schema(description = "博客")
    private Blog blog;
    /**
     * 标签
     */
    @Schema(description = "标签")
    private Tag tag;
}
