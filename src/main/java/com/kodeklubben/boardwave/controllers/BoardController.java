package com.kodeklubben.boardwave.controllers;
import com.kodeklubben.boardwave.models.Board;
import com.kodeklubben.boardwave.models.Card;
import com.kodeklubben.boardwave.models.Column;
import com.kodeklubben.boardwave.models.User;
import com.kodeklubben.boardwave.repositories.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/")
public class BoardController {

    private User userLoggedIn;
    User user = new User();

    @Autowired
    private Repository repository;

    //for testclass
    public BoardController(Repository repository) {
    }

    // Landing page
    @GetMapping("/")
    public String landingPage(Model model){
        boolean success = submitLogin("danieljappe@gmail.com" + ";" + "minhemmeligeemail123", model);
        // If the login is successful (i.e., 'success' is true)...
        if (success) {
            // An ArrayList 'boardIds' is created to store the board IDs of the user.
            ArrayList<Integer> boardIds = new ArrayList<Integer>();
            // The system logs the currently logged-in user.
            System.out.println(userLoggedIn);
            // If the user has any boards...
            if (!userLoggedIn.getBoards().isEmpty()){
                // Their IDs are extracted by splitting the string using ';' as the delimiter.
                String[] ids = userLoggedIn.getBoards().split(";");
                // Each ID is converted from String to Integer and added to the 'boardIds' list.
                for (int i = 0; i < ids.length; i++) {
                    boardIds.add(Integer.parseInt(ids[i]));
                }
                // Boards corresponding to the 'boardIds' are retrieved from a repository (likely a database).
                ArrayList<Board> boards = repository.getBoards(boardIds);
                userLoggedIn.addBoardList(boards);
                // The 'boards' are added to the 'model' to be made available for the view (the front-end).
                model.addAttribute("boards", boards);
            } else {
                // If the user doesn't have any boards, an empty list is added to the 'model'.
                model.addAttribute("boards", new ArrayList<Board>());
            }
            // An empty 'Board' object is added to the 'model' for creating new boards.
            model.addAttribute("board", new Board("", new ArrayList<>(), -1));
            // The method returns a String representing the name of the view that should be rendered.
            // In this case, it's the user's home page.
            return "userHomePage";
        } else {
            // If the login was not successful, an error attribute is added to the 'model'.
            model.addAttribute("error", true);
            // The method returns the result of a 'loginPage' method, likely redirecting back to the login page.
            return loginPage(model);
        }
        //return "index";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "error";
    }

    // Login
    @GetMapping("/login-page")
    public String loginPage(Model model) {
        User userTemplate = new User("", "", "", -1, "");
        model.addAttribute("userTemplate", userTemplate);
        return "loginPage";
    }

    // '@GetMapping("/credentials")' is an annotation that binds the following method to HTTP GET requests on the "/credentials" endpoint.
    @GetMapping("/credentials")
    public Boolean submitLogin(@RequestParam String id, Model model){
        // The id parameter consists of two parts separated by a semicolon: the email and the password.
        // They are extracted here by splitting the id string.
        String email = id.split(";")[0];
        String password = id.split(";")[1];
        // The method 'getIDFromAuthentication' is called on the 'repository' with the provided email and password as parameters.
        // This method checks the credentials and returns the user ID if the credentials are valid, and -1 otherwise.
        int userID = repository.getIDFromAuthentication(email, password);
        // If the userID isn't -1, that means the login credentials were valid.
        if (userID != -1){
            // The user information is retrieved from the repository/database using the userID.
            user = repository.getUserFromId(userID);
            // The 'userLoggedIn' variable is updated with the currently logged in user.
            userLoggedIn = user;
            // The user's information is added to the model, which will be passed to the view.
            model.addAttribute("user", user);
            // The details of the user who just logged in are printed to the console.
            System.out.println("i /credentitals: " + user.toString()); //works
            // If the user is successfully logged in, the method returns true.
            return true;
        }
        // If the user ID is -1 (indicating invalid credentials), the method returns false.
        return false;
    }

    // The '@PostMapping("/login")' annotation is specifying that this method should be invoked to handle POST HTTP requests made to the '/login' endpoint.
    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model) {
        // A method call 'submitLogin' is being executed with user's email and password as a parameter.
        // This method validates the user's credentials. The result of the login attempt is stored in 'success'.
        boolean success = submitLogin(user.getEmail() + ";" + user.getPassword(), model);
        // If the login is successful (i.e., 'success' is true)...
        if (success) {
            // An ArrayList 'boardIds' is created to store the board IDs of the user.
            ArrayList<Integer> boardIds = new ArrayList<Integer>();
            // The system logs the currently logged-in user.
            System.out.println(userLoggedIn);
            // If the user has any boards...
            if (!userLoggedIn.getBoards().isEmpty()){
                // Their IDs are extracted by splitting the string using ';' as the delimiter.
                String[] ids = userLoggedIn.getBoards().split(";");
                // Each ID is converted from String to Integer and added to the 'boardIds' list.
                for (int i = 0; i < ids.length; i++) {
                    boardIds.add(Integer.parseInt(ids[i]));
                }
                // Boards corresponding to the 'boardIds' are retrieved from a repository (likely a database).
                ArrayList<Board> boards = repository.getBoards(boardIds);
                userLoggedIn.addBoardList(boards);
                // The 'boards' are added to the 'model' to be made available for the view (the front-end).
                model.addAttribute("boards", boards);
            } else {
                // If the user doesn't have any boards, an empty list is added to the 'model'.
                model.addAttribute("boards", new ArrayList<Board>());
            }
            // An empty 'Board' object is added to the 'model' for creating new boards.
            model.addAttribute("board", new Board("", new ArrayList<>(), -1));
            // The method returns a String representing the name of the view that should be rendered.
            // In this case, it's the user's home page.
            return "userHomePage";
        } else {
            // If the login was not successful, an error attribute is added to the 'model'.
            model.addAttribute("error", true);
            // The method returns the result of a 'loginPage' method, likely redirecting back to the login page.
            return loginPage(model);
        }
    }

    // Register
    @GetMapping("/register-page")
    public String registerPage(Model model) {
        User userTemplate = new User("", "", "", -1, "");
        model.addAttribute("userTemplate", userTemplate);
        return "registerPage";
    }

    // The '@PostMapping("/register-page")' annotation binds this method to HTTP POST requests to the "/register-page" endpoint.
    @PostMapping("/register-page")
    public String createUser(@ModelAttribute("user") User user, Model model){
        // Check if the provided email already exists in the database.
        boolean emailExists = repository.emailExists(user.getEmail());
        // If the email does not already exist, then create a new user.
        if (!emailExists) {
            // Insert the new user's details into the database.
            repository.insertNewUser(user.getName(), user.getEmail(), user.getPassword());
            // Log in the newly registered user.
            user = repository.loginWithEmailAndPassword(user.getEmail(), user.getPassword());
            // Update the 'userLoggedIn' variable to reflect the currently logged in user.
            userLoggedIn = user;
            // Print the user's details to the console.
            System.out.println(user);
            // Check if the user was successfully logged in.
            if (user.getId() != -1) {
                // If the user has any boards, retrieve them and add them to the model.
                if (!userLoggedIn.getBoards().isEmpty() && !userLoggedIn.getBoards().equals("null")){
                    ArrayList<Integer> boardIds = new ArrayList<Integer>();
                    String[] ids = userLoggedIn.getBoards().split(";");
                    for (int i = 0; i < ids.length; i++) {
                        boardIds.add(Integer.parseInt(ids[i]));
                    }
                    ArrayList<Board> boards = repository.getBoards(boardIds);
                    userLoggedIn.addBoardList(boards);
                    model.addAttribute("boards", boards);
                } else {
                    // If the user does not have any boards, add an empty list to the model.
                    model.addAttribute("boards", new ArrayList<Board>());
                }
                // Add an empty 'Board' object to the model.
                model.addAttribute("board", new Board("", new ArrayList<>(), -1));
                // Return the name of the view to be displayed - in this case, the user's home page.
                return "userHomePage";
            } else {
                // If there was an error during login, add an error attribute to the model and redirect to the registration page.
                model.addAttribute("error", true);
                return registerPage(model);
            }
        } else {
            // If the email already exists, add an error message to the model and redirect to the registration page.
            model.addAttribute("EmailError", true);
            return registerPage(model);
        }
    }


    @PostMapping("/userHomePage")
    public String submitCreateBoard(@ModelAttribute("board") Board board, Model model) {
        int newBoardId = repository.insertNewBoard(board.getTitle(), user.getId(), user.getBoards());
        userLoggedIn.addBoard(newBoardId);
        user = userLoggedIn;
        System.out.println(user);
        System.out.println(model.getAttribute("boards"));
        if (!userLoggedIn.getBoards().isEmpty()) {
            ArrayList<Integer> boardIds = new ArrayList<Integer>();
            String[] ids = userLoggedIn.getBoards().split(";");
            for (int i = 0; i < ids.length; i++) {
                boardIds.add(Integer.parseInt(ids[i]));
            }
            ArrayList<Board> boards = repository.getBoards(boardIds);
            userLoggedIn.addBoardList(boards);
            model.addAttribute("boards", boards);
        } else {
            model.addAttribute("boards", new ArrayList<Board>());
        }
        model.addAttribute("user", userLoggedIn);
        return "userHomePage";
    }


    @RequestMapping("/userHomePage")
    public String userHomePage(@ModelAttribute Board board, Model model){
        if (!userLoggedIn.getBoards().isEmpty()){
            ArrayList<Integer> boardIds = new ArrayList<Integer>();
            String[] ids = userLoggedIn.getBoards().split(";");
            for (int i = 0; i < ids.length; i++) {
                boardIds.add(Integer.parseInt(ids[i]));
            }
            ArrayList<Board> boards = repository.getBoards(boardIds);
            userLoggedIn.addBoardList(boards);

            model.addAttribute("boards", boards);
            userLoggedIn.addBoardList(boards);
        } else {
            model.addAttribute("boards", new ArrayList<Board>());
        }

        model.addAttribute("userModel", user);
        model.addAttribute("board", board);
        return "userHomePage";
    }

    @PostMapping("/editBoard")
    public String editBoard(@ModelAttribute Board board, @RequestParam("name") String name, @RequestParam("boardId") int boardId, Model model) {
        repository.editBoard(name, boardId);
        userLoggedIn = user;
        if (!userLoggedIn.getBoards().isEmpty() || !userLoggedIn.getBoards().equals("null")){
            ArrayList<Integer> boardIds = new ArrayList<Integer>();
            String[] ids = userLoggedIn.getBoards().split(";");
            for (int i = 0; i < ids.length; i++) {
                boardIds.add(Integer.parseInt(ids[i]));
            }
            ArrayList<Board> boards = repository.getBoards(boardIds);
            userLoggedIn.addBoardList(boards);
            model.addAttribute("boards", boards);
        } else {
            model.addAttribute("boards", new ArrayList<Board>());
        }
        model.addAttribute("user", userLoggedIn);
        return "userHomePage";
    }

    @PostMapping("/deleteBoard")
    public String deleteBoard(@ModelAttribute Board board, @RequestParam("boardId") int boardId, Model model) {
        user.removeBoard(boardId);
        userLoggedIn.removeBoard(boardId);
        repository.deleteBoard(boardId, user.getBoards(), user.getId());
        if (userLoggedIn == null) {
            userLoggedIn = repository.loginWithEmailAndPassword(user.getEmail(), user.getPassword());
        }
        if (!userLoggedIn.getBoards().isEmpty() || !userLoggedIn.getBoards().equals("null")){
            ArrayList<Integer> boardIds = new ArrayList<Integer>();
            String[] ids = userLoggedIn.getBoards().split(";");
            for (int i = 0; i < ids.length; i++) {
                boardIds.add(Integer.parseInt(ids[i]));
            }
            ArrayList<Board> boards = repository.getBoards(boardIds);
            userLoggedIn.addBoardList(boards);
            model.addAttribute("boards", boards);
        } else {
            model.addAttribute("boards", new ArrayList<Board>());
        }
        model.addAttribute("user", userLoggedIn);
        return "userHomePage";
    }

    // Contact
    @GetMapping("/contact-page")
    public String contactPage() {
        return "contactPage";
    }

    @GetMapping("/about-page")
    public String aboutPage() {
        return "aboutPage";
    }

    @GetMapping("/board-page")
    public String boardPage() {
        return "boardPage";
    }

    @PostMapping("/boards")
    public String createBoard() {

        return "redirect:/userHomePage";
    }

    @GetMapping("/dontPress")
    public String dontPress(){
        return "dontPress";
    }

    //-----------------Boards------------------//

    @GetMapping("/boardPage={id}")
    public String boardPage(@PathVariable("id") int id, Model model) {
        Board board = repository.getBoard(id);
        ArrayList<Column> columns = board.getColumns();
        model.addAttribute("board", board);
        model.addAttribute("user", userLoggedIn);
        model.addAttribute("card", new Card("", "", -1, -1, -1));
        model.addAttribute("column", new Column("", new ArrayList<Card>(), -1));
        return "midlertidigBoardPage";
    }

    @PostMapping("/addNewColumnToBoard")
    public String boardPageAddColumn(@ModelAttribute("column") Column column, Model model) {
        int columnId = repository.insertNewColumn(column.getTitle(), column.getBoardId());
        System.out.println("column id efter oprettelse: " + column.getId());
        System.out.println("column id erstattes med: " + columnId);
        System.out.println("BoardID = " + column.getBoardId());
        System.out.println("ColumnID = " + columnId);
        column.setId(columnId);
        userLoggedIn.getBoardFromId(column.getBoardId()).addColumn(column);
        Board board = userLoggedIn.getBoardFromId(column.getBoardId());

        model.addAttribute("board", board);
        model.addAttribute("user", userLoggedIn);
        model.addAttribute("card", new Card("", "", -1, -1, -1));
        model.addAttribute("column", new Column("", new ArrayList<Card>(), -1));

        return "midlertidigBoardPage";
    }

    @PostMapping("/addNewCardToColumn")
    public String AddCard(@ModelAttribute("card") Card card, Model model) {
        int cardId = repository.insertNewCard(card.getTitle(), card.getDescription(), card.getMinutesEstimated(), card.getHourlyRate(), card.getColumnId(), card.getBoardId());
        card.setId(cardId);
        Board board = userLoggedIn.getBoardFromId(card.getBoardId());
        System.out.println("BoardID = " + board.getId());
        System.out.println("ColumnID = " + card.getColumnId());
        System.out.println("column title = " + board.getColumnFromId(card.getColumnId()));

        Column column = board.getColumnFromId(card.getColumnId());
        column.addCard(card);

        model.addAttribute("board", board);
        model.addAttribute("user", userLoggedIn);
        model.addAttribute("card", new Card("", "", -1, -1, -1));
        model.addAttribute("column", new Column("", new ArrayList<Card>(), -1));
        return "midlertidigBoardPage";
    }

    @PostMapping("/deleteCard")
    public String removeCard(@RequestParam("cardId") int cardId, @RequestParam("boardId") int boardId, Model model) {
        // Get the board
        Board board = userLoggedIn.getBoardFromId(boardId);
        if (board == null) {
            // Handle error: no board found with the provided id
            return "error";
        }

        // Get the column containing the card
        Column column = board.getColumnFromCardId(cardId);
        if (column == null) {
            // Handle error: no column found containing a card with the provided id
            return "error";
        }

        // Remove the card
        column.deleteCard(cardId);

        // Remove card from database
        repository.deleteCard(cardId);

        // Refresh model attributes
        model.addAttribute("board", board);
        model.addAttribute("user", userLoggedIn);
        model.addAttribute("card", new Card("", "", -1, -1, -1));
        model.addAttribute("column", new Column("", new ArrayList<Card>(), -1));

        // Redirect back to the board page
        return "midlertidigBoardPage";
    }

    @PostMapping("/moveCard")
    public String moveCard(@RequestParam int cardId, @RequestParam int columnId, @RequestParam int boardId, Model model) {
        System.out.println("boardId: " + boardId);
        System.out.println("columnId: " + columnId);
        System.out.println("cardId: " + cardId);
        //move card in database
        repository.moveCardToColumn(cardId, columnId);

        //get new board
        Board board = repository.getBoard(boardId);

        // Assuming you have userLoggedIn available here
        model.addAttribute("board", board);
        model.addAttribute("user", userLoggedIn);
        model.addAttribute("card", new Card("", "", -1, -1, -1));
        model.addAttribute("column", new Column("", new ArrayList<Card>(), -1));

        // Additional code to update model and return view...
        return "midlertidigBoardPage";
    }

    @PostMapping("/deleteColumn")
    public String removeColumn(@RequestParam("columnId") int columnId, @RequestParam("boardId") int boardId, Model model) {
        // Get the board
        Board board = userLoggedIn.getBoardFromId(boardId);
        if (board == null) {
            // Handle error: no board found with the provided id
            return "error";
        }

        // Remove the column
        board.removeColumn(columnId);

        // Remove column from database
        repository.deleteColumn(columnId);

        // Refresh model attributes
        model.addAttribute("board", board);
        model.addAttribute("user", userLoggedIn);
        model.addAttribute("card", new Card("", "", -1, -1, -1));
        model.addAttribute("column", new Column("", new ArrayList<Card>(), -1));

        // Redirect back to the board page
        return "midlertidigBoardPage";
    }

    @PostMapping("/addDescription")
    public String addDescription(@RequestParam("cardId") int cardId, @RequestParam("description") String description, @RequestParam("boardId") int boardId, Model model) {
        // Get the board
        Board board = userLoggedIn.getBoardFromId(boardId);
        if (board == null) {
            // Handle error: no board found with the provided id
            return "error";
        }

        // Get the card
        Card card = board.getCardFromId(cardId);
        if (card == null) {
            // Handle error: no card found with the provided id
            return "error";
        }

        // Update the card
        card.setDescription(description);

        // Update card in database
        //todo

        //repository.updateCard(card);

        // Refresh model attributes
        model.addAttribute("board", board);
        model.addAttribute("user", userLoggedIn);
        model.addAttribute("card", new Card("", "", -1, -1, -1));
        model.addAttribute("column", new Column("", new ArrayList<Card>(), -1));

        // Redirect back to the board page
        return "midlertidigBoardPage";
    }


}