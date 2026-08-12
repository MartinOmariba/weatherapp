

# Weather Information App

A Java-based desktop weather application that uses the OpenWeather API to retrieve and display real-time weather information through a user-friendly graphical interface.

## Features

* Search weather information by city
* Display current temperature and weather conditions
* Show humidity and wind speed
* Weather condition icons
* Dynamic background based on weather conditions
* Temperature unit conversion
* Weather forecast information
* Weather search history
* Input validation and error handling
* Responsive interface using background processing
* Integration with the OpenWeather API

## Technologies Used

* **Java**
* **Java Swing**
* **OpenWeather API**
* **HTTP/API integration**
* **JSON data processing**
* **Object-Oriented Programming**

## Project Structure

```text
WeatherInformationApp/
├── src/
│   ├── Main.java
│   ├── WeatherApp.java
│   ├── WeatherAPI.java
│   ├── WeatherData.java
│   ├── ForecastData.java
│   ├── WeatherException.java
│   ├── WeatherHistory.java
│   ├── DynamicBackground.java
│   └── UnitConverter.java
├── README.md
└── ...
```

## How It Works

The application accepts a city name from the user and sends a request to the OpenWeather API. The returned weather data is processed by the Java application and presented through the graphical interface, allowing users to view current conditions and related weather information.

## Requirements

* Java Development Kit (JDK)
* Internet connection
* OpenWeather API key

## API Configuration

The application requires an OpenWeather API key to retrieve weather data.

Configure your API key in the appropriate location in the application before running the project. **Do not commit your API key to GitHub.**

## Running the Application

Clone the repository:

```bash
git clone https://github.com/MartinOmariba/weatherapp.git
```

Navigate into the project:

```bash
cd weatherapp
```

Compile the Java source files:

```bash
javac -d out src/*.java
```

Run the application:

```bash
java -cp out Main
```

> The exact commands may vary depending on your Java version and project configuration.

## Error Handling

The application includes validation and exception handling to manage invalid city names, API errors, unavailable weather data, and network-related problems without unnecessarily terminating the application.



