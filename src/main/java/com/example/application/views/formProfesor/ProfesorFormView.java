package com.example.application.views.formProfesor;

import com.example.application.controlador.ProfesorController;
import com.example.application.modelo.Profesor2;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.progressbar.ProgressBar;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;



import com.vaadin.flow.component.UI;

@PageTitle("Educantrol - Profesores")
@Route(value = "profesor-form", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class ProfesorFormView extends Composite<VerticalLayout> {
    
    private final ProfesorController controller;
    
    // Campos heredados de Persona
    private final TextField nombreField = new TextField("Nombre");
    private final TextField apellidoField = new TextField("Apellido");
    private final TextField telefonoField = new TextField("Teléfono");
    private final TextField direccionField = new TextField("Dirección");
    private final NumberField edadField = new NumberField("Edad");

    // Campos específicos de Profesor2
    private final TextField especialidadField = new TextField("Especialidad");

    // Grid principal de profesores
    private final Grid<Profesor2> profesoresGrid = new Grid<>(Profesor2.class, false);

    // Componentes para importación de Excel
    private final Upload subirArchivo;
    private final ProgressBar barraProgreso;
    private final MemoryBuffer buffer;
    private Thread hiloImportacion;
    private final AtomicBoolean importacionEnProceso = new AtomicBoolean(false);
    private final Button btnIniciarImportacion;
    private InputStream archivoSeleccionado;

    //Botones para métodos personalizados
    private final Button btnFiltrarPorEspecialidad = new Button("Filtrar por Especialidad");

    public ProfesorFormView(ProfesorController controller) {
        this.controller = controller;
        
        // Inicializar componentes
        this.buffer = new MemoryBuffer();
        this.subirArchivo = new Upload(buffer);
        this.barraProgreso = new ProgressBar();
        this.btnIniciarImportacion = new Button("Importar Archivo");

        btnIniciarImportacion.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnIniciarImportacion.setEnabled(false);

        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();
        
        configureLayout(layoutColumn2, h3, formLayout2Col, layoutRow);
        configureFields();
        configureButtons(buttonPrimary, buttonSecondary);
        
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        
        formLayout2Col.add(nombreField);
        formLayout2Col.add(apellidoField);
        formLayout2Col.add(edadField);
        formLayout2Col.add(telefonoField);
        formLayout2Col.add(direccionField);
        formLayout2Col.add(especialidadField);
        
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);
        layoutRow.add(btnFiltrarPorEspecialidad);

        // Componentes de importación
        HorizontalLayout importLayout = new HorizontalLayout();
        importLayout.setAlignItems(Alignment.BASELINE);
        importLayout.setWidthFull();
        importLayout.add(subirArchivo, btnIniciarImportacion);
        layoutColumn2.add(importLayout);
        layoutColumn2.add(barraProgreso);

        configureImportComponents();
        
        getContent().add(layoutColumn2);
        
        createGrid();
        
        buttonPrimary.addClickListener(e -> saveProfesor());
        btnFiltrarPorEspecialidad.addClickListener(e -> filtrarPorEspecialidad());
    }

    private void configureLayout(VerticalLayout layout, H3 title, FormLayout form, HorizontalLayout buttons) {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        
        layout.setWidth("100%");
        layout.setMaxWidth("800px");
        
        title.setText("Información del Profesor");
        title.setWidth("100%");
        
        form.setWidth("100%");
        
        buttons.addClassName(Gap.MEDIUM);
        buttons.setWidth("100%");
    }

    private void configureFields() {
        nombreField.setLabel("Nombres");
        apellidoField.setLabel("Apellidos");
        edadField.setLabel("Edad");
        telefonoField.setLabel("Teléfono");
        direccionField.setLabel("Dirección");
        especialidadField.setLabel("Especialidad");
    }

    private void configureButtons(Button save, Button cancel) {
        save.setText("Guardar");
        save.setWidth("min-content");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        cancel.setText("Cancelar");
        cancel.setWidth("min-content");
    }

    private void configureImportComponents() {
        barraProgreso.setWidth("100%");
        barraProgreso.setVisible(false);
        
        subirArchivo.setAcceptedFileTypes(".xlsx", ".xls");
        subirArchivo.setMaxFiles(1);
        
        subirArchivo.addSucceededListener(event -> {
            String fileName = event.getFileName();
            archivoSeleccionado = buffer.getInputStream();
            btnIniciarImportacion.setEnabled(true);
            Notification.show("Archivo seleccionado: " + fileName);
        });
        
        subirArchivo.addFailedListener(event -> {
            Notification.show("Error al subir el archivo: " + event.getReason());
            limpiarImportacion();
        });
    
        btnIniciarImportacion.addClickListener(event -> {
            if (archivoSeleccionado != null) {
                barraProgreso.setVisible(true);
                btnIniciarImportacion.setEnabled(false);
                subirArchivo.setVisible(false); // En lugar de setEnabled, usamos setVisible
                iniciarImportacionExcel(archivoSeleccionado);
            } else {
                Notification.show("Por favor, seleccione un archivo primero");
            }
        });
    }

    private void createGrid() {
        //profesoresGrid.addColumn(Profesor2::getId).setHeader("CÓDIGO").setSortable(true);
        profesoresGrid.addColumn(Profesor2::getNombre).setHeader("Nombre").setSortable(true);
        profesoresGrid.addColumn(Profesor2::getApellido).setHeader("Apellido").setSortable(true);
        profesoresGrid.addColumn(Profesor2::getEdad).setHeader("Edad").setSortable(true);
        profesoresGrid.addColumn(Profesor2::getTelefono).setHeader("Teléfono").setSortable(true);
        profesoresGrid.addColumn(Profesor2::getEspecialidad).setHeader("Especialidad").setSortable(true);
        
        profesoresGrid.addColumn(new ComponentRenderer<>(profesor -> {
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            editButton.addClickListener(e -> openEditDialog(profesor));

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteProfesor(profesor));

            return new HorizontalLayout(editButton, deleteButton);
        })).setHeader("Acciones");
        
        getContent().add(profesoresGrid);
        refreshGrid();
    }

    private void iniciarImportacionExcel(InputStream inputStream) {
        if (importacionEnProceso.get()) {
            Notification.show("Ya hay una importación en proceso");
            return;
        }
    
        importacionEnProceso.set(true);
        final UI ui = UI.getCurrent(); // Capturamos la UI actual
        
        hiloImportacion = new Thread(() -> {
            try {
                List<Profesor2> profesores = new ArrayList<>();
                @SuppressWarnings("resource")
                Workbook libroExcel = new XSSFWorkbook(inputStream);
                Sheet hoja = libroExcel.getSheetAt(0);
                
                int totalFilas = hoja.getPhysicalNumberOfRows() - 1;
                AtomicInteger filaActual = new AtomicInteger(0);
                
                for (Row fila : hoja) {
                    if (!importacionEnProceso.get()) {
                        if (ui != null) {
                            ui.access(() -> {
                                Notification.show("Importación cancelada");
                                limpiarImportacion();
                            });
                        }
                        return;
                    }
    
                    if (fila.getRowNum() == 0) continue;
                    
                    try {
                        Profesor2 profesor = new Profesor2();
                        
                        profesor.setNombre(obtenerValorCeldaComoTexto(fila.getCell(0)));
                        profesor.setApellido(obtenerValorCeldaComoTexto(fila.getCell(1)));
                        profesor.setEdad(obtenerValorCeldaComoNumero(fila.getCell(2)));
                        profesor.setTelefono(obtenerValorCeldaComoTexto(fila.getCell(3)));
                        profesor.setDireccion(obtenerValorCeldaComoTexto(fila.getCell(4)));
                        profesor.setEspecialidad(obtenerValorCeldaComoTexto(fila.getCell(5)));
                        
                        if (profesor.getNombre() == null || profesor.getNombre().trim().isEmpty() ||
                            profesor.getApellido() == null || profesor.getApellido().trim().isEmpty() ||
                            profesor.getEspecialidad() == null || profesor.getEspecialidad().trim().isEmpty()) {
                            throw new IllegalArgumentException("Los campos nombre, apellido y especialidad son obligatorios");
                        }
                        
                        profesores.add(profesor);
                        
                        final int currentRow = filaActual.incrementAndGet();
                        final double progreso = (double) currentRow / totalFilas;
                        
                        if (ui != null) {
                            ui.access(() -> {
                                actualizarProgreso(progreso);
                                if (currentRow % 10 == 0) {
                                    Notification.show("Procesando fila " + currentRow + " de " + totalFilas);
                                }
                            });
                        }
                        
                    } catch (Exception e) {
                        final int errorRow = filaActual.get() + 1;
                        if (ui != null) {
                            ui.access(() -> {
                                Notification.show("Error en la fila " + errorRow + ": " + e.getMessage(),
                                        3000, Notification.Position.MIDDLE);
                            });
                        }
                    }
                    
                    Thread.sleep(50);
                }
                
                libroExcel.close();
                inputStream.close();
                
                if (ui != null) {
                    ui.access(() -> {
                        try {
                            if (!profesores.isEmpty()) {
                                profesores.forEach(profesor -> {
                                    try {
                                        controller.save(profesor);
                                    } catch (Exception e) {
                                        Notification.show("Error al guardar profesor: " + e.getMessage());
                                    }
                                });
                                Notification.show("Importación completada: " + profesores.size() + " profesores importados");
                                refreshGrid();
                            } else {
                                Notification.show("No se encontraron datos válidos para importar");
                            }
                        } finally {
                            limpiarImportacion();
                        }
                    });
                }
                
            } catch (Exception e) {
                if (ui != null) {
                    ui.access(() -> {
                        Notification.show("Error en la importación: " + e.getMessage(), 
                                        3000, Notification.Position.MIDDLE);
                        limpiarImportacion();
                    });
                }
            }
        });
        
        hiloImportacion.setName("ImportacionExcel-" + System.currentTimeMillis());
        hiloImportacion.start();
    }


// En el método limpiarImportacion:
private void limpiarImportacion() {
    importacionEnProceso.set(false);
    barraProgreso.setVisible(false);
    barraProgreso.setValue(0);
    btnIniciarImportacion.setEnabled(false);
    subirArchivo.setVisible(true); 
    archivoSeleccionado = null;
    hiloImportacion = null;

}

    private void actualizarProgreso(double progreso) {
        barraProgreso.setValue(progreso);
    }

    private String obtenerValorCeldaComoTexto(Cell celda) {
        if (celda == null) return "";
        
        switch (celda.getCellType()) {
            case STRING:
                return celda.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) celda.getNumericCellValue()).trim();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    private double obtenerValorCeldaComoNumero(Cell celda) {
        if (celda == null) return 0.0;
        
        switch (celda.getCellType()) {
            case NUMERIC:
                return celda.getNumericCellValue();
            case STRING:
                try {
                    String valor = celda.getStringCellValue().trim();
                    return valor.isEmpty() ? 0.0 : Double.parseDouble(valor);
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            default:
                return 0.0;
        }
    }

    private void saveProfesor() {
        Profesor2 profesor = new Profesor2();
        profesor.setNombre(nombreField.getValue());
        profesor.setApellido(apellidoField.getValue());
        profesor.setEdad(edadField.getValue());
        profesor.setDireccion(direccionField.getValue());
        profesor.setTelefono(telefonoField.getValue());
        profesor.setEspecialidad(especialidadField.getValue());
        
        controller.save(profesor);
        
        Notification.show("Profesor guardado correctamente");
        resetFields();
        refreshGrid();
    }

    private void resetFields() {
        nombreField.clear();
        apellidoField.clear();
        edadField.clear();
        telefonoField.clear();
        direccionField.clear();
        especialidadField.clear();
    }

    private void refreshGrid() {
        profesoresGrid.setItems(controller.findAll());
    }

    private void openEditDialog(Profesor2 profesor) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        // Campos del formulario
        TextField nombreEdit = new TextField("Nombre");
        TextField apellidoEdit = new TextField("Apellido");
        TextField telefonoEdit = new TextField("Teléfono");
        TextField direccionEdit = new TextField("Dirección");
        NumberField edadEdit = new NumberField("Edad");
        TextField especialidadEdit = new TextField("Especialidad");

        // Establecer valores actuales
        nombreEdit.setValue(profesor.getNombre());
        apellidoEdit.setValue(profesor.getApellido());
        telefonoEdit.setValue(profesor.getTelefono());
        direccionEdit.setValue(profesor.getDireccion());
        edadEdit.setValue(profesor.getEdad());
        especialidadEdit.setValue(profesor.getEspecialidad());

        formLayout.add(nombreEdit, apellidoEdit, telefonoEdit, direccionEdit, edadEdit, especialidadEdit);

        Button saveButton = new Button("Guardar", event -> {
            profesor.setNombre(nombreEdit.getValue());
            profesor.setApellido(apellidoEdit.getValue());
            profesor.setEdad(edadEdit.getValue());
            profesor.setDireccion(direccionEdit.getValue());
            profesor.setTelefono(telefonoEdit.getValue());
            profesor.setEspecialidad(especialidadEdit.getValue());

            controller.save(profesor);
            refreshGrid();
            dialog.close();
            Notification.show("Profesor actualizado");
        });

        Button cancelButton = new Button("Cancelar", event -> dialog.close());

        dialog.add(formLayout);
        dialog.add(new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void deleteProfesor(Profesor2 profesor) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.add(new Text("¿Estás seguro de que deseas eliminar este profesor?"));

        Button confirmButton = new Button("Eliminar", event -> {
            controller.delete(profesor);
            refreshGrid();
            Notification.show("Profesor eliminado");
            confirmDialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button cancelButton = new Button("Cancelar", event -> confirmDialog.close());

        confirmDialog.add(new HorizontalLayout(confirmButton, cancelButton));
        confirmDialog.open();
    }

    private void filtrarPorEspecialidad() {
        String especialidad = especialidadField.getValue();
        profesoresGrid.setItems(controller.findByEspecialidad(especialidad));
    }

}
