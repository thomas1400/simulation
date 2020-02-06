# DESIGN_EXERCISE.md

### High-Level Simulation Design

- How does a Cell know about its neighbors? How can it update itself without effecting its neighbors update?
    - A Cell stores pointers to each of its four (or eight) neighbors. Corners and sides have null for pointers. It can update without updating its neighbor by having a method that calculates what the update should be, and then a method that actually updates the state of the cell.
- What relationship exists between a Cell and a simulation's simulation.rules?
    - 
- What is the grid? Does it have any behaviors? Who needs to know about it?
    - 2 dimensional array 
    - has no behavior
    - 
- What information about a simulation needs to be the configuration file?
    - Size of grid
    - Initial Grid states
    - update simulation.rules
        - Number of states
        - update state calculation method
- How is the graphical simulation.view of the simulation updated after all the cells have been updated?


TYPE
COLOR
N1 N2 N3 N4 RESULT
N1 N2 N3 N4 RESULT

Could store simulation.rules as a map of type numbers to maps of arrays to results.