package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exeptions.BadMessageException;
import pro.sky.telegrambot.models.Tasks;
import pro.sky.telegrambot.models.Users;
import pro.sky.telegrambot.repository.TasksRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TaskService {
    private final TasksRepository tasksRepository;

    public TaskService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }


    /*
    метод постороеня модели задачи
     */
    private Tasks buildUser(String message, Users user) {
        Tasks task = new Tasks();
        // патерн
        Pattern pattern = Pattern.
                compile("(\\d{2}\\.\\d{2}\\.\\d{4})(\\s)(\\d{2}:\\d{2})(\\s+)(.+)");
        Matcher matcher = pattern.matcher(message);
        DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter TimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // проверка на патерн
        if (matcher.matches()) {
            task.setId(0L);
            task.setDate(LocalDate.parse(matcher.group(1), DateFormatter));
            task.setTime(LocalTime.parse(matcher.group(3), TimeFormatter).truncatedTo(ChronoUnit.MINUTES));
            task.setText(matcher.group(5));
            task.setUser(user);
            return task;
        }
        throw new BadMessageException();
    }

    /*
    добавить задачу
     */
    public Tasks addTask(String message, Users user) {
        Tasks tasks = buildUser(message, user);
        System.out.println(tasks.getTime());
        return tasksRepository.save(tasks);
    }

    /*
    найти задачу по дате
     */
    public List<Tasks> findTaskByDate(LocalDate date) {
        return tasksRepository.findAllByDate(date);
    }

    /*
    найти задачу по времени
     */
    public List<Tasks> findTaskByTime(LocalTime time) {
        return tasksRepository.findAllByTime(time);
    }

    /*
    найти задачу по дате и времени
     */
    public List<Tasks> findTaskByTimeAndDate(LocalDate date, LocalTime time) {
        return tasksRepository.findAllByDateAndTime(date, time);
    }

    /*
    получение чат id
     */
    public String getChatId(Tasks task) {
        return task.getUser().getChatId();
    }

    /*
    удаление задчи
     */
    public void delTask(Long id) {
        tasksRepository.deleteById(id);
    }
}
