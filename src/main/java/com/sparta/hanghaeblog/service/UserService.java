package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.dto.LoginRequestDto;
import com.sparta.hanghaeblog.dto.SignupRequestDto;
import com.sparta.hanghaeblog.entity.User;
import com.sparta.hanghaeblog.entity.UserRoleEnum;
import com.sparta.hanghaeblog.repository.UserRepository;
import com.sparta.hanghaeblog.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public ResponseEntity<Map<String,HttpStatus>> signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            return new ResponseEntity("중복된 username 입니다.",HttpStatus.BAD_REQUEST);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                return new ResponseEntity("관리자 암호가 틀려 등록이 불가능합니다.",HttpStatus.BAD_REQUEST);
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, role);
        userRepository.save(user);
        return new ResponseEntity("등록 되었습니다.",HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String,HttpStatus>> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        Optional<User> user = userRepository.findByUsername(username);
        // 비밀번호 확인
        if(!user.isPresent()){
            return new ResponseEntity("회원을 찾을 수 없습니다.",HttpStatus.BAD_REQUEST);
        }
        if(!passwordEncoder.matches(password, user.get().getPassword())){
            return new ResponseEntity("회원을 찾을 수 없습니다.",HttpStatus.BAD_REQUEST);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.get().getUsername(), user.get().getRole()));
        return new ResponseEntity("로그인 했습니다.",HttpStatus.OK);
    }
}