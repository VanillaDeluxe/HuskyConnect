package userinterface;

import actors.User;
import javafx.application.Application;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;

import static modules.HomePage.loadHomePage;


public class GUI extends Application {
    public BorderPane rootPane;
    public static User loggedInUser = null;
    public static final int DEFAULT_SPACING = 10;
    public static User huskyConnectUser = null;
    public static void start() {
        launch();
    }
    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        huskyConnectUser =  new User("HuskyConnect", "hc", "The official HuskyConnect account!", loadImageResource("\\src\\main\\resources\\husky-connect-user-img.jpg"), this,"huskyconnect", "mtu", "huskies");
        Scene scene = new Scene(createRootPane());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Husky Connect");
        primaryStage.show();
    }

    /**
     * Creates the main pane, which everything else is put in
     * @return the root pane
     */
    private Pane createRootPane() {
        rootPane = new BorderPane();
        rootPane.setPrefSize(900, 600);
        rootPane.setPadding(new Insets(DEFAULT_SPACING));

        Pane loginPage = createLoginPage();

        // set the landing page to the login page
        rootPane.setCenter(loginPage);

        return rootPane;
    }

    /**
     * Create the main login page for the application
     * @return the VBox containing the login page
     */
    private VBox createLoginPage() {
        VBox loginPage = new VBox();

        // create the image and place it into a view
        ImageView logoView = new ImageView(
                loadImageResource("\\src\\main\\resources\\temp-logo.png")
        );

        VBox imageBox = new VBox(logoView);

        // text fields for user data entry
        Label usernameText = new Label("Username");
        TextField username = new TextField();
        username.setPromptText("username");
        VBox usernameBox = new VBox(usernameText, username);

        Label passwordText = new Label("Password");
        PasswordField password = new PasswordField();
        password.setPromptText("password");
        VBox passwordBox = new VBox(passwordText, password);

        VBox userPassContainer = new VBox(usernameBox, passwordBox);

        // Buttons defining behavior
        Button logInButton = new Button("Log in");
        Button signUpButton = new Button ("Sign up");

        HBox buttonContainer = new HBox(logInButton, signUpButton);

        // add both to the main log in page
        loginPage.getChildren().addAll(logoView, userPassContainer, buttonContainer);

        // add the correct spacing for all three pages, so all elements will be affected
        loginPage.setSpacing(DEFAULT_SPACING);
        userPassContainer.setSpacing(DEFAULT_SPACING);

        // Set the max width of the login page to 1/2 of the root pane, so that it will scale with
        // the window and also be centered.
        loginPage.maxWidthProperty().bind(rootPane.widthProperty().divide(2));
        // Set the max height property to the button container and user-pass container
        // so that it does not automatically bind its height to the entirety of the page
        loginPage.maxHeightProperty().bind(userPassContainer.heightProperty().add(buttonContainer.heightProperty()));

        // set the button container's max width property to be half of the login page, so the spacing
        // is roughly equivalent to what was described in the figma board
        buttonContainer.maxWidthProperty().bind(loginPage.widthProperty().divide(2));
        loginPage.setAlignment(Pos.CENTER); // align the login page and all items within it to the center
        // set each button to about a third of the button container's width, RE figma and general aesthetic
        logInButton.minWidthProperty().bind(buttonContainer.widthProperty().divide(3));
        signUpButton.minWidthProperty().bind(buttonContainer.widthProperty().divide(3));
        // set the spacing to this width as well
        buttonContainer.spacingProperty().bind(loginPage.widthProperty().divide(6));

        // set the logo to the login page width and preserve the ratio
        logoView.preserveRatioProperty().setValue(true);
        logoView.fitWidthProperty().bind(buttonContainer.widthProperty());
        // lock the image box width onto the logo
        imageBox.maxWidthProperty().bind(logoView.fitWidthProperty());


        // BUTTON FUNCTIONALITY
        logInButton.setOnAction(event -> {
            // TODO
            loggedInUser = new User("testUser", "test@testuser.com", "I like to test things :)", loadImageResource("default-user-icon.png"), this, "testing", "making things work!");
            loggedInUser.addConnection(new User("a", "b@gmail.com", "cdefghi", null, this, "#j", "#k", "#l"));
            loggedInUser.addConnection(new User("b", "b@gmail.com", "cdefghi", null, this, "#j", "#k", "#l"));
            loggedInUser.addConnection(new User("c", "b@gmail.com", "cdefghi", null, this, "#j", "#k", "#l"));
            loggedInUser.addConnection(new User("d", "b@gmail.com", "cdefghi", null, this, "#j", "#k", "#l"));
            loggedInUser.addConnection(new User("e", "b@gmail.com", "cdefghi", null, this, "#j", "#k", "#l"));
            loggedInUser.addConnection(new User("f", "b@gmail.com", "cdefghi", null, this, "#j", "#k", "#l"));

            rootPane.setCenter(loadHomePage(this));
        });

        signUpButton.setOnAction(event -> rootPane.setCenter(createSignUpPageAccountDetail(null, null)));

        return loginPage;
    }

    private VBox createSignUpPageAccountDetail(String usernameFill, String emailFill) {
        VBox signUpPage = new VBox();

        ImageView logo = new ImageView(
                loadImageResource("\\src\\main\\resources\\temp-logo.png")
        );

        VBox imageBox = new VBox(logo);

        // text fields for user data entry
        Label emailText = new Label("Email");
        TextField email;
        if (emailFill != null) {
            email = new TextField(emailFill);
        } else {
            email = new TextField();
            email.setPromptText("email");

        }
        VBox emailBox = new VBox(emailText, email);

        TextField username;
        if (usernameFill != null) {
            username = new TextField(usernameFill);
        } else {
            username = new TextField();
            username.setPromptText("username");
        }
        Label usernameText = new Label("Username");
        VBox usernameBox = new VBox(usernameText, username);

        Label passwordText = new Label("Password");
        PasswordField password = new PasswordField();
        password.setPromptText("password");
        VBox passwordBox = new VBox(passwordText, password);

        Label confirmText = new Label("Confirm Password");
        PasswordField passwordConfirm = new PasswordField();
        passwordConfirm.setPromptText("confirm password");
        VBox confirmBox = new VBox(confirmText, passwordConfirm);

        VBox fieldContainers = new VBox(emailBox, usernameBox, passwordBox, confirmBox);

        Button back = new Button ("Back");
        Button next = new Button("Next");
        HBox buttonBox = new HBox(back, next);

        signUpPage.getChildren().addAll(logo, fieldContainers, buttonBox);

        // STYLING COMPONENTS
        signUpPage.setSpacing(DEFAULT_SPACING);
        fieldContainers.setSpacing(DEFAULT_SPACING);

        signUpPage.maxWidthProperty().bind(rootPane.widthProperty().divide(2));
        signUpPage.maxHeightProperty().bind(fieldContainers.heightProperty().add(next.heightProperty()));

        buttonBox.maxWidthProperty().bind(signUpPage.widthProperty().divide(2));
        next.minWidthProperty().bind(buttonBox.widthProperty().divide(3));
        back.minWidthProperty().bind(buttonBox.widthProperty().divide(3));
        buttonBox.spacingProperty().bind(signUpPage.widthProperty().divide(6));
        signUpPage.setAlignment(Pos.CENTER);

        logo.preserveRatioProperty().setValue(true);
        logo.fitWidthProperty().bind(next.widthProperty().multiply(3));
        imageBox.maxWidthProperty().bind(logo.fitWidthProperty());

        // BUTTON FUNCTIONALITY
        next.setOnAction(event -> rootPane.setCenter(createSignUpPageProfileDetail(username.getText(), email.getText())));

        back.setOnAction(event -> rootPane.setCenter(createLoginPage()));
        return signUpPage;
    }

    private HBox createSignUpPageProfileDetail(String username, String email) {
        HBox profileDetails = new HBox();

        Image userIcon = loadImageResource("\\src\\main\\resources\\upload-user-icon.png");

        Circle clip = new Circle(1, 1, 1);

        ImageView holding = new ImageView(
                loadImageResource("\\src\\main\\resources\\default-user-icon.png")
        ); // hold the image so it can be modified within a lambda

        clip.setFill(new ImagePattern(userIcon));
        VBox logoBox = new VBox(clip);

        //text fields for user data entry
        VBox textFields = new VBox();

        Label tagsText = new Label("What're you interested in?");
        TextField tags = new TextField();
        tags.setPromptText("Prefix your tags with a # and start typing.");
        // TextFields.bindAutoCompletion(tags, "#computer science", "#huskies", "#TSP");

        VBox tagsBox = new VBox(tagsText, tags);
        textFields.getChildren().add(tagsBox);

        Label bioText = new Label("Bio");
        TextField bio = new TextField();
        bio.setPromptText("Tell us about yourself");

        VBox bioBox = new VBox(bioText, bio);
        textFields.getChildren().add(bioBox);

        Button signUp = new Button("Sign up!");
        Button back = new Button("Back");
        HBox buttonBox = new HBox(back, signUp);
        textFields.getChildren().add(buttonBox);
        profileDetails.getChildren().addAll(logoBox, textFields);

        // STYLING COMPONENTS
        // ensure the clip circle does not grow excessively large
        clip.radiusProperty().bind(logoBox.prefWidthProperty().divide(2));
        logoBox.prefWidthProperty().bind(rootPane.widthProperty().divide(10));

        // set the spacing for the required panes to default
        profileDetails.setSpacing(DEFAULT_SPACING);
        textFields.setSpacing(DEFAULT_SPACING);
        buttonBox.setSpacing(DEFAULT_SPACING);

        // allocate the correct amount of room to the panes
        profileDetails.maxWidthProperty().bind(rootPane.widthProperty());
        profileDetails.maxHeightProperty().bind(bioBox.maxHeightProperty().add(tagsBox.maxHeightProperty()));

        profileDetails.setAlignment(Pos.CENTER);
        textFields.setAlignment(Pos.CENTER);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.maxWidthProperty().bind(textFields.widthProperty());

        signUp.minWidthProperty().bind(buttonBox.widthProperty().divide(3));
        back.minWidthProperty().bind(buttonBox.widthProperty().divide(3));
        buttonBox.spacingProperty().bind(textFields.widthProperty().divide(6));

        textFields.prefWidthProperty().bind(profileDetails.widthProperty().divide(3));
        logoBox.prefWidthProperty().bind(profileDetails.widthProperty().divide(6));

        bio.minHeightProperty().bind(rootPane.prefHeightProperty().divide(8));
        tags.minHeightProperty().bind(rootPane.prefHeightProperty().divide(8));

        // BUTTON FUNCTIONALITY
        // having problems with set on mouse clicked timing -- double click treated as two clicks
        logoBox.setOnMouseClicked(event -> {
            // create a file selection dialog.
            // (This is a swing element, because the JavaFX one doesn't fit the needs of the UI)
            JFileChooser fileChooser = new JFileChooser();
            Image error = loadImageResource("\\src\\main\\resources\\error-user-icon.png");
            // if the file chooser correctly opens
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile(); // retrieve the user selected file
                Image logo = new Image(file.toURI().toString()); // turn it into an image

                // ensure that the file was properly turned into an image (acceptable file format)
                if (logo.isError()) {
                    // if it wasn't, show the error image logo
                    clip.setFill(new ImagePattern(error));
                } else {
                    // if it was, show the file they selected as the icon
                    holding.setImage(logo);
                    clip.setFill(new ImagePattern(logo));
                }
            } else {
                // if the file chooser did not open, show the error image
                clip.setFill(new ImagePattern(error));
            }
        });

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                rootPane.setCenter(createSignUpPageAccountDetail(username, email));
            }
        });

        GUI guiInstance = this;
        signUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String[] tagsResponse = tags.getText().split(",");
                Arrays.stream(tagsResponse).forEach(e -> e.strip());
                System.out.println(holding.getImage().getUrl());
                User newUser = new User(
                        username,
                        email,
                        bio.getText(),
                        holding.getImage(),
                        guiInstance,
                        tagsResponse);

                loggedInUser = newUser;
                rootPane.setCenter(loadHomePage(guiInstance));
            }
        });


        return profileDetails;
    }

    // PRIVATE DEVELOPMENT HELPER METHODS
    /**
     * Get an image resource from the image located in the filepath.
     * Filepath must point to a valid resource.
     * @param filePath filepath relative to directory. Should be of type
     *                 .png, .gif, or other supported by JavaFX Image.
     * @return the ImageView of the image located at the filepath
     */
    public static Image loadImageResource(String filePath) {
        // none of the other ways to get relative file path functioned with the JavaFX image class, so we've got this
        String root = System.getProperty("user.dir"); // retrieve the computer specific file path

        Image resource = new Image("file:" + root + filePath); // retrieve image

        return resource;
    }

    /**
     * Create a label that scales along with the width and height
     * @param content the content you want the label to hold
     * @param width the width you want to scale the label with
     * @param height the height you want to scale the label with
     * @return a label that will scale along with the provided width and height
     */
    public static Label scaleableText(String content, DoubleExpression width, DoubleExpression height) {
        Label title = new Label(content);

        // align the title to the center of the box it's in
        title.setTextAlignment(TextAlignment.CENTER);
        title.setAlignment(Pos.CENTER);
        // bind the title to the proper width for the list
        title.prefWidthProperty().bind(width);
        title.prefHeightProperty().bind(height);

        // create a double property for the text size with a default of 20
        DoubleProperty textSize = new SimpleDoubleProperty(20);

        // set the font to the size of the property
        title.setFont(Font.font(textSize.doubleValue()));

        // scale factor of 10 to the width because it's not a perfect scale
        textSize.bind(width.divide(10));

        // listen for the textsize property changing and update the size of the text when it does
        textSize.addListener((observable, oldValue, newValue) -> title.setFont(Font.font(textSize.doubleValue())));

        return title;
    }

    /**
     * Highlight the borders of a pane. Used for debugging why certain visual elements may not be working
     * @param element the element to outline
     * @param color the color to outline it in
     */
    public static void HELP_HIGHLIGHT_PANE(Pane element, Color color) {
        element.setBorder(
                new Border(
                        new BorderStroke(
                                color,
                                BorderStrokeStyle.SOLID,
                                CornerRadii.EMPTY,
                                BorderWidths.DEFAULT)
                )
        );

    }
}
