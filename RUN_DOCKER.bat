@echo off
echo ========================================
echo Student Teacher Management System
echo Docker Container Startup
echo ========================================
echo.

REM Check if Docker is running
echo Checking if Docker Desktop is running...
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Docker Desktop is not running!
    echo.
    echo Please follow these steps:
    echo 1. Press Windows Key and search for "Docker Desktop"
    echo 2. Click to launch Docker Desktop
    echo 3. Wait 2-3 minutes for Docker to fully start
    echo 4. Look for the Docker icon in system tray (it should be green/running)
    echo 5. Run this script again
    echo.
    echo For more help, see DOCKER_TROUBLESHOOTING.md
    echo.
    pause
    exit /b 1
)

echo Docker Desktop is running!
echo.

echo Building and starting Docker containers...
echo This may take a few minutes on first run.
echo.

docker compose up --build

echo.
echo ========================================
echo Containers stopped.
echo ========================================
pause
