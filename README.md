# BrokeMap

**BrokenMap** aims to provide the best way to find activities near you according to your principles and tastes.

You will be able to visualize all locations on a map and filter the type of location you are looking for with specific criterion such as beer pinte price for bars, a student discount in a museum, ...

## Setup

This section describes what you need to do if you want to build the app locally.

You need to have a Google Maps Platform API key (you can obtain one by following this [guide]()).
The `secrets.example.properties` file is a template for a file named `secrets.properties` which contains the secrets value such as the API keys.
The template contents is the following:
```
MAPS_API_KEY=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
```
Once you have the API key and copied the file replace the value of `MAPS_API_KEY` with yours.