/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.healthcare.chat.service.group;

import com.healthcare.chat.model.GroupChatRequest;
import com.healthcare.chat.model.entity.ChatGroup;
import com.healthcare.chat.model.entity.GroupMember;
import com.healthcare.chat.model.entity.MessageBox;
import com.healthcare.chat.repository.ChatGroupRepository;
import com.healthcare.chat.repository.GroupMemberRepository;
import com.healthcare.chat.repository.MessageBoxRepository;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.response.Response;
import com.healthcare.repository.AdminRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author tungn
 */
@Service
public class ChatGroupServiceImpl implements ChatGroupService {

    @Autowired
    ChatGroupRepository groupRepository;

    @Autowired
    GroupMemberRepository groupMemberRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    MessageBoxRepository messageBoxRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Response createChatGroup(Admin currentAdmin, GroupChatRequest groupChatRequest) {
        // check selectedUserIds and groupName is not null
        if (!CollectionUtils.isEmpty(groupChatRequest.getSelectedIds()) && StringUtils
                .isNotEmpty(groupChatRequest.getName())) {

            ChatGroup existGroupName = groupRepository.findByName(groupChatRequest.getName().toLowerCase());
            // check name of group is not exist
            if (existGroupName == null) {
                // create new group
                ChatGroup group = new ChatGroup();
                group.setName(groupChatRequest.getName());
                group.setCreatedBy(currentAdmin);
                group.setCreatedAt(new Date());
                // save group to database
                groupRepository.save(group);

                // get list admin by select userIds
                Iterable<Admin> adminMemberList = adminRepository.findAll(groupChatRequest.getSelectedIds());
                // check list admin is not null
                if (adminMemberList != null) {
                    // create new group member list
                    List<GroupMember> newGroupMemberList = new ArrayList<>();
                    // create each group member for each admin
                    convertToGroupMemberList(group, adminMemberList, newGroupMemberList);

                    // save all New Group Member list to database
                    groupMemberRepository.save(newGroupMemberList);

                    return new Response(Response.ResultCode.SUCCESS, group, "Create group successfully");
                } else {
                    return new Response(Response.ResultCode.ERROR, null, "Can not find list admin");
                }
            } else {
                return new Response(Response.ResultCode.ERROR, null, "Name of Group is exist");
            }
        } else {
            return new Response(Response.ResultCode.INVALID_DATA, null,
                    "selected UserIds or name of Group is null");
        }
    }

    private void convertToGroupMemberList(ChatGroup group, Iterable<Admin> adminMemberList,
                                          List<GroupMember> newGroupMemberList) {
        for (Admin adminMember : adminMemberList) {
            GroupMember groupMember = new GroupMember();
            groupMember.setUser(adminMember);
            groupMember.setGroup(group);
            groupMember.setCreatedAt(new Date());
//                groupMember.setStatus(Constants.STATUS.ACTIVE.getValue()); TODO fix
            // add each new group member to New Group Member list
            newGroupMemberList.add(groupMember);
        }
    }

    @Override
    public Response editChatGroup(Admin currentAdmin, long groupId, String groupName) {
        try {
            // find group by id
            ChatGroup group = groupRepository.findOne(groupId);
            // check group is not null
            if (group != null) {
                // check name of group is not null
                if (groupName != null) {
                    ChatGroup existGroupName = groupRepository.findByName(groupName.toLowerCase());
                    // check name of group is not exist
                    if (existGroupName == null) {
                        // check group belong to current admin
                        if (group.getCreatedBy().getId().equals(currentAdmin.getId())) {
                            // update detail group
                            group.setName(groupName);
                            group.setUpdatedAt(new Date());

                            // save updated group to database
                            groupRepository.save(group);
                            return new Response(Response.ResultCode.SUCCESS, group, "Update group successfully");
                        } else {
                            return new Response(Response.ResultCode.ERROR, null, "Group is not belong to this user");
                        }
                    } else {
                        return new Response(Response.ResultCode.ERROR, null, "Name of Group is exist");
                    }
                } else {
                    return new Response(Response.ResultCode.INVALID_DATA, null, "selected UserIds or name of Group is null");
                }
            } else {
                return new Response(Response.ResultCode.MODEL_NOT_FOUND, null, "Group are not exist");
            }
        } catch (Exception e) {
            log.error("Exception in GroupServiceImpl, editGroup(), e: " + e.toString());
            return new Response(Response.ResultCode.ERROR, null, e.toString());
        }
    }

    @Override
    public Response addMembersToChatGroup(Admin currentAdmin, long groupId, List<Long> selectedUserIds) {
        try {
            // check selectedUserIds is not null
            if (selectedUserIds != null) {
                // find group by id
                ChatGroup group = groupRepository.findOne(groupId);
                // check group is not null
                if (group != null) {
                    // check group belong to current admin
                    if (group.getCreatedBy().getId().equals(currentAdmin.getId())) {
                        // get exist group member list by selected userIds
                        List<GroupMember> existGroupMemberList = groupMemberRepository.getGroupMemberListByUserIds(selectedUserIds);
                        // check existGroupMemberList has data
                        if (existGroupMemberList != null && !existGroupMemberList.isEmpty()) {
                            // check exist Group Member in selectedUserIds and remove it
                            for (GroupMember existGroupMember : existGroupMemberList) {
                                long existId = existGroupMember.getUser().getId();
                                if (selectedUserIds.contains(existId)) {
                                    selectedUserIds.remove(existId);
                                }
                            }
                        }
                        if (!selectedUserIds.isEmpty()) {
                            // get list admin by selected userIds
                            Iterable<Admin> adminMemberList = adminRepository.findAll(selectedUserIds);
                            // check list admin is not null
                            if (adminMemberList != null) {
                                // create new group member list
                                List<GroupMember> newGroupMemberList = new ArrayList<>();
                                // create each group member for each admin
                                convertToGroupMemberList(group, adminMemberList, newGroupMemberList);

                                // save all New Group Member list to database
                                groupMemberRepository.save(newGroupMemberList);

                                return new Response(Response.ResultCode.SUCCESS, group, "add members to group successfully");
                            } else {
                                return new Response(Response.ResultCode.NO_MORE_DATA, null, "all selected userIds are exist");
                            }
                        } else {
                            return new Response(Response.ResultCode.ERROR, null, "Can not find list admin");
                        }
                    } else {
                        return new Response(Response.ResultCode.ERROR, null, "Group is not belong to this user");
                    }
                } else {
                    return new Response(Response.ResultCode.MODEL_NOT_FOUND, null, "Group are not exist");
                }
            } else {
                return new Response(Response.ResultCode.INVALID_DATA, null, "selected UserIds is null");
            }
        } catch (Exception e) {
            log.error("Exception in GroupServiceImpl, addMembersToGroup(), e: " + e.toString());
            return new Response(Response.ResultCode.ERROR, null, e.toString());
        }
    }

    @Override
    public Response getChatGroupDetail(Admin currentAdmin, long groupId) {
        try {
            // find group by id
            ChatGroup group = groupRepository.findOne(groupId);
            // check group is not null
            if (group != null) {
                // check group belong to current admin
                if (group.getCreatedBy().getId().equals(currentAdmin.getId())) {
                    // get exist group member list by selected userIds
                    List<GroupMember> groupMemberList = groupMemberRepository.getGroupMemberListByGroupId(groupId);
                    // check groupMemberList has data
                    if (groupMemberList != null) {
                        // create data response including group and group members
                        Map<String, Object> groupMap = new HashMap<>();
                        groupMap.put("group", group);
                        groupMap.put("group_members", groupMemberList);
                        return new Response(Response.ResultCode.SUCCESS, groupMap, null);
                    } else {
                        return new Response(Response.ResultCode.ERROR, null, "Can not find list group member");
                    }
                } else {
                    return new Response(Response.ResultCode.ERROR, null, "Group is not belong to this user");
                }
            } else {
                return new Response(Response.ResultCode.MODEL_NOT_FOUND, null, "Group are not exist");
            }
        } catch (Exception e) {
            log.error("Exception in GroupServiceImpl, addMembersToGroup(), e: " + e.toString());
            return new Response(Response.ResultCode.ERROR, null, e.toString());
        }
    }


    @Override
    public Response getChatGroupList(Admin currentAdmin, long currentPageRequest, long count) {
        try {
            int currentPage = currentPageRequest <= 0 ? 1 : (int) currentPageRequest;
            int itemCount = count > 0 ? (int) count : 100;
            int startNo = currentPage - 1;

            long currentAdminId = currentAdmin.getId();

            Page<ChatGroup> groupList;
            Pageable pageable = new PageRequest(startNo, itemCount);

            if (currentAdmin.getRole().getLevel() == 1 && "SUB_BASE_ADMIN".equals(currentAdmin.getRole().getLevelName())) {
                groupList = groupRepository.getChatGroupListContainUser(currentAdminId, pageable);
            } else {
                groupList = groupRepository.getAllChatGroupList(pageable);
            }

            if (groupList != null) {
                return new Response(Response.ResultCode.SUCCESS, groupList, null);
            } else {
                return new Response(Response.ResultCode.MODEL_NOT_FOUND, null, null);
            }
        } catch (Exception e) {
            log.error("Exception in GroupServiceImpl, getGroupList(), e: " + e.toString());
            return new Response(Response.ResultCode.ERROR, null, e.toString());
        }
    }

    @Override
    public Response getMessageByChatGroup(Admin currentAdmin, long currentPageRequest, long groupId) {
        try {
            int currentPage = currentPageRequest <= 0 ? 1 : (int) currentPageRequest;
            int itemCount = 50;
            int startNo = currentPage - 1;

            Page<MessageBox> messageBoxList = null;
            Pageable pageable = new PageRequest(startNo, itemCount);
            if (groupId > 0) {
                messageBoxList = messageBoxRepository.getMessageBoxListByGroupId(groupId, pageable);
            }
            if (messageBoxList != null) {
                return new Response(Response.ResultCode.SUCCESS, messageBoxList, null);
            } else {
                return new Response(Response.ResultCode.MODEL_NOT_FOUND, null, null);
            }
        } catch (Exception e) {
            log.error("Exception in GroupServiceImpl, getGroupList(), e: " + e.toString());
            return new Response(Response.ResultCode.ERROR, null, e.toString());
        }
    }
}
