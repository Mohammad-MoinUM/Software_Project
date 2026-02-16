# 🚀 Quick Start Guide

## Prerequisites Check
- [ ] Java 17 installed
- [ ] PostgreSQL installed and running
- [ ] IDE ready (IntelliJ IDEA recommended)

## Setup in 5 Minutes ⚡

### Step 1: Create Database (1 minute)
Open PostgreSQL command line or pgAdmin and run:
```sql
CREATE DATABASE student_management;
```

### Step 2: Update Database Password (30 seconds)
If your PostgreSQL password is different from `postgres`, update this file:
`src/main/resources/application.properties`

Change this line:
```properties
spring.datasource.password=YOUR_PASSWORD_HERE
```

### Step 3: Run the Application (2 minutes)
Open terminal in project folder and run:

**Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

Wait for the message: `Started StudentTeacherManagementApplication`

### Step 4: Open Browser (30 seconds)
Navigate to: **http://localhost:8080/login**

### Step 5: Test the System (2 minutes)

#### Login as Teacher (Full Access)
```
Username: teacher
Password: teacher123
```

**Try these:**
1. Click "Add New Student" → Create a student profile
2. View the student list
3. Edit a student (notice you CAN change roll number)
4. Delete a student

#### Login as Student (Limited Access)
```
Username: student
Password: student123
```

**Try these:**
1. View your profile
2. Click "Edit Profile"
3. Notice: Roll number is DISABLED (cannot change)
4. Update other fields → Save successfully

---

## 🎯 What to Show Your Teacher

### 1. Authentication (Login System)
- Show the login page
- Demonstrate different user types (teacher vs student)
- Explain password encryption (BCrypt)

### 2. Authorization - Teacher Role
- Login as teacher
- Show full CRUD operations:
  - **Create**: Add new student with credentials
  - **Read**: View all students
  - **Update**: Edit any field including roll number
  - **Delete**: Remove student profile

### 3. Authorization - Student Role
- Login as student
- Show restricted access:
  - Can view own profile ✅
  - Can edit own profile ✅
  - **CANNOT** change roll number ❌
  - **CANNOT** access teacher pages ❌
  - **CANNOT** create own profile ❌

### 4. Security Features to Highlight
- Role-based access control (RBAC)
- URL protection (try accessing /teacher/students as student)
- Field-level restrictions (roll number disabled)
- Password encryption
- Session management (logout works properly)

---

## 📋 Demo Script for Teacher

**"Good morning/afternoon Sir/Madam, let me demonstrate the Student Management System:"**

### Part 1: System Overview (30 seconds)
*"This is a Student Management System with authentication and authorization. It has two roles: TEACHER and STUDENT, each with different permissions."*

### Part 2: Teacher Demonstration (2 minutes)
*"First, I'll login as a teacher..."*

1. Login: `teacher / teacher123`
2. *"As you can see, teachers have full access to student management."*
3. Create a student: Click "Add New Student"
   - Fill: Name: "John Doe", Roll: "CS101", Email: "john@test.com", etc.
   - Username: "john", Password: "john123"
   - *"Notice teachers create the student accounts, students cannot self-register."*
4. Show the list: *"Here's all students in the system"*
5. Edit student: Click Edit on a student
   - *"Teachers can modify everything including roll number"*
6. View details: Click View
7. Optional: Delete a student

### Part 3: Student Demonstration (2 minutes)
*"Now let me logout and login as a student..."*

1. Logout → Login: `student / student123`
2. *"Students are automatically redirected to their profile page"*
3. *"They can view their information"*
4. Click "Edit Profile"
   - *"Notice the roll number field is disabled - students cannot change it"*
   - Update name or phone number
   - Save successfully
   - *"This demonstrates field-level authorization"*

### Part 4: Security Testing (1 minute)
*"Let me demonstrate the security..."*

1. While logged in as student, try to access:
   `http://localhost:8080/teacher/students`
2. *"See? Access denied. The system protects teacher-only pages."*
3. Show logout: *"Session management works properly"*

### Part 5: Code Walkthrough (Optional - if time permits)
*"Let me show the code structure..."*

1. Open SecurityConfig.java
   - *"Here's where we define role-based access rules"*
2. Open TeacherController.java
   - *"The @PreAuthorize annotation ensures only teachers can access"*
3. Open StudentService.java
   - *"The updateStudent method has an isTeacher flag that controls roll number updates"*

---

## 🎓 Key Points to Mention

### Authentication Features:
- ✅ Secure login system
- ✅ Password encryption (BCrypt)
- ✅ Multiple user roles
- ✅ Session management

### Authorization Features:
- ✅ Role-based access control (TEACHER vs STUDENT)
- ✅ URL-level protection
- ✅ Method-level security
- ✅ Field-level restrictions
- ✅ Operation-level restrictions

### Business Logic:
- ✅ Teachers create student accounts
- ✅ Students cannot self-register
- ✅ Students can update profile except roll number
- ✅ Complete CRUD for teachers
- ✅ Read + Limited Update for students

---

## ⚠️ Common Issues & Quick Fixes

### "Cannot connect to database"
```bash
# Check PostgreSQL is running
# Windows: Check Services
# Mac/Linux: sudo systemctl status postgresql
```

### "Port 8080 already in use"
Add to application.properties:
```properties
server.port=8081
```

### "Login doesn't work"
Check database tables are created:
```sql
SELECT * FROM users;
SELECT * FROM students;
```

---

## 📱 Contact Info for Demo Day
- Make sure laptop is charged ⚡
- Have internet connection (if needed) 🌐
- Keep PostgreSQL running 🗄️
- Have backup screenshots 📸

**You got this! Good luck! 🌟**
