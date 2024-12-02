package com.example.application.views.formEstudiante;
import java.time.LocalDate;

import com.example.application.controlador.EstudianteController;
import com.example.application.modelo.Estudiante2;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import java.io.InputStream;
import java.io.IOException;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;

@PageTitle("Educantrol - Estudiantes")
@Route(value = "estudiante-form", layout = MainLayout.class)
public class EstudianteFormView extends Composite<VerticalLayout> {
    private final EstudianteController controller;
    private final Upload fotoUpload;
    private final MemoryBuffer fotoBuffer;
    private byte[] fotoData;

    // Campos de la vista
    private final TextField nombreField = new TextField("Nombre");
    private final TextField apellidoField = new TextField("Apellido");
    private final NumberField edadField = new NumberField("Edad");
    private final TextField direccionField = new TextField("Dirección");
    private final TextField telefonoField = new TextField("Teléfono");
    private final TextField nivelAcademicoField = new TextField("Nivel Académico");
    private final TextField nombrePadreField = new TextField("Nombre del Padre");
    private final TextField carnetField = new TextField("Carnet");
    private final DatePicker fechaInscripcionField = new DatePicker("Fecha de Inscripción");

    // Grid principal de estudiantes
    private final Grid<Estudiante2> estudiantesGrid = new Grid<>(Estudiante2.class, false);

    // Nuevos botones para métodos personalizados
    private final Button btnFiltrarPorNivelAcademico = new Button("Filtrar por Nivel Académico");
    private final Button btnFiltrarPorNombrePadre = new Button("Filtrar por Nombre de Padre");
    private final Button btnFiltrarPorFechaInscripcion = new Button("Filtrar por Fecha de Inscripción");

    public EstudianteFormView(EstudianteController controller) {
        this.controller = controller;
        this.fotoBuffer = new MemoryBuffer();
        this.fotoUpload = new Upload(fotoBuffer);
        
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();
        
        configureFotoUpload();
        configureLayout(layoutColumn2, h3, formLayout2Col, layoutRow);
        configureFields();
        configureButtons(buttonPrimary, buttonSecondary);
        
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        
        formLayout2Col.add(nombreField);
        formLayout2Col.add(apellidoField);
        formLayout2Col.add(edadField);
        formLayout2Col.add(direccionField);
        formLayout2Col.add(telefonoField);
        formLayout2Col.add(nivelAcademicoField);
        formLayout2Col.add(nombrePadreField);
        formLayout2Col.add(carnetField);
        formLayout2Col.add(fechaInscripcionField);
        formLayout2Col.add(fotoUpload);
        
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);
        layoutRow.add(btnFiltrarPorNivelAcademico);
        layoutRow.add(btnFiltrarPorNombrePadre);
        layoutRow.add(btnFiltrarPorFechaInscripcion);
        
        getContent().add(layoutColumn2);
        
        createGrid();
        
        buttonPrimary.addClickListener(e -> saveEstudiante());
        btnFiltrarPorNivelAcademico.addClickListener(e -> filtrarPorNivelAcademico());
        btnFiltrarPorNombrePadre.addClickListener(e -> filtrarPorNombrePadre());
        btnFiltrarPorFechaInscripcion.addClickListener(e -> filtrarPorFechaInscripcion());
    }

    private void configureLayout(VerticalLayout layout, H3 title, FormLayout form, HorizontalLayout buttons) {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        
        layout.setWidth("100%");
        layout.setMaxWidth("800px");
        
        title.setText("Información del Estudiante");
        title.setWidth("100%");
        
        form.setWidth("100%");
        
        buttons.addClassName(Gap.MEDIUM);
        buttons.setWidth("100%");
    }

    private void configureFields() {
        nombreField.setLabel("Nombres");
        apellidoField.setLabel("Apellidos");
        edadField.setLabel("Edad");
        direccionField.setLabel("Dirección");
        telefonoField.setLabel("Teléfono");
        nivelAcademicoField.setLabel("Nivel Académico");
        nombrePadreField.setLabel("Nombre del Padre");
        carnetField.setLabel("Carnet");
        fechaInscripcionField.setLabel("Fecha de Inscripción");
    }

    private void configureButtons(Button save, Button cancel) {
        save.setText("Guardar");
        save.setWidth("min-content");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        cancel.setText("Cancelar");
        cancel.setWidth("min-content");
    }

        private void configureFotoUpload() {
        fotoUpload.setAcceptedFileTypes("image/png");
        fotoUpload.setDropLabel(new Span("Arrastra una foto PNG o haz clic para subir"));
        fotoUpload.setMaxFiles(1);
        fotoUpload.setMaxFileSize(5 * 1024 * 1024); // 5MB máximo
        
        fotoUpload.addSucceededListener(event -> {
            try {
                // Guardar los datos de la imagen
                InputStream is = fotoBuffer.getInputStream();
                fotoData = is.readAllBytes();
                Notification.show("Foto subida correctamente");
            } catch (IOException e) {
                Notification.show("Error al procesar la imagen: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        fotoUpload.addFailedListener(event -> {
            Notification.show("Error al subir la imagen: " + event.getReason(), 
                            3000, Notification.Position.MIDDLE);
        });
    }

    private void createGrid() {
        estudiantesGrid.addComponentColumn(estudiante -> {
            if (estudiante.getFoto() != null) {
                // Crear un StreamResource para la imagen
                StreamResource streamResource = new StreamResource("foto.png", () -> {
                    return new ByteArrayInputStream(estudiante.getFoto());
                });
                
                // Crear el componente Image
                Image foto = new Image(streamResource, "Foto del estudiante");
                foto.setWidth("50px");
                foto.setHeight("50px");
                foto.getStyle().set("object-fit", "cover"); 
                foto.getStyle().set("border", "1px solid #ccc"); 
                
                return foto;
            } else {
                // Si no hay foto, mostrar un placeholder
                Image placeholder = new Image("images/placeholder-user.png", "Sin foto");
                placeholder.setWidth("50px");
                placeholder.setHeight("50px");
                placeholder.getStyle().set("object-fit", "cover");
                placeholder.getStyle().set("border", "1px solid #ccc");
                return placeholder;
            }
        }).setHeader("Foto").setWidth("100px").setFlexGrow(0);
        
        estudiantesGrid.addColumn(Estudiante2::getNombre).setHeader("Nombres").setSortable(true);
        estudiantesGrid.addColumn(Estudiante2::getApellido).setHeader("Apellidos").setSortable(true);
        estudiantesGrid.addColumn(Estudiante2::getNivelAcademico).setHeader("Nivel Académico").setSortable(true);
        estudiantesGrid.addColumn(Estudiante2::getNombrePadre).setHeader("Nombre del Padre").setSortable(true);
        estudiantesGrid.addColumn(Estudiante2::getCarnet).setHeader("Carnet").setSortable(true);
        estudiantesGrid.addColumn(Estudiante2::getFechaInscripcion).setHeader("Fecha de Inscripción").setSortable(true);
        
        estudiantesGrid.addColumn(new ComponentRenderer<>(estudiante -> {
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            editButton.addClickListener(e -> openEditDialog(estudiante));

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteEstudiante(estudiante));

            return new HorizontalLayout(editButton, deleteButton);
        })).setHeader("Acciones");
        
        getContent().add(estudiantesGrid);
        refreshGrid();
    }

    private void saveEstudiante() {
        Estudiante2 estudianteToSave = new Estudiante2();
        estudianteToSave.setNombre(nombreField.getValue());
        estudianteToSave.setApellido(apellidoField.getValue());
        estudianteToSave.setEdad(edadField.getValue());
        estudianteToSave.setDireccion(direccionField.getValue());
        estudianteToSave.setTelefono(telefonoField.getValue());
        estudianteToSave.setNivelAcademico(nivelAcademicoField.getValue());
        estudianteToSave.setNombrePadre(nombrePadreField.getValue());
        estudianteToSave.setCarnet(carnetField.getValue());
        estudianteToSave.setFechaInscripcion(fechaInscripcionField.getValue());
        if (fotoData != null) {
            estudianteToSave.setFoto(fotoData);
        }

        controller.save(estudianteToSave);

        Notification.show("Estudiante guardado correctamente");
        resetFields();
        refreshGrid();
    }

    private void resetFields() {
        nombreField.clear();
        apellidoField.clear();
        edadField.clear();
        direccionField.clear();
        telefonoField.clear();
        nivelAcademicoField.clear();
        nombrePadreField.clear();
        carnetField.clear();
        fechaInscripcionField.clear();
        fotoData = null;
        fotoUpload.clearFileList();
    }

    private void refreshGrid() {
        estudiantesGrid.setItems(controller.findAll());
    }

    private void openEditDialog(Estudiante2 estudiante) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        if (estudiante.getFoto() != null) {
            StreamResource streamResource = new StreamResource("foto.png", () -> {
                return new ByteArrayInputStream(estudiante.getFoto());
            });
            Image fotoActual = new Image(streamResource, "Foto actual");
            fotoActual.setWidth("150px");
            fotoActual.setHeight("150px");
            fotoActual.getStyle().set("object-fit", "cover");
            fotoActual.getStyle().set("border", "1px solid #ccc");
            formLayout.add(fotoActual);
        }

        // Configurar el upload para la nueva foto
        MemoryBuffer buffer = new MemoryBuffer();
        Upload fotoUploadEdit = new Upload(buffer);
        fotoUploadEdit.setAcceptedFileTypes("image/png");
        fotoUploadEdit.setDropLabel(new Span("Actualizar foto (PNG)"));
        fotoUploadEdit.setMaxFiles(1);
        fotoUploadEdit.setMaxFileSize(5 * 1024 * 1024); // 5MB máximo

        fotoUploadEdit.addSucceededListener(event -> {
            try {
            InputStream is = buffer.getInputStream();
            estudiante.setFoto(is.readAllBytes());
            Notification.show("Foto actualizada");
            } catch (IOException e) {
            Notification.show("Error al procesar la imagen: " + e.getMessage());
            }
        });

        // Campos del formulario
        TextField nombreEdit = new TextField("Nombre");
        TextField apellidoEdit = new TextField("Apellido");
        NumberField edadEdit = new NumberField("Edad");
        TextField direccionEdit = new TextField("Dirección");
        TextField telefonoEdit = new TextField("Teléfono");
        TextField nivelAcademicoEdit = new TextField("Nivel Académico");
        TextField nombrePadreEdit = new TextField("Nombre del Padre");
        TextField carnetEdit = new TextField("Carnet");
        DatePicker fechaInscripcionEdit = new DatePicker("Fecha de Inscripción");

        // Establecer valores actuales
        nombreEdit.setValue(estudiante.getNombre());
        apellidoEdit.setValue(estudiante.getApellido());
        edadEdit.setValue(estudiante.getEdad());
        direccionEdit.setValue(estudiante.getDireccion());
        telefonoEdit.setValue(estudiante.getTelefono());
        nivelAcademicoEdit.setValue(estudiante.getNivelAcademico());
        nombrePadreEdit.setValue(estudiante.getNombrePadre());
        carnetEdit.setValue(estudiante.getCarnet());
        fechaInscripcionEdit.setValue(estudiante.getFechaInscripcion());

        formLayout.add(fotoUploadEdit, nombreEdit, apellidoEdit, edadEdit, direccionEdit, telefonoEdit,
                       nivelAcademicoEdit, nombrePadreEdit, carnetEdit, fechaInscripcionEdit);

        Button saveButton = new Button("Guardar", event -> {
            estudiante.setNombre(nombreEdit.getValue());
            estudiante.setApellido(apellidoEdit.getValue());
            estudiante.setEdad(edadEdit.getValue());
            estudiante.setDireccion(direccionEdit.getValue());
            estudiante.setTelefono(telefonoEdit.getValue());
            estudiante.setNivelAcademico(nivelAcademicoEdit.getValue());
            estudiante.setNombrePadre(nombrePadreEdit.getValue());
            estudiante.setCarnet(carnetEdit.getValue());
            estudiante.setFechaInscripcion(fechaInscripcionEdit.getValue());

            controller.save(estudiante);
            refreshGrid();
            dialog.close();
            Notification.show("Estudiante actualizado");
        });

        Button cancelButton = new Button("Cancelar", event -> dialog.close());

        dialog.add(formLayout);
        dialog.add(new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void deleteEstudiante(Estudiante2 estudiante) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.add(new Text("¿Estás seguro de que deseas eliminar este estudiante?"));

        Button confirmButton = new Button("Eliminar", event -> {
            controller.delete(estudiante);
            refreshGrid();
            Notification.show("Estudiante eliminado");
            confirmDialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button cancelButton = new Button("Cancelar", event -> confirmDialog.close());

        confirmDialog.add(new HorizontalLayout(confirmButton, cancelButton));
        confirmDialog.open();
    }

    private void filtrarPorNivelAcademico() {
        String nivelAcademico = nivelAcademicoField.getValue();
        estudiantesGrid.setItems(controller.findByNivelAcademico(nivelAcademico));
    }

    private void filtrarPorNombrePadre() {
        String nombrePadre = nombrePadreField.getValue();
        estudiantesGrid.setItems(controller.findByNombrePadre(nombrePadre));
    }

    private void filtrarPorFechaInscripcion() {
        // Obtener las fechas de inicio y fin desde los campos de fecha
        LocalDate fechaInicio = fechaInscripcionField.getValue();
        LocalDate fechaFin = fechaInscripcionField.getValue();
        estudiantesGrid.setItems(controller.findByFechaInscripcionBetween(fechaInicio, fechaFin));
    }
}