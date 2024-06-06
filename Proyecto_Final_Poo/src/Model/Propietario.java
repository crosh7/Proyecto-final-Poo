
package Model;

import javafx.beans.property.*;

public class Propietario {
    // Declaración de propiedades para la clase Propietario
    private final IntegerProperty id; // Propiedad para el id
    private final StringProperty nombre; // Propiedad para el nombre
    private final StringProperty direccion;
    private final IntegerProperty numero;// Propiedad para la dirección

    // Constructor para la clase Propietario
    public Propietario(int id, String nombre, String direccion, int numero) {
        // Inicialización de las propiedades con los valores proporcionados
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.direccion = new SimpleStringProperty(direccion);
        this.numero = new SimpleIntegerProperty(numero);
    }

    // Método getter para obtener el id
    public int getId() {
        return id.get();
    }

    // Método getter para obtener la propiedad id
    public IntegerProperty idProperty() {
        return id;
    }

    // Método getter para obtener el nombre
    public String getNombre() {
        return nombre.get();
    }

    // Método getter para obtener la propiedad nombre
    public StringProperty nombreProperty() {
        return nombre;
    }

    // Método getter para obtener la dirección
    public String getDireccion() {
        return direccion.get();
    }

    // Método getter para obtener la propiedad dirección
    public StringProperty direccionProperty() {
        return direccion;
    }

    public int getNumero(){
    return numero.get();
    }
    public IntegerProperty numeroProperty() {
        return numero;
    }
    
    
}

