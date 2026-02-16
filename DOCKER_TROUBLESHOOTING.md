# Docker Troubleshooting Guide

## ❌ Error: "The system cannot find the file specified" (dockerDesktopLinuxEngine)

### Problem:
```
unable to get image 'student_teacher_management-app': error during connect: 
Get "http://%2F%2F.%2Fpipe%2FdockerDesktopLinuxEngine/v1.51/images/...": 
open //./pipe/dockerDesktopLinuxEngine: The system cannot find the file specified.
```

### Root Cause:
**Docker Desktop is not running** on your Windows machine.

### ✅ Solution:

#### Step 1: Start Docker Desktop
1. **Press** `Windows Key` and search for "**Docker Desktop**"
2. **Click** to launch Docker Desktop
3. **Wait** for Docker to fully start (2-3 minutes)
4. **Look for** the Docker icon in system tray (bottom-right)
5. **Verify** the icon shows "Docker Desktop is running"

#### Step 2: Verify Docker is Running
Open PowerShell and run:
```powershell
docker --version
docker ps
```

Expected output:
```
Docker version 24.x.x, build xxxxxxx
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
```

#### Step 3: Run Your Application
```powershell
docker compose up --build
```

---

## ❌ Error: Docker Desktop Not Installed

### Solution:
1. Download Docker Desktop for Windows: https://www.docker.com/products/docker-desktop/
2. Install Docker Desktop
3. Restart your computer
4. Launch Docker Desktop
5. Run `docker --version` to verify installation

---

## ❌ Error: "version is obsolete" Warning

### Problem:
```
level=warning msg="docker-compose.yml: the attribute `version` is obsolete"
```

### Solution:
✅ **Already Fixed!** The `version: '3.8'` line has been removed from `docker-compose.yml`.

Modern Docker Compose doesn't need the version field.

---

## ❌ Error: Port Already in Use

### Problem:
```
Error starting userland proxy: listen tcp4 0.0.0.0:8080: bind: 
Only one usage of each socket address is normally permitted.
```

### Solution Option 1: Stop Conflicting Service
```powershell
# Check what's using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID with actual number)
taskkill /PID <PID> /F
```

### Solution Option 2: Change Port in docker-compose.yml
Edit `docker-compose.yml`:
```yaml
app:
  ports:
    - "8081:8080"  # Changed from 8080:8080
```

Then access at: http://localhost:8081

---

## ❌ Error: Database Connection Failed

### Problem:
```
Unable to open JDBC Connection for DDL execution
```

### Solutions:

#### 1. Database Container Not Ready
Wait longer for database to initialize (first run takes time).

#### 2. Check Container Status
```powershell
docker compose ps
```

Both containers should show "running" status.

#### 3. Check Database Logs
```powershell
docker compose logs db
```

#### 4. Restart Containers
```powershell
docker compose down
docker compose up
```

---

## ❌ Error: Build Failed / Maven Errors

### Problem:
```
Failed to execute goal org.apache.maven.plugins...
```

### Solutions:

#### 1. Clean Maven Cache
```powershell
docker compose down
docker compose build --no-cache
docker compose up
```

#### 2. Check Internet Connection
Maven downloads dependencies from internet. Ensure stable connection.

#### 3. Check pom.xml
Ensure `pom.xml` has no syntax errors.

---

## ❌ Error: Out of Disk Space

### Problem:
```
no space left on device
```

### Solution:
Clean up Docker resources:
```powershell
# Remove unused containers, networks, images
docker system prune -a

# Remove all volumes (⚠️ deletes data)
docker volume prune
```

---

## ❌ Error: WSL 2 Installation Required

### Problem:
```
WSL 2 installation is incomplete
```

### Solution:
1. Open PowerShell as Administrator
2. Run:
   ```powershell
   wsl --install
   ```
3. Restart computer
4. Launch Docker Desktop

---

## 🔍 Diagnostic Commands

### Check Docker Status
```powershell
# Docker version
docker --version
docker compose version

# Running containers
docker ps

# All containers (including stopped)
docker ps -a

# Docker system info
docker info

# Check Docker Desktop status
Get-Service "com.docker.service"
```

### Check Application Logs
```powershell
# All logs
docker compose logs

# App logs only
docker compose logs app

# Database logs only
docker compose logs db

# Follow logs (live updates)
docker compose logs -f app
```

### Check Network Connectivity
```powershell
# List networks
docker network ls

# Inspect network
docker network inspect student_teacher_management_stm-network

# Test database connection from app container
docker exec stm-app ping db
```

---

## 🛠️ Common Fix-All Commands

### Complete Reset (Nuclear Option)
```powershell
# Stop and remove everything
docker compose down -v

# Remove all Docker resources
docker system prune -a -f

# Rebuild and start fresh
docker compose up --build
```

### Restart Docker Desktop
1. Right-click Docker icon in system tray
2. Click "Quit Docker Desktop"
3. Wait 10 seconds
4. Launch Docker Desktop again
5. Wait for it to fully start
6. Run `docker compose up --build`

---

## 📊 Understanding Container States

| State | Meaning | What to Do |
|-------|---------|------------|
| **Up** | Container running normally | ✅ Good! |
| **Restarting** | Container keeps crashing | Check logs: `docker compose logs` |
| **Exited** | Container stopped | Check exit code, restart |
| **Created** | Container created but not started | Run `docker compose start` |

---

## 🚨 Emergency Steps

If nothing works, try this sequence:

1. **Stop Everything**
   ```powershell
   docker compose down -v
   ```

2. **Quit Docker Desktop**
   - Right-click system tray icon
   - Select "Quit Docker Desktop"

3. **Wait 30 Seconds**

4. **Start Docker Desktop**
   - Launch from Start menu
   - Wait until icon shows "running"

5. **Verify Docker Works**
   ```powershell
   docker run hello-world
   ```

6. **Start Your Application**
   ```powershell
   docker compose up --build
   ```

---

## 💡 Pro Tips

### Speed Up Builds
```powershell
# Use BuildKit (faster builds)
$env:DOCKER_BUILDKIT=1
docker compose up --build
```

### Run in Background
```powershell
docker compose up -d --build
```

### Watch Logs
```powershell
docker compose logs -f --tail=100 app
```

### Access Container Shell
```powershell
# App container
docker exec -it stm-app bash

# Database container
docker exec -it stm-postgres psql -U postgres
```

---

## 📞 Still Having Issues?

1. **Check Docker Desktop Logs**
   - Docker Desktop → Troubleshoot → View Logs

2. **Check System Requirements**
   - Windows 10/11 64-bit: Pro, Enterprise, or Education
   - Hyper-V and WSL 2 enabled
   - Virtualization enabled in BIOS

3. **Restart Computer**
   - Sometimes a simple restart fixes everything

4. **Reinstall Docker Desktop**
   - Uninstall Docker Desktop
   - Restart computer
   - Install latest version from docker.com
   - Restart computer again

---

## ✅ Verification Checklist

Before running `docker compose up`:

- [ ] Docker Desktop is installed
- [ ] Docker Desktop is running (check system tray icon)
- [ ] `docker --version` works
- [ ] `docker ps` works (shows empty list or running containers)
- [ ] You're in the project directory
- [ ] `docker-compose.yml` file exists
- [ ] Internet connection is active

---

**If you've followed these steps and still have issues, the problem might be with your Docker Desktop installation or Windows configuration.**
