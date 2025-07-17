package com.sisimpur.library.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sisimpur.library.dto.BorrowRequestDTO;
import com.sisimpur.library.dto.BorrowResponseDTO;
import com.sisimpur.library.service.CirculationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/circulation")
@RequiredArgsConstructor
public class CirculationController {

    private final CirculationService circulationService;

    @PostMapping("/borrow")
    public ResponseEntity<List<BorrowResponseDTO>> borrowBooks(@RequestBody @Valid BorrowRequestDTO request) {
        return ResponseEntity.ok(circulationService.borrowBooks(request));
    }

    @PostMapping("/return")
    public ResponseEntity<List<BorrowResponseDTO>> returnBooks(@RequestBody @Valid BorrowRequestDTO request) {
        return ResponseEntity.ok(circulationService.returnBooks(request));
    }
}
