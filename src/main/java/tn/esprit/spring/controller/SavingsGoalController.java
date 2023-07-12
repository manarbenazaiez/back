package tn.esprit.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.SavingsGoal;
import tn.esprit.spring.services.RabbitMQService;
import tn.esprit.spring.services.SavingsGoalService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/savings-goals")
public class SavingsGoalController {
    @Autowired
    private SavingsGoalService savingsGoalService;
    @Autowired

    private JavaMailSender javaMailSender;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQService rabbitMQService;

    @GetMapping
    public List<SavingsGoal> getAllSavingsGoals() {
        return savingsGoalService.getAllSavingsGoals();
    }

    @GetMapping("/{id}")
    public SavingsGoal getSavingsGoalById(@PathVariable Long id) {
        return savingsGoalService.getSavingsGoalById(id);
    }

    @PostMapping
    public SavingsGoal createSavingsGoal(@RequestBody SavingsGoal savingsGoal, @RequestParam String email) {
        savingsGoal.setEmail(email);
        SavingsGoal createdGoal = savingsGoalService.createSavingsGoal(savingsGoal);

        // Send the email
        String subject = "Account Creation";
        String message = "Your account has been created successfully.";
        sendEmail(email, subject, message);

        return createdGoal;
    }


    @PutMapping("/{id}")
    public void updateSavingsGoal(@PathVariable Long id, @RequestBody SavingsGoal savingsGoal) {
        savingsGoal.setId(id);
        savingsGoalService.updateSavingsGoal(savingsGoal);
    }

    @DeleteMapping("/{id}")
    public void deleteSavingsGoal(@PathVariable Long id) {
        savingsGoalService.deleteSavingsGoal(id);
    }

    @PostMapping("/{id}/deposit")
    public void depositToGoal(@PathVariable Long id, @RequestParam BigDecimal amount) {
        savingsGoalService.depositToGoal(id, amount);
    }

    @PostMapping("/{id}/withdraw")
    public void withdrawFromGoal(@PathVariable Long id, @RequestParam BigDecimal amount) {
        savingsGoalService.withdrawFromGoal(id, amount);
    }

    @GetMapping("/{id}/progress")
    public BigDecimal calculateProgressPercentage(@PathVariable Long id) {
        return savingsGoalService.calculateProgressPercentage(id);
    }

    @GetMapping("/{id}/remaining")
    public BigDecimal calculateRemainingAmount(@PathVariable Long id) {
        return savingsGoalService.calculateRemainingAmount(id);
    }

    @PostMapping("/rabbitmq")
    public void sendSavingsGoalToRabbitMQ(@RequestBody SavingsGoal savingsGoal, @RequestParam String email) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String savingsGoalJson = objectMapper.writeValueAsString(savingsGoal);
            rabbitTemplate.convertAndSend("savings-goal.created", savingsGoalJson);

            // Send the email
            String subject = "Account Information";
            String message = "Your account information: ..."; // Construct the email message
            sendEmail(email, subject, message);
        } catch (Exception e) {
            // Handle exception
        }
    }

    private void sendEmail(String email, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
    }
}