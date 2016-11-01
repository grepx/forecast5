#Forecast 5 demo app
Uses the 5 day weather forecast API from openweathermap.org to show a simple weather forecast for a given location.

### Build info
Open the project in Android studio and run the app.

### Libraries used
- Retrofit 2 - to perform API requests.
- GSON - to parse JSON API responses.
- rxJava - to elegantly abstract the domain module from the networking module.
- Picasso - to display weather icons.
- ThreeTenABP - to use the Java 8 time API for help with parsing and managing time data.

### With more time
If I had more time I would:

- Use Dagger 2 for dependency injection (remove injection boilerplate from `MainActivity`).
- Create tests for `MainPresenter` by mocking the network and view layer.
- Refactor towards an architecture where the Presenter is retained across configuration changes so that the view state is not lost.
- Investigate using a object pool for `HourForecastView` since it could potentially be an expensive object to have so many of.
- Fix the todo. I need to figure out the users time zone information and how to convey that correctly to `LocalDateTime`.
- Add more features:
	-  Use the location API to get lat/lng
	-  Show text description of each weather code type.