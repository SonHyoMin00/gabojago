package com.hanium.gabojago.jwt;

import com.hanium.gabojago.domain.User;
import com.hanium.gabojago.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =  userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 사용자가 없습니다."));
        return new UserDetailsImpl(user);
    }
}
