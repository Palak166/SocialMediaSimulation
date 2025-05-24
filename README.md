Social Media Simulation - GUVI Galgotias Project (2nd Sem 2028)
Project Overview
This project is a basic GUI-based social media simulation developed as part of the GUVI - Galgotias Project Board for the 2nd Semester (2028). The aim is to provide a hands-on platform for applying theoretical knowledge in Java programming, database management, and GUI design.

This simulation allows users to:

Register for a new account.
Log in to an existing account.
Create and share new posts on a public feed.
View posts from all users on the main dashboard.
Features
User Management: Secure user registration and login functionality.
Database Integration: Stores user credentials and post content in a MySQL database.
Post Creation: Allows authenticated users to write and publish new posts.
Public Feed: Displays a chronological list of all posts made by users.
Simple Graphical User Interface (GUI): Built using Java Swing for intuitive interaction.
Technologies Used
Java Development Kit (JDK): Version 17+ (e.g., JDK 21)
Integrated Development Environment (IDE): IntelliJ IDEA Community Edition (recommended)
Database: MySQL Server
Database Client: MySQL Workbench
Database Connectivity: JDBC (Java Database Connectivity)
GUI Framework: Java Swing
Project Structure
The project is organized into logical packages to maintain modularity and readability:

social-media-simulation/
├── src/
│   └── com/
│       └── socialmedia/
│           ├── app/           (Contains GUI frames like LoginRegisterFrame, DashboardFrame)
│           ├── dao/           (Data Access Objects: UserDAO, PostDAO - handles database interactions)
│           ├── model/         (Model classes: User, Post - represent database entities)
│           └── util/          (Utility classes: DBConnection - manages database connection)
├── lib/                     (Contains external libraries, specifically the MySQL JDBC Connector JAR)
│   └── mysql-connector-j-8.x.x.jar  (Your specific version might vary)
├── .idea/                   (IntelliJ IDEA project configuration files)
├── README.md                (This file)
└── [Your_Project_Name].iml  (IntelliJ IDEA module file)
Database Setup
Follow these steps to set up the MySQL database for the project:

Install MySQL Server and MySQL Workbench: If you haven't already, download and install both from the official MySQL website. Remember the root password you set during installation.

Open MySQL Workbench and connect to your local MySQL instance (usually localhost:3306 with root username).

Create the Database and Tables: Execute the following SQL commands in a new query tab in MySQL Workbench:

SQL

-- Create the database
CREATE DATABASE social_media_db;

-- Use the newly created database
USE social_media_db;

-- Create the 'users' table
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, -- In a real app, always hash passwords!
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );

-- Create the 'posts' table
CREATE TABLE posts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```
Update Database Credentials in Java Code:
Open the file src/com/socialmedia/util/DBConnection.java in your project.
CRITICALLY IMPORTANT: Replace "your_mysql_root_password" with the actual root password you set for your MySQL server.

Java

// Inside src/com/socialmedia/util/DBConnection.java
private static final String USER = "root";
private static final String PASSWORD = "your_mysql_root_password"; // <--- UPDATE THIS LINE
Getting Started
Follow these instructions to set up and run the project on your local machine.

Prerequisites
Java Development Kit (JDK) 17 or higher: Ensure it's installed and configured in your system's PATH.
IntelliJ IDEA Community Edition: Download and install it.
MySQL Server and MySQL Workbench: As set up in the "Database Setup" section.
Building and Running the Application
Clone the Repository:

Bash

git clone [Your_GitHub_Repository_HTTPS_Link]
cd social-media-simulation
(Replace [Your_GitHub_Repository_HTTPS_Link] with the actual HTTPS link from your GitHub repo.)

Open the Project in IntelliJ IDEA:

Launch IntelliJ IDEA.
Select File -> Open and navigate to the social-media-simulation folder you just cloned.
Click Open.
Add the MySQL JDBC Driver:
The project needs the MySQL Connector/J library to connect to the database.

If not already present in the lib folder or configured:
Download mysql-connector-j-8.x.x.jar from the official MySQL Connector/J Downloads page. Choose the "Platform Independent" .zip archive and extract the JAR file.
In IntelliJ IDEA, go to File -> Project Structure... (or Ctrl+Alt+Shift+S).
Under Project Settings, select Libraries.
Click the + button, select Java, and navigate to the mysql-connector-j-8.x.x.jar file you downloaded. Click OK -> Apply -> OK.
Run the Application:

Navigate to src/com/socialmedia/app/LoginRegisterFrame.java in the Project explorer.
Right-click on LoginRegisterFrame.java.
Select Run 'LoginRegisterFrame.main()'.
The application's Login/Register window should appear. You can now register a new user and explore the dashboard!

Screenshots
(Optional, but highly recommended for a GUI project board. Replace these with actual screenshots of your running application.)

Login/Registration Screen
Dashboard (Public Feed)
Future Enhancements (Ideas for further development)
User profile pages
Liking and commenting on posts
Following/unfollowing other users
Private messaging
Search functionality for users or posts
More robust input validation and error handling
Password hashing for improved security
Contributing
This project is a submission for the GUVI - Galgotias Project Board. While contributions are not expected for this specific submission, feel free to fork the repository for your own learning and experimentation.

License
This project is open-source and available under the MIT License.
(You might need to create a LICENSE file in your root directory if you want to explicitly include the MIT license text.)
