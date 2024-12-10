# NutriFit

NutriFit is a personalized nutrition management app designed to simplify calorie tracking, meal planning, and healthy eating habits.

---

## Features
- Personalized calorie recommendations based on individual weight and goals.
- Meal suggestions tailored to calorie deficit plans.
- Easy tracking of daily food intake and nutrients.

---

## Documentation 
1. Create ML model
2. Convert ML model to tflite
3. Create database for user to register and login
4. Create android project using android studio
5. Import library that we needed
6. Designing user interface
7. Connecting firebase from cloud into Android Studio for Login and Register user
8. Deploy the ML model directly into Android Studio ( model.tflite )
9. Deploy some assets into Android Studio ( resep.csv )
10. Using tflite library to get the data clustering and resep.csv to get the output from model

## Android

Versions Used :

- Android Studio : 2024.1.2
- Kotlin : 1.9.0
- Target SDK : 34
- Min SDK : 24

How to run in your own pc 
1. Clone this branch using this command :
   ```bash
   git clone https://github.com/youngdude/NutriFit.git
2. Open in android studio
3. Sync project with gradle files (under file tab in Android Studio)

## Machine Learning

System Requirement :

- Working on : Google Colab
- Tensorflow : 2.5.0
- Dataset : https://drive.google.com/drive/folders/14qqUuGtCGk-ZR1rfKeEuGMr23nAm5hSd?usp=drive_link

The steps to create an ML model
1. Check version tensorflow
2. Import all required library
3. Download Dataset
4. Split Dataset
5. Accuracy, loss, and score of model
6. Visualize the plot
7. Convert to TFlite

## How to Replicate This Project

### Prerequisites
- Install Python 3.9 or newer.
- Ensure the following libraries are installed:
  - TensorFlow
  - Pandas
  - Scikit-learn
  - Matplotlib
  - IPython
  - TensorFlow.js (for deploying models)

### Steps to Replicate

1. **Clone the Repository**  
   ```bash
   git clone https://github.com/youngdude/NutriFit
