package com.lexuancong.share.utils;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessagesUtils {
    // tìm trong directory resource và nạp vào bundle
    private static final ResourceBundle messageBundle =
            ResourceBundle.getBundle("messages.messages", Locale.getDefault());

    public static String getMessage(String errorKey , Object... params) {
        String message ;
        try {
            message = messageBundle.getString(errorKey);
        } catch (MissingResourceException ex) {
            message = errorKey;
        }
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(message, params);
        return formattingTuple.getMessage();


    }

}
