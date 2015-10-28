# DroneTrackerStudio
by Stephen Cronin, Blair Cutting, Adam Joseph, Devin Platte, and Alex Walts



In order to run the DroneTrackerStudio app on an emulator via Android Studio for the first time, the following instructions are provided. 


Note: If you have an Android Device you would like to test the app on instead of the emulator, this is highly recommended. In this case, you may skip Section A.



A| Preparing Windows:

In Windows, ensure you have the ability to run the Android emulator hardware virtualization

1) Open the Windows Control Panel

2) Select Turn Windows features on or off

3) Uncheck Hyper-V



B| Preparing Android Studio:

In Android Studio, ensure you have the correct SDK Platforms and SDK Tools

1) Open the settings menu via File>Settings

2) In the Naviagion Pane, navigate to Appereance & Behavior > System Settings > Adroid SDK

3) In the SDK Platforms tab, ensure Android 6.0 is checked

4) In the SDK Tool" tab, ensure the following items are checked:

	Android SDK Build Tools
	
	Android SDK Platform-Tools 23.1 rc1
	
	Documetnation for Android SDK
	
	Google Play services, rev 27
	
	Google Repository, rev 22
	
	Intel x86 Emulator Accelerator (HAXM installer), rev 55
	
5) Select OK



C| Loading and Preparing the Emulator:


In Android Studio, launch the emulator and configure the emulator permissions (if you wish to load the app onto your Android device, use the Choose a running device option and select the desired device)

1) Open the MainActivity.java file

2) Select Run

3) In the Device Chooser dialog, select Launch Emulator

4) Choose an Android Virtual device (Nexus 5 API 23 x86 was used for testing)

5) Select Okay

6) If the app immediatly launches, exit the app and return to the home screen

7) In the emulator, navigate to Settings>Apps>DroneTrackerStudio>Permissions

8) Allow the following permissions:

	Camera
	
	Location
	
	Storage
	
9) Return to the home screen



D| Running the App


In the emulator, run and test the app

1) Navigate to the app list via the emulator, and select DroneTrackerStudio

2) To capture an image, press the capture button on the image preview pane (note: this image is generated via the emulator's virtual camera)

3) Enter a description of the event. Any details you wish to include are recommended, but not required; this includes location.

4) Press the Email to Police button to send the email. This email will contain the picture you caputured, description you entered, along with an automatically gathered GPS location


E| Running the Database

1) The database should be run in eclipse - the name of the file is EmailSniffer.java

2) The required libraries to run it are:
	-JavaMail
	-org.apache.commons.lang3.StringUtils;
	-javaxt

	-These have been included in the submission and must be added to the eclipse build path in the folder DroneTrackerStudio/app/EclipseLibraries

3) The program is run simply with the run button.

4) All images attached to the emails send shall be stored in the folder layer one above where the java document is.

5) The database file shall be in the same layer as the java file and will store all information from a single email in a tab separated row of the file

