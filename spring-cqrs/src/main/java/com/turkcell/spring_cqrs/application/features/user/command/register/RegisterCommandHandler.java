package com.turkcell.spring_cqrs.application.features.user.command.register;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.turkcell.spring_cqrs.application.features.user.mapper.UserMapper;
import com.turkcell.spring_cqrs.application.features.user.rule.UserBusinessRules;
import com.turkcell.spring_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.spring_cqrs.domain.Role;
import com.turkcell.spring_cqrs.domain.User;
import com.turkcell.spring_cqrs.persistence.repository.RoleRepository;
import com.turkcell.spring_cqrs.persistence.repository.UserRepository;

@Component
public class RegisterCommandHandler implements CommandHandler<RegisterCommand, RegisterResponse>
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserBusinessRules userBusinessRules;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    
    public RegisterCommandHandler(UserRepository userRepository, RoleRepository roleRepository, UserBusinessRules userBusinessRules,
            PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userBusinessRules = userBusinessRules;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }



    @Override
    public RegisterResponse handle(RegisterCommand command) {
        this.userBusinessRules.userWithSameEmailMustNotExist(command.email());

        Role defaultRole = roleRepository.findByName("USER")
            .orElseGet(() -> {
                Role role = new Role();
                role.setName("USER");
                return roleRepository.save(role);
            });

        User user = userMapper.userFromRegisterCommand(
            command,
            passwordEncoder.encode(command.password()),
            List.of(defaultRole)
        );

        userRepository.save(user);

        return userMapper.registerResponseFromUser(user);
    }
}
    