package com.sesac.aibackend.sp01_hw_cosmetic.service;

import com.sesac.aibackend.sp01_hw_cosmetic.domain.Cosmetic;
import com.sesac.aibackend.sp01_hw_cosmetic.dto.CosmeticRequest;
import com.sesac.aibackend.sp01_hw_cosmetic.dto.CosmeticResponse;
import com.sesac.aibackend.sp01_restapi.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CosmeticService {

    private final Map<Long, Cosmetic> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public CosmeticResponse create (CosmeticRequest req) {
        long id = sequence.getAndIncrement();

        Cosmetic saved = Cosmetic.builder()
                .id(id)
                .category(req.category())
                .name(req.name())
                .price(req.price())
                .build();

        storage.put(id, saved);

        return CosmeticResponse.from(saved);
    }

    public CosmeticResponse get (Long id) {
        Cosmetic cosmetic = storage.get(id);

        return CosmeticResponse.from(cosmetic);
    }

    public CosmeticResponse update (Long id, CosmeticRequest req) {
        Cosmetic existing = storage.get(id);

        if (existing == null) throw NotFoundException.of("cosmetic", id);

        existing.setCategory(req.category());
        existing.setName(req.name());
        existing.setPrice(req.price());

        return CosmeticResponse.from(existing);
    }

    public void delete (Long id) {
        Cosmetic cosmetic = storage.remove(id);
        if (cosmetic == null) throw NotFoundException.of("cosmetic", id);
    }
}
