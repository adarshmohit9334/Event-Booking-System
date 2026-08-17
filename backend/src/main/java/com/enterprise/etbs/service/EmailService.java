package com.enterprise.etbs.service;

import com.enterprise.etbs.entity.Booking;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${app.frontend-url:http://localhost:8080}")
    private String frontendUrl;

    @Async
    public void sendBookingConfirmation(Booking booking) {
        String subject = "Ticket Confirmed! " + booking.getEventTitle();
        String downloadUrl = frontendUrl + "/ticket-invoice.html?bookingId=" + booking.getId();
        
        String htmlContent = "<html><body style='font-family: Arial, sans-serif; background-color: #f4f4f5; padding: 20px; color: #1f2937;'>"
                + "<div style='max-width: 600px; margin: 0 auto; background: white; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.05); border: 1px solid #e4e4e7;'>"
                + "<div style='background: linear-gradient(135deg, #8b5cf6, #ec4899); padding: 30px; text-align: center; color: white;'>"
                + "<h1 style='margin: 0; font-size: 24px; font-weight: bold;'>EVENTIFY RESERVATION</h1>"
                + "<p style='margin: 5px 0 0 0; opacity: 0.9;'>Your ticket is secured & confirmed</p>"
                + "</div>"
                + "<div style='padding: 30px;'>"
                + "<p style='font-size: 16px; margin-top: 0;'>Hello,</p>"
                + "<p style='font-size: 14px; line-height: 1.5;'>Thank you for booking with Eventify. Your payment has been verified successfully. Below are your booking details:</p>"
                + "<table style='width: 100%; border-collapse: collapse; margin: 20px 0;'>"
                + "<tr><td style='padding: 8px 0; font-weight: bold; border-bottom: 1px solid #f4f4f5;'>Booking ID:</td><td style='padding: 8px 0; text-align: right; border-bottom: 1px solid #f4f4f5; color: #8b5cf6; font-weight: bold;'>" + booking.getId().toUpperCase() + "</td></tr>"
                + "<tr><td style='padding: 8px 0; font-weight: bold; border-bottom: 1px solid #f4f4f5;'>Event:</td><td style='padding: 8px 0; text-align: right; border-bottom: 1px solid #f4f4f5;'>" + booking.getEventTitle() + "</td></tr>"
                + "<tr><td style='padding: 8px 0; font-weight: bold; border-bottom: 1px solid #f4f4f5;'>Venue:</td><td style='padding: 8px 0; text-align: right; border-bottom: 1px solid #f4f4f5;'>" + booking.getEventLocation() + "</td></tr>"
                + "<tr><td style='padding: 8px 0; font-weight: bold; border-bottom: 1px solid #f4f4f5;'>Date & Time:</td><td style='padding: 8px 0; text-align: right; border-bottom: 1px solid #f4f4f5;'>" + booking.getEventDate() + " @ " + booking.getEventTime() + "</td></tr>"
                + "<tr><td style='padding: 8px 0; font-weight: bold; border-bottom: 1px solid #f4f4f5;'>Seats Booked:</td><td style='padding: 8px 0; text-align: right; border-bottom: 1px solid #f4f4f5; font-weight: bold;'>" + booking.getSeats() + "</td></tr>"
                + "<tr><td style='padding: 8px 0; font-weight: bold;'>Total Paid:</td><td style='padding: 8px 0; text-align: right; font-weight: bold; color: #10b981; font-size: 16px;'>₹" + booking.getTotalPrice() + "</td></tr>"
                + "</table>"
                + "<div style='text-align: center; margin: 35px 0 10px 0;'>"
                + "<a href='" + downloadUrl + "' style='display: inline-block; background: linear-gradient(135deg, #8b5cf6, #ec4899); color: white; text-decoration: none; padding: 12px 24px; border-radius: 8px; font-weight: bold; font-size: 15px; box-shadow: 0 4px 10px rgba(139, 92, 246, 0.3);'>Download / Print Ticket</a>"
                + "<p style='font-size: 12px; color: #6b7280; margin-top: 15px;'>Present this booking reference at the gate entrance.</p>"
                + "</div>"
                + "</div>"
                + "<div style='background-color: #f9fafb; padding: 20px; text-align: center; font-size: 12px; color: #9ca3af; border-top: 1px solid #e4e4e7;'>"
                + "© 2024 Eventify Portal. All rights reserved."
                + "</div>"
                + "</div>"
                + "</body></html>";

        // Print email structure in a beautiful ASCII container directly to console log as fallback
        printReceiptToConsole(booking, downloadUrl);

        if (mailSender == null) {
            System.out.println("[EmailService] JavaMailSender is not initialized. Using Console log receipt delivery.");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(booking.getUserEmail());
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            System.out.println("[EmailService] HTML receipt successfully sent to: " + booking.getUserEmail());
        } catch (Exception e) {
            System.err.println("[EmailService] Failed to send email via SMTP, fallback to console log: " + e.getMessage());
        }
    }

    private void printReceiptToConsole(Booking booking, String downloadUrl) {
        System.out.println("\n"
                + "┌──────────────────────────────────────────────────────────────┐\n"
                + "│              ✉ NEW EMAIL CONFIRMATION SENT                  │\n"
                + "├──────────────────────────────────────────────────────────────┤\n"
                + "│ To:      " + String.format("%-44s", booking.getUserEmail()) + "│\n"
                + "│ Subject: " + String.format("%-44s", "Ticket Confirmed! " + booking.getEventTitle()) + "│\n"
                + "├──────────────────────────────────────────────────────────────┤\n"
                + "│                   ETICKET DETAILS                            │\n"
                + "│                                                              │\n"
                + "│ Booking Ref: " + String.format("%-44s", booking.getId().toUpperCase()) + "│\n"
                + "│ Event Name:  " + String.format("%-44s", booking.getEventTitle()) + "│\n"
                + "│ Date/Time:   " + String.format("%-44s", booking.getEventDate() + " @ " + booking.getEventTime()) + "│\n"
                + "│ Venue:       " + String.format("%-44s", booking.getEventLocation()) + "│\n"
                + "│ Seats:       " + String.format("%-44s", booking.getSeats()) + "│\n"
                + "│ Total Price: " + String.format("%-44s", "INR " + booking.getTotalPrice()) + "│\n"
                + "│                                                              │\n"
                + "│ Download Link: " + String.format("%-42s", downloadUrl) + "│\n"
                + "│                                                              │\n"
                + "│ Status:      CONFIRMED & VERIFIED                            │\n"
                + "└──────────────────────────────────────────────────────────────┘\n");
    }

    private String getLocalWifiIp() {
        try {
            java.util.Enumeration<java.net.NetworkInterface> interfaces = java.net.NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                java.net.NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }
                java.util.Enumeration<java.net.InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    java.net.InetAddress addr = addresses.nextElement();
                    if (addr instanceof java.net.Inet4Address) {
                        String ip = addr.getHostAddress();
                        // Filter out common local networks
                        if (ip.startsWith("192.168.") || ip.startsWith("10.") || ip.startsWith("172.")) {
                            return ip;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to resolve local network interface IP: " + e.getMessage());
        }
        return "localhost"; // Default fallback
    }
}
