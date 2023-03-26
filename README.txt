# RIFTER

Rifter is an application, which creates interactive experience with local culture, history, and landmarks. User earns VRC coins by completing quests and can spend them on purchases in local stores.

## Installation

JDK18 used

## Usage

Unzip archive
Move to the folder
Open in IntelliJ IDEA
Run Main class

## Project overview
1. Project is split into 3 packages: Database, Game, Menu
	- "Menu" package describes navigation map of the project. Overall, user has interaction with this package, while all logic stores in another package.
	- "Database" package can be considered as backend of the project. In this package we have tight interaction with database, files and logic of the program.
	- "Game" package relates to game in the application. Everything related to map (game) stores inside this package, such as map generation, player interaction, etc.
2. Folder "res" contains all static files which are used in the game. During static files reading an error can happen. This happens because of different systems and IDEs. Program was created and tested on Windows 11, IntelliJ IDEA, JDK18. If you have encounterd a bug, try to change path in java code. Currently path is declared like "folder\\another_folder\\file.ext", you can try to change \\ -> \ or \\ -> /
3. Classes are widely used in the project. I tried to create classes where I can describe object with them.
4. Files with extension .csv are not created by the program. They are provided with zip archive and should not be deleted.
5. Do not change stucture of zip archive (move files or images to another folders).
6. During project creation, I have reffered to different websites. I have left link to the website in comments, as well as in refferences.
7. Tests were conducted, no errors were found.

## References
www.deviantart.com. (n.d.). Building sheet 1 by zetavares852 on DeviantArt. [online] Available at: https://www.deviantart.com/zetavares852/art/Building-sheet-1-171172414 [Accessed 26 Mar. 2023].

itch.io. (n.d.). City Pack - Top Down - Pixel Art by NYKNCK. [online] Available at: 
https://nyknck.itch.io/citypackpixelart?ssp_iabi=1677457588864 [Accessed 26 Mar. 2023].

itch.io. (n.d.). 16x16 RPG character sprite sheet by @javikolog. [online] Available at: 
https://route1rodent.itch.io/16x16-rpg-character-sprite-sheet?ssp_iabi=1677455000285 [Accessed 26 Mar. 2023].

www.youtube.com. (n.d.). Frame Close using close button in AWT Java. [online] Available at: https://www.youtube.com/watch?v=po49sg9ckio [Accessed 26 Mar. 2023].

Stack Overflow. (n.d.). (Delta time) Getting 60 updates a second in java. [online] Available at: https://stackoverflow.com/questions/26838286/delta-time-getting-60-updates-a-second-in-java [Accessed 26 Mar. 2023].

www.youtube.com. (n.d.). World and Camera - How to Make a 2D Game in Java #5. [online] Available at: https://www.youtube.com/watch?v=Ny_YHoTYcxo&list=PL_QPQmz5C6WUF-pOQDsbsKbaBZqXj4qSq&index=5 [Accessed 26 Mar. 2023].

GeeksforGeeks. (2018). Stream anyMatch() in Java with examples. [online] Available at: https://www.geeksforgeeks.org/stream-anymatch-java-examples/.

GeeksforGeeks. (2021). Creating an ArrayList with Multiple Object Types in Java. [online] Available at: https://www.geeksforgeeks.org/creating-an-arraylist-with-multiple-object-types-in-java/.

GeeksforGeeks. (2020). How to Swap Two Elements in an ArrayList in Java? [online] Available at: https://www.geeksforgeeks.org/how-to-swap-two-elements-in-an-arraylist-in-java/ [Accessed 26 Mar. 2023].

Baeldung. (2023) Attaching Values to Java Enum [online] Available at: https://www.baeldung.com/java-enum-values [Accessed 26 Mar. 2023].

## License

[MIT](https://choosealicense.com/licenses/mit/)