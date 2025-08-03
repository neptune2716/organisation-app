# Organisation App 📱

## Description
Application Android d'organisation de vie créée avec Kotlin et Jetpack Compose, optimisée pour le développement avec Visual Studio Code.

## ✨ Fonctionnalités Prévues
- 📅 **Agenda intelligent** des prochains jours
- ✅ **Gestion des tâches** et rappels
- 🎯 **Suivi des habitudes** personnalisées  
- ⏰ **Gestion du temps** et productivité
- 📝 **Prise de notes** / Second cerveau
- 🏃 **Gestion sport** et bien-être
- 📱 **Contrôle temps d'écran** et notifications
- 🏠 **Page d'accueil** avec navigation intuitive

## 🛠️ Stack Technique
- **Langage** : Kotlin
- **UI Framework** : Jetpack Compose
- **Build System** : Gradle
- **IDE** : Visual Studio Code
- **Android SDK** : API 33

## 🚀 Installation rapide

### Via Git (Recommandé)
```powershell
# 1. Cloner le projet
git clone <url-du-repo>
cd organisation-app

# 2. Installer les prérequis (voir section ci-dessous)

# 3. Compiler et installer
.\gradlew.bat installDebug
```

## 📋 Prérequis

### 1. Java 17 (Recommandé)
```powershell
# Installation via winget
winget install Microsoft.OpenJDK.17
```

### 2. Android SDK
**Option A : Via Android Studio (Plus simple)**
- Téléchargez [Android Studio](https://developer.android.com/studio)
- Installez Android SDK Platform 33
- Installez Android Build Tools 33.0.2

**Option B : Command Line Tools uniquement**
```powershell
# Les variables seront configurées automatiquement
$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android"
$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-17.0.16.8-hotspot"
```

### 3. Extensions VS Code
Les extensions suivantes seront suggérées automatiquement :
- Extension Pack for Java
- Kotlin Language  
- Android iOS Emulator
- Gradle for Java

## 🔧 Commandes VS Code

### Via les tâches intégrées (`Ctrl+Shift+P` → "Tasks: Run Task")
- 🔧 **Build Android App** - Compiler l'application
- 📱 **Install on Connected Device** - Installer sur téléphone USB
- 🧹 **Clean Project** - Nettoyer le projet
- 📱 **List Connected Devices** - Voir les appareils connectés

### Via le terminal
```powershell
# Compiler
.\gradlew.bat build

# Installer sur appareil connecté
.\gradlew.bat installDebug

# Nettoyer
.\gradlew.bat clean

# Vérifier les appareils
adb devices
```

## 📱 Test sur appareil réel

1. **Activez le mode développeur** sur votre téléphone Android
2. **Activez le débogage USB** dans les options développeur  
3. **Connectez votre téléphone** en USB
4. **Autorisez la connexion ADB** (popup sur le téléphone)
5. **Lancez la tâche** "📱 Install on Connected Device"

## 📁 Structure du Projet
```
├── app/
│   ├── src/main/
│   │   ├── java/com/example/organisationapp/
│   │   │   ├── MainActivity.kt          # Point d'entrée
│   │   │   └── ui/theme/                # Thème Material Design
│   │   ├── res/                         # Ressources
│   │   └── AndroidManifest.xml          # Configuration app
│   └── build.gradle                     # Dépendances app
├── .vscode/
│   └── tasks.json                       # Tâches VS Code
├── gradle/                              # Wrapper Gradle
├── build.gradle                         # Configuration racine
├── settings.gradle                      # Modules
├── .gitignore                          # Fichiers à ignorer
└── README.md                           # Ce fichier
```

## 🤝 Contribution

1. **Fork** le projet
2. **Créez une branche** pour votre feature (`git checkout -b feature/nouvelle-fonctionnalite`)
3. **Commitez vos changes** (`git commit -am 'Ajout nouvelle fonctionnalité'`)
4. **Push** sur la branche (`git push origin feature/nouvelle-fonctionnalite`)
5. **Créez une Pull Request**

## 📈 Roadmap

- [x] ✅ Configuration projet Android + VS Code
- [x] ✅ Setup développement et build
- [ ] 🏠 Interface principale avec navigation
- [ ] 📅 Module agenda/calendrier
- [ ] ✅ Module gestion de tâches  
- [ ] 🎯 Module suivi d'habitudes
- [ ] 📝 Module prise de notes
- [ ] 🏃 Module gestion sport
- [ ] 📱 Module contrôle temps d'écran

## 🆘 Dépannage

### Problèmes courants
- **`adb` non reconnu** → Vérifiez `ANDROID_HOME` et PATH
- **Java version incorrecte** → Utilisez Java 17
- **Appareil non détecté** → Vérifiez le mode développeur et le débogage USB
- **Build failed** → Lancez `.\gradlew.bat clean` puis `.\gradlew.bat build`

## 📚 Ressources
- [Guide Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Documentation Kotlin](https://kotlinlang.org/docs/)
- [Material Design 3](https://m3.material.io/)
- [Android Developer Guide](https://developer.android.com/guide)
