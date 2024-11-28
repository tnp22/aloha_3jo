package com.aloha.movie_project.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;


    
}