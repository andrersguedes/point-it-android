# 🃏 PointIt - Planning Poker for Agile Teams

**PointIt** is a modern native Android application built to streamline Planning Poker sessions for agile software development teams. The app allows users to create voting rooms, utilize the Fibonacci deck for story point estimations, compute average scores in real time, persist historical data locally, and inspect complete estimation records.

---

## 🚀 Architecture & Tech Stack

The project follows modern Android development best practices without relying on heavy dependency injection frameworks, prioritizing simplicity, high performance, and testability:

* **Language:** 100% Kotlin
* **UI Framework:** Jetpack Compose & Material 3
* **Architecture:** Clean Architecture / MVVM (Model-View-ViewModel)
* **Concurrency:** Kotlin Coroutines & Flow (end-to-end reactivity)
* **Local Persistence:** Room Database (Manual Singleton, Entities, DAOs, and Repositories)
* **Navigation:** Navigation Compose
* **Testing:**
    * Unit Tests: **MockK**, Coroutines Test, JUnit
    * UI / Instrumented Tests: Compose Test Framework
    * End-to-End (E2E) Tests: Complete user flow scenarios verifying app behavior from creation to persistence

---

## 📱 Features & Screens

1. **Screen 1 (Home / Setup):**
    * Moderator identification (username) and task/story definition (e.g., `[PROJ-101]`).
    * Input validation and smooth navigation to the voting room.
2. **Screen 2 (Voting Room / Room):**
    * Interactive cards based on the Fibonacci sequence (`1, 2, 3, 5, 8, 13, 21...`).
    * Visual indicator for cast votes and vote reveal functionality.
    * Automatic calculation of the final average score with automated background saving to Room.
3. **Screen 3 (Estimation History):**
    * Real-time reactive list (via Room `Flow`) of all previously estimated tasks, ordered from newest to oldest.
    * Action to clear all stored history records.
4. **Screen 4 (Estimate Details):**
    * Detailed breakdown view for a selected estimation record (full title, final average, moderator, and completion timestamp).

---

## 📂 Project Structure

```text
com.andrersguedes.pointit/
│
├── data/
│   ├── local/          # Room Database, Entities, and DAOs
│   └── repository/     # Estimate Repository implementation
│
├── presentation/
│   ├── components/     # Reusable UI components (TopBars, Card items, etc.)
│   ├── detail/         # Details Screen (ViewModel, State, and Composable)
│   ├── history/        # History Screen (ViewModel, State, and Composable)
│   ├── home/           # Home Screen (ViewModel, State, and Composable)
│   ├── room/           # Voting Room Screen (ViewModel, State, and Composable)
│   └── theme/          # Color schemes, Typography, and Material 3 Theme
│
├── util/               # Extension functions (Date and formatting extensions)
└── MainActivity.kt     # Entry point and Central NavHost
```

---

## 🧪 Test Suite

PointIt includes a comprehensive testing strategy covering every layer of the application:

* **Unit Tests (`src/test`):** ViewModel testing (`RoomViewModel`, `HistoryViewModel`, `EstimateDetailViewModel`) using **MockK** to mock repository behavior, custom Coroutine dispatchers, and `StateFlow` reactive collection verification.
* **UI / Instrumented Tests (`src/androidTest`):** Verification of screen states (empty, loading, populated) using the Jetpack Compose Testing Library.
* **End-to-End (E2E) Tests:** Full lifecycle simulation (Home ➔ Room ➔ Vote Reveal ➔ Room Persistence ➔ History ➔ Detail View).

### Running Unit Tests via CLI:
```bash
./gradlew testDebugUnitTest
```

### Running Instrumented / E2E Tests:
```bash
./gradlew connectedAndroidTest
```

---

## 🛠️ Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/pointit.git
   ```
2. Open the project in **Android Studio** (Iguana or newer recommended).
3. Sync project Gradle files (`Sync Project with Gradle Files`).
4. Run the project on an emulator or physical device running Android 8.0 (API level 26) or higher.


---