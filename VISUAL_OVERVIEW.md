# 🎓 Student Management System - Visual Overview

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                         USER LOGIN                          │
│                    http://localhost:8080                    │
└──────────────────┬──────────────────────────────────────────┘
                   │
                   ▼
        ┌──────────────────────┐
        │  Spring Security     │
        │  Authentication      │
        └──────────┬───────────┘
                   │
         ┌─────────┴─────────┐
         │                   │
         ▼                   ▼
   ┌──────────┐        ┌──────────┐
   │ TEACHER  │        │ STUDENT  │
   │  ROLE    │        │   ROLE   │
   └────┬─────┘        └────┬─────┘
        │                   │
        ▼                   ▼
┌──────────────┐    ┌──────────────┐
│   Teacher    │    │   Student    │
│  Dashboard   │    │   Profile    │
└──────────────┘    └──────────────┘
        │                   │
        ▼                   ▼
┌──────────────┐    ┌──────────────┐
│ CRUD on ALL  │    │ View/Edit    │
│  Students    │    │  OWN Only    │
└──────────────┘    └──────────────┘
```

---

## 🔐 Authentication & Authorization Flow

### Login Process
```
User enters credentials
        ↓
Spring Security validates
        ↓
Check username in database
        ↓
Verify BCrypt password
        ↓
Check role (TEACHER/STUDENT)
        ↓
Create session
        ↓
Redirect to dashboard
```

### Authorization Check
```
User tries to access URL
        ↓
Spring Security intercepts
        ↓
Check if authenticated
        ↓
Check user's role
        ↓
Match against required role
        ↓
Grant/Deny access
```

---

## 👥 User Roles & Permissions

### 🎓 TEACHER Role
```
CREATE Student ✅
  ├── Add student profile
  ├── Create login credentials
  └── Set roll number

READ Students ✅
  ├── View all students list
  └── View individual details

UPDATE Student ✅
  ├── Edit any field
  ├── Change roll number ✅
  └── Modify all information

DELETE Student ✅
  └── Remove student & account
```

### 📚 STUDENT Role
```
CREATE ❌
  └── Cannot create own profile

READ ✅
  ├── View own profile
  └── Cannot view others

UPDATE ✅ (Limited)
  ├── Edit own information
  ├── Cannot change roll ❌
  └── Update name, email, etc.

DELETE ❌
  └── Cannot delete anything
```

---

## 📊 Database Schema

```
┌─────────────────┐         ┌─────────────────┐
│     USERS       │         │    STUDENTS     │
├─────────────────┤         ├─────────────────┤
│ id (PK)         │◄───────┤ id (PK)         │
│ username        │    │    │ name            │
│ password        │    │    │ roll_number     │
│ role            │    └────│ user_id (FK)    │
│ enabled         │         │ email           │
└─────────────────┘         │ course          │
                            │ phone_number    │
                            │ address         │
                            └─────────────────┘
```

---

## 🎯 Key Features Matrix

| Feature                    | Teacher | Student |
|---------------------------|---------|---------|
| Login                     | ✅      | ✅      |
| View All Students         | ✅      | ❌      |
| View Own Profile          | N/A     | ✅      |
| Create Student Account    | ✅      | ❌      |
| Edit Student Profile      | ✅      | ❌      |
| Edit Own Profile          | N/A     | ✅      |
| Change Roll Number        | ✅      | ❌      |
| Delete Student            | ✅      | ❌      |
| Access /teacher/**        | ✅      | ❌      |
| Access /student/**        | ❌      | ✅      |

---

## 🔒 Security Layers

```
Layer 1: URL Protection
├── /teacher/** → ROLE_TEACHER only
├── /student/** → ROLE_STUDENT only
└── /login → Public

Layer 2: Method Security
├── @PreAuthorize("hasRole('TEACHER')")
└── @PreAuthorize("hasRole('STUDENT')")

Layer 3: Business Logic
├── StudentService checks isTeacher flag
└── Conditional field updates

Layer 4: UI Restrictions
├── Disabled fields for students
└── Hidden buttons based on role

Layer 5: Data Encryption
└── BCrypt password hashing
```

---

## 🛠️ Technology Stack Diagram

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│  ┌──────────────────────────────────┐  │
│  │        Thymeleaf HTML             │  │
│  │  (login, dashboards, forms)       │  │
│  └──────────────────────────────────┘  │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│          Controller Layer               │
│  ┌──────────────────────────────────┐  │
│  │  AuthController                   │  │
│  │  TeacherController                │  │
│  │  StudentController                │  │
│  └──────────────────────────────────┘  │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│          Service Layer                  │
│  ┌──────────────────────────────────┐  │
│  │  StudentService                   │  │
│  │  CustomUserDetailsService         │  │
│  └──────────────────────────────────┘  │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│        Repository Layer                 │
│  ┌──────────────────────────────────┐  │
│  │  UserRepository                   │  │
│  │  StudentRepository                │  │
│  └──────────────────────────────────┘  │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│          Database Layer                 │
│  ┌──────────────────────────────────┐  │
│  │  PostgreSQL / H2                  │  │
│  │  (users, students tables)         │  │
│  └──────────────────────────────────┘  │
└─────────────────────────────────────────┘
```

---

## 📱 User Journey Map

### Teacher Journey
```
1. Login with teacher credentials
        ↓
2. See students list (empty or with data)
        ↓
3. Click "Add New Student"
        ↓
4. Fill form with student details
        ↓
5. Submit → Student created
        ↓
6. View/Edit/Delete as needed
        ↓
7. Logout
```

### Student Journey
```
1. Login with student credentials
        ↓
2. Redirected to profile page
        ↓
3. View own information
        ↓
4. Click "Edit Profile"
        ↓
5. Update allowed fields (roll number disabled)
        ↓
6. Save changes
        ↓
7. Logout
```

---

## 🔄 CRUD Operations Flow

### CREATE (Teacher Only)
```
Teacher clicks "Add Student"
        ↓
Fill form with:
 - Name, Roll, Email
 - Course, Phone, Address
 - Username, Password
        ↓
Submit form
        ↓
StudentService validates
        ↓
Create User account
        ↓
Create Student profile
        ↓
Link Student to User
        ↓
Save to database
        ↓
Redirect to students list
```

### READ
```
Teacher → View all students
Student → View own profile
        ↓
Repository queries database
        ↓
Service returns data
        ↓
Controller passes to view
        ↓
Thymeleaf renders HTML
```

### UPDATE
```
User clicks "Edit"
        ↓
Load current data
        ↓
Show form (roll disabled for student)
        ↓
User modifies fields
        ↓
Submit changes
        ↓
Service validates + checks role
        ↓
Update database (conditional)
        ↓
Redirect with success message
```

### DELETE (Teacher Only)
```
Teacher clicks "Delete"
        ↓
Confirm dialog
        ↓
Teacher confirms
        ↓
Service finds student
        ↓
Delete student record
        ↓
Delete associated user
        ↓
Redirect with success message
```

---

## 🎨 UI Pages Structure

```
├── login.html
│   ├── Username field
│   ├── Password field
│   └── Login button
│
├── teacher/
│   ├── students-list.html
│   │   ├── Table of all students
│   │   ├── View/Edit/Delete buttons
│   │   └── "Add Student" button
│   │
│   ├── student-form.html
│   │   └── Create student form
│   │
│   ├── student-edit.html
│   │   └── Edit student form
│   │
│   └── student-view.html
│       └── Student details display
│
└── student/
    ├── profile.html
    │   ├── Display all info
    │   └── "Edit Profile" button
    │
    └── profile-edit.html
        └── Edit form (roll disabled)
```

---

## ⚡ Quick Commands

### Start Application
```bash
.\mvnw.cmd spring-boot:run
```

### Build Project
```bash
.\mvnw.cmd clean install
```

### Run Tests
```bash
.\mvnw.cmd test
```

---

## 🎯 Demo Script (5 Minutes)

```
[0:00 - 0:30] Introduction
├── Show login page
└── Explain two roles

[0:30 - 2:30] Teacher Demo
├── Login as teacher
├── Create a student
├── View students list
├── Edit student (show roll change)
└── Optional: Delete

[2:30 - 4:30] Student Demo
├── Login as student
├── View profile
├── Click edit
├── Show roll disabled
└── Update other field

[4:30 - 5:00] Security Demo
├── Try teacher URL as student
└── Show access denied
```

---

## 📞 Default Credentials

```
┌─────────────────────────────────────┐
│      TEACHER ACCOUNT                │
├─────────────────────────────────────┤
│  Username: teacher                  │
│  Password: teacher123               │
│  Role:     ROLE_TEACHER             │
│  Access:   Full CRUD                │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│      STUDENT ACCOUNT                │
├─────────────────────────────────────┤
│  Username: student                  │
│  Password: student123               │
│  Role:     ROLE_STUDENT             │
│  Access:   View/Edit own profile    │
└─────────────────────────────────────┘
```

---

## ✅ Project Status

```
Code:           ✅ COMPLETE
Build:          ✅ SUCCESS
Tests:          ✅ VERIFIED
Documentation:  ✅ COMPREHENSIVE
Demo Ready:     ✅ YES
```

---

## 🌟 Success Metrics

| Metric                          | Status |
|--------------------------------|--------|
| Authentication Working          | ✅     |
| Authorization Implemented       | ✅     |
| CRUD Operations Complete        | ✅     |
| Role-based Access Control       | ✅     |
| Security Best Practices         | ✅     |
| Clean Code Architecture         | ✅     |
| Documentation Complete          | ✅     |
| Build Success                   | ✅     |
| Ready for Demonstration         | ✅     |

---

**🎉 PROJECT READY! GOOD LUCK! 🚀**
