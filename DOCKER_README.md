# Docker Setup Guide

## Prerequisites
- Docker Desktop installed on Windows
- Docker Compose (comes with Docker Desktop)

## Quick Start

### 1. Build and Run with Docker Compose
Open PowerShell in the project directory and run:

```powershell
docker compose up --build
```

This command will:
- Build the Spring Boot application
- Start PostgreSQL database
- Start the application
- Create necessary networks and volumes

### 2. Access the Application
Once the containers are running, access the application at:
- **Application URL**: http://localhost:8080
- **Login Page**: http://localhost:8080/login

### 3. Default Credentials
**Teacher Account:**
- Username: `teacher@example.com`
- Password: `teacher123`

**Student Account:**
- Username: `student@example.com`
- Password: `student123`

## Docker Commands

### Start containers (detached mode)
```powershell
docker compose up -d
```

### Stop containers
```powershell
docker compose down
```

### Stop containers and remove volumes (deletes database data)
```powershell
docker compose down -v
```

### View logs
```powershell
# All services
docker compose logs

# Specific service
docker compose logs app
docker compose logs db

# Follow logs
docker compose logs -f app
```

### Rebuild containers
```powershell
docker compose up --build --force-recreate
```

### Check running containers
```powershell
docker compose ps
```

## Database Configuration

The Docker setup uses these database settings:
- **Host**: `db` (internal Docker network)
- **Port**: `5432`
- **Database**: `student_teacher_management`
- **Username**: `postgres`
- **Password**: `postgres`

These are configured in `docker-compose.yml` and override your local `application.properties`.

## Volumes

PostgreSQL data is persisted in a Docker volume named `pgdata`. This means your data will survive container restarts.

To reset the database, remove the volume:
```powershell
docker compose down -v
docker compose up
```

## Troubleshooting

### Port Already in Use
If port 8080 or 5432 is already in use, modify `docker-compose.yml`:

```yaml
ports:
  - "8081:8080"  # Change host port to 8081
```

### Container Fails to Start
Check logs:
```powershell
docker compose logs app
docker compose logs db
```

### Database Connection Issues
Ensure the `db` container is healthy before the app starts. The compose file includes a health check for this.

### Rebuild from Scratch
```powershell
docker compose down -v
docker compose build --no-cache
docker compose up
```

## Production Considerations

For production deployment, consider:

1. **Use environment-specific configuration files**
2. **Change default passwords** (update in `docker-compose.yml`)
3. **Add SSL/TLS configuration**
4. **Configure proper logging**
5. **Set up backup strategy for PostgreSQL volume**
6. **Use Docker secrets for sensitive data**

## File Structure

```
.
├── Dockerfile              # Multi-stage build for Spring Boot app
├── docker-compose.yml      # Orchestrates app and database
├── .dockerignore          # Files to exclude from Docker build
└── DOCKER_README.md       # This file
```
