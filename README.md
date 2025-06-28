# MileageTracker
Mileage Tracker is an Android application designed to accurately track the distance users travel during a journey. It features start/stop journey functionality, real-time and background location tracking, and saves journey data locally—even if the app is killed or the device reboots. The app provides detailed summaries including distance, duration, and route map, and ensures reliability with battery and permission handling for real-world usage.

## Guide

### Grant permissions
1. Open the app.
2. Enter Journey Name.
3. Tap **Get Started**.
4. Grant required location permissions if prompted.

<img src="https://github.com/user-attachments/assets/72eae069-ab8d-4a04-baba-5d8ac5b12bce" width="350" />


### Start & Stop Journey
1. Tap **Start Journey** on the home screen.
3. A foreground service starts and tracks your movement even in the background or if the app is swiped away.
4. Tap **Stop** to end tracking and view a summary with:
   - Route preview on map
   - Start & End Time
   - Total Distance

<img src="https://github.com/user-attachments/assets/e8a8ada3-6cad-4a41-b810-f5ad2c820a0c" width="350" />

### After App is Killed

1. Tap the **persistent notification** to reopen the app (if it was killed or removed from recent apps).
2. The app resumes tracking seamlessly in the background.
3. Tap **Stop** to end tracking.
4. A summary screen will appear with:
   - Route preview on map
   - Start & End Time
   - Total Distance
     
<img src="https://github.com/user-attachments/assets/e8d3fc86-b3b7-4b34-8e61-4636df7ea711" width="350" />

### View Past Journeys
- Tap the **Past Journeys** section.
- Select any journey to view full details and route on map.
  
<img src="https://github.com/user-attachments/assets/47e2a97a-6f17-4d99-b134-9c77fe858647" width="350" />

### Delete Past Journey
- Click on Delete icon to delete the journey details.
  
<img src="https://github.com/user-attachments/assets/34e25137-730d-407f-b950-183f6071aad4" width="350" />
