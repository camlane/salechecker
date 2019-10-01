package org.icognition.salechecker.mail

import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
class EmailService {

  companion object : KLogging()

  @Autowired
  lateinit var emailSender: JavaMailSender

  fun sendSimpleMessage(recipient: String, subject: String, bodyHtml: String) {

    val mimeMessage = emailSender.createMimeMessage()

    MimeMessageHelper(mimeMessage, true).apply {
      setText(bodyHtml, true)
      setSubject(subject)
      setTo(recipient)
      setFrom("alert@sitescanner.com")
    }

    emailSender.send(mimeMessage)
    logger.info { "Mail sent to: $recipient" }
  }
}