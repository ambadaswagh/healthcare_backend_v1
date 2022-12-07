package com.healthcare.chat.service.messageBox;


import com.healthcare.chat.model.ChatMessageModel;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.response.Response;

/**
 *
 * @author tungn
 */
public interface MessageBoxService {

    Response getListMessageBox(Admin currentAdmin, long currentPageRequest, long toAdmin);

    Response createMessageBox(Admin currentAdmin, ChatMessageModel messageModel);
}
