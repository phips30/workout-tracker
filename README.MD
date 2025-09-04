# Workout Tracker
As an avid fitness enthusiast, I often find myself creating custom workout routines 
tailored to my specific needs and goals. However, I've struggled to find an app that offers 
the level of customization I'm looking for. Most existing solutions seem to cater to a 
one-size-fits-all approach, limiting my ability to track my progress effectively.

Moreover, I'm concerned about storing my sensitive health data on remote servers, where 
it may be accessed or shared without my consent. I want to have full control over my data 
and ensure that it remains private.

To address these needs, I'm developing a personal workout tracking app that prioritizes 
customization, data ownership, and control. The project is divided into three phases:

 - Phase 1 (MVP): The backend is already developed, providing a RESTful interface that can be accessed by a simple client on my phone. This allows me to start tracking my workouts and storing my data locally.
 - Phase 2: A user-friendly frontend will be developed to simplify data entry and make it easier to interact with the app.
 - Phase 3: I'll integrate data science models to analyze and forecast my progress, providing valuable insights to help me optimize my workouts and achieve my fitness goals.

By developing this app, I aim to create a solution that meets my specific needs and provides 
a high degree of customization, while also ensuring the security and privacy of my health data.

## Architecture

### Target architecture
![Target Architecture](architecture/target-architecture.drawio.png)

### Minimum viable product
The MVP of the workout tracking app is currently in the development stage. 
The goal of the MVP is to provide a basic implementation of the app's core functionality, 
focusing on tracking workouts and storing data locally.

**Architecture**

The app's architecture is designed to be modular, scalable, and maintainable. 
The backend is written purely in Java, with a focus on separation of concerns and clear layering. 
The architecture consists of the following layers:

 - Domain Layer: This layer represents the business logic of the app, where the core domain models and behaviors are defined.
 - Application Layer: This layer acts as an interface between the domain layer and the infrastructure layer, defining the use cases and interactions.
 - Infrastructure Layer: This layer is responsible for data storage and retrieval. Currently, data is stored in simple JSON files.
 - Interface Layer: This layer provides the RESTful API interface to the app, built using Spring Boot.

**Technology Stack**

 - Backend: Java
 - Framework: Spring Boot
 - Data Storage: JSON files

![MVP Architecture](architecture/mvp-architecture.drawio.png)