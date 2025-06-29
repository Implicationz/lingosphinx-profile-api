package com.lingosphinx.profile;

import com.lingosphinx.profile.service.MockUserService;
import com.lingosphinx.profile.service.UserService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {
    @Bean
    public UserService userService() {
        return new MockUserService();
    }
}
