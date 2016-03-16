/* 
	File Name: Game2048.java
	Name: Lily Wang
	Class: ICS3U1-02
	Date: June 10, 2014
	Description: This program contains several completed methods to be used in the game 2048. Methods created in this program 
	are newSlot, play, newSlot, slide, combine, checkGameOver. At the beginning,it sets the current score as 0 and sets
	the current level as 2, and generates two number tiles. Then it slides and combines the number tiles when the user presses
	any of the direction keys. It updates the score and checks if the move is legal before generating a new number tile. 
	The game ends when the current level reaches the winning level (game won) or there is no legal moves (game over). 	
*/
   
   import java.lang.Math;
   import java.io.*;

    public class Game2048 
   {
   //=== *** Provided class variables (Don't delete this section) *** ===//
      final public static int LEFT_INPUT 	= 0;
      final public static int DOWN_INPUT 	= 1;
      final public static int RIGHT_INPUT = 2;
      final public static int UP_INPUT 	= 3;
   
      final int ROW=4;
      final int COL=4;
   
      final public static int VALUE_GRID_SETTING 	= 0;
      final public static int INDEX_GRID_SETTING	= 1;
   
      private String GAME_CONFIG_FILE = "game_config.txt";
   
      private Game2048GUI gui;
   
   /* position [0][0] represents the Top-Left corner and
   * position [max][max] represents the Bottom-Right corner */
      private int grid [][];
   
   //=== *** Your class variables can be added starting here *** ===//
      private final int EMPTY_SLOT = -1;
   
      private int winningLevel;  
      private long currentScore;
      private int currentLevel;
   	
   /*
   **create 2 public booleans
   */
   //check legal move (validate)
      public boolean validate;
   //check game over (lose)   
      public boolean lose;
   
   /**
   * Constructs Game2048 object.
   *
   * @param gameGUI	The GUI object that will be used by this class.
   */   
       public Game2048(Game2048GUI gameGUI)
      {
         gui = gameGUI;
      
      // - create and initialize the grid array
         grid=new int[ROW][COL];
      
      //fill in the grid array with empty slot.
         for (int i=0;i<ROW;i++){
            for (int j=0;j<COL;j++){
               grid[i][j]= EMPTY_SLOT;
            }
         }
      
      // - initialize the variables 
      //		- winningLevel (value read from text file)
         try{
         //create a new bufferedreader to read in from the file.
            BufferedReader in=new BufferedReader (new FileReader(GAME_CONFIG_FILE));
         
         //ignore the first line
            in.readLine();
         
         //the second line is winning level. (cast it into an int)
            winningLevel=Integer.parseInt(in.readLine());
         
         //close it
            in.close();
         }
             catch(IOException iox){
             //Error message
               System.out.println("Error");
            }
      
      //		- set currentLevel
         currentLevel=2;
      
      //		- set currentScore
         currentScore=0;
      
      //Insert the first TWO number tiles at the beginning (initialze validate as true) 
         validate=true;
         newSlot();
         newSlot();
      }
   
   
   /**
   * Place a new number tile on a random slot on the grid.
   * This method is called every time a key is released.
   */	
   
   /*newSlot method
   **parammeter: no
   **return: nothing
   **generate a new tile
   */ 
       public void newSlot()
      {
      //validate
         if (validate){	    
         //randomNum1, randomNum2: index of row, column
            int randomNum1,randomNum2;
         //possibility: a random number generated to decide if it is going to output 2 or 4   
            int possibility;
         //gen: a boolean to decide if it is going to generate another set of random numbers  
            boolean gen=true;
         //the chance to generate 4 is 10% (1 out of 10)
            final int CHANCE=1;
         //num: an int to store the value of number tile generated  
            int num;
         
         // Make sure to check if a new tile should be inserted first
         // if there is a legal move (boolean validate is true), insert a new number tile on the grid
            while(gen){            
            //Generate two random int between 0 and 3
               randomNum1 = (int)(Math.random()*4);
               randomNum2 = (int)(Math.random()*4);
            
               if (grid[randomNum1][randomNum2] == EMPTY_SLOT){
                  gen=false;
               
               /*check the possibility to generate 4 or 2
               */
                  //Generate a random int between 1 and 10 to represent the possibility
                  possibility=(int)(Math.random()*10)+1;
               
                  if (possibility <= CHANCE){
                  //generate a new tile with value 4 - 10% (1 out of 10)
                     grid[randomNum1][randomNum2]=4;
                  //update num
                     num=4;
                  }
                  else {
                  //generate a new tile with value 2 - 90% (9 out of 10)
                     grid[randomNum1][randomNum2]=2;
                  //update num   
                     num=2;
                  }
               //generate a new number tile with a red square
                  gui.setNewSlotBySlotValue(randomNum1,randomNum2,num);
               }
            }
         
         }
      }
   
   /**
   * Plays the game by the direction specified by the user.     
   * This method is called every time a button is pressed
   */	
   
   /*play method
   **parammeter: int direction
   **return: nothing
   **play the game by calling several methods
   */ 	
       public void play(int direction)
      {
      // implement the action to be taken after an arrow key of the specified direction is pressed.
      
      //reset boolean validate
         validate=false;
      
      //reset boolean lose
         lose=true;
      
      //slide
         slide(direction); 
      
      //combine
         combine(direction);
      
      //slide
         slide(direction);
      
      //update score
         gui.setScore(currentScore);
      
      //set screen
         gui.setGridByValue(grid);  
      
      //show game won
         if (currentLevel == winningLevel){
         //if current level reaches the winning leverl, show game won
            gui.showGameWon();
         }
      
      //show game over
         //call checkGameOver
         checkGameOver();
         if (lose){
         //if lose is true, show game over
            gui.showGameOver();
         }
      }  
   
   /*slide method
   **parammeter: int direction
   **return: nothing
   **slide the tiles when it is moved in different directionschecks. It also checks if the move is valid and updates
   **the boolean validate(check if it is slidable only)
   */        
       public void slide(int direction){
      
      //slide up
         if (direction == UP_INPUT){
            for (int j=0;j<COL;j++){         
               for (int i=1;i<ROW;i++){
                  if (grid[i-1][j] == EMPTY_SLOT && grid[i][j] != EMPTY_SLOT){
                     for (int m=0;m<i;m++){
                        if (grid[m][j] == EMPTY_SLOT){
                        //if the grid is not empty and the one above is empty, move the tile up
                           grid[m][j] = grid [i][j];
                        
                        //validate move
                           validate=true;
                        
                        //this grid is empty.  
                           grid[i][j]=EMPTY_SLOT;
                        }
                     }    
                  }
               }
            } 
         }
         
         //slide down
         else if (direction == DOWN_INPUT){
            for (int j=0;j<COL;j++){
               for (int i=ROW-2;i>=0;i--){
                  if (grid[i+1][j] == EMPTY_SLOT && grid[i][j] != EMPTY_SLOT){
                     for (int m=ROW-1;m>=i;m--){
                        if (grid[m][j] == EMPTY_SLOT){
                        //if the grid is not empty and the one below is empty, move the tile down
                           grid[m][j] = grid [i][j];
                        
                        //validate move
                           validate=true;
                        
                        //this grid is empty.  
                           grid[i][j]=EMPTY_SLOT;
                        }
                     }
                  }
               }
            }
         }
         
         //slide left
         else if (direction == LEFT_INPUT){
            for (int i=0;i<ROW;i++){        
               for (int j=1;j<COL;j++){
                  if (grid[i][j-1] == EMPTY_SLOT && grid[i][j] != EMPTY_SLOT){
                     for (int m=0;m<j;m++){
                        if (grid[i][m] == EMPTY_SLOT){
                        //if the grid is not empty and the one above is empty, move the tile up
                           grid[i][m] = grid [i][j];
                        
                        //validate move
                           validate=true;
                        
                        //this grid is empty.  
                           grid[i][j]=EMPTY_SLOT;
                        }
                     }    
                  }
               }
            }
         }
         
         //slide right
         else if (direction == RIGHT_INPUT){
            for (int i=0;i<ROW;i++){
               for (int j=COL-2;j>=0;j--){
                  if (grid[i][j+1] == EMPTY_SLOT && grid[i][j] != EMPTY_SLOT){
                     for (int m=COL-1;m>=j;m--){
                        if (grid[i][m] == EMPTY_SLOT){
                        //if the grid is not empty and the one below is empty, move the tile down
                           grid[i][m] = grid [i][j];
                        
                        //validate move
                           validate=true;
                        
                        //this grid is empty.  
                           grid[i][j]=EMPTY_SLOT;
                        }
                     }
                  }
               }
            }
         }  
      }
   
   /*
   **combine method
   **parammeter: int direction
   **return: nothing
   **combine the tiles when it is moved in different directions. It also updates the score and the current level, and checks
   **if the move is valid and updates the boolean validate(check if it is combinable only)
   */ 
       public void combine (int direction){
      
      //slide up
         if (direction == UP_INPUT){
            for (int j=0;j<COL;j++){
               for (int i=1;i<ROW;i++){
                  if (grid[i][j]==grid[i-1][j] && grid[i][j] != EMPTY_SLOT){
                  //combine them
                     grid[i-1][j]+=grid[i][j];
                  
                  //update score
                     currentScore+=grid[i-1][j];
                  
                  //update current level
                     if (grid[i-1][j]>currentLevel){
                        currentLevel=grid[i-1][j];
                     }
                  
                  //validate combine
                     validate=true;
                  
                  //this grid is empty.   
                     grid[i][j]=EMPTY_SLOT;
                  }
               }  
            }
         }
         
         //slide down
         else if (direction == DOWN_INPUT){
            for (int j=0;j<COL;j++){
               for (int i=ROW-2;i>=0;i--){
                  if (grid[i][j]==grid[i+1][j] && grid[i][j] != EMPTY_SLOT){
                  //combine them
                     grid[i+1][j]+=grid[i][j];
                  
                  //update score
                     currentScore+=grid[i+1][j];
                  
                  //update current level
                     if(grid[i+1][j]>currentLevel){
                        currentLevel=grid[i+1][j];
                     }
                  
                  //validate combine
                     validate=true;
                  
                  //this grid is empty.   
                     grid[i][j]=EMPTY_SLOT;
                  }
               }
            }
         }
         
         //slide left
         else if (direction == LEFT_INPUT){
            for (int i=0;i<ROW;i++){          
               for (int j=1;j<COL;j++){
                  if (grid[i][j]==grid[i][j-1] && grid[i][j] != EMPTY_SLOT){
                  //combine them
                     grid[i][j-1]+=grid[i][j];
                  
                  //update score
                     currentScore+=grid[i][j-1];
                  
                  //update current level
                     if(grid[i][j-1]>currentLevel){
                        currentLevel=grid[i][j-1];
                     }
                  
                  //validate combine
                     validate=true;
                  
                  //this grid is empty.   
                     grid[i][j]=EMPTY_SLOT;
                  }
               } 
            }
         }
         
         //slide right
         else if (direction == RIGHT_INPUT){
            for (int i=0;i<ROW;i++){   
               for (int j=COL-2;j>=0;j--){
                  if (grid[i][j]==grid[i][j+1] && grid[i][j] != EMPTY_SLOT){
                  //combine them
                     grid[i][j+1]+=grid[i][j];
                  
                  //update score
                     currentScore+=grid[i][j+1];
                  
                  //update current level
                     if(grid[i][j+1]>currentLevel){
                        currentLevel=grid[i][j+1];
                     }
                  
                  //validate combine
                     validate=true;
                  
                  //this grid is empty.   
                     grid[i][j]=EMPTY_SLOT;
                  }
               } 
            }
         }
      }
   
   /*checkGameOver method
   **parammeter: no
   **return: nothing
   **check if the game is over: when there are no empty spaces and no adjacent tiles with the same value
   */ 
       public void checkGameOver(){
      
      //check empty slot
         for (int i=0;i<ROW;i++){   
            for (int j=0;j<COL;j++){
               if (grid[i][j]==EMPTY_SLOT){
               //if there are still some empty spaces remained, the game is not over yet
                  lose=false;
               }
            }
         } 
      
      //check combinable
         for (int i=1;i<ROW-1;i++){ 
         //check combinable in the same column  
            for (int j=0;j<COL;j++){
               if (grid[i][j]==grid[i-1][j] || grid[i][j]==grid[i+1][j]){
               //the game is not over if there are still adjacent tiles in the same column with the same value
                  lose=false;
               }
            }
         }
      
         for (int i=0;i<ROW;i++){
         //check combinable in the same row
            for (int j=1;j<COL-1;j++){
               if(grid[i][j]==grid[i][j-1] || grid[i][j]==grid[i][j+1]){
               //the game is not over if there are still adjacent tiles in the same row with the same value
                  lose=false;
               }
            }
         } 
      }
      
   }