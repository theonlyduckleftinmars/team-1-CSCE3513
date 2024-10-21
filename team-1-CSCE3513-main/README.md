# Team 1 Laser Tag Program - UARK

## Team Members
| GitHub Username                  | Real Name                | Trello Username   |
|----------------------------------|-------------------------|-------------------|
| BriceStegall                     | Brice Stegall           | bs0911            |
| Claire258                        | Claire Oliver           | claireoliver22    |
| theonlyduckleftinmars            | Jose Eduardo Hernandez  | edjh22            |
| JacksonFinger                    | Jackson Finger          | jackson27814400    |
| Will-Taylor08                    | Will Taylor             | wmt001            |

## How to Run the Application
1. Make sure javac is installed!
   Install via `apt` (if available):
   Update the package index and install java version 22.0.2
   ```bash
   sudo apt update
   sudo apt install openjdk-22-jdk
2. Verify with javac -version
   ```bash
   javac -version

This should output the installed version of java

3. If you do not have apt, manually installing Java 22 via SDKMAN is also possible and recommended for new releases. SDKMAN is a tool for managing parallel versions of multiple Software Development Kits.
Install SDKMAN and Java 22:
   ```bash
   curl -s "https://get.sdkman.io" | bash
   sdk install java 22.0.2-open

4. If java 22 is not available, you can also check the available versions with:

   ```bash
   sdk list java

5. Make sure you set the default java version and verify:

   ```bash
   sdk default java 22.0.2-open
   java -version
   
6. After cloning the repository, from the `team-1` directory, make the `run.bash` executable:
   ```bash
   chmod +x run.bash
7. Start the program by typing:
   ```bash
   ./run.bash
