package pro.sky.telegrambot.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pengrad.telegrambot.model.User;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // id пользователя
    @Column(nullable = false, name = "chat_id")
    private String chatId;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Collection<Tasks> tasks;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Collection<Tasks> getUsers() {
        return tasks;
    }

    public void setUsers(Collection<Tasks> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return id == users.id && Objects.equals(chatId, users.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId);
    }
}
