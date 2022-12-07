package com.healthcare.chat.service.chatSection;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.response.Response;

/**
 *
 * @author tungn
 */
public interface ChatSectionService {

    Response getListMessageOfChatSection(Admin currentAdmin, long fromAdmin, long toAdmin, long currentPageRequest);

    Response getListChatSection(Admin currentAdmin, long currentPageRequest, long itemCount);

    Response getListMessageOfChatSectionByFromToDate(Admin currentAdmin, long fromAdminId, long toAdminId, String fromDate, String toDate);

    Response getListChatSectionAndMessageToPrint(Admin currentAdmin, String fromDate, String toDate, long currentPageRequest, long count);

}
