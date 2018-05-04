package Project2;



import Project2.Controllers.BoardCafe;
import Project2.Models.Validator;
import Project2.Models.User;
import Project2.Repos.ListDependent.BookedGameRepository;
import Project2.Repos.ListDependent.GameRepository;
import Project2.Repos.ListDependent.LoggedUserRepository;
import Project2.Repos.ListDependent.UserRepository;

import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;
import java.util.Scanner;

public class App {
    public static String databasePath = "src/main/resources/database.csv";

    public static void main( String[] args ) throws IOException, ParseException {
        BoardCafe myCafe = new BoardCafe(new GameRepository(), new BookedGameRepository(), new UserRepository(), new LoggedUserRepository(), new Validator());
        myCafe.LoadDatabase(databasePath);

        System.out.println("Hello! You are using reservation service for 'Board Cafe' gameshop");
        menu(null, myCafe);
    }

    public static void menu(User user, BoardCafe bc) throws IOException, ParseException {
        boolean takeAction = true;
        String input, login, password, gameName, sDate, sTime, sMoney;
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);;

        while(takeAction) {
            if (!bc.UserIsLoggedIn(user)) {
                System.out.println("\nCURRENTLY NOT LOGGED IN" + "\n");
                System.out.println("Menu\n0. Our games list\n1. Register\n2. Log in\nQ. Quit\n");
                System.out.println("What would you want to do?");
                input = scanner.nextLine();

                switch (input) {
                    case ("0"):
                        System.out.println("Available games:"); System.out.println(bc.ShowGamesList());
                        break;
                    case ("1"):
                        System.out.println("Please enter login:"); login = scanner.nextLine();
                        System.out.println("Please enter password:"); password = scanner.nextLine();
                        try {
                            bc.Register(login, password);
                            break;
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                        break;
                    case("2"):
                        System.out.println("Please enter login:"); login = scanner.nextLine();
                        System.out.println("Please enter password:"); password = scanner.nextLine();
                        try {
                            bc.LogIn(login, password);
                            user = bc.FindLoggedUser(login);
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                        break;
                    case ("Q"):
                    case("q"):
                        System.out.println("Thanks for gaming with us!");
                        takeAction = false;
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
            } else {
                System.out.println("\nCURRENTLY LOGGED IN AS " + user.getUsername() + "\n");
                System.out.println("Menu\n0. Our games list\n1. Your games list\n2. Book a game\n3. Add money\n4. Log out\nQ. Quit\n");
                System.out.println("What would you want to do?");
                input = scanner.nextLine();

                switch (input) {
                    case ("0"):
                        System.out.println("Available games:"); System.out.println(bc.ShowGamesList());
                        break;
                    case ("1"):
                        System.out.println("Your games list:");
                        bc.ShowUsersBookedGamesList(user.getUsername());
                        break;
                    case ("2"):
                        System.out.println("Available games:"); System.out.println(bc.ShowGamesList());
                        System.out.println("What would you want to book? Please enter a name:"); gameName = scanner.nextLine();
                        try {
                            if (bc.GameExists(gameName)) {
                                System.out.println("Please enter date when would you want to book a game [dd/MM/yyyy]:"); sDate = scanner.nextLine();
                                System.out.println("Please enter time when would you want to book a game:"); sTime = scanner.nextLine();
                                bc.UserBookGame(user, gameName, sDate, sTime);
                                System.out.println("Your games list:"); bc.ShowUsersBookedGamesList(user.getUsername());
                            } else {
                                System.out.println("Sorry, we don't own such a game!");
                            }
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                        break;
                    case("3"):
                        user.ShowAccountBalance();
                        System.out.println("Please enter amount you would want to add:"); sMoney = scanner.nextLine();
                        try {
                            bc.AddMoneyToUsersAccount(user.getUsername(), sMoney);
                            //user.AddMoney(sMoney);
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                        user.ShowAccountBalance();
                        break;
                    case ("4"):
                        try {
                            bc.LogOut(user);
                            user = null;
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                        break;
                    case ("Q"):
                    case("q"):
                        try {
                            bc.LogOut(user);
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                        System.out.println("Thanks for gaming with us!");
                        takeAction = false;
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
            }
        }
    }
}
