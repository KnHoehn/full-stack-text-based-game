# Text-Based Game

A full-stack text adventure game built with Java, Spring Boot, React, and TypeScript. This project is an enhanced full-stack version of my [original Python version](https://github.com/KnHoehn/Python-Text-Based-Game), expanding the original console-based game into a web application with a REST API, user authentication, database persistence, dynamic scoring system, and theme selection.

## Overview

The goal of the game is to navigate through a themed map, collect all required items, and reach the boss location. If the player reaches the boss without collecting all required items, the game is lost.

Players can create an account, log in, select a game theme, explore the game world using text commands, collect items, and compete for high scores on the leaderboard.

The application consists of a React/TypeScript frontend communicating with a Java Spring Boot REST API. Game and user data are persisted in a MySQL database.

This project demonstrates full-stack application development, REST API design, authentication and authorization, and database integration.

## Screenshots

### Login

<img width="902" height="437" alt="Screenshot 2026-09-29 173734" src="https://github.com/user-attachments/assets/a8172d12-9c4f-4209-85df-7baaa10a491a" />

### Theme Selection/Leaderboard

<img width="479" height="326" alt="image" src="https://github.com/user-attachments/assets/67e71542-022e-4450-91bc-1bc0775249fd" />

### Gameplay

<img width="478" height="367" alt="image" src="https://github.com/user-attachments/assets/bb37983c-b425-402d-9786-e6f991c028f8" />

<img width="891" height="427" alt="image" src="https://github.com/user-attachments/assets/09841dd6-74e1-4097-9302-a4f6cb10ba30" />

<img width="894" height="428" alt="image" src="https://github.com/user-attachments/assets/337d4cda-1b4a-46a2-b95f-bc66777b4677" />


## Technologies Used

### Backend

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA / Hibernate
* MySQL
* Apache Maven
* Gson

### Frontend

* React
* TypeScript
* Vite
* HTML/CSS

### Development and Testing

* JUnit 5
* Git
* GitHub

## Key Features

* User account creation and login
* JWT-based authentication
* Secure password storage using hashing and salting
* Protected API endpoints
* Multiple game themes:

  * Medieval
  * Cyberpunk
  * Space
* Text-based game commands
* Player inventory and item collection
* Game state management
* Dynamic scoring based on time and number of moves
* Personal score history
* Global leaderboard displaying top scores
* REST API connecting the frontend and backend
* Terminal-inspired web interface with typed command input
* Persistent user and score data using MySQL

## Application Architecture

The application uses a client-server architecture:

```text
React / TypeScript Frontend
            │
            │ HTTP / REST
            ▼
      Spring Boot API
            │
     ┌──────┴──────┐
     │             │
     ▼             ▼
  Game Logic    Authentication
     │             │
     └──────┬──────┘
            ▼
       MySQL Database
```

The frontend is responsible for the user interface and sending requests to the backend. The Spring Boot backend handles authentication, game logic, game state, scoring, and database operations.

JWT tokens are used to authenticate users when accessing protected endpoints.

## User Interface Design

The user interface was intentionally designed with a terminal-inspired aesthetic to complement the text-based nature of the game. 

## Game Mechanics

* The player must collect all 6 required items before reaching the boss.
* Reaching the boss without all required items results in a game loss.
* The final score is calculated using the player's total moves and completion time.
* Completed games are saved to the database and appear on the leaderboard.

## Authentication

User authentication is handled by the Spring Boot backend using Spring Security and JSON Web Tokens (JWT).

The authentication flow is:

1. A user creates an account.
2. Password storage using salted SHA-512 hashing
3. The user logs in with their credentials.
4. The backend verifies the credentials and generates a JWT.
5. The frontend stores the JWT and includes it with requests to protected endpoints.
6. Spring Security validates the token before allowing access to protected resources.

## Database Design

The application uses MySQL for persistent data storage.

The database currently contains tables for:

* **Users** — stores user accounts and authentication information.
* **Scoreboard** — stores completed game scores, moves, completion time, and selected theme.

Each scoreboard entry is associated with a user through a foreign key relationship between score_board.user_id and users.user_id. This allows scores to reference the user's unique database ID rather than storing the username directly in the scoreboard table.

## API

The Spring Boot application exposes REST endpoints for authentication, game functionality, and scores. Protected endpoints require a valid JWT.

## My Contributions

* Redesigned the original console-based game as a full-stack web application
* Built the Spring Boot REST API
* Developed the game state and game progression logic
* Implemented JWT authentication using Spring Security
* Implemented secure password storage using hashing and salting
* Designed and implemented REST endpoints for authentication, gameplay, and scoring
* Integrated Spring Data JPA with MySQL for persistent data storage
* Developed the React/TypeScript frontend
* Connected the frontend to the Spring Boot REST API
* Implemented game theme selection and text-based command input
* Developed the player inventory and item collection system
* Implemented the scoring algorithm based on time and number of moves
* Implemented personal score history and a global leaderboard
* Structured the application using separate controllers, services, repositories, entities, DTOs, and frontend components
* Tested and debugged the application throughout development

## How to Run the Project Locally

### Prerequisites

* Java JDK 17 or higher
* Maven
* Node.js and npm
* MySQL
* Git

### Backend Setup

1. Clone the repository.

2. Create a MySQL database named:

```sql
CREATE DATABASE game_db;
```

3. Select the database:

```sql
USE game_db;
```

4. Create the `users` table:

```sql
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL UNIQUE,
    user_password VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL
);
```

5. Create the `score_board` table:

```sql
CREATE TABLE score_board (
    score_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    score INT NOT NULL,
    moves INT NOT NULL,
    time INT NOT NULL,
    theme VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

6. Configure the database connection and JWT secret using the application's local configuration.

7. Start the Spring Boot application.

### Frontend Setup

1. Navigate to the frontend directory.

2. Install the dependencies:

```bash
npm install
```

3. Start the development server:

```bash
npm run dev
```

4. Open the URL provided by Vite in your browser.

## Challenges and Learning Outcomes

One of the primary challenges of this project was transforming an existing console application into a full-stack web application.

This required learning how to separate the application into a frontend and backend, design REST APIs, manage communication between React and Spring Boot, and maintain game state across HTTP requests.

Additional challenges included:

* Implementing JWT authentication and authorization
* Designing DTOs to control data exchanged through the API
* Integrating Spring Data JPA with MySQL
* Managing game state on the backend
* Connecting a TypeScript frontend to a Java REST API
* Designing a scoring system based on multiple gameplay factors
* Structuring the application into maintainable layers
* Debugging communication between the frontend, backend, and database
* Protecting authenticated API endpoints

Through this project, I strengthened my skills in Java, Spring Boot, REST API development, React, TypeScript, database integration, authentication, and full-stack application architecture.

## Original Project

This application was developed from my original console-based text adventure game:

**[Python Text-Based Game](https://github.com/KnHoehn/Python-Text-Based-Game)**

The original project was a console-based text adventure focused on game logic, room navigation, item collection, and command-based gameplay. I expanded the game into a full-stack web application by adding a React/TypeScript frontend, Spring Boot REST API, user authentication, database persistence, theme selection, and scoring and leaderboard functionality.

## Game Maps

The game is meant to be played without a map, however if you get stuck during a playthrough and would find it helpful the maps for each theme are provided below.

### Space Theme

<img width="377" height="382" alt="Space Text-Based Game Map" src="https://github.com/user-attachments/assets/af45f60c-48b9-49d5-bc8f-6f6b28d052bd" />

### Medieval Theme

<img width="334" height="312" alt="Medieval Text-Based Game Map" src="https://github.com/user-attachments/assets/b0ac8d95-bc0e-40ff-80ee-726ed4d3cf4b" />

### Cyberpunk Theme

<img width="317" height="317" alt="Cyberpunk Text-Based Game Map" src="https://github.com/user-attachments/assets/f49af801-42ce-49cf-bd61-c326c452f55a" />
