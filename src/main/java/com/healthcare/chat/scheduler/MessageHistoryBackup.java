/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.healthcare.chat.scheduler;

import com.healthcare.chat.model.entity.MessageBox;
import com.healthcare.chat.model.entity.MessageBoxBackup;
import com.healthcare.chat.repository.MessageBoxBackupRepository;
import com.healthcare.chat.repository.MessageBoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author johnny
 */
@Component
public class MessageHistoryBackup {

    private SimpleDateFormat fmDay = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat sdf = new SimpleDateFormat("MMM YYYY");
    // Backup all old meesges over 3 months
    private int MONTH_BACKUP = 3;
    // Default record size to process
    Pageable pageable = new PageRequest(0, 25);
    
    @Autowired
    MessageBoxRepository boxRepository;
    
    @Autowired
    MessageBoxBackupRepository boxBackupRepository;

    // Run schedule every 24 hours
//    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void backupMessage() {
        try {
            migrateHistoryMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 
     * @throws ParseException 
     */
    private void migrateHistoryMessage() throws ParseException {

        Calendar cal = Calendar.getInstance();
        // Set date time to gest report from {{month}} ago
        // Count from current month
        cal.add(Calendar.MONTH, -1 * MONTH_BACKUP);
        cal.set(Calendar.DAY_OF_MONTH, 1);        
        String filterDateString = fmDay.format(cal.getTime()) + " 00:00:00";
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date filterDate = fm.parse(filterDateString);     
        // Get old message
        List<MessageBox> messageBoxs=boxRepository.getMessageBoxToDate(filterDate, pageable);
        if(messageBoxs!=null && messageBoxs.size()>0){
            for(MessageBox item: messageBoxs){
                MessageBoxBackup messageBoxBackup=new MessageBoxBackup(item);
                messageBoxBackup.setId(item.getId());
                // Saving backup item
                boxBackupRepository.save(messageBoxBackup);
                // Delete old one
                boxRepository.delete(item);
            }
        }
    
    }
}
