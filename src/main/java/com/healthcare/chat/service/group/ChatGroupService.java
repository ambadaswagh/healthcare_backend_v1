/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.healthcare.chat.service.group;

import com.healthcare.chat.model.GroupChatRequest;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.response.Response;

import java.util.List;

/**
 *
 * @author tungn
 */
public interface ChatGroupService {

    Response createChatGroup(Admin currentAdmin, GroupChatRequest groupChatRequest);

    Response editChatGroup(Admin currentAdmin, long groupId, String groupName);

    Response addMembersToChatGroup(Admin currentAdmin, long groupId, List<Long> selectedUserIds);

    Response getChatGroupDetail(Admin currentAdmin, long groupId);

    Response getChatGroupList(Admin currentAdmin, long currentPageRequest, long itemCount);

    Response getMessageByChatGroup(Admin currentAdmin, long currentPageRequest, long groupId);
}
