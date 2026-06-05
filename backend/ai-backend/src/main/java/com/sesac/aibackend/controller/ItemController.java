package com.sesac.aibackend.controller;

import com.sesac.aibackend.domain.Item;
import com.sesac.aibackend.dto.ItemRequest;
import com.sesac.aibackend.dto.ItemResponse;
import com.sesac.aibackend.error.NotFoundException;
import com.sesac.aibackend.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemResponse> list() {
        return itemService.findAll().stream().map(ItemResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ItemResponse get(@PathVariable Long id) {
        Item item = itemService.findById(id)
                .orElseThrow(() -> NotFoundException.of("item", id));
        return ItemResponse.from(item);
    }

    @PostMapping
    public ResponseEntity<ItemResponse> create(@Valid @RequestBody ItemRequest req) {
        Item saved = itemService.save(req.toEntity()); // req.toEntity로 객체로 만들어준다.
        URI location = URI.create("/items/" + saved.getId()); // 서비스단에 보여주기 위해서
        return ResponseEntity.created(location).body(ItemResponse.from(saved));
    }

    @PutMapping("/{id}")
    public ItemResponse update(@PathVariable Long id, @Valid @RequestBody ItemRequest req) {
        Item item = itemService.findById(id) // id로 찾아서 오는건 영속된 상태로 오는 것.
                .orElseThrow(() -> NotFoundException.of("item", id)); // 결과가 나오면 영속된걸로 오는것
        item.setName(req.name());
        item.setPrice(req.price());
        return ItemResponse.from(itemService.save(item)); // 1차 캐시(스냅샷) 찍은거랑 달라 -> 그럼 영속화를 시작해야겠구나. 해서 영속성 컨텍스트를 실행한다.
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!itemService.existsById(id)) {
            throw NotFoundException.of("item", id);
        }
        itemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

