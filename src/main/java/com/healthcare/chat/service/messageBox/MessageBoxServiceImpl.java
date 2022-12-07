package com.healthcare.chat.service.messageBox;

import com.healthcare.chat.model.ChatMessageModel;
import com.healthcare.chat.model.entity.ChatGroup;
import com.healthcare.chat.model.entity.ChatSection;
import com.healthcare.chat.model.entity.MessageBox;
import com.healthcare.chat.repository.ChatGroupRepository;
import com.healthcare.chat.repository.ChatSectionRepository;
import com.healthcare.chat.repository.MessageBoxRepository;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.response.Response;
import com.healthcare.repository.AdminRepository;
import com.healthcare.util.MD5hash;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author tungn
 */
@Service
public class MessageBoxServiceImpl implements MessageBoxService {

    @Autowired
    MessageBoxRepository messageBoxRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    ChatGroupRepository chatGroupRepository;

    @Autowired
    ChatSectionRepository chatSectionRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Get all MessageBox
     */
    @Override
    public Response getListMessageBox(Admin currentAdmin, long currentPageRequest, long toAdminId) {
        try {
            int currentPage = currentPageRequest <= 0 ? 1 : (int) currentPageRequest;
            int itemCount = 50;
            int startNo = currentPage - 1;

            long fromAdminId = currentAdmin.getId();

            Page<MessageBox> messageBoxList;
            long toAdmin = toAdminId != 0 ? toAdminId : -1;
            Pageable pageable = new PageRequest(startNo, itemCount);
            log.info(
                    MessageFormat.format("MessageBoxServiceImpl, getListMessageBox(): {0}, {1}", fromAdminId, toAdmin));
            if (toAdmin == -1) {
                messageBoxList = messageBoxRepository.getMessageBoxListAllToAdmin(pageable);
            } else {
                messageBoxList =
                        messageBoxRepository.getMessageBoxListFromTo(fromAdminId, toAdmin, pageable);
            }
            if (messageBoxList != null) {
                return new Response(Response.ResultCode.SUCCESS, messageBoxList, null);
            } else {
                return new Response(Response.ResultCode.MODEL_NOT_FOUND, null, null);
            }

        } catch (Exception e) {
            log.error("Exception in MessageBoxServiceImpl, getListMessageBox(), e: " + e.toString());
            return new Response(Response.ResultCode.ERROR, null, e.toString());
        }
    }

    /**
     * Create MessageBox functions
     */
    @Override
    public Response createMessageBox(Admin currentAdmin, ChatMessageModel messageModel) {
        try {
            if (StringUtils.isEmpty(messageModel.getMessage())) {
                return new Response(Response.ResultCode.INVALID_DATA, null, null);
            }
            long toAdmin = -1;

            // chat peer to peer (to exist admin and not chat in group)
            if (messageModel.getToAdminId() != null && messageModel.getGroupId() == null) {
                Admin admin = adminRepository.findOne(messageModel.getToAdminId());
                if (admin != null) {
                    //get toAdminId
                    toAdmin = admin.getId();

                    //Create and hash ChatSectionId
                    List<String> listId = new ArrayList<>();
                    String chatSectionIdStr =
                            currentAdmin.getId() + "" + toAdmin + "," + toAdmin + "" + currentAdmin.getId();
                    String hashChatSectionId = MD5hash.MD5Encrypt(chatSectionIdStr);
                    listId.add(hashChatSectionId);
                    chatSectionIdStr = toAdmin + "" + currentAdmin.getId() + "," + currentAdmin.getId() + "" + toAdmin;
                    hashChatSectionId = MD5hash.MD5Encrypt(chatSectionIdStr);
                    listId.add(hashChatSectionId);

                    // check exist ChatSection
                    ChatSection exitChatSection = chatSectionRepository.findByIds(listId);
                    if (exitChatSection == null) {
                        // ChatSection not exist
                        // Create new Chat Section
                        ChatSection chatSection = new ChatSection();
                        chatSection.setId(hashChatSectionId);
                        chatSection.setUserIds(currentAdmin.getId() + "" + toAdmin);
                        chatSection.setFromUser(currentAdmin);
                        chatSection.setToUser(admin);
                        chatSection.setCreatedAt(new Date());

                        chatSectionRepository.save(chatSection);
                    } else {
                        exitChatSection.setUpdatedAt(new Date());
                        chatSectionRepository.save(exitChatSection);
                    }
                } else {
                    return new Response(Response.ResultCode.MODEL_NOT_FOUND, null, null);
                }
            }

            ChatGroup group = null;
            if (messageModel.getGroupId() != null) {
                group = chatGroupRepository.findOne(messageModel.getGroupId());
            }

            MessageBox messageBox = new MessageBox();
            messageBox.setFromUser(currentAdmin);
            messageBox.setToUser(toAdmin);
            messageBox.setMessage(messageModel.getMessage());
            messageBox.setCreatedAt(new Date());
            messageBox.setStatus(1); // Unread Message status
            messageBox.setGroup(group);
            messageBox = messageBoxRepository.save(messageBox);

            return new Response(Response.ResultCode.SUCCESS, messageBox, null);

        } catch (Exception e) {
            log.error("Exception in MessageBoxServiceImpl", e);
            return new Response(Response.ResultCode.ERROR, null, e.toString());
        }
    }
}
