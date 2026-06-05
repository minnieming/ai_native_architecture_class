package com.sesac.aibackend.service;

import com.sesac.aibackend.domain.Item;
import com.sesac.aibackend.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository repository;

    public List<Item> findAll() {
        return repository.findAll(); // jpa 레포지토리에 있는 것. findAll()이 리스트 형식으로 내보낸다 (컨트롤로 확인해보면 된다)
    }

    public Optional<Item> findById(Long id) { // 있을수도 있고 없을수도 있을때 -> optional
        return repository.findById(id); // 이거 형식이 optional임
    }

    public Item save(Item item) {
        return repository.save(item);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
