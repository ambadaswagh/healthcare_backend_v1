/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.healthcare.chat;

import com.healthcare.api.auth.AbstractBasedAPI;
import com.healthcare.chat.model.ChatMessageModel;
import com.healthcare.chat.model.GroupChatRequest;
import com.healthcare.chat.model.entity.MessageBox;
import com.healthcare.chat.notify.ChatNotifyService;
import com.healthcare.chat.service.chatSection.ChatSectionService;
import com.healthcare.chat.service.group.ChatGroupService;
import com.healthcare.chat.service.messageBox.MessageBoxService;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tungn
 */
@CrossOrigin
@RestController(value = "MessageBoxRestAPI")
@RequestMapping(value = "/api/chat")
public class MessageBoxAPIController extends AbstractBasedAPI {

    // Inject Service
    @Autowired
    private MessageBoxService messageBoxService;

    @Autowired
    private ChatGroupService groupService;

    @Autowired
    private ChatSectionService chatSectionService;

    @Autowired
    private ChatNotifyService chatNotifyService;


    /**
     * API Get List message
     */
    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public @ResponseBody
    Response getMessageBoxList(
            @RequestParam(value = "current_page") long currentPage,
            @RequestParam(value = "to_admin_id", defaultValue = "0", required = false) long toAdminId,
            HttpServletRequest request) {

        Admin currentAuthUser = getCurrentAuthenAdminUser(request);
        return messageBoxService.getListMessageBox(currentAuthUser, currentPage, toAdminId);

    }

    /**
     * API Add New message
     */
    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    @ResponseBody
    public Response addNewMessageBox(@RequestBody ChatMessageModel messageModel, HttpServletRequest request) {
        Admin currentAuthUser = getCurrentAuthenAdminUser(request);
        Response response = messageBoxService.createMessageBox(currentAuthUser, messageModel);

        if (response.getData() != null && response.getData() instanceof MessageBox) {
            MessageBox messageBox = (MessageBox) response.getData();
            chatNotifyService.sendNotify(messageBox);
        }
        return response;
    }

    /**
     * API Add New Group Chat
     */
    @RequestMapping(value = "/groups", method = RequestMethod.POST)
    @ResponseBody
    public Response addNewGroupChat(@RequestBody GroupChatRequest groupChatRequest, HttpServletRequest request) {
        Admin currentAuthUser = getCurrentAuthenAdminUser(request);
        return groupService.createChatGroup(currentAuthUser, groupChatRequest);
    }

    /**
     * API edit group chat
     */
    @RequestMapping(value = "/groups", method = RequestMethod.PUT)
    @ResponseBody
    public Response editGroupChat(@RequestBody GroupChatRequest groupChatRequest, HttpServletRequest request) {
        Admin currentAuthUser = getCurrentAuthenAdminUser(request);

        return groupService.editChatGroup(currentAuthUser, groupChatRequest.getGroupId(), groupChatRequest.getName());
    }

    /**
     * API Add member to group
     */
    @RequestMapping(value = "/groups/member", method = RequestMethod.POST)
    @ResponseBody
    public Response addMemberToChatGroup(@RequestBody GroupChatRequest groupChatRequest, HttpServletRequest request) {
        Admin currentAuthUser = getCurrentAuthenAdminUser(request);

        return groupService
                .addMembersToChatGroup(currentAuthUser, groupChatRequest.getGroupId(), groupChatRequest.getSelectedIds());
    }

    /**
     * API Get Group Detail
     */
    @RequestMapping(value = "/groups/{group_id}", method = RequestMethod.GET)
    @ResponseBody
    public Response getGroupChatDetail(@PathVariable(value = "group_id") long groupId, HttpServletRequest request) {
        Admin currentAuthUser = getCurrentAuthenAdminUser(request);
        return groupService.getChatGroupDetail(currentAuthUser, groupId);
    }

    /**
     * API Get list Group Chat
     *
     * @return Response
     */
    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    @ResponseBody
    public Response getGroupChatList(
            @RequestParam(value = "current_page") long currentPage,
            @RequestParam(value = "item_count", defaultValue = "100", required = false) long itemCount,
            HttpServletRequest request
    ) {
        Admin currentAuthUser = getCurrentAuthenAdminUser(request);
        return groupService.getChatGroupList(currentAuthUser, currentPage, itemCount);
    }

    /**
     * API Get list message of Group Chat
     */
    @RequestMapping(value = "/groups/{group_id}/messages", method = RequestMethod.GET)
    @ResponseBody
    public Response getMessageByChatGroup(@RequestParam(value = "current_page") long currentPage,
                                          @PathVariable(value = "group_id") long groupId,
                                          HttpServletRequest request
    ) {
        Admin currentAuthUser = getCurrentAuthenAdminUser(request);
        return groupService.getMessageByChatGroup(currentAuthUser, currentPage, groupId);
    }

    /**
     * API Get list Chat Section
     **/
    @RequestMapping(value = "/conversations", method = RequestMethod.GET)
    @ResponseBody
    public Response getChatSectionList(
            @RequestParam(value = "current_page", required = true) long currentPage,
            @RequestParam(value = "item_count", defaultValue = "100", required = false) long itemCount,
            HttpServletRequest request
    ) {
        Admin currentAuthUser = getCurrentAuthenAdminUser(request);
        return chatSectionService.getListChatSection(currentAuthUser, currentPage, itemCount);
    }

    /**
     * API Get list message of Chat Section
     */
    @RequestMapping(value = "/conversations/messages", method = RequestMethod.GET)
    @ResponseBody
    public Response getMessageListOfChatSection(
            @RequestParam(value = "current_page") long currentPage,
            @RequestParam(value = "fromAdminId", defaultValue = "0", required = false) long fromAdminId,
            @RequestParam(value = "toAdminId", defaultValue = "0", required = false) long toAdminId,
            HttpServletRequest request
    ) {
        Admin currentAuthUser = getCurrentAuthenAdminUser(request);
        return chatSectionService.getListMessageOfChatSection(currentAuthUser, fromAdminId, toAdminId, currentPage);
    }

    /**
     * API Get list message of Chat Section by from-to date
     */
    @RequestMapping(value = "/conversations/messages/print", method = RequestMethod.GET)
    @ResponseBody
    public Response getMessageListOfChatSectionByFromToDate(
            @RequestParam(value = "fromAdminId", defaultValue = "0", required = false) long fromAdminId,
            @RequestParam(value = "toAdminId", defaultValue = "0", required = false) long toAdminId,
            @RequestParam(value = "fromDate") String fromDate,
            @RequestParam(value = "toDate") String toDate,
            HttpServletRequest request
    ) {
        Admin currentAuthUser = getCurrentAuthenAdminUser(request);
        return chatSectionService
                .getListMessageOfChatSectionByFromToDate(currentAuthUser, fromAdminId, toAdminId, fromDate, toDate);
    }

    /**
     * API get List Chat Section And Message To Print
     */
    @RequestMapping(value = "/conversations/messages/print/all", method = RequestMethod.GET)
    @ResponseBody
    public Response getListChatSectionAndMessageToPrint(
            @RequestParam(value = "fromDate") String fromDate,
            @RequestParam(value = "toDate") String toDate,
            @RequestParam(value = "current_page") long currentPage,
            @RequestParam(value = "item_count", defaultValue = "100", required = false) long itemCount,
            HttpServletRequest request
    ) {
        Admin currentAuthUser = getCurrentAuthenAdminUser(request);
        return chatSectionService
                .getListChatSectionAndMessageToPrint(currentAuthUser, fromDate, toDate, currentPage, itemCount);
    }

}
