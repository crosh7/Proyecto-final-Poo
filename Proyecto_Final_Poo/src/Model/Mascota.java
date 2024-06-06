
package Model;


import javafx.beans.property.*;

public class Mascota {
    // Declaraci�n de propiedades para la clase Mascota
    private final IntegerProperty id; // Propiedad para el id
    private final StringProperty nombre; // Propiedad para el nombre
    private final StringProperty tipo;
    private final StringProperty especie;
    private final StringProperty fecha;// Propiedad para el tipo
    private final IntegerProperty propietarioId; // Propiedad para el id del propietario
    
    // Constructor para la clase Mascota
    public Mascota(int id, String nombre, String tipo, String especie, String fecha, int propietarioId) {
        // Inicializaci�n de las propiedades con los valores proporcionados
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.tipo = new SimpleStringProperty(tipo);
        this.especie = new SimpleStringProperty(especie);
        this.fecha = new SimpleStringProperty(fecha);
        this.propietarioId = new SimpleIntegerProperty(propietarioId);
    }

    // M�todo getter para obtener el id
    public int getId() {
        return id.get();
    }

    // M�todo getter para obtener la propiedad id
    public IntegerProperty idProperty() {
        return id;
    }

    // M�todo getter para obtener el nombre
    public String getNombre() {
        return nombre.get();
    }

    // M�todo getter para obtener la propiedad nombre
    public StringProperty nombreProperty() {
        return nombre;
    }

    // M�todo getter para obtener el tipo
    public String getTipo() {
        return tipo.get();
    }

    // M�todo getter para obtener la propiedad tipo
    public StringProperty tipoProperty() {
        return tipo;
    }
    public String getEspecie(){
    return especie.get();
    }
    public StringProperty especieProperty() {
        return especie;
    }
    public String getFecha(){
    return fecha.get();
}
    public StringProperty fechaProperty() {
        return fecha;
    }

    // M�todo getter para obtener el id del propietario
    public int getPropietarioId() {
        return propietarioId.get();
    }

    // M�todo getter para obtener la propiedad propietarioId
    public IntegerProperty propietarioIdProperty() {
        return propietarioId;
    }
}


