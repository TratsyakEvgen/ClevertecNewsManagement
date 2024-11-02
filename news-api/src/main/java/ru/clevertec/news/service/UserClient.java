package ru.clevertec.news.service;


import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.clevertec.news.dto.response.ResponseUser;

/**
 * Сервис для управления пользователями
 */
@FeignClient(name = "userClient")
public interface UserClient {
    /**
     * Предоставляет информацию о пользователе (роль) от стороннего сервиса
     *
     * @param username имя пользователя
     * @param password пароль для пользователя
     * @return информация о пользователе (роль)
     * @throws FeignException - в случае неудачного запроса к сервису
     */
    @RequestMapping(method = RequestMethod.GET, value = "{username}?password={password}", produces = "application/json")
    ResponseUser getUserInfo(@PathVariable("username") String username, @PathVariable("password") String password);
}
