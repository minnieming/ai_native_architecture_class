package com.sesac.aibackend.controller;

import com.sesac.aibackend.domain.Cosmetic;
import com.sesac.aibackend.dto.CosmeticRequest;
import com.sesac.aibackend.dto.CosmeticResponse;
import com.sesac.aibackend.error.NotFoundException;
import com.sesac.aibackend.service.CosmeticService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/cosmetics")
@RequiredArgsConstructor
public class CosmeticController {

    private final CosmeticService cosmeticService;

    @PostMapping
    public ResponseEntity<CosmeticResponse> create (@Valid @RequestBody CosmeticRequest req) {

        CosmeticResponse res = cosmeticService.create(req);

        return ResponseEntity.created(URI.create("/legacy/cosmetics" + res.id())).body(res);

    }

    // 입력으로 들어오는 값에 대한 validation은 컨트롤러 단에서 처리해야한다!
    @GetMapping("/{id}")
    public CosmeticResponse get (@PathVariable Long id) {

        CosmeticResponse res = cosmeticService.get(id);

        return res;
    }

    @PutMapping("/{id}")
    public CosmeticResponse update (@PathVariable Long id, @Valid @RequestBody CosmeticRequest req) {

        CosmeticResponse res = cosmeticService.update(id, req);

        return res;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {

        cosmeticService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
