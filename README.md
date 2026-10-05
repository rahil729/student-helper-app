# Student Helper App

Student Helper is an Android app built around the Sysslan IT Solutions internship brief. The home screen brings the project's setup and working academic tools together; UI polish from the fifth level is applied throughout the app.

**App description:** Student Helper helps students manage academic life in one place: organize weekly classes, track upcoming exams, and keep dated task reminders. Your entries are stored locally on your device.

## Features
- Home screen with the Level 1 project overview and clear navigation to working features
- Live saved-item counts for classes, exams, and reminders
- Timetable management with day and time entry
- Exam schedule tracking with sorted dates
- Reminder creation, completion, and completed-item deletion
- Consistent, responsive Material UI with local persistence using SharedPreferences
- Custom launcher icon and responsive scrolling home screen

## Internship levels
### Level 1 — App Setup & Home Screen
- Android project, app name, and custom launcher icon
- Home screen with navigation cards for Timetable, Exams, and Reminders
- Intent-based navigation between sections
- Skills: Android project setup, XML UI, intents, and app structure

### Level 2 — Timetable Section
- Add a subject with its day and time
- Display saved timetable entries in a RecyclerView
- Skills: input handling, structured lists, RecyclerView, and layouts

### Level 3 — Exam Schedule Section
- Add an exam subject and date using a date picker
- Sort exams by date and show an empty state when the list is empty
- Skills: date handling, sorting, list management, and empty states

### Level 4 — Reminders Section
- Add a reminder title and date
- Display reminders, mark them completed, and delete completed reminders
- Skills: CRUD operations, list management, and user interaction

### Level 5 — UI Polish & Final Touch
- Apply consistent colors, fonts, spacing, and responsive scrolling
- Final-review checklist and app description for presentation
- Skills: UI/UX, responsive design, testing/debugging, and app presentation

## Tech stack
- Kotlin
- Android SDK
- AndroidX RecyclerView
- Material Components

## Run locally
1. Open the project in Android Studio.
2. Let Gradle sync.
3. Select an emulator or connected device.
4. Run the app.

## Download
Install the latest public APK from the [GitHub Releases page](https://github.com/rahil729/student-helper-app/releases/latest).
On Android, download the APK and allow installation for your browser/file manager if prompted.
This sideloadable APK is built with Android's debug signing key; use it for evaluation rather than Google Play distribution.

For local builds, the debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

## Project structure
- `app/src/main/java/com/example/studenthelper` – application logic
- `app/src/main/res/layout` – XML layouts
- `app/src/main/res/values` – strings, colors, styles

## Notes
The app stores data locally on the device and remains lightweight for student-focused academic use.
The UI polish from Level 5 is applied throughout the app.
