package com.zestindiait.service;

import com.zestindiait.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("UserService" )
public interface UserServiceFeignClient {
    @GetMapping("/user/token/validate")
    User getUserDetails(@RequestHeader("Authorization") String token);

}
