package pro.sky.telegrambot.repository;

import liquibase.pro.packaged.L;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.models.Tasks;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TasksRepository extends JpaRepository<Tasks, Long> {
    List<Tasks> findAllByDate(LocalDate date);
    List<Tasks> findAllByTime(LocalTime time);
    List<Tasks> findAllByDateAndTime(LocalDate date, LocalTime time);
}
