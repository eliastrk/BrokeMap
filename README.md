# BrokeMap

[Version française](./README.fr.md)

BrokeMap is an Android application designed to help users discover nearby places and activities on a map while focusing on budget-oriented filters.

The app retrieves business and activity data from a custom backend API, then displays the results on Google Maps with markers and filtering tools adapted to each place category.

## Project Scope

This repository contains the Android client.

The mobile app is connected to:
- a custom backend API built with FastAPI
- a PostgreSQL 15 database
- a Dockerized deployment for the backend stack
- the Google Maps Android SDK for map rendering and markers

## Main Features

- display nearby places on an interactive Google Map
- show markers for multiple place categories
- filter places by type
- filter places with budget-related criteria
- display detailed information returned by the API
- request and use the device location
- preload and cache business details for a smoother browsing experience

From the codebase, the main supported categories include:
- bars
- restaurants
- fast food places
- museums
- benches / low-cost public spots

## Technical Stack

- Kotlin
- Jetpack Compose
- Google Maps Compose
- Retrofit + Gson
- Android ViewModels and state flows
- DataStore
- FastAPI backend
- PostgreSQL 15
- Docker

## Repository Structure

- `app/src/main/java/fr/nebulo9/brokemap/`: Android application source code
- `app/src/main/java/fr/nebulo9/brokemap/api/`: API client and Retrofit services
- `app/src/main/java/fr/nebulo9/brokemap/models/`: ViewModels and state management
- `app/src/main/java/fr/nebulo9/brokemap/services/`: Android services such as location handling
- `app/src/main/java/fr/nebulo9/brokemap/ui/`: Compose UI, screens, filters, and theme
- `app/src/main/res/`: icons, styles, map resources, and Android resources
- `secrets.example.properties`: example file for local API keys

## How The App Works

At a high level, the workflow is the following:

1. The app initializes its Retrofit client and Google Maps configuration.
2. It requests location permission from the user.
3. It loads businesses from the backend API.
4. It preloads category-specific details when needed.
5. It displays places as map markers.
6. It applies client-side filters based on selected criteria.
7. It shows a details sheet for a selected place.

This makes the project a good example of:
- API consumption on Android
- map-based UI design
- filtering geolocated business data
- integration between a mobile frontend and a custom backend

### Prerequisites

- Android Studio
- a recent Android SDK
- a Google Maps Platform API key
- access to the backend API used by the app

### Secrets Configuration

The project uses a `secrets.properties` file for local secrets.

Use the provided template:

```properties
MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
```

## Notes

- This repository focuses on the Android application layer.
- The backend API, database, and Docker deployment are part of the overall system architecture, but are not included in this repository.

## What This Project Demonstrates

- building an Android app with Jetpack Compose
- integrating Google Maps into a custom mobile UI
- consuming a REST API with Retrofit
- handling user location and map-centered discovery
- implementing domain-specific filters around place type and price-related criteria
- structuring a project around UI, services, API access, and state management
