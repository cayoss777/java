/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package VistaCliente;

import API.ApiPeruService;
import ModeloCliente.Cliente;
import ModeloProducto.Producto;
import ModeloVendedor.Vendedor;
import MySql.ConexionMySql;
import VistaCliente.frmVenta;
import VistaProducto.dlgProducto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USUARIO
 */
public class frmVenta extends javax.swing.JFrame {

    private Vendedor vendedorActual;   // guardar el vendedor logueado

    //Para ventas
    private DefaultTableModel modeloDetalle;
    private ArrayList<Object[]> productosSeleccionados; // opcional, o usar directamente el modelo
    private double totalFactura = 0.0;

    private double txtImporte = 0.0;
    private double totalAcumulado = 0.0;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmVenta.class.getName());

    /**
     * Creates new form frmVenta
     */
    public frmVenta() {
        initComponents();
    }

    // ✅ Constructor que recibe el vendedor (el que usarás desde login)
    public frmVenta(Vendedor v) {
        initComponents();               // crea todos los componentes

        setLocationRelativeTo(null);  // centra en la pantalla
        // Opcional: hacer que no se pueda redimensionar
        setResizable(false);

        this.vendedorActual = v;

        // Mostrar el nombre del vendedor en el JLabel
        if (lblNombreVendedor != null) {
            lblNombreVendedor.setText("Vendedor: " + v.getNombre());
            txtCodigoVendedor.setText("" + v.getId());
        } else {
            System.err.println("Error: lblNombreVendedor no encontrado.");
        }
        //Llamar a la fecha
        fecha_actual();

        //Codigo de factura
        txtCodigoFactura.setText(String.valueOf(obtenerSiguienteNumeroFactura()));

        //Ventas
        // Para ventas
        // Configurar modelo de la tabla de detalle
        modeloDetalle = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Código", "Producto", "Cantidad", "Precio", "Importe"}
        );

        tblDetalleVenta.setModel(modeloDetalle);
        // Opcional: ajustar ancho de columnas

        tblDetalleVenta.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblDetalleVenta.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblDetalleVenta.getColumnModel().getColumn(2).setPreferredWidth(60);
        tblDetalleVenta.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblDetalleVenta.getColumnModel().getColumn(4).setPreferredWidth(80);

        //nunca vacio
        txtTotalFactura.setText("0.00");
    }

    // Fecha de factura
    public void fecha_actual() {
        Date fechaActual = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = formateador.format(fechaActual);
        txtFechaFactura.setText(fecha);
        txtFechaFactura.setEditable(false);
    }

    // Luego, dentro de la clase, define el método:
    private int obtenerSiguienteNumeroFactura() {
        String sql = "SELECT COALESCE(MAX(No_Facturas), 0) + "
                + "1 FROM table_facturas";
        try (Connection conn = ConexionMySql.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    //Ventas 
    private void agregarProducto() {
        // Validar que haya un producto seleccionado
        if (txtIdProducto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto primero.");
            return;
        }

        int idProducto;
        double precio;
        try {
            idProducto = Integer.parseInt(txtIdProducto.getText());
            precio = Double.parseDouble(txtPrecioProducto.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Datos de producto inválidos.");
            return;
        }
        String nombreProducto = txtNombreProducto.getText();

        int cantidad = ((Number) spnCantidad.getValue()).intValue();
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "Cantidad debe ser mayor a cero.");
            return;
        }

        double importe = precio * cantidad;

        // Agregar fila a la tabla
        modeloDetalle.addRow(new Object[]{
            idProducto,
            nombreProducto,
            cantidad,
            precio,
            importe
        });

        // Actualizar total general
        totalFactura += importe;
        txtTotalFactura.setText(String.format("%.2f", totalFactura));

        // Limpiar campos de producto (dejar listo para el siguiente)
        txtIdProducto.setText("");
        txtNombreProducto.setText("");
        txtPrecioProducto.setText("");
        spnCantidad.setValue(1);
        txtImporteVenta.setText("0.00");
    }

    private void eliminarProducto() {
        int filaSeleccionada = tblDetalleVenta.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para eliminar.");
            return;
        }
        double importeEliminar = (double) modeloDetalle.getValueAt(filaSeleccionada, 4);
        modeloDetalle.removeRow(filaSeleccionada);
        totalFactura -= importeEliminar;
        txtTotalFactura.setText(String.format("%.2f", totalFactura));
    }

    /*
    public int id_factura_auto() {
        Generador ge = new Generador();
        int id_max2 = 1;
        try {
            id_max2 = ge.auto_increm("SELECT MAX(No_Facturas) FROM table_facturas;");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return id_max2;
    }
     */
 /*
    public int id_factura_auto() {
        String sql = "SELECT COALESCE(MAX(No_Facturas), 0)"
                + " + 1 FROM table_facturas";
        try (Connection conn = ConexionMySql.getConnection(); 
                Statement st = conn.createStatement(); 
                ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // si hay error, empieza en 1
    }
     */
    //Número de factura
    //this.txtCodigoFactura.setText(String.valueOf(id_factura_auto()));
    /**
     *
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlClientes = new javax.swing.JPanel();
        txtCodigoCliente = new javax.swing.JTextField();
        txtNombreCliente = new javax.swing.JTextField();
        txtApellidoCliente = new javax.swing.JTextField();
        btnClientes = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblNombreVendedor = new javax.swing.JLabel();
        txtCodigoVendedor = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtFechaFactura = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtCodigoFactura = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtTotalFactura = new javax.swing.JTextField();
        btnRegistrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetalleVenta = new javax.swing.JTable();
        btnAgregarProducto = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnLlamarProducto = new javax.swing.JButton();
        txtIdProducto = new javax.swing.JTextField();
        txtNombreProducto = new javax.swing.JTextField();
        txtPrecioProducto = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        spnCantidad = new javax.swing.JSpinner();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtImporteVenta = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(750, 800));

        pnlClientes.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlClientes.setToolTipText("");
        pnlClientes.setName(""); // NOI18N

        txtCodigoCliente.addActionListener(this::txtCodigoClienteActionPerformed);

        btnClientes.setText("Ver Clientes");
        btnClientes.addActionListener(this::btnClientesActionPerformed);

        jLabel2.setText("Apellidos");

        jLabel3.setText("Nombres");

        jLabel4.setText("Código");

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 51, 255));
        jLabel5.setText("Datos de cliente");

        javax.swing.GroupLayout pnlClientesLayout = new javax.swing.GroupLayout(pnlClientes);
        pnlClientes.setLayout(pnlClientesLayout);
        pnlClientesLayout.setHorizontalGroup(
            pnlClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlClientesLayout.createSequentialGroup()
                        .addGroup(pnlClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(37, 37, 37)
                        .addGroup(pnlClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(39, 39, 39)
                        .addGroup(pnlClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtApellidoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        pnlClientesLayout.setVerticalGroup(
            pnlClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlClientesLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(pnlClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlClientesLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(2, 2, 2)
                        .addGroup(pnlClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtApellidoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 0, 204));
        jLabel6.setText("Datos Ventas");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle Venta"));

        lblNombreVendedor.setText("jLabel7");

        jLabel7.setText("Código");

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 51, 255));
        jLabel9.setText("Vendedor");

        jLabel1.setText("Fecha");

        jLabel13.setText("N° Factura");

        jLabel14.setText("Total");

        btnRegistrar.setText("Registrar venta");
        btnRegistrar.addActionListener(this::btnRegistrarActionPerformed);

        tblDetalleVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblDetalleVenta);

        btnAgregarProducto.setText("Agregar producto");
        btnAgregarProducto.addActionListener(this::btnAgregarProductoActionPerformed);

        btnEliminarProducto.setText("Eliminar producto");
        btnEliminarProducto.addActionListener(this::btnEliminarProductoActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(txtCodigoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFechaFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(txtTotalFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblNombreVendedor)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCodigoVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel9)))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegistrar)
                    .addComponent(btnAgregarProducto)
                    .addComponent(btnEliminarProducto))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtCodigoVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotalFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNombreVendedor)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodigoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFechaFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAgregarProducto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEliminarProducto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRegistrar)
                        .addGap(47, 47, 47))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnLlamarProducto.setText("Ver Productos");
        btnLlamarProducto.addActionListener(this::btnLlamarProductoActionPerformed);

        txtIdProducto.addActionListener(this::txtIdProductoActionPerformed);

        txtPrecioProducto.addActionListener(this::txtPrecioProductoActionPerformed);

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 51, 255));
        jLabel8.setText("Datos de productos");

        jLabel10.setText("Código");

        jLabel11.setText("Descripción");

        jLabel12.setText("Precio");

        jLabel16.setText("cantidad");

        jLabel17.setText("importe");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel11)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(spnCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtImporteVenta))))
                    .addComponent(jLabel8))
                .addGap(47, 47, 47)
                .addComponent(btnLlamarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtImporteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnLlamarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(252, 252, 252)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(758, 758, 758))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodigoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoClienteActionPerformed

    private void btnClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientesActionPerformed
        // TODO add your handling code here:
        dlgCliente dialogoClientes = new dlgCliente(this, true);
        dialogoClientes.setVisible(true);

        // Después de cerrar, obtener el cliente seleccionado
        Cliente cliente = dialogoClientes.getClienteSeleccionado();
        if (cliente != null) {
            // Llenar los campos del formulario principal (frmVentas)
            txtCodigoCliente.setText(String.valueOf(cliente.getId()));
            txtNombreCliente.setText(cliente.getNombre());
            txtApellidoCliente.setText(cliente.getApellido());
        }
    }//GEN-LAST:event_btnClientesActionPerformed

    private void btnLlamarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLlamarProductoActionPerformed
        // TODO add your handling code here:
        dlgProducto dialogoProducto = new dlgProducto(this, true);
        dialogoProducto.setVisible(true);

        // Obtener el producto seleccionado (si se seleccionó uno)
        Producto prod = dialogoProducto.getProductoSeleccionado();
        if (prod != null) {
            // Cargar los datos del producto en los JTextField del JFrame
            txtIdProducto.setText(String.valueOf(prod.getId()));
            txtNombreProducto.setText(prod.getNombre());
            txtPrecioProducto.setText(String.valueOf(prod.getPrecio()));
            // Opcional: poner foco en el campo cantidad
            // txtCantidad.requestFocus();
        }
    }//GEN-LAST:event_btnLlamarProductoActionPerformed

    private void txtIdProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdProductoActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        // TODO add your handling code here:
        /*
        double precioVenta = Double.parseDouble(txtPrecioProducto.getText());
        int cantidadVenta = ((Number) spnCantidad.getValue()).intValue();
        double importe = precioVenta * cantidadVenta;

    // Muestra el importe en el campo (opcional)
        txtImporteVenta.setText(String.valueOf(importe));
         */

        // Validar cliente
        if (txtCodigoCliente.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente.");
            return;
        }
        // Validar que haya productos en la tabla
        if (modeloDetalle.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un producto a la venta.");
            return;
        }
        // Validar que el total no esté vacío
        if (txtTotalFactura.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El total no puede estar vacío.");
            return;
        }

        try (Connection conn = ConexionMySql.getConnection()) {
            conn.setAutoCommit(false);

            int numeroFactura = Integer.parseInt(txtCodigoFactura.getText());
            int idCliente = Integer.parseInt(txtCodigoCliente.getText());
            int idVendedor = vendedorActual.getId();
            double total = Double.parseDouble(txtTotalFactura.getText());

            // 1. Insertar cabecera de la factura
            String sqlCab = "INSERT INTO table_facturas (No_Facturas, cliente, fecha, vendedor, totals) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlCab)) {
                ps.setInt(1, numeroFactura);
                ps.setInt(2, idCliente);
                ps.setString(3, txtFechaFactura.getText());
                ps.setInt(4, idVendedor);
                ps.setDouble(5, total);
                ps.executeUpdate();
            }

            // 2. Insertar detalles (los que están en la tabla)
            String sqlDet = "INSERT INTO table_ventas (No_Facturas, Productos, cantidad, importe) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlDet)) {
                for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
                    int idProducto = (int) modeloDetalle.getValueAt(i, 0);
                    int cantidad = (int) modeloDetalle.getValueAt(i, 2);
                    double importe = (double) modeloDetalle.getValueAt(i, 4);
                    ps.setInt(1, numeroFactura);
                    ps.setInt(2, idProducto);
                    ps.setInt(3, cantidad);
                    ps.setDouble(4, importe);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Factura N° " + numeroFactura + " registrada exitosamente.");

            // Limpiar para nueva venta
            modeloDetalle.setRowCount(0);
            totalFactura = 0.0;
            txtTotalFactura.setText("0.00");
            txtCodigoCliente.setText("");
            txtNombreCliente.setText("");
            txtApellidoCliente.setText("");
            txtCodigoFactura.setText(String.valueOf(obtenerSiguienteNumeroFactura()));
            txtIdProducto.setText("");
            txtNombreProducto.setText("");
            txtPrecioProducto.setText("");
            spnCantidad.setValue(1);
            txtImporteVenta.setText("0.00");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
// Si después necesitas usar importe como double, usa la variable calculada, no la leas de txtImporte
// Por ejemplo, para guardar en la base de datos:
// ps.setDouble(5, importe);
        // Validaciones
        if (txtCodigoCliente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente.");
            return;
        }
        if (modeloDetalle.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un producto a la venta.");
            return;
        }

        try (Connection conn = ConexionMySql.getConnection()) {
            conn.setAutoCommit(false); // Iniciar transacción

            int numeroFactura = obtenerSiguienteNumeroFactura(); // ya lo usas en txtCodigoFactura
            int idCliente = Integer.parseInt(txtCodigoCliente.getText());
            int idVendedor = vendedorActual.getId();
            double total = Double.parseDouble(txtTotalFactura.getText());

            // 1. Insertar cabecera en table_facturas
            String sqlCab = "INSERT INTO table_facturas (No_Facturas, cliente, fecha, vendedor, totals) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psCab = conn.prepareStatement(sqlCab)) {
                psCab.setInt(1, numeroFactura);
                psCab.setInt(2, idCliente);
                psCab.setString(3, txtFechaFactura.getText());
                psCab.setInt(4, idVendedor);
                psCab.setDouble(5, total);
                psCab.executeUpdate();
            }

            // 2. Insertar detalles en table_ventas
            String sqlDet = "INSERT INTO table_ventas (No_Facturas, Productos, cantidad, importe) VALUES (?, ?, ?, ?)";
            try (PreparedStatement psDet = conn.prepareStatement(sqlDet)) {
                for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
                    int idProducto = (int) modeloDetalle.getValueAt(i, 0);
                    int cantidad = (int) modeloDetalle.getValueAt(i, 2);
                    double importe = (double) modeloDetalle.getValueAt(i, 4);
                    psDet.setInt(1, numeroFactura);
                    psDet.setInt(2, idProducto);
                    psDet.setInt(3, cantidad);
                    psDet.setDouble(4, importe);
                    psDet.addBatch();
                }
                psDet.executeBatch();
            }

            conn.commit(); // Confirmar transacción

            JOptionPane.showMessageDialog(this, "Factura N° " + numeroFactura + " registrada exitosamente.");

            // Limpiar para una nueva venta
            modeloDetalle.setRowCount(0);
            totalFactura = 0.0;
            txtTotalFactura.setText("0.00");
            // Limpiar cliente (opcional, o mantenerlo)
            txtCodigoCliente.setText("");
            txtNombreCliente.setText("");
            txtApellidoCliente.setText("");
            // Generar nuevo número de factura para la siguiente venta
            txtCodigoFactura.setText(String.valueOf(obtenerSiguienteNumeroFactura()));
            // Limpiar producto
            txtIdProducto.setText("");
            txtNombreProducto.setText("");
            txtPrecioProducto.setText("");
            spnCantidad.setValue(1);
            txtImporteVenta.setText("0.00");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar la venta: " + e.getMessage());
        }
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void txtPrecioProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioProductoActionPerformed

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        // TODO add your handling code here:
        if (txtIdProducto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }
        try {
            int id = Integer.parseInt(txtIdProducto.getText());
            String nombre = txtNombreProducto.getText();
            double precio = Double.parseDouble(txtPrecioProducto.getText());
            int cantidad = ((Number) spnCantidad.getValue()).intValue();
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida.");
                return;
            }
            double importe = precio * cantidad;

            modeloDetalle.addRow(new Object[]{id, nombre, cantidad, precio, importe});

            totalFactura += importe;
            txtTotalFactura.setText(String.format("%.2f", totalFactura));

            // Limpiar campos
            txtIdProducto.setText("");
            txtNombreProducto.setText("");
            txtPrecioProducto.setText("");
            txtImporteVenta.setText("");
            spnCantidad.setValue(1);
            // Opcional: mostrar el importe calculado en el JTextField de importe
            txtImporteVenta.setText(String.format("%.2f", importe));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en datos numéricos.");
        }
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed
        // TODO add your handling code here:
        int fila = tblDetalleVenta.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto a eliminar.");
            return;
        }
        double importe = (double) modeloDetalle.getValueAt(fila, 4);
        modeloDetalle.removeRow(fila);
        totalFactura -= importe;
        txtTotalFactura.setText(String.format("%.2f", totalFactura));
    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new frmVenta().setVisible(true));

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnClientes;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnLlamarProducto;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblNombreVendedor;
    private javax.swing.JPanel pnlClientes;
    private javax.swing.JSpinner spnCantidad;
    private javax.swing.JTable tblDetalleVenta;
    private javax.swing.JTextField txtApellidoCliente;
    private javax.swing.JTextField txtCodigoCliente;
    private javax.swing.JTextField txtCodigoFactura;
    private javax.swing.JTextField txtCodigoVendedor;
    private javax.swing.JTextField txtFechaFactura;
    private javax.swing.JTextField txtIdProducto;
    private javax.swing.JTextField txtImporteVenta;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtPrecioProducto;
    private javax.swing.JTextField txtTotalFactura;
    // End of variables declaration//GEN-END:variables
}
