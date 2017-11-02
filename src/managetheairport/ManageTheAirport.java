package managetheairport;

// Imports for task intervals
import java.util.Timer;
import java.util.TimerTask;

// Imports for user input
import java.util.Scanner;

// Imports for data storage
import java.util.ArrayList;

/**
 *
 * 
 * @author KaiChong
 */
public class ManageTheAirport{
    
    public static void main(String[] args) {
        
        /*
        ***** OBJECTS ******
        */
        Scanner sc = new Scanner(System.in);
        StatisticsSystem airTrafficTower=new StatisticsSystem();
        HighWindWarningSystem windSystem = new HighWindWarningSystem();
        Timer simulatorTicker = new Timer();
        
        /*
        ***** QUEUES *****
        */
        // Plane Queue
        Queue planeQueue=new Queue(100);
        
        
        // TakeOff Queue 1
        Queue takeOffQueueOne=new Queue(5);
        // TakeOff Queue 2
        Queue takeOffQueueTwo=new Queue(5);
        // TakeOff Queue 3
        Queue takeOffQueueThree=new Queue(5);
        
        
        // Landing Queue 1
        Queue landingQueueOne=new Queue(5);
        // Landing Queue 2
        Queue landingQueueTwo=new Queue(5);
        // Landing Queue 3
        Queue landingQueueThree=new Queue(5);
        
        
        // Waiting Plane Queue
        Queue planeHoldingQueue=new Queue(50);
        // Gate Queue
        Queue gateQueue=new Queue(100);
        // Passenger Queue
        Queue passengerQueue= new Queue(500);
        
        /*
        ***** VARIABLES *****
        */
        // Simulator running time
        int runningMinutes;
        // Flight name & number (7x2)
        String[][] flightNameAndNumber= {
            {"MH", "2400"},
            {"VA", "470"},
            {"QF", "540"},
            {"SG", "2300"},
            {"AE", "2100"},
            {"QR", "430"},
            {"MH", "210"}
        };
        // Airport gates
        ArrayList<Gate> smallAirport=new ArrayList<Gate>();
        for (int i=1; i<=5; i++) {
            smallAirport.add(new Gate(i));
        }
        
        /*
        ***** CODE BLOCKS *****
        */
        // CHECK FOR HIGH WIND (1 min interval)
        TimerTask checkHighWind=new TimerTask() {
            @Override
            public void run() {
                windSystem.checkForHighWind();
            }
        };
        
        // FILL passengerQueue UP (1.5 min interval)
        TimerTask fillPassengerQueue=new TimerTask() {
            @Override
            public void run() {
                if (passengerQueue.size()<500) {
                    for (int i=0; i<500; i++) {
                        passengerQueue.enqueue(new Passenger(System.currentTimeMillis()));
                    }
                }
            }
        };
        
        // GENERATE PLANE AND ADD TO PLANE QUEUE (2 min interval)
        TimerTask generateNewPlane=new TimerTask() {
            @Override
            public void run() {
                // Variables for plane
                boolean isPlaneArriving;
                String name;
                String num;
                int capacity;
                
                // Will plane arrive or depart from airport?
                if (airTrafficTower.generateNumWithRange(1, 2)==2) {
                    isPlaneArriving=true;
                } else {
                    isPlaneArriving=false;
                }
                
                // Select flight name
                name=flightNameAndNumber[airTrafficTower.generateNumWithRange(0, 6)][0];
                // Select flight number
                num=flightNameAndNumber[airTrafficTower.generateNumWithRange(0, 6)][1];
                // Select plane capacity
                capacity=airTrafficTower.generateNumWithRange(100, 500);
                
                // Create plane and assign variables
                Plane thisPlane=new Plane(name, num, capacity, isPlaneArriving);
                if (isPlaneArriving==true) {
                    thisPlane.setTimeOfArrival(5);
                } else {
                    thisPlane.setTimeOfDeparture(5);
                }
                
                // Board passengers to plane
                int availablePassengers=thisPlane.getFlightCapacity()-airTrafficTower.generateNumWithRange(0, 50);
                for (int i=0; i<availablePassengers; i++) {
                    thisPlane.boardOnePassenger((Passenger) passengerQueue.dequeue());
                }
                
                // Print info of generated plane
                System.out.println("Flight "+thisPlane.getCompleteName()
                        +" with the capacity of "+thisPlane.getFlightCapacity()
                        +" has been generated. This flight has "+thisPlane.getNumberOfPassengersOnboard()
                        + " passengers onboard.");
                
                // Store plane in queue
                planeQueue.enqueue(thisPlane);
            }
        };
        
        // SET PLANE TO ARRIVE OR DEPART FROM AIRPORT (5 min interval)
        TimerTask planeArriveOrDepartFromAirport=new TimerTask() {
            @Override
            public void run() {
                Plane thisPlane;
                // Process planes in holdingQueue first
                if (planeHoldingQueue.isEmpty()==false) {
                    thisPlane=(Plane) planeHoldingQueue.dequeue();
                } else {
                    thisPlane=(Plane) planeQueue.dequeue();
                }
                
                // Segregate planes to landing or takeoff
                if (thisPlane.isPlaneArriving()==true) {
                            // Set plane to landingQueueOne or landingQueueTwo or landingQueueThree
                            if (landingQueueOne.size()==5&&landingQueueTwo.size()==5&&landingQueueThree.size()==5) {
                                planeHoldingQueue.enqueue(thisPlane);
                                System.out.println("Flight "+thisPlane.getCompleteName()+" has been assigned to wait by the plane holding area since all the landing runways are filled.");
                            } else if (landingQueueOne.size()==5&&landingQueueTwo.size()==5) {
                                thisPlane.storeTimeBeforeQueue();
                                landingQueueThree.enqueue(thisPlane);
                                    System.out.println("Flight "+thisPlane.getCompleteName()+" has been assigned to third landing runway.");
                            } else if (landingQueueOne.size()==5) {
                                thisPlane.storeTimeBeforeQueue();
                                landingQueueTwo.enqueue(thisPlane);
                                    System.out.println("Flight "+thisPlane.getCompleteName()+" has been assigned to second landing runway.");
                            } else {
                                thisPlane.storeTimeBeforeQueue();
                                landingQueueOne.enqueue(thisPlane);
                                    System.out.println("Flight "+thisPlane.getCompleteName()+" has been assigned to first landing runway.");
                            }
                } else if (thisPlane.isPlaneArriving()==false) {
                            // Set plane to takeOffQueueOne or takeOffQueueTwo or takeOffQueueThree
                            if (takeOffQueueOne.size()==5&&takeOffQueueTwo.size()==5&&takeOffQueueThree.size()==5) {
                                planeHoldingQueue.enqueue(thisPlane);
                                System.out.println("Flight "+thisPlane.getCompleteName()+" has been assigned to wait by the plane holding area since all the takeoff runways are filled.");
                            } else if (takeOffQueueOne.size()==5&&takeOffQueueTwo.size()==5) {
                                thisPlane.storeTimeBeforeQueue();
                                takeOffQueueThree.enqueue(thisPlane);
                                    System.out.println("Flight "+thisPlane.getCompleteName()+" has been assigned to third takeoff runway.");
                            } else if (takeOffQueueOne.size()==5) {
                                thisPlane.storeTimeBeforeQueue();
                                takeOffQueueTwo.enqueue(thisPlane);
                                    System.out.println("Flight "+thisPlane.getCompleteName()+" has been assigned to second takeoff runway.");
                            } else {
                                thisPlane.storeTimeBeforeQueue();
                                takeOffQueueOne.enqueue(thisPlane);
                                    System.out.println("Flight "+thisPlane.getCompleteName()+" has been assigned to first takeoff runway.");
                            }
                } else {
                    System.out.println("MAJOR ERROR: Plane is neither arriving nor landing.");
                }  
            }
        };
        
        // SET PLANE TO TAKE OFF (2 min interval)
        TimerTask planeTakesOff=new TimerTask() {
            @Override
            public void run() {
                Plane thisPlane;
                if (takeOffQueueOne.isEmpty()==false&&takeOffQueueTwo.isEmpty()==false&&takeOffQueueThree.isEmpty()==false) {
                    int triDice=airTrafficTower.generateNumWithRange(1, 3);
                    
                    if (triDice==1) {
                        thisPlane=(Plane) takeOffQueueOne.peek();
                        if (windSystem.getWindStatus()==false) {
                            thisPlane=(Plane) takeOffQueueOne.dequeue();
                            thisPlane.storeTimeAfterQueue();
                            airTrafficTower.addToTotalMillisecondsForTakeOffQueue(thisPlane.getTimeTakenInMilliseconds());
                            airTrafficTower.increasePlanesTookOffByOne();
                            if (thisPlane.getFlightCapacity()<300) {
                                airTrafficTower.increaseSmallPlanesTookOffByOne();
                            }
                            System.out.println("Flight "+thisPlane.getCompleteName()+" has TAKEN OFF from the first runway!");
                        } else {
                            System.out.println("Unfortunately, Flight "+thisPlane.getCompleteName()+" is delayed due to high wind and is unable to takeoff from the first runway.");
                            airTrafficTower.increasePlanesThatMissedTargetTimeByOne();
                        }
                    }
                    
                    if (triDice==2) {
                        thisPlane=(Plane) takeOffQueueTwo.peek();
                        if (windSystem.getWindStatus()==false) {
                            thisPlane=(Plane) takeOffQueueTwo.dequeue();
                            thisPlane.storeTimeAfterQueue();
                            airTrafficTower.addToTotalMillisecondsForTakeOffQueue(thisPlane.getTimeTakenInMilliseconds());
                            airTrafficTower.increasePlanesTookOffByOne();
                            if (thisPlane.getFlightCapacity()<300) {
                                airTrafficTower.increaseSmallPlanesTookOffByOne();
                            }
                            System.out.println("Flight "+thisPlane.getCompleteName()+" has TAKEN OFF from the second runway!");
                        } else {
                            System.out.println("Unfortunately, Flight "+thisPlane.getCompleteName()+" is delayed due to high wind and is unable to takeoff from the second runway.");
                        }
                    }
                    
                    if (triDice==3) {
                        thisPlane=(Plane) takeOffQueueThree.peek();
                        if (windSystem.getWindStatus()==false) {
                            thisPlane=(Plane) takeOffQueueThree.dequeue();
                            thisPlane.storeTimeAfterQueue();
                            airTrafficTower.addToTotalMillisecondsForTakeOffQueue(thisPlane.getTimeTakenInMilliseconds());
                            airTrafficTower.increasePlanesTookOffByOne();
                            if (thisPlane.getFlightCapacity()<300) {
                                airTrafficTower.increaseSmallPlanesTookOffByOne();
                            }
                            System.out.println("Flight "+thisPlane.getCompleteName()+" has TAKEN OFF from the third runway!");
                        } else {
                            System.out.println("Unfortunately, Flight "+thisPlane.getCompleteName()+" is delayed due to high wind and is unable to takeoff from the third runway.");
                        }
                    }
                    
                } else {
                    // System.out.println("All takeoff queues are empty.");
                }
            }
        };
        
        // REMOVE PLANES DONE AT GATE
        TimerTask removePlanesDoneAtGate=new TimerTask() {
            @Override
            public void run() {
                
                smallAirport.get(0).checkAndTweakAvailability();
                smallAirport.get(1).checkAndTweakAvailability();
                smallAirport.get(2).checkAndTweakAvailability();
                smallAirport.get(3).checkAndTweakAvailability();
                smallAirport.get(4).checkAndTweakAvailability();
                
            }
        };
        
        // SET PLANE TO LAND
        TimerTask planeLands=new TimerTask() {
            @Override
            public void run() {
                Plane thisPlane;
                if (landingQueueOne.isEmpty()==false&&landingQueueTwo.isEmpty()==false&&landingQueueThree.isEmpty()==false) {
                    int triDice=airTrafficTower.generateNumWithRange(1, 3);
                    
                    if (triDice==3) {
                        thisPlane=(Plane) landingQueueThree.peek();
                        if (windSystem.getWindStatus()==false) {
                            thisPlane=(Plane) landingQueueThree.dequeue();
                            thisPlane.storeTimeAfterQueue();
                            airTrafficTower.addToTotalMillisecondsForLandingQueue(thisPlane.getTimeTakenInMilliseconds());
                            airTrafficTower.increasePlanesLandedByOne();
                                if (thisPlane.getFlightCapacity()>=300) {
                                    airTrafficTower.increaseLargePlanesLandedByOne();
                                }
                            gateQueue.enqueue(thisPlane);
                            System.out.println("Flight "+thisPlane.getCompleteName()+" has LANDED at the third runway!");
                        } else {
                            System.out.println("Unfortunately, Flight "+thisPlane.getCompleteName()+" is delayed due to high wind and is unable to land at the third runway.");
                            airTrafficTower.increasePlanesThatMissedTargetTimeByOne();
                        }
                    }
                    
                    if (triDice==2) {
                        thisPlane=(Plane) landingQueueTwo.peek();
                        if (windSystem.getWindStatus()==false) {
                            thisPlane=(Plane) landingQueueTwo.dequeue();
                            thisPlane.storeTimeAfterQueue();
                            airTrafficTower.addToTotalMillisecondsForLandingQueue(thisPlane.getTimeTakenInMilliseconds());
                            airTrafficTower.increasePlanesLandedByOne();
                                if (thisPlane.getFlightCapacity()>=300) {
                                    airTrafficTower.increaseLargePlanesLandedByOne();
                                }
                            gateQueue.enqueue(thisPlane);
                            System.out.println("Flight "+thisPlane.getCompleteName()+" has LANDED at the second runway!");
                        } else {
                            System.out.println("Unfortunately, Flight "+thisPlane.getCompleteName()+" is delayed due to high wind and is unable to land at the second runway.");
                            airTrafficTower.increasePlanesThatMissedTargetTimeByOne();
                        }
                    }
                    
                    if (triDice==1) {
                        thisPlane=(Plane) landingQueueOne.peek();
                        if (windSystem.getWindStatus()==false) {
                            thisPlane=(Plane) landingQueueOne.dequeue();
                            thisPlane.storeTimeAfterQueue();
                            airTrafficTower.addToTotalMillisecondsForLandingQueue(thisPlane.getTimeTakenInMilliseconds());
                            airTrafficTower.increasePlanesLandedByOne();;
                                if (thisPlane.getFlightCapacity()>=300) {
                                    airTrafficTower.increaseLargePlanesLandedByOne();
                                }
                            gateQueue.enqueue(thisPlane);
                            System.out.println("Flight "+thisPlane.getCompleteName()+" has LANDED at the first runway!");
                        } else {
                            System.out.println("Unfortunately, Flight "+thisPlane.getCompleteName()+" is delayed due to high wind and is unable to land at the first runway.");
                            airTrafficTower.increasePlanesThatMissedTargetTimeByOne();
                        }
                    }
                    
                } else {
                    //System.out.println("All landing queues have to be filled before a plane is allowed to land.");
                }
            }
        };
        
        // Send planes to respective gates
        TimerTask sendPlanesToGate=new TimerTask() {
            @Override
            public void run() {
                Plane thisPlane;
                System.out.println("Size of gateQueue is "+gateQueue.size());
                if (gateQueue.isEmpty()==false) {
                    thisPlane=(Plane) gateQueue.peek();
                    if (thisPlane.getFlightCapacity()>=300&&(smallAirport.get(4).getAvailability()==true||smallAirport.get(3).getAvailability()==true)) {
                        System.out.println("\nPlane is large capacity\n");
                        
                            thisPlane=(Plane) gateQueue.dequeue();
                            if (smallAirport.get(4).getAvailability()==true) {
                                
                                System.out.println("Plane sent to Gate 5.");
                                smallAirport.get(4).sendPlaneToThisGate(thisPlane);
                                System.out.println("Gate 5 availability is now "+smallAirport.get(4).getAvailability());
                                
                            } else if (smallAirport.get(3).getAvailability()==true) {
                                
                                System.out.println("Plane sent to Gate 4.");
                                smallAirport.get(3).sendPlaneToThisGate(thisPlane);
                                System.out.println("Gate 4 availability is now "+smallAirport.get(3).getAvailability());
                                
                            } else {
                                // Since all gates for large planes are filled. Plane in line is sent to rear of queue in order for other planes to be processed.
                                gateQueue.enqueue(gateQueue.dequeue());
                            }
                        
                    }
                    
                    if (thisPlane.getFlightCapacity()<300&&(smallAirport.get(2).getAvailability()==true||smallAirport.get(1).getAvailability()==true||smallAirport.get(0).getAvailability()==true)) {
                        System.out.println("\nPlane is small capacity\n");
                            
                            thisPlane=(Plane) gateQueue.dequeue();
                            if (smallAirport.get(2).getAvailability()==true) {
                                
                                System.out.println("Plane is sent to Gate 3");
                                smallAirport.get(2).sendPlaneToThisGate(thisPlane);
                                System.out.println("Gate 3 availability is now "+smallAirport.get(2).getAvailability());
                                
                            } else if (smallAirport.get(1).getAvailability()==true) {
                                
                                System.out.println("Plane is sent to Gate 2");
                                smallAirport.get(1).sendPlaneToThisGate(thisPlane);
                                System.out.println("Gate 2 availability is now "+smallAirport.get(1).getAvailability());
                                
                            } else if (smallAirport.get(0).getAvailability()==true) {
                                
                                System.out.println("Plane is sent to Gate 1");
                                smallAirport.get(0).sendPlaneToThisGate(thisPlane);
                                System.out.println("Gate 1 availability is now "+smallAirport.get(0).getAvailability());
                                
                            } else {
                                // Since all gates for small planes are filled. Plane in line is sent to rear of queue in order for other planes to be processed.
                                gateQueue.enqueue(gateQueue.dequeue());
                            }
                        
                    }
                    
                }
            }
        };
        
        // PRINT STATISTICS AND HALT SIMULATOR
        TimerTask haltSimulator=new TimerTask() {
                public void run() {
                    // Display simulator statistics
                    System.out.println("The number of planes that landed at the airport is "+airTrafficTower.getNumOfPlanesLanded()+". ");
                    System.out.println("The number of planes that tookoff from the airport is "+airTrafficTower.getNumOfPlanesTookOff()+". ");
                    
                    System.out.println("The average waiting time for each plane at the landing queue is "+airTrafficTower.convertMillisecondsToMinutes(airTrafficTower.getAverageTimeInMillisecondsAtLandingQueue())+" minutes. ");
                    System.out.println("The average waiting time for each plane at the takeoff queue is "+airTrafficTower.convertMillisecondsToMinutes(airTrafficTower.getAverageTimeInMillisecondsAtTakeOffQueue())+" milliseconds. ");
                    
                    System.out.println("The number of planes that missed their arrival/departure time is "+airTrafficTower.getNumOfPlanesThatMissedTargetTime()+". ");
                    
                    System.out.println("The number of planes that Gate 1 has served is "+smallAirport.get(0).getNumberOfPlanesThisGateServed()
                            +" and the number of passengers is "+smallAirport.get(0).getNumberOfPassengersThisGateServed()+". ");
                    System.out.println("The number of planes that Gate 2 has served is "+smallAirport.get(1).getNumberOfPlanesThisGateServed()
                            +" and the number of passengers is "+smallAirport.get(1).getNumberOfPassengersThisGateServed()+". ");
                    System.out.println("The number of planes that Gate 3 has served is "+smallAirport.get(2).getNumberOfPlanesThisGateServed()
                            +" and the number of passengers is "+smallAirport.get(2).getNumberOfPassengersThisGateServed()+". ");
                    System.out.println("The number of planes that Gate 4 has served is "+smallAirport.get(3).getNumberOfPlanesThisGateServed()
                            +" and the number of passengers is "+smallAirport.get(3).getNumberOfPassengersThisGateServed()+". ");
                    System.out.println("The number of planes that Gate 5 has served is "+smallAirport.get(4).getNumberOfPlanesThisGateServed()
                            +" and the number of passengers is "+smallAirport.get(4).getNumberOfPassengersThisGateServed()+". ");
                    
                    System.out.println("The percentage of larger capacity planes that landed is "+airTrafficTower.getLargePlanesOverPlanesLandedPercentage()+"%.");
                    System.out.println("The percentage of smaller capacity planes that tookoff is "+airTrafficTower.getSmallPlanesOverPlanesTookOffPercentage()+"%. ");
                    
                    System.out.println("Simulator ENDED.");
                    System.exit(0);
                }
            };
        
        /**
        * PROGRAM BEGINS HERE5
        */
        // Get user's input for how long simulator should run
        do {
            System.out.println("Please enter the number of minutes you'd like this simulator to run: (between 10 and 500 minutes only)");
            while (!sc.hasNextInt()) {
                System.out.println("Please enter the duration in number! (i.e. 50)");
                sc.next();
            }
            runningMinutes=sc.nextInt();
            sc.nextLine();
        } while (runningMinutes<10 || runningMinutes>500);
        System.out.println("The simulator will run for: "+runningMinutes+" minutes.");
        
            simulatorTicker.scheduleAtFixedRate(checkHighWind, 0,                       60000);
            simulatorTicker.scheduleAtFixedRate(generateNewPlane, 1,                    120000);
            simulatorTicker.scheduleAtFixedRate(fillPassengerQueue,2,                   60000);
            simulatorTicker.scheduleAtFixedRate(planeArriveOrDepartFromAirport, 3,      300000);
            simulatorTicker.scheduleAtFixedRate(planeTakesOff, 4,                       120000);
            simulatorTicker.scheduleAtFixedRate(planeLands, 5,                          180000);
            simulatorTicker.scheduleAtFixedRate(sendPlanesToGate, 6,                    180000);
            simulatorTicker.scheduleAtFixedRate(removePlanesDoneAtGate, 7,              60000);
            simulatorTicker.schedule(haltSimulator, airTrafficTower.convertMinutesToMilliseconds(runningMinutes));
            
        /**
        * PROGRAM ENDS HERE
        */
        
    }
    
}