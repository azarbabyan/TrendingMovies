# TMDb Trending Movies 

Android app that displays **trending movies from TMDb**, built as a **multi-module, clean architecture** project using **XML UI**, **Hilt DI**, and **Paging 3**.

This project focuses on:

- Clean, modular architecture
- Clear separation of layers (data / domain / feature / core)
- Network error handling and UI states (loading, error, success)
- Readable and maintainable Kotlin code

---

## 📱 Features

### Trending Movies Screen
- Displays **trending movies (weekly)** from TMDb
- **Infinite scrolling** with **Paging 3**
- Each item shows:
  - Movie poster
  - Title
  - Rating with a pill-style badge
- Uses a **modern Material-style card design**

### Movie Details Screen
- Displays:
  - Poster
  - Title (also in header)
  - TMDb rating
  - Release date (formatted as: `dd MMM yyyy`, e.g. `23 Sep 1994`)
  - Genres
  - Overview
- Error + loading states with retry
- Simple **back button** in the header row

### Error & Network Handling
- Centralized `safeApiCall` helper for all network calls
- Custom `NetworkException` types:
  - `NetworkUnavailable`
  - `HttpError` (with HTTP status code)
  - `Unknown`
- Shared error → UI message mapper
- UI shows human-friendly messages and **Retry** buttons

---

## 🏗 Architecture

The app follows a layered, multi-module architecture:

- **Presentation (feature modules)**
  - Fragments, ViewModels, adapters, XML layouts
- **Domain**
  - Use cases, domain models, repository interfaces
- **Data**
  - Retrofit API interfaces, DTOs, repository implementations, mappers
- **Core**
  - Shared infrastructure (network, UI utilities, error types)

Data flow:

`UI (Fragments/Adapters)` → `ViewModel` → `UseCase` → `MoviesRepository` → `MoviesApi` → TMDb

### Module Overview

- `:app`
  - Application module
  - `MoviesApp` (`@HiltAndroidApp`)
  - `MainActivity` with `NavHostFragment`
  - Root navigation graph that includes feature navigation graphs

- `:feature:movies`
  - UI for movies:
    - `TrendingMoviesFragment`
    - `MovieDetailsFragment`
  - ViewModels:
    - `TrendingMoviesViewModel`
    - `MovieDetailsViewModel`
  - Paging 3 adapter for trending list
  - XML layouts for list items and details
  - Maps domain models → UI models

- `:domain:movies`
  - Domain models:
    - `Movie`
    - `MovieDetails`
  - Repository interface:
    - `MoviesRepository`
  - Use cases:
    - `GetTrendingMoviesUseCase`
    - `GetMovieDetailsUseCase`
    - `GetTrendingMoviesPagingUseCase`

- `:data:movies`
  - DTOs and Retrofit API:
    - `MoviesApi`
    - `MovieDto`, `MovieDetailsDto`, etc.
  - Repository implementation:
    - `MoviesRepositoryImpl`
  - Mappers DTO → Domain
  - Hilt modules for providing:
    - `MoviesApi`
    - `MoviesRepository` binding

- `:core:network`
  - Network configuration (Retrofit + OkHttp)
  - `BASE_URL`
  - `NetworkModule` (Retrofit/OkHttp/Hilt)
  - Error handling:
    - `NetworkException`
    - `safeApiCall`

- `:core:ui`
  - Shared UI helpers & error mappers:
    - `Throwable.toUIMessage()`

---

## 🛠 Tech Stack

- **Language:** Kotlin
- **UI:** XML layouts + ViewBinding
- **Architecture:** MVVM + Clean Architecture + multi-module
- **DI:** Hilt
- **Networking:**
  - Retrofit
  - OkHttp + Logging Interceptor
  - Json
- **Async:** Coroutines, Flow, Paging 3
- **Navigation:** AndroidX Navigation Component + Safe Args
- **Image Loading:** Glide

---

## 🔑 TMDb API Key Setup

The app uses TMDb’s REST API.

1. Create an account on TMDb and get an **API key (v3 auth)**.
2. Add in local.properties API_KEY=Your key

