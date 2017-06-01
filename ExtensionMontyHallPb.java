/*
 * File: ExtensionMontyHallPb.java
 * ------------------
 * Name: Silvia Fernandez 
 * This program implements a game of the Monty Hall Problem.
 * In this game a door is chosen randomly out of 3 to hold a car. The other 2 doors will 
 * hold a goat.
 * The user chooses one of 3 doors and will win what is behind that door. 
 * After the player chooses a door, the user is shown a goat that is behind one of the
 * remaining doors, and is asked if they want to keep their door choice. If they do, the game 
 * updates the user choice automatically. The game proceeds to check for a win - at the end of the game the
 * 3 doors are revealed and compared to the user's choice.
 * Communication is done with user via the console and game status/progress is illustrated in the canvas.
 * 
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.applet.*;
import java.awt.event.*;
import javax.swing.*;

public class ExtensionMontyHallPb extends ConsoleProgram {

	AudioClip goat = MediaTools.loadAudioClip("goat.au");
	AudioClip car = MediaTools.loadAudioClip("car.au");

	private static final int PAUSE_TIME = 5000;
	private static final double SCALING_IMAGES_Y = 0.6;
	private static final double SCALING_IMAGES_X = 0.9;
	private static final String DOORCHOSEN_ONE = "doorchosenone.png";
	private static final String DOORS_NOCHOICE = "doorsnochoice.png";
	private static final String DOORCHOSEN_TWO = "doorchosentwo.png";
	private static final String DOORCHOSEN_THREE = "doorchosenthree.png";
	private static final int N_ROWS = 3;
	private static final int N_COLS = 3;


	private RandomGenerator rg = new RandomGenerator();

	
	private GCanvas canvas = new GCanvas();
	private int doorChosen;
	private int secondDoorChosen;
	private int doorGoatReveal = -1;
	private int[][] gameStatus = new int[N_ROWS][N_COLS];
	private int doorwithCar;


	/***********************************************************
	 *                    Methods                              *
	 ***********************************************************/

	public void init() {
		add(canvas);

	}



	public void run() {
		drawCanvas();
		initializeConsole();
		drawCanvas();
		println("To help you win this game, am going to reveal what is behind one of the two other doors for you.");
		println("Ready?");
		pause(PAUSE_TIME);
		revealdoorwithGoat();
		changeDoor();
		checkforWin();
		finalCanvasUpdate();
		//simulateuserChoice();
		//displayConclusionfromSimulation();

	}

	private void initializeConsole() {
		doorwithCar = rg.nextInt(3);
		gameStatus[1][doorwithCar] = 1;
		//int temp = doorwithCar +1;
		//println("The car is hidden behind door number" + temp);
		println("Welcome to the MONTY HALL PROBLEM GAME!");
		println("In this game, you need to choose one of 3 identical doors.");
		println("You will win whatever is behind the door of your choice.");
		println("Behind each door, there is either a car or a goat. There is only one door with a car behind it.");
		doorChosen = readInt("Choose your door! ");
		while (doorChosen !=1 && doorChosen !=2 && doorChosen !=3) {
			println("Wrong door choice. Please press one of the buttons below to select a door number between 1 and 3.");
			doorChosen = readInt("Choose your door! ");
		}
		gameStatus[0][doorChosen -1] = 1;
		println("You've chosen door number " +doorChosen+ ".");
	}

	private void revealdoorwithGoat() {
		//displayCountDownSplashScreen();
		int goatShown = -1;
		for (int c = 0; c < gameStatus[0].length; c++) {
			if (gameStatus[0][c] != 1 && gameStatus[1][c] !=1) {
				gameStatus[2][c] = 1;
				goatShown = c;
			}
		}
		doorGoatReveal = goatShown;
		showGoatinCanvas(goatShown);
	}
	private void checkforWin() {
		println("Now let's see what you've won! Ready?");
		pause(PAUSE_TIME);
		if (secondDoorChosen == doorwithCar +1) {
			println("You won the car! The car was behind door number "+ secondDoorChosen + ".");
			car.play();
		} else {
			int temp = doorwithCar +1;
			println("You won a goat! The car was behind door number " + temp + ".");
			goat.play();
		}


	}
	private void changeDoor() {
		int changeDoorChoice = checkchangeUserChoice(doorGoatReveal);
		if (changeDoorChoice == 1) {
			println("FABULOUS JOB deciding to change your door. Did you know that by changing your door choice you are now 2X MORE LIKELY to win the car?");
			secondDoorChosen = 6 - doorChosen - doorGoatReveal-1;
			gameStatus[0][doorChosen -1] = 0;
			gameStatus[0][secondDoorChosen -1] = 0;
			println("Your new door is door number: " + secondDoorChosen + ".");
			GImage updatedImage = null;
			String doorSelected = "";
			String goatShown = "";
			if (secondDoorChosen -1 == 0) { doorSelected = "ONE";}
			if (secondDoorChosen -1 == 1) { doorSelected = "TWO";}
			if (secondDoorChosen -1 == 2) { doorSelected = "THREE";}
			if (doorGoatReveal == 0) { goatShown = "ONE";}
			if (doorGoatReveal == 1) { goatShown = "TWO";}
			if (doorGoatReveal == 2) { goatShown = "THREE";}

			String imagetoShow = "USERCHOSEN" + doorSelected + '_' + "GOATSHOWN" + goatShown+".png";
			//println(imagetoShow);
			updatedImage = new GImage (imagetoShow);
			updatedImage.setSize(canvas.getWidth(), canvas.getHeight()*SCALING_IMAGES_Y);
			canvas.add(updatedImage,0,(canvas.getHeight()-updatedImage.getWidth())*SCALING_IMAGES_X);

		} else { secondDoorChosen = doorChosen; }
	}



	private void drawCanvas() {
		canvas.removeAll();
		GImage doors = null;
		if (doorChosen == 0) {
			doors = new GImage(DOORS_NOCHOICE);
		}
		if (doorChosen ==1) {
			doors = new GImage(DOORCHOSEN_ONE);
		}
		if (doorChosen ==2) {
			doors = new GImage (DOORCHOSEN_TWO);
		}
		if (doorChosen ==3) {
			doors = new GImage (DOORCHOSEN_THREE);
		}	
		doors.setSize(canvas.getWidth(), canvas.getHeight()*SCALING_IMAGES_Y);
		canvas.add(doors,0,(canvas.getHeight()-doors.getWidth())*SCALING_IMAGES_X);
	}

	private void showGoatinCanvas(int goatbeingRevealed){
		canvas.removeAll();
		GImage doorGoat = null;
		String doorSelected = "";
		String goatShown = "";
		//println("doorChosen is" +doorChosen);
		//println("goatbeingRevealedis" + goatbeingRevealed);
		if (doorChosen -1 == 0) { doorSelected = "ONE";}
		if (doorChosen -1 == 1) { doorSelected = "TWO";}
		if (doorChosen -1 == 2) { doorSelected = "THREE";}
		if (goatbeingRevealed == 0) { goatShown = "ONE";}
		if (goatbeingRevealed == 1) { goatShown = "TWO";}
		if (goatbeingRevealed == 2) { goatShown = "THREE";}

		String imagetoShow = "USERCHOSEN" + doorSelected + '_' + "GOATSHOWN" + goatShown+".png";
		//println(imagetoShow);
		//println(gameStatus);
		doorGoat = new GImage (imagetoShow);
		doorGoat.setSize(canvas.getWidth(), canvas.getHeight()*SCALING_IMAGES_Y);
		canvas.add(doorGoat,0,(canvas.getHeight()-doorGoat.getWidth())*SCALING_IMAGES_X);

	}
	private void finalCanvasUpdate() {
		canvas.removeAll();
		GImage finalImage = null;
		String doorSelected = "";
		String carShown = "";
		if (secondDoorChosen -1 == 0) { doorSelected = "ONE";}
		if (secondDoorChosen -1 == 1) { doorSelected = "TWO";}
		if (secondDoorChosen -1 == 2) { doorSelected = "THREE";}
		if (doorwithCar == 0) { carShown = "ONE";}
		if (doorwithCar == 1) { carShown = "TWO";}
		if (doorwithCar == 2) { carShown = "THREE";}

		String imagetoShow = "USERCHOSEN" + doorSelected + '_' + "CARSHOWN" + carShown+".png";
		//println(imagetoShow);
		//println(gameStatus);
		finalImage = new GImage (imagetoShow);
		finalImage.setSize(canvas.getWidth(), canvas.getHeight()*SCALING_IMAGES_Y);
		canvas.add(finalImage,0,(canvas.getHeight()-finalImage.getWidth())*SCALING_IMAGES_X);
	}
	private int checkchangeUserChoice(int goatbeingRevealed) {
		println("Your door choice is: " + doorChosen +".");
		int temp = goatbeingRevealed + 1;
		println("There is a goat in door number: " + temp+".");
		println("With this new information, think hard about your initial door choice....");
		pause(PAUSE_TIME);
		int changeDoor = readInt("Would you like to change your door choice? Enter 1 for yes, 2 for no: ");
		while (changeDoor != 1 && changeDoor != 2) {
			println("Please try answering again.");
			changeDoor = readInt("Would you like to change your door choice? Enter 1 for yes, 2 for no: ");
		}
		return changeDoor;
	}
} 
