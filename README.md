# 🎵 Muzic

> Write about your day. Get a playlist that feels like you.

Muzic is an AI-powered mood-based playlist generator. You write a short journal entry, and Muzic analyzes your mood and curates a 20-track Spotify playlist that matches your vibe — instantly.

---

## Features

- 📝 **Daily journal entry** — describe how you're feeling in your own words
- 🤖 **AI mood analysis** — powered by OpenRouter (Llama / GPT) to extract mood, energy level, and themes
- 🎧 **Spotify playlist curation** — 20 tracks fetched from Spotify based on your mood
- 💾 **Persistent storage** — journal entries and playlists saved to PostgreSQL
- 📱 **Mobile-first UI** — dark, Spotify-inspired design built with Expo and React Native

---

## Tech Stack

### Backend

| Technology                  | Purpose                   |
| --------------------------- | ------------------------- |
| Java 21 + Spring Boot 3.5   | REST API                  |
| Spring Data JPA + Hibernate | Database ORM              |
| PostgreSQL 16               | Data persistence          |
| OpenRouter API              | AI mood analysis          |
| Spotify Web API             | Track search and curation |
| Docker                      | Containerization          |
| Maven                       | Build tool                |

### Mobile

| Technology          | Purpose                   |
| ------------------- | ------------------------- |
| Expo + React Native | Cross-platform mobile app |
| TypeScript          | Type safety               |
| Expo Router         | File-based navigation     |
| Axios               | HTTP client               |

---

## Project Structure

```
muzic/
├── backend/                          # Spring Boot REST API
│   ├── src/main/java/com/muzic/backend/
│   │   ├── controller/               # API endpoints
│   │   ├── service/                  # Business logic (AI + Spotify)
│   │   ├── repository/               # Database access layer
│   │   ├── model/                    # JPA entities
│   │   ├── dto/                      # Request/response objects
│   │   └── config/                   # App configuration
│   ├── Dockerfile
│   └── pom.xml
│
├── mobile/                           # Expo React Native app
│   ├── app/
│   │   ├── _layout.tsx               # Root layout
│   │   ├── index.tsx                 # Journal entry screen
│   │   └── playlist.tsx              # Playlist results screen
│   ├── services/
│   │   └── api.ts                    # Backend API calls
│   ├── constants/
│   │   └── colors.ts                 # App color palette
│   ├── Dockerfile
│   └── package.json
│
├── docker-compose.yml                # Full stack orchestration
└── README.md
```

---

### Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [Node.js 20+](https://nodejs.org/)
- [Expo Go](https://expo.dev/go) app on your phone
- [Spotify Developer Account](https://developer.spotify.com/)
- [OpenRouter Account](https://openrouter.ai/)

---

### 1. Clone the repository

```bash
git clone https://github.com/franciegalapate/muzic-app.git
cd muzic-app
```

---

### 2. Set up environment variables

Create a `.env` file in the project root:

```env
OPENROUTER_API_KEY=your_openrouter_api_key
SPOTIFY_CLIENT_ID=your_spotify_client_id
SPOTIFY_CLIENT_SECRET=your_spotify_client_secret
```

Create `backend/src/main/resources/application.properties`:

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/muzicdb
spring.datasource.username=postgres
spring.datasource.password=muzic123
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false

# OpenRouter
openrouter.api.key=your_openrouter_api_key

# Spotify
spotify.client.id=your_spotify_client_id
spotify.client.secret=your_spotify_client_secret
```

> Both files are gitignored and will never be committed.

---

### 3. Start the backend with Docker

```bash
docker-compose up db backend
```

This starts:

- 🐘 PostgreSQL on port `5432`
- ☕ Spring Boot API on port `8080`

Spring Boot will auto-create the database tables on first run.

---

### 4. Run the mobile app with Expo Go

In a separate terminal:

```bash
cd mobile
npm install --legacy-peer-deps
npx expo start
```

Then:

1. Open the **Expo Go** app on your phone
2. Scan the QR code from the terminal
3. Make sure your phone and Mac are on the **same WiFi network**

> 📌 Update `mobile/services/api.ts` with your Mac's local IP address:
>
> ```typescript
> const BASE_URL = "http://YOUR_MAC_IP:8080";
> ```
>
> Find your IP by running: `ipconfig getifaddr en0`

---

### 5. (Optional) Run everything with Docker

To run the full stack including the mobile web version:

```bash
docker-compose up --build
```

Then open:

- 📱 Mobile web app → [http://localhost:8081](http://localhost:8081)
- 🔌 Backend API → [http://localhost:8080](http://localhost:8080)

---

## Author

**Francie Galapate**
BS Computer Science — Saint Louis University, Baguio City

[![GitHub](https://img.shields.io/badge/GitHub-franciegalapate-181717?style=flat&logo=github)](https://github.com/franciegalapate)
