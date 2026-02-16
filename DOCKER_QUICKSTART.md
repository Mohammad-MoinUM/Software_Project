# 🚀 Quick Start with Docker - MUST READ FIRST!

## ⚠️ IMPORTANT: Before Running Docker Commands

### Step 1: Ensure Docker Desktop is Installed
- If not installed, download from: https://www.docker.com/products/docker-desktop/
- Install and restart your computer

### Step 2: **START DOCKER DESKTOP** ⭐ (CRITICAL!)

**This is the most common mistake - you MUST start Docker Desktop first!**

1. Press `Windows Key` and type "**Docker Desktop**"
2. Click on "Docker Desktop" to launch it
3. **WAIT 2-3 minutes** for Docker to fully start
4. Look for the Docker icon in the **system tray** (bottom-right corner of screen)
5. The icon should show: "**Docker Desktop is running**"

![System Tray Icon Location]
```
┌────────────────────────────────┐
│  Your Desktop                  │
│                                │
│                                │
│                          [🐳]←─┼─ Docker icon here (green/running)
└────────────────────────────────┘
     Bottom-right corner
```

### Step 3: Verify Docker is Running

Open PowerShell and run:
```powershell
docker --version
```

Expected output:
```
Docker version 24.x.x, build xxxxxxx
```

✅ If you see the version, Docker is running!
❌ If you see an error, Docker Desktop is not running (go back to Step 2)

---

## 🎯 Now Run Your Application

### Option 1: Double-Click Method (Easiest)
1. Find **`RUN_DOCKER.bat`** in your project folder
2. **Double-click** it
3. Wait for containers to start (first time: 3-5 minutes)
4. Open browser to: http://localhost:8080

### Option 2: PowerShell Method
```powershell
cd C:\Users\moinu\Desktop\Software\Student_Teacher_Management
docker compose up --build
```

---

## 🔑 Login Credentials

**Teacher:**
- Email: `teacher@example.com`
- Password: `teacher123`

**Student:**
- Email: `student@example.com`
- Password: `student123`

---

## 🛑 How to Stop

Press `Ctrl + C` in the terminal/command window

Or run:
```powershell
docker compose down
```

---

## ❌ Troubleshooting

### Error: "The system cannot find the file specified"
**Cause:** Docker Desktop is not running

**Solution:** 
1. Start Docker Desktop (see Step 2 above)
2. Wait until it's fully running
3. Try again

### Error: "Port already in use"
**Cause:** Another application is using port 8080

**Solution Option 1:** Stop the other application

**Solution Option 2:** Change port in `docker-compose.yml`:
```yaml
app:
  ports:
    - "8081:8080"  # Use port 8081 instead
```

Then access at: http://localhost:8081

### Need More Help?
See **`DOCKER_TROUBLESHOOTING.md`** for comprehensive troubleshooting guide.

---

## 📊 What Happens When You Run Docker Compose?

1. **Downloads PostgreSQL image** (if not already downloaded)
2. **Builds your Spring Boot application** from source code
3. **Starts PostgreSQL database** container
4. **Starts your application** container
5. **Connects everything together** via Docker network
6. **Ready to use** at http://localhost:8080

---

## 💡 Quick Tips

### Run in Background (Detached Mode)
```powershell
docker compose up -d --build
```

### View Logs
```powershell
docker compose logs -f app
```

### Stop Containers
```powershell
docker compose down
```

### Reset Everything (Fresh Start)
```powershell
docker compose down -v
docker compose up --build
```

---

## ✅ Success Indicators

You'll know it's working when you see:
```
stm-postgres | database system is ready to accept connections
stm-app      | Started StudentTeacherManagementApplication in X.XXX seconds
```

Then open: **http://localhost:8080** 🎉

---

## 🎓 For Your Presentation

### Key Points:
1. **"Docker provides consistent environment"** - Works the same everywhere
2. **"One-command deployment"** - `docker compose up` starts everything
3. **"No manual setup needed"** - Database automatically configured
4. **"Production-ready"** - Same setup for dev and production
5. **"Easy to scale"** - Can add more services easily

---

## 📚 Next Steps

1. ✅ Start Docker Desktop
2. ✅ Run `docker compose up --build`
3. ✅ Access http://localhost:8080
4. ✅ Test all features
5. ✅ Read `DOCKER_README.md` for detailed commands
6. ✅ Check `DOCKER_TROUBLESHOOTING.md` if you have issues

---

**Remember: Always start Docker Desktop FIRST before running any Docker commands!** 🐳
