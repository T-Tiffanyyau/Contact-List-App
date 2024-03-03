# Contact List App

This is a simple yet powerful Contact List application for Android, designed to help you manage your contacts efficiently. It's built using Android Studio with Kotlin and follows the Model-View-ViewModel (MVVM) architectural pattern for a robust and maintainable codebase.

## Installation

To run this project, you'll need to have Android Studio installed on your development machine.

1. Clone this repository or download the zip file.
2. Open Android Studio and select 'Open an existing Android Studio project'.
3. Navigate to the downloaded project and open it.
4. Wait for Android Studio to sync the project with gradle files.
5. Press the 'Run' button (or use `Shift + F10`) to build and run the app on your device or emulator.

## Features

- **Manage Contacts:** Users can create, view, and delete contacts. Each contact consists of a name, an email address, and a phone number.
- **Ease of Use:** The app has a user-friendly interface with a single `MainActivity` that hosts two Fragments: `MainFragment` and `IndividualContactFragment`.
- **Data Handling:** Contact data is handled in the MVVM architecture with a dedicated `ContactsListViewModel`.
- **UI Components:** A `RecyclerView` is used to render and manage the list of contact names efficiently.

## Usage

- **Adding a New Contact:** Tap on the '+' icon to add a new contact. Enter the name, email, and phone number, and save.
- **Viewing Contacts:** All your contacts are listed in the main screen. Scroll through the list to view them.
- **Deleting a Contact:** Swipe a contact left or right to delete it from your list.

## Orientation Support

The app supports both portrait and landscape modes, adjusting the layout to provide the best user experience.

## Project Structure

- `MainActivity`: The main screen of the app.
- `MainFragment`: Displays the list of contacts.
- `IndividualContactFragment`: Handles the addition of new contacts and displays individual contact details.
- `ContactsListViewModel`: Responsible for preparing and managing the data for `MainFragment` and `IndividualContactFragment`.
- `RecyclerView`: Used for displaying the list of contacts efficiently.

Last Updated: March 2, 2024
