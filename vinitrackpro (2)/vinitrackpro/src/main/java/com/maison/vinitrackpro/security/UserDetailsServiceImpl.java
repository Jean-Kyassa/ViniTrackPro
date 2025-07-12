package com.maison.vinitrackpro.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maison.vinitrackpro.model.User;
import com.maison.vinitrackpro.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user by username: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User Not Found with username: {}", username);
                    return new UsernameNotFoundException("User Not Found with username: " + username);
                });

        logger.info("User found: {}, password hash starts with: {}", 
                   username, 
                   user.getPassword() != null ? user.getPassword().substring(0, Math.min(10, user.getPassword().length())) : "null");
        
        return UserDetailsImpl.build(user);
    }
}
