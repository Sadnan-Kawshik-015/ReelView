# 🎬 ReelView Backend

Backend service for **CineScope** – a personalized IMDb-style movie discovery platform.  
Built with **Spring Boot** and **MongoDB**, it provides REST APIs for movies, users, ratings, reviews, and watchlists.  

---

## 🚀 Tech Stack
- **Java 21** + **Spring Boot 3**
- **Spring Web** – REST APIs  
- **Spring Data MongoDB** – database layer  
- **Spring Security + JWT** – authentication & authorization  
- **Lombok** – boilerplate reduction  
- **Maven** – build & dependency management  
- **Docker + Docker Compose** – containerization  

---

## 📂 Project Structure
```
cinescope-backend/
 ├── src/main/java/com/cinescope
 │   ├── controller/     # REST controllers
 │   ├── model/          # MongoDB documents
 │   ├── repository/     # Spring Data repositories
 │   ├── service/        # Business logic
 │   ├── security/       # JWT auth & filters
 │   └── CinescopeApp.java
 ├── src/main/resources/
 │   └── application.yml # app configs
 ├── pom.xml
 └── README.md
```

---

## ⚙️ Setup & Installation

### Prerequisites
- Java 21+  
- Maven 3.9+  
- MongoDB (local or Docker)  

### Clone Repository
```bash
git clone https://github.com/your-username/cinescope-backend.git
cd cinescope-backend
```

### Configure Database
Update `src/main/resources/application.yml`:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/cinescope
  jwt:
    secret: your-secret-key
    expiration: 3600000 # 1 hour
server:
  port: 8080
```

### Run Locally
```bash
mvn spring-boot:run
```

API will be available at:  
👉 `http://localhost:8080/api`

---

## 🐳 Run with Docker
```bash
docker-compose up --build
```
This will start both the backend and MongoDB.  

---

## 🔑 Authentication
- **Signup** → `POST /api/auth/signup`  
- **Login** → `POST /api/auth/login` → returns JWT  
- Secure requests by adding header:  
```
Authorization: Bearer <your_token>
```

---

## 📡 API Endpoints (sample)

### Movies
- `GET /api/movies` → list/search movies (paginated)  
- `GET /api/movies/{id}` → get movie details  
- `POST /api/movies` (admin) → create new movie  

### Reviews & Ratings
- `POST /api/reviews` → add review  
- `GET /api/reviews/{movieId}` → get reviews for a movie  

### Watchlist
- `POST /api/users/{id}/watchlist/{movieId}` → add to watchlist  
- `GET /api/users/{id}/watchlist` → view user’s watchlist  

---

## 🧪 Testing
```bash
mvn test
```
Uses **Spring Boot Test** + **Testcontainers (MongoDB)**.  

---

## 🤝 Contributing
1. Fork repo  
2. Create feature branch (`git checkout -b feature/new-api`)  
3. Commit changes (`git commit -m "Add new API"`)  
4. Push branch (`git push origin feature/new-api`)  
5. Create Pull Request  

---

## 📜 License
MIT License © 2025 [Your Name]  
