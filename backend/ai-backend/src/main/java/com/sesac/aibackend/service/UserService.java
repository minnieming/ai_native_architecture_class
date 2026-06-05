package com.sesac.aibackend.service;

import com.sesac.aibackend.domain.User;
import com.sesac.aibackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 조회/저장 서비스.
 *
 * 순수 영속성 작업만 노출하고, "없으면 404" 같은 웹 계층 판단(NotFoundException)은
 * 컨트롤러에서 처리합니다. (ItemService와 동일한 책임 분리)
 */
@Service // 등록되어 빈 객체로 들어간다.
@RequiredArgsConstructor // 생성자 주입 -> 의존성 받아온다.
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true) // 커밋 롤백이 필요하면 transactional을 달아준다.
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional // 이때는 영향을 끼쳐야하기 때문에 readonly는 빼준다.
    public User save(User user) {
        return userRepository.save(user);  // save = insert & update
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
