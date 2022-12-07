package com.healthcare.chat.service.chatSection;

import com.healthcare.chat.model.entity.ChatSection;
import com.healthcare.chat.model.entity.MessageBox;
import com.healthcare.chat.repository.ChatSectionRepository;
import com.healthcare.chat.repository.MessageBoxRepository;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.response.Response;
import com.healthcare.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author tungn
 */
@Service
public class ChatSectionServiceImpl implements ChatSectionService {

    @Autowired
    MessageBoxRepository messageBoxRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    ChatSectionRepository chatSectionRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Response getListMessageOfChatSection(Admin currentAdmin, long fromAdminId, long toAdminId, long currentPageRequest) {
        try {
            int currentPage = currentPageRequest <= 0 ? 1 : (int) currentPageRequest;
            int itemCount = 50;
            int startNo = currentPage - 1;

            Page<MessageBox> messageBoxList;
            Pageable pageable = new PageRequest(startNo, itemCount);

            messageBoxList = messageBoxRepository.getMessageBoxListFromTo(fromAdminId, toAdminId, pageable);

            if (messageBoxList != null) {
                return new Response(Response.ResultCode.SUCCESS, messageBoxList, null);
            } else {
                return new Response(Response.ResultCode.MODEL_NOT_FOUND, null, null);
            }

        } catch (Exception e) {
            log.error("Exception in ChatSectionServiceImpl, getListMessageOfChatSection(), e: " + e.toString());
            return new Response(Response.ResultCode.ERROR, null, e.toString());
        }
    }

    @Override
    public Response getListMessageOfChatSectionByFromToDate(Admin currentAdmin, long fromAdminId, long toAdminId, String fromDate, String toDate) {
        try {
            if (fromDate == null || "".equals(fromDate.trim()) || toDate == null || "".equals(toDate.trim())) {
                return new Response(Response.ResultCode.INVALID_DATA, null, null);
            }

            // Simple Date format to save in DB
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date fromDateFormated = formatDate.parse(fromDate + " 00:00:00");
            Date toDateFormated = formatDate.parse(toDate + " 23:59:59");
            log.info("MessageBoxServiceImpl, getListMessageOfChatSectionByFromToDate()" + fromDate + ", " + toDate);

            List<MessageBox> messageBoxList = messageBoxRepository.getMessageBoxListByFromToDate(fromAdminId, toAdminId, fromDateFormated, toDateFormated);

            if (messageBoxList != null) {
                return new Response(Response.ResultCode.SUCCESS, messageBoxList, null);
            } else {
                return new Response(Response.ResultCode.MODEL_NOT_FOUND, null, null);
            }
        } catch (ParseException e) {
            log.error("Exception in MessageBoxServiceImpl, getListMessageOfChatSectionByFromToDate()", e);
            return new Response(Response.ResultCode.ERROR, null, "From or To date is wrong format");
        } catch (Exception e) {
            log.error("Exception in MessageBoxServiceImpl, getListMessageOfChatSectionByFromToDate()", e);
            return new Response(Response.ResultCode.ERROR, null, e.toString());
        }
    }

    @Override
    public Response getListChatSection(Admin currentAdmin, long currentPageRequest, long count) {
        try {
            int currentPage = currentPageRequest <= 0 ? 1 : (int) currentPageRequest;
            int itemCount = count > 0 ? (int) count : 100;
            int startNo = currentPage - 1;

            Page<ChatSection> chatSectionList;
            Pageable pageable = new PageRequest(startNo, itemCount);

            chatSectionList = chatSectionRepository.getChatSectionList(pageable);

            if (chatSectionList != null) {
                return new Response(Response.ResultCode.SUCCESS, chatSectionList, null);
            } else {
                return new Response(Response.ResultCode.MODEL_NOT_FOUND, null, null);
            }
        } catch (Exception e) {
            log.error("Exception in ChatSectionServiceImpl, getListChatSection(), e: " + e.toString());
            return new Response(Response.ResultCode.ERROR, null, e.toString());
        }
    }

    @Override
    public Response getListChatSectionAndMessageToPrint(Admin currentAdmin, String fromDate, String toDate, long currentPageRequest, long count) {
        try {
            if (fromDate == null || "".equals(fromDate.trim()) || toDate == null || "".equals(toDate.trim())) {
                return new Response(Response.ResultCode.INVALID_DATA, null, "From or To date is null or empty");
            }

            int currentPage = currentPageRequest <= 0 ? 1 : (int) currentPageRequest;
            int itemCount = count > 0 ? (int) count : 100;
            int startNo = currentPage - 1;

            Page<ChatSection> chatSectionList;
            Pageable pageable = new PageRequest(startNo, itemCount);

            chatSectionList = chatSectionRepository.getChatSectionList(pageable);

            if (chatSectionList != null) {
                List<Map<String, Object>> listConversation = new ArrayList<>();
                for (ChatSection chatSection : chatSectionList) {
                    Map<String, Object> mapConversation = new HashMap<>();
                    long fromAdminId = chatSection.getFromUser().getId();
                    long toAdminId = chatSection.getToUser().getId();
                    // Simple Date format
                    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date fromDateFormated = formatDate.parse(fromDate + " 00:00:00");
                    Date toDateFormated = formatDate.parse(toDate + " 23:59:59");
                    log.info("getListChatSectionAndMessage()" + fromAdminId + "," + toAdminId + "," + fromDateFormated + "," + toDateFormated);
                    List<MessageBox> messageBoxList = messageBoxRepository.getMessageBoxListByFromToDate(fromAdminId, toAdminId, fromDateFormated, toDateFormated);

                    if (messageBoxList != null) {
                        mapConversation.put("list_message", messageBoxList);
                    } else {
                        mapConversation.put("list_message", new ArrayList<>());
                    }
                    mapConversation.put("conversation", chatSection);
                    listConversation.add(mapConversation);
                }
                return new Response(Response.ResultCode.SUCCESS, listConversation, null);
            } else {
                return new Response(Response.ResultCode.MODEL_NOT_FOUND, null, "can not find any chat section");
            }
        } catch (ParseException e) {
            log.error("Exception in MessageBoxServiceImpl, getListChatSectionAndMessage()", e);
            return new Response(Response.ResultCode.ERROR, null, "From or To date is wrong format");
        } catch (Exception e) {
            log.error("Exception in MessageBoxServiceImpl, getListChatSectionAndMessage()", e);
            return new Response(Response.ResultCode.ERROR, null, e.toString());
        }
    }

}
