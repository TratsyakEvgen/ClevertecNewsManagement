package ru.clevertec.news.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.clevertec.news.dto.response.ResponseUser;

@FeignClient(name = "userClient")
public interface UserClient {
    @RequestMapping(method = RequestMethod.GET, value = "{username}?password={password}", produces = "application/json")
    ResponseUser getUser(@PathVariable("username") String username, @PathVariable("password") String password);
}
