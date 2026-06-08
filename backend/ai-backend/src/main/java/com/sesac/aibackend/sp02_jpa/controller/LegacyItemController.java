package com.sesac.aibackend.sp02_jpa.controller;

import com.sesac.aibackend.sp01_restapi.domain.Item;
import com.sesac.aibackend.sp01_restapi.dto.ItemRequest;
import com.sesac.aibackend.sp01_restapi.dto.ItemResponse;
import com.sesac.aibackend.sp01_restapi.error.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong; // 자바가 제공하는 기본 클래스

@RestController
@RequestMapping("/legacy/items")
public class LegacyItemController {

    private final Map<Long, Item> storage = new ConcurrentHashMap<>(); // 스토리지. 인메모리에 저장
    private final AtomicLong sequence = new AtomicLong(1); // 카운터. atomiclong : 안전하게 숫자를 증가 / 감소 시키기 위한 클래스

    @GetMapping
    public List<ItemResponse> list() {
        return storage.values().stream().map(ItemResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ItemResponse get(@PathVariable Long id) { // pathVariable 예시용
        Item item = storage.get(id);
        if (item == null) {
            throw NotFoundException.of("item", id);
        }
        return ItemResponse.from(item);
    }

    @PostMapping
    public ResponseEntity<ItemResponse> create(@Valid @RequestBody ItemRequest req) {
        long id = sequence.getAndIncrement(); // 1씩 증가. 이 메서드가 그렇다.
        Item saved = Item.builder().id(id).name(req.name()).price(req.price()).build();
        storage.put(id, saved); // 스토리지에 넣기
        return ResponseEntity.created(URI.create("/legacy/items/" + id)).body(ItemResponse.from(saved));
    }

    @PutMapping("/{id}")
    public ItemResponse update(@PathVariable Long id, @Valid @RequestBody ItemRequest req) { // @valid request를 검증하겠다는 것 (어노테이션으로 해놓은 제약조건을 확인한다) -> 실패시 400에러
        Item existing = storage.get(id);
        if (existing == null) {
            throw NotFoundException.of("item", id);
        }
        existing.setName(req.name());
        existing.setPrice(req.price());
        return ItemResponse.from(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (storage.remove(id) == null) {
            throw NotFoundException.of("item", id);
        }
        return ResponseEntity.noContent().build(); // nocontent는 404 떨어지고 끝난다.
    }

    // 지금 api들은 공부를 위한 흉내만 낸것

}
