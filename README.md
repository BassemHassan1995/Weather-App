# Ahoy Weather App

**Weather** — A basic Weather app in Kotlin using MVVM architecture.

## Basic Features
-   [x] Display a 7-day weather forecast based on the current user location
-   [x] Allow the user to search for any city
-   [x] Allow the user to add any city to a favorite list
-   [x] Clicking on a city(favorite or searched for) will open a simple details view with one day weather
-   [x] Adding settings to allow the user to change his preferences of the temperature unit type
-   [x] Get a daily notification at 6:00 AM with today’s temperature.
-   [x] Adding unit tests for both search city and get current location forecast.


## Layers
The app is multi-module by layer, as following:
1. **data**: Contains all the data accessing and manipulating parts (api, di, model & repository).
    - **api**: For networking using Retrofit
    - **db**: For local database using Room 
    - **di**: For dependency injection using Hilt
    - **model**: For business models and db entities using Moshi
    - **notifications**: For sending a daily reminder notification using WorkManager
    - **repository**: For managing the communication between the Ui layer and the data layer
    - **store**: For storing the user preferences using Datastore

2. **ui**: Contains all the View classes (Fragments, Activities) along with their corresponding ViewModels.
3. **utils**: Utility classes.


## Android Jetpack
-   [MVVM](https://developer.android.com/jetpack/guide)
-   [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
-   [Databinding](https://developer.android.com/topic/libraries/view-binding)
-   [AndroidX](https://developer.android.com/jetpack/androidx)
-   [Dependency Injection with Hilt](https://developer.android.com/training/dependency-injection)
-   [Room Database](https://developer.android.com/training/data-storage/room)
-   [Datastore](https://developer.android.com/topic/libraries/architecture/datastore)
-   [Unit Testing](https://developer.android.com/training/testing/unit-testing)
-   [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
-   [Notifications](https://developer.android.com/develop/ui/views/notifications/build-notification)

## Libraries 
-   [Retrofit](https://github.com/square/retrofit)
-   [Hilt](https://dagger.dev/hilt/)
-   [Moshi](https://github.com/square/moshi)
-   [Glide](https://github.com/bumptech/glide)
-   [Mockito](https://site.mockito.org/)
-   [State FLow](https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda)

