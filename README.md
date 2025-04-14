# 🎬 Mazaady Movies App

A cleanly architected Android app developed for the Mazaady Senior Android Developer Hiring Task.  
This app fetches a list of movies from TMDb API, allows users to mark favorites, view movie details, and supports offline usage with modern Android technologies.

---

## 📱 Features

- Home screen with paginated movie list using **Paging 3**
- Toggle between vertical and grid view
- Mark/Unmark movies as **favorites**, saved locally with **Room**
- View full movie details
- Offline support for favorites and last synced data
- Error handling for network/API issues
- Fully structured with **MVVM Clean Architecture**
- Unit tested: Repositories, ViewModels

---

## 🧠 Architecture Decision

The app is built with **Clean MVVM Architecture** and divided into the following layers:

### 🔹 Domain Layer
- Contains `MovieRepo` interface and the `Movie` model.
- Business logic is defined via `use-cases` (if extended).

### 🔹 Data Layer
- Handles remote data from TMDb API via `Retrofit`.
- Manages local caching via `Room`, with a `MoviesRemoteMediator` for paging.
- Contains mappers to convert between DTOs, entities, and domain models.

### 🔹 Presentation Layer
- Contains `ViewModel`s and `Fragment`s for Home, Favorites, and Details screens.
- Uses `StateFlow`, `PagingData`, and proper state management.

### 🔹 DI Layer
- Dependency Injection is managed using **Hilt**.
- Modules are defined for repository, network, and database.

---

## ⚙️ Tech Stack

- Kotlin + Coroutines + Flow
- Paging 3
- Room
- Retrofit
- Hilt
- ViewModel + LiveData + StateFlow
- Navigation Component (Single Activity Architecture)
- Clean Architecture
- JUnit5, MockK, Turbine, Truth for unit testing

---

## 🚀 How to Run the Project

1. Clone the repository:

   ```bash
   git clone https://github.com/alfayedoficial/Mazaady_company_AndroidTask.git

2. Open the project in Android Studio Hedgehog or newer.

3. Add your TMDb API key in NetworkModule.kt or use the default test API.

4. Sync Gradle and Run the app.

5. To run tests:
   ```bash
   ./gradlew :app:test_devDebugUnitTest --tests "com.*"

---
   
## 🧪 Testing

All ViewModels, Repositories, and Mediators are covered with unit tests.

Room is tested using in-memory DB.

Flow & StateFlow are tested using Turbine.

Paging logic tested via mocked PagingData.

---

## ⚖️ Trade-offs and Assumptions

Used a fixed delay of 1s in details screen to simulate loading (as per UI feedback).

No full offline DB caching for all pages — only last synced + favorites.

Movie genres & runtime are skipped due to free API limitations (can be extended easily).

UI tests (Espresso) were not implemented to save time but architecture supports it.

Used StateFlow instead of LiveData for reactive streams.

## 🔐 🔧 Build Configuration & Secrets Management

This project uses a secure and modular configuration approach for managing sensitive data and build-time variables using:

---

### ✅ `key.properties` File

A simple `key.properties` file (excluded from version control) stores API URLs and signing configurations:

```properties
# Signing
keyStoreFile=../app/keystore/keystore.jks
keyStorePassword=123456789
keyAlias=key0
keyPassword=123456789

# API URLs
serverUrl_dev=https://api.themoviedb.org
serverUrl_pro=https://api.themoviedb.org

# Image base URL
posterImageUrl=https://image.tmdb.org/t/p/

```
📌 Make sure this file is excluded from Git using .gitignore.

✅ buildSrc/KeyHelper.kt
Located in buildSrc/src/main/kotlin/KeyHelper.kt, this Kotlin object reads values from key.properties:

```KeyHelper
object KeyHelper {
    const val KEY_SERVER_URL_DEV = "serverUrl_dev"
    const val KEY_SERVER_URL_PROD = "serverUrl_pro"
    const val KEY_POSTER_IMAGE_URL = "posterImageUrl"

    private val properties by lazy {
        Properties().apply { load(FileInputStream(File("key.properties"))) }
    }

    fun getValue(key: String): String = properties.getProperty(key)
}
```

✅ Flavors Configuration in build.gradle.kts
The app defines two build variants using flavorDimensions and productFlavors:

🔹 _dev
```_dev
applicationIdSuffix = ".dev"
versionNameSuffix = "-dev"
resValue("string", "app_name", "Mazaady AndroidTask-dev")
buildConfigField("String", "SERVER_URL", "\"${KeyHelper.getValue(KeyHelper.KEY_SERVER_URL_DEV)}\"")
buildConfigField("String", "POSTER_URL", "\"${KeyHelper.getValue(KeyHelper.KEY_POSTER_IMAGE_URL)}\"")
```
🔹 _prod
```_prod
applicationIdSuffix = ""
versionNameSuffix = ""
resValue("string", "app_name", "Mazaady AndroidTask")
buildConfigField("String", "SERVER_URL", "\"${KeyHelper.getValue(KeyHelper.KEY_SERVER_URL_PROD)}\"")
buildConfigField("String", "POSTER_URL", "\"${KeyHelper.getValue(KeyHelper.KEY_POSTER_IMAGE_URL)}\"")
```
✅ Why This Setup?
🔐 Keeps secrets and environment-specific values out of source control

🧪 Enables safe development/testing environments

⚙️ Clean and DRY implementation using buildSrc

✅ Easy to scale across flavors and CI/CD pipelines

## 📷 Screenshots

| ![Home](https://raw.githubusercontent.com/alfayedoficial/Mazaady_company_AndroidTask/refs/heads/main/screenshots/Screenshot_20250414_015535.png) | ![Home](https://raw.githubusercontent.com/alfayedoficial/Mazaady_company_AndroidTask/refs/heads/main/screenshots/Screenshot_20250414_015643.png) |
|----------------------------------------|-------------------------------------------|
| ![Favorites](https://raw.githubusercontent.com/alfayedoficial/Mazaady_company_AndroidTask/refs/heads/main/screenshots/Screenshot_20250414_015709.png) | ![Empty Favourites](https://raw.githubusercontent.com/alfayedoficial/Mazaady_company_AndroidTask/refs/heads/main/screenshots/Screenshot_20250414_015723.png) |
|----------------------------------------|-------------------------------------------|
| ![No internet](https://github.com/alfayedoficial/Mazaady_company_AndroidTask/blob/main/screenshots/Screenshot_20250414_015754.png) | ![Movie Details](https://raw.githubusercontent.com/alfayedoficial/Mazaady_company_AndroidTask/refs/heads/main/screenshots/Screenshot_20250414_015827.png) |



## 👨‍💻 Developed by
Ali Al Fayed - Senior Android Developer
