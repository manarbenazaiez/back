package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.SavingsGoal;

@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {
}