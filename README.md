# TMDb Trending Movies

Android app that displays **trending movies from TMDb**, built as a **multi-module Clean Architecture** project using:

- **XML UI** (`feature:movies`)
- **Jetpack Compose UI** (`feature:movies-compose`)
- **Hilt DI**
- **Paging 3**
- **Shared core modules**
- **Shared Material 3 Compose theme**

This project demonstrates scalable modular architecture, clean separation of layers, reusable core utilities, and two fully functional UI implementations (XML + Compose).

---

## 📱 Features

### **Trending Movies Screen**
- Weekly trending movies from TMDb
- Infinite scrolling with **Paging 3**
- Movie card:
  - Poster (with aspect ratio 2:2.4)
  - Gradient overlay
  - Title
  - Rating pill
- Available in **XML** and **Compose**

### **Movie Details Screen**
- Poster
- Title
- Rating
- Release date (`dd MMM yyyy`, e.g. `23 Sep 1994`)
- Genres
- Overview
- Error & retry handling
- **Compose version has a Material 3 TopAppBar with always-visible back button**

---

## 📝 Two UI Implementations

### **1. XML UI (feature:movies)**
- `TrendingMoviesFragment`
- `MovieDetailsFragment`
- RecyclerView + ViewBinding
- Material-style cards
- Custom gradient overlays
- Manual layouts designed in XML

### **2. Compose UI (feature:movies-compose)**
- `TrendingMoviesRoute`
- `MovieDetailsRoute`
- LazyColumn for list
- Material 3 Components
- Same data, domain & UI mapping layer reused
- Poster card + gradient + rating pill
- Scrollable details screen with Material 3 components
- **Shared Material3 theme from `core:ui`**

You can switch which UI launches by modifying `AndroidManifest.xml` to start:

- `MainActivity` → XML version
- `ComposeMoviesActivity` → Compose version

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

- `:feature:movies-compose`
  - Composables for trending and details
  - Paging Compose integration
  - Material 3 components
  - Reuses models, use cases, repository

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

