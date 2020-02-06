# RPS_DESIGN.md

### High-Level Design of RPS:
- Player Class
    - Update its score
    - Chooses its weapon with user input
    - Knows its score
    - Knows its name
    - Generate weapon choice for single player
- Battle Class
    - Contains map of each weapon type & weaknesses
    - Returns who wins
    - Will have add/remove weapon methods
    - Can read simulation.rules from a file
    - Can save added weapons to a file
- Game Class
    - Handles turns
    - Updates interface
    - Accept input from GUI, send to  checker instance
    - Updates player/battle from the GUI
- Configuration File Format
    - Contains weapons and their weaknesses
    - Each line has the format: "weapon weakness weakness ..."

### CRC CARDS
- Player Class
    - public void updateScore();
    - public String generateWeaponChoice();
    - Interacts with Game class.
- Battle Class
    - public Player determineWinner(List\<Player\> players);
    - private void loadWeapons(String filepath);
    - public void addWeapon(String weaponName, ArrayList\<String\> weaknesses);
    - public void removeWeapon(String weaponName);

    - Used by the Game class, accepts Players as arguments to functions.
- Game Class
    - private void createPlayers(int numPlayers);
    - private void playGame();
    - private void setPlayerWeaponChoice(Player player, input from GUI);
    - private void updateGUI();
    - Gets input from the GUI, stores data in Players, and uses Battle for logic and storing weapons.

### Use Cases
- A new game is started with two players, their scores are reset to 0.
    - Game: createPlayers(2);
- A player chooses his RPS "weapon" with which he wants to play for this round.
    - GUI: Get user input
    - Game: setPlayerWeaponChoice(player, input from GUI);
- Given two players' choices, one player wins the round, and their scores are updated.
    - Battle: determineWinner(game.getplayer());
    - Battle: winner.updateScore(); (called from determineWinner)
    - Game: updateGUI();
- A new choice is added to an existing game and its relationship to all the other choices is updated.
    - Battle class: addWeapon(weapon name, list of weaknesses)
- A new game is added to the system, with its own relationships for its all its "weapons".
    - Game: new Battle(filename);
    - battle2 constructor calls loadWeapons(filename);
