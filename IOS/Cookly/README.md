# Cookly - iOS App
Cookly is a social media app for people who want to share, or discover new recipes. This is the iOS frontend that brings the platform to life, providing a smooth, native, and interactive user experience for the community.

$$

## App Architecture
The app is built using SwiftUI and follows the MVVM (Model-View-ViewModel) architectural pattern. This keeps the declarative UI code clean and separates the business logic and API calls into dedicated ViewModels.

## How It's Made:
Tech used: Swift, SwiftUI

I wanted to build a native mobile interface from scratch to really understand how to connect a complex backend to a client application. The idea was to create a responsive, modern UI that could actually handle real-world social media features like feeds, user profiles, authentication, and media uploads.

The first thing I tackled was the UI layout with SwiftUI. It was incredibly fun to see the app come to life visually. However, the real challenge began when I started connecting the app to the backend. Managing the state of the app—especially handling the JWT access and refresh tokens on the client side—was intimidating at first, but it taught me exactly how modern mobile apps keep users securely logged in.

With the views and authentication state set up, I started modeling the frontend data using Codable to parse the JSON coming from my backend. This is where I learned one of my biggest lessons: integration. Connecting the SwiftUI views to the backend API highlighted the fact that you really have to plan your system architecture and data contracts ahead of time. Dealing with mismatched data types or unexpected nulls was a headache, but it made me realize that thinking deeply about integration from day one will make everything in my next project much easier.

The feature that made me the proudest was the image upload system. Picking a photo from the iOS gallery and converting it into a multipart-form request to send to the Java backend seemed like a massive hurdle. But just like on the backend, breaking it down step-by-step made it completely manageable and highly rewarding.

The main objective of this iOS project was to learn how to build and integrate a real frontend with a backend. I definitely achieved that. The project isn't absolutely perfect, but the hands-on experience I gained regarding real-world project development and system integration is invaluable.

## What I've Learned:

- Building declarative UI with SwiftUI

- Implementing the MVVM architecture

- Managing complex app state (StateObject, ObservedObject, Environment)

- Handling asynchronous network requests with URLSession

- Parsing complex JSON responses using Codable

- Client-side JWT management (securely storing and refreshing tokens)

- Implementing image pickers and handling multipart form data uploads

- The critical importance of planning backend/frontend integration early on

![Image of the application](https://i.imgur.com/7M9apVj.png)