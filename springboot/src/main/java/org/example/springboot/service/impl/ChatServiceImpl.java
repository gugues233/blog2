package org.example.springboot.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.springboot.common.BaseContext;
import org.example.springboot.common.enums.UserRole;
import org.example.springboot.domain.dto.ChatDto;
import org.example.springboot.domain.entity.Chat;
import org.example.springboot.domain.entity.User;
import org.example.springboot.domain.vo.ChatVo;
import org.example.springboot.mapper.ChatMapper;
import org.example.springboot.service.IChatService;
import org.example.springboot.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 聊天服务实现类
 * </p>
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements IChatService {
    @Resource
    private IUserService userService;

    @Override
    public boolean saveOrUpdate(Chat entity) {
        if (entity.getId() == null) {
            User user = BaseContext.getUser();
            entity.setSenderId(user.getId());
            return super.save(entity);
        }
        return super.saveOrUpdate(entity);
    }

    @Override
    public List<ChatVo> getList(ChatDto dto) {
        List<Chat> chatList = getWrapper(dto).list();
        if (CollectionUtil.isEmpty(chatList)) {
            return List.of();
        }
        // 发送者
        List<Long> senderIdList = chatList.stream().map(Chat::getSenderId).toList();
        List<User> senderList = userService.listByIds(senderIdList);
        Map<Long, User> senderMap = senderList.stream().collect(Collectors.toMap(User::getId, item -> item));
        // 接收者
        List<Long> receiverIdList = chatList.stream().map(Chat::getReceiverId).toList();
        List<User> receiverList = userService.listByIds(receiverIdList);
        Map<Long, User> receiverMap = receiverList.stream().collect(Collectors.toMap(User::getId, item -> item));
        // 组装VO
        return chatList.stream().map(item -> {
            ChatVo vo = new ChatVo();
            BeanUtils.copyProperties(item, vo);
            vo.setSender(senderMap.getOrDefault(item.getSenderId(), User.builder().name("已删除").build()));
            vo.setReceiver(receiverMap.getOrDefault(item.getReceiverId(), User.builder().name("已删除").build()));
            return vo;
        }).toList();
    }

    @Override
    public IPage<ChatVo> getPage(ChatDto dto) {
        Page<Chat> info = getWrapper(dto).page(new Page<>(dto.getPageNo(), dto.getPageSize()));
        if (CollectionUtil.isEmpty(info.getRecords())) {
            return new Page<>(dto.getPageNo(), dto.getPageSize(), 0);
        }
        // 发送者
        List<Long> senderIdList = info.getRecords().stream().map(Chat::getSenderId).toList();
        List<User> senderList = userService.listByIds(senderIdList);
        Map<Long, User> senderMap = senderList.stream().collect(Collectors.toMap(User::getId, item -> item));
        // 接收者
        List<Long> receiverIdList = info.getRecords().stream().map(Chat::getReceiverId).toList();
        List<User> receiverList = userService.listByIds(receiverIdList);
        Map<Long, User> receiverMap = receiverList.stream().collect(Collectors.toMap(User::getId, item -> item));
        // 组装VO
        return info.convert(item -> {
            ChatVo vo = new ChatVo();
            BeanUtils.copyProperties(item, vo);
            vo.setSender(senderMap.getOrDefault(item.getSenderId(), User.builder().name("已删除").build()));
            vo.setReceiver(receiverMap.getOrDefault(item.getReceiverId(), User.builder().name("已删除").build()));
            return vo;
        });
    }

    @Override
    public ChatVo getOne(ChatDto dto) {
        Chat one = getWrapper(dto).one();
        if (one == null) {
            return null;
        }
        // 发送者
        User sender = Optional.ofNullable(userService.getById(one.getSenderId())).orElse(User.builder().name("已删除").build());
        // 接收者
        User receiver = Optional.ofNullable(userService.getById(one.getReceiverId())).orElse(User.builder().name("已删除").build());
        // 组装VO
        ChatVo vo = new ChatVo();
        BeanUtils.copyProperties(one, vo);
        vo.setSender(sender);
        vo.setReceiver(receiver);
        return vo;
    }

    /**
     * 组装查询包装器
     *
     * @param dto 聊天
     * @return 结果
     */
    private LambdaQueryChainWrapper<Chat> getWrapper(ChatDto dto) {
        // 权限控制
        User user = BaseContext.getUser();
        Map<String, Object> params = dto.getParams();
        // 创建时间
        Object startCreateTime = params == null ? null : params.get("startCreateTime");
        Object endCreateTime = params == null ? null : params.get("endCreateTime");
        return lambdaQuery()
                .eq(!Objects.equals(user.getRole(), UserRole.ADMIN.getCode()), Chat::getSenderId, user.getId())
                .or()
                .eq(!Objects.equals(user.getRole(), UserRole.ADMIN.getCode()), Chat::getReceiverId, user.getId())
                .eq(dto.getId() != null, Chat::getId, dto.getId())
                .eq(dto.getSenderId() != null, Chat::getSenderId, dto.getSenderId())
                .eq(dto.getReceiverId() != null, Chat::getReceiverId, dto.getReceiverId())
                .like(StrUtil.isNotBlank(dto.getMessage()), Chat::getMessage, dto.getMessage())
                .between(ObjectUtil.isAllNotEmpty(startCreateTime, endCreateTime), Chat::getCreateTime, startCreateTime, endCreateTime);
    }
}
