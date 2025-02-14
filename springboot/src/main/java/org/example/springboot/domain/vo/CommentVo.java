package org.example.springboot.domain.vo;

import org.example.springboot.domain.entity.Comment;
import org.example.springboot.domain.entity.Blog;
import org.example.springboot.domain.entity.User;
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
 * 评论
 * </p>
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(name = "评论实体", description = "评论")
public class CommentVo extends Comment {
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
     * 用户
     */
    @Schema(description = "用户")
    private User user;
}
