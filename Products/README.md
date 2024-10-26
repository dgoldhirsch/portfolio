# 2024 Android Scaffold App

A sample app demonstrating basic competence in the Android technology stack:

* Unidirectional Jetpack Architecture:
  - Navigate
  - Compose
  - Data Layer Architecture (Repositories and Data Sources)
  - State Flow
* Compose
  - Previews
  - Progress spinner
  - Pull-refresh indicator
* Coroutines/Flows
* Coin
* Hilt
* Room
  - Auto Migrations
* Retrofit
* JUnit
* Mockk

## The App

![Screen Shot 2024-01-18 at 4 47 59 PM](https://github.com/dgoldhirsch/android-2024/assets/101699/9b042461-c57a-401b-9fa8-72815c908e60)

The app fetches a list of product images each with a title, description, etc.  The products are cached in a Room database.  The page of products is managed by a view model that uses a finite state machine.Because of the caching, the app does some date/time comparisons and computations.
