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
- 

#### What Features are Easy to Add
 - Adding a new Rule is as simple as creating a new rules class, and adding a branch to the main
  rules if tree that creates that type of rules of a certain string is found in the XML.
 - Adding a new simulation is as simple as following the "Custom XML" prompts from the Home
  window to specify all of the specifics of your custom XML that then loads a simulation.
 - Adding new Grid types is very easy, given the inheritance hierarchy of those classes. All you have to do to add a new one is write a new class and add the corresponding conditional branch in Initializer.


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

#### Features Affected by Assumptions


## New Features HowTo

#### Easy to Add Features

#### Other Features not yet Done

