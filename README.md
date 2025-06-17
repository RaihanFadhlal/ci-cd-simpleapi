# Proyek Ujian Teknis CI/CD

Proyek ini mendemonstrasikan implementasi alur kerja CI/CD (Continuous Integration & Continuous Deployment) end-to-end untuk aplikasi Spring Boot. Alur ini mencakup proses dari push kode ke Git, build otomatis, containerisasi dengan Docker, hingga deployment ke cluster Kubernetes menggunakan Jenkins.

## 1. Spesifikasi Aplikasi

- **Framework:** Spring Boot 3.x
- **Bahasa:** Java 17
- **Dependensi Kunci:** Spring Web, Spring Security, Spring Data JPA, PostgreSQL Driver, Lombok.
- **Database:** PostgreSQL
- **API Endpoint:** `GET /api/v1/mahasiswa` untuk mengambil semua data mahasiswa dari database.

## 2. Desain & Implementasi CI/CD Flow (Soal No. 1, 2, 3, 4)

Alur CI/CD dirancang dan diimplementasikan menggunakan Jenkins Pipeline yang didefinisikan dalam file `Jenkinsfile`. Berikut adalah tahapan (stages) dari pipeline tersebut:

1.  **Trigger:** Pipeline dijalankan secara manual atau dapat dikonfigurasi untuk dipicu oleh `git push` ke branch `main`.
2.  **Checkout:** Jenkins mengambil kode sumber terbaru dari repository GitHub.
3.  **Build Application:** Aplikasi Spring Boot di-compile dan di-package menjadi sebuah file `.jar` menggunakan Maven Wrapper (`./mvnw clean package`). Proses testing di-skip untuk mempercepat pipeline.
4.  **Build & Push Docker Image:**
    - Sebuah Docker image baru dibuat berdasarkan `Dockerfile` yang ada di repository.
    - Image diberi tag unik menggunakan nomor build dari Jenkins (contoh: `raihanfadhlal/simple-api:1.5`) dan juga tag `latest`.
    - Menggunakan credentials yang tersimpan aman di Jenkins, pipeline melakukan login ke Docker Hub.
    - Kedua tag image (unik dan `latest`) di-push ke Docker Hub.
5.  **Deploy to Kubernetes:**
    - Pipeline secara otomatis memperbarui file `k8s/deployment.yaml` untuk menggunakan tag image yang baru saja di-push.
    - `kubectl apply` dijalankan untuk menerapkan semua konfigurasi dari folder `k8s/`, yang memicu proses *rolling update* di cluster Kubernetes.
    - Pipeline menunggu hingga proses deployment selesai dengan sukses.

### Alur Mobile (Konseptual)

Untuk aplikasi mobile, alur CI/CD akan diadaptasi sebagai berikut:
- **Artefak:** Output utama adalah file biner seperti `.apk` (Android) atau `.ipa` (iOS), bukan Docker image.
- **Proses Tambahan:** Melibatkan tahap *code signing* menggunakan sertifikat dan *provisioning profile* yang aman.
- **Deployment:** Artefak yang sudah ditandatangani diunggah ke platform distribusi pengujian seperti Firebase App Distribution (untuk Android/iOS) atau TestFlight (untuk iOS) untuk didistribusikan ke tim QA.

## 3. Komponen Proyek

- **`Dockerfile`**: Berisi instruksi *multi-stage build* untuk menciptakan Docker image yang efisien dan kecil untuk aplikasi Spring Boot.
- **`k8s/`**: Direktori ini berisi semua manifest Kubernetes yang diperlukan:
    - `deployment.yaml`: Mendefinisikan cara menjalankan aplikasi di cluster.
    - `service.yaml`: Mengekspos aplikasi di dalam cluster dengan tipe `NodePort`.
    - `ingress.yaml`: Mengatur routing dari traffic eksternal ke aplikasi menggunakan Ingress.
- **`Jenkinsfile`**: Mendefinisikan keseluruhan pipeline CI/CD sebagai *code*, memastikan proses build dan deployment yang konsisten dan dapat direproduksi.

## 4. Cara Menjalankan & Menguji Proyek

Untuk menjalankan proyek ini secara lokal, Anda memerlukan Docker Desktop dengan Kubernetes yang sudah diaktifkan.

1.  **Clone Repository:**
    ```bash
    git clone https://github.com/RaihanFadhlal/ci-cd-simpleapi.git
    cd ci-cd-simpleapi
    ```

2.  **Setup Lingkungan Kubernetes:**
    - Pastikan cluster Kubernetes dari Docker Desktop berjalan.
    - Instal NGINX Ingress Controller jika belum ada.
    - Terapkan manifest Kubernetes ke cluster Anda:
      ```bash
      kubectl apply -f k8s/
      ```

3.  **Konfigurasi DNS Lokal:**
    - Tambahkan baris berikut ke file `hosts` di sistem operasi Anda (`C:\Windows\System32\drivers\etc\hosts` untuk Windows atau `/etc/hosts` untuk Mac/Linux):
      ```
      127.0.0.1   my-simple-api.local
      ```

4.  **Akses Aplikasi:**
    - Buka browser dan kunjungi URL berikut untuk melihat data mahasiswa dalam format JSON:
      `http://my-simple-api.local/api/v1/mahasiswa`