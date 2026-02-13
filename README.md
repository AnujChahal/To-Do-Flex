# 📝 ToDoFlex

> A modern, feature-rich To-Do Android application built using **Kotlin, Jetpack Compose, MVVM Architecture, and Room Database**.  
> Designed with clean architecture principles and smooth UI interactions including drag-and-drop reordering.

---

## 📱 App Preview

<p align="center">
  <img src="assets/app_preview.gif" width="260"/>
</p>

---

## 🎥 Demo Video

<p align="center">
  <a href="assets/demo_video.mp4">
    <img src="![Screen Recording 2026-02-13 110310](https://github.com/user-attachments/assets/602b021a-29ce-4929-b94a-47e58c94b993)
"/>
  </a>
</p>

---

## 🚀 Features

- ✅ Add new tasks
- 🗑️ Delete tasks  
- ✔️ Mark tasks as completed  
- 📅 Calendar strip UI for date-based tasks  
- 🔄 Drag & Drop task reordering  
- 💾 Persistent storage using Room Database  
- ⚡ Reactive UI updates using StateFlow  
- 🎨 Clean UI built fully with Jetpack Compose  
- 🌙 Material Design styling  

---

## 🏗️ Tech Stack

| Technology | Purpose |
|------------|----------|
| **Kotlin** | Programming Language |
| **Jetpack Compose** | Declarative UI |
| **MVVM** | Architecture Pattern |
| **Room Database** | Local Data Storage |
| **Coroutines & Flow** | Async & Reactive Programming |
| **Jetpack Navigation 3** | Screen Navigation |

---

## 🧠 Architecture Overview

This project follows **MVVM (Model-View-ViewModel)** architecture for separation of concerns and scalability.

```
UI (Compose Screens)
        ↓
ViewModel (State Management)
        ↓
Repository
        ↓
Room DAO
        ↓
SQLite Database
```

---

## 📂 Project Structure

```
todoflex
│
├── dao
│   └── ToDoFlexDao.kt
│
├── entity
│   └── TaskDatabase.kt
│
├── model
│   └── Task.kt
│
├── repository
│   └── ToDoFlexRepository.kt
│
├── ui
│   ├── navigation
│   │   ├── AppNavigation.kt
│   │   └── Routes.kt
│   │
│   ├── screens
│   │   ├── components
│   │   │   ├── CalenderStrip.kt
│   │   │   ├── HeaderSection.kt
│   │   │   └── TaskCard.kt
│   │   │
│   │   ├── AddTaskScreen.kt
│   │   └── ToDoFlexScreen.kt
│   │
│   └── theme
│
├── ReorderableLazyColumn.kt
├── ReorderableLazyColumn2.kt
├── ToDoFlexViewModel.kt
└── MainActivity.kt
```

---

## ✨ UI & Animation Highlights

- `AnimatedVisibility`
- `animateContentSize()`
- Drag-and-drop with custom `ReorderableLazyColumn2`
- Smooth recomposition using StateFlow
- Clean Material-based UI

---

## 🗄️ Room Database Implementation

- `@Entity` for Task model
- `@Dao` interface for CRUD operations
- `@Database` class for Room configuration
- Repository layer for abstraction
- Flow-based reactive data updates

---

## 🎯 Key Learning Outcomes

- Practical MVVM implementation  
- Room database integration with Flow  
- Advanced Compose layouts  
- Drag-and-drop implementation in LazyColumn  (Credit goes to [Kyriakos-Georgiopoulos](https://gist.github.com/Kyriakos-Georgiopoulos/8d09d757e94497305a5266d02374637b))
- Clean navigation structure with Compose Navigation 3 
- Scalable Android app structure  

---

## 📸 Screenshots

<p align="center">
  <img src="<img width="396" height="829" alt="Screenshot 2026-02-13 103504" src="https://github.com/user-attachments/assets/150a1189-f833-4ef6-aa02-2d176538cb23" />
" width="250"/>
  <img src="<img width="391" height="828" alt="Screenshot 2026-02-13 103517" src="https://github.com/user-attachments/assets/6e47bc48-edc0-40e2-a562-f5d422f268ed" />
" width="250"/>
  <img src="<img width="390" height="830" alt="Screenshot 2026-02-13 103525" src="https://github.com/user-attachments/assets/0a69fafa-52cc-42f1-8140-7e1d0344d8f1" />
" width="250"/>
</p>

---

## 🛠️ Setup Instructions

1. Clone the repository:

```
git clone https://github.com/AnujChahal/To-Do-Flex.git
```

2. Open in Android Studio  
3. Sync Gradle  
4. Run on emulator or physical device  

Download APK to use the app: - [APK File](https://github.com/AnujChahal/To-Do-Flex/blob/main/app/build/outputs/apk/debug/app-debug.apk)
Click View Raw to download the APK

---

## 👨‍💻 Author

**Anuj Chahal**  
Android Developer | Kotlin | Jetpack Compose  

🔗 GitHub: [AnujChahal](https://github.com/AnujChahal)  
🔗 LinkedIn: [Anuj Chahal](https://www.linkedin.com/in/anuj-chahal-1079sr/)  

---

## ⭐ Show Your Support

If you like this project, consider giving it a ⭐ on GitHub!
