package com.example.carekeeper.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
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
     * Envia um e-mail usando um template HTML e substitui os placeholders.
     * Caso seja necessário, também adiciona uma imagem inline (por exemplo, o ícone de alerta).
     *
     * @param to          destinatário do e-mail
     * @param subject     assunto do e-mail
     * @param template    template HTML (enum)
     * @param placeholders mapa de placeholders e valores
     * @param contentId   identificador da imagem inline (ex: "alertIcon")
     * @param imagePath   caminho da imagem (em resources/static/images)
     */
    public void sendEmailWithInlineImage(String to, String subject, EmailTemplate template,
                                         Map<String, String> placeholders,
                                         String contentId, String imagePath) {
        try {
            // Carrega o template HTML
            ClassPathResource resource = new ClassPathResource(template.getPath());
            Path path = resource.getFile().toPath();
            String html = Files.readString(path);

            // Substitui placeholders {{chave}} → valor
            StringBuilder htmlBuilder = new StringBuilder(html);
            placeholders.forEach((key, value) -> {
                int start;
                while ((start = htmlBuilder.indexOf("{{" + key + "}}")) != -1) {
                    htmlBuilder.replace(start, start + key.length() + 4, value);
                }
            });

            String htmlFinal = htmlBuilder.toString().replace("{{timestamp}}", LocalDateTime.now().toString());

            // Cria a mensagem de e-mail
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlFinal, true);

            // 🔹 Adiciona imagem inline (ex: src="cid:alertIcon" no HTML)
            FileSystemResource image = new FileSystemResource(imagePath);
            helper.addInline(contentId, image);

            // Envia o e-mail
            mailSender.send(message);

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
}
