# MemoriesV1Backend
Memories App where user can store their personal notes, images etc. This is the Backend Application made using Spring boot. This app offers Spring Security and JWT authentication for top level security.

# 1. Image Upload and Delete Service

This project provides a RESTful API for uploading and deleting images. It includes endpoints for uploading images with a timestamped filename and deleting images based on their URL.

## Features

- **Image Upload:** Uploads images to a specified folder with a timestamped filename.
- **Image Delete:** Deletes images based on their URL.

## Technologies Used

- **Spring Boot:** Framework for building the application.
- **Spring Web:** Module for creating RESTful web services.
- **Spring Dependency Injection:** Used for injecting the `ImageUploadService` into the controller.

## Endpoints

### Upload Image

- **URL:** `/api/images/upload`
- **Method:** `POST`
- **Request Parameter:**
  - `file` (MultipartFile): The image file to be uploaded.
- **Response:**
  - **Success:** `200 OK` with the URL of the uploaded image.
  - **Failure:** `500 Internal Server Error` with an error message.

### Delete Image

- **URL:** `/api/images/delete`
- **Method:** `DELETE`
- **Request Parameter:**
  - `url` (String): The URL of the image to be deleted.
- **Response:**
  - **Success:** `200 OK` with a success message.
  - **Failure:** `500 Internal Server Error` with an error message.

 # 2. User and Post Management Service

This project provides a RESTful API for managing users and their posts. It includes endpoints for user-specific actions such as updating user information, retrieving current user details, and managing posts (creating, updating, retrieving, and deleting).

## Features

- **User Management:**
  - Retrieve current user details
  - Update current user information
- **Post Management:**
  - Create a new post
  - Retrieve a single post by ID
  - Retrieve all posts for the current user
  - Update a post
  - Delete a post

## Technologies Used

- **Spring Boot:** Framework for building the application.
- **Spring Web:** Module for creating RESTful web services.
- **Spring Security:** Module for handling authentication and authorization.
- **Spring Data JPA:** For database interactions.
- **Lombok:** To reduce boilerplate code with annotations.

## Endpoints

### User Endpoints

#### Get User Home

- **URL:** `/api/v1/user`
- **Method:** `GET`
- **Response:**
  - **Success:** `200 OK` with a welcome message and the username.
  - **Failure:** `401 Unauthorized` if the user is not authenticated.

#### Update Current User

- **URL:** `/api/v1/user/update-user`
- **Method:** `POST`
- **Request Body:** `User` object with updated information.
- **Response:**
  - **Success:** `200 OK` with a success message and updated user information.
  - **Failure:** `404 Not Found` if the user is not found.
  - **Failure:** `500 Internal Server Error` if there is an error during the update.

#### Get Current User

- **URL:** `/api/v1/user/current-user`
- **Method:** `GET`
- **Response:**
  - **Success:** `200 OK` with current user details.
  - **Failure:** `404 Not Found` if the user is not found.

### Post Endpoints

#### Create Post

- **URL:** `/api/v1/user/create-post`
- **Method:** `POST`
- **Request Body:** `Posts` object with post details.
- **Response:**
  - **Success:** `200 OK` with a success message and the saved post.
  - **Failure:** `404 Not Found` if the user is not found.

#### Get Post by ID

- **URL:** `/api/v1/user/get-post/{postId}`
- **Method:** `GET`
- **Path Variable:** `postId` (Long): The ID of the post to retrieve.
- **Response:**
  - **Success:** `200 OK` with the post details.
  - **Failure:** `404 Not Found` if the post or user is not found.

#### Get All Posts for Current User

- **URL:** `/api/v1/user/get-all-posts`
- **Method:** `GET`
- **Response:**
  - **Success:** `200 OK` with a list of all posts for the current user.
  - **Failure:** `404 Not Found` if the user is not found.

#### Update Post

- **URL:** `/api/v1/user/update-post`
- **Method:** `PUT`
- **Request Body:** `Posts` object with updated post details.
- **Response:**
  - **Success:** `200 OK` with the updated post details.
  - **Failure:** `403 Forbidden` if the user is not authorized to update the post.
  - **Failure:** `404 Not Found` if the post or user is not found.
  - **Failure:** `500 Internal Server Error` if there is an error during the update.

#### Delete Post

- **URL:** `/api/v1/user/delete-post/{postId}`
- **Method:** `DELETE`
- **Path Variable:** `postId` (Long): The ID of the post to delete.
- **Response:**
  - **Success:** `200 OK` with a success message.
  - **Failure:** `403 Forbidden` if the user is not authorized to delete the post.
  - **Failure:** `404 Not Found` if the post or user is not found.
  - **Failure:** `500 Internal Server Error` if there is an error during the deletion.


# 3. Admin and User Management Service

This project provides a RESTful API for managing admin and user entities. It includes endpoints for admin-specific actions such as updating admin details, fetching all admins, and managing users (retrieving, deleting).

## Features

- **Admin Management:**
  - Retrieve a demo message for admin.
  - Update admin details.
  - Fetch all admins.
- **User Management:**
  - Retrieve all users.
  - Retrieve a user by ID.
  - Delete a user by ID.

## Technologies Used

- **Spring Boot:** Framework for building the application.
- **Spring Web:** Module for creating RESTful web services.
- **Spring Data JPA:** For database interactions.
- **Lombok:** To reduce boilerplate code with annotations.

## Endpoints

### Admin Endpoints

#### Admin Demo

- **URL:** `/api/v1/admin`
- **Method:** `GET`
- **Response:**
  - **Success:** `200 OK` with a demo message for the admin.

#### Update Admin Details

- **URL:** `/api/v1/admin/updateAdminDetails/{id}`
- **Method:** `PUT`
- **Path Variable:** `id` (UUID): The ID of the admin to update.
- **Request Body:** `Admin` object with updated information.
- **Response:**
  - **Success:** `200 OK` with the updated admin details.
  - **Failure:** `404 Not Found` if the admin is not found.

#### Fetch All Admins

- **URL:** `/api/v1/admin/getAllAdmin`
- **Method:** `GET`
- **Response:**
  - **Success:** `200 OK` with a list of all admins.
  - **Failure:** `404 Not Found` if no admins are found.

### User Endpoints

#### Fetch All Users

- **URL:** `/api/v1/admin/getAllUsers`
- **Method:** `GET`
- **Response:**
  - **Success:** `200 OK` with a list of all users.
  - **Failure:** `404 Not Found` if no users are found.

#### Fetch User by ID

- **URL:** `/api/v1/admin/getUserById/{id}`
- **Method:** `GET`
- **Path Variable:** `id` (UUID): The ID of the user to retrieve.
- **Response:**
  - **Success:** `200 OK` with the user details.
  - **Failure:** `404 Not Found` if the user is not found.

#### Delete User by ID

- **URL:** `/api/v1/admin/deleteUser/{id}`
- **Method:** `DELETE`
- **Path Variable:** `id` (UUID): The ID of the user to delete.
- **Response:**
  - **Success:** `200 OK` with a success message.
  - **Failure:** `500 Internal Server Error` if there is an error during deletion.
  - **Failure:** `404 Not Found` if the user is not found.

## Usage

### 1. Clone the Repository

```bash
git clone https://github.com/Chayan9991/MemoriesV1Backend.git
