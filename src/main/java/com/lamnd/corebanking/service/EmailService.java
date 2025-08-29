package com.lamnd.corebanking.service;

import com.lamnd.corebanking.dto.EmailDetails;

public interface EmailService {
    void sendEmail(EmailDetails emailDetails);
}
