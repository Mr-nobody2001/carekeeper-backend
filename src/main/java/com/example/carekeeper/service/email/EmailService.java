package com.example.carekeeper.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;

import com.example.carekeeper.enums.EmailTemplate;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envia e-mail usando um template específico e substituindo os placeholders.
     *
     * @param to       destinatário
     * @param subject  assunto do e-mail
     * @param template template a ser usado (enum)
     * @param placeholders mapa de placeholders e seus valores
     */
    public void sendEmail(String to, String subject, EmailTemplate template, Map<String, String> placeholders) {
        try {
            // Carrega template
            ClassPathResource resource = new ClassPathResource(template.getPath());
            Path path = resource.getFile().toPath();
            String html = Files.readString(path);

            // Substitui placeholders usando StringBuilder
            StringBuilder htmlBuilder = new StringBuilder(html);
            placeholders.forEach((key, value) -> {
                int start;
                while ((start = htmlBuilder.indexOf("{{" + key + "}}")) != -1) {
                    htmlBuilder.replace(start, start + key.length() + 4, value);
                }
            });

            // Substitui timestamp
            String htmlFinal = htmlBuilder.toString().replace("{{timestamp}}", LocalDateTime.now().toString());

            // Cria e envia
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlFinal, true);

            mailSender.send(message);

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
}
