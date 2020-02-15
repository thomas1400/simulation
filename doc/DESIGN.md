# Simulation Design Final
### Names

## Team Roles and Responsibilities

 * Grant LoPresti
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
 - Adding a new GridType is as simple as _**TODO**_ 

## High-level Design
- The Home class is the entry way into the simulation, it can load as many independent
 simulations as specified, either through 1 of 6 default choices, an XML file picker, or by
  creating a custom simulation.
- Each of these options creates an independent Simulation Object which involves calls to helper
 classes which handle reading in the XML (XMLReader) and initializing the simulation (Initializer).

#### Core Classes


## Assumptions that Affect the Design

#### Features Affected by Assumptions


## New Features HowTo

#### Easy to Add Features

#### Other Features not yet Done

