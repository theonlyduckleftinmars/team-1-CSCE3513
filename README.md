# Team 1 Laser Tag Program - UARK

## Team Members
| GitHub Username                  | Real Name                | Trello Username   |
|----------------------------------|-------------------------|-------------------|
| BriceStegall                     | Brice Stegall           | bs0911            |
| Claire258                        | Claire Oliver           | claireoliver22    |
| theonlyduckleftinmars            | Jose Eduardo Hernandez  | edjh22            |
| JacksonFinger                    | Jackson Finger          | jackson27814400    |
| Will-Taylor08                    | Will Taylor             | wmt001            |

## Install Git LFS (for game sounds to work)!

1. Install Git LFS:
   For macOS:
   ```bash
   brew install git-lfs
   ```
   For Linux:
   ```bash
   sudo apt install git-lfs
2. Initialize Git LFS: In your repository (after cloning), run:
   ```bash
   git lfs install
3. Fetch the actual files. (fetches .wav file):
   ```bash
   git lfs pull

## How to Run the Application

1. Install SDKMAN and Java 22:
   ```bash
   curl -s "https://get.sdkman.io" | bash
   sdk install java 22.0.2-amzn

2. Please open a new terminal, or run the following in the existing one, use your username for local username (obviously):

   ```bash
    source "/Users/<local username>/.sdkman/bin/sdkman-init.sh"

2. If java 22 is not available, you can also check the available versions with:

   ```bash
   sdk list java

3. Make sure you set the default java version and verify:

   ```bash
   sdk default java 22.0.2-amzn
   javac -version
   
4. After cloning the repository, from the `team-1` directory, make the `run.bash` executable:
   ```bash
   chmod +x run.bash
5. Start the program by typing:
   ```bash
   ./run.bash

## To simulate traffic between players (From initial directory):

1. Compile TrafficGenerator.java:
   ```bash
   javac network.TrafficGenerator.java
2. Run TrafficGenerator.java:
   ```bash
   java network.TrafficGenerator.java
