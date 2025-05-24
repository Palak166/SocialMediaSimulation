# Social Media Simulation - GUVI Galgotias Project Board (2nd Sem 2028)

## Project Overview
This project is a basic social media simulation developed by a first-year student for the GUVI - Galgotias Project Board. It demonstrates the application of theoretical knowledge in Java programming, database management (MySQL), and GUI development (Swing) to create a practical, real-world application.

## Features
* User Registration
* User Login
* Create New Posts
* View Public Feed (all posts)

## Technologies Used
* **Java (JDK 17/21)**
* **MySQL Database**
* **JDBC** for database connectivity
* **Swing** for Graphical User Interface (GUI)
* **IntelliJ IDEA** (IDE)

## Project Structure
social-media-simulation/
├── src/
│   ├── com/
│   │   └── socialmedia/
│   │       ├── app/           (Contains GUI frames like LoginRegisterFrame, DashboardFrame)
│   │       ├── dao/           (Data Access Objects: UserDAO, PostDAO)
│   │       ├── model/         (Model classes: User, Post)
│   │       └── util/          (Utility classes: DBConnection)
│   └── &lt;your-main-class-file>.java
├── lib/                    (Optional: for external JARs if not managed by IDE directly)
│   └── mysql-connector-j-8.x.x.jar
├── .idea/                  (IntelliJ IDEA project files)
├── README.md               (This file)
└── social-media-simulation.iml


## Database Setup
1.  **Install MySQL Server** and **MySQL Workbench**.
2.  Open MySQL Workbench and create a new schema named `social_media_db`.
3.  Execute the following SQL commands to create the `users` and `posts` tables:

    ```sql
    CREATE DATABASE social_media_db;
    USE social_media_db;

    CREATE TABLE users (
        id INT PRIMARY KEY AUTO_INCREMENT,
          username VARCHAR(50) UNIQUE NOT NULL,
password VARCHAR(255) NOT NULL,
email VARCHAR(100) UNIQUE NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

    CREATE TABLE posts (
        id INT PRIMARY KEY AUTO_INCREMENT,
        user_id INT,
        content TEXT NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );
    ```
4.  **Update `DBConnection.java`:**
    Open `src/com/socialmedia/util/DBConnection.java` and update the `PASSWORD` constant with your MySQL root password.

    ```java
    private static final String PASSWORD = "your_mysql_root_password"; // <--- UPDATE THIS
    ```

## Project Setup and How to Run
1.  **Prerequisites:**
    * Install **JDK (Java Development Kit)** version 17 or higher.
    * Install **IntelliJ IDEA Community Edition** (recommended) or Eclipse IDE for Java Developers.
    * Install **MySQL Server** and **MySQL Workbench**.
2.  **Clone the Repository:**
    ```bash
    git clone [https://github.com/your-username/SocialMediaSimulation.git](https://github.com/your-username/SocialMediaSimulation.git)
    cd SocialMediaSimulation
    ```
3.  **Import into IntelliJ IDEA:**
    * Open IntelliJ IDEA.
    * Select `File` -> `Open` and navigate to the `SocialMediaSimulation` folder you just cloned.
    * Select `Open as Project`.
4.  **Add MySQL JDBC Driver:**
    * Download `mysql-connector-j-8.x.x.jar` from [MySQL Connector/J Downloads](https://dev.mysql.com/downloads/connector/j/). Choose the "Platform Independent" `.zip`.
    * Extract the JAR file.
    * In IntelliJ IDEA, go to `File` -> `Project Structure` (`Ctrl+Alt+Shift+S`).
    * Under `Project Settings`, select `Libraries`.
    * Click the `+` button, select `Java`, and navigate to the `mysql-connector-j-8.x.x.jar` file you downloaded. Click `OK` -> `Apply` -> `OK`.
5.  **Run the Application:**
    * Navigate to `src/com/socialmedia/app/LoginRegisterFrame.java`.
    * Right-click on the file and select `Run 'LoginRegisterFrame.main()'`.

## Contributing
(Optional: You can remove this section for a project board)
Feel free to fork this repository and contribute!

## License
(Optional: You can add a license like MIT if you wish)
````
