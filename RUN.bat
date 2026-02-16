@echo off
echo ========================================
echo Student Management System - Quick Run
echo ========================================
echo.
echo This will start your application!
echo.
echo Make sure PostgreSQL is running OR
echo Switch to H2 database in application.properties
echo.
echo Starting application...
echo.
echo Wait for "Started StudentTeacherManagementApplication" message
echo Then open: http://localhost:8080/login
echo.
echo Login credentials:
echo Teacher: teacher / teacher123
echo Student: student / student123
echo.
echo ========================================
echo.

cd /d "%~dp0"
call mvnw.cmd spring-boot:run

pause
