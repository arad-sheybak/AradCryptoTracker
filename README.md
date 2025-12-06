# 📈 AradCryptoTracker - Real-time Cryptocurrency Tracker

![Android](https://img.shields.io/badge/Android-100%25-green?style=for-the-badge&logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-100%25-purple?style=for-the-badge&logo=kotlin)
![Clean Architecture](https://img.shields.io/badge/Architecture-Clean-blue?style=for-the-badge)
![WebSocket](https://img.shields.io/badge/Real--time-WebSocket-orange?style=for-the-badge&logo=websocket)

A modern Android application for tracking cryptocurrency prices in **real-time** using **WebSocket connections**. Built with **Clean Architecture**, **MVI pattern**, and modern Android development practices.


## 🎥 Demo

![App Demo](./demo/screen_record.gif)  
*Real-time price updates with smooth UI transitions*

## ✨ Features

✅ **Real-time Updates** - Live cryptocurrency prices via WebSocket  
✅ **Multiple Currencies** - Track BTC, ETH, ADA, and more  
✅ **Clean Architecture** - Separation of concerns for maintainability  
✅ **MVI Pattern** - Predictable state management  
✅ **Modern UI** - Built with Jetpack Compose and Material 3  
✅ **Connection Management** - Automatic reconnection and error handling  
✅ **Dependency Injection** - Using Koin for clean dependency management  
✅ **JSON Parsing** - Efficient parsing with Moshi  

## 🏗️ Architecture

📱 Presentation Layer (UI)
    ├── Jetpack Compose
    ├── MVI Pattern (State + Intent)
    └── Material 3 Design

🔧 Domain Layer (Business Logic)
    ├── Use Cases
    ├── Repository Interfaces
    └── Entities

🗄️ Data Layer (Data Sources)
    ├── WebSocket Client (CoinEx API)
    ├── Repository Implementation
    └── DTOs with Moshi


## 🛠️ Tech Stack

| Layer | Technology | Purpose |
|-------|------------|---------|
| **Language** | Kotlin | Main programming language |
| **UI Framework** | Jetpack Compose | Modern declarative UI |
| **Architecture** | Clean Architecture + MVI | Scalable architecture |
| **Dependency Injection** | **Koin** | Lightweight DI framework |
| **JSON Parsing** | **Moshi** | Efficient JSON parsing |
| **Networking** | OkHttp + WebSocket | HTTP/WebSocket client |
| **Coroutines** | Kotlin Coroutines + Flow | Asynchronous programming |
| **Navigation** | Compose Navigation | Screen navigation |


## 🔌 WebSocket Integration

This project demonstrates **real WebSocket implementation** with:

- 🔗 **Connection state management** (Connecting, Connected, Error, Disconnected)
- 🔄 **Automatic reconnection** with exponential backoff
- 📊 **Buffer management** for real-time data streaming
- 🎯 **Subscription management** for multiple currencies

### CoinEx API Integration 🌟

Special thanks to **CoinEx** for providing a clean and straightforward WebSocket API that made this project possible. Their API documentation is excellent and their WebSocket feed is reliable for real-time cryptocurrency data.

📚 [CoinEx WebSocket API Documentation](https://docs.coinex.com/v2/)


## 🚀 Getting Started
Prerequisites
Android Studio Hedgehog or later

Android SDK 34+

Java 17

## Installation

1.Clone the repository:

git clone https://github.com/arad-sheybak/AradCryptoTracker.git


2.Open the project in Android Studio

3.Build and run the app on an emulator or physical device

## Configuration
The app uses CoinEx WebSocket API by default. No API key is required for public price data.

## 🔧 Key Implementation Details
## WebSocket Client (CoinExWebSocketClient.kt)
🏗️ Manages WebSocket connection lifecycle

🔄 Handles reconnection logic

📊 Buffers incoming messages with MutableSharedFlow

📡 Tracks connection state with StateFlow

## Repository Pattern (CryptoRepositoryImpl.kt)
🎯 Single source of truth for cryptocurrency data

🔄 Converts DTOs to Domain Entities

📡 Manages data streaming from WebSocket

⚠️ Handles error scenarios gracefully

## MVI Implementation (MainViewModel.kt)
📊 Predictable state management

🏗️ Clear separation between State, Intent, and Effect

🔄 Side-effect handling for navigation and toasts

💾 State persistence across configuration changes

## 🤝 Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License
This project is licensed under the MIT License - see the LICENSE file for details.

🙏 Acknowledgments
🌟 CoinEx for their excellent WebSocket API

🚀 JetBrains for Kotlin and Compose

⚡ Square for OkHttp and Moshi

👥 The Android developer community

## 📞 Contact

**Arad Sheybak**  
- 🔗 LinkedIn: [arad-sheybak](https://www.linkedin.com/in/arad-sheybak/)  
- 💻 GitHub: [arad-sheybak](https://github.com/arad-sheybak)  
- 📧 Email: arad.sheybak@gmail.com

**Project Link:** [https://github.com/arad-sheybak/AradCryptoTracker](https://github.com/arad-sheybak/AradCryptoTracker)


## 🔗 Project Link: https://github.com/arad-sheybak/AradCryptoTracker

## ⭐ If you found this project helpful, please give it a star! ⭐




