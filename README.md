# SoundWave Backend

SoundWave is a Spring Boot REST API for a music streaming application. It handles user authentication, song uploads and search, playlists, and playback tracking, with media files stored on Cloudinary and a PostgreSQL database for persistence.

## Features

- **Authentication & Authorization**
  - Register / login with email and password
  - JWT-based stateless authentication
  - Role-based access control (`USER`, `ADMIN`)
  - Change password, update profile
  - Forgot password / reset password via email (SMTP)
- **Songs**
  - Upload songs with audio file + optional cover image (Cloudinary storage)
  - Browse all songs
  - Search songs with pagination and sorting
  - Track recently played songs and total play count
- **Playlists**
  - Create, rename, and delete playlists
  - Add/remove songs from a playlist
  - List a user's playlists
- **User Profile**
  - Fetch the authenticated user's profile

## Tech Stack

- **Java 21**
- **Spring Boot 3.2.5** (Web, Data JPA, Security, Validation, Mail)
- **PostgreSQL** — primary database
- **JWT (jjwt 0.11.5)** — stateless authentication
- **Cloudinary** — audio/image file storage
- **Lombok** — boilerplate reduction
- **Maven** — build tool
- **Docker** — containerized deployment

## Project Structure

```
src/main/java/com/ashvinprajapati/soundwave/
├── auth/            # Registration, login, password reset, user entity/role
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── user/            # User profile endpoints
├── song/            # Song upload, search, recently played
├── playlist/        # Playlist CRUD and song management
├── storage/         # Cloudinary storage abstraction
├── common/
│   ├── config/      # Security & Cloudinary configuration
│   └── security/    # JWT service & authentication filter
└── SoundWaveApplication.java
```

## Prerequisites

- Java 21
- Maven 3.9+ (or use the included `mvnw` wrapper)
- PostgreSQL database
- A Cloudinary account (for file storage)
- An SMTP-enabled email account (for password reset emails, e.g. Gmail)

## Configuration

The application is configured entirely through environment variables (see `src/main/resources/application.yml`):

| Variable | Description |
|---|---|
| `DB_URL` | PostgreSQL JDBC connection URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `MAIL_USERNAME` | SMTP username (e.g. Gmail address) |
| `MAIL_PASSWORD` | SMTP password / app password |
| `JWT_SECRET` | Secret key used to sign JWTs |
| `CLOUDINARY_NAME` | Cloudinary cloud name |
| `CLOUDINARY_KEY` | Cloudinary API key |
| `CLOUDINARY_SECRET` | Cloudinary API secret |
| `PORT` | Server port (defaults to `8080`) |

Create a `.env` file or export these variables in your shell before running the app. Example:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/soundwave
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=your-app-password
export JWT_SECRET=your-256-bit-secret
export CLOUDINARY_NAME=your-cloud-name
export CLOUDINARY_KEY=your-api-key
export CLOUDINARY_SECRET=your-api-secret
```

> JPA is configured with `ddl-auto: update`, so tables are created/updated automatically based on the entities — no manual schema migration is required for local development.

## Running Locally

1. Clone the repository and navigate into it:
   ```bash
   git clone <repo-url>
   cd SoundWave-Backend
   ```
2. Set the environment variables listed above.
3. Run with the Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```
   On Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```
4. The API will be available at `http://localhost:8080`.

### Build a JAR

```bash
./mvnw clean package
java -jar target/soundwave-0.0.1-SNAPSHOT.jar
```

## Running with Docker

```bash
docker build -t soundwave-backend .
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host:5432/soundwave \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=postgres \
  -e MAIL_USERNAME=your-email@gmail.com \
  -e MAIL_PASSWORD=your-app-password \
  -e JWT_SECRET=your-256-bit-secret \
  -e CLOUDINARY_NAME=your-cloud-name \
  -e CLOUDINARY_KEY=your-api-key \
  -e CLOUDINARY_SECRET=your-api-secret \
  soundwave-backend
```

## API Endpoints

### Auth — `/api/auth`

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/register` | Register a new user | No |
| POST | `/login` | Log in and receive a JWT | No |
| POST | `/change-password` | Change the current user's password | Yes |
| POST | `/forgot-password?email=` | Request a password reset email | No |
| POST | `/reset-password?token=&newPassword=` | Reset password using a token | No |
| PUT | `/update-profile` | Update the current user's profile | Yes |

### Users — `/api/users`

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| GET | `/me` | Get the authenticated user's profile | Yes |

### Songs — `/api/songs`

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| GET | `/` | List all songs | Yes |
| GET | `/search?q=&page=&size=&sortBy=&sortDir=` | Search songs (paginated & sortable) | Yes |
| POST | `/upload` | Upload a new song (audio + optional cover) | Admin only |
| POST | `/{id}/played` | Mark a song as played by the current user | Yes |
| GET | `/recently-played` | Get the current user's recently played songs | Yes |
| GET | `/played-count` | Get the total number of plays for the current user | Yes |

### Playlists — `/api/playlists`

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/?name=` | Create a new playlist | Yes |
| GET | `/` | Get all playlists for the current user | Yes |
| POST | `/{playlistId}/songs/{songId}` | Add a song to a playlist | Yes |
| DELETE | `/{playlistId}/songs/{songId}` | Remove a song from a playlist | Yes |
| PUT | `/{playlistId}/update` | Rename a playlist | Yes |
| DELETE | `/{playlistId}/delete` | Delete a playlist | Yes |

## Authentication

Most endpoints require a JWT in the `Authorization` header:

```
Authorization: Bearer <token>
```

Obtain a token by calling `POST /api/auth/login` (or `/api/auth/register`), then include it on subsequent requests. Tokens are valid for 24 hours by default (`jwt.expiration=86400000` ms), configurable in `application.yml`.

## License

No license has been specified for this project.
