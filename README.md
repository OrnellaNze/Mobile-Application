# Vacation Planner App

## Introduction
The Vacation Planner App is designed to help users organize and manage their vacations, providing tools to schedule vacations, 
list excursions related to specific vacations, and receive timely notifications about upcoming vacation events.

## Features
- **Vacation Management**: Add, edit, and delete vacation details such as dates, hotel accommodations, and titles.
- **Excursion Scheduling**: Link excursions to specific vacations, manage details, and keep track of schedules.
- **Alerts and Notifications**: Receive notifications for the start and end of vacations, ensuring users are well-prepared for their trips.
- **Integrated Calendar Views**: Utilize date pickers to easily select and view vacation and excursion dates.

## Getting Started

#### Very Important Note####
Summary of the "Edit" Button Functionality in VacationList
In the Vacation Planner App, each vacation item in the VacationList features an "Edit" button that serves as a gateway to a multi-functional interface specifically tailored for that vacation. Here’s a breakdown of what this button enables for each individual vacation:

Edit Vacation Details: Tapping the "Edit" button takes the user to the VacationDetails activity. Here, users can modify essential details of the vacation such as the title, hotel, start and end dates. This ensures that all vacation specifics are up-to-date and reflect the user's current plans.
View and Manage Associated Excursions: Upon entering the VacationDetails view via the "Edit" button, not only are the vacation details made editable, but the interface also displays all excursions that are linked to that vacation. This integrated view allows users to:
Edit Excursions: Each associated excursion can be individually edited to update details such as the title or date.
Delete Excursions: Users have the option to remove excursions that are no longer relevant or needed. This deletion is facilitated through a dialog confirmation to ensure intentional removal.
Set Alerts for the Vacation: From the same VacationDetails interface, users can set alerts for the start and end of the vacation. This feature ensures that users receive timely notifications that help them manage their vacation schedules effectively.
Share Vacation and Excursion Details: Additionally, the interface provides functionality to share comprehensive details of the vacation along with its associated excursions. This sharing is facilitated through common communication and social media platforms, allowing users to easily send detailed itineraries to friends, family, or colleagues.
Emphasized Functionalities
The "Edit" button is distinctly designed to offer a personalized and detailed management interface for each vacation listed in the VacationList. It uniquely links to the specific vacation it represents, ensuring that the user can not only edit the vacation itself but also:

Have a detailed overview of all its associated excursions,
Perform direct edits or deletions of these excursions from the same interface,
Set necessary alerts, and
Share all related information seamlessly.

#### Continue ####

Operating Instructions
Main Activity: Start at the main activity where you can navigate TO add a new vacation.
Manage Vacations: Use the Vacation List to view all scheduled vacations or add a new one using the floating action button.

Vacation Details:
Add or edit vacation details such as title, hotel, start, and end dates.
Navigate to the associated excursions or add new ones.

Excursion Details:
From a vacation, view and manage all linked excursions.
Set notifications for each excursion to get alerts on the scheduled dates.
To Access Specific Features:
To Add a Vacation: Tap the + button on the Vacation List screen.
To Edit a Vacation: In the Vacation List, tap on a vacation item to edit its details.
To View Excursions: In the Vacation Details, scroll down to view linked excursions.
To Add an Excursion: Tap the + button in the Vacation Details.

1. Vacation Details Menu
   a. Save Vacation
   Description: Saves the details of the current vacation. This includes the title, hotel, start and end dates.
   Functionality: Validates the dates to ensure the end date is not before the start date. If the vacationID is -1 (indicating a new vacation), it creates a new Vacation object and inserts it into the repository. If updating an existing vacation, it updates the existing Vacation object in the repository.
   b. See Vacation
   Description: Navigates back to the list of vacations.
   Functionality: Triggers the viewVacations() method that navigates to the VacationList activity.
   c. Share
   Description: Shares the details of the current vacation.
   Functionality: Builds a detailed string of the vacation and associated excursions and shares it via other apps using an intent with ACTION_SEND.
   d. Set Alert
   Description: Sets alerts for the vacation's start and end dates.
   Functionality: Registers alarms using the AlarmManager that will trigger notifications at the start and end dates of the vacation.
2. Excursion List Menu
   a. Add Vacation
   Description: Adds a new vacation.
   Functionality: Opens the VacationDetails activity to allow the user to enter details of a new vacation.
   b. View All Excursions
   Description: Displays all excursions regardless of vacation association.
   Functionality: Calls loadAllExcursions() method which loads all excursions from the repository and displays them in the ExcursionList activity.
3. Vacation List Menu
   a. Add Vacation Button
   Description: Triggers the addition of a new vacation.
   Functionality: Opens the VacationDetails activity to create a new vacation.
   b. See Excursions
   Description: Navigates to see all excursions.
   Functionality: Opens the ExcursionList activity to display all excursions.






### SDK
Android Version Compatibility
minSdkVersion 26
targetSdkVersion 34
The application is built and tested for devices running Android 5.0 (Lollipop) and above.
The signed APK is targeted towards Android API level 34  to accommodate recent performance and security features.
- Latest version of Android Studio and Gradle.

### Installation
1. Clone the repository to my local machine:
   ```bash
https://gitlab.com/wgu-gitlab-environment/student-repos/onzegbu/d308-mobile-application-development-android.git


