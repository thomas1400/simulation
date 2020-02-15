simulation
====

This project implements a cellular automata simulation.controller.

Names: Grant LoPresti, Sebastian Williams, Thomas Owens

### Timeline

Start Date: Jan 23rd, 2020

Finish Date: Feb 14th, 2020

Hours Spent: 40+ each

### Primary Roles
* Grant
    - Focused on controller and xmlGeneration packages

 * Sebastian
    - Focused on view and events packages

 * Thomas
    - Focused on rules and model packages

### Resources Used
* 308 Website
* Stack Overflow

### Running the Program

Main class: 
- Run the Home Class to begin the program
    - src/simulation/view/Home.java

Data files needed: 
- Essential data files (mostly fxml) are included in the src path. "Extra" data files are in the
 data resources folder and include both XML files that can be directly selected by the program
 , as well as .txt files which can be used to load a custom simulation.
 
Features implemented:
* Load multiple simulations
* 6 Default Rule Types to select from
* Rectangle, Triangle, and Future grid types
* Play, pause, speed up, slow down
* Reset to initial simulation
* Dynamically change global variables (Settings)
* View visualization of cell populations
* Create and Run custom Simulations
* Save the current state of a simulation (grid and variables) in a new .xml


### Notes/Assumptions

Assumptions or Simplifications:
* We made strong assumptions about the format of XML files, which limits the addition of new features by making it difficult to parse new additions. We could change this by simply making our XMLReader a bit more intelligent in its parsing, using tags to identify values.
* We made certain assumptions about the kind of simulations that we run, assuming at first that cell state would only depend on those around them, but later modifying our assumption to assume that the future state of a cell will only depend on the current state of all of the other cells in the grid, and no other information.

Interesting data files:
- rpsSimulation.xml
    - A random triangle RPS grid that really displays the dynamic variable changing abilities of
     our simulation (adjust the global variables by clicking "Settings")
- segregationtest.xml
    - Also a random triangle grid that forms a really satisfying and also very random "segregated
     model of space"

Known Bugs:
- XML Generation doesn't have dynamic error checking and simply tells you you've done something
 wrong if the custom simulation doesnt compile.
