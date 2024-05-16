package org.ricardofigueroa.controllers;

/**
 * Nombre: Ricardo Figueroa Fecha de creacion: 11/04/2024 Ultmia Fecha de
 * edicion : 02/05/2024
 *
 */
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.ricardofigueroa.beans.Clientes;
import org.ricardofigueroa.beans.Producto;
import org.ricardofigueroa.beans.Proveedor;
import org.ricardofigueroa.db.Conexion;
import org.ricardofigueroa.system.Main;

public class MenuProductoController implements Initializable {

    private Main escenarioPrincipal;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoOperaciones = operaciones.NINGUNO;
    private ObservableList<Producto> listaProductos;
    @FXML
    private Button btnRegresar;
    @FXML
    private TextField txtProductoId;
    @FXML
    private TextField txtNombreP;
    @FXML
    private TextField txtDescripcionProducto;
    @FXML
    private TextField txtCantidadStock;
    @FXML
    private TextField txtPrecioMayor;
    @FXML
    private TextField txtPrecioCompra;
    @FXML
    private TextField txtDistribuidorId;
    @FXML
    private TextField txtCategoriaProductoId;
    @FXML
    private TableView tblProductos;
    @FXML
    private TableColumn colProductoId;
    @FXML
    private TableColumn colNombreP;
    @FXML
    private TableColumn colDescripcionProducto;
    @FXML
    private TableColumn colCantidadStock;
    @FXML
    private TableColumn colPrecioMayor;
    @FXML
    private TableColumn colPrecioCompra;
    @FXML
    private TableColumn colCategoriaProductoId;
    @FXML
    private TableColumn colImagen;
    @FXML
    private TableColumn colDistribuidorId;
    @FXML
    private Button btnAgregarP;
    @FXML
    private Button btnEliminarP;
    @FXML
    private Button btnEditarP;
    @FXML
    private Button btnReportesP;
    @FXML
    private ImageView imgAgregarP;
    @FXML
    private ImageView imgEliminarP;
    @FXML
    private ImageView imgEditarP;
    @FXML
    private ImageView imgReportesP;

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        cargarDatos();
        deactivarControllers();
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void cargarDatos() {
        tblProductos.setItems(getProductos());
        colProductoId.setCellValueFactory(new PropertyValueFactory<>("productoId"));
        colNombreP.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colDescripcionProducto.setCellValueFactory(new PropertyValueFactory<>("descripcionProducto"));
        colCantidadStock.setCellValueFactory(new PropertyValueFactory<>("cantidadStock"));
        colPrecioMayor.setCellValueFactory(new PropertyValueFactory<>("precioVentaMayor"));
        colPrecioCompra.setCellValueFactory(new PropertyValueFactory<>("precioCompra"));
        colCategoriaProductoId.setCellValueFactory(new PropertyValueFactory<>("categoriaProductosId"));
        colDistribuidorId.setCellValueFactory(new PropertyValueFactory<>("distribuidorId"));
    }

    public void seleccionarDatos() {
        txtProductoId.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getProductoId()));
        txtNombreP.setText(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
        txtDescripcionProducto.setText(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto());
        txtCantidadStock.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCantidadStock()));
        txtPrecioMayor.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getPrecioVentaMayor()));
        txtPrecioCompra.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getPrecioCompra()));
        txtCategoriaProductoId.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCategoriaProductosId()));
        txtDistribuidorId.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getDistribuidorId()));
    }

    public ObservableList<Producto> getProductos() {
        ArrayList<Producto> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Producto(
                        resultado.getInt("productoId"),
                        resultado.getString("nombreProducto"),
                        resultado.getString("descripcionProducto"),
                        resultado.getInt("cantidadStock"),
                        resultado.getDouble("precioVentaMayor"),
                        resultado.getDouble("precioCompra"),
                        resultado.getInt("distribuidorId"),
                        resultado.getString("imagenProducto"),
                        resultado.getInt("categoriaProductosId")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProductos = FXCollections.observableArrayList(lista);
    }

    public void agregar() {
        switch (tipoOperaciones) {
            case NINGUNO:
                activarControllers();
                btnAgregarP.setText("GUARDAR");
                btnReportesP.setText("CANCELAR");
                btnEditarP.setDisable(true);
                btnEliminarP.setDisable(true);
                imgAgregarP.setImage(new Image("/org/ricardofigueroa/images/guardarImagen.png"));
                imgReportesP.setImage(new Image("/org/ricardofigueroa/images/cancelar.png"));
                tipoOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                deactivarControllers();
                limpiarControllers();
                btnAgregarP.setText("AGREGAR");
                btnReportesP.setText("REPORTES");
                btnEditarP.setDisable(false);
                btnReportesP.setDisable(false);
                imgAgregarP.setImage(new Image("/org/ricardofigueroa/images/agregarUsuario.png"));
                imgReportesP.setImage(new Image("/org/ricardofigueroa/images/reportecliente.png"));
                tipoOperaciones = operaciones.NINGUNO;
                break;
        }
    }

    public void guardar() {
        Producto producto = new Producto();
        producto.setProductoId(Integer.parseInt(txtProductoId.getText()));
        producto.setNombreProducto(txtNombreP.getText());
        producto.setDescripcionProducto(txtDescripcionProducto.getText());
        producto.setCantidadStock(Integer.parseInt(txtCantidadStock.getText()));
        producto.setDistribuidorId(Integer.parseInt(txtDistribuidorId.getText()));
        producto.setPrecioVentaMayor(Double.parseDouble(txtPrecioMayor.getText()));
        producto.setPrecioCompra(Double.parseDouble(txtPrecioCompra.getText()));
        producto.setCategoriaProductosId(Integer.parseInt(txtCategoriaProductoId.getText()));

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProductos (?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, producto.getProductoId());
            procedimiento.setString(2, producto.getNombreProducto());
            procedimiento.setString(3, producto.getDescripcionProducto());
            procedimiento.setInt(4, producto.getCantidadStock());
            procedimiento.setDouble(5, producto.getPrecioVentaMayor());
            procedimiento.setDouble(6, producto.getPrecioCompra());
            procedimiento.setInt(7, producto.getDistribuidorId());
            procedimiento.setString(8, producto.getImagenProducto());
            procedimiento.setInt(9, producto.getCategoriaProductosId());
            procedimiento.execute();
            listaProductos.add(producto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        switch (tipoOperaciones) {
            case ACTUALIZAR:
                deactivarControllers();
                limpiarControllers();
                tipoOperaciones = operaciones.NINGUNO;
                break;
            default:
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "CONFIRMAR elminar Registro",
                            "ELIMINAR CLIENTES", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement prodecimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProductos (?) }");
                            prodecimiento.setInt(1, ((Producto) tblProductos.getSelectionModel().getSelectedItem()).getProductoId());
                            prodecimiento.execute();
                            listaProductos.remove(tblProductos.getSelectionModel().getSelectedItem());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento");
                }
        }
    }

    public void editar() {
        switch (tipoOperaciones) {
            case NINGUNO:
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                    btnEditarP.setText("ACTUALIZAR");
                    btnReportesP.setText("CANCELAR");
                    btnAgregarP.setDisable(true);
                    btnEliminarP.setDisable(true);
                    imgReportesP.setImage(new Image("/org/ricardofigueroa/images/cancelar.png"));
                    activarControllers();
                    txtProductoId.setEditable(false);
                    tipoOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "¡Debe de seleccionar un registro!");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditarP.setText("EDITAR");
                btnReportesP.setText("REPORTE");
                btnAgregarP.setDisable(false);
                btnEliminarP.setDisable(false);
                imgReportesP.setImage(new Image("/org/ricardofigueroa/images/reportecliente.png"));
                deactivarControllers();
                limpiarControllers();
                tipoOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void reporte() {
        switch (tipoOperaciones) {
            case ACTUALIZAR:
                deactivarControllers();
                limpiarControllers();
                btnEditarP.setText("EDITAR");
                btnReportesP.setText("REPORTE");
                btnAgregarP.setDisable(false);
                btnEliminarP.setDisable(false);
                btnEditarP.setDisable(false);
                imgEditarP.setImage(new Image("/org/ricardofigueroa/images/editarClientes.png"));
                imgReportesP.setImage(new Image("/org/ricardofigueroa/images/reportecliente.png"));
                tipoOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarProductos (?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            Producto producto = (Producto) tblProductos.getSelectionModel().getSelectedItem();
            producto.setNombreProducto(txtNombreP.getText());
            producto.setDescripcionProducto(txtDescripcionProducto.getText());
            producto.setCantidadStock(Integer.parseInt(txtCantidadStock.getText()));
            producto.setPrecioVentaMayor(Double.parseDouble(txtPrecioMayor.getText()));
            producto.setPrecioCompra(Double.parseDouble(txtPrecioCompra.getText()));
            producto.setCategoriaProductosId(Integer.parseInt(txtCategoriaProductoId.getText()));
            producto.setDistribuidorId(Integer.parseInt(txtDistribuidorId.getText())); // Agregar DistribuidorId
            procedimiento.setInt(1, producto.getProductoId());
            procedimiento.setString(2, producto.getNombreProducto());
            procedimiento.setString(3, producto.getDescripcionProducto());
            procedimiento.setInt(4, producto.getCantidadStock());
            procedimiento.setDouble(5, producto.getPrecioVentaMayor());
            procedimiento.setDouble(6, producto.getPrecioCompra());
            procedimiento.setInt(7, producto.getDistribuidorId());
            procedimiento.setString(8, producto.getImagenProducto());
            procedimiento.setInt(9, producto.getCategoriaProductosId());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deactivarControllers() {
        txtDescripcionProducto.setEditable(false);
        txtNombreP.setEditable(false);
        txtCantidadStock.setEditable(false);
        txtPrecioMayor.setEditable(false);
        txtPrecioCompra.setEditable(false);
        txtDistribuidorId.setEditable(false);
        txtCategoriaProductoId.setEditable(false);
    }

    public void activarControllers() {
        txtDescripcionProducto.setEditable(true);
        txtNombreP.setEditable(true);
        txtCantidadStock.setEditable(true);
        txtPrecioMayor.setEditable(true);
        txtPrecioCompra.setEditable(true);
        txtDistribuidorId.setEditable(true);
        txtCategoriaProductoId.setEditable(true);
    }

    public void limpiarControllers() {
        txtDescripcionProducto.clear();
        txtNombreP.clear();
        txtCantidadStock.clear();
        txtPrecioMayor.clear();
        txtPrecioCompra.clear();
        txtDistribuidorId.clear();
        txtCategoriaProductoId.clear();
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuPrincipalView();
        }
    }
}