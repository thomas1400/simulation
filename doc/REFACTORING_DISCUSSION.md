# REFACTORING_DISCUSSION

## To Fix:
 - Make Grid only contained in a "Grid" class.
     - Make the Grid handle updating and stepping instead of delegating to individual Cells, in order to make the Grid more open to extension.
 - Grid needs to implement immutable interface 
    - Interface has smaller set of methods

## Fixed:
- Fixed XML leading errors by surrounding with try/catch and using a .xml filter
- Improved all Rules classes, especially PredatorPreyRule, by extracting simpler, shorter methods from the method that calculates the next state of a cell.
- Removed empty methods from Rules classes that did not have global variables by incorporating variable setting into the constructor.
- Improved error catching for XML files by extracting the method that creates new Simulation instances in the GUI class and surrounding the instantiation with a try/catch block. If there is an XML file issue, a popup pops up alerting the user and the Simulation window closes (WIP).
- Shortened a very long method in the GUI by extracting a method to create a Button, which took the method down significantly from 43 lines.
- Extracted all Cell state values (magic numbers) from Rules classes, e.g.: 0 means BLOCKED, 1 means EMPTY, 2 means FILLED.
- Extracted magic numbers from the GUI, e.g., window width and height.