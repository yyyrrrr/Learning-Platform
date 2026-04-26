package com.learningplatform.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learningplatform.entity.User;
import com.learningplatform.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserRepository, User> {
}
