package com.app.simpleapi.simple_api.controller;

import com.app.simpleapi.simple_api.model.Mahasiswa;
import com.app.simpleapi.simple_api.repository.MahasiswaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mahasiswa")
@RequiredArgsConstructor
public class MahasiswaController {
    private final MahasiswaRepository mahasiswaRepository;

    @GetMapping
    public List<Mahasiswa> getAllMahasiswa() {
        return mahasiswaRepository.findAll();
    }
}
