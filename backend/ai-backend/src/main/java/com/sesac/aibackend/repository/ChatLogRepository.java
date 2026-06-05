package com.sesac.aibackend.repository;

import com.sesac.aibackend.domain.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {

    List<ChatLog> findByUserIdOrderByCreatedAtDesc(Long userId); // 이건 공식문서 보고 하는 수 밖에 없다.

    @Query("""
          select c from ChatLog c
          join fetch c.user
          where c.user.id = :userId
          order by c.createdAt desc
          """) // jpql을 직접 만들어서 쿼리를 날리는것. 자체로 쿼리를 날리는것. fetch join이라고 보면 된다.
    // select를 한번으로 다 조회 완료하기.
    List<ChatLog> findByUserIdWithUser(Long userId);
    // 이건 영속상태에 영향을 받지 않음. (fetch 조인해서 트랜잭션이 닫혀도 (service) 점검할 수 있다
}
