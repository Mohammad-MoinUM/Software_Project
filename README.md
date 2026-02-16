<<<<<<< HEAD
# Student Management System

## 📋 Project Overview
A comprehensive Student Management System built with Spring Boot that demonstrates **Authentication** and **Authorization** principles. The system implements role-based access control with two distinct user roles: **TEACHER** and **STUDENT**.

## 🎯 Key Features

### Authentication & Authorization (Main Focus)
- **Spring Security** integration with role-based access control
- **Two User Roles:**
  - **TEACHER**: Full CRUD operations on student profiles
  - **STUDENT**: Can only view and update own profile (except roll number)
- Password encryption using BCrypt
- Custom login page with session management
- Protected endpoints based on user roles

### Teacher Features (ROLE_TEACHER)
✅ **Create** - Teachers can create new student profiles with login credentials  
✅ **Read** - View all students and individual student details  
✅ **Update** - Edit any student profile including roll number  
✅ **Delete** - Remove student profiles and associated user accounts  

### Student Features (ROLE_STUDENT)
✅ **View Profile** - Students can view their complete profile  
✅ **Update Profile** - Students can update their own information  
❌ **Cannot Change Roll Number** - Roll number is restricted (only teachers can modify)  
❌ **Cannot Create Profiles** - Students cannot create their own accounts  

## 🛠️ Technology Stack
- **Backend**: Spring Boot 4.0.2
- **Security**: Spring Security 6
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA (Hibernate)
- **Template Engine**: Thymeleaf
- **Build Tool**: Maven
- **Java Version**: 17

## 📁 Project Structure
```
src/main/java/com/example/student_teacher_management/
├── config/
│   ├── SecurityConfig.java          # Security configuration with role-based access
│   └── DataInitializer.java         # Creates default users on startup
├── controller/
│   ├── AuthController.java          # Login and dashboard routing
│   ├── TeacherController.java       # Teacher operations (CRUD)
│   └── StudentController.java       # Student profile operations
├── entity/
│   ├── User.java                    # User entity with roles
│   └── Student.java                 # Student profile entity
├── repository/
│   ├── UserRepository.java          # User data access
│   └── StudentRepository.java       # Student data access
├── service/
│   ├── CustomUserDetailsService.java # Spring Security user details
│   └── StudentService.java          # Business logic for student operations
└── dto/
    └── StudentDTO.java              # Data transfer object

src/main/resources/
├── templates/
│   ├── login.html                   # Login page
│   ├── teacher/
│   │   ├── students-list.html       # List all students
│   │   ├── student-form.html        # Create student form
│   │   ├── student-edit.html        # Edit student form
│   │   └── student-view.html        # View student details
│   └── student/
│       ├── profile.html             # Student profile view
│       └── profile-edit.html        # Student profile edit
└── application.properties           # Application configuration
```

## 🚀 Setup Instructions

### Prerequisites
- Java 17 or higher
- PostgreSQL database
- Maven
- IDE (IntelliJ IDEA recommended)

### Step 1: Database Setup
1. Install PostgreSQL if not already installed
2. Create a new database:
```sql
CREATE DATABASE student_management;
```

### Step 2: Configure Database Connection
Open `src/main/resources/application.properties` and update if needed:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/student_management
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### Step 3: Build and Run
Using Maven:
```bash
# Clean and build
mvn clean install

# Run the application
mvn spring-boot:run
```

Or using the Maven wrapper (Windows):
```bash
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

### Step 4: Access the Application
Open your browser and navigate to:
```
http://localhost:8080/login
```

## 👥 Default Login Credentials

The application creates default accounts on first startup:

### Teacher Account
- **Username**: `teacher`
- **Password**: `teacher123`
- **Access**: Full CRUD operations on all students

### Student Account (For Testing)
- **Username**: `student`
- **Password**: `student123`
- **Access**: View and edit own profile only

## 🔐 Security Implementation Details

### Authorization Rules
```java
/teacher/**    → Requires ROLE_TEACHER
/student/**    → Requires ROLE_STUDENT
/login         → Public access
/logout        → Authenticated users
```

### Key Security Features
1. **Password Encryption**: BCrypt hashing algorithm
2. **CSRF Protection**: Enabled by default
3. **Session Management**: Automatic session handling
4. **Method Security**: `@PreAuthorize` annotations on controllers
5. **Custom User Details**: Integration with database user store

### Authorization Demonstration
The project showcases authorization through:
- **Role-based URL protection**: Different users see different dashboards
- **Field-level restrictions**: Students cannot modify roll numbers
- **Operation restrictions**: Students cannot create/delete profiles
- **Data isolation**: Students can only access their own data

## 📝 API Endpoints

### Authentication
- `GET /login` - Login page
- `POST /login` - Process login
- `POST /logout` - Logout user
- `GET /dashboard` - Redirect based on role

### Teacher Endpoints (ROLE_TEACHER required)
- `GET /teacher/students` - List all students
- `GET /teacher/students/new` - Show create form
- `POST /teacher/students` - Create new student
- `GET /teacher/students/{id}/view` - View student details
- `GET /teacher/students/{id}/edit` - Show edit form
- `POST /teacher/students/{id}` - Update student
- `POST /teacher/students/{id}/delete` - Delete student

### Student Endpoints (ROLE_STUDENT required)
- `GET /student/profile` - View own profile
- `GET /student/profile/edit` - Show edit form
- `POST /student/profile` - Update own profile

## 🧪 Testing the Authorization

### Test Scenario 1: Teacher Creating Student
1. Login as `teacher` / `teacher123`
2. Click "Add New Student"
3. Fill in student details including username and password
4. Submit - Student account is created
5. **Verify**: Teacher can create student accounts ✅

### Test Scenario 2: Student Cannot Change Roll Number
1. Login as a student
2. Go to "Edit Profile"
3. **Observe**: Roll number field is disabled
4. Try to update other fields
5. **Verify**: Roll number remains unchanged ✅

### Test Scenario 3: Role-Based Access Control
1. Login as student
2. Try to access `/teacher/students` directly
3. **Result**: Access Denied (403 Forbidden) ✅

### Test Scenario 4: Complete CRUD Operations (Teacher)
1. Login as teacher
2. **Create**: Add a new student ✅
3. **Read**: View student list and details ✅
4. **Update**: Edit student information ✅
5. **Delete**: Remove student profile ✅

## 📊 Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL
);
```

### Students Table
```sql
CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    roll_number VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    course VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    address VARCHAR(500),
    user_id BIGINT UNIQUE REFERENCES users(id)
);
```

## 🎨 UI Features
- Modern, responsive design
- Gradient color scheme (purple/blue)
- Clean and intuitive interface
- Flash messages for success/error feedback
- Confirmation dialogs for delete operations
- Disabled fields for restricted access

## 📌 Important Notes for Demonstration

### What to Highlight to Your Teacher:

1. **Authentication System**
   - Secure login with encrypted passwords
   - Role-based user accounts (TEACHER, STUDENT)

2. **Authorization Implementation**
   - Teachers have full CRUD access
   - Students have restricted access (no create, no delete)
   - Students cannot modify their roll number
   - URL-based security protection

3. **Security Best Practices**
   - Password encryption (BCrypt)
   - CSRF protection
   - Session management
   - Method-level security annotations

4. **Clean Architecture**
   - Separation of concerns (Entity, Repository, Service, Controller)
   - DTO pattern for data transfer
   - Service layer for business logic

## 🐛 Troubleshooting

### Issue: Cannot connect to database
**Solution**: Make sure PostgreSQL is running and credentials are correct

### Issue: Port 8080 already in use
**Solution**: Change port in `application.properties`:
```properties
server.port=8081
```

### Issue: Login not working
**Solution**: Check if database tables are created. Enable SQL logging:
```properties
spring.jpa.show-sql=true
```

## 🎓 Learning Outcomes
This project demonstrates:
- ✅ Spring Security configuration
- ✅ Role-based authorization
- ✅ CRUD operations
- ✅ Form-based authentication
- ✅ Password encryption
- ✅ RESTful endpoint design
- ✅ MVC architecture
- ✅ Database relationships
- ✅ Thymeleaf templating

## 📞 Support
If you encounter any issues, check:
1. Database connection settings
2. PostgreSQL service is running
3. Correct Java version (17+)
4. All dependencies are downloaded

---

**Good luck with your demonstration! Your teacher will be impressed! 🌟**
=======
# Software_Project
>>>>>>> ee9151923adf80f74477bfb331404799a3316aaa
