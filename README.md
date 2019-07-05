# PacKnight


# GAME DEMO </br>

![Game Demo](demo/demo.gif)


<h2 id="clarifications">Design Specifications</h2>
<p>This section will be for any details we believe were not clear enough in the original spec. It is placed at the top for better visibility.</p>

<ul>
  <li>For phase 1, your game does not necessarily need to suppprt <code class="highlighter-rouge">playWithKeyboard()</code> but it must support a portion of the <code class="highlighter-rouge">playWithInputString()</code> method. Specifically, you should be able to play your game with an input string which starts a new game, types in a seed, begins the game/generates the world, and then returns the array representing the world at that point.</li>
  <li>Letters in the input string can be of either case and your game should be able to accept either keypress (ie. “N” and “n” should both start a new game from the menu screen).</li>
  <li>In the case that a player attempts to load a game with no previous save, your game should end and the game window should close with no errors produced.</li>
  <li>In the base requirements, the command “:Q” should save and completely terminate the program. This means an input string that contains “:Q” should not have any more characters after it and loading a game would require the program to be run again with an input string starting with “L”</li>
  <li>Your game should be able to handle any positive seed up to 9,223,372,036,854,775,807.</li>
  <li>Your game should <strong>NOT</strong> render any tiles or play any sound when played with <code class="highlighter-rouge">playWithInputString()</code>.</li>
  <li>StdDraw does not support key combinations. When we say “:Q”, we mean “:” followed by “Q”.</li>
  <li>Your project should only use standard java libraries (imported from java.*) or any libraries we provided with your repo. This is only relevant to the autograder so if you’d like to other libraries for gold points and for the video, feel free to do so.</li>
  <li>Any TETile objects you create should be given a unique character that other tile’s do not use. Even if you are using your own images for rendering the tile, each TETile should still have its own character representation.</li>
  <li>The only files you may create must have the suffix “.txt” (for example “savefile.txt”). You will get autograder issues if you do not do this.</li>
</ul>

<h2 id="overview">Overview</h2>

<p>Your task is to design and implement a 2D tile-based game. By “tile-based”, we mean the world for your game will consist of a 2D grid of tiles. By “game” we mean that the player will be able to walk around and interact with the world. Your game will have an overhead perspective. As an example of a much more sophisticated game than you will build, the NES game “Zelda II” is (sometimes) a tile based overhead game:</p>

<p><img src="http://www.mobygames.com/images/shots/l/31358-zelda-ii-the-adventure-of-link-nes-screenshot-an-overhead-view.jpg" alt="Zelda2" /></p>

<p>The game you build can either use graphical tiles (as shown above), or it can use text based tiles, like the <a href="https://sites.google.com/site/broguegame/">game shown below</a>:</p>

<p><img src="/materials/proj/proj2/img/brogue_textbased_example.png" alt="brogue" /></p>

<p>We will provide a tile rendering engine, a small set of starter tiles, and the headers for a few required methods that must be implemented for your game and that will be used by the autograder. </p>


<h3 id="skeleton-code-structure">Skeleton Code Structure</h3>

<p>The skeleton code contains two key packages that you’ll be using: <code class="highlighter-rouge">byog.TileEngine</code> and <code class="highlighter-rouge">byog.Core</code>. <code class="highlighter-rouge">byog.TileEngine</code> provides some basic methods for rendering, as well as basic code structure for tiles, and contains:</p>
<ul>
  <li><code class="highlighter-rouge">TERenderer.java</code> - contains rendering-related methods.</li>
  <li><code class="highlighter-rouge">TETile.java</code> - the type used for representing tiles in the world.</li>
  <li><code class="highlighter-rouge">Tileset.java</code> - a library of provided tiles.</li>
</ul>

<p><strong>IMPORTANT NOTE: Do NOT change TETile.java’s <code class="highlighter-rouge">charcter</code> field or <code class="highlighter-rouge">character()</code> method as it may lead to bad autograder results.</strong></p>

<p>The other package <code class="highlighter-rouge">byog.Core</code> contains everything unrelated to tiles. We recommend that you put all of your game code in this package, though this not required. The <code class="highlighter-rouge">byog.Core</code> package comes with the following classes:</p>

<ul>
  <li><code class="highlighter-rouge">RandomUtils.java</code> - Handy utility methods for doing randomness related things.</li>
  <li><code class="highlighter-rouge">Main.java</code> - How the player starts the game. Reads command line arguments and calls the appropriate function in <code class="highlighter-rouge">Game.java</code>.</li>
  <li><code class="highlighter-rouge">Game.java</code> - Contains the two methods that allow playing of your game.</li>
</ul>

<p><code class="highlighter-rouge">byog.Core.Game</code> provides two methods for playing your game. The first is <code class="highlighter-rouge">public TETile[][] playWithInputString(String input)</code>. This method takes as input a series of keyboard inputs, and returns a 2D TETile array representing the state of the universe after processing all the key presses provided in input (described below). The second is <code class="highlighter-rouge">public void playWithKeyboard()</code>. This method takes input from the keyboard, and draws the result of each keypress to the screen.</p>

<p>The game engine makes heavy use of <code class="highlighter-rouge">StdDraw</code>. You may need to consult the API specification for <code class="highlighter-rouge">StdDraw</code> at some points in the project, which can be found <a href="https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html">here</a>.</p>

<h3 id="phase-1-world-generation">Phase 1: World Generation</h3>

<p>As mentioned above, the first goal of the project will be to write a world generator. The requirements for your world are listed below:</p>
<ul>
  <li>The world must be a 2D grid, drawn using our tile engine. The tile engine is described in <a href="../../lab/lab5/lab5">lab5</a>.</li>
  <li>The world must be pseudorandomly generated. Pseudorandomness is discussed in lab 5.</li>
  <li>The generated world must include rooms and hallways, though it may also include outdoor spaces.</li>
  <li>At least some rooms should be rectangular, though you may support other shapes as well.</li>
  <li>Your game must be capable of generating hallways that include turns (or equivalently, straight hallways that intersect).</li>
  <li>The world should contain a random number of rooms and hallways.</li>
  <li>The locations of the rooms and hallways should be random.</li>
  <li>The width and height of rooms should be random.</li>
  <li>The length of hallways should be random.</li>
  <li>Rooms and hallways must have walls that are visually distinct from floors. Walls and floors should be visually distinct from unused spaces.</li>
  <li>Rooms and hallways should be connected, i.e. there should not be gaps in the floor between adjacent rooms or hallways.</li>
  <li>The world should be substantially different each time, i.e. you should not have the same basic layout with easily predictable features</li>
</ul>

<p>As an example of a world that meets all of these requirements (click for higher resolution), see the image below. In this image, # represents walls, a dot represents floors, and there is also one golden colored wall segment that represents a locked door. All unused spaces are left blank.</p>

<p><a href="img/compliant_world_example.png"><img src="/materials/proj/proj2/img/compliant_world_example.png" alt="compliant_world_example" /></a></p>

<p>Once you’ve completed lab 5, you can start working on your world generation algorithm without reading or understanding the rest of the spec.</p>

<p><strong>It is very likely that you will end up throwing away your first world generation algorithm.</strong> This is normal! In real world systems, it is common to build several completely new versions before getting something you’re happy with. The room generation algorithm above was my 3rd one, and was ultimately much simpler than either of my first two.</p>

<p>You’re welcome to search the web for cool world generation algorithms. You should not copy and paste code from existing games, but you’re welcome to draw inspiration from code on the web. <strong>Make sure to cite your sources using @source tags.</strong> You can also try playing existing 2D tile based games for inspiration. <a href="https://sites.google.com/site/broguegame/">Brogue</a> is an example of a particularly elegant, beautiful game. <a href="http://www.bay12games.com/dwarves/">Dwarf Fortress</a> is an example of an incredibly byzantine, absurdly complex world generation engine.</p>

<h4 id="the-default-tileset-and-tile-rendering-engine">The Default Tileset and Tile Rendering Engine</h4>

<p>The tile rendering engine we provide takes in a 2D array of <code class="highlighter-rouge">TETile</code> objects and draws it to the screen. Let’s call this <code class="highlighter-rouge">TETile[][] world</code> for now. <code class="highlighter-rouge">world[0][0]</code> corresponds to the bottom left tile of the world. The first coordinate is the x coordinate, e.g. <code class="highlighter-rouge">world[9][0]</code> refers to the tile 9 spaces over to the right from the bottom left tile. The second coordinate is the y coordinate, and the value increases as we move upwards, e.g. <code class="highlighter-rouge">world[0][5]</code> is 5 tiles up from the bottom left tile. All values should be non-null, i.e. make sure to fill them all in before calling <code class="highlighter-rouge">renderFrame</code>. <strong>Make sure you understand the orientation of the world grid!</strong> If you’re unsure, write short sample programs that draw to the grid to deepen your understanding. <strong>If you mix up x vs. y or up vs. down, you’re going to have an incredibly confusing time debugging.</strong></p>

<p>We have provided a small set of default tiles in <code class="highlighter-rouge">Tileset.java</code> and these should serve as a good example of how to create <code class="highlighter-rouge">TETile</code> objects. We strongly recommend adding your own tiles as well.</p>

<p>The tile engine also supports graphical tiles! To use graphical tiles, simply provide the filename of the tile as the fifth argument to the <code class="highlighter-rouge">TETile</code> constructor. Images must be 16 x 16, and should ideally be in PNG format. There are a large number of open source tilesets available online for tile based games. Feel free to use these. Note: Your github accounts are set up to reject files other than .txt or .java files. We will not have access to your tiles when running your code. Make sure to keep your own copy of your project somewhere else other than Github if you want to keep a copy of your project with graphics for archival purposes. Graphical tiles are not required.</p>

<p>If you do not supply a filename or the file cannot be opened, then the tile engine will use the unicode character provided instead. This means that if someone else does not have the image file locally in the same location you specified, the game will still be playable but will use unicode characters instead of textures you chose.</p>

<p>The tile rendering engine relies on <code class="highlighter-rouge">StdDraw</code>. We recommend against using <code class="highlighter-rouge">StdDraw</code> commands like <code class="highlighter-rouge">setXScale</code> or <code class="highlighter-rouge">setYScale</code> unless you really know what you’re doing, as you may considerably alter or damage the a e s t h e t i c of the game otherwise.</p>

<h4 id="starting-the-game">Starting the Game</h4>

<p>Your game must support both methods of starting it, one using the <code class="highlighter-rouge">Core.Game.playWithKeyboard()</code> method, and the other using the <code class="highlighter-rouge">Core.Game.playWithInputSting(String s)</code> method.</p>

<p>When your <code class="highlighter-rouge">Core.Game.playWithKeyboard()</code> method is run, your game must display a Main Menu that provides at LEAST the option to start a new game, load a previously saved game, and quit the game. The Main Menu should be navigable only using the keyboard, using N for “new game”, L for “load game”, and Q for quit. You may include additional options.</p>

<p><img src="/materials/proj/proj2/img/mainmenu_example.png" alt="mainmenu_example" /></p>

<p>After pressing N for “new game”, the user should be prompted to enter a “random seed”, which is an integer of their choosing. This integer will be used to generate the world randomly (as described later and in lab 5). After the user has pressed the final number in their seed, they should press S to tell the game that they’ve entered the entire seed that they want.</p>

<p>The behavior of the “Load” command is described elsewhere in this specification.</p>

<p>If the game instead started with <code class="highlighter-rouge">Core.Game.playWithInputString()</code>, no menu should be displayed and nothing should be drawn to the screen. The game should otherwise process the given String as if a human player was pressing the given keys using the <code class="highlighter-rouge">Core.Game.playWithKeyboard()</code> method. For example, if we call <code class="highlighter-rouge">Core.Game.playWithInputString("N3412S")</code>, the game should generate a world with seed 3412 and return the generated 2D tile array.</p>

<p>We recommend that you do not implement <code class="highlighter-rouge">Core.Game.playWithKeyboard()</code> until you get to phase 2 of the project (interactivity), though you’re welcome to do so at anytime. It will be easier to test drive and debug your world generator by using <code class="highlighter-rouge">playWithInputString</code> instead.</p>

<p>If you want to allow the user to have additional options, e.g. the ability to pick attributes of their character, specify world generation parameters, etc., you should create additional options. For example, you might add a fourth option “S” to the main menu for “select creature and start new game” if you want the user to be able to pick what sort of creature to play as. These additional options may have arbitrary behavior of your choosing. The behavior of N, L, and Q must be exactly as described in the spec!</p>

<h3 id="phase-2-interactivity">Phase 2: Interactivity</h3>

<p>In the second phase of the game, you’ll add the ability for the user to actually play the game, and will also add user interface (UI) elements to your game to make it feel more immersive and informative.</p>

<p>The requirements for interactivity are as follows:</p>
<ul>
  <li>The player must be able to control some sort of entity that can moved around using the W, A, S, and D keys. Lab 6 covers how to include interactivity in your game.</li>
  <li>The entity must be able to interact with the world in some way.</li>
  <li>Your game must be deterministic in that the same sequence of keypresses from the same seed must result in exactly the same behavior every time. It is OK if you use a pseudorandom generator, since the <code class="highlighter-rouge">Random</code> object is guaranteed to output the same random numbers every time.</li>
</ul>

<p>Optionally, you may also include game mechanics that allow the player to win or lose (see gold points below). Aside from these feature requirements, there will be a few technical requirements for your game, described in more detail below.</p>

<h4 id="game-ui-user-interface-appearance">Game UI (User Interface) Appearance</h4>

<p>After the user has entered a seed and pressed S, the game should start. The user interface of the game must include:</p>
<ul>
  <li>A 2D grid of tiles showing the current state of the world.</li>
  <li>A “Heads Up Display” that provides additional information that maybe useful to the user. At the bare minimum, this should include Text that describes the tile currently under the mouse pointer.</li>
</ul>

<p>As an example of the bare minimum, the game below displays a grid of tiles and a HUD that displays the description of the tile under the mouse pointer (click image for higher resolution):</p>

<p><a href="img/UI_example0.png"><img src="/materials/proj/proj2/img/UI_example0.png" alt="mouseover_example1" /></a></p>

<p>You may include additional features if you choose. In the example below (click image for higher resolution), as with the previous example, the mouse cursor is currently over a wall, so the HUD displays the text “wall” in the top right. However, this game’s HUD also provides the user with 5 hearts representing the player’s “health”. Note that this game does not meet the requirements of the spec above, as it is a large erratic cavernous space, as opposed to rooms connected by hallways.</p>

<p><a href="img/UI_example1.png"><img src="/materials/proj/proj2/img/UI_example1.png" alt="mouseover_example1" /></a></p>

<p>As an example, the game below (click image for higher resolution) uses the GUI to list additional valid key presses, and provides more verbose information when the user mouses-over a tile (“You see grass-like fungus.”). The image shown below is a professional game, yours isn’t expect to look anywhere near as good.</p>

<p><a href="img/UI_example2.png"><img src="/materials/proj/proj2/img/UI_example2.png" alt="mouseover_example2" /></a></p>

<p>For information about how to specify the location of the HUD, see the <code class="highlighter-rouge">initialize(int width, int height, int xOffset, int yOffset)</code> method of <code class="highlighter-rouge">TERenderer</code> or see lab 6.</p>

<h4 id="game-ui-behavior">Game UI Behavior</h4>

<p>When the game begins, the user must be in control of some sort of entity that is displayed in the world. The user must be able to move up, left, down, and right using the W, A, S, and D keys, respectively. These keys may also do additional things, e.g. pushing objects. You may include additional keys in your game. <strong>The user may not interact with the world using mouse clicks</strong>, e.g. no clicking to allow movement.</p>

<p>The game must behave pseudorandomly. That is, given a certain seed, the same set of key presses must yield the exact same results!</p>

<p>In addition to movement keys, if the user enters “:Q”, the game should quit and save. The description of the saving (and loading) function is described in the next section. <strong>This command must immediately quit and save</strong>, and should require no further keypresses to complete, e.g. do not ask them if they are sure before quitting. We will call this single action of quitting and saving at the same time “quit/saving”.</p>

<p>This project uses <code class="highlighter-rouge">StdDraw</code> to handle user input. This results in a couple of important limitations:</p>
<ul>
  <li>Can only register key presses that result in a char. This means any unicode character will be fine but keys such as the arrow keys and escape will not work.</li>
  <li>On some computers, may not support holding down of keys without some significant modifications, i.e. you can’t hold down the e key and keep moving east. If you can figure out how to support holding down of keys in a way that is compatible with <code class="highlighter-rouge">playwithInputString</code>, you’re welcome to do so.</li>
</ul>

<p>Because of the requirement that your game must be playable from a String, your game cannot make use of real time, i.e. your game cannot have any mechanic which depends on a certain amount of time passing in real life, since that would not be captured in an input string and would not lead to deterministic behavior when using that string vs. playing at the keyboard. Keeping track of the number of turns that have elapsed is a perfectly reasonable mechanic,and might be an interesting thing to include in your game, e.g. maybe the game grows steadily darker in color with each step. You’re welcome to include other key presses like allowing the player to press space bar in order to move forwards one time step.</p>

<h4 id="saving-and-loading">Saving and Loading</h4>

<p>Sometimes, you’ll be playing a game, and you suddenly notice that it’s time to go to 61B lecture. For times like those, being able to save your progress and load it later is very handy. Your game must have the ability to save the game while playing and load the game in the exact state of the most recent save after quitting and opening the game back up.</p>

<p>Within a running Java program, we use variables to store and load values. However, to be able to quit the game (kill the program), and then load it back up, we need to have memory that’s a bit more persistent. This means you must save any information that is relevant to the state of the game concretely in a file somewhere you would be able to find when the game is opened again. The technique to accomplish this will be up to you, but we recommend looking into the Java interface <code class="highlighter-rouge">Serializable</code>, which is the easiest approach.</p>

<p>When the user restarts Game.main and presses L, the game should into exactly the same state as it was before the game was terminated. </p>
