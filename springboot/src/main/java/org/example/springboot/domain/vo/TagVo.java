package org.example.springboot.domain.vo;

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
 * 标签
 * </p>
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(name = "标签实体", description = "标签")
public class TagVo extends Tag {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 占位符
     */
    @Schema(description = "占位符")
    private String placeholder;
}
