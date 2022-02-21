# ResposNews - Simple News App

ResposNews was intially a project during one of my interview. Later became one of my playground project. App follows MVVM pattern, and have two screens.
One containing all the news fetched from RoomDB.
Another screen for news details, which is shown on news list item click.

The news search `SEARCH_KEYWORD` is hardcoded currently as *abuse*

## Updates 21.02.2022:
- UI updated for top headlines.
- Top headlines now connected to UI from data source.

## Updates 11.02.2022:
- News UI changed.
- New API added for top headlines added. UI will be updated on next commit.

## News API
News data is fetched from [News API](https://newsapi.org/). You can check the documents from [here](https://newsapi.org/docs).
Once you have the api key, add it inside `API_KEY=<your_api_key>` in `local.properties` file.


## This app includes:
- Data Binding - Declaratively bind observable data to UI elements.
- Lifecycles - Create a UI that automatically responds to lifecycle events.
- LiveData - Build data objects that notify views when the underlying database changes.
- Navigation - Handle everything needed for in-app navigation.
- Room - Access your app's SQLite database with in-app objects and compile-time checks.
- ViewModel - Store UI-related data that isn't destroyed on app rotations. Easily schedule asynchronous tasks for optimal execution.
- Coroutine - for async operations
- Glide for image loading
- Hilt: for dependency injection
- Kotlin Coroutines: for managing background threads with simplified code and reducing needs for callbacks


##Upcomings:
- News search option
- To convert into clean code architecture: Issue: #1
- To modularize app, new module with MVP architecture: Issue: #2
- Add testing for entire app: #3


## Contributing
Pull requests are welcome. For major changes/add new feature, please open an issue first to discuss what you would like to change.
Please make sure to update tests as appropriate.


## License
[MIT](https://choosealicense.com/licenses/mit/)
