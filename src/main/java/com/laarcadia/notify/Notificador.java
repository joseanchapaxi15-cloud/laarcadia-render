package com.laarcadia.notify;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class Notificador {
    private static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    private static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    private static final String FROM = System.getenv("TWILIO_PHONE_NUMBER"); // ej: whatsapp:+14155238886
    private static final String ADMIN_TO = System.getenv("ADMIN_TO");        // ej: whatsapp:+593998667865

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static void enviarWhatsApp(String mensaje) {
        try {
            Message message = Message.creator(
                new PhoneNumber(ADMIN_TO),
                new PhoneNumber(FROM),
                mensaje
            ).create();
            System.out.println("✅ WhatsApp enviado al administrador: " + message.getSid());
        } catch (Exception e) {
            System.out.println("❌ Error al enviar WhatsApp al administrador: " + e.getMessage());
        }
    }

    public static void enviarWhatsAppCliente(String numeroCliente, String mensaje) {
        try {
            String to = numeroCliente.startsWith("whatsapp:")
                ? numeroCliente
                : "whatsapp:" + numeroCliente;

            Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(FROM),
                mensaje
            ).create();
            System.out.println("✅ WhatsApp enviado al cliente: " + message.getSid());
        } catch (Exception e) {
            System.out.println("❌ Error al enviar WhatsApp al cliente: " + e.getMessage());
        }
    }
}