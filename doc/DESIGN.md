# Simulation Design Final
### Grant LoPresti, Sebastian Williams, Thomas Owens

## Team Roles and Responsibilities

 * Grant
    - Focused on controller and xmlGeneration packages with some work in the view package
    - Simulation API that communicates with Grid and GUI
    - XML generation, parsing, and custom creation

 * Sebastian
    - Focused on view and events packages

 * Thomas
    - Focused on rules and model packages


## Design goals
- The overall goal of this project was to create an extremely flexible implementation of a simulation platform for cellular automata. We aimed to create code and data structures that were easy to extend with new features and that held up to rigorous design standards, exemplifying the model-view-controller model of programming.

## High-level Design
* Overall, the project is decomposed into 5 main parts: the Controller, Model, Rules, XML Generation, and View.
    * The Controller is made up of the Simulation class, which handles stepping and running the simulation, and the Initializer and XMLReader, which handle initializing new simulations from a file. The module acts as an interface between the back-end of the Model and the front-end of the View.
    * The Model is made up of the Grid and States. The Grid holds the simulation's data at a low level: it stores the state of each cell of the simulation and directly controls the updating of each cell. State is a class that bundles all cell data and updates the cell. The Model is created and used by the Controller, and relies on the Rules for the logic of the simulation, meaning it is simulation-type-independent.
    * The Rules hold the rules of a specific simulation type. They are initialized by the Controller and passed off to the Model, which uses them to update the Grid.
    * The XML Generation module handles all generation of new XML files and parsing of user input. It is created and used by the View exclusively, since all XML generation relies on user input from the GUI.
    * The View displays all visual information from the Controller, which is passed to the Controller from the Model. The View has nothing to do with the Model, and does not even independently generate the Model's visual representation. Instead, the Model generates its own visual representation and passes it through the Controller to the View. The View is nothing more than a bundling of UI elements and simulation output.

#### Core Classes
The core classes include:
* Home
    * Home is a landing page that lets you launch multiple simulation windows, either from preset simulation options or from a file. It only interacts with the SimulationWindow class, which it only launches, then does not interact with again.
* SimulationWindow
    * SimulationWindow is, as you'd guess, the main simulation window. It holds the graphical representation of the simulation and its controls. This class holds a Simulation instance and pulls updates from it as a listener.
* Simulation
    * Simulation is the controller of the simulation. When created, it passes its XML file off to an Initializer, which creates new Grid and Rules instances for it. It holds these objects and controls the stepping and updating of the Grid, and acts as a liason between the model (Grid and Rules) and the view (SimulationWindow). 
* Grid
    * Grid is the back-end data structure that holds the state of the simulation. Its implementation is hidden from all other classes. It calculates updates using a Rules class and passes the result off as a graphical representation to the View through Simulation.
* Rules
    * Rules are a set of classes that encode the rules of specific simulation types. They are derived from an abstract class Rules, making the addition of new simulation types easy.

## Assumptions that Affect the Design
* We made strong assumptions about the format of XML files, which limits the addition of new features by making it difficult to parse new additions. We could change this by simply making our XMLReader a bit more intelligent in its parsing, using tags to identify values.
* We made certain assumptions about the kind of simulations that we run, assuming at first that cell state would only depend on those around them, but later modifying our assumption to assume that the future state of a cell will only depend on the current state of all of the other cells in the grid, and no other information.

#### Features Affected by Assumptions
* The XML assumption made it more difficult to add new features to the XML files, and required that each time we added a new feature, it had to be added to every testing XML file we had. This made it difficult to test implementations and add new properties to simulations.
* The simulation types assumption means that we cannot run simulations that require a "patch of ground" or properties outside of the typical cell state associated with most cellular automata. In particular, we could not run Sugarscape because we do not have the capability to store information about the ground itself. However, this is acceptable, because it enabled us to create a very robust design for those simulations that we do handle.

## New Features How-To
* To add a new type of grid, such as hexagonal, create a new class that extends Grid in the model package and implement the one required method, getGridPane(). Then, add a new conditional branch in the Initializer.
* To add a new type of ruleset, like Langton's Loop, create a new class that extends Rules in the rules package and implement the required abstract methods. Then, add a new conditional branch in the Initializer.
* To add infinite (unbounded) grids, you do have to modify the Grid method update() or a similar method to check the edge cells for state updates and expand the grid as necessary. However, no change needs to be made to the GUI or controller. Both will adjust to the changes automatically.
* To improve error checking further, simply add a new Label to the alert box that contains the detailed error message provided with every MalformedXMLException thrown, a very quick addition.

#### What Features are Easy to Add
 - Adding a new Rule is as simple as creating a new rules class, and adding a branch to the main
  rules if tree that creates that type of rules of a certain string is found in the XML.
 - Adding a new simulation is as simple as following the "Custom XML" prompts from the Home
  window to specify all of the specifics of your custom XML that then loads a simulation.
 - Adding new Grid types is very easy, given the inheritance hierarchy of those classes. All you have to do to add a new one is write a new class and add the corresponding conditional branch in Initializer.

