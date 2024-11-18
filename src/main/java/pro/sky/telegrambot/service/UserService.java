package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.models.Users;
import pro.sky.telegrambot.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /*
    добавление пользователя
     */
    public Users addUser(Users user) {
        return userRepository.save(user);
    }

    /*
    поиск пользователя по чат id
     */
    public Users findUserByChatId(String chatId) {
        return userRepository.findFirstByChatId(chatId).orElse(null);
    }

}
