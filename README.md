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

1. Install SDKMAN and Java 22:
   ```bash
   curl -s "https://get.sdkman.io" | bash
   sdk install java 22.0.2-amzn

2. Please open a new terminal, or run the following in the existing one:

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
