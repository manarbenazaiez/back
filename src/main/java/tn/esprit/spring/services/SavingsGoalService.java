package tn.esprit.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.SavingsGoal;
import tn.esprit.spring.repositories.SavingsGoalRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SavingsGoalService {
    private final SavingsGoalRepository savingsGoalRepository;

    @Autowired
    public SavingsGoalService(SavingsGoalRepository savingsGoalRepository) {
        this.savingsGoalRepository = savingsGoalRepository;
    }

    public List<SavingsGoal> getAllSavingsGoals() {
        return savingsGoalRepository.findAll();
    }

    public SavingsGoal createSavingsGoal(SavingsGoal savingsGoal) {
        savingsGoal.setCurrentAmount(BigDecimal.ZERO);
        return savingsGoalRepository.save(savingsGoal);
    }

    public SavingsGoal getSavingsGoalById(Long id) {
        return savingsGoalRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Savings goal not found"));
    }

    public void updateSavingsGoal(SavingsGoal savingsGoal) {
        savingsGoalRepository.save(savingsGoal);
    }

    public void deleteSavingsGoal(Long id) {
        savingsGoalRepository.deleteById(id);
    }

    public void depositToGoal(Long id, BigDecimal amount) {
        SavingsGoal savingsGoal = getSavingsGoalById(id);
        BigDecimal currentAmount = savingsGoal.getCurrentAmount();
        savingsGoal.setCurrentAmount(currentAmount.add(amount));
        savingsGoalRepository.save(savingsGoal);
    }

    public void withdrawFromGoal(Long id, BigDecimal amount) {
        SavingsGoal savingsGoal = getSavingsGoalById(id);
        BigDecimal currentAmount = savingsGoal.getCurrentAmount();
        BigDecimal newAmount = currentAmount.subtract(amount);
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient funds for withdrawal");
        }
        savingsGoal.setCurrentAmount(newAmount);
        savingsGoalRepository.save(savingsGoal);
    }

    public BigDecimal calculateProgressPercentage(Long id) {
        SavingsGoal savingsGoal = getSavingsGoalById(id);
        BigDecimal currentAmount = savingsGoal.getCurrentAmount();
        BigDecimal targetAmount = savingsGoal.getTargetAmount();

        if (targetAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return currentAmount.divide(targetAmount, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal calculateRemainingAmount(Long id) {
        SavingsGoal savingsGoal = getSavingsGoalById(id);
        BigDecimal currentAmount = savingsGoal.getCurrentAmount();
        BigDecimal targetAmount = savingsGoal.getTargetAmount();
        return targetAmount.subtract(currentAmount);
    }
}
