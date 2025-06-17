package com.app.simpleapi.simple_api;

import com.app.simpleapi.simple_api.model.Mahasiswa;
import com.app.simpleapi.simple_api.repository.MahasiswaRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class SimpleApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(SimpleApiApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(MahasiswaRepository repository) {
		return args -> {
			if (repository.count() == 0) {
				System.out.println("Database kosong, menambahkan data awal...");
				Mahasiswa budi = new Mahasiswa(null, "Budi Santoso", "12345678", LocalDate.of(2002, 5, 15));
				Mahasiswa ani = new Mahasiswa(null, "Ani Lestari", "87654321", LocalDate.of(2003, 1, 20));

				repository.saveAll(List.of(budi, ani));
				System.out.println("Data awal berhasil ditambahkan.");
			} else {
				System.out.println("Database sudah berisi data, tidak perlu menambahkan data awal.");
			}
		};
	}
}
