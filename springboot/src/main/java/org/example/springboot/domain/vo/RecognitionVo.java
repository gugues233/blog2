package org.example.springboot.domain.vo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 图像识别结果
 * </p>
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(name = "图像识别结果实体", description = "图像识别结果")
public class RecognitionVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 识别结果
     */
    @Schema(description = "识别结果")
    private String result;
}

