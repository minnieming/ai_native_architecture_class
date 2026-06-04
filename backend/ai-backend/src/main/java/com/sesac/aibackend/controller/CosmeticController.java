package com.sesac.aibackend.controller;

import com.sesac.aibackend.domain.Cosmetic;
import com.sesac.aibackend.dto.CosmeticRequest;
import com.sesac.aibackend.dto.CosmeticResponse;
import com.sesac.aibackend.error.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/legacy/cosmetics")
public class CosmeticController {

    private final Map<Long, Cosmetic> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @PostMapping
    public ResponseEntity<CosmeticResponse> create (@Valid @RequestBody CosmeticRequest req) {
        long id = sequence.getAndIncrement();

        Cosmetic saved = Cosmetic.builder()
                .id(id)
                .sort(req.sort())
                .name(req.name())
                .price(req.price())
                .build();

        storage.put(id, saved);

        return ResponseEntity.created(URI.create("/legacy/cosmetics" + id)).body(CosmeticResponse.from(saved));

    }

    @GetMapping("/{id}")
    public CosmeticResponse get (@PathVariable Long id) {
        Cosmetic cosmetic = storage.get(id);

        if (cosmetic == null) throw NotFoundException.of("cosmetic", id);

        return CosmeticResponse.from(cosmetic);
    }

    @PutMapping("/{id}")
    public CosmeticResponse update (@PathVariable Long id, @Valid @RequestBody CosmeticRequest req) {
        Cosmetic existing = storage.get(id);

        if (existing == null) throw NotFoundException.of("cosmetic", id);

        existing.setSort(req.sort());
        existing.setName(req.name());
        existing.setPrice(req.price());

        return CosmeticResponse.from(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        if (storage.remove(id) == null) throw NotFoundException.of("cosmetic", id);
        storage.remove(id);
        return ResponseEntity.noContent().build();
    }
}
