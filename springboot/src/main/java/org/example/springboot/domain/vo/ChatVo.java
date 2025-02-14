package org.example.springboot.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.example.springboot.domain.entity.Chat;
import org.example.springboot.domain.entity.User;

import java.io.Serial;

/**
 * <p>
 * 聊天
 * </p>
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(name = "聊天实体", description = "聊天")
public class ChatVo extends Chat {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 占位符
     */
    @Schema(description = "占位符")
    private String placeholder;
    /**
     * 发送者
     */
    @Schema(description = "发送者")
    private User sender;
    /**
     * 接收者
     */
    @Schema(description = "接收者")
    private User receiver;
}
