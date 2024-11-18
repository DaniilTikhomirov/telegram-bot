package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exeptions.BadMessageException;
import pro.sky.telegrambot.models.Tasks;
import pro.sky.telegrambot.models.Users;
import pro.sky.telegrambot.service.TaskService;
import pro.sky.telegrambot.service.UserService;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    private final TaskService taskService;

    private final UserService userService;


    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      TaskService taskService,
                                      UserService userService) {
        this.telegramBot = telegramBot;
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            Long id = update.message().chat().id();
            logger.info("Processing update: {}", update);
            // команда старт
            if (update.message().text().equals("/start")) {
                SendMessage message = new SendMessage(
                        id,
                        "hello its toDo list");
                // если пользователя не заносим в бд
                if(userService.findUserByChatId(String.valueOf(id)) == null){
                    Users user = new Users();
                    user.setChatId(String.valueOf(id));
                    user = userService.addUser(user);
                }
                telegramBot.execute(message);
            }
            else{
                // проверка на коректное сообщение
                try{
                    // проверяем есть ли такой пользователь
                    Users user = userService.findUserByChatId(String.valueOf(id));
                    if(user == null){
                        SendMessage message = new SendMessage(
                                update.message().chat().id(),
                                "вы похоже что то сломали напишите /start"
                        );
                        // если есть добавляем задачу в бд
                    }else{
                        System.out.println(taskService.addTask(update.message().text(), user));
                    }
                    // если не коректное
                }catch(BadMessageException e){
                    SendMessage message = new SendMessage(
                            update.message().chat().id(),
                            "не правильный формат введите в формате \"dd.MM.yyyy HH:mm text\" пример: " +
                                    "01.01.2022 20:00 Сделать домашнюю работу"
                    );
                    telegramBot.execute(message);
                }
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /*
    проверка есть ли задачт о который надо напомнить (проверка каждую минуту)
     */
    @Scheduled(cron = "0 0/1 * * * *")
    public void notification() {
        List<Tasks> tasks = taskService.findTaskByTimeAndDate(LocalDate.now(), LocalTime.now().truncatedTo(ChronoUnit.MINUTES));
        for (Tasks task : tasks) {
            if (task != null) {
                SendMessage message = new SendMessage(
                        Long.parseLong(taskService.getChatId(task)),
                        task.getText()
                );
                telegramBot.execute(message);
                taskService.delTask(task.getId());
            }
        }

        }

}


