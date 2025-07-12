// package com.maison.vinitrackpro.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.stereotype.Service;

// import java.time.LocalDateTime;

// @Service
// public class EmailService {

//     @Autowired
//     private JavaMailSender emailSender;

//     public void sendCustomerWelcomeEmail(String to, String companyName, String contactPerson) {
//         SimpleMailMessage message = new SimpleMailMessage();
//         message.setTo(to);
//         message.setSubject("Welcome to ViniTrack Pro - " + companyName);
//         message.setText(String.format(
//             "Dear %s,\n\n" +
//             "We're pleased to inform you that %s has been successfully registered in our ViniTrack Pro system.\n\n" +
//             "This means you can now:\n" +
//             "- Receive delivery notifications\n" +
//             "- Track your wine shipments in real-time\n" +
//             "- Access your order history\n\n" +
//             "Our team will be your primary point of contact for all logistics operations.\n\n" +
//             "If you have any questions about our services, please don't hesitate to contact us.\n\n" +
//             "Best regards,\n" +
//             "The ViniTrack Pro Team\n" +
//             "%s",
//             contactPerson,
//             companyName,
//             LocalDateTime.now().getYear()
//         ));

//         emailSender.send(message);
//     }

//     public void sendDriverAssignmentEmail(String to, String driverName, String vehicleDetails, LocalDateTime assignmentDate) {
//         SimpleMailMessage message = new SimpleMailMessage();
//         message.setTo(to);
//         message.setSubject("New Driver Assignment - ViniTrack Pro");
//         message.setText(String.format(
//             "Hello %s,\n\n" +
//             "You have been assigned as the primary driver for %s in our system.\n\n" +
//             "Assignment Details:\n" +
//             "- Vehicle: %s\n" +
//             "- Assignment Date: %s\n\n" +
//             "Please report any discrepancies to your logistics manager.\n\n" +
//             "Regards,\n" +
//             "ViniTrack Pro Logistics Team",
//             driverName,
//             vehicleDetails,
//             assignmentDate.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy"))
//         ));

//         emailSender.send(message);
//     }
// }



package com.maison.vinitrackpro.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.maison.vinitrackpro.model.ProductCategory;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public void sendCustomerWelcomeEmail(String to, String companyName, String contactPerson) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to ViniTrack Pro - " + companyName);
        message.setText(String.format(
            "Dear %s,\n\n" +
            "We're pleased to inform you that %s has been successfully registered in our ViniTrack Pro system.\n\n" +
            "This means you can now:\n" +
            "- Receive delivery notifications\n" +
            "- Track your wine shipments in real-time\n" +
            "- Access your order history\n\n" +
            "Our team will be your primary point of contact for all logistics operations.\n\n" +
            "If you have any questions about our services, please don't hesitate to contact us.\n\n" +
            "Best regards,\n" +
            "The ViniTrack Pro Team\n" +
            "%s",
            contactPerson,
            companyName,
            LocalDateTime.now().getYear()
        ));
        emailSender.send(message);
    }

    // public void sendSupplierWelcomeEmail(String to, String companyName, String contactPerson, Set<ProductCategory> productCategories) {
    //     SimpleMailMessage message = new SimpleMailMessage();
    //     message.setTo(to);
    //     message.setSubject("Welcome to ViniTrack Pro Supplier Network - " + companyName);
        
    //     // Format product categories for display
    //     String categoriesText = productCategories.stream()
    //         .map(ProductCategory::name)
    //         .collect(Collectors.joining(", "));
            
    //     message.setText(String.format(
    //         "Dear %s,\n\n" +
    //         "We're pleased to inform you that %s has been successfully registered as a supplier in our ViniTrack Pro system.\n\n" +
    //         "Your company has been registered for the following product categories: %s\n\n" +
    //         "As a registered supplier, you can now:\n" +
    //         "- Receive purchase orders electronically\n" +
    //         "- Track delivery schedules\n" +
    //         "- Access your supply history\n" +
    //         "- Update product availability\n\n" +
    //         "Our inventory management team will be your primary point of contact for all procurement operations.\n\n" +
    //         "If you have any questions about our procurement process, please don't hesitate to contact us.\n\n" +
    //         "Best regards,\n" +
    //         "The ViniTrack Pro Procurement Team\n" +
    //         "%s",
    //         contactPerson,
    //         companyName,
    //         categoriesText,
    //         LocalDateTime.now().getYear()
    //     ));
    //     emailSender.send(message);
    // }

    public void sendSupplierWelcomeEmail(String to, String companyName, String contactPerson, ProductCategory productCategory) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to ViniTrack Pro Supplier Network - " + companyName);
        
        message.setText(String.format(
            "Dear %s,\n\n" +
            "We're pleased to inform you that %s has been successfully registered as a supplier in our ViniTrack Pro system.\n\n" +
            "Your company has been registered for the product category: %s\n\n" +
            "As a registered supplier, you can now:\n" +
            "- Receive purchase orders electronically\n" +
            "- Track delivery schedules\n" +
            "- Access your supply history\n" +
            "- Update product availability\n\n" +
            "Our inventory management team will be your primary point of contact for all procurement operations.\n\n" +
            "If you have any questions about our procurement process, please don't hesitate to contact us.\n\n" +
            "Best regards,\n" +
            "The ViniTrack Pro Procurement Team\n" +
            "%s",
            contactPerson,
            companyName,
            productCategory != null ? productCategory.name() : "Not specified",
            LocalDateTime.now().getYear()
        ));
        emailSender.send(message);
    }
    

    public void sendDriverAssignmentEmail(String to, String driverName, String vehicleDetails, LocalDateTime assignmentDate) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("New Driver Assignment - ViniTrack Pro");
        message.setText(String.format(
            "Hello %s,\n\n" +
            "You have been assigned as the primary driver for %s in our system.\n\n" +
            "Assignment Details:\n" +
            "- Vehicle: %s\n" +
            "- Assignment Date: %s\n\n" +
            "Please report any discrepancies to your logistics manager.\n\n" +
            "Regards,\n" +
            "ViniTrack Pro Logistics Team",
            driverName,
            vehicleDetails,
            assignmentDate.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        ));
        emailSender.send(message);
    }
}
