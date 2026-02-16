# 🔧 Troubleshooting Guide

## If PostgreSQL Setup is Difficult - Use H2 Database Instead

### Option 1: Quick Switch to H2 (In-Memory Database)

**Step 1:** Open `src/main/resources/application.properties`

**Step 2:** Replace ALL content with this:

```properties
spring.application.name=Student_Teacher_Management

# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:student_management
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

# Thymeleaf Configuration
spring.thymeleaf.cache=false

# Server Configuration
server.port=8080
```

**Step 3:** Run the application:
```bash
.\mvnw.cmd spring-boot:run
```

**Done!** No PostgreSQL needed! ✅

**Note:** With H2, data is lost when you stop the application. This is perfect for demonstration purposes.

---

## Common Errors & Solutions

### Error 1: "Cannot connect to database"
**Symptoms:** Application fails to start, shows connection error

**Solution A - Use H2 (Easier):**
Follow the H2 setup above ☝️

**Solution B - Fix PostgreSQL:**
1. Check if PostgreSQL is running:
   - Windows: Open Services → Find PostgreSQL → Start it
   - Mac: `brew services start postgresql`
   - Linux: `sudo systemctl start postgresql`

2. Check credentials in application.properties:
   ```properties
   spring.datasource.username=postgres
   spring.datasource.password=YOUR_PASSWORD
   ```

3. Create database:
   ```sql
   CREATE DATABASE student_management;
   ```

---

### Error 2: "Port 8080 already in use"
**Symptoms:** Error message about port 8080

**Solution:** Change the port in application.properties:
```properties
server.port=8081
```
Then access at: `http://localhost:8081/login`

---

### Error 3: "Maven build failed"
**Symptoms:** Compilation errors, dependencies not downloaded

**Solution:**
```bash
# Windows
.\mvnw.cmd clean install -U

# Mac/Linux
./mvnw clean install -U
```

If still failing:
1. Delete the `.m2/repository` folder
2. Run the command again

---

### Error 4: "Login page not loading"
**Symptoms:** Browser shows error or blank page

**Solution:**
1. Make sure application is running (check console for errors)
2. Clear browser cache (Ctrl+Shift+Delete)
3. Try incognito mode
4. Try different browser

---

### Error 5: "Tables not created"
**Symptoms:** Login fails, no data in database

**Solution:**
Check `application.properties` has:
```properties
spring.jpa.hibernate.ddl-auto=update
```

For H2, check tables in console:
- Go to: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:student_management`
- Username: `sa`
- Password: (leave empty)
- Click Connect

---

### Error 6: "Default users not created"
**Symptoms:** Cannot login with teacher/student123

**Solution:**
1. Check console logs for DataInitializer output
2. Manually create user in database:

For PostgreSQL:
```sql
INSERT INTO users (username, password, role, enabled) 
VALUES ('teacher', '$2a$10$YourBcryptHashHere', 'TEACHER', true);
```

Or delete and recreate database:
```sql
DROP DATABASE student_management;
CREATE DATABASE student_management;
```
Then restart application.

---

### Error 7: "Thymeleaf template not found"
**Symptoms:** "Error resolving template" message

**Solution:**
1. Check file structure:
   ```
   src/main/resources/templates/
   ├── login.html
   ├── teacher/
   │   └── students-list.html
   └── student/
       └── profile.html
   ```

2. Restart application

---

### Error 8: "Java version mismatch"
**Symptoms:** Error about Java version

**Solution:**
1. Check Java version:
   ```bash
   java -version
   ```
   Should be 17 or higher

2. Update pom.xml if needed:
   ```xml
   <properties>
       <java.version>17</java.version>
   </properties>
   ```

---

## Quick Fixes

### Application won't start?
```bash
# Kill any process on port 8080 (Windows)
netstat -ano | findstr :8080
taskkill /PID <PID_NUMBER> /F

# Then restart
.\mvnw.cmd spring-boot:run
```

### Need to reset everything?
```bash
# Stop application (Ctrl+C)
# Delete database
# Restart application
```

### Can't access /teacher/students?
- Make sure you're logged in as teacher (teacher/teacher123)
- Check URL is correct: `http://localhost:8080/teacher/students`

### Student can't login?
- First, login as teacher
- Create a student account
- Then login with that username/password

---

## Pre-Demo Checklist ✅

Before showing to your teacher:

- [ ] Application starts without errors
- [ ] Can access login page
- [ ] Can login as teacher (teacher/teacher123)
- [ ] Can create a student
- [ ] Can view student list
- [ ] Can edit student
- [ ] Can delete student
- [ ] Can logout
- [ ] Can login as student
- [ ] Student can view profile
- [ ] Student can edit profile (but NOT roll number)
- [ ] Roll number field is disabled for student

---

## Emergency Backup Plan

If nothing works on demo day:

1. **Record a video** beforehand showing all features
2. **Take screenshots** of each major feature
3. **Prepare slide deck** explaining the code and features

---

## Contact for Help

If you're stuck:
1. Check README.md for detailed setup
2. Check QUICK_START.md for fast setup
3. Google the specific error message
4. Check Spring Boot documentation
5. Ask a classmate who knows Spring Boot

---

## Most Important Tip 🌟

**USE H2 DATABASE FOR DEMO!**

Why?
- ✅ No setup needed
- ✅ Works immediately
- ✅ No PostgreSQL installation required
- ✅ No database connection issues
- ✅ Perfect for demonstrations

Just follow "Option 1" at the top of this file!

**Good luck! You've got this! 💪**
