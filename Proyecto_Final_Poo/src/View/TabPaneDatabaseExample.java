
package View;

import Model.Mascota;
import Model.Propietario;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import javafx.application.Application; // Importa la clase Application del paquete javafx.application, que es necesaria para crear una aplicaciï¿½n JavaFX.
import javafx.collections.FXCollections; // Importa la clase FXCollections del paquete javafx.collections, que proporciona mï¿½todos estï¿½ticos para crear instancias de listas observables.
import javafx.collections.ObservableList; // Importa la interfaz ObservableList del paquete javafx.collections, que define una lista que notifica a los observadores de cambios en los elementos de la lista.
import javafx.scene.Scene; // Importa la clase Scene del paquete javafx.scene, que representa el contenido visual de una aplicaciï¿½n JavaFX.
import javafx.scene.control.*; // Importa todas las clases del paquete javafx.scene.control, que proporciona controles de interfaz de usuario como botones, etiquetas y tablas.
import javafx.scene.layout.BorderPane; // Importa la clase BorderPane del paquete javafx.scene.layout, que es un contenedor que organiza los nodos en una disposiciï¿½n de borde alrededor de un centro.
import javafx.scene.layout.GridPane; // Importa la clase GridPane del paquete javafx.scene.layout, que es un contenedor que organiza los nodos en una cuadrï¿½cula.
import javafx.stage.Stage; // Importa la clase Stage del paquete javafx.stage, que representa la ventana principal de una aplicaciï¿½n JavaFX.
import java.sql.Connection; // Importa la clase Connection del paquete java.sql, que representa una conexiï¿½n con una base de datos SQL.
import java.sql.DriverManager; // Importa la clase DriverManager del paquete java.sql, que administra un conjunto de controladores de JDBC.
import java.sql.ResultSet; // Importa la clase ResultSet del paquete java.sql, que representa un conjunto de resultados de una consulta SQL.
import java.sql.Statement; // Importa la clase Statement del paquete java.sql, que representa una declaraciï¿½n SQL para ser ejecutada en una base de datos.
import javafx.scene.image.Image;
import org.mindrot.jbcrypt.BCrypt;






public class TabPaneDatabaseExample extends Application {

    private TableView<Propietario> propietarioTable;
    private TableView<Mascota> mascotaTable;
    private TabPane tabPane;
    private Tab propietarioTab;
    private Tab mascotaTab;
    private Tab editarPropietarioTab;
    private Tab editarMascotaTab;
    private Tab crearMascotaTab;
    private Tab crearPropietarioTab;
    private ComboBox<Integer> propietarioIdComboBox;
    private ComboBox<Integer> mascotaIdComboBox;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inventario ");

        tabPane = new TabPane();

        // Pestaña de Propietarios
        propietarioTab = new Tab("Mostrar Propietarios");
        propietarioTable = new TableView<>();
        propietarioTab.setContent(propietarioTable);

        // Pestaï¿½a de Mascotas
        mascotaTab = new Tab("Mostrar Mascotas");
        mascotaTable = new TableView<>();
        mascotaTab.setContent(mascotaTable);

        // Pestaï¿½a de Editar Propietarios
        editarPropietarioTab = new Tab("Editar/Eliminar Propietarios");
        editarPropietarioTab.setContent(createEditPropietarioPane());

        // Pestaï¿½a de Editar Mascotas
        editarMascotaTab = new Tab("Editar/Eliminar Mascotas");
        editarMascotaTab.setContent(createEditMascotaPane());
        
        //pestaï¿½a de crear mascotas
        crearMascotaTab = new Tab("Crear Mascota");
        crearMascotaTab.setContent(createMascotaPane());
        
        //pestaï¿½a de crear propietarios
        crearPropietarioTab = new Tab("Crear Propietarios");
        crearPropietarioTab.setContent(createPropietarioPane());
        
       

        // Configurar las columnas para Propietarios
        TableColumn<Propietario, Integer> propietarioIdColumn = new TableColumn<>("ID");
        propietarioIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<Propietario, String> propietarioNombreColumn = new TableColumn<>("Nombre");
        propietarioNombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());

        TableColumn<Propietario, String> propietarioDireccionColumn = new TableColumn<>("Direccion");
        propietarioDireccionColumn.setCellValueFactory(cellData -> cellData.getValue().direccionProperty());
        
        TableColumn<Propietario, Integer> propietarioNumeroColumn = new TableColumn<>("Numero");
        propietarioNumeroColumn.setCellValueFactory(cellData -> cellData.getValue().numeroProperty().asObject());
        
        

        propietarioTable.getColumns().addAll(propietarioIdColumn, propietarioNombreColumn, propietarioDireccionColumn, propietarioNumeroColumn);

        // Configurar las columnas para Mascotas
        TableColumn<Mascota, Integer> mascotaIdColumn = new TableColumn<>("ID");
        mascotaIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<Mascota, String> mascotaNombreColumn = new TableColumn<>("Nombre");
        mascotaNombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        
        TableColumn<Mascota, String> mascotaTipoColumn = new TableColumn<>("Tipo");
        mascotaTipoColumn.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());

        TableColumn<Mascota, String> mascotaEspecieColumn = new TableColumn<>("Especie");
        mascotaEspecieColumn.setCellValueFactory(cellData -> cellData.getValue().especieProperty());       
        
        TableColumn<Mascota, String> mascotaFechaColumn = new TableColumn<>("Fecha Nacimiento");
        mascotaFechaColumn.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());

        TableColumn<Mascota, Integer> mascotaPropietarioIdColumn = new TableColumn<>("Propietario ID");
        mascotaPropietarioIdColumn.setCellValueFactory(cellData -> cellData.getValue().propietarioIdProperty().asObject());

        mascotaTable.getColumns().addAll(mascotaIdColumn, mascotaNombreColumn, mascotaEspecieColumn, mascotaFechaColumn, mascotaPropietarioIdColumn);

        // Conectar a la base de datos y cargar datos
        loadData();

        // Crear el menï¿½
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Opciones");
        
        
        MenuItem openPropietarioTab = new MenuItem("Abrir Propietarios");
        openPropietarioTab.setOnAction(e -> openTab(propietarioTab));
        

        MenuItem openMascotaTab = new MenuItem("Abrir Mascotas");
        openMascotaTab.setOnAction(e -> openTab(mascotaTab));
        

        MenuItem openEditarPropietarioTab = new MenuItem("Editar/Eliminar Propietarios");
        openEditarPropietarioTab.setOnAction(e -> openTab(editarPropietarioTab));
        

        MenuItem openEditarMascotaTab = new MenuItem("Editar/Eliminar Mascotas");
        openEditarMascotaTab.setOnAction(e -> openTab(editarMascotaTab));
        
        MenuItem openCrearMascotaTab = new MenuItem("Crear Mascota");
        openCrearMascotaTab.setOnAction(e -> openTab(crearMascotaTab));
        
        MenuItem openCrearPropietarioTab = new MenuItem("Crear Propietario");
        openCrearPropietarioTab.setOnAction(e -> openTab(crearPropietarioTab));
        
        

        menu.getItems().addAll(openPropietarioTab, openMascotaTab, openCrearPropietarioTab, openCrearMascotaTab, openEditarPropietarioTab, openEditarMascotaTab);
        menuBar.getMenus().add(menu);
    
        
        // Layout principal
        BorderPane root = new BorderPane();
       
        root.setTop(menuBar);
        root.setCenter(tabPane);
        root.setStyle("    -fx-background-color: #deffd3; \n" +
"    -fx-background-size: 400px 400px, 400px 150px;\n" +
"    -fx-background-position: center center, center bottom;\n" +
"    -fx-background-image: url(Imagenes/logo.png), url(Imagenes/Recurso11.png);\n" +
"    -fx-background-repeat: no-repeat, no-repeat;");
        
        
        BorderPane imagenFondo = new BorderPane();
        imagenFondo.setId("imagen-fondo");


        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/Styles/styles.css").toExternalForm());
        
        Image icon = new Image(getClass().getResourceAsStream("/Imagenes/logo.png"));
        
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(icon);
        primaryStage.show();
        
        
        
        
        openPropietarioTab.setId("open-propietarioTab");

        openMascotaTab.setId("open-mascotaTab");

        openCrearMascotaTab.setId("crear-mascotaTab");

        openCrearPropietarioTab.setId("crear-propietarioTab");
        
        openEditarPropietarioTab.setId("editar-propietario");

        openEditarMascotaTab.setId("editar-mascota");
        

    }

    private void openTab(Tab tab) {
        if (!tabPane.getTabs().contains(tab)) {
            tabPane.getTabs().add(tab);
        }
        tabPane.getSelectionModel().select(tab);
    }

    private GridPane createMascotaPane() {
    GridPane pane = new GridPane();
    pane.setHgap(10);
    pane.setVgap(10);

    Label nombreLabel = new Label("Nombre:");
    TextField nombreField = new TextField();

    Label tipoLabel = new Label("Tipo:");
    TextField tipoField = new TextField();
    
    Label especieLabel = new Label("Especie:");
    TextField especieField = new TextField();
    
    Label fechaLabel = new Label("Fecha de nacimiento:");
    TextField fechaField = new TextField();

    Label propietarioIdLabel = new Label("Propietario ID:");
    TextField propietarioIdField = new TextField();
    
    Button createMascotaButton = new Button("Crear Mascota");
    
    createMascotaButton.setOnAction(e -> {
    String nombre = nombreField.getText();
    String tipo = tipoField.getText();
    String especie = especieField.getText();
    String fecha = fechaField.getText();
    int propietarioId = Integer.parseInt(propietarioIdField.getText());
        crearMascota(nombre, tipo, especie, fecha, propietarioId);
        nombreField.clear();
        tipoField.clear();
        especieField.clear();
        fechaField.clear();
        propietarioIdField.clear();
    
        createMascotaButton.setOnAction(event -> mascotaRegisterMessage(nombreField.getText(), tipoField.getText(), especieField.getText(), fechaField.getText()));
    loadData(); // Actualizar la vista despuï¿½s de la inserciï¿½n
    });
    
    pane.add(nombreLabel, 0, 1);
    pane.add(nombreField, 1, 1);
    pane.add(tipoLabel, 0, 2);
    pane.add(tipoField, 1, 2);
    pane.add(especieLabel, 0, 3);
    pane.add(especieField, 1, 3);
    pane.add(fechaLabel, 0, 4);
    pane.add(fechaField, 1, 4);
    pane.add(propietarioIdLabel, 0, 5);
    pane.add(propietarioIdField, 1, 5);
    pane.add(createMascotaButton, 1, 6); // Añadir el botï¿½n Crear Mascota
    
    
        return pane;
    }
    private GridPane createPropietarioPane() {
    GridPane pane = new GridPane();
    pane.setHgap(10);
    pane.setVgap(10);

    Label nombreLabel = new Label("Nombre:");
    TextField nombreField = new TextField();

    Label direccionLabel = new Label("Direccion:");
    TextField direccionField = new TextField();
    
    Label numeroLabel = new Label("Numero:");
    TextField numeroField = new TextField();
    
    Button createPropietarioButton = new Button("Crear Propietario");
    
        
    createPropietarioButton.setOnAction(e -> {
        String nombre = nombreField.getText();
        String direccion = direccionField.getText();
        int numero = Integer.parseInt(numeroField.getText());
        crearPropietario(nombre, direccion, numero);
        nombreField.clear();
        direccionField.clear();
        numeroField.clear();
        createPropietarioButton.setOnAction(event -> personaRegisterMessage(nombreField.getText(), direccionField.getText(), numeroField.getText()));
        loadData(); // Actualizar la vista despuï¿½s de la inserciï¿½n
    });
     
    pane.add(nombreLabel, 0, 1);
    pane.add(nombreField, 1, 1);
    pane.add(direccionLabel, 0, 2);
    pane.add(direccionField, 1, 2);
    pane.add(numeroLabel, 0, 3);
    pane.add(numeroField, 1, 3);
    pane.add(createPropietarioButton, 0, 5); // Aï¿½adir el botï¿½n Crear Propietario
    
    
    
    
        return pane;
    
    }
    
    private GridPane createEditPropietarioPane() {
    GridPane pane = new GridPane();
    pane.setHgap(10);
    pane.setVgap(10);

    Label idLabel = new Label("ID:");
    propietarioIdComboBox = new ComboBox<>();
    propietarioIdComboBox.setOnAction(e -> loadPropietarioData());

    Label nombreLabel = new Label("Nombre:");
    TextField nombreField = new TextField();

    Label direccionLabel = new Label("Direccion:");
    TextField direccionField = new TextField();

    Label numeroLabel = new Label("Numero:");
    TextField numeroField = new TextField();
    
    Button updateButton = new Button("Actualizar");
    Button deleteButton = new Button("Eliminar");
    
    updateButton.setOnAction(e -> {
        
        // Lï¿½gica para actualizar el propietario
        int id = propietarioIdComboBox.getValue();
        String nombre = nombreField.getText();
        String direccion = direccionField.getText();
        int numero = Integer.parseInt(numeroField.getText());
        updatePropietario(id, nombre, direccion, numero);
        loadData();
    });

    deleteButton.setOnAction(e -> {
        // Lï¿½gica para eliminar el propietario
        int id = propietarioIdComboBox.getValue();
        deletePropietario(id);
        loadData();
    });



    pane.add(idLabel, 0, 0);
    pane.add(propietarioIdComboBox, 1, 0);
    pane.add(nombreLabel, 0, 1);
    pane.add(nombreField, 1, 1);
    pane.add(direccionLabel, 0, 2);
    pane.add(direccionField, 1, 2);
    pane.add(numeroLabel, 0, 3);
    pane.add(numeroField, 1, 3);
    pane.add(updateButton, 0, 4);
    pane.add(deleteButton, 1, 4);


    return pane;
}


    private GridPane createEditMascotaPane() {
    GridPane pane = new GridPane();
    pane.setHgap(10);
    pane.setVgap(10);

    Label idLabel = new Label("ID:");
    mascotaIdComboBox = new ComboBox<>();
    mascotaIdComboBox.setOnAction(e -> loadMascotaData());

    Label nombreLabel = new Label("Nombre:");
    TextField nombreField = new TextField();

    Label tipoLabel = new Label("Tipo:");
    TextField tipoField = new TextField();

    Label especieLabel = new Label("Especie:");
    TextField especieField = new TextField();
    
    Label fechaLabel = new Label("Fecha de nacimiento:");
    TextField fechaField = new TextField();
    
    Label propietarioIdLabel = new Label("Propietario ID:");
    TextField propietarioIdField = new TextField();

    Button updateButton = new Button("Actualizar");
    Button deleteButton = new Button("Eliminar");

    updateButton.setOnAction(e -> {
        // Lï¿½gica para actualizar la mascota
        int id = mascotaIdComboBox.getValue();
        String nombre = nombreField.getText();
        String tipo = tipoField.getText();
        String especie = especieField.getText();
        String fecha = fechaField.getText();
        int propietarioId = Integer.parseInt(propietarioIdField.getText());
        updateMascota(id, nombre, tipo, especie, fecha, propietarioId);
        loadData();
    });

    deleteButton.setOnAction(e -> {
        // Lï¿½gica para eliminar la mascota
        int id = mascotaIdComboBox.getValue();
        deleteMascota(id);
        loadData();
    });

    pane.add(idLabel, 0, 0);
    pane.add(mascotaIdComboBox, 1, 0);
    pane.add(nombreLabel, 0, 1);
    pane.add(nombreField, 1, 1);
    pane.add(tipoLabel, 0, 2);
    pane.add(tipoField, 1, 2);
    pane.add(especieLabel, 0, 3);
    pane.add(especieField, 1, 3);
    pane.add(fechaLabel, 0, 4);
    pane.add(fechaField, 1, 4);
    pane.add(propietarioIdLabel, 0, 5);
    pane.add(propietarioIdField, 1, 5);
    pane.add(updateButton, 0, 6);
    pane.add(deleteButton, 1, 6);
    

    return pane;
}

    private void updatePropietario(int id, String nombre, String direccion, int numero) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");
            Statement statement = connection.createStatement();
            String query = "UPDATE Propietario SET nombre = '" + nombre + "', direccion = '" + direccion + "', numero = " + numero + " WHERE id = " + id;
            statement.executeUpdate(query);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deletePropietario(int id) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");
            Statement statement = connection.createStatement();
            String query = "DELETE FROM Propietario WHERE id = " + id;
            statement.executeUpdate(query);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMascota(int id, String nombre, String tipo, String especie, String fecha, int propietarioId) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");
            Statement statement = connection.createStatement();
            String query = "UPDATE Mascota SET nombre = '" + nombre + "', tipo = '" + tipo + "', especie ='" + especie +"', fecha nacimiento ='" + fecha +"', propietario_id = " + propietarioId + " WHERE id = " + id;
            statement.executeUpdate(query);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteMascota(int id) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");
            Statement statement = connection.createStatement();
            String query = "DELETE FROM Mascota WHERE id = " + id;
            statement.executeUpdate(query);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    /////
    // Mï¿½todo para crear una nueva entrada de propietario en la base de datos
private void crearPropietario(String nombre, String direccion, int numero) {
    try {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");
        Statement statement = connection.createStatement();
        String query = "INSERT INTO Propietario (nombre, direccion, numero) VALUES ('" + nombre + "', '" + direccion + "', '" + numero + "')";
        statement.executeUpdate(query);
        connection.close();
        
    } catch (Exception e) {
        e.printStackTrace();
    }
}

// Mï¿½todo para crear una nueva entrada de mascota en la base de datos
private void crearMascota(String nombre, String tipo, String especie, String fecha, int propietarioId) {
    try {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");
        Statement statement = connection.createStatement();
        String query = "INSERT INTO Mascota (nombre, tipo, especie, fecha, propietario_id) VALUES ('" + nombre + "', '" + tipo + "','"+ especie +"' , '"+ fecha +"' , " + propietarioId + ")";
        statement.executeUpdate(query);
        connection.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    
    
    /////
    private void loadData() {
        ObservableList<Propietario> propietarioData = FXCollections.observableArrayList();
        ObservableList<Mascota> mascotaData = FXCollections.observableArrayList();
        ObservableList<Integer> propietarioIds = FXCollections.observableArrayList();
        ObservableList<Integer> mascotaIds = FXCollections.observableArrayList();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");
            Statement statement = connection.createStatement();

            // Cargar datos de propietarios
            ResultSet propietarioResultSet = statement.executeQuery("SELECT * FROM Propietario");
            while (propietarioResultSet.next()) {
                int id = propietarioResultSet.getInt("id");
                String nombre = propietarioResultSet.getString("nombre");
                String direccion = propietarioResultSet.getString("direccion");
                int numero = propietarioResultSet.getInt("numero");
                propietarioData.add(new Propietario(id, nombre, direccion, numero));
                propietarioIds.add(id);
            }

            // Cargar datos de mascotas
            ResultSet mascotaResultSet = statement.executeQuery("SELECT * FROM Mascota");
            while (mascotaResultSet.next()) {
                int id = mascotaResultSet.getInt("id");
                String nombre = mascotaResultSet.getString("nombre");
                String tipo = mascotaResultSet.getString("tipo");
                String especie = mascotaResultSet.getString("especie");
                String fecha = mascotaResultSet.getString("fecha");
                int propietarioId = mascotaResultSet.getInt("propietario_id");
                mascotaData.add(new Mascota(id, nombre, tipo, especie, fecha, propietarioId));
                mascotaIds.add(id);
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        propietarioTable.setItems(propietarioData);
        mascotaTable.setItems(mascotaData);
        propietarioIdComboBox.setItems(propietarioIds);
        mascotaIdComboBox.setItems(mascotaIds);
    }

    private void loadPropietarioData() {
    Integer id = propietarioIdComboBox.getValue();
    if (id != null) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Propietario WHERE id = " + id);
            if (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String direccion = resultSet.getString("direccion");
                int numero = resultSet.getInt("numero");
                ((TextField) ((GridPane) editarPropietarioTab.getContent()).getChildren().get(3)).setText(nombre);
                ((TextField) ((GridPane) editarPropietarioTab.getContent()).getChildren().get(5)).setText(direccion);
                ((TextField) ((GridPane) editarPropietarioTab.getContent()).getChildren().get(7)).setText(String.valueOf(numero));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

private void loadMascotaData() {
    Integer id = mascotaIdComboBox.getValue();
    if (id != null) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Mascota WHERE id = " + id);
            if (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String tipo = resultSet.getString("tipo");
                 String especie = resultSet.getString("especie");
                String fecha = resultSet.getString("fecha");
                int propietarioId = resultSet.getInt("propietario_id");
                
                ((TextField) ((GridPane) editarMascotaTab.getContent()).getChildren().get(3)).setText(nombre);
                ((TextField) ((GridPane) editarMascotaTab.getContent()).getChildren().get(5)).setText(tipo);
                ((TextField) ((GridPane) editarMascotaTab.getContent()).getChildren().get(7)).setText(especie);
                ((TextField) ((GridPane) editarMascotaTab.getContent()).getChildren().get(9)).setText(fecha);
                ((TextField) ((GridPane) editarMascotaTab.getContent()).getChildren().get(11)).setText(String.valueOf(propietarioId));
            }
            connection.close();        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

private void personaRegisterMessage(String nombre, String direccion, String numero) {
        if (nombre.isEmpty() || direccion.isEmpty() || numero.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de registro");
            alert.setHeaderText("Campos vacíos");
            alert.setContentText("Por favor, complete todos los campos.");
            alert.showAndWait();
            
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro exitoso");
            alert.setHeaderText(null);
            alert.setContentText("persona registrado con éxito.");
            alert.showAndWait();
            
            
            }


    }

private void mascotaRegisterMessage(String nombre, String tipo, String especie, String fecha) {
        if (nombre.isEmpty() || tipo.isEmpty() || especie.isEmpty() || fecha.isEmpty() ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de registro");
            alert.setHeaderText("Campos vacíos");
            alert.setContentText("Por favor, complete todos los campos.");
            alert.showAndWait();
            
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro exitoso");
            alert.setHeaderText(null);
            alert.setContentText("Mascota registrado con éxito.");
            alert.showAndWait();
            
            
            }


    }

    public static void main(String[] args) {
        launch(args);
    }
}