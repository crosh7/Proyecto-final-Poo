
package View;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

import static javafx.application.Application.launch;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

public class LoginAppWithMenu extends Application {

    private Stage primaryStage;
    private Stage menuStage;
    private ListView<String> userListView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Inicio de Sesi�n - Gestor de Mascostas USTA");
        // Cargar la imagen del icono
        Image icon = new Image(getClass().getResourceAsStream("/Imagenes/logo.png"));

// Obtener las dimensiones de la pantalla
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getVisualBounds().getWidth();
// Calcular el ancho m�ximo para los campos de texto
        double maxWidth = screenWidth * 0.5;

        // Agregar el icono a la ventana principal
        primaryStage.getIcons().add(icon);

        Label usernameLabel = new Label("Nombre de Usuario:");
        TextField usernameField = new TextField();
usernameField.setMaxWidth(maxWidth);

        Label passwordLabel = new Label("Contrase�a:");
        PasswordField passwordField = new PasswordField();
passwordField.setMaxWidth(maxWidth);        
        Button loginButton = new Button("Iniciar Sesi�n");
        Button registerButton = new Button("Registrar nuevo usuario");
        Button forgotPasswordButton = new Button("Olvid� mi contrase�a");
        Button changePasswordButton = new Button("Cambiar Contrase�a"); // Nuevo bot�n para cambiar contrase�a
        changePasswordButton.getStyleClass().add("hover-button");

        // Agregar clases CSS a los botones
        loginButton.getStyleClass().add("hover-button");
        registerButton.getStyleClass().add("hover-button");
        forgotPasswordButton.getStyleClass().add("hover-button");

        // Manejo de eventos para los botones
        loginButton.setOnAction(event -> handleLogin(usernameField.getText(), passwordField.getText()));
        registerButton.setOnAction(event -> showRegisterForm());
        forgotPasswordButton.setOnAction(event -> showForgotPasswordDialog());
        changePasswordButton.setOnAction(event -> showChangePasswordDialog()); // Manejo de eventos para el bot�n de cambiar contrase�a

        // Disposici�n horizontal para los botones
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(loginButton, registerButton, forgotPasswordButton, changePasswordButton); // Agregar el nuevo bot�n aqu�

        // Establecer estilos para la caja de botones
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);

        Image bannerImage = new Image("/Imagenes/Recurso11.png");
        ImageView bannerImageView = new ImageView(bannerImage);
        bannerImageView.setFitWidth(500);
        bannerImageView.setFitHeight(150);

        // Agregar el t�tulo o texto tipo t�tulo
        Label titleLabel = new Label("�Bienvenido a la Aplicaci�n de Gesti�n Mascotas!");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;"); // Estilos para el t�tulo
        titleLabel.setAlignment(Pos.CENTER); // Alinear el t�tulo al centro

        usernameField.setId("username-field");
        passwordField.setId("password-field");

        VBox formLayout = new VBox(10);
        formLayout.setStyle("-fx-background-color: #fff; -fx-background-radius: 10; -fx-padding: 20; -fx-spacing: 10;");
        formLayout.getChildren().addAll(bannerImageView, titleLabel, new Label(""), usernameLabel, usernameField, passwordLabel, passwordField, buttonBox);

        formLayout.setAlignment(Pos.CENTER);

        // Obtener las dimensiones de la pantalla
        /*   Screen screen = Screen.getPrimary();
        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();

        // Establecer el tama�o de la escena para que ocupe toda la pantalla
        Scene scene = new Scene(formLayout, screenWidth, screenHeight); */

        
        double screenHeight = screen.getVisualBounds().getHeight();

// Calcular el 90% del ancho y alto de la pantalla
        double sceneWidth = screenWidth * 0.7;
        double sceneHeight = screenHeight * 0.7;

// Establecer el tama�o de la escena
        Scene scene = new Scene(formLayout, sceneWidth, sceneHeight);

        //Scene scene = new Scene(formLayout, 700, 500);
        scene.getStylesheets().add(getClass().getResource("/Styles/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        // Manejo de evento para la tecla Enter en el campo de contrase�a
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin(usernameField.getText(), passwordField.getText());
            }
        });
    }

    private void handleLogin(String username, String password) {
        if (checkCredentials(username, password)) {
                openMenu();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de inicio de sesi�n");
            alert.setHeaderText("Credenciales incorrectas");
            alert.setContentText("Verifique su nombre de usuario y contrase�a.");
            alert.showAndWait();
        }
    }

    private boolean checkCredentials(String username, String password) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File("users.json"));

            for (JsonNode userNode : rootNode.get("users")) {
                String storedUsername = userNode.get("username").asText();
                String storedPassword = userNode.get("password").asText();

                if (storedUsername.equals(username) && BCrypt.checkpw(password, storedPassword)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void openMenu() {
        primaryStage.close();
        menuStage = new Stage();
        menuStage.setTitle("Men� Principal");

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("Archivo");
        MenuItem exitMenuItem = new MenuItem("Salir");
        exitMenuItem.setOnAction(event -> System.exit(0));
        MenuItem loadUsersMenuItem = new MenuItem("Cargar Lista de Usuarios");
        loadUsersMenuItem.setOnAction(event -> loadUserList());

        fileMenu.getItems().addAll(loadUsersMenuItem, exitMenuItem);

        // Nueva opci�n de men�: Gestor de Empleados
        Menu employeesMenu = new Menu("Gestor de inventario");
        MenuItem manageEmployeesMenuItem = new MenuItem("Gestionar Empleados");
        manageEmployeesMenuItem.setOnAction(event -> openEmployeeManager());
        employeesMenu.getItems().add(manageEmployeesMenuItem);

        menuBar.getMenus().addAll(fileMenu, employeesMenu);

        userListView = new ListView<>();
        userListView.setPrefHeight(200);

        VBox menuLayout = new VBox(menuBar, userListView);
        Scene menuScene = new Scene(menuLayout, 400, 300);

        menuStage.setScene(menuScene);
        menuStage.show();
    }

    private void loadUserList() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File("users.json"));
            JsonNode usersNode = rootNode.get("users");

            ObservableList<String> userNames = FXCollections.observableArrayList();

            for (JsonNode userNode : usersNode) {
                String userName = userNode.get("username").asText();
                userNames.add(userName);
            }

            userListView.setItems(userNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openEmployeeManager() {
        TabPaneDatabaseExample tabPaneDatabaseExample = new TabPaneDatabaseExample();
        tabPaneDatabaseExample.start(new Stage());
    }

    private void showRegisterForm() {
        Stage registerStage = new Stage();
        registerStage.setTitle("Registro de Usuario");

        Label idLabel = new Label("Identificaci�n:");
        TextField idField = new TextField(); // Campo de entrada para la identificaci�n
        Label usernameLabel = new Label("Nombre de Usuario:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Contrase�a:");
        PasswordField passwordField = new PasswordField();
        Button registerButton = new Button("Registrar");
        registerButton.getStyleClass().add("hover-button");

        VBox formLayout = new VBox(10);
        formLayout.setStyle("-fx-background-color: #fff; -fx-background-radius: 10; -fx-padding: 20; -fx-spacing: 10;");
        formLayout.getChildren().addAll(idLabel, idField, usernameLabel, usernameField, passwordLabel, passwordField, registerButton);
        formLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(formLayout, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/Styles/styles.css").toExternalForm());

        registerStage.setScene(scene);
        registerStage.show();

        registerButton.setOnAction(event -> handleRegister(idField.getText(), usernameField.getText(), passwordField.getText(), registerStage));
    }

    private void handleRegister(String id, String username, String password, Stage registerStage) {
        if (id.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de registro");
            alert.setHeaderText("Campos vac�os");
            alert.setContentText("Por favor, complete todos los campos.");
            alert.showAndWait();
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("users.json");
            JsonNode rootNode;
            ArrayNode usersArray;

            if (file.exists()) {
                rootNode = objectMapper.readTree(file);
                usersArray = (ArrayNode) rootNode.get("users");
            } else {
                rootNode = objectMapper.createObjectNode();
                usersArray = objectMapper.createArrayNode();
                ((ObjectNode) rootNode).set("users", usersArray);
            }

            // Verificar si el ID ya est� en uso
            for (JsonNode userNode : usersArray) {
                if (userNode.get("id").asText().equals(id)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error de registro");
                    alert.setHeaderText("ID de usuario existente");
                    alert.setContentText("El ID de usuario ya est� en uso. Por favor, elija otro.");
                    alert.showAndWait();
                    return;
                }
            }

            // Verificar si el nombre de usuario ya est� en uso
            for (JsonNode userNode : usersArray) {
                if (userNode.get("username").asText().equals(username)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error de registro");
                    alert.setHeaderText("Nombre de usuario existente");
                    alert.setContentText("El nombre de usuario ya est� en uso. Por favor, elija otro.");
                    alert.showAndWait();
                    return;
                }
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            ObjectNode newUser = objectMapper.createObjectNode();
            newUser.put("id", id); // Agregar el campo de identificaci�n al nuevo usuario
            newUser.put("username", username);
            newUser.put("password", hashedPassword);
            usersArray.add(newUser);

            // Indentaci�n para un formato m�s legible
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro exitoso");
            alert.setHeaderText(null);
            alert.setContentText("Usuario registrado con �xito.");
            alert.showAndWait();

            registerStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showForgotPasswordDialog() {
        Stage forgotPasswordStage = new Stage();
        forgotPasswordStage.setTitle("Recuperar Contrase�a");

        Label idLabel = new Label("Identificaci�n:");
        TextField idField = new TextField(); // Campo de entrada para la identificaci�n
        Button resetButton = new Button("Restablecer Contrase�a");
        resetButton.getStyleClass().add("hover-button");

        VBox formLayout = new VBox(10);
        formLayout.setStyle("-fx-background-color: #fff; -fx-background-radius: 10; -fx-padding: 20; -fx-spacing: 10;");
        formLayout.getChildren().addAll(idLabel, idField, resetButton);
        formLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(formLayout, 400, 200);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        forgotPasswordStage.setScene(scene);
        forgotPasswordStage.show();

        resetButton.setOnAction(event -> handlePasswordReset(idField.getText(), forgotPasswordStage));
    }

    private void handlePasswordReset(String id, Stage forgotPasswordStage) {
        if (id.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de restablecimiento");
            alert.setHeaderText("Campo vac�o");
            alert.setContentText("Por favor, ingrese la identificaci�n del usuario.");
            alert.showAndWait();
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("users.json");
            JsonNode rootNode = objectMapper.readTree(file);
            ArrayNode usersArray = (ArrayNode) rootNode.get("users");

            for (JsonNode userNode : usersArray) {
                JsonNode idNode = userNode.get("id");
                if (idNode != null && idNode.asText().equals(id)) {
                    String newTemporaryPassword = generateTemporaryPassword();
                    String hashedPassword = BCrypt.hashpw(newTemporaryPassword, BCrypt.gensalt());
                    ((ObjectNode) userNode).put("password", hashedPassword);

                    // Indentaci�n para un formato m�s legible
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Contrase�a restablecida");
                    alert.setHeaderText("Nueva contrase�a temporal generada");
                    alert.setContentText("Su nueva contrase�a temporal es: " + newTemporaryPassword);

                    // Copiar autom�ticamente la contrase�a al portapapeles cuando se hace clic en el bot�n "Aceptar" de la alerta
                    alert.setOnShown(event -> {
                        Button copyButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                        copyButton.setText("Copiar contrase�a");
                        copyButton.setOnAction(e -> {
                            Clipboard clipboard = Clipboard.getSystemClipboard();
                            ClipboardContent content = new ClipboardContent();
                            content.putString(newTemporaryPassword);
                            clipboard.setContent(content);
                            alert.close();
                        });
                    });

                    alert.showAndWait();

                    forgotPasswordStage.close();
                    return;
                }
            }

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de restablecimiento");
            alert.setHeaderText("Usuario no encontrado");
            alert.setContentText("La identificaci�n proporcionada no corresponde a ning�n usuario registrado. Por favor, verifique e intente nuevamente.");
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showChangePasswordDialog() {
        Stage changePasswordStage = new Stage();
        changePasswordStage.setTitle("Cambiar Contrase�a");

        Label usernameLabel = new Label("Nombre de Usuario:");
        TextField usernameField = new TextField();
        Label currentPasswordLabel = new Label("Contrase�a Actual:");
        PasswordField currentPasswordField = new PasswordField();
        Label newPasswordLabel = new Label("Nueva Contrase�a:");
        PasswordField newPasswordField = new PasswordField();
        Button changePasswordButton = new Button("Cambiar Contrase�a");
        changePasswordButton.getStyleClass().add("hover-button");

        
        VBox formLayout = new VBox(10);
        formLayout.setStyle("-fx-background-color: #fff; -fx-background-radius: 10; -fx-padding: 20; -fx-spacing: 10;");
        formLayout.getChildren().addAll(usernameLabel, usernameField, currentPasswordLabel, currentPasswordField, newPasswordLabel, newPasswordField, changePasswordButton);
        formLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(formLayout, 400, 300);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        changePasswordStage.setScene(scene);
        changePasswordStage.show();

        changePasswordButton.setOnAction(event -> handleChangePassword(usernameField.getText(), currentPasswordField.getText(), newPasswordField.getText(), changePasswordStage));
    }

    private void handleChangePassword(String username, String currentPassword, String newPassword, Stage changePasswordStage) {
        try {
            // Crear un objeto ObjectMapper para leer y escribir JSON
            ObjectMapper objectMapper = new ObjectMapper();

            // Leer el archivo JSON de usuarios
            File file = new File("users.json");
            JsonNode rootNode = objectMapper.readTree(file);
            ArrayNode usersArray = (ArrayNode) rootNode.get("users");

            // Buscar el usuario en la lista de usuarios
            for (JsonNode userNode : usersArray) {
                // Obtener el nombre de usuario y la contrase�a almacenada
                String storedUsername = userNode.get("username").asText();
                String storedPassword = userNode.get("password").asText();

                // Verificar si el nombre de usuario coincide con el proporcionado
                if (storedUsername.equals(username)) {
                    // Verificar si la contrase�a actual coincide con la contrase�a almacenada (usando BCrypt)
                    if (BCrypt.checkpw(currentPassword, storedPassword)) {
                        // Generar el hash de la nueva contrase�a
                        String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

                        // Actualizar la contrase�a almacenada en el JSON
                        ((ObjectNode) userNode).put("password", hashedNewPassword);

                        // Escribir el JSON actualizado de vuelta al archivo
                        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);

                        // Mostrar un mensaje de �xito
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Cambio de Contrase�a Exitoso");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("La contrase�a ha sido cambiada exitosamente.");
                        successAlert.showAndWait();

                        // Cerrar la ventana de cambio de contrase�a
                        changePasswordStage.close();

                        // Salir del m�todo despu�s de encontrar y actualizar el usuario
                        return;
                    } else {
                        // Mostrar un mensaje de error si la contrase�a actual es incorrecta
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error de Cambio de Contrase�a");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("La contrase�a actual es incorrecta.");
                        errorAlert.showAndWait();
                        return;
                    }
                }
            }

            // Mostrar un mensaje de error si el nombre de usuario no se encuentra en el archivo JSON
            Alert userNotFoundErrorAlert = new Alert(Alert.AlertType.ERROR);
            userNotFoundErrorAlert.setTitle("Error de Cambio de Contrase�a");
            userNotFoundErrorAlert.setHeaderText(null);
            userNotFoundErrorAlert.setContentText("El usuario no fue encontrado.");
            userNotFoundErrorAlert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateTemporaryPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[6];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}

