package com.zestindiait.externalservice;

import com.zestindiait.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("UserService" )
public interface UserServiceFeignClient {
    @GetMapping("/user/{userId}")
    User getUserDetails(@PathVariable("userId") String userId);

}
